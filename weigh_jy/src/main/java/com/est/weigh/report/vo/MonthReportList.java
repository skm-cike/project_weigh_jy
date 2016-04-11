package com.est.weigh.report.vo;

import java.util.List;

public class MonthReportList {
	private String companycode;
	private String companyname;
	private Double jzje;
	private Double yfk;
	private Double chl;
	private Double xsjezj;
	private Double hkye;
	private String jizu;
	private String pz;
	private List<GradeForMonthReport>  gradeForMonth;
	
	
	private String startdate;
	private String enddate;
	private String username;
	
	
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPz() {
		return pz;
	}
	public void setPz(String pz) {
		this.pz = pz;
	}
	public String getJizu() {
		return jizu;
	}
	public void setJizu(String jizu) {
		this.jizu = jizu;
	}
	public String getCompanycode() {
		return companycode;
	}
	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public Double getJzje() {
		return jzje;
	}
	public void setJzje(Double jzje) {
		this.jzje = jzje;
	}
	public Double getYfk() {
		return yfk;
	}
	public void setYfk(Double yfk) {
		this.yfk = yfk;
	}
	public Double getChl() {
		return chl;
	}
	public void setChl(Double chl) {
		this.chl = chl;
	}
	public Double getXsjezj() {
		return xsjezj;
	}
	public void setXsjezj(Double xsjezj) {
		this.xsjezj = xsjezj;
	}
	public Double getHkye() {
		return hkye;
	}
	public void setHkye(Double hkye) {
		this.hkye = hkye;
	}
	public List<GradeForMonthReport> getGradeForMonth() {
		return gradeForMonth;
	}
	public void setGradeForMonth(List<GradeForMonthReport> gradeForMonth) {
		this.gradeForMonth = gradeForMonth;
	}
}
