package com.est.workflow.process.service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.workflow.process.vo.WfApprove;

public interface IWfApproveService {


	public WfApprove getApproveInfoByTaskInstance(SearchCondition condition);

	public void savApproveInfo(WfApprove approve);

	public Result<WfApprove> getApproveHistoryByPiid(Page page, Long piid);

	
	
}
