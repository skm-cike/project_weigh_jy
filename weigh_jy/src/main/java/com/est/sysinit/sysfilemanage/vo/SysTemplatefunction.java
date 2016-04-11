package com.est.sysinit.sysfilemanage.vo;
// default package

import java.util.HashSet;
import java.util.Set;

/**
 * SysTemplatefunction entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysTemplatefunction implements java.io.Serializable {

	// Fields

	private Long functionid;
	private SysTemplate sysTemplate;
	private String functioncode;
	private String functiondesc;
	private String functioncontent;
	private String functionparam;
	private Long executeorder;
	private Set sysTemplateparams = new HashSet(0);

	// Constructors

	/** default constructor */
	public SysTemplatefunction() {
	}

	/** full constructor */
	public SysTemplatefunction(SysTemplate sysTemplate, String functioncode,
			String functiondesc, String functioncontent,String functionparam, Long executeorder,
			Set sysTemplateparams) {
		this.sysTemplate = sysTemplate;
		this.functioncode = functioncode;
		this.functiondesc = functiondesc;
		this.functioncontent = functioncontent;
		this.functionparam = functionparam;
		this.executeorder = executeorder;
		this.sysTemplateparams = sysTemplateparams;
	}

	// Property accessors

	public Long getFunctionid() {
		return this.functionid;
	}

	public void setFunctionid(Long functionid) {
		this.functionid = functionid;
	}

	public SysTemplate getSysTemplate() {
		return this.sysTemplate;
	}

	public void setSysTemplate(SysTemplate sysTemplate) {
		this.sysTemplate = sysTemplate;
	}

	public String getFunctioncode() {
		return this.functioncode;
	}

	public void setFunctioncode(String functioncode) {
		this.functioncode = functioncode;
	}

	public String getFunctiondesc() {
		return this.functiondesc;
	}

	public void setFunctiondesc(String functiondesc) {
		this.functiondesc = functiondesc;
	}

	public String getFunctioncontent() {
		return this.functioncontent;
	}

	public void setFunctioncontent(String functioncontent) {
		this.functioncontent = functioncontent;
	}

	public Long getExecuteorder() {
		return this.executeorder;
	}

	public void setExecuteorder(Long executeorder) {
		this.executeorder = executeorder;
	}

	public Set getSysTemplateparams() {
		return this.sysTemplateparams;
	}

	public void setSysTemplateparams(Set sysTemplateparams) {
		this.sysTemplateparams = sysTemplateparams;
	}

	public String getFunctionparam() {
		return functionparam;
	}

	public void setFunctionparam(String functionparam) {
		this.functionparam = functionparam;
	}

}