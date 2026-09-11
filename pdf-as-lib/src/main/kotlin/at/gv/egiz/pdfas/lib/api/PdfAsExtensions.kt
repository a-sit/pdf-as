package at.gv.egiz.pdfas.lib.api

import at.gv.egiz.pdfas.common.exceptions.PdfAsException
import at.gv.egiz.pdfas.lib.api.sign.IAsyncSigner
import at.gv.egiz.pdfas.lib.api.sign.SignParameter
import at.gv.egiz.pdfas.lib.api.sign.SignResult
import at.gv.egiz.pdfas.lib.impl.ErrorExtractor
import at.gv.egiz.pdfas.lib.impl.PdfAsImpl
import at.gv.egiz.pdfas.lib.impl.status.RequestedSignature
import jakarta.activation.DataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import java.io.OutputStream
import java.security.cert.CertificateException

/** Wrapper to avoid bridging through [PdfAs.signAsync]'s Future wrappers for Kotlin signers. */
suspend fun PdfAs.signSuspend(param: SignParameter, document: DataSource, signer: IAsyncSigner, output: OutputStream): SignResult {
    if (signer !is ISuspendingSigner) return signAsync(param, document, signer, output).await()
    this as PdfAsImpl
    val state = startSign(param, document)
    try {
        return state.run {
            val data = signer.getCertificateDataSuspend(signParameter)
            setCertificate(data.cert, data.pdfFilter, data.pdfSubFilter)
        }.run {
            setSignature(signer.signSuspend(signatureData, signatureDataByteRange, signParameter, requestedSignature))
        }.run {
            finishSign(output)
        }
    } catch (x: CertificateException) {
        throw ErrorExtractor.searchPdfAsError(x, state.status)
    } catch (x: PdfAsException) {
        throw ErrorExtractor.searchPdfAsError(x, state.status)
    }
}

/** Helper interface for Kotlin consumers. Bridges Kotlin suspend function implementations to Java's [IAsyncSigner]. */
interface ISuspendingSigner : IAsyncSigner {
    val coroutineScope: CoroutineScope
    override fun getCertificateData(parameter: SignParameter) =
        coroutineScope.future { getCertificateDataSuspend(parameter) }.minimalCompletionStage()
    @Throws(PdfAsException::class)
    suspend fun getCertificateDataSuspend(parameter: SignParameter): IAsyncSigner.CertificateData
    override fun sign(input: ByteArray, byteRange: IntArray, parameter: SignParameter, requestedSignature: RequestedSignature) =
        coroutineScope.future { signSuspend(input, byteRange, parameter, requestedSignature) }.minimalCompletionStage()
    @Throws(PdfAsException::class)
    suspend fun signSuspend(input: ByteArray, byteRange: IntArray, parameter: SignParameter, requestedSignature: RequestedSignature): ByteArray
}
