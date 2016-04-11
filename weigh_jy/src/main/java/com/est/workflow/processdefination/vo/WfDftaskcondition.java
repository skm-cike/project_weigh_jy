package com.est.workflow.processdefination.vo;

/**
 * WfDftaskcondition entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WfDftaskcondition implements java.io.Serializable {

	// Fields

	private Long taskconditionId;
	private WfDftask wfDftask;
	private String conditionexpression;
	private String transitionname;

	// Constructors

	/** default constructor */
	public WfDftaskcondition() {
	}

	/** full constructor */
	public WfDftaskcondition(WfDftask wfDftask, String conditionexpression,
			String transitionname) {
		this.wfDftask = wfDftask;
		this.conditionexpression = conditionexpression;
		this.transitionname = transitionname;
	}

	// Property accessors

	public Long getTaskconditionId() {
		return this.taskconditionId;
	}

	public void setTaskconditionId(Long taskconditionId) {
		this.taskconditionId = taskconditionId;
	}

	public WfDftask getWfDftask() {
		return this.wfDftask;
	}

	public void setWfDftask(WfDftask wfDftask) {
		this.wfDftask = wfDftask;
	}

	public String getConditionexpression() {
		return this.conditionexpression;
	}

	public void setConditionexpression(String conditionexpression) {
		this.conditionexpression = conditionexpression;
	}

	public String getTransitionname() {
		return this.transitionname;
	}

	public void setTransitionname(String transitionname) {
		this.transitionname = transitionname;
	}

}