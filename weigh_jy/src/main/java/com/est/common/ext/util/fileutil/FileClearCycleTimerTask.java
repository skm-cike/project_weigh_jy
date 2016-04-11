package com.est.common.ext.util.fileutil;

import java.io.File;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.est.common.ext.util.InitSystem;

/**
 * 
 * @desc 文件定时清理类
 * @author 何波
 * @date Jul 30, 2009
 * @path com.est.common.ext.util.fileutil.FileClearTimerTask
 * @corporation Enstrong S&T
 */
public class FileClearCycleTimerTask extends TimerTask{
	private static final Log log = LogFactory.getLog(FileClearCycleTimerTask.class);
	
	private static ServletContext context ;		//web 上下文
	
	private static String tmpDirPath; 				//临时文件存放路径

	public static String getTmpDirPath() {
		return tmpDirPath;
	}

	public static void setTmpDirPath(String tmpDirPath) {
		FileClearCycleTimerTask.tmpDirPath = tmpDirPath;
	}

	public static ServletContext getContext() {
		return context;
	}

	public static void setContext(ServletContext context) {
		FileClearCycleTimerTask.context = context;
	}

	/**
	 * 
	 *@desc 删除临时文件夹下的所有文件
	 *@date Jul 30, 2009
	 *@author Administrator
	 */
	public  void clearTempFiles() {
		if(FileClearCycleTimerTask.tmpDirPath == null ) {
			ApplicationContext cxt = WebApplicationContextUtils.getWebApplicationContext(context);
			tmpDirPath = (String) cxt.getBean("TmpDirPath");
			tmpDirPath = InitSystem.getSystemPath() + "/" + tmpDirPath;
		}
		
		File tmpDir = new File(tmpDirPath) ;
		if(tmpDir.exists()) {
			try {
				log.info("delete tmp files ...");
				FileUtil.deleteAllFiles(tmpDir); 
				tmpDir.mkdir();
				log.info("delete tmp files success.");
			} catch (Exception ex) {
				log.error("clear temp file error !!");
				ex.printStackTrace();
			}
		} else {
			try {
				log.info("create tmp directory ...");
				tmpDir.mkdir();
				log.info("create tmp directory success...");
			} catch (Exception e) {
				log.error("create tmp dir failured !!");
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	public void run() {
		clearTempFiles();
	}
}
