package at.gv.egiz.pdfas.lib.impl.pdfbox3

import at.gv.egiz.pdfas.common.exceptions.ErrorConstants
import at.gv.egiz.pdfas.common.exceptions.PDFASError
import at.gv.egiz.pdfas.common.exceptions.PdfAsException
import at.gv.egiz.pdfas.common.messages.MessageResolver
import at.gv.egiz.pdfas.lib.api.ByteArrayDataSource
import at.gv.egiz.pdfas.lib.api.Configuration
import at.gv.egiz.pdfas.lib.api.IConfigurationConstants
import at.gv.egiz.pdfas.lib.api.ISuspendingSigner
import at.gv.egiz.pdfas.lib.api.PdfAs
import at.gv.egiz.pdfas.lib.api.PdfAsFactory
import at.gv.egiz.pdfas.lib.api.sign.IAsyncSigner
import at.gv.egiz.pdfas.lib.api.sign.IPlainSigner
import at.gv.egiz.pdfas.lib.api.sign.SignParameter
import at.gv.egiz.pdfas.lib.api.signSuspend
import at.gv.egiz.pdfas.lib.api.verify.VerifyParameter
import at.gv.egiz.pdfas.lib.api.verify.VerifyResult
import at.gv.egiz.pdfas.lib.impl.status.RequestedSignature
import at.gv.egiz.pdfas.sigs.pades.PAdESSignerKeystore
import jakarta.activation.DataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.runBlocking
import org.apache.pdfbox.Loader
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.encryption.AccessPermission
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import org.zeroturnaround.zip.ZipUtil
import java.io.ByteArrayOutputStream
import java.security.KeyStore
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.CancellationException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException

@RunWith(BlockJUnit4ClassRunner::class)
class SignVerifyTest {
    companion object {
        @JvmField
        @field:ClassRule
        public val tempFolder = TemporaryFolder()

        lateinit var pdfAs: PdfAs
        fun captureSign(param: SignParameter, document: DataSource, signer: IPlainSigner): ByteArray =
            ByteArrayOutputStream().use {
                pdfAs.sign(param, document, signer, it)
                it.toByteArray()
            }

        suspend fun captureSignSuspend(param: SignParameter, document: DataSource, signer: IAsyncSigner): ByteArray =
            ByteArrayOutputStream().use {
                pdfAs.signSuspend(param, document, signer, it)
                it.toByteArray()
            }

        fun captureSign(pdf: DataSource, signer: IPlainSigner, config: SignParameter.()->Unit = {}) =
            PdfAsFactory.createSignParameter(pdfAs.configuration)
                .apply(config)
                .let { captureSign(it, pdf, signer) }

        suspend fun captureSignSuspend(pdf: DataSource, signer: IAsyncSigner, config: SignParameter.()->Unit = {}) =
            PdfAsFactory.createSignParameter(pdfAs.configuration)
                .apply(config)
                .let { captureSignSuspend(it, pdf, signer) }

        fun doVerify(pdf: DataSource, config: VerifyParameter.()->Unit = {}) : List<VerifyResult> =
            PdfAsFactory.createVerifyParameter(pdfAs.configuration).apply {
                signatureVerificationLevel = VerifyParameter.SignatureVerificationLevel.INTEGRITY_ONLY_VERIFICATION
            }.apply(config).let { pdfAs.verify(it, pdf) }

        @JvmStatic
        @BeforeClass
        fun setUp() {
            // unzip default config to temp dir
            val configDir = tempFolder.newFolder()
            ZipUtil.unpack(
                PdfAs::class.java.getResourceAsStream("/config/config.zip"),
                configDir
            )
            pdfAs = PdfAsFactory.createPdfAs(configDir)
        }
    }

    private val getInputPdf = object : Function1<String, ByteArrayDataSource>, Function2<String, PDDocument.()->Unit, ByteArrayDataSource> {
        private val _map = mutableMapOf<String, ByteArrayDataSource>()
        private fun normalize(key: String) = when {
            key.endsWith(".pdf") -> key
            else -> "$key.pdf"
        }

        override operator fun invoke(key: String) = normalize(key).let { pdfName ->
            _map.getOrPut(pdfName) {
                SignVerifyTest::class.java.getResourceAsStream("/data/$pdfName").use {
                    ByteArrayDataSource(it!!.readAllBytes())
                }
            }
        }

        override operator fun invoke(key: String, verifier: PDDocument.()->Unit) =
            invoke(key).also {
                it.inputStream.use { input -> Loader.loadPDF(input.readAllBytes()).use(verifier) }
            }
    }

