package at.gv.egiz.pdfas.lib.impl.verify;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.val;

import java.io.ByteArrayOutputStream;

@Value
public class SignatureInputData {
  byte[] baseData;
  int[] signedByteRanges;

  @Getter(lazy = true)
  byte[] signatureInputBytes = buildSignatureInputBytes();

  private byte[] buildSignatureInputBytes() {
    assert(signedByteRanges.length % 2 == 0);
    val builder = new ByteArrayOutputStream();
    for (int i = 0; i < signedByteRanges.length; i += 2) {
      builder.write(baseData, signedByteRanges[i], signedByteRanges[i+1]);
    }
    return builder.toByteArray();
  }
}
