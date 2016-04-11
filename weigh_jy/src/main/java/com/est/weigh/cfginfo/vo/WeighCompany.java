package com.est.weigh.cfginfo.vo;

import java.util.Date;

/**
 * WeighCompany entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WeighCompany implements java.io.Serializable {

	// Fields

	private String companyid;
	private String companyname;
	private String companycode;
	private String phone;
	private String atten;
	private String address;
	private String remark;
	private String type;       //货物代码
	private Long inouttype;  //1:收货单位 2:供货单位
	private Long status;
    private String beforename; //曾用名
//    private Date lasttime; //最后修改时间
    //=====临时变量======
    private String typename;   //货名
	// Constructors

	/** default constructor */
	public WeighCompany() {
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	/** minimal constructor */
	public WeighCompany(String companyid) {
		this.companyid = companyid;
	}

	/** full constructor */
	public WeighCompany(String companyid, String companyname, String companycode, String phone, String atten,
			String address, String remark, Long status) {
		this.companyid = companyid;
		this.companyname = companyname;
		this.companycode = companycode;
		this.phone = phone;
		this.atten = atten;
		this.address = address;
		this.remark = remark;
		this.status = status;
	}

	// Property accessors

	public String getCompanyid() {
		return this.companyid;
	}


	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

    public String getBeforename() {
        return beforename;
    }

    public void setBeforename(String beforename) {
        this.beforename = beforename;
    }

    public String getCompanycode() {
		return companycode;
	}

	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getInouttype() {
		return inouttype;
	}

	public void setInouttype(Long inouttype) {
		this.inouttype = inouttype;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAtten() {
		return this.atten;
	}

	public void setAtten(String atten) {
		this.atten = atten;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
}