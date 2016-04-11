package com.est.workflow.processdefination.action;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.workflow.processdefination.service.IWfDftaskfieldService;
import com.est.workflow.processdefination.vo.WfDftaskfield;

/**
 * 
 *@desc 流程任务设置字段
 *@author jingpj
 *@date Nov 2, 2009
 *@path com.est.workflow.processdefination.action.WfDftaskfieldAction
 *@corporation Enstrong S&T
 */
public class WfDftaskfieldAction extends BaseAction {

	private static final long serialVersionUID = 2524051993035507085L;

	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * 
	 *@desc 查询列表
	 *@date Nov 2, 2009
	 *@author jingpj
	 *@return
	 */
	public String getWfDftaskfieldList(){
		String taskId = req.getParameter("taskId");
		
		SearchCondition condition = new SearchCondition();
		condition.set("taskId",StringUtil.parseLong(taskId));
		
		IWfDftaskfieldService service = (IWfDftaskfieldService)getBean("wfDftaskfieldService");
		Result<WfDftaskfield> result =null;
		result = service.getWfDftaskfieldList(getPage(), condition); 
		return toJSON(result);
		
	}
	
	/**
	 * 
	 *@desc 批量保存
	 *@date Nov 2, 2009
	 *@author jingpj
	 *@return
	 */
	public String savWfDftaskfieldList() {
		String data = req.getParameter("data");
		try {
			data = java.net.URLDecoder.decode(data, "utf-8");
			EditableGridDataHelper editGridData = EditableGridDataHelper
					.data2bean(data, WfDftaskfield.class);
			IWfDftaskfieldService service = (IWfDftaskfieldService)getBean("wfDftaskfieldService");
			if(service.savWfDftaskfieldList(editGridData)){
				return toSTR("{success:true}");
			}
				return toSTR("{success:false}");
		} catch (Exception e) {
			return toSTR("{success:false}");
		}
	}
}
