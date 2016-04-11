package com.est.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.est.common.ext.util.classutil.DateUtil;

/**
 * 
 *@desc 导出普通报表工具类
 *@author LuHua
 *@date 2011-11-18
 *@path com.est.fuel.quantity.common.ExcelToFile
 *@corporation Enstrong S&T
 */
public abstract class ExcelToFile<T> {
	protected List<T> datas;
	protected String title;
	protected String[] needFields;
	protected String[] tableHead;
	protected Object headValue;
	protected Object rootValue;
	
	protected Integer rowIndex;
	protected ServletOutputStream os;
	protected WritableWorkbook book;
	protected WritableSheet sheet;
	protected WritableCellFormat normalFormat;
	
	protected Integer titleSize;
	protected Integer normalSize;
	protected Integer headerAndRootSize;
	public ExcelToFile() {
	}
	public ExcelToFile(HttpServletResponse response, String fileName, List<T> datas, String title, String[] needFields, String[] tableHead) {
		super();
		this.setResponse(response, fileName);
		this.setDatas(datas);
		this.setTitle(title);
		this.setNeedFields(needFields);
		this.setTableHead(tableHead);
	}
	
	public void setNeedFields(String[] needFields) {
		this.needFields = needFields;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setResponse(HttpServletResponse response, String fileName) {
		try {
			String _fileName = java.net.URLEncoder.encode(fileName,"UTF-8");
			response.setHeader("Content-Type", "application/vnd.ms-excel" + ";charset='UTF-8'");
			response.setHeader("Content-Type", "application/force-download");
			response.setHeader("Connection","close");   
			response.setHeader("Content-Type","application/octet-stream"); 
			response.setHeader("Content-Disposition", "attachment;filename="+ _fileName + ".xls");
			os = response.getOutputStream();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void setTitleSize(Integer titleSize) {
		this.titleSize = titleSize;
	}

	public void setNormalSize(Integer normalSize) {
		this.normalSize = normalSize;
	}

	public void setHeadValue(Object headValue) {
		this.headValue = headValue;
	}

	public void setRootValue(Object rootValue) {
		this.rootValue = rootValue;
	}

	public void setHeaderAndRootSize(Integer headerAndRootSize) {
		this.headerAndRootSize = headerAndRootSize;
	}

	public void setTableHead(String[] tableHead) {
		this.tableHead = tableHead;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	protected void createBody(Object... dateformat) {
		for (T o: datas) {
			addBody(o,dateformat);
		}
	}
	/**
	 * 
	 * @param o
	 * @param dateformat 日期显示格式
	 */
	protected void addBody(T o,Object... dateformat) {
		int column = 0;
		Class clazz = o.getClass();
		String[] values = new String[needFields.length];
		for (int i = 0; i < needFields.length; i++) {
			String[] temp = needFields[i].split("\\.");
			String methodName = null;
			Object temp_o = o;                              //临时对象
			Class temp_clazz = clazz;                       //临时类类
			for (int j = 0; j < temp.length; j++) {
				methodName = "get" + firstToUpperCase(temp[j]);
				
				try {
					Object o1 = temp_clazz.getMethod(methodName).invoke(temp_o);
					if (j != temp.length - 1) {
						temp_clazz = o1.getClass();
					}
					temp_o = o1;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
			if (null == temp_o) {
				temp_o = "";
			}
			if (temp_o instanceof Date) {
				if (dateformat != null && dateformat.length > 0) {
					temp_o = DateUtil.format((Date) temp_o, dateformat[0].toString());
				}else{
					temp_o = DateUtil.format((Date) temp_o);
				}
			}
			values[i] = temp_o + "";
		}
		try {
			for (int i = 0; i < values.length; i++) {
				try {
					normalFormat = new WritableCellFormat(new WritableFont(WritableFont.TIMES, normalSize));
					normalFormat.setAlignment(jxl.format.Alignment.RIGHT);
					normalFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
					normalFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
					normalFormat.setWrap(true);
					Double v = Double.parseDouble(values[i]);
					WritableCell cell = new jxl.write.Number(column, rowIndex, v, normalFormat);
					sheet.addCell(cell);
				} catch (NumberFormatException e) {
					normalFormat = new WritableCellFormat(new WritableFont(WritableFont.TIMES, normalSize));
					normalFormat.setAlignment(jxl.format.Alignment.LEFT);
					normalFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
					normalFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
					normalFormat.setWrap(true);
					Label label = new Label(column, rowIndex, values[i], normalFormat);
					sheet.addCell(label);
				}
				column++;
			}
			rowIndex++;
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}
	
	protected void creatTitle() {
		if (titleSize == null) {
			titleSize = 11;
		}
		WritableCellFormat titleFormat = new WritableCellFormat(new WritableFont(WritableFont.TIMES, titleSize,WritableFont.BOLD));
		try {
			titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
			titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			titleFormat.setBorder(Border.NONE, BorderLineStyle.NONE);
		} catch (WriteException e1) {
			e1.printStackTrace();
		}
		if (rowIndex == null) {
			rowIndex = 0;
		}
		Label label = new Label(0, rowIndex, title, titleFormat);
		try {
			sheet.addCell(label);
			sheet.mergeCells(0, rowIndex, needFields.length-1, rowIndex);
			rowIndex++;
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}
	
	protected void creatTableHead() {
		Integer column = 0;
		try {
			WritableCellFormat format = new WritableCellFormat(new WritableFont(WritableFont.TIMES, normalSize));
			format.setAlignment(jxl.format.Alignment.CENTRE);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBorder(Border.ALL, BorderLineStyle.THIN);
			for (int i=0; i < tableHead.length; i++) {
				Label label = new Label(column,rowIndex, tableHead[i], format);
				sheet.addCell(label);
				column++;
			}
			rowIndex++;
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}
	
	protected void init() {
		rowIndex = 0;
		try {
			book = Workbook.createWorkbook(os);
			sheet = book.createSheet("第一页", 0);
			if (normalSize == null) {
				normalSize = 9;
			}
			normalFormat = new WritableCellFormat(new WritableFont(WritableFont.TIMES, normalSize));
			normalFormat.setAlignment(jxl.format.Alignment.CENTRE);
			normalFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			normalFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			normalFormat.setWrap(true);
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createExcel(Object... dateformat) {
		init();
		creatTitle();
		creatHead();
		creatTableHead();
		createBody(dateformat);
		creatRoot();
		formatTable();
		close();
	}
	
	protected void close() {
		try {
			book.write();
			book.close();
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	protected String firstToUpperCase(String str) {
		char[] cs = str.toCharArray();
		cs[0] = Character.toUpperCase(cs[0]);
		return new String(cs);
	}
	
	protected abstract void creatRoot();
	protected abstract void creatHead();
	protected abstract void formatTable();
}
