package com.xiyi.weixin.util.jpush;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

import com.xiyi.util.config.WebAppConfig;







public class JPushUtil {
	private static String appKey = WebAppConfig.getProperty("jpush.key");
	private static String masterSecret = WebAppConfig.getProperty("jpush.secret");
	private static final Logger  logger = LoggerFactory.getLogger(JPushUtil.class);
	/**
	 * HttpClient post 请求
	 * @param url:请求地址
	 * @param queryString:请求参数
	 * @param appKey:极光推送key
	 * @param masterSecret:极光推送masterSecret
	 * @return
	 */
	private static String post(String url, String queryString) {
		InputStream is = null;
		HttpClient client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		PostMethod method = new PostMethod(url);
		logger.debug("发送的url为:"+url);
		try {
			// https协议
			Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
			Protocol.registerProtocol("https", myhttps);
			// jpush要求的认证头
			String authorization = new BASE64Encoder().encode((appKey + ":" + masterSecret).getBytes());
			logger.debug("");
			logger.debug("发送的数据为:"+authorization);
			method.setRequestHeader("Authorization", "Basic " + authorization);
			
			// 设置请求参数
			InputStream queryStringIs = new ByteArrayInputStream(queryString.getBytes("utf-8"));
			RequestEntity reqEntity = new InputStreamRequestEntity(queryStringIs);
			method.setRequestEntity(reqEntity);
			// 模拟请求
			client.executeMethod(method);
			is = method.getResponseBodyAsStream();
			// 返回字符串
			String rst= InputStreamToStringUtil.inputStream2String(is);
			return rst;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				method.releaseConnection();
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 推送调用的方法
	 * @param platform：推送平台设置,必填,null会默认全部平台,有android,ios,winphone,详见JPushPlatform
	 * @param audience：推送设备指定,必填,null会默认所有对象,详见JPushAudience
	 * @param notification：通知内容体,可选,notification和message二选一，详见JPushNotification
	 * @param message：消息内容体,可选,notification和message二选一，详见JPushMessage
	 * @param options：推送参数,可选,详见JPushOptions
	 * @return
	 * @throws Exception
	 */
	public static String pushMsg(String platform, String audience, String notification, String message, String options) throws Exception {
		String url = WebAppConfig.getProperty("jpush.url");
		Map<String, String> map = new HashMap<String, String>();
		// 推送的平台
		if (platform == null || platform.isEmpty()) {
			map.put("platform", JPushPlatform.ALL);
		} else {
			map.put("platform", platform.toString());
		}
		if (audience == null || audience.isEmpty()) {
			map.put("audience", "all");
		} else {
			map.put("audience", audience);
		}

		if (null != notification && !"".equals(notification))
			map.put("notification", notification);
		if (null != message && !"".equals(message))
			map.put("message", message);
		if (null != options && !"".equals(options))
			map.put("options", options);
		String responseStr = post(url, JSONObject.fromObject(map).toString());
		return responseStr;
	}

	/**
	 * jpush所支持的平台类
	 */
	public static class JPushPlatform {
		public static String ALL = "all";
		public static String ANDROID = "android";
		public static String IOS = "ios";
		public static String WINPHONE = "winphone";

		/**
		 * 多个平台的
		 * @param platform
		 * @return
		 */
		public static String multiple(String... platform) {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < platform.length; i++) {
				list.add(platform[i]);
			}
			return list.toString();
		}
	}

	/**
	 * 推送对象类
	 */
	public static class JPushAudience {
		public static String TAG = "tag";
		public static String TAG_AND = "tag_and";
		public static String ALIAS = "alias";
		public static String REGISTRATION_ID = "registration_id";

		/**
		 * 添加推送对象
		 * @param obj：对象名称，有TAG，TAG_AND，ALIAS，REGISTRATION_ID
		 * @param value：对应的值，可以是一个数组
		 * @return
		 */
		public static String addPushObject(String obj, String... value) {
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < value.length; i++) {
				list.add(value[i]);
			}
			map.put(obj, list);
			return JSONObject.fromObject(map).toString();
		}
		
		/**添加多个推送对象
		 * @param obj：对象名称
		 * @param values：相同对象名称的值
		 * @return
		 */
		public static String addPushObject(String obj,List<String> values) {
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			map.put(obj, values);
			return JSONObject.fromObject(map).toString();
		}
		
		

		/**
		 * 多种推送对象
		 * @param pushObject：来自于addPushObject的返回结果
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public static String addMultiplePushObject(String... pushObject) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < pushObject.length; i++) {
				Map<String, Object> pushObjectMap = (Map<String, Object>) JSONObject.fromObject(pushObject[i]);
				String key = String.valueOf(pushObjectMap.keySet().toArray()[0]);
				map.put(key, pushObjectMap.get(key));
			}
			return JSONObject.fromObject(map).toString();
		}
	}

	/**
	 * 通知类
	 * 
	 * @author llh
	 * 
	 */
	public static class JPushNotification {
		public static String ALERT = "alert";
		public static String ANDROID = "android";
		public static String IOS = "ios";
		public static String WINPHONE = "winphone";
		public static String BUILDERID="101";

		/**
		 * 基本的
		 * @param value：推送的内容
		 * @return
		 */
		public static String alert(String value) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(ALERT, value);
			return JSONObject.fromObject(map).toString();
		}

		/**
		 * 安卓端推送通知
		 * @param alert：通知内容，必填
		 * @param title：通知标题，选填
		 * @param builder_id：通知栏样式ID，选填
		 * @param extras：扩展字段，供业务使用
		 * @return
		 */
		public static String android(String alert, String title, int builder_id, Map<String, Object> extras) {
			if (alert != null && !"".equals(alert)) {
				Map<String, Object> map = new HashMap<String, Object>();
				Map<String, Object> androidMap = new HashMap<String, Object>();
				androidMap.put("alert", alert);
				if (title != null && !"".equals(title)) {
					androidMap.put("title", title);
				}
				androidMap.put("builder_id", builder_id);
				if (extras != null && !extras.isEmpty()) {
					androidMap.put("extras", JSONObject.fromObject(extras).toString());
				}
				map.put(ANDROID, androidMap);
				return JSONObject.fromObject(map).toString();
			} else {
				return "error:alert must be not null";
			}
		}

		/**
		 * ios端推送通知
		 * @param alert：通知内容，必填
		 * @param sound：通知提示声音，选填
		 * @param badge：应用角标，选填
		 * @param content_available：静默推送标志，如果为 1 表示要静默推送，选填
		 * @param category：设置APNs payload中的"category"字段值，ios8才支持该字段，选填
		 * @param extras:扩展字段，供业务使用
		 * @return
		 */
		public static String ios(String alert, String sound, String badge, int content_available, String category, Map<String, Object> extras) {
			if (alert != null && !"".equals(alert)) {
				Map<String, Object> map = new HashMap<String, Object>();
				Map<String, Object> iosMap = new HashMap<String, Object>();
				iosMap.put("alert", alert);
				if (sound != null && !"".equals(sound)) {
					iosMap.put("sound", sound);
				}
				if (badge != null && !"".equals(badge)) {
					iosMap.put("badge", badge);
				}
				iosMap.put("content_available", content_available);

				if (extras != null && !extras.isEmpty()) {
					iosMap.put("extras", JSONObject.fromObject(extras).toString());
				}
				map.put(IOS, iosMap);
				return JSONObject.fromObject(map).toString();
			} else {
				return "error:alert must be not null";
			}
		}

		/**
		 * winphone 端推送通知
		 * @param alert:通知内容，必填
		 * @param title:通知标题，选填
		 * @param _open_page:点击打开的页面名称，选填
		 * @param extras:扩展字段,作为参数附加到上述打开页面的后边，选填
		 * @return
		 */
		public static String winphone(String alert, String title, String _open_page, Map<String, Object> extras) {
			if (alert != null && !"".equals(alert)) {
				Map<String, Object> map = new HashMap<String, Object>();
				Map<String, Object> winphoneMap = new HashMap<String, Object>();
				winphoneMap.put("alert", alert);
				if (title != null && !"".equals(title)) {
					winphoneMap.put("title", title);
				}
				if (_open_page != null && !"".equals(_open_page)) {
					winphoneMap.put("_open_page", _open_page);
				}
				if (extras != null && !extras.isEmpty()) {
					winphoneMap.put("extras", JSONObject.fromObject(extras).toString());
				}
				map.put(WINPHONE, winphoneMap);
				return JSONObject.fromObject(map).toString();
			} else {
				return "error:alert must be not null";
			}
		}

		/**
		 * 多种通知
		 * @date 2014-10-29
		 * @param notification
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public static String multipleNotification(String... notification) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < notification.length; i++) {
				Map<String, Object> notificationMap = (Map<String, Object>) JSONObject.fromObject(notification[i]);
				String key = String.valueOf(notificationMap.keySet().toArray()[0]);
				map.put(key, notificationMap.get(key));
			}
			return JSONObject.fromObject(map).toString();
		}
	}

	/**
	 * 透传消息类
	 */
	public static class JPushMessage {
		/**
		 * 透传消息
		 * @param msg_content:消息内容本身，必填
		 * @param title:消息标题，选填
		 * @param content_type:消息内容类型，选填
		 * @param extras:可选参数，选填
		 * @return
		 */
		public static String message(String msg_content, String title, String content_type, Map<String, Object> extras) {
			if (msg_content != null && !"".equals(msg_content)) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("msg_content", msg_content);
				if (title != null && !"".equals(title)) {
					map.put("title", title);
				}
				map.put("content_type", content_type);
				if (extras != null && !extras.isEmpty()) {
					map.put("extras", JSONObject.fromObject(extras).toString());
				}
				return JSONObject.fromObject(map).toString();
			} else {
				return "error:alert must be not null";
			}
		}
	}

