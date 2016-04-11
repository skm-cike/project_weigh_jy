package com.est.sysinit.sysgroup.service;

import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.sysgroup.vo.SysGroup;
import com.est.sysinit.sysgroup.vo.SysGroupuser;

/**
 *@desc 用户权限组授权service接口
 *@author zhanglk
 *@date Jul 6, 2009
 *@path com.est.sysinit.sysgroup.service.ISysGroupuserService
 *@corporation Enstrong S&T 
 */
public interface ISysGroupuserService {

	/**
	 * 
	 *@desc 查询已分配权限用户列表 
	 *@date Jul 6, 2009
	 *@author zhanglk
	 *@param page
	 *@param sc
	 *@return
	 */
	public Result<SysGroupuser> getGroupuserList(Page page, SearchCondition sc);
	/**
	 * 
	 *@desc 分配用户组权限 
	 *@date Jul 6, 2009
	 *@author zhanglk
	 *@param sc
	 */
	public void savGroupuserList(SearchCondition sc);
	/**
	 * 
	 *@desc 批量删除已分配权限用户 
	 *@date Jul 6, 2009
	 *@author zhanglk
	 *@param sc
	 */
	public void delGroupuserList(SearchCondition sc);
	
	/**
	 *@desc 判断用户是否有某一工作者权限
	 *@date Jul 7, 2009
	 *@author zhanglk
	 *@param groupId
	 *@param userId
	 *@return
	 */
	public boolean chkGroupHasUser(String groupName,Long userId);
	
	/**
	 *@desc 判断用户是否有某一工作者权限(通过角色类型判断)
	 *@date Jul 7, 2009
	 *@author zhanglk
	 *@param groupId
	 *@param userId
	 *@return
	 */
	public boolean chkGroupHasUserByGrouptype(String grouptype,Long userId);
	
	/**
	 *@desc 判断用户是否有定期工作组的权限
	 *@date Aug 25, 2009
	 *@author jingpj
	 *@param userid
	 *@return
	 */
	public boolean isCycleworkUser(Long userid);
	
	
	
	
	
}
