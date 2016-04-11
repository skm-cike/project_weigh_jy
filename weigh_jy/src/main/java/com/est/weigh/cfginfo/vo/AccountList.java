package com.est.weigh.cfginfo.vo;

import java.util.Date;

/**
 * AccountList entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AccountList implements java.io.Serializable {

	// Fields

	private String id;
	private String accountType;
	private Double money;
	private String company;
	private String weighId;
	private Date operDate;
	private String remark;
	private String breedtype;
	private String grade;

	// Property accessors

	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getWeighId() {
		return this.weighId;
	}

	public void setWeighId(String weighId) {
		this.weighId = weighId;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBreedtype() {
		return this.breedtype;
	}

	public void setBreedtype(String breedtype) {
		this.breedtype = breedtype;
	}

	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}


}