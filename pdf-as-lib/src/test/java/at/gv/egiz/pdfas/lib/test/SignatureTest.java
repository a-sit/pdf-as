package at.gv.egiz.pdfas.lib.test;

import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.lib.api.ByteArrayDataSource;
import at.gv.egiz.pdfas.lib.api.PdfAs;
import at.gv.egiz.pdfas.lib.api.PdfAsFactory;
import at.gv.egiz.pdfas.sigs.pades.PAdESSignerKeystore;
import lombok.val;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.zeroturnaround.zip.ZipUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RunWith(BlockJUnit4ClassRunner.class)
public class SignatureTest {

    @ClassRule
    public static TemporaryFolder tempFolder = new TemporaryFolder();
    static PdfAs pdfAs;
    static KeyStore keyStore;

    @BeforeClass
    public static void initialize() throws Exception {
        // unzip default config to temp dir
        val configDir = tempFolder.newFolder();
        ZipUtil.unpack(PdfAs.class.getResourceAsStream("/config/config.zip"), configDir);
        pdfAs = PdfAsFactory.createPdfAs(configDir);

        // load keystore
        keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(SignatureTest.class.getResourceAsStream("/test.p12"), "password".toCharArray());
    }

    private final static Map<String, ByteArrayDataSource> _inputPdfCache = new HashMap<>();
    public static ByteArrayDataSource getInputPdf(String filename) throws IOException {
        val normalizedName = filename.endsWith(".pdf") ? filename : (filename + ".pdf");
        var existing = _inputPdfCache.get(normalizedName);
        if (existing == null) {
            try (val stream = SignatureTest.class.getResourceAsStream("/data/" + normalizedName)) {
                existing = new ByteArrayDataSource(Objects.requireNonNull(stream).readAllBytes());
            }
            _inputPdfCache.put(normalizedName, existing);
        }
        return existing;
    }

    private final static Map<String, PAdESSignerKeystore> _keystoreSignerCache = new HashMap<>();
    public static PAdESSignerKeystore getKeystoreSigner(String keyAlias) throws PDFASError {
        var existing = _keystoreSignerCache.get(keyAlias);
        if (existing == null) {
            existing = new PAdESSignerKeystore(keyStore, keyAlias, "password");
            _keystoreSignerCache.put(keyAlias, existing);
        }
        return existing;
    }

    @Test
    public void signatureTest() throws Exception {
        val inputPdf = getInputPdf("align.pdf");

        val param = PdfAsFactory.createSignParameter(pdfAs.getConfiguration(), inputPdf, null);
        param.setPlainSigner(getKeystoreSigner("test-key"));
        param.setSignatureProfileId("SIGNATURBLOCK_SMALL_EN_NOTE");

        val outputStream1 = new ByteArrayOutputStream();
        param.setOutputStream(outputStream1);
        pdfAs.sign(param);

        val outputStream2 = new ByteArrayOutputStream();
        param.setOutputStream(outputStream2);
        pdfAs.sign(param);
        val state1 = pdfAs.startSign(param);
        val state2 = state1.setCertificate(param.getPlainSigner().getCertificate(state1.getSignParameter()).getEncoded());
        val state3 = state2.setSignature(param.getPlainSigner().sign(
                        state2.getSignatureData(),
                        state2.getSignatureDataByteRange(),
                        state2.getSignParameter(),
                        state2.getRequestedSignature()));
        state3.finishSign();

        try (FileOutputStream fos = new FileOutputStream(tempFolder.newFile())) {
          fos.write(outputStream1.toByteArray());
        }
    }
}
