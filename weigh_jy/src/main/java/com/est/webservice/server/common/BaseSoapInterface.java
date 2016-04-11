package com.est.webservice.server.common;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

public abstract class BaseSoapInterface {
	@Resource
	protected WebServiceContext context;
	
	protected javax.servlet.http.HttpServletRequest getRequest() {
		return (javax.servlet.http.HttpServletRequest)context.getMessageContext().get(MessageContext.SERVLET_REQUEST);
	}
	
	protected javax.servlet.http.HttpServletResponse getResponse() {
		return (javax.servlet.http.HttpServletResponse)context.getMessageContext().get(MessageContext.SERVLET_RESPONSE);
	}
}
