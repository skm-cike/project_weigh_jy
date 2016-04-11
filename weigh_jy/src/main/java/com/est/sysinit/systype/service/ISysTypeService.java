package com.est.sysinit.systype.service;

import java.io.Serializable;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.systype.vo.SysType;
import com.est.sysinit.sysuser.vo.SysUser;

/**
 * 
 * 
 * @desc 
 * @author hxing
 * @date 2013-4-1上午09:28:03
 * @path com.est.sysinit.systype.service
 * @corporation Enstrong S&T
 */
public interface ISysTypeService {
	/**
	 * 添加合同类型
	 * @param sysType
	 */
	public abstract void savSysType(SysType sysType);
	
	/**
	 * 删除合同类型
	 * @return true ,false
	 */
	public abstract boolean delSysType(Long typeid);
	
	public abstract SysType getTypeById(Serializable typeid);
	/**
	 * 
	 * @param page
	 * @param condition
	 * @return
	 */
	public Result<SysType> getTypeList(Page page, SearchCondition condition);
	
	/**
	 * 获取所有的合同类型
	 * @return List<SysType>
	 */
	public abstract List<SysType> getAllSysType(SearchCondition condition);
	
	/**
	 * 获取类型树
	 * @return
	 */
	public abstract List<SysType> getTreeSysType(Serializable typeid);
	/**
	 * 更新合同类型
	 * @param sysType
	 * @return true ,false
	 */
	public abstract void updateSysType(List<Object> paramslist);


	
}
