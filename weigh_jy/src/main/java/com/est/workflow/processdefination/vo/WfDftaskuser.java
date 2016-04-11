package com.est.workflow.processdefination.vo;

import com.est.sysinit.sysgroup.vo.SysGroup;
import com.est.sysinit.sysuser.vo.SysUser;

/**
 * WfDftaskuser entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WfDftaskuser implements java.io.Serializable {

	// Fields

	private Long taskuserId;
	private WfDftask wfDftask;
	private SysGroup group;
	private SysUser user;
	private Long ordernum;
	private String remark;

	// Constructors

	/** default constructor */
	public WfDftaskuser() {
	}

	/** minimal constructor */
	public WfDftaskuser(WfDftask wfDftask) {
		this.wfDftask = wfDftask;
	}


	// Property accessors

	public Long getTaskuserId() {
		return this.taskuserId;
	}

	public void setTaskuserId(Long taskuserId) {
		this.taskuserId = taskuserId;
	}

	public WfDftask getWfDftask() {
		return this.wfDftask;
	}

	public void setWfDftask(WfDftask wfDftask) {
		this.wfDftask = wfDftask;
	}


	public SysGroup getGroup() {
		return group;
	}

	public void setGroup(SysGroup group) {
		this.group = group;
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public Long getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Long ordernum) {
		this.ordernum = ordernum;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}