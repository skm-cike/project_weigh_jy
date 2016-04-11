package com.est.webservice.server.vo;

import java.util.Date;

public class Weight {
	private Long sequence;               //序号
	private String water_no;			 //流水号
	private String vehicle_no;			 //车号
	private String weight_type;			 //称重类型
	private String send_company;		 //发货单位
	private String recive_company;       //收货单位
	private String type;                 //货名
	private String guige;				 //规格
	private Double gross_weight;         //毛重
	private Double vehicle_weight;       //皮重
	private Double de_weight;            //扣重
	private Double net_weight;           //净重
	private String gross_man;			//毛重司磅人
	private String tare_man;            //皮重司磅人
	private Date tare_date;             //皮重时间
	private Date gross_date;            //毛重时间
	private String remark;              //备注
	private String outno;				//出门证号
	private String banbie;				//班别
	private Integer printCount;         //打印次数
	private String updateMan;          //更新人
	private Date updateDate;         //更新时间
	private String jizu;			 //机组
	
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
	public String getJizu() {
		return jizu;
	}
	public void setJizu(String jizu) {
		this.jizu = jizu;
	}
	public String getUpdateMan() {
		return updateMan;
	}
	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getPrintCount() {
		return printCount;
	}
	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}
	public Long getSequence() {
		return sequence;
	}
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	public String getWater_no() {
		return water_no;
	}
	public void setWater_no(String water_no) {
		this.water_no = water_no;
	}
	public String getVehicle_no() {
		return vehicle_no;
	}
	public void setVehicle_no(String vehicle_no) {
		this.vehicle_no = vehicle_no;
	}
	public String getWeight_type() {
		return weight_type;
	}
	public void setWeight_type(String weight_type) {
		this.weight_type = weight_type;
	}
	public String getSend_company() {
		return send_company;
	}
	public void setSend_company(String send_company) {
		this.send_company = send_company;
	}
	public String getRecive_company() {
		return recive_company;
	}
	public void setRecive_company(String recive_company) {
		this.recive_company = recive_company;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGuige() {
		return guige;
	}
	public void setGuige(String guige) {
		this.guige = guige;
	}
	public Double getGross_weight() {
		return gross_weight;
	}
	public void setGross_weight(Double gross_weight) {
		this.gross_weight = gross_weight;
	}
	public Double getVehicle_weight() {
		return vehicle_weight;
	}
	public void setVehicle_weight(Double vehicle_weight) {
		this.vehicle_weight = vehicle_weight;
	}
	public Double getDe_weight() {
		return de_weight;
	}
	public void setDe_weight(Double de_weight) {
		this.de_weight = de_weight;
	}
	public Double getNet_weight() {
		return net_weight;
	}
	public void setNet_weight(Double net_weight) {
		this.net_weight = net_weight;
	}
	public String getGross_man() {
		return gross_man;
	}
	public void setGross_man(String gross_man) {
		this.gross_man = gross_man;
	}
	public String getTare_man() {
		return tare_man;
	}
	public void setTare_man(String tare_man) {
		this.tare_man = tare_man;
	}
	public Date getTare_date() {
		return tare_date;
	}
	public void setTare_date(Date tare_date) {
		this.tare_date = tare_date;
	}
	public Date getGross_date() {
		return gross_date;
	}
	public void setGross_date(Date gross_date) {
		this.gross_date = gross_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
