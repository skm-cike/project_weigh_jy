package com.est.workflow.process.callback;

/**
 *@desc 工作流流转回调接口
 *@author jingpj
 *@date Feb 24, 2010
 *@path com.est.workflow.process.callback.IWFCallback
 *@corporation Enstrong S&T
 */
public interface IWFCallback {

	/**
	 *@desc  开始流程之前执行
	 *@date Feb 24, 2010
	 *@author jingpj
	 *@param id 主表id
	 */
	public void beforeProcessStart(Long id);
	
	/**
	 *@desc  开始流程之后执行
	 *@date Feb 24, 2010
	 *@author jingpj
	 *@param id 主表id
	 */	
	public void afterProcessStart(Long id,String status);
	
	/**
	 *@desc 流程节点之前执行
	 *@date Feb 24, 2010
	 *@author jingpj
	 *@param id 主表id
	 */	
	public void beforeNodeEntry(Long id);
	
	/**
	 *@desc 流程节点结束执行
	 *@date Feb 24, 2010
	 *@author jingpj
	 *@param id 主表id
	 */	
	public void afterNodeLeave(Long id,String status);
	
	/**
	 *@desc  流程结束之前执行
	 *@date Feb 24, 2010
	 *@author jingpj
	 *@param id 主表id
	 */
	public void beforeProcessEnd(Long id);
	
	/**
	 *@desc  流程结束之后执行
	 *@date Feb 24, 2010
	 *@author jingpj
	 *@param id 主表id
	 */	
	public void afterProcessEnd(Long id,String status);
	
	
	/**
	 *@desc 流程被终止
	 *@date Feb 24, 2010
	 *@author jingpj
	 *@param id
	 */
	public void afterProcessTerminate(Long id);
	
	
	
	
}
