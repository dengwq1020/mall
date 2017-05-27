package com.xiyi.weixin.filter;

import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.github.sd4324530.fastweixin.api.OauthAPI;
import com.github.sd4324530.fastweixin.api.enums.OauthScope;
import com.github.sd4324530.fastweixin.api.response.OauthGetTokenResponse;
import com.xiyi.constants.CookieConstants;
import com.xiyi.util.config.WebAppConfig;
import com.xiyi.weixin.util.CookieUtils;
import com.xiyi.weixin.util.SpringConfigTool;
import com.xiyi.weixin.util.StringUtils;
import com.xiyi.weixin.util.WebUtils;



public class WeiXinOauthFilter implements HandlerInterceptor {
	private static final Logger logger =  LoggerFactory.getLogger(WeiXinOauthFilter.class);
	private static Element superElement ;
	
	private OauthAPI oauthAPI=SpringConfigTool.getBean(OauthAPI.class);
		
	public synchronized void init(HttpServletRequest request) throws DocumentException{
		if(superElement==null){
			String path=request.getServletContext().getRealPath("/")+"WEB-INF/etc/filter/WeiXinOauthConfig.xml";
			superElement = new SAXReader().read(path).getRootElement();
		}
	}
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,	Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;        
        response.setCharacterEncoding("UTF-8");

      //在白名单中不过滤
        if(isDoFilter(request)){
            return true;
        }
        
        String openid =CookieUtils.getCookieValue(request, CookieConstants.Wx_CookieNameForOpenid);
        
        //查询到cookie
        if(!StringUtils.isNull(openid)){
        	return true;        	
        }
        
        String code=request.getParameter("code");
        
        if(StringUtils.isNull(code) && !WebUtils.isAjax(handlerMethod)){        	
        	logger.info("code is null");
        	auth(request,response);
        	return false;
        }
        
        OauthGetTokenResponse returnData = oauthAPI.getToken(code);
	   	openid = returnData.getOpenid();
	   	if(!StringUtils.isNull(openid)){
	   		
		   	request.setAttribute(CookieConstants.Wx_CookieNameForOpenid, openid);
		   	
		   	Cookie cookie=new Cookie(CookieConstants.Wx_CookieNameForOpenid, openid);
		   	cookie.setMaxAge(60*60*24*30);
		   	cookie.setPath("/");
		   	response.addCookie(cookie);
		   	
			return true;
	   	}else{
	   		return false;
	   	}
	}
	
	public boolean isDoFilter(HttpServletRequest request) {
        try {
            if(superElement==null) {
                 init(request);
            }
            Element whiteList = superElement.element("whiteList");

            if (whiteList != null) {
                Iterator<Element> whiteIt = whiteList.elementIterator();
                logger.debug("过滤链接:"+request.getServletPath());
                while (whiteIt.hasNext()) {
                    Element white = (Element) whiteIt.next();
                    String whiteUrl = white.getText();
                    // 按目录路径匹配白名单
                    if (!StringUtils.isNull(whiteUrl) && request.getServletPath().startsWith(whiteUrl)) {       
                    	logger.debug("{} 白名单连接",request.getServletPath());
                        return true;
                    }
                }
                return false;
            } else {
                logger.error("安全过滤配置文件中没有 whiteList 属性");
                return false;
            }
        } catch (DocumentException e) {
            logger.error("过滤白名单链接出错" , e);
        }
        return false;
    } 

    public void auth(HttpServletRequest request,HttpServletResponse response){
    	String returnUrl=WebAppConfig.getProperty("web.url") + request.getServletPath();
    	try {    		
        	String urlString=oauthAPI.getOauthPageUrl(returnUrl,OauthScope.SNSAPI_BASE,"filter");
        	response.sendRedirect(urlString);
		} catch (Exception e) {
			logger.error("error",e);
		}
    	
    }
}
