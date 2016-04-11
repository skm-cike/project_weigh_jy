package com.est.sysinit.sysuser.service;

import java.util.HashSet;
import java.util.List;

import com.est.sysinit.sysauthority.dao.ISysUserModuleDao;
import com.est.sysinit.sysauthority.vo.SysUsermodule;
import com.est.sysinit.sysuser.dao.ISysUserModifyDao;
import com.est.sysinit.sysuser.vo.SysUser;

public class SysUserModifyServiceImp implements ISysUserModifyService {

	private ISysUserModifyDao sysUserModifyDao;
	private ISysUserModuleDao sysUserModuleDao;
	
	public void setSysUserModifyDao(ISysUserModifyDao sysUserModifyDao) {
		this.sysUserModifyDao = sysUserModifyDao;
	}
	
	/**Setters
	 * @param sysUserModuleDao 要设置的 sysUserModuleDao。
	
	 */
	public void setSysUserModuleDao(ISysUserModuleDao sysUserModuleDao) {
		this.sysUserModuleDao = sysUserModuleDao;
	}


	public SysUser savUser(SysUser user) {
		if(user.getSysDept()!=null && (user.getSysDept().getDeptid()==null || user.getSysDept().getDeptid()==0 ))
		{
			user.setSysDept(null);
		}
		
		String hql = "from SysUsermodule t where t.sysUser.userid=?";
		
		List<SysUsermodule> sysUsermodules=sysUserModuleDao.findByHql(hql, user.getUserid());
		
		user.setSysUsermodules(new HashSet(sysUsermodules));
		
		sysUserModifyDao.saveAndClear(user);
		
		return sysUserModifyDao.findById(user.getUserid());
		
	}

}
