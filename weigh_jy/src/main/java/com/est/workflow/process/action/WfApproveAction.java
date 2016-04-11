package com.est.workflow.process.action;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.workflow.process.service.IWfApproveService;
import com.est.workflow.process.vo.WfApprove;

/**
 *@desc 工作流审批意见action
 *@author jingpj
 *@date Nov 2, 2009
 *@path com.est.workflow.process.action.WfApproveAction
 *@corporation Enstrong S&T
 */
public class WfApproveAction extends BaseAction {
	private static final long serialVersionUID = -5236565446298628901L;
	
	private WfApprove approve = new WfApprove();
	
	public WfApprove getApprove() {
		return approve;
	}

	public void setApprove(WfApprove approve) {
		this.approve = approve;
	}

	@Override
	public Object getModel() {
		return approve;
	}

	
	/**
	 *@desc 得到流程节点的审批信息
	 *@date Oct 15, 2009
	 *@author jingpj
	 *@return
	 */
	public String getApproveInfo() {
		Long taskinstanceId = StringUtil.parseLong(req.getParameter("taskinstanceId"));
		Long masterId = StringUtil.parseLong(req.getParameter("_masterid"));
		Long taskDefId = StringUtil.parseLong(req.getParameter("_taskDefid"));
		Long wfProcessInstanceId = StringUtil.parseLong(req.getParameter("_wfProcessInstanceId"));
		
		SearchCondition condition = new SearchCondition();
		condition.set("taskinstanceId", taskinstanceId);
		condition.set("wfProcessInstanceId",wfProcessInstanceId);
		
		IWfApproveService wfApproveService = (IWfApproveService) getBean("wfApproveService");
		WfApprove approve = wfApproveService.getApproveInfoByTaskInstance(condition);
		return toJSON(approve,"{success:true,data:","}");
	}	
	
	/**
	 *@desc 保存流程节点的审批信息
	 *@date Oct 15, 2009
	 *@author jingpj
	 *@return
	 */
	public String savApproveInfo() {
		
		IWfApproveService wfApproveService = (IWfApproveService) getBean("wfApproveService");
		wfApproveService.savApproveInfo(approve);
		
		return toJSON(approve,"{success:true,data:","}");
	}
	
	/**
	 *@desc 得到审批历史记录
	 *@date Nov 2, 2009
	 *@author jingpj
	 *@return
	 */
	public String getgetApproveInfoList(){
		
		Long piid = StringUtil.parseLong(req.getParameter("piid"));
		IWfApproveService wfApproveService = (IWfApproveService) getBean("wfApproveService");
		Result<WfApprove> result = wfApproveService.getApproveHistoryByPiid(getPage(),piid);
		return toJSON(result);
	}
	
	
	
}
