package com.est.common.ext.util.excelexport;

import java.util.ArrayList;
/**
 *@desc 配置Excel表头
 *@author jingpj
 *@date Jan 22, 2010
 *@path com.est.common.ext.util.excelexport.ExcelHeader
 *@corporation Enstrong S&T
 */
public class ExcelHeader {

	private String headerName;
	
	private ArrayList<ExcelHeader> headers;
	
	private int width = 130;
	
	private int len ;

	//constructor
	public ExcelHeader(){
		headers = new ArrayList<ExcelHeader>();
		
	}
	
	public ExcelHeader(String headerName,String... subHeaders){
		headers = new ArrayList<ExcelHeader>();
		this.headerName = headerName;
		for(String header : subHeaders) {
			headers.add(new ExcelHeader(header));
		}
	}
	
	public ExcelHeader(ExcelHeader parentHeader,String headerName,String... subHeaders){
		headers = new ArrayList<ExcelHeader>();
		this.headerName = headerName;
		for(String header : subHeaders) {
			headers.add(new ExcelHeader(header));
		}
		parentHeader.addSubHeader(this);
	}	
	
	//getter/setters
	public ArrayList<ExcelHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<ExcelHeader> headers) {
		this.headers = headers;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	
	
	public void addSubHeader(ExcelHeader header) {
		headers.add(header);
	}

	public void removeSubHeader(ExcelHeader header) {
		headers.remove(header);
	}
	
	public void clearSubHeader(ExcelHeader header) {
		headers.clear();
	}
	

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	/**
	 *@desc  构建表头
	 *@date Jan 22, 2010
	 *@author jingpj
	 */
	public String buildHeader(int headerDeep) {
		calLength();
		StringBuilder[] bufs = new StringBuilder[headerDeep];
		for(int i=0;i<headerDeep;i++) {
			bufs[i] = new StringBuilder(512);
			bufs[i].append("<TR>");
		}
		
		
		ArrayList<ExcelHeader> hs = this.headers;
		int deepcnt = 0;
		
		while (hs.size()>0) {
			ArrayList<ExcelHeader> tmp = new ArrayList<ExcelHeader>();
			
			for(ExcelHeader header:hs) {
				bufs[deepcnt].append(header);
				tmp.addAll(header.headers);
			}
			deepcnt ++ ;
			hs = tmp;
		}
		
		for(StringBuilder builder : bufs) {
			builder.append("</TR>");
		}
		
		StringBuilder result = new StringBuilder();
		for(StringBuilder builder : bufs) {
			result.append(builder);
		}	
		
		return result.toString();
		
		
	}
	
	/**
	 *@desc 计算每个表头单元格的长度
	 *@date Jan 22, 2010
	 *@author jingpj
	 *@return
	 */
	public int calLength(){
		int len = 1;
		if(headers.size()>0) {
			len = 0;
			for(ExcelHeader header : headers) {
				header.setLen(header.calLength());
				len += header.getLen();
			}
			
		}
		this.setLen(len);
		return len;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		setSubTitleHeaderWidth(width);
	}
	
	private void setSubTitleHeaderWidth(int width){
		for(ExcelHeader header : this.getHeaders()) {
			header.setWidth(width);
		}
	}

	@Override
	public String toString(){
		String colspan = "";
		String width = "width="+this.width;
		if(this.len>1) {
			colspan = "colspan="+this.len;
		}
//		if(this.width!=0 && this.len == 1) {
//			width = "width="+this.width;
//		}
		
		return "<TH style='text-align:center' " + colspan + " " +  width + ">"+headerName+"</TH>";
	}
	
	
}
