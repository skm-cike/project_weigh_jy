package com.est.sysinit.systype.dao;

import com.est.common.base.IBaseDao;
import com.est.sysinit.systype.vo.SysType;

/**
 * 
 * 
 * @desc 合同类型接口
 * @author hxing
 * @date 2013-4-1上午09:03:29
 * @path com.est.sysinit.systype.dao
 * @corporation Enstrong S&T
 */
public interface ISysTypeDao extends IBaseDao<SysType> {

	void updateBySql(String sql, Object...params)throws Exception;
	
}
