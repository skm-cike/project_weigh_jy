package com.est.weigh.report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.est.common.ExcelToFile;
import com.est.weigh.report.vo.GradeForMonthReport;
import com.est.weigh.report.vo.MonthReportList;
import com.est.weigh.report.vo.UnitForMonthReport;

public class AllowMonthReportExcel extends ExcelToFile<MonthReportList> {
	private int totalCol = 0;
	private Map<String, Integer> gradGroupForUnti;
	private List<String> grades;
	public AllowMonthReportExcel() {
		super();
	}

	public AllowMonthReportExcel(HttpServletResponse response, String fileName,
			List datas, String title, String[] needFields, String[] tableHead) {
		super(response, fileName, datas, title, needFields, tableHead);
		MonthReportList report = this.datas.get(0);
		String pz = report.getPz();
		List<String> headField = new ArrayList();
		List<String> headList = new ArrayList();
		headList.add("单位名称");headField.add("companyname");
		
		//找到等级,为其分组
		Set<String> _grades = new HashSet();
		boolean hasTwoCol = false;
		for (MonthReportList r: this.datas) {
			List<GradeForMonthReport> gradesReports = r.getGradeForMonth();
			if (gradesReports != null && gradesReports.size() != 0) {
				hasTwoCol = true;
				for (GradeForMonthReport g: gradesReports) {
					_grades.add(g.getGrade());
				}
			}
		}
		totalCol=6;
		totalCol += _grades.size() * 2;
		grades = new ArrayList(_grades);
	}

	
	@Override
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
		Label label = new Label(0, rowIndex, title.split(",")[0], titleFormat);
		try {
			sheet.addCell(label);
			sheet.mergeCells(0, rowIndex, totalCol-1, rowIndex);
			rowIndex++;
			label = new Label(0, rowIndex, title.split(",")[1], titleFormat);
			sheet.addCell(label);
			sheet.mergeCells(0, rowIndex, totalCol-1, rowIndex);
			rowIndex++;
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}
	
//	@Override
//	protected void createBody(Object... dateformat) {
//		
//	}

