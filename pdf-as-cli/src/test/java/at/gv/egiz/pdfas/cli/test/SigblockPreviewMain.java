package at.gv.egiz.pdfas.cli.test;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import at.gv.egiz.pdfas.lib.api.PdfAs;
import at.gv.egiz.pdfas.lib.api.PdfAsFactory;
import at.gv.egiz.pdfas.lib.api.sign.SignParameter;
import iaik.x509.X509Certificate;
import lombok.SneakyThrows;

public class SigblockPreviewMain {

  private static final String PDFAS_CONFIG_DIR = "/home/tlenz/Projekte/pdfas4/config/default_4.2.0/";
 

  @SneakyThrows
  public static void main(String[] args) throws Exception {
    String cwd = System.getProperty("user.dir");
    File output = new File(cwd + File.separator + "build/output.png");
    X509Certificate cert = new X509Certificate(new FileInputStream("/home/tlenz/diverses/cert_debug/binding3.pem"));

    PdfAs pdfas = PdfAsFactory.createPdfAs(new File(PDFAS_CONFIG_DIR));
    SignParameter param = PdfAsFactory.createSignParameter(pdfas.getConfiguration(), null, null);
    param.setSignatureProfileId("ERROR_LOGO");    
    //param.setSignatureProfileId("SIGNATURBLOCK_DE");
    
    
    Image placeholder = pdfas.generateVisibleSignaturePreview(param, cert, 72 * 4);
    ImageIO.write((BufferedImage)placeholder, "png", output);
  }

}
