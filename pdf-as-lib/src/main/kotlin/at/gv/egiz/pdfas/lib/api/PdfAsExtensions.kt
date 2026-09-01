package at.gv.egiz.pdfas.lib.api

import at.gv.egiz.pdfas.common.exceptions.PdfAsException
import at.gv.egiz.pdfas.lib.api.sign.SignParameter
import at.gv.egiz.pdfas.lib.api.sign.SignResult
import at.gv.egiz.pdfas.lib.impl.ErrorExtractor
import at.gv.egiz.pdfas.lib.impl.PdfAsImpl
import at.gv.egiz.pdfas.lib.impl.status.RequestedSignature
import iaik.x509.X509Certificate

suspend fun PdfAs.signSuspend(param: SignParameter): SignResult {
    this as PdfAsImpl
    val signer = param.suspendingSigner ?:
        try { return sign(param) }
        catch (x: IllegalArgumentException) {
            throw IllegalArgumentException("You need to specify suspendingSigner to use signSuspend()", x)
        }
    val state = startSign(param)
    try {
        return state.run {
            setCertificate(signer.getCertificate(signParameter), signer.getPDFFilter(), signer.getPDFSubFilter())
        }.run {
            setSignature(signer.sign(signatureData, signatureDataByteRange, signParameter, requestedSignature))
        }.run {
            finishSign()
        }
    } catch (x: PdfAsException) {
        throw ErrorExtractor.searchPdfAsError(x, state.status)
    }
}

interface ISuspendingSigner {
    @Throws(PdfAsException::class)
    suspend fun getCertificate(parameter: SignParameter): X509Certificate
    @Throws(PdfAsException::class)
    suspend fun sign(input: ByteArray, byteRange: IntArray, parameter: SignParameter, requestedSignature: RequestedSignature): ByteArray
    fun getPDFFilter(): String
    fun getPDFSubFilter(): String
}
