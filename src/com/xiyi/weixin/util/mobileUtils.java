package com.xiyi.weixin.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiyi.util.config.WebAppConfig;

/**
 * 发送短信验证码接口
 * @author xiaowu
 *
 */
public class mobileUtils {
	@SuppressWarnings("deprecation")
	private static final Logger  logger = LoggerFactory.getLogger(mobileUtils.class);
	@SuppressWarnings({ "unchecked", "deprecation" })
	/**
	 * 互亿短信发送
	 * @param mobile
	 * @param content
	 */
	public static Map<String,Object> sendMessage(String mobile,String content){
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> returnMap = new HashMap<String, Object>();
		//设置用户和密码
		String account =  WebAppConfig.getProperty("sms.ihuyi.account"); 
		String password = WebAppConfig.getProperty("sms.ihuyi.password");
		//转换content
		content = URLEncoder.encode(content);
		//要发送的数据
		String postDate = String.format("account=%s&password=%s&mobile=%s&content=%s", account,password,mobile,content); 
		String url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
		String returnData = "";
		//发送post请求
		//1.设置请求方式
		logger.debug("要请求的url地址为："+url);
		PostMethod post = new PostMethod(url);
		HttpClient http = new HttpClient();
		//2.设置请求头
		post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		//3.设置请求参数
		post.setRequestBody(postDate);
		post.releaseConnection();
		try {
			//发送请求给互亿
			logger.debug("要请求的post数据为：{}",postDate);
			http.executeMethod(post);
			returnData = post.getResponseBodyAsString();
			//编码格式转换
			//returnData = new String(returnData.getBytes("ISO-8859-1"), "utf-8");		
			logger.info("data:{}",returnData);
			//xml转json
			XStreamUtils  xs = new XStreamUtils();
			Object json = xs.xmlToJson(returnData);
		
			//json转map
			try {
				map = JsonUtils.jsonToObject(json.toString(), Map.class);
			} catch (Exception e) {
			logger.error("json转为map转换失败");
			}
			//code等于2时，表示成功
			if(map.get("code").toString().equals("2")){
				returnMap.put("code", 0);
				returnMap.put("msg","成功");
				logger.info("发送短信成功");		
			}else{
				returnMap.put("code", 1);
				returnMap.put("msg", map.get("msg"));
				logger.error(map.get("msg").toString());
			}
		} catch (Exception e) {
			returnMap.put("code", 2);
			returnMap.put("msg", e.getMessage());
			logger.error("请求互亿短信发生错误",e);
		}
		return returnMap;
	}

	/**
	 * 短信宝发送接口
	 */
	public static void sendMessage2(String mobile,String content){
		//设置用户和密码
		String account = "carrbeen";
		String password = "czx123";
		password  = MD5Utils.GetMD5Code(password);
		String result = "";
		Map<String,Object> map= new HashMap<String, Object>();
		String url = "http://www.smsbao.com/sms?u="+account+"&p="+password+"&m="+mobile+"&c="+content;
		//1.设置请求方式
		HttpClient httpclient = new HttpClient();
		logger.debug("开始发送get请求url:"+url);
		GetMethod get = new GetMethod(url);
		//2.设置请求头
		get.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		get.releaseConnection();
		try {
			httpclient.executeMethod(get);
			result = get.getResponseBodyAsString();
			logger.debug("返回的信息是："+result);
		} catch (Exception e) {
			logger.error("请求失败",e);
		}
	}
}
