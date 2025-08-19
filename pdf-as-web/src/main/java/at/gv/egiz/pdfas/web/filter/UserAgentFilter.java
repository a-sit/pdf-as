package at.gv.egiz.pdfas.web.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAgentFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	private static final ThreadLocal<String> requestUserAgent = new ThreadLocal<String>() {

		@Override
		protected String initialValue() {
			return "unkown";
		}
		
	};

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if(request instanceof HttpServletRequest) {
			log.debug("Processing Parameters into Attributes");
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			requestUserAgent.set(httpRequest.getHeader("User-Agent"));
		}
		try {
			chain.doFilter(request, response);
		} finally {
			requestUserAgent.remove();
		}
	}

	@Override
	public void destroy() {
	}

	public static String getUserAgent() {
		return requestUserAgent.get();
	}
	
}
