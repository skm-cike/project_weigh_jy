package com.est.sysinit.sysauthority.action;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysauthority.service.ISysDeptModuleService;
import com.est.sysinit.sysmodule.vo.SysModule;
import com.est.sysinit.sysuser.vo.SysUser;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 27, 2009
 * @path com.est.sysinit.sysauthority.action
 * @name com.est.sysinit.sysauthority.action.SysDeptModuleAction
 * @description 系统模块->权限管理->部门模块授权Action
 */
public class SysDeptModuleAction extends BaseAction{
	
//	
//	private SysModule module = new SysModule();
//	
//	public void setModule(SysModule module) {
//		this.module = module;
//	}

	@Override
	public Object getModel() {
		return null;
	}
	
	
	/**
	 * 
	 *@description 得到部门权限分配列表
	 *@date May 27, 2009
	 *@author jingpj
	 *1:41:33 PM
	 *@return
	 */
	public String getDeptModuleList(){
		ISysDeptModuleService service = (ISysDeptModuleService) getBean("deptModuleService");
		//SysUser usr = (SysUser) req.getAttribute("loginUser");
		String deptId = req.getParameter("deptId");
		String deptModuleString = service.getDeptmoduleList(StringUtil.parseLong(deptId));
		return toSTR(deptModuleString);
	}
	
	/**
	 * 
	 *@description 保存部门权限分配列表
	 *@date May 30, 2009
	 *@author jingpj
	 *6:10:30 PM
	 *@return
	 */
	public String savDeptModuleList(){
		String deptId =  req.getParameter("deptId");
		String data =  req.getParameter("data");
		ISysDeptModuleService service = (ISysDeptModuleService) getBean("deptModuleService");
		try{
			service.saveDeptModuleList(StringUtil.parseLong(deptId),data);
			return toSTR("{success:true}");
		} catch (Exception ex){
			return toSTR("{success:false,info:'"+ex.getMessage()+"'}");
		}
	}
	
	
	/**
	 * 转向到部门权限管理页面 
	 * @return
	 */
	public String fwdModule() {
		return toJSP("deptmodule");
	}
}
