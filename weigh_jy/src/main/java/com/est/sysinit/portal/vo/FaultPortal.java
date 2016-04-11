package com.est.sysinit.portal.vo;

import java.util.Date;

/**
 * 
 *@desc 缺陷考核
 *@author yzw
 *@date Dec 28, 2009
 *@path com.est.fault.fault.vo.FaultCheck
 *@corporation Enstrong S&T
 */
public class FaultPortal {

	
	private String dept; 			// 部门
	private int faultTotal;			// 缺陷总数
	
	private int aTypeAmount;		// A类缺陷数
	private int bTypeAmount;		// B类缺陷数
	private int cTypeAmount;		// C类缺陷数
	private int dTypeAmount;		// D类缺陷数
	
	private int needClearAmount;    // 需要消缺的数
	
	private int onepassAmount;      // 一次通过的数
	private double onepassreate;   // 一次通过率
	
	private int faultClearTotal;	// 已消缺数量
	private int faultIntimeTotal;   // 及时消缺的数
	private int faultfinishTotal;   // 完成总数
	
	private double faultClearRate;	// 消缺率
	private double faultIntimeRate; // 及时率
	
	private String faultType;		// 缺陷类别
	
	private String faultDeptname;	// 消缺部门
	

	
	
	
	
	
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public int getATypeAmount() {
		return aTypeAmount;
	}
	public void setATypeAmount(int typeAmount) {
		aTypeAmount = typeAmount;
	}
	public int getBTypeAmount() {
		return bTypeAmount;
	}
	public void setBTypeAmount(int typeAmount) {
		bTypeAmount = typeAmount;
	}
	public int getCTypeAmount() {
		return cTypeAmount;
	}
	public void setCTypeAmount(int typeAmount) {
		cTypeAmount = typeAmount;
	}
	public int getDTypeAmount() {
		return dTypeAmount;
	}
	public void setDTypeAmount(int typeAmount) {
		dTypeAmount = typeAmount;
	}
	public int getFaultTotal() {
		return faultTotal;
	}
	public void setFaultTotal(int faultTotal) {
		this.faultTotal = faultTotal;
	}
	public String getFaultType() {
		return faultType;
	}
	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}
	public String getFaultDeptname() {
		return faultDeptname;
	}
	public void setFaultDeptname(String faultDeptname) {
		this.faultDeptname = faultDeptname;
	}
	public int getFaultClearTotal() {
		return faultClearTotal;
	}
	public void setFaultClearTotal(int faultClearTotal) {
		this.faultClearTotal = faultClearTotal;
	}
	public int getFaultIntimeTotal() {
		return faultIntimeTotal;
	}
	public void setFaultIntimeTotal(int faultIntimeTotal) {
		this.faultIntimeTotal = faultIntimeTotal;
	}
	public int getFaultfinishTotal() {
		return faultfinishTotal;
	}
	public void setFaultfinishTotal(int faultfinishTotal) {
		this.faultfinishTotal = faultfinishTotal;
	}
	public double getFaultClearRate() {
		return faultClearRate;
	}
	public void setFaultClearRate(double faultClearRate) {
		this.faultClearRate = faultClearRate;
	}
	public double getFaultIntimeRate() {
		return faultIntimeRate;
	}
	public void setFaultIntimeRate(double faultIntimeRate) {
		this.faultIntimeRate = faultIntimeRate;
	}
	public int getNeedClearAmount() {
		return needClearAmount;
	}
	public void setNeedClearAmount(int needClearAmount) {
		this.needClearAmount = needClearAmount;
	}
	public int getOnepassAmount() {
		return onepassAmount;
	}
	public void setOnepassAmount(int onepassAmount) {
		this.onepassAmount = onepassAmount;
	}
	public double getOnepassreate() {
		return onepassreate;
	}
	public void setOnepassreate(double onepassreate) {
		this.onepassreate = onepassreate;
	}
	
	

	
	
	
}