	@Override
	protected void addBody(MonthReportList o, Object... dateformat) {
		int column = 0;
		
		try {
			WritableCellFormat numberFormat = new WritableCellFormat(new WritableFont(WritableFont.TIMES, normalSize));
			numberFormat.setAlignment(jxl.format.Alignment.RIGHT);
			numberFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			numberFormat.setWrap(true);
			
			WritableCellFormat strFormat = new WritableCellFormat(new WritableFont(WritableFont.TIMES, normalSize));
			strFormat.setAlignment(jxl.format.Alignment.LEFT);
			strFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			strFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			strFormat.setWrap(true);
			
			//单位名称
			Label label = new Label(column, rowIndex, o.getCompanyname(), strFormat);
			sheet.addCell(label);
			column++;
			//结转金额
			WritableCell cell = new jxl.write.Number(column, rowIndex, o.getJzje(), numberFormat);
			sheet.addCell(cell);
			column++;
			//预收货款
			cell = new jxl.write.Number(column, rowIndex, o.getYfk(), numberFormat);
			sheet.addCell(cell);
			column++;
			//自动列
			column = 3;
			for (int i = 0; i < grades.size(); i++) {
				GradeForMonthReport gr = null;
				for (GradeForMonthReport g: o.getGradeForMonth()) {
					if (grades.get(i).equals(g.getGrade())) {
						gr = g;
						break;
					}
				}
				
				
				if (gr != null) {
					cell = new jxl.write.Number(column,rowIndex, gr.getChl(), numberFormat);
					sheet.addCell(cell);
					column++;
					cell = new jxl.write.Number(column,rowIndex, gr.getXsje(), numberFormat);
					sheet.addCell(cell);
					column++;
				} else {
					cell = new jxl.write.Number(column,rowIndex, 0, numberFormat);
					sheet.addCell(cell);
					column++;
					cell = new jxl.write.Number(column,rowIndex, 0, numberFormat);
					sheet.addCell(cell);
					column++;
				}
			}
			
			cell = new jxl.write.Number(column, rowIndex, o.getChl(), numberFormat);
			sheet.addCell(cell);
			column++;
			
			cell = new jxl.write.Number(column, rowIndex, o.getXsjezj(), numberFormat);
			sheet.addCell(cell);
			column++;
			
			cell = new jxl.write.Number(column, rowIndex, o.getHkye(), numberFormat);
			sheet.addCell(cell);
			
			rowIndex++;
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void creatTableHead() {
		MonthReportList report = this.datas.get(0);
		String pz = report.getPz();
		List<String> headField = new ArrayList();
		List<String> headList = new ArrayList();
		headList.add("单位名称");headField.add("companyname");
		
		headList.add("结转金额");headField.add("jzje");
		headList.add("预收货款");headField.add("yfk");
		//每个等级下有成组出现的单价分组单元格 每个单价分组单元格包括     [出货量,单价,销售金额]
		//且每个等级下有  出货量小计, 销售金额小计
		for (int i = 0; i < grades.size(); i++) {
			headList.add(grades.get(i));headField.add(grades.get(i));           
		}
		headList.add("出货量（吨） 合计");headField.add("chl");
		headList.add("销售金额合计（元）");headField.add("xsjezj");
		headList.add("货款余额（元）");headField.add("hkye");
			
		//合成头
		Integer column = 0;
		try {
			WritableCellFormat format = new WritableCellFormat(new WritableFont(WritableFont.TIMES, normalSize, WritableFont.BOLD));
			format.setAlignment(jxl.format.Alignment.CENTRE);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBorder(Border.ALL, BorderLineStyle.THIN);
			format.setWrap(true);
//			for (int i=0; i < tableHead.length; i++) {
//				Label label = new Label(column,rowIndex, tableHead[i], format);
//				sheet.addCell(label);
//				column++;
//			}
			rowIndex = 3;
			//单位名称
			Label label = new Label(column,rowIndex, "单位名称", format);
			sheet.addCell(label);
			column++;
			//结转金额
			label = new Label(column,rowIndex, "结转金额(元)", format);
			sheet.addCell(label);
			column++;
			//预收货款
			label = new Label(column,rowIndex, "预收货款(元)", format);
			sheet.addCell(label);
			column++;
			//品种等级
			for (int i = 0; i < grades.size(); i++) {
				label = new Label(column,rowIndex, grades.get(i), format);
				sheet.addCell(label);
				column+=2;
			}
			
			//出货量合计
			label = new Label(column,rowIndex, "出货量合计(吨)", format);
			sheet.addCell(label);
			column++;
			//销售金额合计
			label = new Label(column,rowIndex, "销售金额合计(元)", format);
			sheet.addCell(label);
			column++;
			//货款余额合计
			label = new Label(column,rowIndex, "货款余额(元)", format);
			sheet.addCell(label);
			column++;
			rowIndex++;
			
			//第二行表头
			column = 3;
			for (int i = 0; i < grades.size(); i++) {
				label = new Label(column,rowIndex, "出货量小计(吨)", format);
				sheet.addCell(label);
				column++;
				label = new Label(column,rowIndex, "销售金额小计(吨)", format);
				sheet.addCell(label);
				column++;
			}
			
			rowIndex++;
			
			sheet.mergeCells(0, rowIndex-2, 0, rowIndex-1);
			sheet.mergeCells(1, rowIndex-2, 1, rowIndex-1);
			sheet.mergeCells(2, rowIndex-2, 2, rowIndex-1);
			column = 3;
			for (int i = 0; i < grades.size(); i++) {
				sheet.mergeCells(column, rowIndex-2, column+1, rowIndex-2);
				column += 2;
			}
			sheet.mergeCells(column, rowIndex-2, column, rowIndex-1);
			column++;
			sheet.mergeCells(column, rowIndex-2, column, rowIndex-1);
			column++;
			sheet.mergeCells(column, rowIndex-2, column, rowIndex-1);
			
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void creatHead() {
		Integer column = 0;
		try {
			WritableCellFormat format = new WritableCellFormat(new WritableFont(WritableFont.TIMES, normalSize));
			format.setAlignment(jxl.format.Alignment.LEFT);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBorder(Border.ALL, BorderLineStyle.NONE);
			format.setWrap(true);
			
			Label label = new Label(0,rowIndex, tableHead[0], format);
			sheet.addCell(label);
			
			format = new WritableCellFormat(new WritableFont(WritableFont.TIMES, normalSize));
			format.setAlignment(jxl.format.Alignment.RIGHT);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBorder(Border.ALL, BorderLineStyle.NONE);
			format.setWrap(true);
		
			label = new Label(totalCol-3,rowIndex, tableHead[1], format);
			sheet.addCell(label);
		
			sheet.mergeCells(totalCol-3, rowIndex, totalCol-1, rowIndex);
			sheet.mergeCells(0, rowIndex, 2, rowIndex);
			rowIndex++;
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void creatRoot() {
		int startRow = 5;
		int column = 0;
		try {
			//合计
			for (int j = 0; j < totalCol; j++) {
				List<jxl.write.Number> cells = new ArrayList();
				for (int i = startRow; i < rowIndex; i++) {
					if (j ==0) {
						if (column == 0) {
							WritableCellFormat format = new WritableCellFormat(new WritableFont(WritableFont.TIMES, normalSize));
							format.setAlignment(jxl.format.Alignment.LEFT);
							format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
							format.setBorder(Border.ALL, BorderLineStyle.THIN);
							format.setBackground(Colour.YELLOW);
							format.setWrap(true);
							Label label = new Label(column, rowIndex, "合计", format);
							sheet.addCell(label);
							column++;
						}
					} else {
						cells.add((jxl.write.Number)sheet.getCell(j, i));
					}
					
					if (j !=0) {
						WritableCellFormat numberFormat = new WritableCellFormat(new WritableFont(WritableFont.TIMES, normalSize));
						numberFormat.setAlignment(jxl.format.Alignment.RIGHT);
						numberFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
						numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
						numberFormat.setWrap(true);
						numberFormat.setBackground(Colour.YELLOW);
						
						double val = 0d;
						for (jxl.write.Number c: cells) {
							val += c.getValue();
						}
						
						WritableCell cell = new jxl.write.Number(j, rowIndex, val, numberFormat);
						sheet.addCell(cell);
						column++;
					}
				}
			}
			rowIndex++;
			
			//脚
			WritableCellFormat format = new WritableCellFormat(new WritableFont(WritableFont.TIMES, normalSize));
			format.setAlignment(jxl.format.Alignment.LEFT);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBorder(Border.ALL, BorderLineStyle.NONE);
			format.setWrap(true);
			Label label = new Label(0,rowIndex, "统计时间:" + this.datas.get(0).getStartdate() + "——" + this.datas.get(0).getEnddate(), format);
			sheet.addCell(label);
			sheet.mergeCells(0, rowIndex, totalCol - 1, rowIndex);
			rowIndex++;
			
			format = new WritableCellFormat(new WritableFont(WritableFont.TIMES, normalSize));
			format.setAlignment(jxl.format.Alignment.LEFT);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBorder(Border.ALL, BorderLineStyle.NONE);
			format.setWrap(true);
			String str = "批准：                                              审核：                                                        统计: " + this.datas.get(0).getUsername();
			label = new Label(0,rowIndex, str, format);
			sheet.addCell(label);
			sheet.mergeCells(0, rowIndex, totalCol - 1, rowIndex);
			rowIndex++;
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void formatTable() {
		sheet.setColumnView(0, 15);
		for (int i = 1; i < totalCol; i++) {
			sheet.setColumnView(i, 10);
		}
	}
}
