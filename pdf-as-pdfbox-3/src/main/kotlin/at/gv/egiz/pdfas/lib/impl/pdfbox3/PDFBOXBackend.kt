package at.gv.egiz.pdfas.lib.impl.pdfbox3

import at.gv.egiz.pdfas.lib.backend.PDFASBackend
import at.gv.egiz.pdfas.lib.impl.signing.pdfbox3.PDFBOXSigner
import at.gv.egiz.pdfas.lib.impl.verify.pdfbox3.PDFBOXVerifier
import org.slf4j.LoggerFactory

class PDFBOXBackend : PDFASBackend {
    companion object {
        const val NAME = "PDFBOX_3_BACKEND"
        private val logger = LoggerFactory.getLogger(PDFBOXBackend::class.java)
        init {
            logger.info(" +++++++++++++++++++++++++++++++++++++++++++++++++++++")
            logger.info(" + PDFBOX 3 Backend is ready to go")
            logger.info(" + Using PDFBOX version {}", org.apache.pdfbox.util.Version.getVersion())
            logger.info(" +++++++++++++++++++++++++++++++++++++++++++++++++++++")
        }
    }

    override fun getName() = NAME
    override fun usedAsDefault() = true
    override fun getPdfSigner() = PDFBOXSigner
    override fun getPlaceholderExtractor() = PDFBoxPlaceholderExtractor
    override fun getVerifier() = PDFBOXVerifier
}