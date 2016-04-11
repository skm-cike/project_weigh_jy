package com.est.workflow.processdefination.action;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.workflow.processdefination.service.IWfDftaskconditionService;
import com.est.workflow.processdefination.vo.WfDftaskcondition;

/**
 * 
 *@desc 流程任务分配用户
 *@author jingpj
 *@date Nov 2, 2009
 *@path com.est.workflow.processdefination.action.WfDftaskuserAction
 *@corporation Enstrong S&T
 */
public class WfDftaskconditionAction extends BaseAction {

	@Override
	public Object getModel() {
		return null;
	}
	
	
	/**
	 * 
	 *@desc 获取任务条件列表
	 *@date Nov 3, 2009
	 *@author jingpj
	 *@return
	 */
	public String getWfDftaskconditionListByPage(){
		String taskId=req.getParameter("taskId");
		try{
			if(taskId!=null && !"".equals(taskId)){
				
				SearchCondition condition=new SearchCondition();
				condition.set("taskId", StringUtil.parseLong(taskId));
				
				IWfDftaskconditionService service = (IWfDftaskconditionService)getBean("wfDftaskconditionService");
				
				return toJSON(service.getWfDftaskconditionListByPage(condition, getPage()));
				
			}else{
				return toSTR("{success:true,rows:[]}");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return toSTR("{success:true,rows:[]}");
		}
		
	}
	
	/**
	 * 
	 *@desc 批量删除、保存列表信息
	 *@date Nov 3, 2009
	 *@author jingpj
	 *@return
	 */
	public String savWfDftaskconditionList(){
		String data = req.getParameter("data");
		try {
			data = java.net.URLDecoder.decode(data, "utf-8");
			
			EditableGridDataHelper editGridData = EditableGridDataHelper
					.data2bean(data, WfDftaskcondition.class);
			IWfDftaskconditionService service = (IWfDftaskconditionService)getBean("wfDftaskconditionService");
			
			service.savWfDftaskconditionList(editGridData);
			
			return toSTR("{success:true}");
			
		} catch (Exception e) {
			return toSTR("{success:false}");
		}
	}
	
}
