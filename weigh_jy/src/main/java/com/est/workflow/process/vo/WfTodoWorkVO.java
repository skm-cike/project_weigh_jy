package com.est.workflow.process.vo;

import java.util.Date;

/**
 * 
 *@desc 用于前台显示待办事项内容的vo类
 *@author jingpj
 *@date Oct 16, 2009
 *@path com.est.workflow.process.vo.WfTodoWorkVO
 *@corporation Enstrong S&T
 */
public class WfTodoWorkVO {
	
	private Long taskId;
	
	private String taskName;
	
	private String taskDescription;

	private String taskStatus;

	private Long taskDefinitionId;
	
	private String processTitle;
	
	private Long wfProcessInstanceId;
	
	private Long masterId;
	
	private Date creatDate;

	private Date startDate;

	private Date endDate;

	private String actor;
	
	private String draftby;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Long getTaskDefinitionId() {
		return taskDefinitionId;
	}

	public void setTaskDefinitionId(Long taskDefinitionId) {
		this.taskDefinitionId = taskDefinitionId;
	}

	public Date getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getProcessTitle() {
		return processTitle;
	}

	public void setProcessTitle(String processTitle) {
		this.processTitle = processTitle;
	}

	public Long getWfProcessInstanceId() {
		return wfProcessInstanceId;
	}

	public void setWfProcessInstanceId(Long wfProcessInstanceId) {
		this.wfProcessInstanceId = wfProcessInstanceId;
	}

	public Long getMasterId() {
		return masterId;
	}

	public void setMasterId(Long masterId) {
		this.masterId = masterId;
	}

	public String getDraftby() {
		return draftby;
	}

	public void setDraftby(String draftby) {
		this.draftby = draftby;
	}
	
	
	

	

}
