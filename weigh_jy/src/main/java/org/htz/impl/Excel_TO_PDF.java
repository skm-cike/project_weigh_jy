package org.htz.impl;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.htz.Excel2Pdf;
import org.htz.core.Convert;
import org.htz.core.HeaderText;
import org.htz.pagesetting.PageSetting;
import org.htz.pdfevent.PageEvent;
import org.htz.pdfpag.PdfPageSize;



public class Excel_TO_PDF implements Excel2Pdf{

	public void conVertFormLocal(String strExcelFilePath, OutputStream outPut, PageSetting pageSetting) 
				throws Exception 
	{
		Convert con=new Convert(strExcelFilePath,outPut);
		if(pageSetting!=null)
		{
			con.setPageSize(pageSetting.getPageSize());
			HeaderText text=con.getHeader();
			PageEvent event=new PageEvent();
			setUp(event,pageSetting,text);
			int iRows = con.getIRows();
			int iTotalRows = con.getITotalrows();
			if(iTotalRows > 3000)
			{
				for(int i = 0; i < (iTotalRows / 3000) + 1; i++)
				{
					con.convert(event);
				}
			}else
			{
				con.convert(event);
			}
		}else{
			throw new Exception("页面参数未设置");
		}
		
	}

	public void convertFromDB(String strExcelFileName, OutputStream outPut, PageSetting pageSetting) 
				throws Exception 
	{
//		FileIntoDB db=new FileIntoDB();
//		db.fileIntoDB("D:\\CRM1.xls","crm11");
//		InputStream input=db.getInputStream(excelFileName);
//		Convert con=new Convert(input,output);
//		if(pageSetting!=null){
//		con.setPageSize(pageSetting.getPageSize());
//		HeaderText text=con.getHeader();
//		PageEvent event=new PageEvent();
//		event.setHeaderText(text);
//		event.setFooter(pageSetting.getFooter());
//		event.setHeader(pageSetting.getHeader());
//		event.setPageNumberSize(pageSetting.getFooter().getFontSize());
//		event.setPageNumberStyle(pageSetting.getFooter().getPageNumberStyle());
//		con.transform(event);
//		}else{
//			throw new Exception("页面参数未设置");
//		}
		
	}

	public void convertFromLocal(String strExcelFilePath, String strPdfFilePath, PageSetting pageSetting) 
				throws Exception 
	{
		Convert con=new Convert(strExcelFilePath,strPdfFilePath);
		if(pageSetting!=null){
		con.setPageSize(pageSetting.getPageSize());
		HeaderText text=con.getHeader();
		PageEvent event=new PageEvent();
		setUp(event,pageSetting,text);
		con.convert(event);
		}else{
			throw new Exception("页面参数未设置");
		}
		
	}
	
	/**
	 * 设置PdfEvent事件
	 * @param event 
	 * @param pageSetting
	 * @param text
	 */
	private void setUp(PageEvent event,PageSetting pageSetting,HeaderText text){
		event.setHeaderText(text);
		event.setFooter(pageSetting.getFooter());
		event.setHeader(pageSetting.getHeader());
		event.setPageNumberSize(pageSetting.getFooter().getFontSize());
		event.setPageNumberStyle(pageSetting.getFooter().getPageNumberStyle());
	}
	
	public static void main(String[] args) {
		PageSetting set=new PageSetting();
		set.setPageSize(PdfPageSize.A4.rotate());
		Excel2Pdf pdf = new Excel_TO_PDF();
		try {
			pdf.conVertFormLocal("D:/sdk.xls", new FileOutputStream("D:/sdk.pdf"), set);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void convertFromInputStream(InputStream in, OutputStream outPut,
			PageSetting pageSetting) throws Exception {
		Convert con=new Convert(in,outPut);
		if(pageSetting!=null)
		{
			con.setPageSize(pageSetting.getPageSize());
			HeaderText text=con.getHeader();
			PageEvent event=new PageEvent();
			setUp(event,pageSetting,text);
			int iRows = con.getIRows();
			int iTotalRows = con.getITotalrows();
			if(iTotalRows > 3000)
			{
				for(int i = 0; i < (iTotalRows / 3000) + 1; i++)
				{
					con.convert(event);
				}
			}else
			{
				con.convert(event);
			}
		}else{
			throw new Exception("页面参数未设置");
		}
		
	}

}
