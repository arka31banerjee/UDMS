package com.udms.interceptors;

import java.util.Enumeration;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import com.udms.constants.FOODCOMCAB_APPID;
import com.udms.exception.ValidationAndBusinessException;
import com.udms.utility.UdmsUtility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestValidationInterceptor implements HandlerInterceptor {
	 private static final Logger logger = LoggerFactory.getLogger(RequestValidationInterceptor.class);
	 private final Pattern ALLOWED_PATTERN = Pattern.compile("(/udms/)([a-zA-Z0-9_]*)/");
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    		throws Exception {
    	final StringBuffer URI = request.getRequestURL();
    	String appId = UdmsUtility.extractAppName(ALLOWED_PATTERN,URI);
    	logger.info("Request URL and appId: {} : {}", URI,appId);
    	FOODCOMCAB_APPID validatedAppId = switch(appId.toUpperCase()) {
    	case "GODELIVER" -> FOODCOMCAB_APPID.GODELIVER;
    	case "FOODWHEELS" -> FOODCOMCAB_APPID.FOODWHEELS;
    	case "FAREMARKET" -> FOODCOMCAB_APPID.FAREMARKET;
		default -> throw new ValidationAndBusinessException("Not a valid appName: " + appId.toUpperCase());
    	};
    	UdmsUtility.setAppNameToRequestContext(validatedAppId);
    	Enumeration<String> headerNames = request.getHeaderNames();
    	if (headerNames != null) {
    		while (headerNames.hasMoreElements()) {
    			String headerName = headerNames.nextElement();
    			String headerValue = request.getHeader(headerName);
    			logger.info("Header: {}: {}", headerName, headerValue);
    		}
    	}
    	return true;
    }
}