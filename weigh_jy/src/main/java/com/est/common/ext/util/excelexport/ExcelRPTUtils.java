package com.est.common.ext.util.excelexport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.est.common.ext.util.classutil.NumberUtil;

/**
 * 
 * @desc excel报表模版工具类
 * @author heb
 * @date 2011-8-11
 * @path com.xls.ExcelRPTUtils
 * @corporation Enstrong S&T
 */
public class ExcelRPTUtils {

	private List<Object[]> datas;
	private int startRow;
	private String xlsTplPath;
	private String rptPath;
	private int sheet;
	private WritableWorkbook wb;
	private boolean border = true;

	/**
	 * 
	 * @desc 设置有无边框
	 * @date 2011-8-10
	 * @author heb
	 * @param border
	 */
	public void setBorder(boolean border) {
		this.border = border;
	}

	/**
	 * Constructors
	 * 
	 * @param xlsTplPath
	 *            模版文件路径
	 * @param rptPath
	 *            报表输出路径
	 */
	public ExcelRPTUtils(String xlsTplPath, String rptPath) {
		this.xlsTplPath = xlsTplPath;
		this.rptPath = rptPath;

		if (this.xlsTplPath == null) {
			throw new RuntimeException("报表模版文件路径为空");
		}
		if (this.rptPath == null) {
			throw new RuntimeException("报表输出路径为空");
		}

		try {

			Workbook wbin = Workbook.getWorkbook(new File(this.xlsTplPath));
			wb = Workbook.createWorkbook(new File(this.rptPath), wbin);

		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @desc 设置sheet
	 * @date 2011-8-10
	 * @author heb
	 * @param sheet
	 */
	public void setSheet(int sheet) {
		this.sheet = sheet;
	}

	/**
	 * 
	 * @desc 设置某行的填充数据
	 * @date 2011-8-10
	 * @author heb
	 * @param r
	 *            行号
	 * @throws Exception
	 */
	public void setRowData(int r, Object[] data) throws Exception {

		WritableSheet ws = wb.getSheet(sheet);
		for (int i = 0; i < data.length; i++) {

			String v = "";

			if (data[i] != null) {
				v = (data[i]).toString();
			}

			WritableCell lb = null;
			
			try {
				Double d = Double.parseDouble(v);
				int scale = 0;
				if (v.contains(".")) {
					scale = v.split("\\.")[1].length();
				}
				
				d = NumberUtil.round(d, scale);
				lb = new jxl.write.Number(i, r, d);
				setNumberScale(lb, scale);
			} catch (NumberFormatException e) {
				lb = new Label(i, r, v);
			}

			if (this.border) {
				this.drawBorder(lb);
			}

			ws.addCell(lb);

		}
	}

	/**
	 * 
	 * @desc 设置列填充值
	 * @date 2011-8-10
	 * @author heb
	 * @param r
	 *            行号
	 * @param c
	 *            列号
	 * @param value
	 *            值
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws IOException
	 */
	public WritableCell setColData(int r, int c, Object value) throws RowsExceededException, WriteException, IOException {

		WritableSheet ws = wb.getSheet(sheet);
		String v = "";
		if (value != null) {
			v = value.toString();
		}
		
		WritableCell lb = null;
		
		try {
			Double d = Double.parseDouble(v);
			int scale = 0;
			if (v.contains(".")) {
				scale = v.split("\\.")[1].length();
			}
			
			d = NumberUtil.round(d, scale);
			lb = new jxl.write.Number(c, r, d);
			setNumberScale(lb, scale);
		} catch (NumberFormatException e) {
			lb = new Label(c, r, v);
		}
		
		if (this.border) {
			this.drawBorder(lb,1);
		}
		ws.addCell(lb);
		return lb;
	}

	
	/**
	 * 
	 * @desc 设置列填充值
	 * @date 2011-8-10
	 * @author heb
	 * @param r
	 *            行号
	 * @param c
	 *            列号
	 * @param value
	 *            值
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws IOException
	 */
	public WritableCell setColData(int r, int c, Object value,String fonttype,int fontsize) throws RowsExceededException, WriteException, IOException {

		WritableSheet ws = wb.getSheet(sheet);
		String v = "";
		if (value != null) {
			v = value.toString();
		}
		
		WritableCell lb = null;
		
		try {
			Double d = Double.parseDouble(v);
			int scale = 0;
			if (v.contains(".")) {
				scale = v.split("\\.")[1].length();
			}
			
			d = NumberUtil.round(d, scale);
			lb = new jxl.write.Number(c, r, d);
			setNumberScale(lb, scale);
		} catch (NumberFormatException e) {
			lb = new Label(c, r, v);
		}
		
		if (this.border) {
			this.drawBorder(lb,fonttype,fontsize);
		}
		ws.addCell(lb);
		return lb;
	}
	/**
	 * 
	 * @desc 设置列填充值
	 * @date 2011-8-10
	 * @author heb
	 * @param r
	 *            行号
	 * @param c
	 *            列号
	 * @param value值
	 * @param alignment_center  水平是否居中
	 * @param verticalAlignment_center 垂直是否居中
	 * @param newline 是否换行
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws IOException
	 * 
	 *  
	 */
	public WritableCell setColData(int r, int c, Object value, boolean alignment_center,boolean verticalAlignment_center, boolean newline) throws RowsExceededException,
			WriteException, IOException {

		WritableSheet ws = wb.getSheet(sheet);
		String v = "";
		if (value != null) {
			v = value.toString();
		}
		
		WritableCell lb = null;
		
		try {
			Double d = Double.parseDouble(v);
			int scale = 0;
			if (v.contains(".")) {
				scale = v.split("\\.")[1].length();
			}
			d = NumberUtil.round(d, scale);
			lb = new jxl.write.Number(c, r, d);
			setNumberScale(lb, scale);
		} catch (NumberFormatException e) {
			lb = new Label(c, r, v);
		}
		
		if (this.border) {
			this.drawBorder(lb, alignment_center,verticalAlignment_center,newline);
		}
		ws.addCell(lb);
		return lb;
	}

	/**
	 * 
	 * @desc 合并单元格
	 * @date 2011-8-10
	 * @author heb
	 * @param col1
	 * @param row1
	 * @param col2
	 * @param row2
	 * @throws Exception
	 */
	public void mergeCells(int col1, int row1, int col2, int row2) throws Exception {

		WritableSheet ws = wb.getSheet(sheet);

		ws.mergeCells(col1, row1, col2, row2);

	}

	/**
	 * 
	 * @desc 刷新数据
	 * @date 2011-8-10
	 * @author heb
	 * @param wb
	 * @throws IOException
	 */
	public FileInputStream flushData() throws IOException {

		wb.write();
		wb.close();

		FileInputStream fis = new FileInputStream(this.rptPath);

		return fis;

	}

	/**
	 * 
	 * @desc 填充数据
	 * @date 2011-8-10
	 * @author heb
	 * @param writewb
	 * @throws Exception
	 */
	private void putInData() throws Exception {

		try {

			List<Object[]> datalst = this.datas;

			WritableSheet ws = wb.getSheet(sheet);// sheet；

			for (int i = 0; i < datalst.size(); i++) {

				Object[] datas = datalst.get(i);

				for (int j = 0; j < datas.length; j++) {

					String dataitem = "";

					if (datas[j] != null) {
						dataitem = (datas[j]).toString();
					}
					
					WritableCell lb = null;
					try {
						Double d = Double.parseDouble(dataitem);
						int scale = 0;
						if (dataitem.contains(".")) {
							scale = dataitem.split("\\.")[1].length();
						}
						d = NumberUtil.round(d, scale);
						lb = new jxl.write.Number(j, i + startRow, d);
						setNumberScale(lb, scale);
					} catch (NumberFormatException e) {
						lb = new Label(j, i + startRow, dataitem);
					}
					
					if (this.border) {
						this.drawBorder(lb);
					}

					ws.addCell(lb);

				}

			}

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 
	 * @desc 画边框线
	 * @date 2011-8-10
	 * @author heb
	 * @param lb
	 * @throws WriteException
	 */
	private void drawBorder(WritableCell lb, Object... styles) throws WriteException {
		WritableCellFormat alignStyle = null;
		CellFormat cf = lb.getCellFormat();
		WritableFont font =null;
		if (styles.length == 2) {
			font = new WritableFont(WritableFont.createFont((String) styles[0]), (Integer) styles[1], WritableFont.BOLD);
		}
		if (cf == null || !(cf instanceof WritableCellFormat)) {
			if (font != null) {
				alignStyle = new WritableCellFormat(font);
			} else {
				alignStyle = new WritableCellFormat();
			}
		} else {
			if (font != null) {
				alignStyle = new WritableCellFormat(font);
			} else {
				alignStyle = new WritableCellFormat();
			}
		}

		alignStyle.setBorder(Border.ALL, BorderLineStyle.THIN);
//		WritableFont font1 = new WritableFont(WritableFont.createFont("宋体"),12,WritableFont.BOLD);
//		WritableCellFormat format1=new WritableCellFormat(font1);
		boolean alignment_center = false;
		boolean verticalAlignment_center=false;
		boolean newline = false;
		if (styles.length == 3) {
			alignment_center = (Boolean) styles[0];
			verticalAlignment_center = (Boolean) styles[1];
			newline=(Boolean) styles[2];
		}
		
		if (alignment_center) {
			// 把水平对齐方式指定为居中
			alignStyle.setAlignment(jxl.format.Alignment.CENTRE);
		}
		if (verticalAlignment_center) {
			// 把垂直对齐方式指定为居中
			alignStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		}
		if (newline) {
			// 设置自动换行
			alignStyle.setWrap(true); // suf
		}
		lb.setCellFormat(alignStyle);
		lb.setCellFormat(alignStyle);

	}

	/**
	 * 
	 * @desc 使用List<Object[]>填充报表数据
	 * @date 2011-8-10
	 * @author heb
	 * @param datas
	 *            数据
	 * @param startRow
	 *            开始填充行
	 * @throws Exception
	 */
	public void putDataByLst(List<Object[]> datas, int startRow) throws Exception {

		this.datas = datas;
		this.startRow = startRow;
		this.putInData();
	}
	
	public void setNumberScale(WritableCell cell, int scale) {
		String suffix = "";
		if (scale != 0) {
			for (int i = 0; i < scale; i++) {
				suffix += "0";
			}
		}
		NumberFormat fivedps = new NumberFormat("#." + suffix);
		WritableCellFormat cellformat = (WritableCellFormat)cell.getCellFormat();
		if (cellformat == null) {
			cellformat = new WritableCellFormat(fivedps);
		}
		cell.setCellFormat(cellformat);
	}
}
