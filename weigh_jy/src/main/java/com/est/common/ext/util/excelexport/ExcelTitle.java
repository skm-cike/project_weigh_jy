package com.est.common.ext.util.excelexport;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *@desc Excel 标题
 *@author hebo
 *@date Jul 23, 2010
 *@path com.est.common.ext.util.excelexport.ExcelTitle
 *@corporation Enstrong S&T
 */
public class ExcelTitle {

	private List<ExcelItem> items=new ArrayList<ExcelItem>();
	private StringBuffer buf = new StringBuffer(200);
	
	
	public void addItem(ExcelItem item){
		this.items.add(item);
	}
	
	private List<ExcelItem> getItems(){
		return this.items;
	}
	
	public String getTitle(){
		for(ExcelItem item : this.getItems()){
			buf.append(item.getItem());
		}
		return buf.toString();
	}
	
	
}
