package at.gv.egiz.pdfas.lib.impl.verify.pdfbox3

import at.gv.egiz.pdfas.lib.api.ByteArrayDataSource
import at.gv.egiz.pdfas.lib.api.PdfAs
import at.gv.egiz.pdfas.lib.api.PdfAsFactory
import at.gv.egiz.pdfas.lib.api.sign.SignParameter
import at.gv.egiz.pdfas.lib.api.verify.VerifyParameter
import at.gv.egiz.pdfas.sigs.pades.PAdESSignerKeystore
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
class PDFBox2To3Test {

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
                    PDFBox2To3Test::class.java.getResourceAsStream("/data/$pdfName").use {
                        ByteArrayDataSource(it!!.readAllBytes())
                    }
                }
            }
        }

        val getKeystoreSigner = object : Function1<String, PAdESSignerKeystore> {
            private val _keyStore = KeyStore.getInstance("PKCS12").apply {
                PDFBox2To3Test::class.java.getResourceAsStream("/test.p12").use {
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
    fun pdfBox2To3Test() {
        val verificationResult =
            PdfAsFactory.createVerifyParameter(
                pdfAs.configuration,
                getInputPdf("align_signed.pdf"))
            .apply {
                signatureVerificationLevel = VerifyParameter.SignatureVerificationLevel.INTEGRITY_ONLY_VERIFICATION
            }
            .let(PDFBOXVerifier::verify)
        Assert.assertEquals(verificationResult.size, 1)
        verificationResult[0].let {
            Assert.assertTrue(it.isVerificationDone)
            Assert.assertEquals(it.signerCertificate, getKeystoreSigner("test-key").getCertificate(null))
        }
    }
}