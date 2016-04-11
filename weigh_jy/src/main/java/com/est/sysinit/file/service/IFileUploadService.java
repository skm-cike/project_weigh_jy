package com.est.sysinit.file.service;

import java.io.File;

/**
 * 
 *@desc 文件上传接口
 *@author jingpj
 *@date Jun 26, 2009
 *@path com.est.sysinit.file.service.IFileUploadService
 *@corporation Enstrong S&T
 */
public interface IFileUploadService {
	
	/**
	 * 
	 *@desc 
	 *@date Jun 26, 2009
	 *@author jingpj
	 *@param file ：struts2 上传文件封装 file
	 *@param path ：文件上传路径
	 *@param fileName ： 保存文件名
	 *@return 保存文件的路径（包含文件名）
	 */
	public String fileUpload(File file,String path,String fileName)  ;

}
