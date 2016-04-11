package com.est.sysinit.sysuser.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.FingerPrintMatchUtils;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.ObjectUtil;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.fusioncharts.FusionChartContainer;
import com.est.sysinit.sysauthority.dao.ISysUserModuleDao;
import com.est.sysinit.sysdept.dao.ISysDeptDao;
import com.est.sysinit.sysdept.vo.SysDept;
import com.est.sysinit.sysgroup.vo.SysGroupuser;
import com.est.sysinit.sysuser.dao.ISysUserDao;
import com.est.sysinit.sysuser.vo.SysUser;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 4, 2009
 * @path com.est.sysinit.sysuser.service
 * @name com.est.sysinit.sysuser.service.SysUserServiceImp
 * @description
 */
public class SysUserServiceImp implements ISysUserService {
	private ISysUserDao sysUserDao;
	private ISysDeptDao sysDeptDao;
	private ISysUserModuleDao sysUserModuleDao;

	public void setSysUserDao(ISysUserDao sysUserDao) {
		this.sysUserDao = sysUserDao;
	}

	public void setSysDeptDao(ISysDeptDao sysDeptDao) {
		this.sysDeptDao = sysDeptDao;
	}

	public void setSysUserModuleDao(ISysUserModuleDao sysUserModuleDao) {
		this.sysUserModuleDao = sysUserModuleDao;
	}

	/**
	 * 
	 * @desc 同步标示(1表示需要同步,0表示不需要同步)
	 * @date Nov 20, 2012
	 * @author heb
	 * @param sync
	 *            同步标示(1表示需要同步,0表示不需要同步)
	 * @param username
	 *            用户姓名
	 * @param login
	 *            登录用户名
	 * @param pwd
	 *            登录密码
	 * @return
	 * @throws Exception
	 * @see com.est.sysinit.sysuser.service.ISysUserService#savSyncUser(java.lang.Long,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public void savSyncUser(String sync, String username, String login, String pwd) throws Exception {

		SearchCondition con = new SearchCondition();

		if ("1".equals(sync)) {
			String hql = "update SysUser t set t.password=? where t.username=? and t.login=?";
			sysUserDao.updateByHql(hql, pwd, username, login);
		}
	}

	public void delUser(Serializable userId) {
		try {
			try {
				sysUserModuleDao.updateByHql("delete from SysUsermodule t where t.sysUser.userid=?", userId);
				sysUserModuleDao.updateByHql("delete from SysGroupuser t where t.sysUser.userid=?", userId);

			} catch (Exception e) {
				throw new BaseBussinessException("删除用户失败!!");
			}

			sysUserDao.updateByHql("delete from SysUser where userid=?", userId);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public SysUser getUserById(Serializable userId) {
		return sysUserDao.findById(userId);
	}

	/**
	 * @desc 根据条件查询所有用户
	 * @date 2011-3-31
	 * @author hebo
	 * @param condition
	 * @return
	 * @see com.est.sysinit.sysuser.service.ISysUserService#getUserList(com.est.common.ext.util.SearchCondition)
	 */
	public List<SysUser> getUserList(SearchCondition condition) {
		String userName = (String) condition.get("userName");
		String duty = (String) condition.get("duty");// 职务
		String strDeptId = (String) condition.get("deptId");
		String includeSubDept = (String) condition.get("includeSubDept");
		Long deptId = StringUtil.parseLong(strDeptId);

		StringBuffer buf = new StringBuffer(200);
		List<Object> paramList = new ArrayList<Object>();

		buf.append("from SysUser t where 1=1");
		if (deptId != null && deptId != 0) {
			if (null != includeSubDept && "true".equals(includeSubDept)) {
				SysDept dept = sysDeptDao.findById(deptId);
				String deptcode = dept.getDeptcode();
				deptcode = deptcode == null ? "" : deptcode;
				buf.append(" and t.sysDept.deptcode like ?");
				paramList.add(deptcode + "%");
			} else {
				buf.append(" and t.sysDept.deptid = ?");
				paramList.add(deptId);
			}
		}

		if (userName != null && !"".equals(userName)) {
			buf.append(" and t.username like ? ");
			paramList.add("%" + userName + "%");
		}
		if (duty != null && !"".equals(duty)) {
			buf.append(" and t.duty like ?");
			paramList.add("%" + duty + "%");
		}

		buf.append(" order by t.userorder,t.username,t.userid");
		return sysUserDao.findByHql(buf.toString(), paramList.toArray());
	}

