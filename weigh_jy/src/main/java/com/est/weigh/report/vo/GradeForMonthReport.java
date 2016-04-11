package com.est.weigh.report.vo;

import java.util.List;

public class GradeForMonthReport {
	private String grade;
	private Double chl;
	private Double xsje;
	private List<UnitForMonthReport> unitReport;
	public List<UnitForMonthReport> getUnitReport() {
		return unitReport;
	}
	public void setUnitReport(List<UnitForMonthReport> unitReport) {
		this.unitReport = unitReport;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Double getChl() {
		return chl;
	}
	public void setChl(Double chl) {
		this.chl = chl;
	}
	public Double getXsje() {
		return xsje;
	}
	public void setXsje(Double xsje) {
		this.xsje = xsje;
	}
}
