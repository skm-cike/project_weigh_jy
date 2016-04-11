package com.est.sysinit.sysuser.action;

import javax.servlet.http.HttpSession;

import com.est.common.base.BaseAction;
import com.est.sysinit.sysuser.service.ISysUserModifyService;
import com.est.sysinit.sysuser.service.ISysUserService;
import com.est.sysinit.sysuser.vo.SysUser;

public class SysUserModifyAction extends BaseAction{

	SysUser sysUser = new SysUser();
	
	private SysUser user = new SysUser();
	
	public void setUser(SysUser user) {
		this.user = user;
	}

	@Override
	public Object getModel() {
		return user;
	}
	/**
	 * 
	 *@desc 跳转到修改用户信息的页面 
	 *@date Oct 14, 2009
	 *@author yzw
	 *@return
	 */
	public String fwdSysuserModify()
	{
		return toJSP("sysusermodify");
	}
	
	/**
	 * 
	 *@desc 得到用户信息 
	 *@date Oct 14, 2009
	 *@author yzw
	 *@return
	 */
	public String getUser()
	{
		SysUser user = (SysUser) req.getSession().getAttribute("loginUser");
		return toJSON(user, "{success: true, data:", "}");
	}
	
	/**
	 * 
	 *@desc 保存用户信息 
	 *@date Oct 14, 2009
	 *@author yzw
	 *@return
	 */
	public String savUser()
	{
		ISysUserModifyService userModifyService = (ISysUserModifyService)getBean("sysUserModifyService");
		try
		{	
			
			ISysUserService userService = (ISysUserService) getBean("sysUserService");
			
			SysUser sysUser = userService.getUserById(user.getUserid());
			
			user.setFp1(sysUser.getFp1());
			user.setFp2(sysUser.getFp2());
			user.setFp3(sysUser.getFp3());
			
			SysUser _user = userModifyService.savUser(user);
			req.getSession().setAttribute("loginUser",_user);
			return toJSON(user, "{success: true, data:", "}");
		}catch(Exception e)
		{
			return toSTR("{success:false}");
		}
	}
}
