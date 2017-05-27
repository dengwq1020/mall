package com.xiyi.constants;

import org.apache.commons.lang.StringUtils;

public class SysConstants {
	public static final String wx_userBind="xy_userBind_${}"; 

	public static final String code = "code_${}";

	public static final String jpushId = "jpush_${}";
	public static final String orderNumber = "order_${}";//订单状态
	public static final String payJson = "pay_${}";//支付数据
	//用户信息
	public static String USER_INFO="USER_INFO_${}";

	/***********************************商城参数***********************************************/
	public static final String shopcart = "shopcart_${}";//购物车
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
	
	/*---------时间开始-------------------------*/
	public static final long uuid_time = 86400; //1天
	public static final long login_time = 604800; //
	/*---------时间结束-------------------------*/
}
