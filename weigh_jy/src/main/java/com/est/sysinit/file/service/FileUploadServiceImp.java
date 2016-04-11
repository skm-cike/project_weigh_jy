package com.est.sysinit.file.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.est.common.ext.util.fileutil.DirectoryCreateException;
import com.est.common.ext.util.fileutil.FileUtil;

/**
 * 
 *@desc 
 *@author jingpj
 *@date Jun 26, 2009
 *@path com.est.sysinit.file.service.FileUploadServiceImp
 *@corporation Enstrong S&T
 */
public class FileUploadServiceImp implements IFileUploadService {
	
	private final Log log = LogFactory.getLog(FileUploadServiceImp.class);
	
	/**
	 *@desc 
	 *@date Jun 26, 2009
	 *@author jingpj
	 *@param file ：struts2 上传文件封装 file
	 *@param path ：文件上传路径
	 *@param fileName ： 保存文件名
	 *@return 保存文件的路径（包含文件名）
	 */
	public String fileUpload(File file, String path, String fileName) {
		//目标文件
		File destFile = new File(path,fileName);
		try {
			
			//判断是否文件夹已存在，不存在就新建
			FileUtil.chkDirExist(path);
			//保存文件
			FileUtil.file2file(file, destFile);
			
		} catch (FileNotFoundException e) {
			log.info("文件没有找到！！");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			log.info("文件传送或者保存发生IO异常！！");
			e.printStackTrace();
			return null;
		} catch (DirectoryCreateException e) {
			log.info("文件夹创建失败！！");
			e.printStackTrace();
			return null;
		}
		
		//返回保存文件路径
		return destFile.getAbsolutePath()+"/"+destFile.getName();
	}

}
