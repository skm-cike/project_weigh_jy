package com.est.sysinit.systype.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.systype.dao.ISysTypeDao;
import com.est.sysinit.systype.vo.SysType;
import com.est.sysinit.sysuser.vo.SysUser;
@Service
public class TypeForTreeServiceImp implements ITypeForTreeService{
	private ISysTypeDao sysTypeDao;
	public List<SysType> getSubType(SearchCondition condition) {
		String node = (String)condition.get("node");
		String hql = null;
		List params = new ArrayList();
		if (StringUtil.isEmpty(node) || "ROOT".equals(node.toUpperCase()) || "NULL".equals(node.toUpperCase())) {
			hql = "FROM SysType WHERE parenttypeid IS NULL";
		}
		
		if (node.toUpperCase().startsWith("ROOT")) {
			String typecode = node.split("_")[1];
			hql = "FROM SysType WHERE parenttypeid=(SELECT typeid FROM SysType WHERE typecode=?)";
			params.add(typecode);
		} else {
			hql = "FROM SysType WHERE parenttypeid=?";
			params.add(Long.parseLong(node));
		}
		return sysTypeDao.findByHql(hql, params.toArray());
	}
	
	public void setSysTypeDao(ISysTypeDao sysTypeDao) {
		this.sysTypeDao = sysTypeDao;
	}

	/**
	 * 
	 * @描述: 获得当前登录人员所属部门拥有的一级类型代码
	 * @作者: 陆华
	 * @时间: 2013-4-15 上午03:57:00
	 * @return ,分隔
	 */
	public List<SysType> getTopTypeStr(SysUser currentUser) {
		Long deptid = currentUser.getSysDept().getDeptid();
		String sql = "SELECT DISTINCT T1.TYPECODE,T3.TYPENAME FROM CONTRACT_TYPEDEPTGROUP T1" +
				" LEFT JOIN SYS_DEPT T2 ON T1.DEPTID=T2.DEPTID LEFT JOIN SYS_TYPE T3 ON" +
				" T1.TYPECODE=T3.TYPECODE WHERE T2.DEPTID=? AND T3.PARENTTYPEID IS NULL";
		List<Object[]> list = sysTypeDao.sqlQuery(sql, new Object[]{deptid});
		List<SysType> typeList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			SysType stype = new SysType();
			Object[] os = list.get(i);
			stype.setTypecode((String)os[0]);
			stype.setTypename((String)os[1]);
			stype.setSysType(null);
			stype.setSysTypes(null);
			stype.setTypeid(null);
			stype.setTyperemark(null);
			stype.setTyperule(null);
			typeList.add(stype);
		}
		return typeList;
	}

	/**
	 * 
	 * @描述: 获得分配给相关部门的子类型
	 * @作者: 陆华
	 * @时间: 2013-4-15 上午03:55:30
	 * @return
	 */
	public List<SysType> getNodeByDept(Long deptid, String node) {
		String hql = null;
		List params = new ArrayList();
		if (StringUtil.isEmpty(node) || "ROOT".equals(node.toUpperCase()) || "NULL".equals(node.toUpperCase())) {
			hql = "FROM SysType WHERE parenttypeid IS NULL";
		}
		
		if (node.toUpperCase().startsWith("ROOT")) {
			String typecode = node.split("_")[1];
			hql = "FROM SysType WHERE parenttypeid=(SELECT typeid FROM SysType WHERE typecode=?)";
			params.add(typecode);
		} else {
			hql = "FROM SysType WHERE parenttypeid=?";
			params.add(Long.parseLong(node));
		}
		return sysTypeDao.findByHql(hql, params.toArray());
	}
}
