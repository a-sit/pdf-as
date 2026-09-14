package at.gv.egiz.pdfas.lib.api.sign;

import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.lib.api.PdfAs;
import at.gv.egiz.pdfas.lib.impl.status.RequestedSignature;
import lombok.NonNull;

import java.io.OutputStream;
import java.security.cert.X509Certificate;
import java.util.concurrent.CompletionStage;
import jakarta.activation.DataSource;

/**
 * Asynchronous signer interface.
 * <p>
 * PDF-AS can use three ways to sign a document.
 * <li> Use the multi-stage API yourself. See {@link PdfAs#startSign(SignParameter, DataSource)}.
 * <li> Use a synchronous IPlainSigner. See {@link PdfAs#sign(SignParameter, DataSource, IPlainSigner, OutputStream)}.
 * <li> Use an asynchronous IAsyncSigner. See {@link PdfAs#signAsync(SignParameter, DataSource, IAsyncSigner, OutputStream)}. <b>You are here.</b>
 */
public interface IAsyncSigner {
  /** @param cert The certificate corresponding to the used signing key
   *  @param pdfFilter The PDF Filter string for the used signature algorithm
   *  @param pdfSubFilter the PDF SubFilter string for the used signature algorithm */
  public record CertificateData(@NonNull X509Certificate cert, @NonNull String pdfFilter, @NonNull String pdfSubFilter) {}
  /** Should return the signing certificate data to use with this operation. May either throw, or complete exceptionally, on failure. */
  public @NonNull CompletionStage<@NonNull CertificateData> getCertificateData(@NonNull SignParameter parameter) throws PdfAsException;
  /** Should return the PDF signature `/Contents` covering the specified bytes. For PAdES, this is the encoded bytes of the CMS `SignedData`. */
  public @NonNull CompletionStage<byte @NonNull[]> sign(byte @NonNull[] input, int @NonNull[] byteRange, @NonNull SignParameter parameter, @NonNull RequestedSignature requestedSignature) throws PdfAsException;
}
