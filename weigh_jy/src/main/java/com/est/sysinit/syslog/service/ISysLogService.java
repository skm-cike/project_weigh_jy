package com.est.sysinit.syslog.service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.syslog.vo.SysLog;

/**
 * 
 *@desc 基础信息配置service接口
 *@author yzw
 *@date Jun 7, 2010
 *@path com.est.fuel.base.service.IFuelBaseconfigService
 *@corporation Enstrong S&T
 */
public interface ISysLogService {

	public Result<SysLog> getModuleList(Page page, SearchCondition condition);
	
	public Result<SysLog> getModuleListById(Page page, SearchCondition condition);
	
	public void saveSysLog(SysLog sysLog);

}
