package com.est.weigh.report.vo;

/**
 * Monthreport entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Monthreport implements java.io.Serializable {

	// Fields

	private String reportid;
	private String companyname;
	private String companycode;
	private Double jzje;
	private Double yshk;
	private Double chl;
	private Double xsje;
	private String grade;
	private Double xsjexj;
	private Double hkye;
	private String yearmonth;
	private String startdate;
	private String enddate;
	private String breedtype;
	private Double danjia;
	private String jizu;
	private Long vehicles;
	// Constructors

	public String getJizu() {
		return jizu;
	}

	public void setJizu(String jizu) {
		this.jizu = jizu;
	}

	/** default constructor */
	public Monthreport() {
	}

	/** minimal constructor */
	public Monthreport(String reportid) {
		this.reportid = reportid;
	}

	/** full constructor */
	public Monthreport(String reportid, String companyname, String companycode,
			Double jzje, Double yshk, Double chl, Double xsje, String grade,
			Double xsjexj, Double hkye, String yearmonth, String startdate,
			String enddate) {
		this.reportid = reportid;
		this.companyname = companyname;
		this.companycode = companycode;
		this.jzje = jzje;
		this.yshk = yshk;
		this.chl = chl;
		this.xsje = xsje;
		this.grade = grade;
		this.xsjexj = xsjexj;
		this.hkye = hkye;
		this.yearmonth = yearmonth;
		this.startdate = startdate;
		this.enddate = enddate;
	}

	// Property accessors

	public String getReportid() {
		return this.reportid;
	}

	public void setReportid(String reportid) {
		this.reportid = reportid;
	}

	public String getCompanyname() {
		return this.companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getCompanycode() {
		return this.companycode;
	}

	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}

	public Double getJzje() {
		return this.jzje;
	}

	public void setJzje(Double jzje) {
		this.jzje = jzje;
	}

	public Double getYshk() {
		return this.yshk;
	}

	public void setYshk(Double yshk) {
		this.yshk = yshk;
	}

	public Double getChl() {
		return this.chl;
	}

	public void setChl(Double chl) {
		this.chl = chl;
	}

	public Double getXsje() {
		return this.xsje;
	}

	public void setXsje(Double xsje) {
		this.xsje = xsje;
	}

	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Double getXsjexj() {
		return this.xsjexj;
	}

	public void setXsjexj(Double xsjexj) {
		this.xsjexj = xsjexj;
	}

	public Double getHkye() {
		return this.hkye;
	}

	public void setHkye(Double hkye) {
		this.hkye = hkye;
	}

	public String getYearmonth() {
		return this.yearmonth;
	}

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	public String getStartdate() {
		return this.startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return this.enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getBreedtype() {
		return breedtype;
	}

	public void setBreedtype(String breedtype) {
		this.breedtype = breedtype;
	}

	public Double getDanjia() {
		return danjia;
	}

	public void setDanjia(Double danjia) {
		this.danjia = danjia;
	}

	public Long getVehicles() {
		return vehicles;
	}

	public void setVehicles(Long vehicles) {
		this.vehicles = vehicles;
	}
}