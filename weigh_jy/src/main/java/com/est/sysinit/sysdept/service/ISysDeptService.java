package com.est.sysinit.sysdept.service;

import java.io.Serializable;
import java.util.List;

import com.est.sysinit.sysdept.vo.SysDept;
/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 5, 2009
 * @path com.est.sysinit.sysdept.service
 * @name com.est.sysinit.sysdept.service.ISysDeptService
 * @description 系统部门service 接口
 */
public interface ISysDeptService {
	/**
	 * 
	 *@description 得到父部门的子部门列表
	 *@date May 5, 2009
	 *@author jingpj
	 *3:57:01 PM
	 *@param deptId 父部门id
	 *@return
	 */
	public List<SysDept> getDepartTree(Serializable deptId);
	
	/**
	 * 
	 *@description 得到所有部门(combo使用)
	 *@date May 22, 2009
	 *@author jingpj
	 *11:21:31 AM
	 *@return
	 */
	public List<SysDept> getAllDepart();
	
	/**
	 * 
	 *@description 删除部门
	 *@date May 5, 2009
	 *@author jingpj
	 *3:57:37 PM
	 *@param deptId  部门id
	 */
	public void delDept(Serializable deptId);
	
	/**
	 * 
	 *@description 通过id得到部门对象
	 *@date May 5, 2009
	 *@author jingpj
	 *3:57:51 PM
	 *@param deptId 部门id
	 *@return
	 */
	public SysDept getDeptById(Serializable deptId);
	
	/**
	 * 
	 *@description 保存部门
	 *@date May 5, 2009
	 *@author jingpj
	 *3:58:23 PM
	 *@param sysDept 被保存部门对象
	 * @throws Exception 
	 */
	public void savDept(SysDept sysDept) throws Exception;
	
	/**
	 * 
	 *@description 找到某部门的一级（顶极）部门
	 *@date Jun 5, 2009
	 *@author jingpj
	 *9:44:57 AM
	 *@param deptId
	 *@return
	 */
	public SysDept findFirstLevelDept(Long deptId);
	/**
	 * 
	 *@desc 检验部门下是否存在子内容
	 *@date Oct 19, 2009
	 *@author hebo
	 *@param deptId 部门ID
	 *@return
	 * @throws Exception 
	 */
	public void isExistsContents(Long deptId);
	
	
	
}
