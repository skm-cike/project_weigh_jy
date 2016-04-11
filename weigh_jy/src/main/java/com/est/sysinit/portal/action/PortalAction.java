package com.est.sysinit.portal.action;

import java.util.Date;

import javax.servlet.http.HttpSession;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.DateUtil;
import com.est.common.ext.util.encrypt.Des;
import com.est.sysinit.sysauthority.service.ISysUserModuleService;
import com.est.sysinit.sysuser.service.ISysUserService;
import com.est.sysinit.sysuser.vo.SysUser;


/**
 *@desc 门户页面portal转向
 *@author jingpj
 *@date Apr 20, 2010
 *@path com.est.common.business.action.PortalAction
 *@corporation Enstrong S&T
 */
@SuppressWarnings("serial")
public class PortalAction extends BaseAction {


	@Override
	public Object getModel() {
		return null;
	}

	/**
	 *@desc 跳转到portal的显示窗口
	 *@date Apr 20, 2010
	 *@author jingpj
	 *@return
	 */
	public String fwdPortalMain(){
		return toJSP("portalmain");
	}
	
	
	public String fwdPortal(){
		
		
		String login = (String) req.getSession().getAttribute("LOGINID");// 验证条件：用户名
		String timestamp = (String) req.getSession().getAttribute("Timestamp");// 时间戳
		
		
		//如果单点登录，进行初始化
		if(login!=null && timestamp!=null) {
			SearchCondition condition = new SearchCondition();
			condition.set("login", login);
			condition.set("timestamp", timestamp);
			
			Des des = (Des) getBean("DES");
			
			try {
				login = des.decode(login);
				timestamp = des.decode(timestamp);
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			Date datestamp = DateUtil.parse(timestamp, "yyyy-MM-dd HH:mm");
			try{
				if(new Date().getTime() - datestamp.getTime() > 1000*60*15) {
					return toSTR("该次登录已过期！");
				}
			} catch(Exception ex) {
				return toSTR("该次登录已过期！");
			}
			
			ISysUserService userService = (ISysUserService) getBean("sysUserService");
			SysUser sysUser = userService.getUserByLogin(login);
			if (sysUser != null) {// 用户验证成功
				HttpSession session = req.getSession();
				session.setAttribute("loginUser", sysUser);// 保存用户登陆信息
				if (sysUser.getSysDept() != null) {
					sysUser.getSysDept().getDeptname();
				}
				try {
					// 保存用户菜单
					ISysUserModuleService service = (ISysUserModuleService) getBean("userModuleService");
					String menuString = service.getUserModuleMenu(sysUser);
					session.setAttribute("menu", menuString);
				} catch (Exception e) {
					session.setAttribute("menu", "[]");
					e.printStackTrace();
				}
			} else {// 用户验证失败
				return toSTR("没有该用户！");
			}
			
		}
		
		return toJSP("portal");
	}
	
	
	
} 