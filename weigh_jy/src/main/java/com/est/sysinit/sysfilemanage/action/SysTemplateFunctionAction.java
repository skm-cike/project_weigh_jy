package com.est.sysinit.sysfilemanage.action;

import java.io.UnsupportedEncodingException;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysfilemanage.service.ISysTemplateFunctionService;
import com.est.sysinit.sysfilemanage.service.ISysTemplateService;
import com.est.sysinit.sysfilemanage.vo.SysTemplate;
import com.est.sysinit.sysfilemanage.vo.SysTemplatefunction;

public class SysTemplateFunctionAction extends BaseAction {

	private static final long serialVersionUID = -5236565446298628901L;
	SysTemplatefunction sysTemplateFunction = new SysTemplatefunction();
	public void setSysTemplateFunction(SysTemplatefunction sysTemplateFunction){
		this.sysTemplateFunction = sysTemplateFunction;
	}
	@Override
	public Object getModel() {
		return sysTemplateFunction;
	}
	/**
	 * 
	 *@desc 分页查询模板函数 
	 *@date Jun 26, 2009
	 *@author zhanglk
	 *@return
	 */
	public String getTemplatefunctionList(){
		String templateId=req.getParameter("templateid");
		SearchCondition sc = new SearchCondition();
		sc.set("templateid", templateId);
		ISysTemplateFunctionService templateFunctionService =  (ISysTemplateFunctionService) getBean("sysTemplateFunctionService");
		Result<SysTemplatefunction> result =  templateFunctionService.getTemplateFunctionResult(getPage(),sc);
		if(result.getContent().size()==0) {
			return toSTR("{success:true,rows:[]}");
		} else {
			return toJSON(result);
		}
	}
	/**
	 * 
	 *@desc 
	 *@date Jun 26, 2009
	 *@author zhanglk
	 *@return
	 */
	public String savTemplateFunctionList(){
		ISysTemplateFunctionService templateFunctionService = 
			(ISysTemplateFunctionService) getBean("sysTemplateFunctionService");
		String data = req.getParameter("data");
		try {
			data = java.net.URLDecoder.decode(data,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		EditableGridDataHelper editGridData = EditableGridDataHelper.data2bean(data, SysTemplatefunction.class);
		templateFunctionService.savChanges(editGridData);
		return toSTR("{success:true}");
	}
	
	/**
	 *@desc 计算从用户模块中加载的自定义函数
	 *@date Jul 17, 2009
	 *@author Administrator
	 *@return
	 *@throws Exception
	 */
	public String calcFunc() throws Exception {
		try{
			
			String reportCode = req.getParameter("templatecode");
			String funName = req.getParameter("funName");
			String funArg = req.getParameter("funArg");
			String funContent = req.getParameter("funContent");
			
			
			ISysTemplateService templateService = (ISysTemplateService) getBean("sysTemplateService");
			SysTemplate template = templateService.getTemplateByCode(reportCode);
			
			ISysTemplateFunctionService templateFunctionService = (ISysTemplateFunctionService) getBean("sysTemplateFunctionService");
			
			String ret = templateFunctionService.calcFunc(template.getTemplateid(), funName, funArg,funContent);
			
			return toSTR("{success:true,data:"+ret+"}");
		}catch(Exception ex){
			ex.printStackTrace();
			return toSTR("{success:false}");
		}
		
		
	}
}
