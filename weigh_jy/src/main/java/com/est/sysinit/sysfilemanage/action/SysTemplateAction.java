package com.est.sysinit.sysfilemanage.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.fileutil.FileUtil;
import com.est.sysinit.sysfilemanage.service.ISysDirService;
import com.est.sysinit.sysfilemanage.service.ISysFileInfoService;
import com.est.sysinit.sysfilemanage.service.ISysTemplateService;
import com.est.sysinit.sysfilemanage.vo.SysDir;
import com.est.sysinit.sysfilemanage.vo.SysFileinfo;
import com.est.sysinit.sysfilemanage.vo.SysTemplate;

/**
 *@desc 模板管理action
 *@author zhanglk
 *@date Jun 25, 2009
 *@path com.est.sysinit.sysfilemanage.SysTemplateAction
 *@corporation Enstrong S&T 
 */
public class SysTemplateAction extends FileAction {

	private static final long serialVersionUID = -5236565446298628901L;
	SysTemplate sysTemplate = new SysTemplate();
	public void setSysTemplate(SysTemplate sysTemplate){
		this.sysTemplate = sysTemplate;
	}
	@Override
	public Object getModel() {
		return sysTemplate;
	}
	/**
	 * 
	 *@desc 转向到模板管理页面 
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@return
	 */
	public String fwdTemplate(){
		return toJSP("template");
	}
	/**
	 * 
	 *@desc 分页获取模板list 
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@return
	 */
	public String getTemplateList(){
		String dirId = req.getParameter("dirid");
		String templateName = req.getParameter("templatename");
		String dirCode = req.getParameter("dircode");
		
		SearchCondition sc = new SearchCondition();
		sc.set("dirId",dirId);
		sc.set("templateName", templateName);
		sc.set("dirCode", dirCode);
		ISysTemplateService templateService = (ISysTemplateService) getBean("sysTemplateService");
		Result<SysTemplate> result = null;
		try{
			result = templateService.getTemplateList(getPage(), sc);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return toJSON(result);
	}
	/**
	 * 
	 *@desc 获取模板对象 
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@return
	 */
	public String getTemplate(){
		String templateId = req.getParameter("templateid");
		ISysTemplateService templateService = (ISysTemplateService) getBean("sysTemplateService");
		SysTemplate template = templateService.getTemplateById(StringUtil.parseLong(templateId));
		return toJSON(template, "{success: true, data:", "}");
	}
	
	
	/**
	 *@desc 通过编码获取模板对象 
	 *@date Jul 14, 2009
	 *@author jingpj
	 *@url est/sysinit/systemplate/SysTemplate/getTemplateByCode
	 *@return
	 */
	public String getTemplateByCode(){
		String templateCode = req.getParameter("templatecode");
		ISysTemplateService templateService = (ISysTemplateService) getBean("sysTemplateService");
		SysTemplate template = templateService.getTemplateByCode(templateCode);
		if(template == null) {
			return toSTR("{success:false}");
		} else {
			return toJSON(template, "{success: true, data:", "}");
		}
	}
	
	/**
	 * 
	 *@desc 删除模板对象 
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@return
	 */
	public String delTemplate(){
		ISysTemplateService templateService = (ISysTemplateService) getBean("sysTemplateService");
		try{
			templateService.delTemplate(sysTemplate.getTemplateid());
			return toSTR("{success:true}");
		} catch (Exception ex) {
			return toSTR("{success:false}");
		}
	}
	/**
	 * 
	 *@desc 保存模板 
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@return
	 */
	public String savTemplate(){
		ISysTemplateService templateService = (ISysTemplateService) getBean("sysTemplateService");
		
		try{
			
			SysDir dir = sysTemplate.getSysDir(); //报表路径
			
			ISysDirService dirService = (ISysDirService) getBean("sysDirService");
			dir = dirService.getSysDirById(dir.getDirid());
			
			if(getFile()!=null) {
				SysFileinfo fileInfo = fileUpload(dir,sysTemplate,null);
				sysTemplate.setFileno(fileInfo.getFileno());
			}
			
			templateService.savTemplate(sysTemplate);
			
			return toSTR("{success:true}");
		}catch(Exception ex){
			ex.printStackTrace();
			return toSTR("{success:false}");
		}
	}
	
	
	/**
	 * 
	 *@desc 跳转到华表模板初始化界面
	 *@date Jun 28, 2009
	 *@author jingpj
	 *@path est/sysinit/systemplate/SysTemplate/fwdTemplateInit
	 *@return
	 */
	public String fwdTemplateInit(){
		return toJSP("templateInit");
	}
	
	/**
	 * 
	 *@desc 在华表编辑中调用的保存方法
	 *@date Jun 30, 2009
	 *@author jingpj
	 *@path est/sysinit/systemplate/SysTemplate/savReport
	 *@return
	 */
	public String savReport(){
		
		ISysTemplateService templateService = (ISysTemplateService) getBean("sysTemplateService");
		ISysFileInfoService sysFileInfoService = (ISysFileInfoService) getBean("sysFileInfoService");
		
		String fileno = req.getParameter("fileno");
		SysFileinfo oldSysFileinfo = null;
		SysTemplate tpl = null;
		if(fileno == null || "".equals(fileno)) {
			tpl = templateService.getTemplateById(sysTemplate.getTemplateid());
			oldSysFileinfo = sysFileInfoService.getSysFileInfoByFileNo(tpl.getFileno());
		} else {
			oldSysFileinfo = sysFileInfoService.getSysFileInfoByFileNo(fileno);
			tpl = oldSysFileinfo.getSysTemplate();
		}
		
		try {
			InputStream is = req.getInputStream();
			SysFileinfo newSysFileinfo = sysFileInfoService.savUploadFile(is, tpl.getSysDir(), tpl, tpl.getTemplatecode()+".cll", oldSysFileinfo);
			
			if(oldSysFileinfo == null) {
				tpl.setFileno(newSysFileinfo.getFileno());
				templateService.savTemplate(tpl);
			}
			
			
			
			/*
			if(tpl.getTemplateurl()==null) {
				url = reportTemplatePath + "/" + tpl.getTemplatecode() + ".cll";
				isNew = true;
			} else {
				url = tpl.getTemplateurl();
			}
			
			File file = new File(url);
			File dirFile = new File(file.getParent());
			
			//检查文件夹是否存在，没有则创建
            FileUtil.chkDirExist(dirFile);
            
			//保存文件
            FileUtil.stream2File(is, file);
            
			if(isNew) {
				templateService.savTemplate(tpl);
			}
			*/
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	
	/**
	 *@desc 文件下载
	 *@date Jun 30, 2009
	 *@author jingpj
	 *@return
	 *@throws IOException
	 *@url est/sysinit/systemplate/SysTemplate/downloadReport
	 */
	public String downloadReport() throws IOException{
		 try {
	            
			 	String tmpPath = (String) getBean("TmpDirPath"); //临时文件存放路径
			 	String path = null ; //文件保存地址
			 	String fileno = req.getParameter("fileno"); //报表文件序列号
			 	ISysFileInfoService sysFileInfoService = (ISysFileInfoService) getBean("sysFileInfoService");
			 	SysFileinfo sysFileinfo = null;
			 	ISysTemplateService templateService = (ISysTemplateService) getBean("sysTemplateService");
				SysTemplate tpl = templateService.getTemplateById(sysTemplate.getTemplateid());
			 	if(fileno != null) {
			 		sysFileinfo = sysFileInfoService.getSysFileInfoByFileNo(fileno);
					
					if(sysFileinfo == null || sysFileinfo.getUrl() == null) {
						return toSTR("{success:false}");
					}  else {
						path = sysFileinfo.getUrl();
					}
			 	} else {
			 		 return toSTR("{success:false}");
			 	}
	            
			 	String tmpFileName = (new Date().getTime())+".cll"; 
			 	String tmpFileAbsolutePath = req.getSession().getServletContext().getRealPath(tmpPath) +"/"+ tmpFileName;//临时文件名
			 	String tmpFileUrl = tmpPath + "/" + tmpFileName;
	            
			 	File destFile = new File(tmpFileAbsolutePath);
	            
	            //将模板拷贝到临时cll文件
	            FileUtil.stream2File(new FileInputStream(path), destFile);
	            //向前台cell控件返回临时cll文件路径
	            return toSTR("{success:true,filepath:'"+tmpFileUrl+"'}");
	        } catch (IOException ex) {
	            ex.printStackTrace();
	            return toSTR("{success:false}");
	        } 
	       
	}
}
