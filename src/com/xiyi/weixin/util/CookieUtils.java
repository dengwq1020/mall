package com.xiyi.weixin.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * cookie接口
 * @author Administrator
 *
 */
public class CookieUtils {
	
	/**
	 * 将cookie封装到Map里面
	 * @param request
	 * @return
	 */
	public static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){  
	    Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
	    Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	        for(Cookie cookie : cookies){
	            cookieMap.put(cookie.getName(), cookie);
	        }
	    }
	    return cookieMap;
	}
	
	
	public static String getCookieValue(HttpServletRequest request,String key){
		Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	        for(Cookie cookie : cookies){
	        	if(key.equals(cookie.getName()) ){
	        		return cookie.getValue();
	        	}	           
	        }
	    }
	    return null;
	}
	
	public static void setCookie(HttpServletResponse response,String key,String value){
		setCookie(response,key,value,0);
	}
	
	public static void setCookie(HttpServletResponse response,String key,String value,int expiry){
		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/");
		if(expiry>0){
			cookie.setMaxAge(expiry);
		}
		response.addCookie(cookie);
	}
}