	public Result<SysUser> getUserList(Page page, SearchCondition condition) {
		String userName = (String) condition.get("userName");
		String strDeptId = (String) condition.get("deptId");
		String includeSubDept = (String) condition.get("includeSubDept");
		String duty = (String) condition.get("duty");
		Long deptId = StringUtil.parseLong(strDeptId);

		StringBuffer buf = new StringBuffer(200);
		List<Object> paramList = new ArrayList<Object>();

		buf.append("from SysUser t where 1=1");
		if (deptId != null && deptId != 0) {
			if (null != includeSubDept && "true".equals(includeSubDept)) {
				SysDept dept = sysDeptDao.findById(deptId);
				String deptcode = dept.getDeptcode();
				deptcode = deptcode == null ? "" : deptcode;
				buf.append(" and t.sysDept.deptcode like ?");
				paramList.add(deptcode + "%");
			} else {
				buf.append(" and t.sysDept.deptid = ?");
				paramList.add(deptId);
			}
		}

		if (userName != null && !"".equals(userName)) {
			buf.append(" and t.username like ? ");
			paramList.add("%" + userName + "%");
		}
		// 用于短信发送按照岗位来发送用,设定位置：com.est.fault.fault.vo.FaultProperty类的sendmsgDuty属性中设置
		if (duty != null && !"".equals(duty)) {
			buf.append(" and t.duty in(" + duty + ")");
		}

		buf.append(" order by t.userorder,t.username,t.userid");
		return sysUserDao.findByPage(page, buf.toString(), paramList.toArray());
	}

	public void savUser(SysUser sysUser) {
		if (sysUser.getSysDept() != null
				&& (sysUser.getSysDept().getDeptid() == null || sysUser.getSysDept().getDeptid() == 0L)) {
			sysUser.setSysDept(null);
		}

		if (sysUser.getUserid() != null && sysUser.getUserid() != 0L) {
			SysUser oldUser = sysUserDao.findById(sysUser.getUserid());
			sysUser.setSysGroupusers(null);
			sysUser.setSysUsermodules(null);
			// 如果部门修改，删除该用户的所有权限
			if (oldUser.getSysDept() != null) {
				if (sysUser.getSysDept() == null
						|| !sysUser.getSysDept().getDeptid().equals(oldUser.getSysDept().getDeptid())) {

					try {
						sysUserModuleDao.updateByHql("delete from SysUsermodule t where t.sysUser.userid=?",
								sysUser.getUserid());
					} catch (Exception e) {
						throw new BaseBussinessException("部门变更，删除用户原有权限失败!!");
					}

				}
			}
			ObjectUtil.objcetMerge(oldUser, sysUser);
//			oldUser.setIpaddress(sysUser.getIpaddress());
			sysUserDao.save(oldUser);
			sysUser = oldUser;
		} else {
			sysUserDao.save(sysUser);
		}
	}

	/**
	 * 
	 * @desc 删除指纹特征
	 * @date Nov 25, 2012
	 * @author heb
	 * @param sysUser
	 * @return
	 * @throws Exception
	 * @see com.est.sysinit.sysuser.service.ISysUserService#delUserFp(com.est.sysinit.sysuser.vo.SysUser)
	 */
	public SysUser delUserFp(SysUser sysUser) throws Exception {
		if (sysUser.getUserid() != null && sysUser.getUserid() != 0L) {

			String hql = "update SysUser t set t.fp1=null,t.fp2=null,fp3=null where t.userid=?";
			sysUserDao.updateByHql(hql, sysUser.getUserid());

			sysUser.setFp1(null);
			sysUser.setFp2(null);
			sysUser.setFp3(null);

		} else {
			throw new RuntimeException("用户基础信息不存在，指纹删除失败。");
		}
		return sysUser;
	}

	/**
	 * 
	 * @desc 保存指纹特征
	 * @date Nov 25, 2012
	 * @author heb
	 * @param sysUser
	 * @return
	 * @throws Exception
	 * @see com.est.sysinit.sysuser.service.ISysUserService#savUserFp(com.est.sysinit.sysuser.vo.SysUser)
	 */
	public SysUser savUserFp(SysUser sysUser) throws Exception {

		if (sysUser.getUserid() != null && sysUser.getUserid() != 0L) {

			String fp1 = sysUser.getFp1();
			String fp2 = sysUser.getFp2();
			String fp3 = sysUser.getFp3();

			String hql = "update SysUser t set t.fp1=?,t.fp2=?,fp3=? where t.userid=?";
			sysUserDao.updateByHql(hql, fp1, fp2, fp3, sysUser.getUserid());

		} else {
			throw new RuntimeException("用户基础信息不存在，指纹保存失败。");
		}
		return sysUser;
	}

	/**
	 * 
	 * @description 删除当前用户的所有权限
	 * @date Jun 9, 2009
	 * @author jingpj 3:32:13 PM
	 * @param sysUser
	 * @throws Exception
	 */
	public void removeAllAuthorities(SysUser sysUser) throws Exception {
		String hql = "delete from SysUsermodule t where t.sysUser = ?";
		sysUserDao.updateByHql(hql, sysUser);
	}

