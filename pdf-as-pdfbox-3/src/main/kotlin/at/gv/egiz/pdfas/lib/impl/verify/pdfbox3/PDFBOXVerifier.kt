package at.gv.egiz.pdfas.lib.impl.verify.pdfbox3

import at.gv.egiz.pdfas.common.settings.ISettings
import at.gv.egiz.pdfas.lib.api.verify.VerifyParameter
import at.gv.egiz.pdfas.lib.api.verify.VerifyResult
import at.gv.egiz.pdfas.lib.impl.pdfbox3.asDereferencedSequence
import at.gv.egiz.pdfas.lib.impl.verify.SignatureInputData
import at.gv.egiz.pdfas.lib.impl.verify.VerifierDispatcher
import at.gv.egiz.pdfas.lib.impl.verify.VerifyBackend
import org.apache.pdfbox.Loader
import org.apache.pdfbox.cos.COSDictionary
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.cos.COSObject
import org.apache.pdfbox.cos.COSString
import org.apache.pdfbox.pdmodel.PDDocument

object PDFBOXVerifier : VerifyBackend {
    override fun verify(parameter: VerifyParameter): List<VerifyResult> {
        val dispatcher = VerifierDispatcher(parameter.configuration as ISettings)
        val pdfData = parameter.dataSource.inputStream.readAllBytes()
        Loader.loadPDF(pdfData).use { document ->
            val trailer = document.document.trailer ?: return emptyList()
            val root = trailer.getCOSDictionary(COSName.ROOT) ?: return emptyList()
            val acroForm = root.getCOSDictionary(COSName.ACRO_FORM) ?: return emptyList()
            val fields = acroForm.getCOSArray(COSName.FIELDS) ?: return emptyList()

            val signatureIndex = parameter.whichSignature
            val onlyVerifyThisSignature = when {
                signatureIndex >= 0 -> parameter.whichSignature
                // TODO: document this magic value somewhere?
                signatureIndex == -2 -> -2
                else -> null
            }
            return fields.asDereferencedSequence()
                .filterIsInstance<COSDictionary>()
                .filter { it.getCOSName(COSName.FT) == COSName.SIG }
                .let {
                    if (onlyVerifyThisSignature == -2) sequenceOf(it.lastOrNull()).filterNotNull()
                    else it.filterIndexed { i, _ -> onlyVerifyThisSignature?.equals(i) ?: true }
                }
                .mapNotNull { it.getCOSDictionary(COSName.V) }
                .flatMap { dispatcher.checkSignature(it, pdfData, parameter) }
                .toList()
        }
    }

    private fun VerifierDispatcher.checkSignature(
        sigDict: COSDictionary, document: ByteArray, parameter: VerifyParameter
    ): List<VerifyResult> {
        val byteRanges = sigDict.getCOSArray(COSName.BYTERANGE).let {
            IntArray(it.size(), it::getInt)
        }
        val filter = sigDict.getNameAsString(COSName.FILTER)
        val subFilter = sigDict.getNameAsString(COSName.SUB_FILTER)
        val content = sigDict.getDictionaryObject(COSName.CONTENTS) as COSString

        val filterVerifier = getVerifier(filter, subFilter)
        val levelVerifier = getVerifierByLevel(parameter.signatureVerificationLevel)
        synchronized(levelVerifier) {
            levelVerifier.setConfiguration(parameter.configuration)
            return filterVerifier.verify(
                SignatureInputData(document, byteRanges),
                content.bytes, parameter.verificationTime, levelVerifier)
        }
    }
}