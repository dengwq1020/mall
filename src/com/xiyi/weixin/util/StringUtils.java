package com.xiyi.weixin.util;

/**
 * 字符串工具类
 * @author Administrator
 *
 */
public class StringUtils {
	public String stringFilter(String filter){
		filter = filter.replace("%20", "");
		filter = filter.replace("%27", "");
		filter = filter.replace("%2527", "");
		filter = filter.replace("*", "");
		filter = filter.replace("\"", "");
		filter = filter.replace("\'", "");
		filter = filter.replace(";", "");
		filter = filter.replace("<", "");
		filter = filter.replace(">", "");
		filter = filter.replace("{", "");
		filter = filter.replace("}", "");
		filter = filter.replace("\\", "");
		return filter;
	}
	public String htmlEncode2(String string){
		string = string.trim();
		string=string.replace("&","&"); 
		string=string.replace("'","'"); 
		string=string.replace("&amp;","&"); 
		string=string.replace("&quot;",""); 
		string=string.replace("\"",""); 
		string=string.replace("&lt;","<"); 
		string=string.replace("<","<"); 
		string=string.replace("&gt;",">"); 
		string=string.replace(">",">"); 
		string=string.replace("&nbsp;",""); 
		string=string.replace("&lrm;",""); 	
		string=string.replace(" ",""); 
		string=string.replace("\u00A0", " "); 
		return string;		
	}
	
	public static boolean isNull(String str){
		return str==null || str.trim().equals("");
	}
}