	/**
	 * 推送可选项
	 */
	public static class JPushOptions {
		/**
		 * @param sendno:推送序号,纯粹用来作为 API 调用标识，API 返回时被原样返回，以方便 API 调用方匹配请求与返回。
		 * @param time_to_live:离线消息保留时长,默认 86400 （1 天），最长 10 天。设置为 0表示不保留离线消息，只有推送当前在线的用户可以收到。
		 * @param override_msg_id:要覆盖的消息ID,如果当前的推送要覆盖之前的一条推送，这里填写前一条推送的 msg_id,就会产生覆盖效果，即：1）该 msg_id 离线收到的消息是覆盖后的内容；2）即使该 msg_id Android:端用户已经收到，如果通知栏还未清除，则新的消息内容会覆盖之前这条通知；覆盖功能起作用的时限是：1 天。
		 * @param apns_production:APNs 是否生产环境,True 表示推送生产环境，False 表示要推送开发环境； 如果不指定则为推送生产环境。
		 * @param big_push_duration:定速推送时长（分钟）,又名缓慢推送，把原本尽可能快的推送速度，降低下来，在给定的 n分钟内，均匀地向这次推送的目标用户推送。最大值为 1440。未设置则不是定速推送。
		 * @return
		 */
		public static String options(String sendno, String time_to_live, String override_msg_id, String apns_production, String big_push_duration) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (sendno != null && !"".equals(sendno)) {
				map.put("sendno", sendno);
			}
			if (time_to_live != null && !"".equals(time_to_live)) {
				map.put("time_to_live", time_to_live);
			}
			if (override_msg_id != null && !"".equals(override_msg_id)) {
				map.put("override_msg_id", override_msg_id);
			}
			if (apns_production != null && !"".equals(apns_production)) {
				map.put("apns_production", apns_production);
			}
			if (big_push_duration != null && !"".equals(big_push_duration)) {
				map.put("big_push_duration", big_push_duration);
			}
			return JSONObject.fromObject(map).toString();
		}
	}
	
	public static void main(String[] args) {
		String appKey = WebAppConfig.getProperty("jpush.key");
		String masterSecret = WebAppConfig.getProperty("jpush.secret");
		try {
			String platform = JPushPlatform.multiple(JPushPlatform.ANDROID, JPushPlatform.IOS);
			String audience = null;
//			String audience = JPushAudience.addPushObject(JPushAudience.ALIAS, "18046426806","18020746956");
//			String audience = JPushAudience.addPushObject(JPushAudience.REGISTRATION_ID, "0200be6b52f");
//			String audience = JPushAudience.addMultiplePushObject(
//					JPushAudience.addPushObject(JPushAudience.ALIAS, "18046426806", "123456789098"), 
//					JPushAudience.addPushObject(JPushAudience.TAG, "测试"));
//			String notification = JPushNotification.alert("推送测试");
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("news_id", 1);
//			map.put("my_key", "23456");
//			String notification1 = JPushNotification.android("推送测试alert", "推送测试title", 3, map);
//			String notification2 = JPushNotification.ios("推送测试alert", "happy", "1", 1, null, null);
			String notification3 = JPushNotification.multipleNotification(
					JPushNotification.android("推送测试alert", "推送测试title", 101, null), 
					JPushNotification.ios("推送测试alert", "happy","1", 1, null, null));
//			String message = JPushMessage.message("推送测试msg_content", "推送测试title", "news", map);
			String rtn = pushMsg(platform, audience, notification3, "", "");
			System.err.println(appKey+":"+masterSecret+":"+notification3);
			System.err.println(rtn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

