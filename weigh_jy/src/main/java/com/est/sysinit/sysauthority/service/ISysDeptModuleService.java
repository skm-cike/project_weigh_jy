package com.est.sysinit.sysauthority.service;

import java.io.Serializable;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.sysauthority.vo.SysDeptmodule;
import com.est.sysinit.sysmodule.vo.SysModule;

/**
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 27, 2009
 * @path com.est.sysinit.sysauthority.service
 * @name com.est.sysinit.sysauthority.service.ISysDeptModuleService
 * @description 部门权限配置service接口
 */
public interface ISysDeptModuleService {
	
	
	/**
	 * 
	 *@description 通过部门的id查询部门已分配的权限
	 *@date May 27, 2009
	 *@author jingpj
	 *11:11:18 AM
	 *@param deptId
	 *@return
	 */
	public String getDeptmoduleList (Long deptId);
	
	
	
	/**
	 * 
	 *@description 保存部门所分配的权限
	 *@date May 27, 2009
	 *@author jingpj
	 *11:14:38 AM
	 */
	public void saveDeptModuleList(Long deptId,String data) throws Exception;
	
	
}
