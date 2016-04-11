package com.est.sysinit.sysfilemanage.action;

import java.io.UnsupportedEncodingException;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysfilemanage.service.ISysTemplateparamService;
import com.est.sysinit.sysfilemanage.vo.SysTemplateparam;

public class SysTemplateparamAction extends BaseAction {

	private static final long serialVersionUID = -5236565446298628901L;
	SysTemplateparam sysTemplateparam = new SysTemplateparam();
	public void setSysTemplateparam(SysTemplateparam sysTemplateparam){
		this.sysTemplateparam = sysTemplateparam;
	}
	@Override
	public Object getModel() {
		return sysTemplateparam;
	}
	/**
	 * 
	 *@desc 分页查询模板参数 
	 *@date Jun 26, 2009
	 *@author zhanglk
	 *@return
	 */
	public String getTemplateparamList(){
		String templateId=req.getParameter("templateid");
		SearchCondition sc = new SearchCondition();
		sc.set("templateid", templateId);
		ISysTemplateparamService templateParamService = 
			(ISysTemplateparamService) getBean("sysTemplateparamService");
		Result<SysTemplateparam> result = 
			templateParamService.getTemplateparamResult(getPage(),sc);
		if(result.getContent().size()==0) {
			return toSTR("{success:true,rows:[]}");
		} else {
			return toJSON(result);
		}
	}
	/**
	 * 
	 *@desc 保存可编辑grid
	 *@date Jun 26, 2009
	 *@author zhanglk
	 *@return
	 */
	public String savTemplateparamList(){
		ISysTemplateparamService templateParamService = 
			(ISysTemplateparamService) getBean("sysTemplateparamService");
		String data = req.getParameter("data");
		try {
			data = java.net.URLDecoder.decode(data,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		EditableGridDataHelper editGridData = EditableGridDataHelper.data2bean(data, SysTemplateparam.class);
		templateParamService.savChanges(editGridData);
		return toSTR("{success:true}");
	}

}
