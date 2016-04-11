package com.est.workflow.processdefination.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * WfDftask entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WfDftask implements java.io.Serializable {

	// Fields

	private Long taskId;
	private WfDfprocess wfDfprocess;
	private Long jbpmtid;
	private String name;
	private String description;
	private Double limitdays;
	private String isapproved;
	private String iscondition;
	private Long order;
	private String signcolumn;
	private Set wfDftaskusers = new HashSet(0);
	private Set wfDftaskconditions = new HashSet(0);

	// Constructors

	/** default constructor */
	public WfDftask() {
	}


	// Property accessors

	public Long getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public WfDfprocess getWfDfprocess() {
		return this.wfDfprocess;
	}

	public void setWfDfprocess(WfDfprocess wfDfprocess) {
		this.wfDfprocess = wfDfprocess;
	}

	public Long getJbpmtid() {
		return this.jbpmtid;
	}

	public void setJbpmtid(Long jbpmtid) {
		this.jbpmtid = jbpmtid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getLimitdays() {
		return this.limitdays;
	}

	public void setLimitdays(Double limitdays) {
		this.limitdays = limitdays;
	}

	public String getIsapproved() {
		return this.isapproved;
	}

	public void setIsapproved(String isapproved) {
		this.isapproved = isapproved;
	}

	public String getIscondition() {
		return this.iscondition;
	}

	public void setIscondition(String iscondition) {
		this.iscondition = iscondition;
	}

	public Long getOrder() {
		return this.order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public String getSigncolumn() {
		return this.signcolumn;
	}

	public void setSigncolumn(String signcolumn) {
		this.signcolumn = signcolumn;
	}

	public Set getWfDftaskusers() {
		return this.wfDftaskusers;
	}

	public void setWfDftaskusers(Set wfDftaskusers) {
		this.wfDftaskusers = wfDftaskusers;
	}

	public Set getWfDftaskconditions() {
		return this.wfDftaskconditions;
	}

	public void setWfDftaskconditions(Set wfDftaskconditions) {
		this.wfDftaskconditions = wfDftaskconditions;
	}

}