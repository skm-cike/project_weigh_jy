package com.est.common.ext.util.excelexport.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
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
 *@desc excel报表模版工具类
 *@author heb
 *@date 2011-8-11
 *@path com.xls.ExcelRPTUtils
 *@corporation Enstrong S&T
 */
public class UtilExcel_Inven{
	

	private List<Object[]> datas;
	private int startRow;
	private String xlsTplPath;
	private String rptPath;
	private int sheet;
	private WritableWorkbook wb ;
	private boolean border=true;
	/**
	 * 
	 *@desc 设置有无边框
	 *@date 2011-8-10
	 *@author heb
	 *@param border
	 */
	public void setBorder(boolean border) {
		this.border=border;
	}


	/**
	 * Constructors
	 * @param xlsTplPath 模版文件路径 
	 * @param rptPath 报表输出路径
	 */
	public UtilExcel_Inven(String xlsTplPath,String rptPath){
		this.xlsTplPath = xlsTplPath;
		this.rptPath = rptPath;
		
		if(this.xlsTplPath==null){
			throw new RuntimeException("报表模版文件路径为空");
		}
		if(this.rptPath==null){
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
	 *@desc 设置sheet
	 *@date 2011-8-10
	 *@author heb
	 *@param sheet
	 */
	public void setSheet(int sheet) {
		this.sheet = sheet;
	}

	/**
	 * 
	 *@desc 设置某行的填充数据
	 *@date 2011-8-10
	 *@author heb
	 *@param r 行号
	 * @throws Exception 
	 */
	public  void setRowData(int r,Object[] data) throws Exception{
			
			WritableSheet ws = wb.getSheet(sheet);
			for(int i=0; i< data.length;i++){
				
				String v = "";
				
				if(data[i]!=null){
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
				
				if(this.border){
					this.drawBorder(lb);
				}
				
				ws.addCell(lb);
				
			}
	}
	
	/**
	 * 
	 *@desc 设置列填充值
	 *@date 2011-8-10
	 *@author heb
	 *@param r 行号
	 *@param c 列号
	 *@param value 值
	 *@throws RowsExceededException
	 *@throws WriteException
	 *@throws IOException
	 */
	public  WritableCell setColData(int r,int c,Object value) throws RowsExceededException, WriteException, IOException{
		
		
		WritableSheet ws = wb.getSheet(sheet);
		String v = "";
		if(value != null){
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
		if(this.border){
			this.drawBorder(lb);
		}
		this.setFondAndSize(lb);
		ws.addCell(lb);
		return lb;
	}
	
	/**
	 * 
	 *@desc 设置列填充值
	 *@date 2011-8-10
	 *@author heb
	 *@param r 行号
	 *@param c 列号
	 *@param value 值
	 *@throws RowsExceededException
	 *@throws WriteException
	 *@throws IOException
	 */
	public  WritableCell setColData(int r,int c,Object value, WritableCellFormat format) throws RowsExceededException, WriteException, IOException{
		
		
		WritableSheet ws = wb.getSheet(sheet);
		String v = "";
		if(value != null){
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
			lb = new jxl.write.Number(c, r, d,format);
			setNumberScale(lb, scale);
		} catch (NumberFormatException e) {
			lb = new Label(c, r, v, format);
		}
		ws.addCell(lb);
		return lb;
	}
	
	/**
	 * 
	 *@desc 设置列填充值
	 *@date 2011-8-10
	 *@author heb
	 *@param r 行号
	 *@param c 列号 
	 *@param value 值
	 *@throws RowsExceededException
	 *@throws WriteException
	 *@throws IOException
	 *与上面的不同之处在于整个边框都画
	 */
public  WritableCell setColData_Lc(int r,int c,Object value) throws RowsExceededException, WriteException, IOException{
		
		
		WritableSheet ws = wb.getSheet(sheet);
		String v = "";
		if(value != null){
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
		if(this.border){
			this.drawBorder(lb);
		}
		this.setFondAndSize_Lc(lb);
		ws.addCell(lb);
		return lb;
	}
/**
 * 
 *@desc 设置某行的填充数据,没有边框,水平对齐方式：靠左，垂直对齐方式：靠下
 *@date 2011-8-10
 *@author lc 
 *@param r 行号
 * @throws Exception 
 */
public  WritableCell setColData_button(int r,int c,Object value) throws RowsExceededException, WriteException, IOException{
	
	
	WritableSheet ws = wb.getSheet(sheet);
	String v = "";
	if(value != null){
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
	if(this.border){
		this.drawBorder(lb);
	}
	this.setFondAndSize_button(lb);
	ws.addCell(lb);
	return lb;
}
	
/**
 * 
 *@desc 设置某行的填充数据,没有边框,水平对齐方式：靠左，垂直对齐方式：靠下
 *@date 2011-8-10
 *@author lc 
 *@param r 行号
 * @throws Exception 
 */
public  WritableCell setColData_button_center(int r,int c,Object value) throws RowsExceededException, WriteException, IOException{
	
	
	WritableSheet ws = wb.getSheet(sheet);
	String v = "";
	if(value != null){
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
	if(this.border){
		this.drawBorder(lb);
	}
	this.setFondAndSize_button_center(lb);
	ws.addCell(lb);
	return lb;
}
	
	/**
	 * 
	 *@desc 设置列填充值
	 *@date 2011-8-10
	 *@author heb
	 *@param r 行号
	 *@param c 列号
	 *@param value 值
	 *@throws RowsExceededException
	 *@throws WriteException
	 *@throws IOException
	 */
	public  WritableCell setColData_Left(int r,int c,Object value) throws RowsExceededException, WriteException, IOException{
		
		
		WritableSheet ws = wb.getSheet(sheet);
		String v = "";
		if(value != null){
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
		if(this.border){
			this.drawBorder(lb);
		}
		this.setFondAndSize_Left(lb);
		ws.addCell(lb);
			
		return lb;
	}
	
	/**
	 * @描述: 设置单元格格式
	 * @param r
	 * @param c
	 * @param value
	 * @param borders  是否有边框，数组中依次有4个值，顺序为上下左右，为null时不设置
	 * @param colours  边框颜色，数组中依次有4个值，顺序为上下左右,为null时不设置
	 * @return
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws IOException
	 */
	public  WritableCell setColData(int r,int c,Object value, boolean[] borders) throws RowsExceededException, WriteException, IOException{
		WritableSheet ws = wb.getSheet(sheet);
		String v = "";
		if(value != null){
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
		this.drawBorder(lb, borders);
		this.setFondAndSizen(lb);
		ws.addCell(lb);
			
		return lb;
	}
	
	private void setFondAndSizen(WritableCell lb) throws WriteException {
		//WritableFont font1 = new WritableFont(WritableFont.TIMES,12/*,WritableFont.BOLD*/, false);
		WritableFont font1 = new WritableFont(WritableFont.TIMES,10); 
		CellFormat format = lb.getCellFormat();
		if (format == null || !format.getClass().equals(WritableCellFormat.class)) {
			WritableCellFormat format0=new WritableCellFormat(font1); 
			lb.setCellFormat(format0);
		}
		WritableCellFormat format1 = (WritableCellFormat)lb.getCellFormat();
		//把水平对齐方式指定为靠左
		 format1.setAlignment(Alignment.LEFT);         
		//把垂直对齐方式指定为居中
		 format1.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);
		 //设置自动换行
		  format1.setWrap(true); 
	}


	/**
	 * 
	 *@desc 合并单元格 
	 *@date 2011-8-10
	 *@author heb
	 *@param col1
	 *@param row1
	 *@param col2
	 *@param row2
	 * @throws Exception 
	 */
	public void mergeCells(int col1, int row1, int col2, int row2) throws Exception{
		
			
			WritableSheet ws = wb.getSheet(sheet);
			
			ws.mergeCells(col1, row1, col2, row2);
			
		
	}
	/**
	 * 
	 *@desc 刷新数据
	 *@date 2011-8-10
	 *@author heb
	 *@param wb
	 *@throws IOException
	 */
	public FileInputStream flushData( ) throws IOException{
		
		wb.write();
		wb.close();
		
		FileInputStream fis = new FileInputStream(this.rptPath);
		
		return fis;
		
	}
	
	
	/**
	 * 
	 *@desc 填充数据
	 *@date 2011-8-10
	 *@author heb
	 *@param writewb
	 *@throws Exception
	 */
	private void putInData() throws Exception {

		try {
			
			List<Object[]> datalst = this.datas;
			
			WritableSheet ws = wb.getSheet(sheet);// sheet；
			
			for (int i = 0; i < datalst.size(); i++) {

				Object[] datas = datalst.get(i);

				for (int j = 0; j < datas.length; j++) {
					
					String dataitem="";
					
					if(datas[j]!=null){
						dataitem =  (datas[j]).toString();
					}
					
					WritableCell lb = null;
					
					try {
						Double d = Double.parseDouble(dataitem);
						int scale = 0;
						if (dataitem.contains(".")) {
							scale = dataitem.split("\\.")[1].length();
						}
						d = NumberUtil.round(d, scale);
						lb = new jxl.write.Number(j, i+startRow, d);
						setNumberScale(lb, scale);
					} catch (NumberFormatException e) {
						lb = new Label(j, i+startRow, dataitem);
					}
					
					if(this.border){
						this.drawBorder(lb);
					}
					setFondAndSize_Left(lb);
					ws.addCell(lb);

				}

			}

			
		} catch (Exception e) {
			throw e;
		}

	}
	
	/**
	 * 
	 *@desc 画边框线
	 *@date 2011-8-10
	 *@author heb
	 *@param lb
	 *@throws WriteException
	 */
	private void drawBorder(WritableCell lb) throws WriteException{
		WritableCellFormat alignStyle = null;
		CellFormat cf = lb.getCellFormat();
		if (cf == null || !(cf instanceof WritableCellFormat)) {
			alignStyle = new WritableCellFormat();
		} else {
			alignStyle = new WritableCellFormat();
		}
		alignStyle.setBorder(Border.ALL, BorderLineStyle.THIN);
		lb.setCellFormat(alignStyle);
		
	}
	
	/**
	 * @描述: 画边框
	 * @param lb
	 * @param borders  是否有边框，数组中依次有4个值，顺序为上下左右，为null时不设置
	 * @throws WriteException
	 */
	private void drawBorder(WritableCell lb, boolean[] borders) throws WriteException{
		if (borders == null) {
			return;
		}
		WritableCellFormat alignStyle = null;
		CellFormat cf = lb.getCellFormat();
		if (cf == null || !(cf instanceof WritableCellFormat)) {
			alignStyle = new WritableCellFormat();
		} else {
			alignStyle = new WritableCellFormat();
		}
		
		alignStyle.setBorder(Border.ALL, jxl.format.BorderLineStyle.NONE);
		
		boolean tborder = borders[0];
		boolean bborder = borders[1];
		boolean lborder = borders[2];
		boolean rborder = borders[3];
		
		if (tborder) {
			alignStyle.setBorder(Border.TOP, jxl.format.BorderLineStyle.THIN);
		}
		
		if (bborder) {
			alignStyle.setBorder(Border.BOTTOM, jxl.format.BorderLineStyle.THIN);
		}
		
		if (lborder) {
			alignStyle.setBorder(Border.LEFT, jxl.format.BorderLineStyle.THIN);
		}
		
		if (rborder) {
			alignStyle.setBorder(Border.RIGHT, jxl.format.BorderLineStyle.THIN);
		}
		
		lb.setCellFormat(alignStyle);
	}
	
	
	/**
	 * 
	 *@desc 设置字体
	 *@date 2011-8-10
	 *@author heb
	 *@param lb
	 *@throws WriteException
	 */
	private void setFondAndSize(WritableCell lb) throws WriteException{
		WritableFont font1 = new WritableFont(WritableFont.TIMES,10); 
		WritableCellFormat format1=new WritableCellFormat(font1); 
		//把水平对齐方式指定为居中
		 format1.setAlignment(jxl.format.Alignment.CENTRE); 
		//把垂直对齐方式指定为居中
		 format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		 //设置自动换行
		  format1.setWrap(true); 
		  lb.setCellFormat(format1);
		
	}
	/**
	 * 
	 *@desc 设置字体
	 *@date 2011-8-10
	 *@author lc
	 *@param lb
	 *@throws WriteException
	 */
	private void setFondAndSize_Left(WritableCell lb) throws WriteException{
		//WritableFont font1 = new WritableFont(WritableFont.TIMES,12/*,WritableFont.BOLD*/, false);
		WritableFont font1 = new WritableFont(WritableFont.TIMES,10); 

		WritableCellFormat format1=new WritableCellFormat(font1); 
		//把水平对齐方式指定为靠左
		 format1.setAlignment(Alignment.LEFT);         
		//把垂直对齐方式指定为居中
		 format1.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);
		 format1.setBorder(Border.ALL, BorderLineStyle.THIN);
			lb.setCellFormat(format1);
		 //设置自动换行
		  format1.setWrap(true); 
		  lb.setCellFormat(format1);
		
	}
	/**
	 * 
	 *@desc 设置字体
	 *@date 2011-8-10
	 *@author lc
	 *@param lb
	 *@throws WriteException
	 */
	private void setFondAndSize_button(WritableCell lb) throws WriteException{
		//WritableFont font1 = new WritableFont(WritableFont.TIMES,12/*,WritableFont.BOLD*/, false);
		WritableFont font1 = new WritableFont(WritableFont.TIMES,10); 

		WritableCellFormat format1=new WritableCellFormat(font1); 
		//把水平对齐方式指定为靠左
		 format1.setAlignment(Alignment.LEFT);         
		//把垂直对齐方式指定为居中
		 format1.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);
		// format1.setBorder(Border.ALL, BorderLineStyle.THIN);
			lb.setCellFormat(format1);
		 //设置自动换行
		  format1.setWrap(true); 
		  lb.setCellFormat(format1);
		
	}
	/**
	 * 
	 *@desc 设置字体
	 *@date 2011-8-10
	 *@author lc
	 *@param lb
	 *@throws WriteException
	 */
	private void setFondAndSize_button_center(WritableCell lb) throws WriteException{
		//WritableFont font1 = new WritableFont(WritableFont.TIMES,12/*,WritableFont.BOLD*/, false);
		WritableFont font1 = new WritableFont(WritableFont.TIMES,10); 
		WritableCellFormat format1=new WritableCellFormat(font1); 
		//把水平对齐方式指定为靠左
		 format1.setAlignment(Alignment.CENTRE);         
		//把垂直对齐方式指定为居中
		 format1.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);
		// format1.setBorder(Border.ALL, BorderLineStyle.THIN);
			lb.setCellFormat(format1);
		 //设置自动换行
		  format1.setWrap(true); 
		  lb.setCellFormat(format1);
		
	}
	
	/**
	 * 
	 *@desc 设置字体
	 *@date 2011-8-10
	 *@author lc
	 *@param lb
	 *@throws WriteException
	 */
	private void setFondAndSize_Lc(WritableCell lb) throws WriteException{
		WritableFont font1 = new WritableFont(WritableFont.TIMES,10); 
		WritableCellFormat format1=new WritableCellFormat(font1); 
		//把水平对齐方式指定为居中
		 format1.setAlignment(Alignment.CENTRE);         
		//把垂直对齐方式指定为居中
		 format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		 format1.setBorder(Border.ALL, BorderLineStyle.THIN);
		 lb.setCellFormat(format1);
		 //设置自动换行
		  format1.setWrap(true); 
		  lb.setCellFormat(format1);
	}
	
	/**
	 * 
	 *@desc 使用List<Object[]>填充报表数据
	 *@date 2011-8-10
	 *@author heb
	 *@param datas 数据
	 *@param startRow 开始填充行
	 *@throws Exception
	 */
	public void putDataByLst(List<Object[]> datas,int startRow) throws Exception{
		
		this.datas = datas;
		this.startRow = startRow;
		this.putInData();
		
	}
	
	/**
	 * 
	 *@desc 设置打印格式 
	 *@date 2011-8-10
	 *@author lc
	 *@param col1
	 *@param row1
	 *@param col2
	 *@param row2
	 * @throws Exception 
	 */
	public void setPrintFormat() throws Exception{
			WritableSheet ws = wb.getSheet(sheet);
			SheetSettings ss = ws.getSettings();
			//设置纸张的高,设置纸张的宽
			/*ss.setFitHeight(280);
			ss.setFitWidth(241);
			//ss.setPaperSize(PaperSize.getPaperSize(500));
			ss.setPaperSize(PaperSize.getPaperSize(sheet));				
			//设置缩放大小
			ss.setScaleFactor(85);
			//设置页边距
			ss.setBottomMargin(1);
			ss.setTopMargin(1);
			ss.setLeftMargin(0.3);	
			ss.setRightMargin(0.3);
			*/
			//设置打印质量
			ss.setVerticalPrintResolution(180);
			ss.setPageStart(1);
			//ss.setHorizontalFreeze(1);//列冻结
			//ss.setVerticalFreeze(3);//行冻结
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
