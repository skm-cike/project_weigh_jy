package com.est.workflow.process.vo;

import java.util.Date;

import org.jbpm.taskmgmt.exe.TaskInstance;

/**org.jbpm.taskmgmt.exe.TaskInstance
 * WfApprove entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WfApprove implements java.io.Serializable {

	// Fields

	private Long approveId;
	private WfProcessinstance wfProcessinstance;
	private Long processId;
	private Long jbpmpiid;
	private Long jbpmtiid;
	private String result;
	private String opinion;
	private String remark;
	private Long approveby;
	private String nameapproveby;
	private Date approvedate;
	private String taskName;

	// Constructors


	// Property accessors

	public Long getApproveId() {
		return this.approveId;
	}

	public void setApproveId(Long approveId) {
		this.approveId = approveId;
	}

	public WfProcessinstance getWfProcessinstance() {
		return this.wfProcessinstance;
	}

	public void setWfProcessinstance(WfProcessinstance wfProcessinstance) {
		this.wfProcessinstance = wfProcessinstance;
	}

	public Long getProcessId() {
		return this.processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public Long getJbpmpiid() {
		return this.jbpmpiid;
	}

	public void setJbpmpiid(Long jbpmpiid) {
		this.jbpmpiid = jbpmpiid;
	}

	public Long getJbpmtiid() {
		return this.jbpmtiid;
	}

	public void setJbpmtiid(Long jbpmtiid) {
		this.jbpmtiid = jbpmtiid;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getOpinion() {
		return this.opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getApproveby() {
		return this.approveby;
	}

	public void setApproveby(Long approveby) {
		this.approveby = approveby;
	}

	public String getNameapproveby() {
		return this.nameapproveby;
	}

	public void setNameapproveby(String nameapproveby) {
		this.nameapproveby = nameapproveby;
	}

	public Date getApprovedate() {
		return this.approvedate;
	}

	public void setApprovedate(Date approvedate) {
		this.approvedate = approvedate;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	

}