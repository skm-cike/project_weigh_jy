package com.est.common.resulttype;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

public class ExceptionResultType extends StrutsResultSupport {

	protected final Log log = LogFactory.getLog(ExceptionResultType.class);

	private static final long serialVersionUID = -2659052470244868842L;

	private Exception exception;

	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}



	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation)
			throws Exception {
		exception = (Exception) invocation.getStack().findValue("_EXCEPTION");
		//exception.printStackTrace();
		ActionContext context = invocation.getInvocationContext();
		HttpServletResponse response = (HttpServletResponse) context
				.get(HTTP_RESPONSE);
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		StringBuilder builder = new StringBuilder();
		builder.append("{success:false,error:'");
		builder.append(exception.getMessage());
		builder.append("'}");
		try {
			writer.write(builder.toString());
		} finally{
			if(writer!=null){
				writer.flush();
				writer.close();
			}
		}
	}

}
