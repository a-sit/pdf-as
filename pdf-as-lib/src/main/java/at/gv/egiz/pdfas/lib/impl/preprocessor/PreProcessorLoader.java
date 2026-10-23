package at.gv.egiz.pdfas.lib.impl.preprocessor;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.gv.egiz.pdfas.lib.api.Configuration;
import at.gv.egiz.pdfas.lib.api.preprocessor.PreProcessor;

@Slf4j
public class PreProcessorLoader {

	private static final ServiceLoader<PreProcessor> preProcessorLoader = ServiceLoader.load(PreProcessor.class);
    
    public synchronized static List<PreProcessor> getPreProcessors(Configuration configuration) {
        log.debug("building PreProcessors");
        
        List<PreProcessor> processors = new ArrayList<>();

        for (PreProcessor preProcessor : preProcessorLoader) {
            log.debug("Loading {} [{}]", preProcessor.getName(), preProcessor.getClass().getName());
            processors.add(preProcessor);
            log.debug("Preprocessor added {}", preProcessor.getName());
        }
        
        log.debug("PreProcessors constructed");

        processors.sort(Comparator.comparingInt(PreProcessor::registrationPosition));
        
        log.debug("PreProcessors sorted");
        return processors;
    }
}
