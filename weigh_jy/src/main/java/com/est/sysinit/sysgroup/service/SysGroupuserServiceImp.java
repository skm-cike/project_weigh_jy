package com.est.sysinit.sysgroup.service;

import java.util.ArrayList;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysgroup.dao.ISysGroupuserDao;
import com.est.sysinit.sysgroup.vo.SysGroup;
import com.est.sysinit.sysgroup.vo.SysGroupuser;
import com.est.sysinit.sysuser.vo.SysUser;

/**
 *@desc 用户组授权service
 *@author zhanglk
 *@date Jul 6, 2009
 *@path com.est.sysinit.sysgroup.service.SysGroupuserServiceImp
 *@corporation Enstrong S&T 
 */
public class SysGroupuserServiceImp implements ISysGroupuserService {
	
	private ISysGroupuserDao sysGroupuserDao;
	public void setSysGroupuserDao(ISysGroupuserDao sysGroupuserDao){
		this.sysGroupuserDao = sysGroupuserDao;
	}
	/**
	 * 
	 *@desc 查询已分配权限用户列表 
	 *@date Jul 6, 2009
	 *@author zhanglk
	 *@param page
	 *@param sc
	 *@return
	 */
	public Result<SysGroupuser> getGroupuserList(Page page, SearchCondition sc){
		Long groupId = StringUtil.parseLong((String)sc.get("groupid"));
		Long deptId = StringUtil.parseLong((String )sc.get("deptid"));
		String username = (String) sc.get("username");
		StringBuffer buf = new StringBuffer(200);
		List<Object> paramList = new ArrayList<Object>();
		
		buf.append("from SysGroupuser t where 1=1");
		if(groupId!=null && groupId!=0) {
				buf.append(" and t.sysGroup.groupid = ?");
				paramList.add(groupId);
		} 
		if(deptId!=null && deptId!=0){
			buf.append(" and t.sysUser.userid = ? ");
			paramList.add(deptId);
		}
		if(username!=null && !"".equals(username)){
			buf.append(" and t.sysUser.username like ?");
			paramList.add(username+"%");
		}
		buf.append(" order by t.sysUser.userid");
		return sysGroupuserDao.findByPage(page, buf.toString(), paramList.toArray());
	}
	/**
	 * 
	 *@desc 分配用户组权限 
	 *@date Jul 6, 2009
	 *@author zhanglk
	 *@param sc
	 */
	public void savGroupuserList(SearchCondition sc){
		Long groupId = StringUtil.parseLong((String)sc.get("groupid"));//权限组id
		String strUserid = (String)sc.get("userid");//用户id串，用逗号隔开
		String[] arrUserid = strUserid.split(",");
		SysGroup group = new SysGroup();
		group.setGroupid(groupId);
		Long userId=0L;
		for(int i=0;i<arrUserid.length;i++){
			userId = StringUtil.parseLong(arrUserid[i].toString());
			if(!this.getGroupuser(groupId, userId)){//判断该用户是否已经被分配权限了
				SysUser user = new SysUser();
				user.setUserid(userId);
				SysGroupuser groupUser = new SysGroupuser();
				groupUser.setSysGroup(group);
				groupUser.setSysUser(user);
				sysGroupuserDao.save(groupUser);
			}
		}
	}
	/**
	 * 
	 *@desc 批量删除已分配权限用户 
	 *@date Jul 6, 2009
	 *@author zhanglk
	 *@param sc
	 */
	public void delGroupuserList(SearchCondition sc){
		String strGroupUserid = (String)sc.get("groupuserid");
		String[] arrGroupuserId = strGroupUserid.split(",");
		String ids = "";
		for(int i=0;i<arrGroupuserId.length;i++){
			ids += arrGroupuserId[i] + ",";
		}
		if (!"".equals(ids)) {
			ids = ids.substring(0, ids.length() - 1);
		}
		String hql = "delete from SysGroupuser where groupuserid in (" + ids + ")";
		try {
			sysGroupuserDao.updateByHql(hql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 *@desc 判断授权用户是否重复，true为重复，false不重复
	 *@date Jul 7, 2009
	 *@author zhanglk
	 *@param groupId
	 *@param userId
	 *@return
	 */
	public boolean getGroupuser(Long groupId,Long userId){
		List<Object> paramList = new ArrayList<Object>();
		
		StringBuffer hql = new StringBuffer(200);
		hql.append("select count(*) from SysGroupuser t where t.sysUser.userid=?");
		
		paramList.add(userId);
		if(groupId!=null && groupId>0){
			hql.append(" and t.sysGroup.groupid=?");
			paramList.add(groupId);
		}
		Object obj = sysGroupuserDao.findUniqueByHql(hql.toString(), paramList.toArray());
		int cnt = ((Long)obj).intValue();
		return cnt > 0 ? true : false;
		
	}

	/**
	 *@desc 判断用户是否有某一工作者权限
	 *@date Jul 7, 2009
	 *@author zhanglk
	 *@param groupId
	 *@param userId
	 *@return
	 */
	public boolean chkGroupHasUser(String groupName,Long userId){
		List<Object> paramList = new ArrayList<Object>();
		
		StringBuffer hql = new StringBuffer(200);
		hql.append("select count(*) from SysGroupuser t where t.sysUser.userid=?");
		
		paramList.add(userId);
		if(groupName!=null && !"".equals(groupName)){
			hql.append(" and t.sysGroup.groupname=?");
			paramList.add(groupName);
		}
		Object obj = sysGroupuserDao.findUniqueByHql(hql.toString(), paramList.toArray());
		int cnt = ((Long)obj).intValue();
		return cnt > 0 ? true : false;
	}
	
	/**
	 * 
	 *@desc  判断用户是否有某一工作者权限(通过角色类型判断)
	 *@date May 11, 2012
	 *@author Administrator
	 *@param grouptype
	 *@param userId
	 *@return
	 *@see com.est.sysinit.sysgroup.service.ISysGroupuserService#chkGroupHasUserByGrouptype(java.lang.String, java.lang.Long)
	 */
	public boolean chkGroupHasUserByGrouptype(String grouptype, Long userId) {
		List<Object> paramList = new ArrayList<Object>();
		
		StringBuffer hql = new StringBuffer(200);
		hql.append("select count(*) from SysGroupuser t where t.sysUser.userid=?");
		
		paramList.add(userId);
		if(grouptype!=null && !"".equals(grouptype)){
			hql.append(" and t.sysGroup.grouptype=?");
			paramList.add(grouptype);
		}
		Object obj = sysGroupuserDao.findUniqueByHql(hql.toString(), paramList.toArray());
		int cnt = ((Long)obj).intValue();
		return cnt > 0 ? true : false;
	}
	/**
	 *@desc 判断用户是否有定期工作组的权限
	 *@date Aug 25, 2009
	 *@author jingpj
	 *@param userid
	 *@return
	 */
	public boolean isCycleworkUser(Long userid){
		String hql = "Select count(*) From SysGroupuser t where t.sysUser.userId = ? and t.sysGroup.grouptype = 'DQGZ'" ;
		Long cnt = (Long)sysGroupuserDao.findUniqueByHql(hql,userid);
		if( cnt > 0 ){
			return true;
		} else {
			return false;
		}
	}
	
	
}
