package com.est.common.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @desc 检查用户登录拦截器
 * @author jingpj
 * @date Aug 27, 2009
 * @path com.est.common.interceptor.ChkLoginInterceptor
 * @corporation Enstrong S&T
 */
@SuppressWarnings("unchecked")
public class ExceptionInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -2266836690857007733L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		try {
			return invocation.invoke();
		} catch (Exception ex) {
			invocation.getInvocationContext().getValueStack().set("_EXCEPTION", ex);
			return "exception";
			/*if(ex instanceof BaseBussinessException) {
				return "exception";
			} else {
				return "exception";
			}*/
		}
	}
}

