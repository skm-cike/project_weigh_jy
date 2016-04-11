package com.est.sysinit.sysgroup.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * SysGroup entity.
 * 
 * @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class SysGroup implements java.io.Serializable {

	// Fields

	private Long groupid;
	private String groupname;
	private String grouptype;
	private String groupdesc;
	private String orderindex;
	private Set sysGroupusers = new HashSet(0);

	// Property accessors

	public Long getGroupid() {
		return this.groupid;
	}

	public void setGroupid(Long groupid) {
		this.groupid = groupid;
	}

	public String getGroupname() {
		return this.groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getGrouptype() {
		return this.grouptype;
	}

	public void setGrouptype(String grouptype) {
		this.grouptype = grouptype;
	}

	public String getGroupdesc() {
		return this.groupdesc;
	}

	public void setGroupdesc(String groupdesc) {
		this.groupdesc = groupdesc;
	}

	public String getOrderindex() {
		return this.orderindex;
	}

	public void setOrderindex(String orderindex) {
		this.orderindex = orderindex;
	}

	public Set getSysGroupusers() {
		return this.sysGroupusers;
	}

	public void setSysGroupusers(Set sysGroupusers) {
		this.sysGroupusers = sysGroupusers;
	}

}