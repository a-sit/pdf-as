/*******************************************************************************
 * <copyright> Copyright 2014 by E-Government Innovation Center EGIZ, Graz, Austria </copyright>
 * PDF-AS has been contracted by the E-Government Innovation Center EGIZ, a
 * joint initiative of the Federal Chancellery Austria and Graz University of
 * Technology.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 ******************************************************************************/
package at.gv.egiz.pdfas.web.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.gv.egiz.pdfas.api.processing.PdfasSignResponse;
import at.gv.egiz.pdfas.api.processing.SignedDocument;
import at.gv.egiz.pdfas.api.ws.PDFASVerificationResponse;
import at.gv.egiz.pdfas.web.config.WebConfiguration;
import at.gv.egiz.pdfas.web.helper.PdfAsHelper;
import at.gv.egiz.pdfas.web.helper.PdfAsParameterExtractor;
import at.gv.egiz.pdfas.web.stats.StatisticEvent;
import at.gv.egiz.pdfas.web.stats.StatisticEvent.Status;
import at.gv.egiz.pdfas.web.stats.StatisticFrontend;
import lombok.extern.slf4j.Slf4j;

/**
 * Servlet implementation class PDFData
 */
@Slf4j
public class PDFData extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public PDFData() {
    super();
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  @Override
  protected void doGet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    this.process(request, response);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  @Override
  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    this.process(request, response);
  }

  protected void process(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PdfasSignResponse resultObject = PdfAsHelper.getPdfSigningResponse(request);
    
    if (resultObject == null) {
      log.warn("No data for session with Id: {}", request.getSession().getId());
      PdfAsHelper.setSessionException(request, response,
          "No signed pdf document available.", null);
      PdfAsHelper.gotoError(getServletContext(), request, response);
      
    } else if (resultObject.getSignedPdfs().isEmpty()) {
      log.info("No signed pdf document available.");
      PdfAsHelper.setSessionException(request, response,
          "No signed pdf document available.", null);
      PdfAsHelper.gotoError(getServletContext(), request, response);     
      
    } else if (PdfAsHelper.getPdfSigningResponse(request).getSignedPdfs().size() == 1)  {
      buildSingleFileResult(request, response,
          PdfAsHelper.getPdfSigningResponse(request).getSignedPdfs().get(0));
            
    } else {
      buildMultipleFileResult(request, response, PdfAsHelper.getPdfSigningResponse(request).getSignedPdfs());
      
    }
       
  }

  private void buildMultipleFileResult(HttpServletRequest request, HttpServletResponse response,
      List<SignedDocument> signedPdfs) throws IOException, ServletException {
    
    final StatisticEvent statisticEvent = PdfAsHelper.getStatisticEvent(request,response);

    // check if some files are expired
    if (WebConfiguration.isKeepSignedDocument()) {            
      if (signedPdfs.stream()
              .filter(el -> isSignedDataExpired(el))
              .findFirst().isPresent()) {
        log.info("Destroying expired signed data in session");
        request.getSession().invalidate();
        PdfAsHelper.setSessionException(request, response,
            "No signed pdf document available.", null);
        PdfAsHelper.gotoError(getServletContext(), request, response);
        return;
      }
    }
    
    // package files into ZIP
    byte[] zippedFiles = packageSignedPdfsIntoZip(signedPdfs);
        
    // write static log
    if (statisticEvent != null) {
      if (!statisticEvent.isLogged()) {
        statisticEvent.setStatus(Status.OK);
        statisticEvent.setEndNow();
        statisticEvent.setTimestampNow();
        StatisticFrontend.getInstance().storeEvent(statisticEvent);
        statisticEvent.setLogged(true);
      }
    }
    
    // build response
    response.setHeader("Content-Disposition", "inline;filename=\"multiple_documents.zip\"");        
    response.setContentType("application/zip");
    final OutputStream os = response.getOutputStream();

    os.write(zippedFiles);
    os.close();

    // When data is collected destroy session!
    if (!WebConfiguration.isKeepSignedDocument()) {
      log.debug("Destroying signed data in session : {}", request.getSession().getId());
      request.getSession().invalidate();
    } else {
      log.debug("Keeping signed data in session : {}", request.getSession().getId());
    }
    
  }

  private byte[] packageSignedPdfsIntoZip(List<SignedDocument> signedPdfs) throws IOException {    
    ByteArrayOutputStream baOut = new ByteArrayOutputStream();
    
    try {    
      ZipOutputStream zos = new ZipOutputStream(baOut);
      zos.setLevel(Deflater.BEST_COMPRESSION);
      zos.setMethod(Deflater.DEFLATED);
      
      Iterator<SignedDocument> it = signedPdfs.iterator();
      while (it.hasNext()) {         
        SignedDocument entry = it.next();
         if (entry.getOutputData() != null) {                                 
            log.debug("Compressing file {}.", entry.getFileName());
            ZipEntry oze = new ZipEntry(entry.getFileName());
            zos.putNextEntry(oze);          
            zos.write(entry.getOutputData());
            zos.closeEntry();            
            
         } else {
            log.warn("Ignore entry with name: {} because it's empty", entry.getFileName());
            
         }
      }     
      zos.closeEntry();  
      zos.finish();
      zos.close();
      
      return baOut.toByteArray();
    
    } finally {
      baOut.close();
      
    }
    
  }

  private void buildSingleFileResult(HttpServletRequest request, HttpServletResponse response, SignedDocument signedFile) throws ServletException, IOException {
    final byte[] signedData = signedFile.getOutputData();

    final StatisticEvent statisticEvent = PdfAsHelper.getStatisticEvent(request,
        response);

    final String plainPDFDigest = PdfAsParameterExtractor.getOrigDigest(request);

    if (signedData != null) {

      if (WebConfiguration.isKeepSignedDocument()) {
        if (isSignedDataExpired(signedFile)) {
          log.info("Destroying expired signed data in session");
          request.getSession().invalidate();
          PdfAsHelper.setSessionException(request, response,
              "No signed pdf document available.", null);
          PdfAsHelper.gotoError(getServletContext(), request, response);
          return;
        }
      }

      if (plainPDFDigest != null) {
        final String signatureDataHash = PdfAsHelper
            .getSignatureDataHash(request);
        if (!plainPDFDigest.equalsIgnoreCase(signatureDataHash)) {
          log.warn("Digest Hash mismatch!");
          log.warn("Requested digest: " + plainPDFDigest);
          log.warn("Saved     digest: " + signatureDataHash);

          PdfAsHelper.setSessionException(request, response,
              "Signature Data digest do not match!", null);
          PdfAsHelper.gotoError(getServletContext(), request,
              response);
          return;
        }
      }
      response.setHeader("Content-Disposition", "inline;filename=\""
          + PdfAsHelper.getPDFFileName(request) + "\"");
      final String pdfCert = signedFile.getSignerCertificate();
      if (pdfCert != null) {
        response.setHeader("Signer-Certificate", pdfCert);
      }

      if (statisticEvent != null) {
        if (!statisticEvent.isLogged()) {
          statisticEvent.setStatus(Status.OK);

          statisticEvent.setEndNow();
          statisticEvent.setTimestampNow();
          StatisticFrontend.getInstance().storeEvent(statisticEvent);
          statisticEvent.setLogged(true);
        }
      }

      final PDFASVerificationResponse resp = signedFile.getVerificationResponse();
      if (resp != null) {
        response.setHeader("CertificateCheckCode",
            String.valueOf(resp.getCertificateCode()));
        response.setHeader("ValueCheckCode",
            String.valueOf(resp.getValueCode()));
      }
      response.setContentType("application/pdf");
      final OutputStream os = response.getOutputStream();
      os.write(signedData);
      os.close();

      // When data is collected destroy session!
      if (!WebConfiguration.isKeepSignedDocument()) {
        log.debug("Destroying signed data in session : {}", request.getSession().getId());
        request.getSession().invalidate();
      } else {
        log.debug("Keeping signed data in session : {}", request.getSession().getId());
      }
    } else {
      log.info("No signed pdf document available.");
      PdfAsHelper.setSessionException(request, response,
          "No signed pdf document available.", null);
      PdfAsHelper.gotoError(getServletContext(), request, response);
      
    }
    
  }

  private static boolean isSignedDataExpired(SignedDocument signedFile) {
    final long now = System.currentTimeMillis();
    final long validUntil = signedFile.getSigningTimestamp() + 300000;
    
    log.debug("Checking signed data valid until {} now is {}", validUntil, now);
    return validUntil < now;

  }

}
