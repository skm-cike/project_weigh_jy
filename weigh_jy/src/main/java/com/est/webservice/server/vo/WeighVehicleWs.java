package com.est.webservice.server.vo;

import java.util.Date;

public class WeighVehicleWs {
	private Long vehicleid;			//id
	private String vehicleno;		//车号
	private Double vehicleweight;	//车重
	private String remark;			//备注
	private String inputman;		//输入人
	private Date inputtime;			//输入时间
	private String companycode;		//公司代码
	private String companyname;     //单位名称
	
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public Long getVehicleid() {
		return vehicleid;
	}
	public void setVehicleid(Long vehicleid) {
		this.vehicleid = vehicleid;
	}
	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	public Double getVehicleweight() {
		return vehicleweight;
	}
	public void setVehicleweight(Double vehicleweight) {
		this.vehicleweight = vehicleweight;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInputman() {
		return inputman;
	}
	public void setInputman(String inputman) {
		this.inputman = inputman;
	}
	public Date getInputtime() {
		return inputtime;
	}
	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}
	public String getCompanycode() {
		return companycode;
	}
	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}
}
