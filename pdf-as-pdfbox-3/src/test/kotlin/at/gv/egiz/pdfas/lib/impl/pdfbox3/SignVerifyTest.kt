package at.gv.egiz.pdfas.lib.impl.pdfbox3

import at.gv.egiz.pdfas.lib.api.ByteArrayDataSource
import at.gv.egiz.pdfas.lib.api.IConfigurationConstants
import at.gv.egiz.pdfas.lib.api.PdfAs
import at.gv.egiz.pdfas.lib.api.PdfAsFactory
import at.gv.egiz.pdfas.lib.api.sign.IPlainSigner
import at.gv.egiz.pdfas.lib.api.sign.SignParameter
import at.gv.egiz.pdfas.lib.api.verify.VerifyParameter
import at.gv.egiz.pdfas.lib.impl.verify.pdfbox3.PDFBOXVerifier
import at.gv.egiz.pdfas.sigs.pades.PAdESSignerKeystore
import jakarta.activation.DataSource
import org.apache.pdfbox.Loader
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

@RunWith(BlockJUnit4ClassRunner::class)
class SignVerifyTest {
    companion object {
        @JvmField
        @field:ClassRule
        public val tempFolder = TemporaryFolder()

        lateinit var pdfAs: PdfAs
        fun captureSign(param: SignParameter): ByteArray =
            ByteArrayOutputStream().use {
                param.outputStream = it
                pdfAs.sign(param)
                it.toByteArray()
            }

        fun captureSign(pdf: DataSource, signer: IPlainSigner, config: SignParameter.()->Unit = {}) =
            captureSign(
                PdfAsFactory.createSignParameter(pdfAs.configuration, pdf, null)
                    .apply { plainSigner = signer }
                    .apply(config)
            )

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

        val getInputPdf = object : Function1<String, ByteArrayDataSource> {
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
        }

        val getKeystoreSigner = object : Function1<String, PAdESSignerKeystore> {
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
    }

    @Test
    fun signVerify() {
        val signedPdf = captureSign(getInputPdf("align.pdf"), getKeystoreSigner("test-key"))
        val verificationResult =
            PdfAsFactory.createVerifyParameter(
                pdfAs.configuration,
                ByteArrayDataSource(signedPdf)
            )
                .apply {
                    signatureVerificationLevel = VerifyParameter.SignatureVerificationLevel.INTEGRITY_ONLY_VERIFICATION
                }
                .let(PDFBOXVerifier::verify)
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
                val acroForm = requireNotNull(document.documentCatalog.acroForm)
                require(acroForm.fields.none { it is PDSignatureField })
                acroForm.fields.size.also { require (it>0) }
            }
        }

        val signedPdf = captureSign(input, getKeystoreSigner("test-key"))

        Loader.loadPDF(signedPdf).use { document ->
            val acroForm = requireNotNull(document.documentCatalog.acroForm)
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
            doc.documentCatalog.acroForm.fieldTree
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
        val parameter = PdfAsFactory.createVerifyParameter(
            pdfAs.configuration,
            getInputPdf("existing-acroform.pdf")
        ).apply {
            whichSignature = -2
        }

        Assert.assertTrue(PDFBOXVerifier.verify(parameter).isEmpty())
    }

    /** the provided pdf has the signature field as a direct field, rather than the more common indirect fields */
    @Test
    fun directSignatureField() {
        val parameter = PdfAsFactory.createVerifyParameter(
            pdfAs.configuration,
            getInputPdf("align-signed-direct.pdf")
        ).apply {
            signatureVerificationLevel =
                VerifyParameter.SignatureVerificationLevel.INTEGRITY_ONLY_VERIFICATION
        }

        Assert.assertEquals(1, PDFBOXVerifier.verify(parameter).size)
    }

    @Test
    fun existingEmptySignatureField() {
        val fieldName = "ownerSignature"
        val output = captureSign(getInputPdf("acroform-placeholder.pdf"), getKeystoreSigner("test-key")) {
            configuration.setValue(IConfigurationConstants.SIGNATURE_FIELD_NAME, fieldName)
        }
        Loader.loadPDF(output).use { doc ->
            val field = doc.documentCatalog.acroForm
                .getField(fieldName) as PDSignatureField

            Assert.assertNotNull(field.signature)
        }
    }
}