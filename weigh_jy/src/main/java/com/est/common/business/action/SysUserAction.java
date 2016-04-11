package com.est.common.business.action;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.sysuser.service.ISysUserService;
import com.est.sysinit.sysuser.vo.SysUser;

/**
 * @desc系统用户相关公用功能Actioin
 * @author hebo
 * @date Jun 29, 2009
 * @path com.est.common.business.action.SysUserAction
 * @corporation Enstrong S&T
 */
@SuppressWarnings("serial")
public class SysUserAction extends BaseAction {

	// model object
	private SysUser user = new SysUser();

	public void setUser(SysUser user) {
		this.user = user;
	}

	@Override
	public Object getModel() {
		return user;
	}

	/**
	 * @description 验证用户(用于运行日志交接班人的签名验证)
	 * @date June 1,2009
	 * @author hebo 9:54 AM
	 * @return
	 */
	public String verfyUser() {
		String login = req.getParameter("login");// 验证条件：用户名
		String password = req.getParameter("password");// 验证条件：密码

		SearchCondition condition = new SearchCondition();
		condition.set("login", login);
		condition.set("password", password);
		ISysUserService userService = (ISysUserService) getBean("sysUserService");
		SysUser sysUser = userService.vefUser(condition);
		if (sysUser != null) {// 用户验证成功
			return toJSON(sysUser, "{success:true,data:", "}"); // modified by
			// jingpj
			// 返回用户的详细信息
		} else {// 用户验证失败
			return toSTR("{success:false}");
		}
	}
	/**
	 * 
	 *@desc 用户指纹验证
	 *@date Nov 25, 2012
	 *@author heb
	 *@return
	 */
	public String verfyUserFp(){
		
//		String username = req.getParameter("username");// 验证条件：用户姓名
//		String fp = req.getParameter("fp");// 验证条件：指纹特征码
//		
//		try{
//			
//			ISysUserService userService = (ISysUserService) getBean("sysUserService");
//			if((username!=null && !"".equals(username)) && (fp!=null && !"".equals(fp))){
//				SysUser sysUser = userService.verfyUserFp(username, fp);
//				if (sysUser != null) {// 用户验证成功
//					return toJSON(sysUser, "{success:true,data:", "}"); // modified by
//				} else {// 用户验证失败
//					return toSTR("{success:false}");
//				}
//			}
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return toSTR("{success:false}");
		return null;
	}
}