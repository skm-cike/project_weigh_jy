package org.htz;

import java.io.InputStream;
import java.io.OutputStream;

import org.htz.pagesetting.PageSetting;


public interface Excel2Pdf {
	
	/**
	 * 把本地的excel文件转化为本地的PDF文件 
	 * @param strExcelFilePath excel文件所在本地路径
	 * @param strPdfFilePath 生成PDF文件的路径
	 * @param pageSetting 设置页面参数信息的类 
	 * @throws Exception
	 */
	public void convertFromLocal(String strExcelFilePath,String strPdfFilePath,PageSetting pageSetting) throws Exception;
	
	
	/**
	 * 把数据库中存取excel文件转化为PDF文件输出流 
	 * @param strExcelFileName excel文件名称
	 * @param outPut PDF文件输出流 
	 * @param pageSetting 设置页面参数信息的类
	 * @throws Exception
	 */
	public void convertFromDB(String strExcelFileName,OutputStream outPut,PageSetting pageSetting) throws Exception;
	
	/**
	 * 把本地的excel文件转化为PDF文件输出流 
	 * @param strExcelFilePath  excel文件所在本地路径
	 * @param outPut PDF文件输出流
	 * @param pageSetting  设置页面参数信息的类
	 * @throws Exception
	 */
	public void conVertFormLocal(String strExcelFilePath,OutputStream outPut,PageSetting pageSetting) throws Exception;
	
	/**
	 * @描述: 把输入流转换为PDF文件输出流
	 * @param in
	 * @param outPut
	 * @param pageSetting
	 * @throws Exception
	 */
	public void convertFromInputStream(InputStream in,OutputStream outPut,PageSetting pageSetting) throws Exception;
}
