package com.udms.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.udms.constants.FOODCOMCAB_APPID;

public class UdmsUtility {
	public static String fetchAppName() {
		return (String)RequestContextHolder.getRequestAttributes()
				.getAttribute("appId", RequestAttributes.SCOPE_REQUEST);
	}
	
	public static String extractAppName(Pattern allowedPattern,StringBuffer uri) {
		final Matcher matcher = allowedPattern.matcher(uri);
		String appName = matcher.find()?matcher.group(2):null;
		return appName;
	}
	public static void setAppNameToRequestContext(FOODCOMCAB_APPID validatedAppId) {
		RequestContextHolder.getRequestAttributes().setAttribute("appId", validatedAppId.name(), RequestAttributes.SCOPE_REQUEST);
	}
}
