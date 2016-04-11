package com.est.workflow.processdefination.service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.workflow.processdefination.vo.WfDftaskcondition;


public interface IWfDftaskconditionService {
	
	/**
	 * 
	 *@desc 获取任务条件列表
	 *@date Nov 3, 2009
	 *@author jingpj
	 *@param condition
	 *@param page
	 *@return
	 *@throws Exception
	 */
	public Result<WfDftaskcondition> getWfDftaskconditionListByPage(SearchCondition condition,Page page) throws Exception;
	
	/**
	 * 
	 *@desc 批量删除、保存列表信息
	 *@date Nov 3, 2009
	 *@author jingpj
	 *@param data
	 * @throws Exception 
	 */
	public void savWfDftaskconditionList(EditableGridDataHelper data) throws Exception;
	
}
