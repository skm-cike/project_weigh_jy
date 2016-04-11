package com.est.common.ext.util.excelexport;

import java.util.ArrayList;
import java.util.List;

/**
 *@desc EXCEL导出的数据
 *@author jingpj
 *@date Jan 22, 2010
 *@path com.est.common.ext.util.excelexport.ExcelExportData
 *@corporation Enstrong S&T
 */
public class ExcelExportData {
	
	/** 导出文件名 */
	private String fileName;
	
	/** 标题 */
	private ExcelTitle caption;
	
	/** 页脚 **/
	private ExcelFooter footer;
	
	/** 表头 */
	private ExcelHeader header;
	
	/** 表头行数 */
	private int headerDeep;
	
	/** 数据 */
	private List<Object[]> data;
	
	/** 结果 */
	private String result;

	
	
	
	@Override
	public String toString() {
		
		
		return this.result;
	}
	
	/**
	 *@desc 生成table body 
	 *@date Jan 22, 2010
	 *@author jingpj
	 *@return
	 */
	private String buildeBody() {
		StringBuilder buf = new StringBuilder(1024);
		try{
			for(Object[] objs : data) {
				buf.append("<TR align=center>");
				for(Object obj : objs) {
					buf.append("<TD>");
					buf.append(obj==null ? "" : obj);
					buf.append("</TD>");
				}
				buf.append("</TR>");
			}
		}catch (ClassCastException ex) {
			for(Object objs : data) {
				buf.append("<TR align=center>");
					buf.append("<TD>");
					buf.append(objs==null ? "" : objs);
					buf.append("</TD>");
				buf.append("</TR>");
			}
		}
		
		return buf.toString();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ExcelHeader getHeader() {
		return header;
	}

	public void setHeader(ExcelHeader header) {
		this.header = header;
	}

	public int getHeaderDeep() {
		return headerDeep;
	}

	public void setHeaderDeep(int headerDeep) {
		this.headerDeep = headerDeep;
	}

	public List<Object[]> getData() {
		return data;
	}

	public void setData(List<Object[]> data) {
		this.data = data;
	}
	
	public String getResult() {
		if(this.result == null) {
			StringBuilder buf = new StringBuilder(512);
			buf.append("<HTML>");
			buf.append("<HEAD>");
			
			buf.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />");
			buf.append("<TITLE>");
			buf.append(header.getHeaderName());
			buf.append("</TITLE>");
			buf.append("</HEAD>");
			
			buf.append("<BODY>");
			buf.append("<TABLE border=1 style='border-collapse:collapse'>");
			if(this.caption != null){
				buf.append(this.caption.getTitle());
			}
			buf.append(header.buildHeader(headerDeep));
			buf.append(buildeBody());
			if(this.footer != null){
				buf.append(this.footer.getFooter());
			}
			buf.append("</TABLE>");
			buf.append("</BODY>");
			buf.append("</HTML>");
			
			this.result = buf.toString();
		}
		
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Deprecated
	public static void main(String[] args) {
		ExcelExportData sss = new ExcelExportData();
		ArrayList lst = new ArrayList();
		lst.add(new Object[]{"a2","a3","a4"});
		lst.add(new Object[]{"b2","b3","b4"});
		
		sss.setData(lst);
		sss.setFileName("ssss");
		
		ExcelHeader h1 = new ExcelHeader("title");
		
		ExcelHeader h2 = new ExcelHeader(h1,"b1","1","2");
		ExcelHeader h3 = new ExcelHeader(h1,"b2","3");
		sss.setHeader(h1);
		sss.setHeaderDeep(2);
		System.out.println(sss);
		
	}

	public ExcelTitle getCaption() {
		return caption;
	}

	public void setCaption(ExcelTitle caption) {
		this.caption = caption;
	}

	public ExcelFooter getFooter() {
		return footer;
	}

	public void setFooter(ExcelFooter footer) {
		this.footer = footer;
	}

	
	
}
