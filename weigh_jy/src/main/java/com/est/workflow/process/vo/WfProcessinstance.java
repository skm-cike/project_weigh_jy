package com.est.workflow.process.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.est.workflow.processdefination.vo.WfDfprocess;

/**
 * WfProcessinstance entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WfProcessinstance implements java.io.Serializable {

	// Fields

	private Long processinstanceId;
	private Long jbpmId;
	private WfDfprocess wfDfprocess;
	private Long masterid;
	private String topic;
	private String draftby;
	private Long draftbyId;
	private Date draftdate;
	private Date enddate;
	private String flowstate;
	private Long previoustask;
	private Long currenttask;
	private Set wfApproves = new HashSet(0);

	// Constructors

	/** default constructor */
	public WfProcessinstance() {
	}


	// Property accessors

	public Long getProcessinstanceId() {
		return this.processinstanceId;
	}

	public void setProcessinstanceId(Long processinstanceId) {
		this.processinstanceId = processinstanceId;
	}

	public WfDfprocess getWfDfprocess() {
		return this.wfDfprocess;
	}

	public void setWfDfprocess(WfDfprocess wfDfprocess) {
		this.wfDfprocess = wfDfprocess;
	}

	public Long getMasterid() {
		return this.masterid;
	}

	public void setMasterid(Long masterid) {
		this.masterid = masterid;
	}

	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}



	public String getDraftby() {
		return draftby;
	}


	public void setDraftby(String draftby) {
		this.draftby = draftby;
	}

	

	public Long getDraftbyId() {
		return draftbyId;
	}


	public void setDraftbyId(Long draftbyId) {
		this.draftbyId = draftbyId;
	}


	public Date getDraftdate() {
		return this.draftdate;
	}

	public void setDraftdate(Date draftdate) {
		this.draftdate = draftdate;
	}

	public String getFlowstate() {
		return this.flowstate;
	}

	public void setFlowstate(String flowstate) {
		this.flowstate = flowstate;
	}

	public Long getPrevioustask() {
		return this.previoustask;
	}

	public void setPrevioustask(Long previoustask) {
		this.previoustask = previoustask;
	}

	public Long getCurrenttask() {
		return this.currenttask;
	}

	public void setCurrenttask(Long currenttask) {
		this.currenttask = currenttask;
	}

	public Set getWfApproves() {
		return this.wfApproves;
	}

	public void setWfApproves(Set wfApproves) {
		this.wfApproves = wfApproves;
	}


	public Long getJbpmId() {
		return jbpmId;
	}


	public void setJbpmId(Long jbpmId) {
		this.jbpmId = jbpmId;
	}


	public Date getEnddate() {
		return enddate;
	}


	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	
	
	

}