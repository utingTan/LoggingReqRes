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
	public ResponseEntity<String> process(HttpServletRequest request, HttpEntity<String> entity)
			throws DBException {

		String url = getServiceUrl(request);
		if (url.equalsIgnoreCase(DISPATCHER_ERROR_NULL_URL)) 
			return ResponseEntity.ok().body(genResponeData(DISPATCHER_ERROR_NULL_URL));
		else if (url.equalsIgnoreCase(DISPATCHER_ERROR_NOSERVICE)) 
			return ResponseEntity.ok().body(genResponeData(DISPATCHER_ERROR_NOSERVICE));
		if (entity != null && entity.getBody() != null)
			LOG.info("\n"+url+"\n"+entity.getBody().toString()+"\n");
		ResponseEntity<String> rslt = template.exchange(url, HttpMethod.POST, entity, String.class);
		return ResponseEntity.ok(rslt.getBody());
	}

	@PostMapping(value = { "/SendOTPServlet" })
	public ResponseEntity<String> processServlet(final HttpServletRequest request, HttpEntity<String> entity)
			throws DBException {

		String url = getServiceUrl(request);
		if (url.equalsIgnoreCase(DISPATCHER_ERROR_NULL_URL)) 
			return ResponseEntity.ok().body(genResponeData(DISPATCHER_ERROR_NULL_URL));
		else if (url.equalsIgnoreCase(DISPATCHER_ERROR_NOSERVICE)) 
			return ResponseEntity.ok().body(genResponeData(DISPATCHER_ERROR_NOSERVICE));
		HttpHeaders headers = new HttpHeaders();
		// 請勿輕易改變此提交方式，大部分的情況下，提交方式都是表單提交
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
		params.add("MOBILE_PHONE", request.getParameter("MOBILE_PHONE")); // 用戶手機號碼 
		params.add("SMS_CONTENT", request.getParameter("SMS_CONTENT")); // 簡訊內容
		params.add("BIZACCNO", request.getParameter("BIZACCNO")); // 業務別代號
		try {
			LOG.info("\n"+url+"\n"+"mobilePhone=["+request.getParameter("MOBILE_PHONE")+"], smsContent=["+request.getParameter("SMS_CONTENT")+"]["+URLDecoder.decode(request.getParameter("SMS_CONTENT"),"utf-8")+"], bizAccno=["+request.getParameter("BIZACCNO")+"]");
		} catch (UnsupportedEncodingException e) {
			LOG.info("\n"+url+"\n"+"mobilePhone=["+request.getParameter("MOBILE_PHONE")+"], smsContent=["+request.getParameter("SMS_CONTENT")+"], bizAccno=["+request.getParameter("BIZACCNO")+"]");
		}
		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		ResponseEntity<String> rslt = template.exchange(url.toString(), HttpMethod.POST, requestEntity, String.class);
		return new ResponseEntity<>(rslt.getBody(), rslt.getHeaders(), HttpStatus.OK);
	}
	
	private GwUrlMapping findGateWayUrl(String apiId) throws DBException {
		return mappingRepository.findByKey(GwUrlMapping.class, apiId);
	}
	
	private String getServiceUrl(HttpServletRequest request) throws DBException {
		try {
			String path = request.getRequestURL().toString();
			LOG.debug("getServiceUrl() begin - path=[{}]", path);
			String host = path.replace(request.getRequestURI(), "");
			String apiId = path.substring(path.lastIndexOf('/') + 1);
			GwUrlMapping mapping = findGateWayUrl(apiId);
			
			if ( null == mapping) {
				return DISPATCHER_ERROR_NULL_URL;
				
			} else {

//				ServiceInstance serviceInstance = lbClient.choose(TCB_OTP_SERVICE);
//				
//				if ( null == serviceInstance) {
//					serviceInstance = lbClient.choose(TCB_OTP_SERVICE);
//					
//					if ( null == serviceInstance)
//						return DISPATCHER_ERROR_NOSERVICE;
//				}
				
				String gwUrl = mapping.getUrl();
				LOG.debug("getServiceUrl() dbUrl=[{}]", gwUrl);
				
				String rtnUrl = String.format("%s%s"
						, host.replace("9080", "9085")
						, gwUrl.replace("otp-service", TCB_OTP_SERVICE));
				LOG.debug("getServiceUrl() end - rtnUrl=[{}]", rtnUrl);
				return rtnUrl;
			}
			
		} catch (Exception e) {
			LOG.info(e.toString());
			return DISPATCHER_ERROR_NOSERVICE;
		}
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

		ObjectMapper mapper = new ObjectMapper();

		OtpApiRequest<BaseReqBody> request = null;

		Map<String, String> rslMap = null;
		OtpHeader reqHeader = null; //
		BaseReqBody reqBody = null;

		try {
			rslMap = mapper.readValue(content, Map.class);
			// 取得 API Request Header
			if (rslMap.get("reqHeader") != null)
				reqHeader = mapper.convertValue(rslMap.get("reqHeader"), OtpHeader.class);
			// 取得 API Request Body
			if (rslMap.get("reqBody") != null)
				reqBody = mapper.convertValue(rslMap.get("reqBody"), BaseReqBody.class);
			request = new OtpApiRequest(reqHeader, reqBody);
		} catch (Exception e) {
			LOG.error("Initialization API Request Instance Error", e);
			throw new APIException("unknow yat..."); // TODO defined new msgCode for the error 2020.08.10
		}

		return request;
	}

	private String genResponeData(String rtnMsg) {
		LOG.debug("genResponeData() begin - rtnMsg=[{}]", rtnMsg);
		String xml = null;
		OtpApiResponse<BaseResBody> response = new OtpApiResponse<BaseResBody>();
		OtpHeader resHeader = new OtpHeader();

		// API Request 交易時間
		resHeader.setTxSn(DateUtils.getcurrentTimeStrByDateFormat(DateUtils.FORMAT_DATETIME_WITH_SLASH));
		resHeader.setReturnCode(UNKNOWN_RUNTIME_EXCEPTION);
		resHeader.setReturnMsg(rtnMsg);
		// 回應說明 依據 MsgCode 取得對應 i18n 訊息, 無法對應則顯示原訊息代碼
		// i18nMsgHelper.message("dispatcher.error.null.url");
		response.setResponseHeader(resHeader);
		
		try {
			String objectResponseXml = new XmlMapper().writeValueAsString(response);
			String replaceEmptyNameSpace = null;
			
			if ( StringUtils.isNotBlank(objectResponseXml)) {
				replaceEmptyNameSpace = objectResponseXml.replaceAll(XML_EMPTY_NAMESPACE, "");
				
			} else {
				replaceEmptyNameSpace = "";
			}
			xml = String.format("%s%s", XML_VERSION, replaceEmptyNameSpace);
			LOG.debug("genResponeData() end - xml=[{}]", xml);
			
		} catch (JsonProcessingException e) {
			LOG.error("genResponeData() fail - rtnMsg=[{}], e=", rtnMsg, e);
		}
		return xml;
	}
}