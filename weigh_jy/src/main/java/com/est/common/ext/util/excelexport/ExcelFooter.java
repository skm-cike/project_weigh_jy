package com.est.common.ext.util.excelexport;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 *@desc Excel页脚
 *@author hebo
 *@date Jul 23, 2010
 *@path com.est.common.ext.util.excelexport.ExcelFooter
 *@corporation Enstrong S&T
 */

public class ExcelFooter {
	private List<ExcelItem> items=new ArrayList<ExcelItem>();
	private StringBuffer buf = new StringBuffer(200);
	public void addItem(ExcelItem item){
		this.items.add(item);
	}
	
	private List<ExcelItem> getItems(){
		return this.items;
	}
	
	public String getFooter(){
		for(ExcelItem item : this.getItems()){
			buf.append(item.getItem());
		}
		return buf.toString();
	}
}
