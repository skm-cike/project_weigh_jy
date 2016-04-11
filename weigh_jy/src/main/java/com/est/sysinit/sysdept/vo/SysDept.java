package com.est.sysinit.sysdept.vo;

import java.util.HashSet;
import java.util.Set;


public class SysDept implements java.io.Serializable {

	// Fields

	private Long deptid;
	private SysDept sysDept;
	private String deptname;
	private String deptcode;
	private Long deptorder;
	private Set sysDepts = new HashSet(0);
	private Set sysUsers = new HashSet(0);


	/** default constructor */
	public SysDept() {
	}


	// Property accessors
	public Long getDeptid() {
		return this.deptid;
	}

	public void setDeptid(Long deptid) {
		this.deptid = deptid;
	}

	public SysDept getSysDept() {
		return this.sysDept;
	}

	public void setSysDept(SysDept sysDept) {
		this.sysDept = sysDept;
	}

	public String getDeptname() {
		return this.deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getDeptcode() {
		return this.deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}
	
	

	public Long getDeptorder() {
		return deptorder;
	}


	public void setDeptorder(Long deptorder) {
		this.deptorder = deptorder;
	}


	public Set getSysDepts() {
		return this.sysDepts;
	}

	public void setSysDepts(Set sysDepts) {
		this.sysDepts = sysDepts;
	}

	public Set getSysUsers() {
		return this.sysUsers;
	}

	public void setSysUsers(Set sysUsers) {
		this.sysUsers = sysUsers;
	}

}