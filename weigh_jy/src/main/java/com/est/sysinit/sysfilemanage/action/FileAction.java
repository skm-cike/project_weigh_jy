package com.est.sysinit.sysfilemanage.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysfilemanage.service.ISysFileInfoService;
import com.est.sysinit.sysfilemanage.vo.SysDir;
import com.est.sysinit.sysfilemanage.vo.SysFileinfo;
import com.est.sysinit.sysfilemanage.vo.SysTemplate;
import com.est.sysinit.sysmodule.service.ISysModuleService;
import com.est.sysinit.sysmodule.vo.SysModule;
/**
 * 
 *@desc 文件上传Action基础类，有删除文件action可继承该类
 *@author jingpj
 *@date Jun 25, 2009
 *@path com.est.sysinit.sysfilemanage.action
 *@url est/sysinit/sysfilemanage/File
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
	 *@date Sep 2, 2009
	 *@author jingpj
	 *@param filePath  保存路径
	 *@param fileno 关联对象的fileno
	 *@param fileName 文件名称
	 *@param oldSysFileInfo 原fileinfo（修改时使用）
	 *@return 上传文件的fileno
	 *@throws IOException
	 */
	public String fileUpload() throws IOException{ 
		
		String fileno = req.getParameter("fileno");
		String filename = req.getParameter("filename");
		String oldfileno = req.getParameter("oldfileno");
		String moduleId = req.getParameter("moduleId");
		
		ISysFileInfoService fileInfoService = (ISysFileInfoService)getBean("sysFileInfoService");
		
		SysFileinfo oldSysFileinfo = null;
		oldSysFileinfo = fileInfoService.getSysFileInfoByFileNo(oldfileno);
		
		ISysModuleService sysModuleService  = (ISysModuleService) getBean("sysModuleService");
		SysModule module = sysModuleService.getModuleById(StringUtil.parseLong(moduleId));
		String filePath = module.getFileurl();
		
		String fname =  filename;
		if(filename==null){
			fname = this.filename;
		}
		
		if(fname != null ){
			String newfileno = fileInfoService.savUploadFile(new FileInputStream(file), filePath, fileno, filename, fileFileName,oldSysFileinfo);
			return toSTR("{success:true,fileno:'"+newfileno+"'}");
		} else {
			return null;
		}
	}
	

	/**
	 *@desc 模板文件上传
	 *@date Jun 26, 2009
	 *@author Administrator
	 *@return
	 *@throws IOException
	 */
	public SysFileinfo fileUpload(SysDir dir,SysTemplate template,String fileName) throws IOException{
		
//		reportTemplatePath = (String) this.getBean("reportTemplatePath");
//		IFileUploadService fileUploadService = (IFileUploadService) getBean("fileUploadService");
		
		ISysFileInfoService fileInfoService = (ISysFileInfoService)getBean("sysFileInfoService");
		String fname =  fileName;
		if(fileName==null){
			fname = this.filename;
		}

		if(fname == null ){
			SysFileinfo fileInfo = fileInfoService.savUploadFile(new FileInputStream(file), dir, template, fname, null);
			return fileInfo;
		} else {
			return null;
		}
		
		
		
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
	 *@desc 删除文件
	 *@date Sep 2, 2009
	 *@author jingpj
	 *@throws IOException
	 */
	public String delFileinfo() throws IOException{ 
		
		String strfileinfoId = req.getParameter("fileinfoId");
		
		ISysFileInfoService fileInfoService = (ISysFileInfoService)getBean("sysFileInfoService");
		
		Long fileinfoId = StringUtil.parseLong(strfileinfoId);
		try{
			fileInfoService.delFileinfo(fileinfoId);
			return toSTR("{success:true}");
		}catch(Exception ex) {
			return toSTR("{success:false,info:'删除文件失败'}");
		}
	}
	
	
	/**
	 *@desc 根据fileno得到其关联的所有文件列表
	 *@date Sep 3, 2009
	 *@author jingpj
	 *@return
	 */
	public String getFilesByFileno(){
		String fileno = req.getParameter("fileno");
		ISysFileInfoService fileInfoService = (ISysFileInfoService)getBean("sysFileInfoService");
		Result<SysFileinfo> result = fileInfoService.getSysFileInfoListByFileNo(getPage(),fileno);
		return toJSON(result);
	}
	
	
	/**
	 *@desc 文件下载
	 *@date Jun 30, 2009
	 *@author jingpj
	 *@return
	 *@throws IOException
	 *@url est/sysinit/systemplate/SysTemplate/downloadReport
	 */
	public String downloadFile() throws IOException{
		 try {
	            
			 	String strfileinfoId = req.getParameter("fileinfoId");
				
				ISysFileInfoService fileInfoService = (ISysFileInfoService)getBean("sysFileInfoService");
				
				Long fileinfoId = StringUtil.parseLong(strfileinfoId);
				
				SysFileinfo sysFileinfo = fileInfoService.getSysFileInfoById(fileinfoId);
				
				
			 
//			 	String tmpPath = (String) getBean("TmpDirPath"); //临时文件存放路径
			 	String path = null ; //文件保存地址
//			 	String fileno = req.getParameter("fileno"); //报表文件序列号
				if(sysFileinfo == null || sysFileinfo.getUrl() == null) {
					return toSTR("{success:false}");
				}  else {
					path = sysFileinfo.getUrl();
				}
//			 	String tmpFileName = "" + new Date().getTime();
//			 	if(sysFileinfo.getFileextension()!=null || sysFileinfo.getFileextension().equals("")){
//			 		tmpFileName += "." + sysFileinfo.getFileextension();
//			 	}
//			 	String tmpFileAbsolutePath = req.getSession().getServletContext().getRealPath(tmpPath) +"/"+ tmpFileName;//临时文件名
//			 	String tmpFileUrl = tmpPath + "/" + tmpFileName;
	            
//			 	File destFile = new File(tmpFileAbsolutePath);
	            
	            //拷贝到临时文件
//	            FileUtil.stream2File(new FileInputStream(path), destFile);
				
			 	String tmpFileName = sysFileinfo.getFilename();
			 	if(sysFileinfo.getFileextension()!=null || sysFileinfo.getFileextension().equals("")){
			 		tmpFileName += "." + sysFileinfo.getFileextension();
			 	}
				
			 	FileInputStream srcStream = new FileInputStream(path);
			 	
				String fileName = java.net.URLEncoder.encode(tmpFileName,"UTF-8");
				res.setHeader("Content-Type", "application/force-download");
//				res.setHeader("Content-Type","application/vnd.ms-excel;charset='UTF-8'");
//				res.setContentType("application/x-msdownload");
				res.setHeader("Connection","close");   
	            res.setHeader("Content-Type","application/octet-stream"); 
				res.setHeader("Content-Disposition", "attachment;filename="+ fileName);
				
		//res.setCharacterEncoding("UTF-8");
	            
	            ServletOutputStream out = res.getOutputStream();
	           //FileUtil.stream2Stream(srcStream, out);
	            
	            byte[] buffer = new byte[1024];
	    		int length = 0;
	    		while((length = srcStream.read(buffer)) > 0){
	    			out.write(buffer,0,length);
	    		}
	    		
	    		srcStream.close();
	    		
	            
	            return null;
	            //return toSTR("{success:true,filepath:'"+tmpFileUrl+"'}");
	        } catch (IOException ex) {
	            ex.printStackTrace();
	            return toSTR("{success:false}");
	        } 
	       
	}
	
	
	
	/**
	 * 
	 *@description 跳转到主界面
	 *@date Jun 12, 2009
	 *@author jingpj
	 *@return
	 */
	public String fwdFile(){
		return toJSP("file");
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
