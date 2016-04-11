package com.est.sysinit.sysfilemanage.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysfilemanage.vo.SysTemplateparam;

public interface ISysTemplateparamService {

	/**
	 * 分页查询模板参数list
	 *@desc 
	 *@date Jun 29, 2009
	 *@author zhanglk
	 *@param page
	 *@param sc
	 *@return
	 */
	public Result<SysTemplateparam> getTemplateparamResult(Page page,SearchCondition sc);
	/**
	 * 
	 *@desc 保存可编辑grid数据  
	 *@date Jun 29, 2009
	 *@author zhanglk
	 *@param changeData
	 */
	public void savChanges(EditableGridDataHelper changeData);
	
	/**
	 * 
	 *@desc 得到模板的参数列表 
	 *@date Jul 20, 2009
	 *@author jingpj
	 *@param templateId
	 *@return
	 */
	public List<SysTemplateparam> getTemplateparamsById(Long templateId);
}