    private val getKeystoreSigner = object : Function1<String, PAdESSignerKeystore> {
        private val _keyStore = KeyStore.getInstance("PKCS12").apply {
            SignVerifyTest::class.java.getResourceAsStream("/test.p12").use {
                load(it, "password".toCharArray())
            }
        }
        private val _map = mutableMapOf<String, PAdESSignerKeystore>()
        override operator fun invoke(alias: String) = _map.getOrPut(alias) {
            PAdESSignerKeystore(_keyStore, alias, "password")
        }
    }
    private suspend fun IPlainSigner.wrapSuspend(): ISuspendingSigner {
        val coroutineScope = CoroutineScope(currentCoroutineContext())
        return object : ISuspendingSigner {
            override val coroutineScope get() = coroutineScope
            override suspend fun getCertificateDataSuspend(parameter: SignParameter) =
                IAsyncSigner.CertificateData(this@wrapSuspend.getCertificate(parameter), this@wrapSuspend.pdfFilter, this@wrapSuspend.pdfSubFilter)
            override suspend fun signSuspend(input: ByteArray, byteRange: IntArray, parameter: SignParameter, requestedSignature: RequestedSignature) =
                this@wrapSuspend.sign(input, byteRange, parameter, requestedSignature)
        }
    }

    @Test
    fun keystoreErrorsKeepTheirSpecificCodes() {
        val keyStore = KeyStore.getInstance("PKCS12").apply {
            SignVerifyTest::class.java.getResourceAsStream("/test.p12").use {
                load(it, "password".toCharArray())
            }
        }
        Assert.assertEquals(ErrorConstants.ERROR_SIG_KEY_PASSWORD_MISSING,
            Assert.assertThrows(PDFASError::class.java) { PAdESSignerKeystore(keyStore, "test-key", null) }.code)
        Assert.assertEquals(ErrorConstants.ERROR_SIG_PRIVATE_KEY_MISSING,
            Assert.assertThrows(PDFASError::class.java) { PAdESSignerKeystore(keyStore, "missing", "password") }.code)
        val file = tempFolder.newFile().apply {
            outputStream().use { keyStore.store(it, "password".toCharArray()) }
        }
        Assert.assertEquals(ErrorConstants.ERROR_SIG_KEYSTORE_PASSWORD_MISSING,
            Assert.assertThrows(PDFASError::class.java) { PAdESSignerKeystore(file.absolutePath, "test-key", null, "password", "PKCS12") }.code)
    }

    @Test
    fun missingProfileUsesDedicatedCode() {
        val configuration = object : Configuration by pdfAs.configuration {
            override fun hasValue(key: String) =
                key != IConfigurationConstants.DEFAULT_SIGNATURE_PROFILE && pdfAs.configuration.hasValue(key)
            override fun getValue(key: String): String? =
                if (key == IConfigurationConstants.DEFAULT_SIGNATURE_PROFILE) null else pdfAs.configuration.getValue(key)
        }
        val parameter = PdfAsFactory.createSignParameter(configuration)
        val error = Assert.assertThrows(PDFASError::class.java) {
            captureSign(parameter, getInputPdf("align.pdf"), getKeystoreSigner("test-key"))
        }
        Assert.assertEquals(ErrorConstants.ERROR_SIG_NO_PROFILE, error.code)
    }

    @Test
    fun protectedPdfUsesDedicatedCode() {
        val encrypted = ByteArrayOutputStream()
        PDDocument().use { document ->
            document.addPage(PDPage())
            val permissions = AccessPermission().apply {
                setCanModify(false)
                setCanModifyAnnotations(false)
                setCanFillInForm(false)
            }
            document.protect(StandardProtectionPolicy("owner", "", permissions))
            document.save(encrypted)
        }
        val error = Assert.assertThrows(PDFASError::class.java) {
            captureSign(ByteArrayDataSource(encrypted.toByteArray()), getKeystoreSigner("test-key"))
        }
        Assert.assertEquals(ErrorConstants.ERROR_SIG_DOCUMENT_PROTECTED, error.code)
    }

