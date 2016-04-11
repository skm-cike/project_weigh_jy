package com.est.common.ext.util.frontdatautil;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 22, 2009
 * @path com.est.common.ext
 * @name com.est.common.ext.JsonFieldFormat
 * @description 业务对象字段转换成json的格式
 */
public class JsonFieldFormat {
	/**
	 * 原名称
	 */
	private String oldName;
	/**
	 * 新名称
	 */
	private String newName;
	/**
	 * 转换成字符串时的格式
	 */
	private String format;
	
	public String getOldName() {
		return oldName;
	}
	
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	
	public String getNewName() {
		return newName;
	}
	
	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	
}
