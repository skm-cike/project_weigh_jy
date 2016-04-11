package com.est.weigh.transaction.vo;

import java.util.Date;

/**
 * WeighReceivemoney entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WeighReceivemoney implements java.io.Serializable {

	// Fields

	private String id;
	private String companyname;
	private String companycode;
	private Double money;
	private String receiptno;
	private Date receivedate;
	private String operator;
	private String auditor;
	private Date audittime;
	private String breedtype;
	private String remark;
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

	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getReceiptno() {
		return this.receiptno;
	}

	public void setReceiptno(String receiptno) {
		this.receiptno = receiptno;
	}

	public Date getReceivedate() {
		return this.receivedate;
	}

	public void setReceivedate(Date receivedate) {
		this.receivedate = receivedate;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getAuditor() {
		return this.auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public Date getAudittime() {
		return this.audittime;
	}

	public void setAudittime(Date audittime) {
		this.audittime = audittime;
	}

	public String getBreedtype() {
		return this.breedtype;
	}

	public void setBreedtype(String breedtype) {
		this.breedtype = breedtype;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}