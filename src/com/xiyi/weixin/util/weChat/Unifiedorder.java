package com.xiyi.weixin.util.weChat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiyi.pojo.WxAppPaySendData;
import com.xiyi.pojo.WxPaySendData;
import com.xiyi.util.config.WebAppConfig;
import com.xiyi.weixin.util.JsonUtils;
import com.xiyi.weixin.util.WeChatUtil;
import com.xiyi.weixin.util.XStreamUtils;
import com.xiyi.weixin.util.config.WeChatConfig;

public class Unifiedorder {
	private static final Logger logger = LoggerFactory.getLogger(Unifiedorder.class);
	
	private static String webPath = WebAppConfig.getProperty("web.url");
	@SuppressWarnings({ "unchecked", })
	public static Map<String,String> unifiedorder(Map<String, Object> paramMap,String path,String out_trade_no){
		
		// 商品描述
		String body = "微信支付";
		// 商品详情
		String detail = "订单";
		// 总金额
		String total_fee = paramMap.get("total_fee").toString()+"00";
		// 终端IP
		String spbill_create_ip = paramMap.get("spbill_create_ip").toString();
		try {
			body = new String(body.getBytes("UTF-8"));
			detail = new String(detail.getBytes("UTF-8"));
		} catch (Exception e) {
			logger.error("中文转换失败",e);
		}
		// 微信支付API请求参数
		WxAppPaySendData data = new WxAppPaySendData();
		data.setAppid(WeChatConfig.app_id);
		data.setMch_id(WeChatConfig.partner);
		data.setBody(body);
		data.setNonce_str(UUID.randomUUID().toString().replaceAll("-", ""));
		data.setDetail(detail);
		data.setOut_trade_no(out_trade_no);
		data.setTotal_fee(total_fee);
		data.setSpbill_create_ip(spbill_create_ip);
		// 通知地址__
		data.setNotify_url(webPath+"pay/wechat/appnotify.do");
		//将对象转换为map
		
		String dataJson = "";
		try {
			dataJson = JsonUtils.objectToJson(data);
			Map<String, String> wxPaySendDataMap = new HashMap<String, String>();
			wxPaySendDataMap = JsonUtils.jsonToObject(dataJson, Map.class);
		
			//进行签名
			data.setSign(WeChatUtil.getSign(wxPaySendDataMap, WeChatConfig.key));
			//logger.debug(WeChatUtil.getSign(wxPaySendDataMap, WeChatConfig.key));
			//将对象转换为xml
			XStreamUtils xstream = new XStreamUtils();
			String paramXml = xstream.getXMLForObject(data, WxPaySendData.class);
			logger.info("需要发送到微信服务端的数据 xml格式 ：" + paramXml);
			String returnData = WebTool.sendPost(WeChatConfig.unifiedorder,
					EncodeTool.getStringEncode(paramXml,"utf-8","utf-8"));
			returnData = new String(returnData.getBytes("gbk"), "utf-8");
			logger.info("需要发送到微信服务端的数据 xml格式 ：" + paramXml);
		
			Object returnObject = null;
		
			returnObject = xstream.xmlToJson(returnData);
		
			logger.info("unifiedorder return data is:"+returnObject);
			// TODO
			String wxPaySend = "";
		
			wxPaySend = JsonUtils.objectToJson(returnObject);
		
			// POJO转Map
			Map<String, String> wxPayReturnDataMap = new HashMap<String, String>();
	
			wxPayReturnDataMap = JsonUtils.jsonToObject(wxPaySend, Map.class);
			return wxPayReturnDataMap;
		} catch (Exception e1) {
			logger.error("生成信息错误",e1);
			
		}
		return null;
	}
	//中文转unicode
	 public static String chinaToUnicode(String str){  
	        String result="";  
	        for (int i = 0; i < str.length(); i++){  
	            int chr1 = (char) str.charAt(i);  
	            if(chr1>=19968&&chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)  
	                result+="\\u" + Integer.toHexString(chr1);  
	            }else{  
	                result+=str.charAt(i);  
	            }  
	        }  
	        return result;  
	    }  
}
