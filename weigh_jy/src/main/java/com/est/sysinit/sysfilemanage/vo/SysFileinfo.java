package com.est.sysinit.sysfilemanage.vo;

/**
 * SysFileinfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysFileinfo implements java.io.Serializable {

	// Fields

	private Long fileinfoId;
	private SysDir sysDir;
	private SysTemplate sysTemplate;
	private String filename;
	private String fileextension;
	private String url;
	private String fileno;

	// Constructors

	/** default constructor */
	public SysFileinfo() {
	}
	
	public SysFileinfo(SysFileinfo fileinfo,String filename){
		this.sysDir = fileinfo.sysDir;
		this.sysTemplate = fileinfo.sysTemplate;
		if(filename == null || "".equals(filename)){
			this.filename = fileinfo.filename;
		} else {
			this.filename = filename;
		}
		this.fileextension = fileinfo.fileextension;
	}

	// Property accessors

	public SysDir getSysDir() {
		return this.sysDir;
	}

	public void setSysDir(SysDir sysDir) {
		this.sysDir = sysDir;
	}

	public SysTemplate getSysTemplate() {
		return this.sysTemplate;
	}

	public void setSysTemplate(SysTemplate sysTemplate) {
		this.sysTemplate = sysTemplate;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFileextension() {
		return this.fileextension;
	}

	public void setFileextension(String fileextension) {
		this.fileextension = fileextension;
	}

	public String getFileno() {
		return this.fileno;
	}

	public void setFileno(String fileno) {
		this.fileno = fileno;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getFileinfoId() {
		return fileinfoId;
	}

	public void setFileinfoId(Long fileinfoId) {
		this.fileinfoId = fileinfoId;
	}
	
	

}