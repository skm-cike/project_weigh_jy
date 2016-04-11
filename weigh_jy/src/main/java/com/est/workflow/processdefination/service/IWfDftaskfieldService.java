package com.est.workflow.processdefination.service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.workflow.processdefination.vo.WfDftaskfield;


public interface IWfDftaskfieldService {

	Result<WfDftaskfield> getWfDftaskfieldList(Page page,
			SearchCondition condition);

	boolean savWfDftaskfieldList(EditableGridDataHelper editGridData);

	
	
	
}
