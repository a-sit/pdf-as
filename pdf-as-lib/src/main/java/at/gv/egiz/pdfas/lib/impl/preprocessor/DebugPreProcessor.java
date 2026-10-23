package at.gv.egiz.pdfas.lib.impl.preprocessor;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.lib.api.preprocessor.PreProcessor;
import at.gv.egiz.pdfas.lib.api.sign.SignParameter;
import at.gv.egiz.pdfas.lib.api.verify.VerifyParameter;

public class DebugPreProcessor implements PreProcessor {

    private static final Logger logger = LoggerFactory
            .getLogger(DebugPreProcessor.class);
    
    private static final String NAME = "Debug PreProcessor";

    private void listPPArguments(Map<String, String> map) {
        if(map != null) {
            logger.debug("pre processor arguments:");
            for (Entry<String, String> entry : map.entrySet()) {
                logger.debug("  {} => {}", entry.getKey(), entry.getValue());
            }
        } else {
            logger.debug("No pre processor arguments");
        }
    }
    
    @Override
    public void sign(SignParameter parameter) throws PDFASError {
        logger.debug("preprocessor signing ...");
        
        listPPArguments(parameter.getPreprocessorArguments());
        
        logger.debug("preprocessor signing done");
    }

    @Override
    public void verify(VerifyParameter parameter) throws PDFASError {
        logger.debug("preprocessor verifing ...");
        
        listPPArguments(parameter.getPreprocessorArguments());
        
        logger.debug("preprocessor verifing done");
    }

    @Override
    public int registrationPosition() {
        return -1;
    }

    @Override
    public String getName() {
        return NAME;
    }

}
