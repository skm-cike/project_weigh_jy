package com.est.workflow.processdefination.vo;

import com.est.sysinit.sysmodule.vo.SysModulefield;

/**
 * WfDftaskfield entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WfDftaskfield implements java.io.Serializable {

	// Fields

	private Long taskfieldId;
	private WfDftask wfDftask;
	private SysModulefield sysModulefield;
	private String fieldCode;
	private String fieldName;
	private String isreadonly;
	private String isvisible;
	private Long ordernum;

	// Constructors

	/** default constructor */
	public WfDftaskfield() {
	}


	// Property accessors

	public Long getTaskfieldId() {
		return this.taskfieldId;
	}

	public void setTaskfieldId(Long taskfieldId) {
		this.taskfieldId = taskfieldId;
	}

	public WfDftask getWfDftask() {
		return this.wfDftask;
	}

	public void setWfDftask(WfDftask wfDftask) {
		this.wfDftask = wfDftask;
	}

	public SysModulefield getSysModulefield() {
		return this.sysModulefield;
	}

	public void setSysModulefield(SysModulefield sysModulefield) {
		this.sysModulefield = sysModulefield;
	}

	public String getFieldCode() {
		return this.fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getIsreadonly() {
		return this.isreadonly;
	}

	public void setIsreadonly(String isreadonly) {
		this.isreadonly = isreadonly;
	}

	public String getIsvisible() {
		return this.isvisible;
	}

	public void setIsvisible(String isvisible) {
		this.isvisible = isvisible;
	}


	public Long getOrdernum() {
		return ordernum;
	}


	public void setOrdernum(Long ordernum) {
		this.ordernum = ordernum;
	}
	

}