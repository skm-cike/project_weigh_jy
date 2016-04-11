package com.est.sysinit.sysuser.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.est.sysinit.sysauthority.vo.SysUsermodule;
import com.est.sysinit.sysdept.vo.SysDept;
import com.est.sysinit.sysgroup.vo.SysGroupuser;


public class SysUser implements java.io.Serializable {

	private static final long serialVersionUID = -7685264767938664828L;
	// Fields

	private Long userid;
	private SysDept sysDept;
	private String username;
	private String usercode;
	private String login;
	private String password;
	private Date birthday;
	private String sex;
	private String duty;
	private String email;
	private String mobile;
	private String officetel;
	private Long userorder;
	private String isvalid;
	private String fp1;//指纹1
	private String fp2;//指纹2
	private String fp3;//指纹3

	private Set<SysGroupuser> sysGroupusers=new HashSet<SysGroupuser>(); //用户角色组
	private Set<SysUsermodule> sysUsermodules = new HashSet<SysUsermodule>(0);

	// Constructors
	
	
	/** default constructor */
	public SysUser() {
	}

	// Property accessors
	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public SysDept getSysDept() {
		return this.sysDept;
	}

	public void setSysDept(SysDept sysDept) {
		this.sysDept = sysDept;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsercode() {
		return this.usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDuty() {
		return this.duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOfficetel() {
		return this.officetel;
	}

	public void setOfficetel(String officetel) {
		this.officetel = officetel;
	}

	public Set<SysUsermodule> getSysUsermodules() {
		return this.sysUsermodules;
	}

	public void setSysUsermodules(Set<SysUsermodule> sysUsermodules) {
		this.sysUsermodules = sysUsermodules;
	}

	public Long getUserorder() {
		return userorder;
	}

	public void setUserorder(Long userorder) {
		this.userorder = userorder;
	}

	public String getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}

	public String getFp1() {
		return fp1;
	}

	public void setFp1(String fp1) {
		this.fp1 = fp1;
	}

	public String getFp2() {
		return fp2;
	}

	public void setFp2(String fp2) {
		this.fp2 = fp2;
	}

	public String getFp3() {
		return fp3;
	}

	public void setFp3(String fp3) {
		this.fp3 = fp3;
	}

	public Set<SysGroupuser> getSysGroupusers() {
		return sysGroupusers;
	}

	public void setSysGroupusers(Set<SysGroupuser> sysGroupusers) {
		this.sysGroupusers = sysGroupusers;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}