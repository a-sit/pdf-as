package at.gv.egiz.pdfas.lib.impl.backend;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import at.gv.egiz.pdfas.common.settings.ISettings;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.gv.egiz.pdfas.common.exceptions.ErrorConstants;
import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.lib.api.Configuration;
import at.gv.egiz.pdfas.lib.backend.PDFASBackend;

/** The Class BackendLoader. */
@Slf4j
public class BackendLoader implements ErrorConstants {
	
	/** The available backends. */
	private static final Map<String, PDFASBackend> availableBackends = new HashMap<String, PDFASBackend>();
	
	/** The Constant BACKEND_CONFIG. */
	public static final String BACKEND_CONFIG = "runtime.backend";
	
	/** The default backend. */
	private static PDFASBackend defaultBackend = null;
	
	static {
		log.debug("building PDF-AS Backends");

        for (PDFASBackend backend : ServiceLoader.load(PDFASBackend.class)) {
            log.debug("Loading {} [{}]", backend.getName(), backend.getClass().getName());
            availableBackends.put(backend.getName(), backend);
            if (backend.usedAsDefault()) {
                defaultBackend = backend;
                log.debug("PDF-Backend added as default {}", backend.getName());
            } else {
                log.debug("PDF-Backend added {}", backend.getName());
			}
        }
		
		log.debug("PDF-AS Backends constructed");
		
		if(defaultBackend != null) {
            log.debug("Default backend is {}", defaultBackend.getName());
		}
	}

	public static @NonNull PDFASBackend getPDFASBackend(String name) throws PDFASError {
		if (name != null) {
			if(availableBackends.containsKey(name)) {
				return availableBackends.get(name);
			}
			throw new PDFASError(ERROR_NO_BACKEND);
		}
		
		if(defaultBackend != null) {
			return defaultBackend;
		}
		
		throw new PDFASError(ERROR_NO_BACKEND);
	}

	public static @NonNull PDFASBackend getPDFASBackend(ISettings configuration) throws PDFASError {
      return getPDFASBackend(configuration.getValue(BACKEND_CONFIG));
	}
	public static @NonNull PDFASBackend getPDFASBackend() throws PDFASError { return getPDFASBackend((String)null); }
}
