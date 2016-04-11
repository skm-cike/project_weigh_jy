package com.est.sysinit.sysauthority.service;

import java.util.List;

import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.sysauthority.vo.SysUsermodule;
import com.est.sysinit.sysuser.vo.SysUser;

/**
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 27, 2009
 * @path com.est.sysinit.sysauthority.service
 * @name com.est.sysinit.sysauthority.service.ISysDeptModuleService
 * @description 部门权限配置service接口
 */
public interface ISysUserModuleService {
	
	
	/**
	 * 
	 *@description 通过用户id,部门id查询用户可分配及已分配的权限
	 *@date May 27, 2009
	 *@author jingpj
	 *@param userId
	 *@param deptId
	 *@return
	 */
	public String getUsermoduleList (Long userId,Long deptId);
	
	
	
	/**
	 * 
	 *@description 保存用户所分配的权限
	 *@date May 27, 2009
	 *@author jingpj
	 *11:14:38 AM
	 */
	public void saveUserModuleList(Long userId,Long deptId,String data) throws Exception;
	
	/**
	 * 
	 *@description 查询登录用户可使用模块，并组装成下拉菜单
	 *@date Jun 2, 2009
	 *@author jingpj
	 *4:51:11 PM
	 *@param user
	 *@return
	 */
	public String getUserModuleMenu(SysUser user) throws Exception;
	
	
	/**
	 *@desc 构造菜单中按钮的点击事件方法
	 *@date Nov 25, 2009
	 *@author jingpj
	 *@param userModule
	 *@param module
	 *@return
	 */
	public String getBtnClickEvent(SysUsermodule userModule);
	
	
	/**
	 *@desc 构造菜单中按钮的点击事件方法
	 *@date Nov 25, 2009
	 *@author jingpj
	 *@param userModule
	 *@param module
	 *@return
	 */
	public String getFrameBtnClickEvent(SysUsermodule userModule);
	/**
	 *@desc 得到工作流处理任务路径
	 *@date Oct 21, 2009
	 *@author jingpj
	 *@param userId
	 *@param moduleId
	 *@return
	 */
	public String getWfHandleUrl(SearchCondition condition);
	
	/**
	 *@desc 得到用户的模块按钮
	 *@date Sep 1, 2009
	 *@author jingpj
	 *@param user 用户
	 *@param rootModuleName 作为根模块的模块名称
	 *@return
	 */
	@Deprecated
	public List<SysUsermodule> getUserModuleButtons(SysUser user , String rootModuleName);


	/**
	 *@desc 得到用户的模块按钮
	 *@date Sep 1, 2009
	 *@author jingpj
	 *@param user 用户
	 *@param rootModuleName 作为根模块的模块名称
	 *@return
	 */
	public List<SysUsermodule> getUserModuleButtons(SysUser user, Long moduleId); 
	
	
}
