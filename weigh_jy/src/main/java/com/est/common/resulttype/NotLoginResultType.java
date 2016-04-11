package com.est.common.resulttype;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

public class NotLoginResultType extends StrutsResultSupport {

	private static final long serialVersionUID = 6355603611137025312L;

	protected static final Log log = LogFactory.getLog(NotLoginResultType.class);

	protected final String CONTENTTYPE = "application/json";
	protected final String CHARSET = "utf-8";

	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation)
			throws Exception {

		System.out.println(finalLocation);
		ActionContext context = invocation.getInvocationContext();
		HttpServletResponse response = (HttpServletResponse) context.get(HTTP_RESPONSE);
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.setContentType("text/html; charset=utf-8");
		
		PrintWriter writer = response.getWriter();
		
		try {
			writer.write("{isLogin:false}");
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}

	}

}
