package com.tcb.otp.gw.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class SessionFilter implements Filter {
	private static Logger LOG = LogManager.getLogger(SessionFilter.class);
	private static String BEGIN = "Begin";
	private static String END = "End";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httprequest = (HttpServletRequest)request;
		memoryTracking(httprequest, BEGIN);
		chain.doFilter(request, response);
		memoryTracking(httprequest, END);
	}

	private void memoryTracking(HttpServletRequest request, String aspect) {
    	Runtime rt = Runtime.getRuntime();
    	long totalMemoryMb = rt.totalMemory();
    	long freeMemoryMb = rt.freeMemory();
    	long maxMemoryMb = rt.maxMemory();
    	
		LOG.debug("[memoryTracking] {} - SessionId[{}], URL[{}], usageMemory[{}], totalMemory[{}], freeMemory[{}], maxMemory[{}]"
				, aspect
				, request.getSession().getId()
				, request.getRequestURL()
        		, totalMemoryMb - freeMemoryMb
        		, totalMemoryMb
        		, freeMemoryMb
        		, maxMemoryMb
				);
	}
}
