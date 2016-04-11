package com.est.common.ext.util;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ContextUtil {
	private static ServletContext context;
	

	public static Object getBean(String name) {
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		return ctx.getBean(name);
	}
	
	
	
	public static ServletContext getContext() {
		return context;
	}

	public static void setContext(ServletContext context) {
		ContextUtil.context = context;
	}
	

}
