package at.gv.egiz.pdfas.lib.impl.preprocessor;

import java.util.*;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import at.gv.egiz.pdfas.lib.api.preprocessor.PreProcessor;

@Slf4j
public class PreProcessorLoader {

    private static List<PreProcessor> loadPreProcessors() {
        log.debug("building PreProcessors");

        List<PreProcessor> processors = new ArrayList<>();

        for (PreProcessor preProcessor : ServiceLoader.load(PreProcessor.class)) {
            log.debug("Loading {} [{}]", preProcessor.getName(), preProcessor.getClass().getName());
            processors.add(preProcessor);
            log.debug("Preprocessor added {}", preProcessor.getName());
        }

        log.debug("PreProcessors constructed");

        processors.sort(Comparator.comparingInt(PreProcessor::registrationPosition));

        log.debug("PreProcessors sorted");
        return processors;
    }

    @Getter(lazy = true)
    private final List<PreProcessor> preProcessors = loadPreProcessors();
}
