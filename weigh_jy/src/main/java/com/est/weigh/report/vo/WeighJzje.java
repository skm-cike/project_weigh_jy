package com.est.weigh.report.vo;

import java.util.Date;

/**
 * WeighJzje entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WeighJzje implements java.io.Serializable {

	// Fields

	private String id;
	private String companycode;
	private String breedtype;
	private String yearmonth;
	private Double money;
	private Date operdate;
	private String jizu;

	public String getJizu() {
		return jizu;
	}

	public void setJizu(String jizu) {
		this.jizu = jizu;
	}
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanycode() {
		return this.companycode;
	}

	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}

	public String getBreedtype() {
		return this.breedtype;
	}

	public void setBreedtype(String breedtype) {
		this.breedtype = breedtype;
	}

	public String getYearmonth() {
		return this.yearmonth;
	}

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Date getOperdate() {
		return this.operdate;
	}

	public void setOperdate(Date operdate) {
		this.operdate = operdate;
	}

}