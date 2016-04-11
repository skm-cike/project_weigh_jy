package com.est.sysinit.systype.service;

import java.util.List;

import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.systype.vo.SysType;
import com.est.sysinit.sysuser.vo.SysUser;

public interface ITypeForTreeService {
	/**
	 * 
	 * @描述: 获得子类型
	 * @作者: 陆华
	 * @时间: 2013-4-15 上午04:31:20
	 * @param condition
	 * @return
	 */
	List<SysType> getSubType(SearchCondition condition);

	/**
	 * 
	 * @描述: 获得当前登录人员所属部门拥有的一级类型代码
	 * @作者: 陆华
	 * @时间: 2013-4-15 上午04:31:31
	 * @param currentUser
	 * @return
	 */
	List<SysType> getTopTypeStr(SysUser currentUser);

	/**
	 * 
	 * @描述: 获得分配给相关部门的子类型
	 * @作者: 陆华
	 * @时间: 2013-4-15 上午03:55:30
	 * @return
	 */
	List<SysType>  getNodeByDept(Long deptid, String node);
}
