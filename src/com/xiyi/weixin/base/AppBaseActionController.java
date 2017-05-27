package com.xiyi.weixin.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiyi.constants.CacheConstants;
import com.xiyi.constants.CookieConstants;
import com.xiyi.pojo.XyUser;
import com.xiyi.weixin.util.DESUtil;
import com.xiyi.weixin.util.DESUtilForIos;
import com.xiyi.weixin.util.JsonUtils;
import com.xiyi.weixin.util.RedisUtils;

public class AppBaseActionController extends BaseActionController{
	public static final Logger logger = LoggerFactory.getLogger(AppBaseActionController.class);

	public XyUser getUser(HttpServletRequest request){
    	try{
        	String userJson= DESUtil.decode(DESUtilForIos.KEY,getCookieValue(request, CookieConstants.UID));
        	if(StringUtils.isBlank(userJson)){
        		return null;
        	}
        	String uid = JsonUtils.jsonToObject(userJson, Map.class).get("uid").toString();
        	logger.debug("getUser uid-->"+ uid);
        	if(StringUtils.isBlank(uid)){
        		return null;
        	}
        	XyUser user=RedisUtils.getBean(CacheConstants.handleStr(CacheConstants.USER_INFO, uid), XyUser.class);
        	logger.debug("user_id--->" +user.getUid());
        	return user;
    	}catch(Exception ex){
    		logger.error("getUser error-->",ex);
    	}    	
    	return null;
    }
}
