package com.est.common.ext.util.fileutil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 *@desc 文件(文件夹)帮助类
 *@author jingpj
 *@date Jun 30, 2009
 *@path com.est.common.ext.util.fileutil.FileUtil
 *@corporation Enstrong S&T
 */
public class FileUtil {
	private static final Log log = LogFactory.getLog(FileUtil.class);
	
	/**
	 * 缓冲区大小，默认为8k字节
	 */
	private static final int BUFFER_SIZE = 1024*8;
	/**
	 * 
	 *@desc 判断文件夹是否存在，不存在则新建
	 *@date Jun 30, 2009
	 *@author jingpj
	 *@param dirPath
	 *@exception DirectoryCreateException
	 */
	public static void chkDirExist(String dirPath) throws DirectoryCreateException{
		//文件夹路径
		File dirFile = new File(dirPath);
        //判断是否文件夹已存在，不存在就新建
		if ( ! (dirFile.exists())  &&   ! (dirFile.isDirectory())) {
            boolean  isCreated  =  dirFile.mkdirs();
            if (isCreated) {
               log.info("make dir success!");
            } else {
               log.error("make dir error!!!");  
               throw new DirectoryCreateException();
            }
       } 
	}
	/**
	 * 
	 *@desc 判断文件夹是否存在，不存在则新建
	 *@date Jun 30, 2009
	 *@author jingpj
	 *@param dirPath
	 *@exception DirectoryCreateException
	 */
	public static void chkDirExist(File dirFile) throws DirectoryCreateException{
        //判断是否文件夹已存在，不存在就新建
		if ( ! (dirFile.exists())  &&   ! (dirFile.isDirectory())) {
            boolean  isCreated  =  dirFile.mkdirs();
            if (isCreated) {
               log.info("make dir success!");
            } else {
               log.error("make dir error!!!");  
               throw new DirectoryCreateException();
            }
       } 
	}	
	
	/**
	 * 
	 *@desc 将一个文件用字节形式拷贝到另一个文件
	 *@date Jul 1, 2009
	 *@author jingpj
	 *@param srcFile
	 *@param destFile
	 *@throws IOException
	 */
	public static void file2file(String srcFile,String destFile) throws IOException{
		InputStream srcStream = new BufferedInputStream(new FileInputStream(srcFile));
        OutputStream destStream = new BufferedOutputStream(new FileOutputStream(destFile));
        stream2Stream(srcStream,destStream);
	}
	
	
	
	/**
	 * 
	 *@desc 将一个文件用字节形式拷贝到另一个文件
	 *@date Jul 1, 2009
	 *@author jingpj
	 *@param srcFile
	 *@param destFile
	 *@throws IOException
	 */
	public static void file2file(File srcFile,File destFile) throws IOException{
		InputStream srcStream = new BufferedInputStream(new FileInputStream(srcFile));
        OutputStream destStream = new BufferedOutputStream(new FileOutputStream(destFile));
        stream2Stream(srcStream,destStream);
	}
	
	/**
	 * 
	 *@desc 将文件拷贝到输出流
	 *@date Jul 1, 2009
	 *@author jingpj
	 *@param srcFile
	 *@param destFile
	 *@throws IOException
	 */
	public static void file2Stream(File srcFile,OutputStream destStream) throws IOException{
		InputStream srcStream = new BufferedInputStream(new FileInputStream(srcFile));
        stream2Stream(srcStream,destStream);
	}

	/**
	 * 
	 *@desc 将文件拷贝到输出流
	 *@date Jul 1, 2009
	 *@author jingpj
	 *@param srcFile
	 *@param destFile
	 *@throws IOException
	 */
	public static void file2Stream(String srcFile,OutputStream destStream) throws IOException{
		InputStream srcStream = new BufferedInputStream(new FileInputStream(srcFile));
        stream2Stream(srcStream,destStream);
	}
	
	
	/**
	 * 
	 *@desc  将输入流拷贝到文件
	 *@date Jul 1, 2009
	 *@author Administrator
	 *@param srcStream
	 *@param destFile
	 *@throws IOException
	 */
	public static void stream2File(InputStream srcStream,String destFile) throws IOException{
        OutputStream destStream = new BufferedOutputStream(new FileOutputStream(destFile));
        stream2Stream(srcStream,destStream);
	}
	/**
	 * 
	 *@desc  将输入流拷贝到文件
	 *@date Jul 1, 2009
	 *@author Administrator
	 *@param srcStream
	 *@param destFile
	 *@throws IOException
	 */
	public static void stream2File(InputStream srcStream,File destFile) throws IOException{
        OutputStream destStream = new BufferedOutputStream(new FileOutputStream(destFile));
        stream2Stream(srcStream,destStream);
	}
		
	
	/**
	 * 
	 *@desc 将输入流数据输出到输出流中
	 *@date Jul 1, 2009
	 *@author jingpj
	 *@param srcStream 输入流
	 *@param destStream 输出流
	 *@throws IOException
	 */
	public static void stream2Stream(InputStream srcStream,OutputStream destStream) throws IOException{
		byte[] buffer = new byte[BUFFER_SIZE];
		int length = 0;
		while((length = srcStream.read(buffer)) > 0){
			destStream.write(buffer,0,length);
		}
		srcStream.close();
		destStream.close();
	}
	
	/**
	 *@desc 根据文件名删除文件
	 *@date Jul 30, 2009
	 *@author Administrator
	 *@param fileName
	 */
	public static void deleteFile(String fileName){
		File file = new File(fileName);
		file.delete();
	}
	
	
	/**
	 *@desc 
	 *@date Jun 26, 2009
	 *@author jingpj
	 *@param file ：struts2 上传文件封装 file
	 *@param path ：文件上传路径
	 *@param fileName ： 保存文件名
	 *@return 保存文件的路径（包含文件名）
	 */
	public static String fileUpload(File file, String path, String fileName) {
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
	/**
	 * 
	 *@desc 删除文件目录下的子目录，及其文件
	 *@date Jul 30, 2009
	 *@author 何波
	 *@param dirPath 目录路径
	 */
	public static void deleteAllFiles(File file){
		if(file.isDirectory()) {
			for(File subFile : file.listFiles()){
				deleteAllFiles(subFile);
			}
		}
		file.delete();
	}

}
