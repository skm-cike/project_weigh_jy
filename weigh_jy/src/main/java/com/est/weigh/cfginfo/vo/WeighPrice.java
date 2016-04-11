package com.est.weigh.cfginfo.vo;

/**
 * PriceCfg entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WeighPrice implements java.io.Serializable {

	// Fields

	private String priceid;
	private String companyname;
	private String companycode;
	private String breedtype;
	private String grade;
	private Double unit_price;
	private String remark;
	private Long enableweighcfg;
	private String jizu;
	
	public String getJizu() {
		return jizu;
	}
	public void setJizu(String jizu) {
		this.jizu = jizu;
	}
	public String getPriceid() {
		return priceid;
	}
	public void setPriceid(String priceid) {
		this.priceid = priceid;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getCompanycode() {
		return companycode;
	}
	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}
	public String getBreedtype() {
		return breedtype;
	}
	public void setBreedtype(String breedtype) {
		this.breedtype = breedtype;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(Double unit_price) {
		this.unit_price = unit_price;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getEnableweighcfg() {
		return enableweighcfg;
	}
	public void setEnableweighcfg(Long enableweighcfg) {
		this.enableweighcfg = enableweighcfg;
	}

	
}