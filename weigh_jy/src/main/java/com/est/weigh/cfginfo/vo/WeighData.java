package com.est.weigh.cfginfo.vo;

import java.util.Date;

public class WeighData implements java.io.Serializable {
	private String id;
	private Double vehicle_weight;
	private Double gross_weight;
	private Double de_weight;
	private Double net_weight;
	private String vehicle_no;
	private String companyname;
	private String companycode;
	private String type;
	private String grade;
	private String jizu;
	private Date gross_time;
	private Date vehicle_time;
	private String remark;
	private String pounds_number;
	private String weighman;
	private String transferman;
	private Long inouttype;
	private Double unit_price;
	private Double total_price;
	private String isclosed;
	private Double water;
	private Date operatTime;
	private Integer dayreportMark;
	private Integer monthreportMark;
	private String tareweightman;
	
	private String outno;
	private String banbie;
	
	public String getOutno() {
		return outno;
	}

	public void setOutno(String outno) {
		this.outno = outno;
	}

	public String getBanbie() {
		return banbie;
	}

	public void setBanbie(String banbie) {
		this.banbie = banbie;
	}

	public String getTareweightman() {
		return tareweightman;
	}

	public void setTareweightman(String tareweightman) {
		this.tareweightman = tareweightman;
	}

	public String getCompanycode() {
		return companycode;
	}

	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}

	public Integer getDayreportMark() {
		return dayreportMark;
	}

	public void setDayreportMark(Integer dayreportMark) {
		this.dayreportMark = dayreportMark;
	}

	public Integer getMonthreportMark() {
		return monthreportMark;
	}

	public void setMonthreportMark(Integer monthreportMark) {
		this.monthreportMark = monthreportMark;
	}

	public Date getOperatTime() {
		return operatTime;
	}

	public void setOperatTime(Date operatTime) {
		this.operatTime = operatTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getNet_weight() {
		if (net_weight == null) {
			net_weight = 0d;
		}
		return net_weight;
	}

	public void setNet_weight(Double net_weight) {
		if (net_weight == null) {
			net_weight = 0d;
		} else if (net_weight < 1) {
			net_weight = 0d;
		}
		this.net_weight = net_weight;
	}

	public Double getVehicle_weight() {
		if (vehicle_weight == null) {
			vehicle_weight = 0d;
		}
		return vehicle_weight;
	}

	public void setVehicle_weight(Double vehicle_weight) {
		this.vehicle_weight = vehicle_weight;
	}

	public Double getGross_weight() {
		if (gross_weight == null) {
			gross_weight = 0d;
		}
		return gross_weight;
	}

	public void setGross_weight(Double gross_weight) {
		this.gross_weight = gross_weight;
	}

	public Double getDe_weight() {
		if (de_weight == null) {
			de_weight = 0d;
		}
		return de_weight;
	}

	public void setDe_weight(Double de_weight) {
		this.de_weight = de_weight;
	}

	public String getVehicle_no() {
		return vehicle_no;
	}

	public void setVehicle_no(String vehicle_no) {
		this.vehicle_no = vehicle_no;
	}


	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getJizu() {
		return jizu;
	}

	public void setJizu(String jizu) {
		this.jizu = jizu;
	}

	public Date getGross_time() {
		return gross_time;
	}

	public void setGross_time(Date gross_time) {
		this.gross_time = gross_time;
	}

	public Date getVehicle_time() {
		return vehicle_time;
	}

	public void setVehicle_time(Date vehicle_time) {
		this.vehicle_time = vehicle_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPounds_number() {
		return pounds_number;
	}

	public void setPounds_number(String pounds_number) {
		this.pounds_number = pounds_number;
	}

	public String getWeighman() {
		return weighman;
	}

	public void setWeighman(String weighman) {
		this.weighman = weighman;
	}

	public String getTransferman() {
		return transferman;
	}

	public void setTransferman(String transferman) {
		this.transferman = transferman;
	}

	public Long getInouttype() {
		return inouttype;
	}

	public void setInouttype(Long inouttype) {
		this.inouttype = inouttype;
	}

	public Double getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(Double unit_price) {
		this.unit_price = unit_price;
	}

	public Double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(Double total_price) {
		this.total_price = total_price;
	}

	public String getIsclosed() {
		return isclosed;
	}

	public void setIsclosed(String isclosed) {
		this.isclosed = isclosed;
	}

	public Double getWater() {
		return water;
	}

	public void setWater(Double water) {
		this.water = water;
	}

}