	@SuppressWarnings("unchecked")
	public SysUser vefUser(SearchCondition condition) {
		String login = (String) condition.get("login");
		String password = (String) condition.get("password");
		StringBuffer buf = new StringBuffer(80);
		try {
			List<Object> paramList = new ArrayList<Object>();
			buf
					.append("from com.est.sysinit.sysuser.vo.SysUser t where t.login= ?  and  t.password=? and (t.isvalid !='否' or t.isvalid is null)");
			paramList.add(login);
			paramList.add(password);
			List userList = sysUserDao.findByHql(buf.toString(), paramList.toArray());
			if (userList != null && userList.size() > 0) {
				SysUser u = (SysUser) userList.get(0);
				return (SysUser) userList.get(0);
			} else {
				return null;
			}

		} catch (Exception ex) {
			return null;
		}

	}

	/**
	 * 
	 * @desc 指纹验证
	 * @date Nov 25, 2012
	 * @author heb
	 * @param username
	 *            用户姓名
	 * @param fp
	 *            指纹特征码
	 * @return
	 * @see com.est.sysinit.sysuser.service.ISysUserService#verfyUserFp(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public SysUser verfyUserFp(String username, String fp) throws Exception {

		String hql = "from SysUser t where t.username=? ";

		List<SysUser> users = sysUserDao.findByHql(hql, username);

		for (SysUser user : users) {
			// 对比第一个指纹
			if (user.getFp1() != null && !"".equals(user.getFp1())) {

				String res = FingerPrintMatchUtils.match2FB(fp, user.getFp1());
				if ("成功".equals(res)) {
					return user;
				}

			}
			// 对比第二个指纹
			if (user.getFp2() != null && !"".equals(user.getFp2())) {

				String res = FingerPrintMatchUtils.match2FB(fp, user.getFp2());
				if ("成功".equals(res)) {
					return user;
				}

			}
			// 对比第三个指纹
			if (user.getFp3() != null && !"".equals(user.getFp3())) {

				String res = FingerPrintMatchUtils.match2FB(fp, user.getFp3());
				if ("成功".equals(res)) {
					return user;
				}

			}

		}

		return null;
	}

	public SysUser chkUser(SearchCondition condition) {
		String login = (String) condition.get("login");
		StringBuffer buf = new StringBuffer(80);
		try {
			List<Object> paramList = new ArrayList<Object>();
			buf.append("from com.est.sysinit.sysuser.vo.SysUser t where t.login=? ");
			paramList.add(login);

			List userList = sysUserDao.findByHql(buf.toString(), paramList.toArray());
			if (userList != null && userList.size() > 0) {
				SysUser u = (SysUser) userList.get(0);
				return (SysUser) userList.get(0);
			} else {
				return null;
			}
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 
	 * @desc 修改用户密码
	 * @date Sep 22, 2009
	 * @author hebo
	 * @param sysUser
	 * @throws Exception
	 * @see com.est.sysinit.sysuser.service.ISysUserService#savModifiedUserPwd(com.est.sysinit.sysuser.vo.SysUser)
	 */
	public void savModifiedUserPwd(SysUser sysUser) throws Exception {
		try {
			String hql = "update SysUser t set t.password=? where t.userid=?";
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(sysUser.getPassword());
			paramList.add(sysUser.getUserid());
			sysUserDao.updateByHql(hql, paramList.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public String getChartXml() {
		FusionChartContainer chartContainer = new FusionChartContainer();
		chartContainer.setSeriesHql("abc",
				"select t.sysDept.deptname ,count(t)/11.00 from SysUser t group by t.sysDept.deptname");
		return chartContainer.generateResult(sysUserDao);
	}

	public SysUser getUserByLogin(String login) {
		String hql = "from SysUser t where t.login = ?";
		SysUser sysuser = (SysUser) sysUserDao.findUniqueByHql(hql, login);
		return sysuser;
	}

	/**
	 * 查询用户角色
	 */
	public boolean getUserRole(String userid, String power) {
		String hql = "from SysUser t where t.userid='" + userid + "'";
		SysUser sysuser = (SysUser) sysUserDao.findUniqueByHql(hql);
		Set<SysGroupuser> set = sysuser.getSysGroupusers();
		Iterator<SysGroupuser> it = set.iterator();
		String userRole = "userRole";
		boolean flag = false;
		while (it.hasNext()) {
			SysGroupuser groupuser = it.next();
			userRole = groupuser.getSysGroup().getGrouptype();// 查询用户角色
			if (userRole.equals(power)) {
				flag = true;
				return flag;
			}
		}
		return flag;

	}

}
