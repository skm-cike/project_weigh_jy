package com.est.weigh.report.vo;

/**
 * Dayreport entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Dayreport implements java.io.Serializable {

	// Fields

	private String reportid;
	private String reportDate;
	private String breedtype;
	private String grade;
	private String jizu;
	private String companyname;
	private String companycode;
	private String poundsNumber;
	private String outNumber;
	private String vehicleNo;
	private Double netWeight;
	private Long vehicleNum;
    private Double unitPrice;
    private Double totalMoney;

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        if (unitPrice == null) {
            unitPrice = 0d;
        }
        this.unitPrice = unitPrice;
    }

    public Double getTotalMoney() {
        if (totalMoney == null) {
            totalMoney = 0d;
        }
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getReportid() {
		return this.reportid;
	}

	public String getOutNumber() {
		return outNumber;
	}

	public void setOutNumber(String outNumber) {
		this.outNumber = outNumber;
	}

	public void setReportid(String reportid) {
		this.reportid = reportid;
	}

	public String getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
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

	public String getPoundsNumber() {
		return this.poundsNumber;
	}

	public void setPoundsNumber(String poundsNumber) {
		this.poundsNumber = poundsNumber;
	}

	public String getVehicleNo() {
		return this.vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public Double getNetWeight() {
		return this.netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public Long getVehicleNum() {
		return this.vehicleNum;
	}

	public void setVehicleNum(Long vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

	public String getJizu() {
		return jizu;
	}

	public void setJizu(String jizu) {
		this.jizu = jizu;
	}

}