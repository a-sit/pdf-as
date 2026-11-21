package at.gv.egiz.pdfas.lib.impl.verify.pdfbox3

import at.gv.egiz.pdfas.common.exceptions.PdfAsException
import at.gv.egiz.pdfas.common.settings.ISettings
import at.gv.egiz.pdfas.lib.api.verify.VerifyParameter
import at.gv.egiz.pdfas.lib.api.verify.VerifyResult
import at.gv.egiz.pdfas.lib.impl.verify.SignatureInputData
import at.gv.egiz.pdfas.lib.impl.verify.VerifierDispatcher
import at.gv.egiz.pdfas.lib.impl.verify.VerifyBackend
import jakarta.activation.DataSource
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature

object PDFBOXVerifier : VerifyBackend {
    override fun verify(parameter: VerifyParameter, document: DataSource): List<VerifyResult> {
        val dispatcher = VerifierDispatcher(parameter.configuration as ISettings)
        val pdfData = document.inputStream.readAllBytes()
        Loader.loadPDF(pdfData).use { document ->
            val allSignatures = document.signatureDictionaries
            val selectedSignatures = when(val i = parameter.whichSignature) {
                -2 -> allSignatures.takeLast(1)
                in 0..Int.MAX_VALUE -> listOfNotNull(allSignatures.getOrNull(i))
                else -> allSignatures
            }
            return selectedSignatures.flatMap {
                dispatcher.checkSignature(it, pdfData, parameter)
            }
        }
    }

    private fun VerifierDispatcher.checkSignature(
        sig: PDSignature, document: ByteArray, parameter: VerifyParameter
    ): List<VerifyResult> {
        if (sig.byteRange.isEmpty() || sig.contents.isEmpty()) {
            throw PdfAsException("error.pdf.verify.01")
        }
        val filterVerifier = getVerifier(sig.filter, sig.subFilter) ?: return emptyList()
        val levelVerifier = getVerifierByLevel(parameter.signatureVerificationLevel)
        synchronized(levelVerifier) {
            levelVerifier.setConfiguration(parameter.configuration)
            return filterVerifier.verify(
                SignatureInputData(document, sig.byteRange),
                sig.contents, parameter.verificationTime, levelVerifier)
        }
    }
}
