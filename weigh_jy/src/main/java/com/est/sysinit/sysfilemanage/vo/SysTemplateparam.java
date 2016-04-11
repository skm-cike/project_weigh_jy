package com.est.sysinit.sysfilemanage.vo;
// default package

/**
 * SysTemplateparam entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysTemplateparam implements java.io.Serializable {

	// Fields

	private Long paramid;
	private SysTemplate sysTemplate;
	private String paramname;
	private String paramtype;
	private String paramdesc;
	private Long paramorder;

	// Constructors

	/** default constructor */
	public SysTemplateparam() {
	}

	/** full constructor */
	public SysTemplateparam(SysTemplate sysTemplate, String paramname,
			String paramtype, String paramdesc, Long paramorder) {
		this.sysTemplate = sysTemplate;
		this.paramname = paramname;
		this.paramtype = paramtype;
		this.paramdesc = paramdesc;
		this.paramorder = paramorder;
	}

	// Property accessors

	public Long getParamid() {
		return this.paramid;
	}

	public void setParamid(Long paramid) {
		this.paramid = paramid;
	}

	public SysTemplate getSysTemplate() {
		return this.sysTemplate;
	}

	public void setSysTemplate(SysTemplate sysTemplate) {
		this.sysTemplate = sysTemplate;
	}

	public String getParamname() {
		return this.paramname;
	}

	public void setParamname(String paramname) {
		this.paramname = paramname;
	}

	public String getParamtype() {
		return this.paramtype;
	}

	public void setParamtype(String paramtype) {
		this.paramtype = paramtype;
	}

	public String getParamdesc() {
		return this.paramdesc;
	}

	public void setParamdesc(String paramdesc) {
		this.paramdesc = paramdesc;
	}

	public Long getParamorder() {
		return this.paramorder;
	}

	public void setParamorder(Long paramorder) {
		this.paramorder = paramorder;
	}

}