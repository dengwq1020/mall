package com.xiyi.weixin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.xiyi.pojo.CommonJpush;
import com.xiyi.weixin.util.jpush.JPushUtil;
import com.xiyi.weixin.util.jpush.JPushUtil.JPushAudience;
import com.xiyi.weixin.util.jpush.JPushUtil.JPushMessage;
import com.xiyi.weixin.util.jpush.JPushUtil.JPushNotification;
import com.xiyi.weixin.util.jpush.JPushUtil.JPushOptions;
import com.xiyi.weixin.util.jpush.JPushUtil.JPushPlatform;

/**
 * 微信推送接口
 * @author Administrator
 *
 */
public class AppNotify {
	private final static Logger logger = LoggerFactory.getLogger(AppNotify.class);
	@Value("{web.name}")
	private String webName;
	@SuppressWarnings("unchecked")
	public void appNotify(Map<String,Object>userMap,String title,String content) throws Exception{
		 logger.debug("开始通知APP手机用户");
		 CommonJpush commonJpush = new CommonJpush();

		 commonJpush.setPush_type("notification");
		 commonJpush.setTitle(title);
		 commonJpush.setContent(content);
		 commonJpush.setExtras(JsonUtils.objectToJson(userMap));
		 String message=null;
		 String notification=null;
		 String audience=null;
		
		//封装推送对象
		List<String> jpush_ids=new ArrayList<String>();
		jpush_ids.add(userMap.get("device_token").toString());
		commonJpush.setReceiver(userMap.get("id").toString());
		audience=JPushAudience.addPushObject(JPushAudience.REGISTRATION_ID, jpush_ids);
		//封装推送消息
		if(!StringUtils.isBlank(commonJpush.getPush_type())&&commonJpush.getPush_type().equals("message")){
			message = JPushMessage.message(commonJpush.getContent(), commonJpush.getTitle(), "news", userMap);
		}else{
			notification = JPushNotification.multipleNotification(
					JPushNotification.android(commonJpush.getContent(), commonJpush.getTitle(), 101, userMap),
					JPushNotification.ios(commonJpush.getContent(), "happy","1", 1, null, userMap)
			);
		}
		//封装推送平台 android和IOS
		String platform = JPushPlatform.multiple(JPushPlatform.ANDROID, JPushPlatform.IOS);
			String resultStr = "";
			try {
				String options = JPushOptions.options("", "", "", "False", "");
				resultStr = JPushUtil.pushMsg(platform, audience, notification,message,options);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Map<String, Object> resultStrMap = JsonUtils.jsonToObject(resultStr, Map.class);
			//封装实体数据
			commonJpush.setPush_rtn(resultStr);
			commonJpush.setIs_read("y");
			commonJpush.setSender(webName);
			commonJpush.setSender_type("1");
			
			if ("0".equals(resultStrMap.get("sendno")+"")) {
				logger.info("推送成功");
				commonJpush.setStatus("成功");
			}else{
				logger.error("推送失败");
				commonJpush.setStatus("失败");
			}
	}
}
