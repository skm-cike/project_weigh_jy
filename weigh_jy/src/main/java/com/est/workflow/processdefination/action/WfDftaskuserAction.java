package com.est.workflow.processdefination.action;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.workflow.processdefination.service.IWfDftaskuserService;
import com.est.workflow.processdefination.vo.WfDftaskuser;

/**
 * 
 *@desc 流程任务分配用户
 *@author jingpj
 *@date Nov 2, 2009
 *@path com.est.workflow.processdefination.action.WfDftaskuserAction
 *@corporation Enstrong S&T
 */
public class WfDftaskuserAction extends BaseAction {

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
	public String getWfDftaskuserList(){
		String taskId = req.getParameter("taskId");
		
		SearchCondition condition = new SearchCondition();
		condition.set("taskId",StringUtil.parseLong(taskId));
		
		IWfDftaskuserService service = (IWfDftaskuserService)getBean("wfDftaskuserService");
		Result<WfDftaskuser> result =null;
		result = service.getWfDftaskuserList(getPage(), condition); 
		return toJSON(result);
		
	}
	
	/**
	 * 
	 *@desc 批量保存
	 *@date Nov 2, 2009
	 *@author jingpj
	 *@return
	 */
	public String savWfDftaskuserList() {
		String data = req.getParameter("data");
		try {
			data = java.net.URLDecoder.decode(data, "utf-8");
			EditableGridDataHelper editGridData = EditableGridDataHelper
					.data2bean(data, WfDftaskuser.class);
			IWfDftaskuserService service = (IWfDftaskuserService)getBean("wfDftaskuserService");
			if(service.savWfDftaskuserList(editGridData)){
				return toSTR("{success:true}");
			}
				return toSTR("{success:false}");
		} catch (Exception e) {
			return toSTR("{success:false}");
		}
	}
}
