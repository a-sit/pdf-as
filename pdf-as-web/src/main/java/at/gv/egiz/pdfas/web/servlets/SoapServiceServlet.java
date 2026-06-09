package at.gv.egiz.pdfas.web.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.xml.ws.Endpoint;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.logging.FaultListener;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import at.gv.egiz.pdfas.web.ws.PDFASSigningImpl;
import at.gv.egiz.pdfas.web.ws.PDFASVerificationImpl;

import java.util.LinkedList;
import java.util.List;

@Slf4j
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

	private List<EndpointImpl> endpoints = new LinkedList<>();
	@Override
	protected void loadBus(ServletConfig sc) {
		// You could add the endpoint publish codes here
        Bus bus = BusFactory.newInstance(BusFactory.DEFAULT_BUS_FACTORY).createBus();
		bus.setProperty(FaultListener.class.getName(), new Faults());
		setBus(bus);

		val signingEndpoint = new EndpointImpl(bus, new PDFASSigningImpl());
		endpoints.add(signingEndpoint);
		signingEndpoint.publish("/wssign");

		val verificationEndpoint = new EndpointImpl(bus, new PDFASVerificationImpl());
		endpoints.add(verificationEndpoint);
		verificationEndpoint.publish("/wsverify");
	}

	@Override
	public void destroyBus() {
		endpoints.forEach(p -> {
			try { p.close(); } catch (Exception e) { log.warn("Failed to close endpoint cleanly", e); }
		});
		super.destroyBus();
	}
}
