package com.est.workflow.processdefination.service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.workflow.processdefination.vo.WfDftaskuser;


public interface IWfDftaskuserService {

	Result<WfDftaskuser> getWfDftaskuserList(Page page,
			SearchCondition condition);

	boolean savWfDftaskuserList(EditableGridDataHelper editGridData);

	
	
	
}
