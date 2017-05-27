package com.xiyi.weixin.util.weChat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiyi.pojo.WxPaySendData;
import com.xiyi.weixin.util.JsonUtils;
import com.xiyi.weixin.util.XStreamUtils;

public class WxUnifiedorderTool {
	private final static Logger logger = LoggerFactory.getLogger(WxUnifiedorderTool.class);
	
	public static String  unifiedorder(WxPaySendData sendData){
		//将对象转换为xml
		try {							
			XStreamUtils xstream = new XStreamUtils();
			String paramXml = xstream.ObjectToXml(sendData);
			paramXml = paramXml.replace("__", "_");
			logger.info("需要发送到微信服务端的数据 xml格式 ：" + paramXml);
			String returnData = "";
			//配置请求方式
			String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			PostMethod post = new PostMethod(url);
			HttpClient http = new HttpClient();
			//设置请求头
			post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8"); 
			//设置请求参数
			post.setRequestBody(paramXml);
			post.releaseConnection();
			//发送post请求给微信
			http.executeMethod(post);
			//得到返回的数据
			returnData= post.getResponseBodyAsString();			
			// 编码格式		
			returnData = new String(returnData.getBytes("ISO-8859-1"), "utf-8");
			
			logger.info("unifiedorder return data is:"+returnData);
	
			
			//讲xml转成Json
			Object returnObject = null;
			logger.info("unifiedorder return data is:"+returnObject);
			// TODO
			String wxPaySend = "";
			returnObject = xstream.xmlToJson(returnData);						
			wxPaySend = JsonUtils.objectToJson(returnObject);
			return wxPaySend;
		} catch (Exception e) {
			logger.error("请求微信失败",e);
			return null;
		}
	}
}
