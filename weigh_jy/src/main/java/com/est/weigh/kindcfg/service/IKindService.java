package com.est.weigh.kindcfg.service;

import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.weigh.kindcfg.vo.TableAndKindNeedField;
import com.est.weigh.kindcfg.vo.WeighKindneedfield;
import com.est.weigh.kindcfg.vo.WeighVockind;
import com.est.weigh.table.vo.TableStruct;

public interface IKindService {

	void savKinds(List<WeighVockind> kinds);

	void delKinds(List<WeighVockind> kinds);

	Result<WeighVockind> getKindsByPage(SearchCondition params, Page page);

	TableStruct createTable(SearchCondition params)  throws Exception ;

	void clearTable(SearchCondition params) throws Exception ;

	void savFields(EditableGridDataHelper editGridData2) throws Exception ;

	Result<TableAndKindNeedField> merge(Result<TableStruct> tsrst);
	
	public List<WeighKindneedfield> getKindNeedField();
}
