package com.est.sysinit.sysmodule.service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysmodule.vo.SysModulefield;

/**
 *@desc 模块对应字段service
 *@author jingpj
 *@date Feb 4, 2010
 *@path com.est.sysinit.sysmodule.service.ISysModulefieldService
 *@corporation Enstrong S&T
 */
public interface ISysModulefieldService {

	/**
	 *@desc 保存模块对应字段 
	 *@date Feb 4, 2010
	 *@author jingpj
	 *@param editGridData
	 */
	void savSysModulefileList(EditableGridDataHelper editGridData);

	/**
	 *@desc 查询模块下配置的字段
	 *@date Feb 4, 2010
	 *@author jingpj
	 *@param page
	 *@param moduleId
	 *@return
	 */
	Result<SysModulefield> getFieldListByModuleId(Page page, Long moduleId);

}
