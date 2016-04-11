package com.est.sysinit.sysgroup.service;

import java.io.Serializable;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysgroup.vo.SysGroup;
/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date Jun 9, 2009
 * @path com.est.sysinit.sysgroup.service
 * @name com.est.sysinit.sysgroup.service.ISysGroupService
 * @description 运行角色Service接口
 */
public interface ISysGroupService {
	
	/**
	 *@description 得到角色列表
	 *@date Jun 18, 2009
	 *@author jingpj
	 *12:04:45 AM
	 *@return
	 */
	Result<SysGroup> getGroupList(Page page,SearchCondition cond);
	
	/**
	 * 
	 *@description 得到父角色的子角色列表
	 *@date May 5, 2009
	 *@author jingpj
	 *3:57:01 PM
	 *@param groupId 父角色id
	 *@return
	 */
	public List<SysGroup> getGroupTree(Serializable groupId);

	/**
	 * 
	 *@description 得到全部权限列表
	 *@date Jun 18, 2009
	 *@author jingpj
	 *5:21:04 PM
	 *@return
	 */
	List<SysGroup> getAllGroup();

	/**
	 * 
	 *@description 提交修改数据
	 *@date Jun 19, 2009
	 *@author jingpj
	 *1:05:44 AM
	 */
	void savChanges(EditableGridDataHelper changeData);
	/**
	 * 
	 *@desc 通过id获取group对象 
	 *@date Jul 6, 2009
	 *@author zhanglk
	 *@param groupId
	 *@return
	 */
	public SysGroup getGroupById(Serializable groupId);
	
	/**
	 *@desc 得到用户的某一角色类别下的所有角色
	 *@date Aug 25, 2009
	 *@author jingpj
	 *@param userid
	 *@param groupType
	 *@return
	 */
	public List<SysGroup> getSysGroupByType(Long userid, String groupType);
	
}
