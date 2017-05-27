package com.xiyi.weixin.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;



public class SpringConfigTool implements ApplicationContextAware {
	
	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		
	    SpringConfigTool.context=context;
		
	}

	public static ApplicationContext getContext() {
		return context;
	}
	
	public static Object getBean(String beanName) {
		
		return context.getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> requireType) {
	    return (T)context.getBean(requireType);
	}
	
}
