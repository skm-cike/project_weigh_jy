package com.est.sysinit.sysmodule.service;

import java.io.Serializable;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.sysmodule.vo.SysModule;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 22, 2009
 * @path com.est.sysinit.sysmodule.service
 * @name com.est.sysinit.sysmodule.service.ISysModuleService
 * @description 系统模块配置service接口
 */
public interface ISysModuleService {
	/**
	 * 
	 *@description 得到父模块的子模块列表
	 *@date May 5, 2009
	 *@author jingpj
	 *3:57:01 PM
	 *@param moduleId 父模块id
	 *@return
	 */
	public List<SysModule> getModuleTree(Serializable moduleId);
	
	
	
	/**
	 * 
	 *@description 根据条件，分页查询系统模块列表
	 *@date May 26, 2009
	 *@author jingpj
	 *11:31:28 AM
	 *@param page
	 *@param condition
	 *@return
	 */
	public Result<SysModule> getModuleList(Page page,SearchCondition condition);
	
	/**
	 * 
	 *@description 删除模块
	 *@date May 5, 2009
	 *@author jingpj
	 *3:57:37 PM
	 *@param moduleId  模块id
	 */
	public void delModule(Serializable moduleId);
	
	/**
	 * 
	 *@description 通过id得到模块对象
	 *@date May 5, 2009
	 *@author jingpj
	 *3:57:51 PM
	 *@param moduleId 模块id
	 *@return
	 */
	public SysModule getModuleById(Serializable moduleId);
	
	/**
	 * 
	 *@description 保存模块
	 *@date May 5, 2009
	 *@author jingpj
	 *3:58:23 PM
	 *@param sysModule 被保存模块对象
	 */
	public void savModule(SysModule sysModule) throws Exception ;


	/**
	 * 
	 *@description 得到所有系统模块列表(combo使用)
	 *@date May 26, 2009
	 *@author jingpj
	 *2:32:49 PM
	 *@return
	 */
	public List<SysModule> getAllModule();
	
	/**
	 *@desc 通过模块id得到模块的完整路径(用于上方toolbar显示)
	 *@date Aug 15, 2009
	 *@author jingpj
	 *@param moduleId
	 *@return
	 */
	public String getModuleFullPathById(Serializable moduleId);


	/**
	 *@desc 通过模块名称得到模块
	 *@date Sep 6, 2009
	 *@author jingpj
	 *@param modulename
	 *@return
	 */
	public SysModule getModuleByModulename(String modulename);
	
	
}
