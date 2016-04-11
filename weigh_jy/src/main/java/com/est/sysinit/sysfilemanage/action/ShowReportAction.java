package com.est.sysinit.sysfilemanage.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.fileutil.DirectoryCreateException;
import com.est.common.ext.util.fileutil.FileUtil;
import com.est.sysinit.file.action.FileAction;
import com.est.sysinit.sysfilemanage.service.ISysTemplateService;
import com.est.sysinit.sysfilemanage.vo.SysTemplate;

/**
 *@desc 报表显示Action
 *@author jingpj
 *@date Jul 14, 2009
 *@path com.est.sysinit.sysfilemanage.action.DisplayReportAction
 *@url est/sysinit/systemplate/ShowReport/
 *@corporation Enstrong S&T
 */
public class ShowReportAction extends FileAction {

	private static final long serialVersionUID = 2127723231782863147L;

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
	 *@desc 转向到报表显示页面 
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@return
	 */
	public String fwdShowReport(){
		return toJSP("showReport");
	}
	/**
	 * 
	 *@desc 分页获取模板list 
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@return
	 */
	public String getTemplateList(){
		String typeId = req.getParameter("typeid");
		String templateName = req.getParameter("templatename");
		String typeCode = req.getParameter("typecode");
		
		SearchCondition sc = new SearchCondition();
		sc.set("typeId",typeId);
		sc.set("templateName", templateName);
		sc.set("typeCode", typeCode);
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
			//sysTemplate.setTemplatecode("TPL_BBBB"); //测试数据
			String url = fileUpload(sysTemplate.getTemplatecode()+".cll");
			templateService.savTemplate(sysTemplate);
			return toSTR("{success:true}");
		}catch(Exception ex){
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
		SysTemplate tpl = templateService.getTemplateById(sysTemplate.getTemplateid());
		
		String reportTemplatePath = (String) this.getBean("reportTemplatePath");
		boolean isNew = false;
		try {
			InputStream is = req.getInputStream();
			String url = null;
			
			/*
			if(tpl.getTemplateurl()==null) {
				url = reportTemplatePath + "/" + tpl.getTemplatecode() + ".cll";
				isNew = true;
			} else {
				url = tpl.getTemplateurl();
			}
			*/
			File file = new File(url);
			File dirFile = new File(file.getParent());
			
			//检查文件夹是否存在，没有则创建
            FileUtil.chkDirExist(dirFile);
            
			//保存文件
            FileUtil.stream2File(is, file);
            
			if(isNew) {
				templateService.savTemplate(tpl);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DirectoryCreateException e) {
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
	            
			 	String path = req.getParameter("filepath");
			 	// path是指欲下载的文件的路径。
	            File file = new File(path);
	            // 取得文件名。
	            String filename = file.getName();
	            
	            String dirPath = req.getSession().getServletContext().getRealPath("/report_tmp");
	            //检查文件夹是否存在，没有则创建
	            FileUtil.chkDirExist(dirPath);
	            //临时cll文件
	            File destFile = new File(dirPath+"/"+(new Date().getTime())+".cll");
	            
	            // 取得文件的后缀名。
	            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
	            //将模板拷贝到临时cll文件
	            FileUtil.stream2File(new FileInputStream(path), destFile);
	            //向前台cell控件返回临时cll文件路径
	            return toSTR("{success:true,filepath:'/report_tmp/"+destFile.getName()+"'}");
	        } catch (IOException ex) {
	            ex.printStackTrace();
	            return toSTR("{success:false}");
	        } catch (DirectoryCreateException e) {
				e.printStackTrace();
				return toSTR("{success:false}");
			}
	       
	}
}
