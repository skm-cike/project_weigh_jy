package com.est.sysinit.sysmodule.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.TreeObject;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysmodule.service.ISysModuleService;
import com.est.sysinit.sysmodule.service.ISysModulefieldService;
import com.est.sysinit.sysmodule.vo.SysModule;
import com.est.sysinit.sysmodule.vo.SysModulefield;
/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 5, 2009
 * @path com.est.sysinit.sysdept.action
 * @name com.est.sysinit.sysdept.action.SysDeptAction
 * @description 系统模块->模块管理->模块定义Action
 */

@SuppressWarnings("serial")
public class SysModuleAction extends BaseAction{
	
	private SysModule module = new SysModule();
	
	

	public void setModule(SysModule module) {
		this.module = module;
	}

	@Override
	public Object getModel() {
		return module;
	}
	
	/**
	 * 
	 *@description 得到部门树，返回结果为参数部门的所有子部门
	 *@date May 5, 2009
	 *@author jingpj
	 *3:50:59 PM
	 *@return
	 */
	public String getModuleTree(){
		ISysModuleService sysModuleService  = (ISysModuleService) getBean("sysModuleService");
		String node = req.getParameter("node"); 
		List<SysModule> moduleList = sysModuleService.getModuleTree(StringUtil.parseLong(node));
		List<TreeObject> treeList = this.parseModuleTreeList(moduleList);
		
		if(treeList.size()>0) {
			return toJSON(treeList);
		} else {
			return toSTR("[]");
		}
	}
	
	/**
	 * 
	 *@description 删除模块
	 *@date May 5, 2009
	 *@author jingpj
	 *3:52:29 PM
	 *@return
	 */
	public String delModule(){
		ISysModuleService sysModuleService  = (ISysModuleService) getBean("sysModuleService");
		try{
			sysModuleService.delModule(module.getModuleid());
			return toSTR("{success:true}");
		} catch (Exception ex) {
			return toERROR("删除错误！");
		}
	}
	
	/**
	 * 
	 *@description 保存模块
	 *@date May 5, 2009
	 *@author jingpj
	 *3:52:48 PM
	 *@return
	 */
	public String savModule(){
		ISysModuleService sysModuleService  = (ISysModuleService) getBean("sysModuleService");

		try{
			sysModuleService.savModule(module);
			//return toSTR("{success:true}");
			return toJSON(module,"{success:true,data:","}");
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			return toERROR("删除错误："+ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			return toERROR("删除错误："+ex.getMessage());
		}
	}
	
	/**
	 * 
	 *@description 通过id得到模块
	 *@date May 22, 2009
	 *@author jingpj
	 *10:30:21 AM
	 *@return
	 */
	public String getModule(){
		ISysModuleService sysModuleService  = (ISysModuleService) getBean("sysModuleService");
		Long moduleId = module.getModuleid();
		SysModule module = sysModuleService.getModuleById(moduleId);
		return toJSON(module,"{success:true,data:","}");
	}
	
	/**
	 * 
	 *@description 通过id得到模块
	 *@date May 22, 2009
	 *@author jingpj
	 *10:30:21 AM
	 *@return
	 */
	public String getModuleByModulename(){
		ISysModuleService sysModuleService  = (ISysModuleService) getBean("sysModuleService");
		String modulename = module.getModulename();
		SysModule module = sysModuleService.getModuleByModulename(modulename);
		return toJSON(module,"{success:true,data:","}");
	}
	
	/**
	 * 
	 *@description 将模块列表转换成Ext tree json 列表
	 *@date May 5, 2009
	 *@author jingpj
	 *3:53:05 PM
	 *@param moduleList
	 *@return
	 */
	private List<TreeObject> parseModuleTreeList(List<SysModule> moduleList){
		 List<TreeObject> treeList = new ArrayList<TreeObject>();
		 for(SysModule module : moduleList) {
			 TreeObject treeobj = new TreeObject();
			 treeobj.setId(String.valueOf(module.getModuleid()));
			 treeobj.setText(module.getModulename());
			 treeobj.setQtip(module.getModulename());
			 treeList.add(treeobj);
		 }
		 return treeList;
	}
	
	
	/**
	 * 
	 *@description 分页查询模块列表
	 *@date May 5, 2009
	 *@author jingpj
	 *4:05:06 PM
	 *@return
	 */
	public String getModuleList(){
		String moduleName = req.getParameter("modulename"); 		//查询条件:模块名
		String parentModuleId = req.getParameter("parentModuleId");		//查询条件：父模块id
		
		SearchCondition condition = new SearchCondition();
		condition.set("moduleName", moduleName);
		condition.set("parentModuleId", StringUtil.parseLong(parentModuleId));
		
		Page page = getPage();
		
		ISysModuleService moduleService = (ISysModuleService) getBean("sysModuleService");
		Result<SysModule> result = null;
		try{
			result = moduleService.getModuleList(page, condition);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		//log.debug("result========>" + result);
		return toJSON(result);
	}
	
	
	/**
	 * 
	 *@description 得到所有模块，combo使用
	 *@date May 22, 2009
	 *@author jingpj
	 *11:25:31 AM
	 *@return 
	 */
	public String getAllModule(){
		ISysModuleService moduleService = (ISysModuleService) getBean("sysModuleService");
		List<SysModule> moduleList = moduleService.getAllModule();
		if(moduleList.size()>0) {
			return toJSON(moduleList ,"{success:true,rows:","}");
		} else {
			return toSTR("{success:true,rows:[]}");
		}
	}
	
	/**
	 * 转向到用户管理页面 
	 * @return
	 */
	public String fwdModule() {
		return toJSP("module");
	}
	
	
	/**
	 * 
	 *@desc 根据模块查询字段
	 *@date Feb 4, 2010
	 *@author jingpj
	 *@return
	 */
	public String getFieldListByModuleId(){
		
		String strModuleId = req.getParameter("moduleid");
		Long moduleId = StringUtil.parseLong(strModuleId);
		
		ISysModulefieldService service = (ISysModulefieldService) getBean("sysModulefieldService");
		Result<SysModulefield> result = null;
		try{
			result = service.getFieldListByModuleId(getPage(), moduleId);
			return toJSON(result);
		}catch(Exception ex){
			ex.printStackTrace();
			return toSTR("{success:false,error:'读取错误'}");
		}
		
	}
	
	
	/**
	 *@desc 保存模块字段 
	 *@date Feb 4, 2010
	 *@author jingpj
	 *@return
	 */
	public String savSysModulefieldList(){
	
		ISysModulefieldService service = (ISysModulefieldService) getBean("sysModulefieldService");
		String data = req.getParameter("data");
		try {
			data = java.net.URLDecoder.decode(data,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		EditableGridDataHelper editGridData = EditableGridDataHelper.data2bean(data, SysModulefield.class);
		service.savSysModulefileList(editGridData);
		return toSTR("{success:true}");
	}
}
