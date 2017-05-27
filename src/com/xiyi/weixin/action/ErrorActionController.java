package com.xiyi.weixin.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/error")
public class ErrorActionController {
	
    @RequestMapping(value="/error/500", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) {
    	return new ModelAndView("error/500");
    }
}
