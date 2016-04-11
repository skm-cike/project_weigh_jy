package com.est.weigh.cfginfo.vo;

/**
 * WeighWeighprice entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WeighWeighprice implements java.io.Serializable {

	// Fields

	private String id;
	private Double limitWeight;
	private Double weightPrice;
	private String priceid;

	// Constructors

	/** default constructor */
	public WeighWeighprice() {
	}

	/** minimal constructor */
	public WeighWeighprice(String id) {
		this.id = id;
	}

	/** full constructor */
	public WeighWeighprice(String id, Double limitWeight, Double weightPrice,
			String priceid) {
		this.id = id;
		this.limitWeight = limitWeight;
		this.weightPrice = weightPrice;
		this.priceid = priceid;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getLimitWeight() {
		return this.limitWeight;
	}

	public void setLimitWeight(Double limitWeight) {
		this.limitWeight = limitWeight;
	}

	public Double getWeightPrice() {
		return this.weightPrice;
	}

	public void setWeightPrice(Double weightPrice) {
		this.weightPrice = weightPrice;
	}

	public String getPriceid() {
		return this.priceid;
	}

	public void setPriceid(String priceid) {
		this.priceid = priceid;
	}

}