package com.est.sysinit.file.action;

import java.io.File;
import java.io.IOException;

import com.est.common.base.BaseAction;
import com.est.sysinit.file.service.IFileUploadService;
/**
 * 
 *@desc 文件上传Action
 *@author jingpj
 *@date Jun 25, 2009
 *@path com.est.sysinit.file.action.FileAction
 *@corporation Enstrong S&T
 */

@SuppressWarnings("serial")
public class FileAction extends BaseAction {

	private String reportTemplatePath;
	
	/** 文件名 */
	private String filename;
	
	/** 上传类型 
	 * REPORT_TEMPLATE 报表模板 
	 * 
	 * REPORT_FILE    报表文件
	*/
	private String uploadType;

	private File file ;
	
	private String fileFileName;
	
	private String fileContentType;
	
	public void setPath(String path){
		this.reportTemplatePath = path;
	}
	
	
	
	public File getFile() {
		return file;
	}




	public void setFile(File file) {
		this.file = file;
	}




	public String getFilename() {
		return filename;
	}




	public void setFilename(String filename) {
		this.filename = filename;
	}




	@Override
	public Object getModel() {
		return null;
	}
	
	
	
	
	public String getUploadType() {
		return uploadType;
	}




	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}



	/**
	 *@desc 文件上传
	 *@date Jun 26, 2009
	 *@author Administrator
	 *@return
	 *@throws IOException
	 */
	public String fileUpload(String filename) throws IOException{ 
		
		reportTemplatePath = (String) this.getBean("reportTemplatePath");
		
		IFileUploadService fileUploadService = (IFileUploadService) getBean("fileUploadService");
		
		String fileurl = fileUploadService.fileUpload(file, reportTemplatePath, filename);
		
		return fileurl;
		
		
		/*
		System.out.println(req.getParameter("uploadfile"));
		DiskFileItemFactory dfif = new DiskFileItemFactory();
		dfif.setRepository(new File(path));
		dfif.setSizeThreshold(1024*1024);
		ServletFileUpload upload = new ServletFileUpload(dfif);
		ServletActionContext.getRequest();
		try {
			List<FileItem> lst = upload.parseRequest(ServletActionContext.getRequest());
			for(FileItem fi : lst) {
				System.out.println(fi.getFieldName());
			}
			
			
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		
		
		String a ;
		
		InputStream is = req.getInputStream();
		BufferedReader br= new BufferedReader(new InputStreamReader(is));
		while( (a = br.readLine()) != null) {
			System.out.println(a);
		}
		*/
		
	}
	

	/**
	 * 
	 *@description 跳转到group主界面
	 *@date Jun 12, 2009
	 *@author jingpj
	 *@return
	 */
	public String fwdFile(){
		return toJSP("file");
	}

	public String fwdTest(){
		return toJSP("test");
	}


	public String getFileFileName() {
		return fileFileName;
	}




	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}




	public String getFileContentType() {
		return fileContentType;
	}




	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
	
	
}
