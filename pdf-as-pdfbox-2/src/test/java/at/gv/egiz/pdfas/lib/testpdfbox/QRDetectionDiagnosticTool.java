package at.gv.egiz.pdfas.lib.testpdfbox;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

/**
 * Diagnostic tool to analyze QR code detection behavior for each image in a PDF
 */
public class QRDetectionDiagnosticTool {
    
    public static class ImageAnalysisResult {
        public String imageName;
        public boolean imageExtracted;
        public boolean qrDetected;
        public String qrContent;
        public String errorMessage;
        public long detectionTimeMs;
        public int imageWidth;
        public int imageHeight;
        public String imageFormat;
        
        @Override
        public String toString() {
            return String.format("Image: %s, Size: %dx%d, Format: %s, QR Detected: %s, Content: %s, Error: %s, Time: %dms",
                imageName, imageWidth, imageHeight, imageFormat, qrDetected, qrContent, errorMessage, detectionTimeMs);
        }
    }
    
    public static List<ImageAnalysisResult> analyzeAllImages(String pdfPath) throws IOException {
        List<ImageAnalysisResult> results = new ArrayList<>();
        
        PDDocument doc = Loader.loadPDF(QRDetectionDiagnosticTool.class.getResourceAsStream(pdfPath));
        
        System.out.println("=== QR DETECTION DIAGNOSTIC ANALYSIS ===");
        System.out.println("PDF: " + pdfPath);
        System.out.println("Pages: " + doc.getNumberOfPages());
        
        for (int pageNum = 0; pageNum < doc.getNumberOfPages(); pageNum++) {
            PDPage page = doc.getPage(pageNum);
            System.out.println("\n--- Page " + (pageNum + 1) + " ---");
            
            if (page.getResources() != null && page.getResources().getXObjectNames() != null) {
                for (COSName xObjectName : page.getResources().getXObjectNames()) {
                    PDXObject xObject = page.getResources().getXObject(xObjectName);
                    
                    if (xObject instanceof PDImageXObject) {
                        PDImageXObject imageXObject = (PDImageXObject) xObject;
                        ImageAnalysisResult result = analyzeImage(imageXObject, xObjectName.getName());
                        results.add(result);
                        
                        System.out.println("  " + result.toString());
                        
                        // Save image for manual inspection
                        saveImageForInspection(imageXObject, xObjectName.getName());
                    }
                }
            }
        }
        
        doc.close();
        
        System.out.println("\n=== SUMMARY ===");
        System.out.println("Total images analyzed: " + results.size());
        long qrDetectedCount = results.stream().filter(r -> r.qrDetected).count();
        System.out.println("QR codes detected: " + qrDetectedCount);
        
        return results;
    }
    
    private static ImageAnalysisResult analyzeImage(PDImageXObject image, String imageName) {
        ImageAnalysisResult result = new ImageAnalysisResult();
        result.imageName = imageName;
        
        try {
            BufferedImage bufferedImage = image.getImage();
            if (bufferedImage == null) {
                result.imageExtracted = false;
                result.errorMessage = "Could not extract image - unsupported format: " + image.getSuffix();
                return result;
            }
            
            result.imageExtracted = true;
            result.imageWidth = bufferedImage.getWidth();
            result.imageHeight = bufferedImage.getHeight();
            result.imageFormat = image.getSuffix();
            
            // Check minimum size
            if (bufferedImage.getHeight() < 10 || bufferedImage.getWidth() < 10) {
                result.errorMessage = "Image too small for QR detection";
                return result;
            }
            
            // Try QR detection with multiple strategies
            result = tryQRDetection(bufferedImage, result);
            
        } catch (IOException e) {
            result.errorMessage = "IOException: " + e.getMessage();
        }
        
        return result;
    }
    
    private static ImageAnalysisResult tryQRDetection(BufferedImage image, ImageAnalysisResult result) {
        // Strategy 1: Default ZXing detection (current approach)
        long startTime = System.currentTimeMillis();
        try {
            Result qrResult = detectQRWithDefaultSettings(image);
            result.qrDetected = true;
            result.qrContent = qrResult.getText();
            result.detectionTimeMs = System.currentTimeMillis() - startTime;
            return result;
        } catch (ReaderException e) {
            result.errorMessage = "Default detection failed: " + e.getClass().getSimpleName();
        }
        
        // Strategy 2: Try with different hints
        try {
            Result qrResult = detectQRWithAlternativeHints(image);
            result.qrDetected = true;
            result.qrContent = qrResult.getText();
            result.detectionTimeMs = System.currentTimeMillis() - startTime;
            result.errorMessage += " (succeeded with alternative hints)";
            return result;
        } catch (ReaderException e) {
            result.errorMessage += ", Alternative hints failed: " + e.getClass().getSimpleName();
        }
        
        // Strategy 3: Try with pure barcode detection
        try {
            Result qrResult = detectQRWithPureBarcode(image);
            result.qrDetected = true;
            result.qrContent = qrResult.getText();
            result.detectionTimeMs = System.currentTimeMillis() - startTime;
            result.errorMessage += " (succeeded with pure barcode)";
            return result;
        } catch (ReaderException e) {
            result.errorMessage += ", Pure barcode failed: " + e.getClass().getSimpleName();
        }
        
        result.detectionTimeMs = System.currentTimeMillis() - startTime;
        return result;
    }
    
    private static Result detectQRWithDefaultSettings(BufferedImage image) throws ReaderException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        Vector<BarcodeFormat> formats = new Vector<>();
        formats.add(BarcodeFormat.QR_CODE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        
        return new MultiFormatReader().decode(bitmap, hints);
    }
    
    private static Result detectQRWithAlternativeHints(BufferedImage image) throws ReaderException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        Vector<BarcodeFormat> formats = new Vector<>();
        formats.add(BarcodeFormat.QR_CODE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        
        return new MultiFormatReader().decode(bitmap, hints);
    }
    
    private static Result detectQRWithPureBarcode(BufferedImage image) throws ReaderException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        Vector<BarcodeFormat> formats = new Vector<>();
        formats.add(BarcodeFormat.QR_CODE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        
        return new MultiFormatReader().decode(bitmap, hints);
    }
    
    private static void saveImageForInspection(PDImageXObject image, String imageName) {
        try {
            BufferedImage bufferedImage = image.getImage();
            if (bufferedImage != null) {
                File outputDir = new File("build/qr-diagnostic");
                outputDir.mkdirs();
                
                File outputFile = new File(outputDir, imageName + ".png");
                ImageIO.write(bufferedImage, "PNG", outputFile);
                System.out.println("    Saved image: " + outputFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("    Failed to save image " + imageName + ": " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        try {
            List<ImageAnalysisResult> results = analyzeAllImages("/data/platzhalter_en_de_test.pdf");
            
            System.out.println("\n=== DETAILED ANALYSIS ===");
            for (ImageAnalysisResult result : results) {
                System.out.println("\n" + result.imageName + ":");
                System.out.println("  Extracted: " + result.imageExtracted);
                System.out.println("  Size: " + result.imageWidth + "x" + result.imageHeight);
                System.out.println("  Format: " + result.imageFormat);
                System.out.println("  QR Detected: " + result.qrDetected);
                System.out.println("  Content: " + result.qrContent);
                System.out.println("  Error: " + result.errorMessage);
                System.out.println("  Detection Time: " + result.detectionTimeMs + "ms");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}