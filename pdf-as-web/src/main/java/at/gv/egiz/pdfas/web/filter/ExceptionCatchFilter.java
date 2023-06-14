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
package at.gv.egiz.pdfas.web.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import com.beust.jcommander.Strings;
import com.beust.jcommander.internal.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * Servlet Filter implementation class ExceptionCatchFilter
 */
@Slf4j
public class ExceptionCatchFilter implements Filter {

  List<String> statelessPaths;
  
	/**
	 * Default constructor.
	 */
	public ExceptionCatchFilter() {
	}

	 /**
   * @see Filter#init(FilterConfig)
   */
  public void init(FilterConfig fConfig) throws ServletException {    
    String statelessConfigStrings = fConfig.getInitParameter("statelessServlets");
    if (statelessConfigStrings != null) {
      statelessPaths = Lists.newArrayList(StringUtils.split(statelessConfigStrings, ","));
      
    } else {
      statelessPaths = Collections.emptyList();
      
    }    
    log.info("Stateless paths set to: {}", Strings.join(", ", statelessPaths));
    
  }

	
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {

			if (request instanceof HttpServletRequest) {
				HttpServletRequest httpRequest = (HttpServletRequest) request;				
				
				HttpSession session = httpRequest.getSession(isStatefull(httpRequest.getServletPath()));
				String sessionId = session != null ? session.getId() : "-";												
				MDC.put("SESSION_ID", sessionId);				  
				log.info("Access from IP: {}", getClientIpAddr(httpRequest));
				log.info("Access to: {} in Session: {}", httpRequest.getServletPath(), sessionId);
				
				log.debug("Processing Parameters into Attributes");
				@SuppressWarnings("unchecked")
				Enumeration<String> parameterNames = httpRequest.getParameterNames();
				while (parameterNames.hasMoreElements()) {
					String name = parameterNames.nextElement();
					String value = httpRequest.getParameter(name);
					request.setAttribute(name, value);
				}
			}

			try {
				chain.doFilter(request, response);
				
			} finally {
  			if (response instanceof HttpServletResponse) {
	  			HttpServletResponse resp = (HttpServletResponse) response;
		  		log.debug("Got response status: {}", resp.getStatus());
		  		
			  } else {
				  log.warn("Response is not a HttpServletResponse!");
				  
				}
			}
		} catch (Throwable e) {
			log.error("Unhandled exception found", e);
			throw new ServletException(e.getMessage());
			
		} finally {
			MDC.remove("SESSION_ID");
			
		}
		/*
		 * } catch(Throwable e) {
		 * System.err.println("Unhandled Exception found!");
		 * e.printStackTrace(System.err);
		 * log.error("Unhandled Exception found!", e); }
		 */
	}

	private boolean isStatefull(String contextPath) {
	  boolean statefull = !statelessPaths.contains(contextPath);
	  log.trace("ServletPath: {} is marked as {}", contextPath, statefull ? "statefull" : "stateless");	  
    return statefull;
    
  }

  public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
