package com.est.sysinit.sysgroup.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysgroup.dao.ISysGroupDao;
import com.est.sysinit.sysgroup.vo.SysGroup;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 4, 2009
 * @path com.est.sysinit.sysuser.service
 * @name com.est.sysinit.sysuser.service.SysUserServiceImp
 * @description
 */
public class SysGroupServiceImp implements ISysGroupService {
	
	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(SysGroupServiceImp.class);
	
	private ISysGroupDao sysGroupDao ;
	public void setSysGroupDao(ISysGroupDao sysGroupDao) {
		this.sysGroupDao = sysGroupDao;
	}
	public Result<SysGroup> getGroupList(Page page,SearchCondition cond) {
		String grouptype = (String) cond.get("grouptype");
		StringBuffer hql = new StringBuffer(100);
		ArrayList<Object> paramList = new ArrayList<Object>();
		hql.append("from SysGroup t ");
		if(grouptype!=null && !"".equals(grouptype)) {
			hql.append(" where t.grouptype = ?");
			paramList.add(grouptype);
		}
		
		hql.append(" order by t.orderindex");
		
		return sysGroupDao.findByPage(page, hql.toString(),paramList.toArray());
	}
	/**
	 * 
	 *@desc 通过id获取group对象 
	 *@date Jul 6, 2009
	 *@author zhanglk
	 *@param groupId
	 *@return
	 */
	public SysGroup getGroupById(Serializable groupId){
		return sysGroupDao.findById(groupId);
	}

	public List<SysGroup> getAllGroup() {
		return sysGroupDao.findAll();
	}
	
	public void savChanges(EditableGridDataHelper changeData) {
		List<SysGroup> saveList = new ArrayList<SysGroup>();
		List<SysGroup> delList = new ArrayList<SysGroup>();
		Iterator<Object> it = changeData.getSaveObjects().iterator();
		while(it.hasNext()) {
			SysGroup sysGroup = (SysGroup)it.next();
			if(sysGroup.getGroupid()==0) {
				sysGroup.setGroupid(null);
			}
			saveList.add(sysGroup);
		}
		sysGroupDao.saveAll(saveList);
		
		it = changeData.getDelObjects().iterator();
		while(it.hasNext()) {
			delList.add((SysGroup)it.next());
		}
		sysGroupDao.delAll(delList);
	}
	
	/**
	 * 
	 *@description 得到父角色的子角色列表
	 *@date May 5, 2009
	 *@author jingpj
	 *3:57:01 PM
	 *@param groupId 父角色id
	 *@return
	 */
	public List<SysGroup> getGroupTree(Serializable groupId) {
		Long id = (Long)groupId;
		StringBuffer buf = new StringBuffer(200);
		List<Object> paramList = new ArrayList<Object>();
		buf.append("from SysGroup t where ");
		if(id!=null && id!=0L) {
			buf.append(" t.sysGroup.groupid = ?");
			paramList.add(id);
		} else {
			buf.append(" t.sysGroup is null");
		}
		buf.append(" order by t.groupid");
		return sysGroupDao.findByHql(buf.toString(), paramList.toArray());
	}

	
	/**
	 *@desc 得到用户的某一角色类别下的所有角色
	 *@date Aug 25, 2009
	 *@author jingpj
	 *@param userid
	 *@param groupType
	 *@return
	 */
	public List<SysGroup> getSysGroupByType(Long userid, String groupType){
		String hql = "Select t.sysGroup From SysGroupuser t where t.sysUser.userid = ? and t.sysGroup.grouptype = ?" ;
		List<SysGroup> groupList = sysGroupDao.findByHql(hql, userid,groupType);
		return groupList;
	}
	
	
}
