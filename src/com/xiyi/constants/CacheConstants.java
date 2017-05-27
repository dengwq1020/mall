package com.xiyi.constants;

import org.apache.commons.lang.StringUtils;

public class CacheConstants {
	//用户信息 参数为uid
	public static String USER_INFO="USER_INFO_${}";
	//订单信息
	public static String Order_Info = "Order_Info";
	
	public static String handleStr(String pattern, Object... params) {
		if (null == pattern || pattern.trim().length() == 0) {
			return pattern;
		}

		if (null == params || params.length == 0) {
			return pattern;
		}
		for (int i = 0; i < params.length; i++) {
			if (null == params[i]) {
				params[i] = "null";
			}
			pattern = StringUtils.replaceOnce(pattern, "${}",
				params[i].toString());
		}
		return pattern;
	}
}
