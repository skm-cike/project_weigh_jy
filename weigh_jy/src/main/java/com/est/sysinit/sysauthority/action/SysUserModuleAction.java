package com.est.sysinit.sysauthority.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.est.common.annotation.CalTodoTaskAnnotation;
import com.est.common.base.BaseAction;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysauthority.service.ISysUserModuleService;
import com.est.sysinit.sysauthority.vo.SysUsermodule;
import com.est.sysinit.sysdept.service.ISysDeptService;
import com.est.sysinit.sysdept.vo.SysDept;
import com.est.sysinit.sysmodule.vo.SysModule;
import com.est.sysinit.sysuser.service.ISysUserService;
import com.est.sysinit.sysuser.vo.SysUser;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 27, 2009
 * @path com.est.sysinit.sysauthority.action
 * @name com.est.sysinit.sysauthority.action.SysDeptModuleAction
 * @description 系统模块->权限管理->人员模块授权Action
 */
public class SysUserModuleAction extends BaseAction{
	
	
//	private SysModule module = new SysModule();
//	
//	public void setModule(SysModule module) {
//		this.module = module;
//	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public Object getModel() {
		return null;
	}
	
	
	/**
	 * 
	 *@description 得到用户权限分配列表
	 *@date May 27, 2009
	 *@author jingpj
	 *1:41:33 PM
	 *@return
	 */
	public String getUserModuleList(){
		ISysUserModuleService service = (ISysUserModuleService) getBean("userModuleService");
		ISysUserService userService = (ISysUserService) getBean("sysUserService");
		String userId = req.getParameter("userId");
		SysUser usr = userService.getUserById(StringUtil.parseLong(userId));
		if(usr == null || usr.getSysDept()== null) {
			return toSTR("");
		} else {
			//当前用户部门
			Long deptId = usr.getSysDept()==null ? 0L : usr.getSysDept().getDeptid();
			//当前用户的第一级部门
			ISysDeptService deptService = (ISysDeptService) getBean("sysDeptService");
			SysDept firstLevelDept = deptService.getDeptById(deptId);//.findFirstLevelDept(deptId);
			
			Long firstLevelDeptId = (firstLevelDept == null ? 0L : firstLevelDept.getDeptid());
			String userModuleString = service.getUsermoduleList(usr.getUserid(),firstLevelDeptId);
			return toSTR(userModuleString);
		}
	}
	
	/**
	 * 
	 *@description 保存部门权限分配列表
	 *@date May 30, 2009
	 *@author jingpj
	 *@return
	 */
	public String savUserModuleList(){
		//String deptId =  req.getParameter("deptId");
		
		String userId = req.getParameter("userId");
		
		ISysUserService userService = (ISysUserService) getBean("sysUserService");
		SysUser user = userService.getUserById(StringUtil.parseLong(userId));
		ISysDeptService deptService = (ISysDeptService) getBean("sysDeptService");
		SysDept dept = deptService.findFirstLevelDept(user.getSysDept().getDeptid());
		
		String data =  req.getParameter("data");
		ISysUserModuleService userModuleservice = (ISysUserModuleService) getBean("userModuleService");
		try {
			userModuleservice.saveUserModuleList(user.getUserid(), dept.getDeptid(), data);
		} catch (Exception e) {
			e.printStackTrace();
			toERROR("保存失败:"+e.getMessage());
		}
		return toSTR("{success:true}");
	}
	
	/**
	 * 
	 *@description 组装用户可使用模块下拉菜单
	 *@date Jun 2, 2009
	 *@author jingpj
	 *3:50:33 PM
	 *@return
	 */
	public String getUserModuleMenu(){
		SysUser user =getCurrentUser();
		ISysUserModuleService service = (ISysUserModuleService) getBean("userModuleService");
		try {
			String menuString = service.getUserModuleMenu(user);
			return toSTR(menuString);
		} catch (Exception e) {
			e.printStackTrace();
			return toERROR("读取权限失败！");
		}
	}
	
	/**
	 *@desc 得到模块的子模块按钮
	 *@date Dec 3, 2009
	 *@author jingpj
	 *@return
	 */
	public String getModuleButtons() {
		SysUser user =getCurrentUser();
		Long moduleId = StringUtil.parseLong(req.getParameter("_moduleId"));
		ISysUserModuleService service = (ISysUserModuleService) getBean("userModuleService");
		List<SysUsermodule> lst = service.getUserModuleButtons(user, moduleId);
		
		StringBuilder buf = new StringBuilder(500);
		buf.append("[");
		for(int i=0;i<lst.size();i++) {
			SysUsermodule usermodule = lst.get(i);
			if(i!=0){
				buf.append(",");
			}
			buf.append("{");
			buf.append("text:'"+usermodule.getSysModule().getModulename());
			buf.append("");
			buf.append(getTotalByModule(usermodule.getSysModule()));
			buf.append("'");
			buf.append(",id:'"+usermodule.getSysModule().getModulename()+"'");
			buf.append(",handler:");
			String clkHandler = service.getFrameBtnClickEvent(usermodule);
			buf.append(clkHandler);
			buf.append("}");
			
		}
		buf.append("]");
		return toSTR("{success:true,data:"+buf.toString()+"}");
		
		
	}
	
	
	
	/**
	 * 转向到用户权限管理页面 
	 * @return
	 */
	public String fwdUserModule() {
		SysUser user = getCurrentUser();
		if("000".equals(user.getSysDept().getDeptcode())){
			//管理员
			req.setAttribute("rootId", 0);
			req.setAttribute("rootText", "部门列表");
		} else {
			//其他部门
			req.setAttribute("rootId", user.getSysDept().getDeptid());
			req.setAttribute("rootText", user.getSysDept().getDeptname());
			
		}
		return toJSP("usermodule");
	}
	
	/**
	 * @desc 得到模块按钮的待办工作数量
	 * @date Oct 27, 2009
	 * @author jingpj
	 * @param module
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getTotalByModule(SysModule module) {
		String url = module.getUrl();
		url = url.split("\\?")[0];
		String[] arrPath = url.split("/");
		StringBuilder path = new StringBuilder(200);
		for (int i = arrPath.length - 4; i < arrPath.length - 1; i++) {
			path.append(".");
			String tmp = arrPath[i];
			if (i == arrPath.length - 2) {
				path.append("action.");
				path.append(tmp);
				path.append("Action");
			} else {
				path.append(tmp);
			}

		}
		String classname = "com.est" + path.toString();
		String fwdmethodname = url.substring(url.lastIndexOf("/")+1);
		String calmethodname = null;
		
		try {
			Class actionClass =  Class.forName(classname);
			Method fwdmethod = actionClass.getMethod(fwdmethodname);
			if(fwdmethod.isAnnotationPresent(CalTodoTaskAnnotation.class)){
				CalTodoTaskAnnotation annotation = fwdmethod.getAnnotation(CalTodoTaskAnnotation.class);
				calmethodname = annotation.value();
			} else {
				calmethodname = "getTotal";
			}
			
			BaseAction obj = (BaseAction) actionClass.newInstance();
			obj.setServletContext(context);
			obj.setServletRequest(req);
			obj.setServletResponse(res);
			Method method = actionClass.getMethod(calmethodname);
			
			String result = (String) method.invoke(obj);
			
			return result;
			
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
		return "";
	}
}
