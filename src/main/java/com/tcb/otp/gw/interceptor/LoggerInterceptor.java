/**
 * @(#) LoggerInterceptor.java
 * 
 * Description: OtpGateway API Request 攔截器
 *
 */

package com.tcb.otp.gw.interceptor;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hitrust.core.exception.APIException;

@Component
public class LoggerInterceptor implements HandlerInterceptor {

	// Lo4j
	private static Logger LOG = LogManager.getLogger(LoggerInterceptor.class);
	
//	private static final long MB = 1024 * 1024L;
	
	/**
	 * Executed before actual handler is executed - 請求提交前處理
	 * 
	 * @param request
	 * @param response
	 * @param handler
	 * @throws Exception
	 * @return true or false
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws APIException {
    	Runtime rt = Runtime.getRuntime();
    	long totalMemoryMb = rt.totalMemory();
    	long freeMemoryMb = rt.freeMemory();
    	long maxMemoryMb = rt.maxMemory();
    	
		LOG.debug("[API Request Begin] preHandle() - SessionId[{}], URL[{}], usageMemory[{}], totalMemory[{}], freeMemory[{}], maxMemory[{}] <<======"
				, request.getSession().getId()
				, request.getRequestURL()
        		, totalMemoryMb - freeMemoryMb
        		, totalMemoryMb
        		, freeMemoryMb
        		, maxMemoryMb
				);
//		HandlerMethod hm = (HandlerMethod) handler;
		LocaleContextHolder.setLocale(Locale.TAIWAN);
		return true;
	}
	
    /**
     * Executed before after handler is executed
     **/
    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) {
    	Runtime rt = Runtime.getRuntime();
    	long totalMemoryMb = rt.totalMemory();
    	long freeMemoryMb = rt.freeMemory();
    	long maxMemoryMb = rt.maxMemory();
    	
    	LOG.debug("[API Request End] postHandle() - SessionId[{}], URL[{}], usageMemory[{}], totalMemory[{}], freeMemory[{}], maxMemory[{}] <<======"
				, request.getSession().getId()
				, request.getRequestURL()
        		, totalMemoryMb - freeMemoryMb
        		, totalMemoryMb
        		, freeMemoryMb
        		, maxMemoryMb
    			);
    }

	/**
	 * 請求提交處理結束操作
	 * 
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws APIException {

	}
}
