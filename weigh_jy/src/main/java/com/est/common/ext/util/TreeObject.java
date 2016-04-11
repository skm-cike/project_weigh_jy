package com.est.common.ext.util;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 5, 2009
 * @path com.est.common.ext.util
 * @name com.est.common.ext.util.TreeObject
 * @description EXT tree 数据封装对象
 */
public class TreeObject {
	
	private String id;
	private String text;
	private boolean leaf;
	private String qtip;
	private String code;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getQtip() {
		return qtip;
	}
	public void setQtip(String qtip) {
		this.qtip = qtip;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	

}