    @Test
    fun signingErrorsAreNormalized() = runBlocking {
        val failures = listOf(
            PDFASError(ErrorConstants.ERROR_SIG_INVALID_BKU_SIG, "specific failure", PDFASError(ErrorConstants.ERROR_GENERIC)) to ErrorConstants.ERROR_SIG_INVALID_BKU_SIG,
            PdfAsException("error.pdf.sig.02") to ErrorConstants.ERROR_PDF_PROCESSING_FAILED,
            CertificateException("invalid certificate") to ErrorConstants.ERROR_INVALID_CERTIFICATE,
            IllegalStateException("unexpected failure") to ErrorConstants.ERROR_GENERIC,
            AssertionError("fatal failure") to null,
            CancellationException("cancelled") to null
        )
        for ((failure, expectedCode) in failures) {
            fun checkFailure(actual: Throwable?) {
                if (expectedCode == null) {
                    Assert.assertTrue("Expected ${failure.javaClass.name}, got $actual", failure.javaClass.isInstance(actual))
                    return
                }
                Assert.assertTrue("Expected PDFASError, got $actual", actual is PDFASError)
                actual as PDFASError
                Assert.assertEquals(expectedCode, actual.code)
                if (failure is PDFASError) Assert.assertEquals(failure.message, actual.message)
            }

            // certificate failure during sync signing
            val signer = object : IPlainSigner by getKeystoreSigner("test-key") {
                override fun getCertificate(parameter: SignParameter): X509Certificate = throw failure
            }
            checkFailure(runCatching { captureSign(getInputPdf("align.pdf"), signer) }.exceptionOrNull())

            // certificate failure during async signing
            val certificate = CompletableFuture<IAsyncSigner.CertificateData>()
            val asyncSigner = object : IAsyncSigner {
                override fun getCertificateData(parameter: SignParameter) = certificate
                override fun sign(input: ByteArray, byteRange: IntArray, parameter: SignParameter, requestedSignature: RequestedSignature): CompletableFuture<ByteArray> =
                    error("Certificate lookup should fail")
            }
            val result = pdfAs.signAsync(PdfAsFactory.createSignParameter(pdfAs.configuration),
                getInputPdf("align.pdf"), asyncSigner, ByteArrayOutputStream()).toCompletableFuture()
            Assert.assertFalse(result.isDone)
            certificate.completeExceptionally(failure)
            checkFailure(Assert.assertThrows(CompletionException::class.java) { result.join() }.cause)

            // certificate failure during suspend signing
            checkFailure(runCatching {
                pdfAs.signSuspend(PdfAsFactory.createSignParameter(pdfAs.configuration),
                    getInputPdf("align.pdf"), signer.wrapSuspend(), ByteArrayOutputStream())
            }.exceptionOrNull())
        }
    }

    @Test
    fun signVerify() {
        val signedPdf = captureSign(getInputPdf("align.pdf"), getKeystoreSigner("test-key"))
        val verificationResult = doVerify(ByteArrayDataSource(signedPdf))
        Assert.assertEquals(1, verificationResult.size)
        verificationResult[0].let {
            Assert.assertTrue(it.isVerificationDone)
            Assert.assertEquals(getKeystoreSigner("test-key").getCertificate(null), it.signerCertificate)
        }
    }

    @Test
    fun signFlatTreePdfWithoutParentTreeNextKey() {
        val input = getInputPdf("platzhalter_en_de_test.pdf") {
            Assert.assertFalse(documentCatalog.structureTreeRoot.cosObject.containsKey(COSName.PARENT_TREE_NEXT_KEY))
        }
        val output = captureSign(input, getKeystoreSigner("test-key"))
        Loader.loadPDF(output).use { document ->
            Assert.assertEquals(1, document.signatureFields.single().widgets.single().structParent)
            Assert.assertEquals(2, document.documentCatalog.structureTreeRoot.parentTreeNextKey)
        }
        Assert.assertTrue(doVerify(ByteArrayDataSource(output)).single().isVerificationDone)
    }

