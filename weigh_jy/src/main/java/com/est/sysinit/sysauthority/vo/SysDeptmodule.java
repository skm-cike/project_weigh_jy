package com.est.sysinit.sysauthority.vo;

import com.est.sysinit.sysmodule.vo.SysModule;

/**
 * SysDeptmodule entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysDeptmodule implements java.io.Serializable {

	// Fields

	private Long deptmoduleid;
	private SysModule sysModule;
	private Long deptid;

	// Constructors

	/** default constructor */
	public SysDeptmodule() {
	}

	/** full constructor */
	public SysDeptmodule(SysModule sysModule, Long deptid) {
		this.sysModule = sysModule;
		this.deptid = deptid;
	}

	// Property accessors

	public Long getDeptmoduleid() {
		return this.deptmoduleid;
	}

	public void setDeptmoduleid(Long deptmoduleid) {
		this.deptmoduleid = deptmoduleid;
	}

	public SysModule getSysModule() {
		return this.sysModule;
	}

	public void setSysModule(SysModule sysModule) {
		this.sysModule = sysModule;
	}

	public Long getDeptid() {
		return this.deptid;
	}

	public void setDeptid(Long deptid) {
		this.deptid = deptid;
	}

}