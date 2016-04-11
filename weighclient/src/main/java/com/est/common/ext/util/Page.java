package com.est.common.ext.util;

import com.est.common.ext.util.classutil.StringUtil;

/**
 * 分页帮助类
 * @author jingpj
 *
 */
public class Page {
	
	/** 每页的行数 */
	private int rowPerPage;
	/** 当前的页数 */
	private int curPage;
	/** 总行数 */
	private int totalRows;
	/** 总页数 */
	private int totalPages;
	/**当前页开始行索引*/
	private int startRowIndex;
	
	/** constructors*/
	public Page(){
		rowPerPage = 20;
		
	} 
	
	public Page(int rowPerPage){
		this.rowPerPage = rowPerPage;
	}
	
	public Page(String start,String limit){
		this.rowPerPage = StringUtil.parseInt(limit, 20); //如果limit为null，或者格式错误，默认设置为20
		this.startRowIndex = StringUtil.parseInt(start, 0);//如果start为null，或者格式错误，默认设置为0
	}
	
	public Page(int start,int limit){
		this.rowPerPage = limit;
		this.startRowIndex = start;
	}
	
	
	/**  根据查询到的总行数分页 */
	public Page pagging(int totalRows){
		this.totalRows = totalRows;
		/*
		this.totalPages = (totalRows + this.rowPerPage -1 )/this.rowPerPage;
		if(this.curPage>this.totalPages) {
			this.curPage = this.totalPages;
		}
		this.startRowIndex = (this.curPage-1)*this.rowPerPage;
		*/
		return this;
	}
	
		
	/** getter/setter */
	public int getRowPerPage() {
		return rowPerPage;
	}

	public void setRowPerPage(int rowPerPage) {
		this.rowPerPage = rowPerPage;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getStartRowIndex() {
		return startRowIndex;
	}

	public void setStartRowIndex(int startRowIndex) {
		this.startRowIndex = startRowIndex;
	}
	
	
	
}
