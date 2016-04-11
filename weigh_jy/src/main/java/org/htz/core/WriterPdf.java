package org.htz.core;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.htz.pdfpag.PdfPageSize;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 一个pdf的抽象模板类
 * @author newapps
 * 2009-11-8
 */
public abstract class WriterPdf {
	/**document属性*/
	protected Document document=null;
	/**设置pdf页面大小、默认为A4*/
	public Rectangle pageSize=PdfPageSize.A4;
	/**pdf输出流*/
	protected PdfWriter writer=null;
	/**获得pdf页面大小*/
	public Rectangle getPageSize() {
		return pageSize;
	}
	/**设置pdf页面大小*/
	public void setPageSize(Rectangle pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 写到本地的pdf文件
	 * @param destFilePath pdf文件路径
	 */
	public WriterPdf(String destFilePath){
		document=new Document(null,20,20,60,30);
		System.out.println("-->创建Document对象成功!");
		try {
			FileOutputStream fos=new FileOutputStream(destFilePath);
			writer=PdfWriter.getInstance(document,fos);
		} catch (Exception e) {
			System.err.println("-->创建Document对象失败!");
			e.printStackTrace();
		}
	}
	/**
	 * 此方法一般是将是写到网络中
	 * @param output 输出流
	 */
	public WriterPdf(OutputStream output){
		try {
			document=new Document(null,20,20,60,30);
			System.out.println("-->创建Document对象成功!");
			writer=PdfWriter.getInstance(document,output);
		} catch (DocumentException e) {
			System.err.println("-->创建Document对象失败!");
			e.printStackTrace();
		}
	}
	/**
	 * 关闭document对象
	 * @throws Exception
	 */
	public void CloseDocument() throws Exception{
		try {
			if(document!=null){
			   document.close();
			System.out.println("-->关闭Document对象成功!");
			}
		} catch (Exception e) {
			System.err.println("-->关闭Document对象失败!");
			e.printStackTrace();
		}
	}
}
