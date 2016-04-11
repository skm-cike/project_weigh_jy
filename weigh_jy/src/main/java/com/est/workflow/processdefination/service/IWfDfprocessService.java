package com.est.workflow.processdefination.service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.workflow.processdefination.vo.WfDfprocess;
import com.est.workflow.processdefination.vo.WfDftask;

public interface IWfDfprocessService {

	public WfDfprocess getWfDfprocess(Long processId);
	
	/**
	 * 
	 *@desc 获取流程定义列表
	 *@date Nov 5, 2009
	 *@author hebo
	 *@param condition
	 *@param page
	 *@return
	 *@throws Exception
	 */
	public Result<WfDfprocess> getProcessDefinationListByPage(SearchCondition condition,Page page) throws Exception;
	
	/**
	 * 
	 *@desc 设置流程定义是否有效 
	 *@date Nov 4, 2009
	 *@author hebo
	 *@param condition
	 * @throws Exception 
	 */
	public void savProcessValid(SearchCondition condition) throws Exception;

	/**
	 *@desc 读取流程配置json 
	 *@date Nov 5, 2009
	 *@author jingpj
	 *@param processId
	 *@return
	 */
	public String getProcessJson(Long processId);
	
	/**
	 * 
	 *@desc 保存任务
	 *@date Nov 5, 2009
	 *@author hebo
	 *@param wfDftask
	 * @throws Exception 
	 */
	public void savWfDftask(WfDftask wfDftask) throws Exception;
	
	/**
	 * 
	 *@desc 任务详细信息
	 *@date Nov 5, 2009
	 *@author hebo
	 *@param taskId
	 *@return
	 */
	public WfDftask getWfDftaskById(Long taskId);
	
	/**
	 * 
	 *@desc 删除任务
	 *@date Nov 5, 2009
	 *@author hebo
	 *@param taskId
	 * @throws Exception 
	 */
	public void delWfDftaskById(Long taskId) throws Exception;
	
	
}
