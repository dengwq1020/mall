package com.xiyi.weixin.filter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xiyi.constants.CookieConstants;
import com.xiyi.constants.SysConstants;
import com.xiyi.pojo.Result;
import com.xiyi.pojo.XyUser;
import com.xiyi.util.config.WebAppConfig;
import com.xiyi.weixin.util.CookieUtils;
import com.xiyi.weixin.util.JsonUtils;
import com.xiyi.weixin.util.RedisUtils;
import com.xiyi.weixin.util.StringUtils;
import com.xiyi.weixin.util.WebUtils;


public class WxAuthFilter implements HandlerInterceptor{
	private static final Logger logger =  LoggerFactory.getLogger(WxAuthFilter.class);
	private static Element superElement ;

	public synchronized void init(HttpServletRequest request) throws DocumentException{
		if(superElement==null){
			String path=request.getServletContext().getRealPath("/")+"WEB-INF/etc/filter/WxFilterConfig.xml";
			superElement = new SAXReader().read(path).getRootElement();
		}
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,	Object handler) throws Exception {
		try {	        
	        response.setCharacterEncoding("UTF-8");
	      //在白名单中不过滤
	        if(isDoFilter(request)){
	            return true;
	        }
	        
	        //获取参数
	        String openid =CookieUtils.getCookieValue(request, CookieConstants.Wx_CookieNameForOpenid);
	        logger.info("user openid:{}",openid);
	        //未查询到cookie，返回重新登录页面
	        if(openid==null){	        	
	            returError(request,response,handler,2);
	            return false;
	        }
	        
	        XyUser user = RedisUtils.getBean(SysConstants.handleStr(SysConstants.wx_userBind, openid), XyUser.class);	       
	        //如果uid是数字
	        if(user!=null && user.getLogin_status()==1){
	        	logger.info("已登陆用户");
	        	return true;
	        }       
	        logger.info("未登陆用户");
	        returError(request,response,handler,2);
			return false;
		 }catch (Exception e){
	            logger.error("preHandle error", e);
	            returError(request,response,handler,1);
	     }
		return false;
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

    public void returError(HttpServletRequest request,HttpServletResponse response,Object handler,int  resultCode){
    	logger.info("未登陆请求过滤:"+request.getServletPath());
    	PrintWriter writer=null;
    	try {
            writer = response.getWriter();           

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            
            if(WebUtils.isAjax(handlerMethod)){
            	Result result=new Result();
                result.setCode(resultCode);        
                       
                writer.print(JsonUtils.objectToJson(result));
            }else{
            	String url;
            	if(resultCode==2){
            		url=WebAppConfig.getProperty("web.url") +"/mobile/user/login";
            	}else{
            		url=WebAppConfig.getProperty("web.url") +"/error/500";
            	}
            	response.sendRedirect(url);             	
            }  
            return;
        }catch (Exception e){
            logger.error("returError error", e);
        }finally {
            if(writer!=null){
                writer.close();
            }

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
}
