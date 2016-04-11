package com.est.sysinit.sysauthority.vo;

import com.est.sysinit.sysmodule.vo.SysModule;
import com.est.sysinit.sysuser.vo.SysUser;

/**
 * SysUsermodule entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysUsermodule implements java.io.Serializable {

	// Fields

	private Long usermoduleid;
	private SysModule sysModule;
	private SysUser sysUser;
	private String modulecode;
	private String rwflag;

	// Constructors

	/** default constructor */
	public SysUsermodule() {
	}

	/** full constructor */
	public SysUsermodule(SysModule sysModule, SysUser sysUser,
			String modulecode, String rwflag) {
		this.sysModule = sysModule;
		this.sysUser = sysUser;
		this.modulecode = modulecode;
		this.rwflag = rwflag;
	}

	// Property accessors

	public Long getUsermoduleid() {
		return this.usermoduleid;
	}

	public void setUsermoduleid(Long usermoduleid) {
		this.usermoduleid = usermoduleid;
	}

	public SysModule getSysModule() {
		return this.sysModule;
	}

	public void setSysModule(SysModule sysModule) {
		this.sysModule = sysModule;
	}

	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public String getModulecode() {
		return this.modulecode;
	}

	public void setModulecode(String modulecode) {
		this.modulecode = modulecode;
	}

	public String getRwflag() {
		return this.rwflag;
	}

	public void setRwflag(String rwflag) {
		this.rwflag = rwflag;
	}

}