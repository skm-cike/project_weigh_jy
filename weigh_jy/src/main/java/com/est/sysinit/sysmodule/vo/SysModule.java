package com.est.sysinit.sysmodule.vo;

import java.util.HashSet;
import java.util.Set;


public class SysModule implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields
	/**
	 * 模块id
	 */
	private Long moduleid;
	/**
	 * 父模块
	 */
	private SysModule sysModule;
	/**
	 * 模块名称
	 */
	private String modulename;
	/**
	 * 模块编码
	 */
	private String modulecode;
	/**
	 * 是否公用只读模块
	 */
	private String publicflag;
	/**
	 * 模块url
	 */
	private String url;
	/**
	 * 模块排序序号
	 */
	private Long orderindex;
	/**
	 * 模块层次数（1-3）
	 */
	private Long levelnum;
	/**
	 * 模块是否有效
	 */
	private String isvalidity;
	/**
	 * 是否工作流标示？
	 */
	private String iswfflag;
	
	/**
	 * 该模块下的上传文件存放路径
	 */
	private String fileurl;
	
	/**
	 * 是否显示子菜单
	 */
	private String showsubmenu;
	/**
	 * 备注
	 */
	private String remark;
	
	/*
	private Set sysUsermodules = new HashSet(0);
	private Set sysDeptmodules = new HashSet(0);
	*/
	private Set sysModules = new HashSet(0);
	
	
	// Constructors

	/** default constructor */
	public SysModule() {
	}


	// Property accessors

	public Long getModuleid() {
		return this.moduleid;
	}

	public void setModuleid(Long moduleid) {
		this.moduleid = moduleid;
	}

	public SysModule getSysModule() {
		return this.sysModule;
	}

	public void setSysModule(SysModule sysModule) {
		this.sysModule = sysModule;
	}

	public String getModulename() {
		return this.modulename;
	}

	public void setModulename(String modulename) {
		this.modulename = modulename;
	}

	public String getModulecode() {
		return this.modulecode;
	}

	public void setModulecode(String modulecode) {
		this.modulecode = modulecode;
	}

	public String getPublicflag() {
		return this.publicflag;
	}

	public void setPublicflag(String publicflag) {
		this.publicflag = publicflag;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getOrderindex() {
		return this.orderindex;
	}

	public void setOrderindex(Long orderindex) {
		this.orderindex = orderindex;
	}



	public Long getLevelnum() {
		return levelnum;
	}


	public void setLevelnum(Long levelnum) {
		this.levelnum = levelnum;
	}


	public String getIsvalidity() {
		return this.isvalidity;
	}

	public void setIsvalidity(String isvalidity) {
		this.isvalidity = isvalidity;
	}

	public String getIswfflag() {
		return this.iswfflag;
	}

	public void setIswfflag(String iswfflag) {
		this.iswfflag = iswfflag;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public String getShowsubmenu() {
		return showsubmenu;
	}


	public void setShowsubmenu(String showsubmenu) {
		this.showsubmenu = showsubmenu;
	}


	public Set getSysModules() {
		return sysModules;
	}


	public void setSysModules(Set sysModules) {
		this.sysModules = sysModules;
	}
	
	
	

}