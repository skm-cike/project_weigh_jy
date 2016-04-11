package com.est.weigh.cfginfo.vo;

import java.util.Date;

import com.est.sysinit.sysdept.vo.SysDept;

public class WeighVehicle {
	private Long vehicleid;			//id
	private String vehicleno;		//车号
	private Double vehicleweight;	//车重
	private String remark;			//备注
	private SysDept sysDept;		//部门id
	private String inputman;		//输入人
	private Date inputtime;			//输入时间
	private String companycode;		//公司代码
	private String companyid;         //商家id
//	private Date lasttime; //最后修改时间
	
	//临时变量
	private String companyname;     //单位名称
	public Long getVehicleid() {
		return vehicleid;
	}
	public void setVehicleid(Long vehicleid) {
		this.vehicleid = vehicleid;
	}
	
	
	public String getCompanyid() {
		return companyid;
	}
	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}
	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	
	public String getCompanycode() {
		return companycode;
	}
	public void setCompanycode(String companycode) {
		this.companycode = companycode;
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
	
	public SysDept getSysDept() {
		return sysDept;
	}
	public void setSysDept(SysDept sysDept) {
		this.sysDept = sysDept;
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
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
}