    @Test
    fun signNestedTreePdfWithoutParentTreeNextKey() {
        val input = getInputPdf("align-parent-tree-no-next-key-nested.pdf") {
            Assert.assertFalse(documentCatalog.structureTreeRoot.cosObject.containsKey(COSName.PARENT_TREE_NEXT_KEY))
        }
        val output = captureSign(input, getKeystoreSigner("test-key"))
        Loader.loadPDF(output).use { document ->
            Assert.assertEquals(1, document.signatureFields.single().widgets.single().structParent)
            Assert.assertEquals(2, document.documentCatalog.structureTreeRoot.parentTreeNextKey)
        }
        Assert.assertTrue(doVerify(ByteArrayDataSource(output)).single().isVerificationDone)
    }

    @Test
    fun signWithPlaceholderSearchAndJpeg2000Image() {
        val output = captureSign(getInputPdf("jpeg2000.pdf"), getKeystoreSigner("test-key")) {
            isPlaceHolderSearchEnabled = true
            configuration.setValue(IConfigurationConstants.PLACEHOLDER_SEARCH_ENABLED, "true")
        }
        Assert.assertTrue(doVerify(ByteArrayDataSource(output)).single().isVerificationDone)
    }

    @Test
    fun signWithPlaceholderSearchAndTextLineOperator() {
        // The sample PDF uses the ' text operator.
        val signedPdf = captureSign(getInputPdf("eupl-1.1.pdf"), getKeystoreSigner("test-key")) {
            isPlaceHolderSearchEnabled = true
            configuration.setValue(IConfigurationConstants.PLACEHOLDER_SEARCH_ENABLED, "true")
        }
        val verificationResult = doVerify(ByteArrayDataSource(signedPdf))
        Assert.assertEquals(1, verificationResult.size)
        verificationResult[0].let {
            Assert.assertTrue(it.isVerificationDone)
            Assert.assertEquals(getKeystoreSigner("test-key").getCertificate(null), it.signerCertificate)
        }
    }

    @Test
    fun signSuspend() {
        val signedPdf = runBlocking {
            captureSignSuspend(getInputPdf("align.pdf"), getKeystoreSigner("test-key").wrapSuspend())
        }
        val verificationResult = doVerify(ByteArrayDataSource(signedPdf))
        Assert.assertEquals(1, verificationResult.size)
        verificationResult[0].let {
            Assert.assertTrue(it.isVerificationDone)
            Assert.assertEquals(getKeystoreSigner("test-key").getCertificate(null), it.signerCertificate)
        }
    }

    @Test
    fun existingAcroForm() {
        val input = getInputPdf("existing-acroform.pdf")

        val originalFieldCount = input.inputStream.use { stream ->
            Loader.loadPDF(stream.readAllBytes()).use { document ->
                val acroForm = requireNotNull(document.documentCatalog.getAcroForm(null))
                require(acroForm.fields.none { it is PDSignatureField })
                acroForm.fields.size.also { require (it>0) }
            }
        }

        val signedPdf = captureSign(input, getKeystoreSigner("test-key"))

        Loader.loadPDF(signedPdf).use { document ->
            val acroForm = requireNotNull(document.documentCatalog.getAcroForm(null))
            val signatureFields = acroForm.fields.filterIsInstance<PDSignatureField>()

            Assert.assertEquals("Wrong field count",
                originalFieldCount + 1, acroForm.fields.size)
            Assert.assertEquals("Signature field missing",
                1, signatureFields.size
            )

            val signatureField = signatureFields.single()

            Assert.assertNotNull("Signature value missing", signatureField.signature)

            val annotations = document.pages.flatMap { it.annotations }

            Assert.assertTrue(
                "Signature widget missing",
                signatureField.widgets.any { widget ->
                    annotations.any { annotation ->
                        annotation.cosObject === widget.cosObject
                    }
                }
            )
        }
    }

