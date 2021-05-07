/**
 * @(#) DispatcherController.java
 * 
 * Description: Gateway 各 API 調度接口
 *
 * Modify History: 
 * 	v1.00, 2020/11/16, Jack
 *	 1) First release
 */

package com.tcb.otp.gw.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.hitrust.core.api.BaseReqBody;
import com.hitrust.core.api.BaseResBody;
import com.hitrust.core.exception.APIException;
import com.hitrust.core.exception.DBException;
import com.tcb.otp.api.OtpApiRequest;
import com.tcb.otp.api.OtpApiResponse;
import com.tcb.otp.api.OtpHeader;
import com.tcb.otp.gw.entity.GwUrlMapping;
import com.tcb.otp.gw.repo.GwUrlMappingRepo;
import com.tcb.otp.util.DateUtils;

@RestController
public class DispatcherController { 

	// Log4j
	private static final Logger LOG = LogManager.getLogger(DispatcherController.class);

	private static String UNKNOWN_RUNTIME_EXCEPTION = "O8888";
	private static String DISPATCHER_ERROR_NULL_URL = "此交易的業務代碼不存在或未審核，請通知相關人員處理";
	private static String DISPATCHER_ERROR_NOSERVICE = "系統忙碌，請稍候";
	private static String XML_VERSION = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
	private static String XML_EMPTY_NAMESPACE = " xmlns=\"\"";
	private static String TCB_OTP_SERVICE = "TCB.OTP.SERVICE";

//	public static ThreadLocal<String> originHost = new ThreadLocal<>();

	@Autowired
	private RestTemplate template;

	@Autowired
	private GwUrlMappingRepo mappingRepository;

//	@Autowired
//	private LoadBalancerClient lbClient;

	@GetMapping(value = { "/otpok" })
	@ResponseBody
	public String optOk() throws Exception {
		return "ok";
	}
	
	@PostMapping(value = { "/rest/Otp*", "/rest/Device*", "/rest/SendOtp*" })
	@ResponseBody
	public ResponseEntity<String> process(HttpServletRequest request, HttpEntity<String> entity)
			throws DBException {
		return ResponseEntity.status(HttpStatus.OK).body("ok");
	}

	@PostMapping(value = { "/SendOTPServlet" })
	@ResponseBody
	public ResponseEntity<String> processServlet(final HttpServletRequest request, HttpEntity<String> entity)
			throws DBException {
		return ResponseEntity.status(HttpStatus.OK).body("ok");
	}
	
	private GwUrlMapping findGateWayUrl(String apiId) throws DBException {
		return null;
	}
	
	private String getServiceUrl(HttpServletRequest request) throws DBException {
		return null;
	}
	
	/**
	 * 初始化 API Request Instance
	 * 
	 * @param content
	 * @return TcbCorpRequest or null
	 * @throws APIException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected OtpApiRequest<BaseReqBody> initAPIRequestInstance(String content) throws APIException {
		return null;
	}

	private String genResponeData(String rtnMsg) {
		return null;
	}
}