package com.est.common.interceptor;

import java.util.ArrayList;
import java.util.List;

import com.est.sysinit.portal.action.PortalAction;
import com.est.sysinit.portal.action.RealtimeIndexAction;
import com.est.sysinit.sysdept.action.SysDeptAction;
import com.est.sysinit.sysfilemanage.action.NormalFileAction;
import com.est.sysinit.sysproperty.action.SysPropertyAction;
import com.est.sysinit.sysuser.action.SysUserAction;
import com.est.sysinit.sysuser.vo.SysUser;
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
public class ChkLoginInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -2266836690857007733L;

	private static List<ChkLoginExclude> excludeList;

	static {
		excludeList = new ArrayList<ChkLoginExclude>();
		
		//用户判断
		excludeList.add(new ChkLoginExclude(SysUserAction.class, "vefUser",ChkLoginExclude.EXCLUDE_REF_METHOD));
		excludeList.add(new ChkLoginExclude(SysUserAction.class, "chkUser",ChkLoginExclude.EXCLUDE_REF_METHOD));
		excludeList.add(new ChkLoginExclude(SysUserAction.class, "ssologin",ChkLoginExclude.EXCLUDE_REF_METHOD));
		excludeList.add(new ChkLoginExclude(SysUserAction.class, "fwdReg",ChkLoginExclude.EXCLUDE_REF_METHOD));
		excludeList.add(new ChkLoginExclude(SysUserAction.class, "cancelUser",ChkLoginExclude.EXCLUDE_REF_METHOD));
		excludeList.add(new ChkLoginExclude(SysUserAction.class, "toINDEX",ChkLoginExclude.EXCLUDE_REF_METHOD));
		
		//部门树
		excludeList.add(new ChkLoginExclude(SysDeptAction.class,"getDepartTree", ChkLoginExclude.EXCLUDE_REF_METHOD));
		
		//签名
		excludeList.add(new ChkLoginExclude(com.est.common.business.action.SysUserAction.class,"verfyUser", ChkLoginExclude.EXCLUDE_REF_METHOD));
		
		//门户
		excludeList.add(new ChkLoginExclude(RealtimeIndexAction.class,"fwdRealtime", ChkLoginExclude.EXCLUDE_ALL_METHOD));
		excludeList.add(new ChkLoginExclude(PortalAction.class,"fwdPortalMain", ChkLoginExclude.EXCLUDE_ALL_METHOD));
		
		//属性选择框
		excludeList.add(new ChkLoginExclude(SysPropertyAction.class,"getPropertiesByCode", ChkLoginExclude.EXCLUDE_REF_METHOD));

        //下载
        excludeList.add(new ChkLoginExclude(NormalFileAction.class, "checkversion",ChkLoginExclude.EXCLUDE_REF_METHOD));
        excludeList.add(new ChkLoginExclude(NormalFileAction.class, "download",ChkLoginExclude.EXCLUDE_REF_METHOD));
	}

	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		SysUser loginUser = (SysUser) invocation.getInvocationContext()
				.getSession().get("loginUser");

		if (loginUser == null) {
			Class action = invocation.getAction().getClass();
			String methodname = invocation.getProxy().getMethod();
			
			for (ChkLoginExclude exclude : ChkLoginInterceptor.excludeList) {
				if(exclude.chkExclude(action, methodname)) {
					return invocation.invoke();
				}
			}
			if (methodname != null && methodname.startsWith("fwd")) {
				return "toLogin";
			} else {
				return "notLogin";
			}
		} else {
			return invocation.invoke();
		}
	}
}

@SuppressWarnings("unchecked")
class ChkLoginExclude {
	public final static int EXCLUDE_ALL_METHOD = 0; // 排除action所有方法
	public final static int EXCLUDE_REF_METHOD = 1; // 排除action中指定方法
	public final static int EXCLUDE_EXCEPT_METHOD = 2; // 排除action中指定方法以外的方法

	private Class action;
	private String methodname;
	private int excludetype;

	public ChkLoginExclude() {

	}

	public ChkLoginExclude(Class action, String methodname, int excludetype) {
		this.action = action;
		this.methodname = methodname;
		this.excludetype = excludetype;
	}
	
	public boolean chkExclude(Class action, String methodname){
		if(action.equals(this.action)) {
			switch (this.getExcludetype()) {
			case ChkLoginExclude.EXCLUDE_ALL_METHOD:
				return true;
			case ChkLoginExclude.EXCLUDE_REF_METHOD:
				if (methodname.equals(this.getMethodname())) {
					return true;
				}
				break;
			case ChkLoginExclude.EXCLUDE_EXCEPT_METHOD:
				if (!methodname.equals(this.getMethodname())) {
					return true;
				}
				break;
			}
			return false;
		} else {
			return false;
		}

		
	}

	public Class getAction() {
		return action;
	}

	public void setAction(Class action) {
		this.action = action;
	}

	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

	public int getExcludetype() {
		return excludetype;
	}

	public void setExcludetype(int excludetype) {
		this.excludetype = excludetype;
	}

}