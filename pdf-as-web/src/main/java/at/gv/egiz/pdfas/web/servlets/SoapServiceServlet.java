package at.gv.egiz.pdfas.web.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.xml.ws.Endpoint;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.logging.FaultListener;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import at.gv.egiz.pdfas.web.ws.PDFASSigningImpl;
import at.gv.egiz.pdfas.web.ws.PDFASVerificationImpl;

public class SoapServiceServlet extends CXFNonSpringServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8903883276191902043L;

	@Slf4j
	public static class Faults implements FaultListener {
		@Override
		public boolean faultOccurred(Exception exception, String description, Message message) {
			String operation = "-";
			if (message != null && message.getExchange() != null) {
				val boi = message.getExchange().getBindingOperationInfo();
				if (boi != null && boi.getName() != null) {
					operation = boi.getName().toString();
				}
			}

			log.error("Unhandled SOAP fault in operation {}: {}", operation, description, exception);
			return false;
		}
	}

	@Override
	protected void loadBus(ServletConfig sc) {
		super.loadBus(sc);

		// You could add the endpoint publish codes here
        Bus bus = BusFactory.newInstance(BusFactory.DEFAULT_BUS_FACTORY).createBus();
		bus.setProperty(FaultListener.class.getName(), new Faults());
        BusFactory.setDefaultBus(bus);

        Endpoint signEp = Endpoint.publish("/wssign", new PDFASSigningImpl());
        /*
         * SOAPBinding signBinding = (SOAPBinding)signEp.getBinding();
        signBinding.setMTOMEnabled(true);
        */
        
        Endpoint verifyEp = Endpoint.publish("/wsverify", new PDFASVerificationImpl());
        /*
        SOAPBinding verifyBinding = (SOAPBinding)verifyEp.getBinding();
        verifyBinding.setMTOMEnabled(true);
        */
        
	}
}
