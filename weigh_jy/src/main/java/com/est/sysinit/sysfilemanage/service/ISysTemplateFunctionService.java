package com.est.sysinit.sysfilemanage.service;

import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysfilemanage.vo.SysTemplatefunction;

/**
 *@desc 模板函数管理service接口
 *@author zhanglk
 *@date Jun 26, 2009
 *@path com.est.sysinit.sysfilemanage.service.ISysTemplateFunctionService
 *@corporation Enstrong S&T 
 */
public interface ISysTemplateFunctionService {

	/**
	 * 
	 *@desc 分页查询模板函数 
	 *@date Jun 26, 2009
	 *@author zhanglk
	 *@param page
	 *@param sc
	 *@return
	 */
	public Result<SysTemplatefunction> getTemplateFunctionResult(Page page,SearchCondition sc);
	/**
	 * 
	 *@desc 获取模板函数list 
	 *@date Jun 26, 2009
	 *@author zhanglk
	 *@return
	 */
	public List<SysTemplatefunction> getAllTemplateFunction();
	/**
	 * 
	 *@desc 保存可编辑grid数据 
	 *@date Jun 26, 2009
	 *@author zhanglk
	 *@param changeData
	 */
	public void savChanges(EditableGridDataHelper changeData);
	
	/**
	 *@desc 计算报表函数
	 *@date Jul 17, 2009
	 *@author Administrator
	 *@param templateid
	 *@param funName
	 *@param funArg
	 *@return
	 */
	public String calcFunc(Long templateid, String funName, String funArg,String funContent);
}
