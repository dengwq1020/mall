package com.xiyi.weixin.base;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiyi.constants.CookieConstants;
import com.xiyi.constants.SysConstants;
import com.xiyi.pojo.XyUser;
import com.xiyi.weixin.util.RedisUtils;
import com.xiyi.weixin.util.StringUtils;






public class BaseActionController extends MultiActionController {
	private static final Logger logger = LoggerFactory.getLogger(BaseActionController.class);
    

	
    protected void doResponse(HttpServletResponse response, String jsonStr) {
        try {
            response.getWriter().print(jsonStr);
        } catch (Exception e) {
        	logger.error("doResponse error",e);
            e.printStackTrace();
        }
    }

    public void outResult(HttpServletResponse response, JSONObject jsonObj) {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(jsonObj.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
        	logger.error("outResult error",e);
            e.printStackTrace();
        }       
    }

    public void outResult(HttpServletResponse response, JSONArray jsonObj) {
        // response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(jsonObj.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
        	logger.error("outResult error",e);
            e.printStackTrace();
        }        
    }

    /**
     *
     * 方法描述:获取请求中的参数转为MAP对象
     *
     * @param
     */
    protected Map<String, Object> getMapParameter(HttpServletRequest request) {
        Enumeration<String> it = request.getParameterNames();
        Map<String, Object> re = new HashMap<String, Object>();
        while (it.hasMoreElements()) {
            String key = it.nextElement();
            Object value = request.getParameter(key);
            re.put(key, value);
            if (logger.isDebugEnabled()) {
                logger.debug("请求获得参数");
                logger.debug("key=" + key + ",value=" + value);
            }
        }
        return re;
    }
    
    protected String getCookieValue(HttpServletRequest request, String key) {
        String value = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    value = cookie.getValue();
                    break;
                }
            }
        }

        return value;
    }
    
    protected void removeCookie(HttpServletRequest request,HttpServletResponse response,String key) throws Exception{
    	Cookie[] cookies = request.getCookies();
    	 if (cookies != null) {
             for (Cookie cookie : cookies) {
                 if (key.equals(cookie.getName())) {
                	 logger.debug(cookie.getName());
                	 cookie.setPath("/");
                     cookie.setMaxAge(0);
                     response.addCookie(cookie);
                     break;
                 }
             }
         }
    }

	
	 /** 基于@ExceptionHandler异常处理 */  
    @ExceptionHandler  
    public ModelAndView handleException(HttpServletRequest request, Exception ex) { 
    	String uriString=request.getServletPath();
    	logger.error("Action Exception {}",uriString,ex);
    	ex.printStackTrace();
        request.setAttribute("ex", ex);        
        return new ModelAndView("error/500");
    } 
    
    protected String getOpenid(HttpServletRequest request){
	    try {
	    	String openid=getCookieValue2(request, CookieConstants.Wx_CookieNameForOpenid);
	   		
	   		if(StringUtils.isNull(openid)){
	   			openid=(String)request.getAttribute(CookieConstants.Wx_CookieNameForOpenid);
	    	}
//	   		return openid;
		} catch (Exception e) {
			logger.error("getOpenid error",e);
		}
	  return "oTWcYwwfV2ZFjN5D8xDc3tQZWlhI";
//    	return null;
    }
    protected String getCookieValue2(HttpServletRequest request, String key) {
        String value = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    value = cookie.getValue();
                    break;
                }
            }
        }

        return value;
    }
    
    /**
     * 方法描述: 获取当前微信绑定用户信息
     * @param
     * @return
     */
	protected XyUser wxGetUserBind(HttpServletRequest request){
    	//通过phone查询用户信息
		XyUser user = null;
		try {
			//从cookie中得到用户信息
			String openid = getOpenid(request);
			if(StringUtils.isNull(openid)){
				return null;
			}			
			user =  RedisUtils.getBean(SysConstants.handleStr(SysConstants.wx_userBind, openid), XyUser.class);		
			if(user==null || user.getLogin_status()==0){
				return null;
			}		
		} 
		catch (Exception e1) {
			logger.error("get user details error", e1);
		}
		return user;
    }
	
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	
}