    @Test
    fun signTwiceYieldsUniqueSignatureFieldNames() {
        val signedOnce = captureSign(getInputPdf("align.pdf"), getKeystoreSigner("test-key"))
        val signedTwice = captureSign(ByteArrayDataSource(signedOnce), getKeystoreSigner("test-key"))

        val fieldNames = Loader.loadPDF(signedTwice).use { doc ->
            doc.documentCatalog.getAcroForm(null).fieldTree
                .filterIsInstance<PDSignatureField>()
                .map { it.fullyQualifiedName }
        }

        Assert.assertEquals("expected two signature fields", 2, fieldNames.size)
        Assert.assertEquals(
            "signature field names must be unique, but were $fieldNames",
            fieldNames.size, fieldNames.toSet().size
        )
    }

    /** the provided pdf has acroform fields, but none are signatures; trying to select the "last" of these should not throw */
    @Test
    fun lastSignatureOnUnsignedAcroForm() {
        val result = doVerify(getInputPdf("existing-acroform.pdf")) {
            whichSignature = -2
        }
        Assert.assertTrue(result.isEmpty())
    }

    /** the provided pdf has the signature field as a direct field, rather than the more common indirect fields */
    @Test
    fun directSignatureField() {
        val result = doVerify((getInputPdf("align-signed-direct.pdf")))
        Assert.assertEquals(1, result.size)
    }

    @Test
    fun signPdfUaFieldWithoutAlternateName() {
        val fieldName = "Signature_0"
        val input = SignVerifyTest::class.java.getResourceAsStream("/1Sign_manyQR.pdf").use {
            ByteArrayDataSource(it!!.readAllBytes())
        }
        val output = captureSign(input, getKeystoreSigner("test-key")) {
            signatureProfileId = "SIGNATURBLOCK_DE"
            configuration.setValue("sig_obj.SIGNATURBLOCK_DE.key.SIG_PDFUA_FORCE", "true")
            configuration.setValue(IConfigurationConstants.SIGNATURE_FIELD_NAME, fieldName)
        }
        Loader.loadPDF(output).use { document ->
            Assert.assertEquals(fieldName, document.signatureFields.single().alternateFieldName)
        }
        Assert.assertTrue(doVerify(ByteArrayDataSource(output)).single().isVerificationDone)
    }

    @Test
    fun existingEmptySignatureField() {
        val fieldName = "ownerSignature"
        val output = captureSign(getInputPdf("acroform-placeholder.pdf"), getKeystoreSigner("test-key")) {
            configuration.setValue(IConfigurationConstants.SIGNATURE_FIELD_NAME, fieldName)
        }
        Loader.loadPDF(output).use { doc ->
            val field = doc.documentCatalog.getAcroForm(null)
                .getField(fieldName) as PDSignatureField

            Assert.assertNotNull(field.signature)
        }
    }

    @Test
    fun existingSignatureWithoutAcroFormFixup() {
        val output = captureSign(getInputPdf("signed-without-fixup.pdf"), getKeystoreSigner("test-key"))
        Loader.loadPDF(output).use { doc ->
            val acroForm = requireNotNull(doc.documentCatalog.getAcroForm(null))
            Assert.assertFalse(acroForm.cosObject.containsKey(COSName.DA))
            Assert.assertNotEquals(true, acroForm.defaultResources?.fontNames?.any())
        }
    }

    @Test
    fun rejectIncompleteSignatureDictionaries() {
        for (pdfName in listOf("align-missing-signature-byterange.pdf", "align-missing-signature-contents.pdf")) {
            val output = captureSign(getInputPdf(pdfName), getKeystoreSigner("test-key")) {
                signatureProfileId = "INVISIBLE"
            }
            Assert.assertTrue(doVerify(ByteArrayDataSource(output)) {
                whichSignature = 1
            }.single().isVerificationDone)

            val error = Assert.assertThrows(PDFASError::class.java) {
                doVerify(ByteArrayDataSource(output))
            }
            Assert.assertEquals(PDFASError.ERROR_PDF_PROCESSING_FAILED, error.code)
            Assert.assertEquals(MessageResolver.resolveMessage("error.pdf.verify.01"), error.info)
        }
    }

    @Test
    fun unsupportedSignatureFilter() {
        val result = doVerify(getInputPdf("unsupported-filter.pdf"))
        Assert.assertTrue(result.isEmpty())
    }
}
