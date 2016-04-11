package com.est.sysinit.sysgroup.vo;

import com.est.sysinit.sysuser.vo.SysUser;

/**
 * SysGroupuser entity.
 * 
 * @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class SysGroupuser implements java.io.Serializable {

	// Fields

	private Long groupuserid;
	private SysUser sysUser;
	private SysGroup sysGroup;

	// Property accessors

	public Long getGroupuserid() {
		return this.groupuserid;
	}

	public void setGroupuserid(Long groupuserid) {
		this.groupuserid = groupuserid;
	}

	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public SysGroup getSysGroup() {
		return this.sysGroup;
	}

	public void setSysGroup(SysGroup sysGroup) {
		this.sysGroup = sysGroup;
	}

}