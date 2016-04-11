package com.est.weigh.table.service;

import java.sql.SQLException;

import org.hibernate.HibernateException;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.weigh.table.vo.TableStruct;

public interface ITableService {

	TableStruct createTable(SearchCondition params)  throws HibernateException, SQLException;

	Result<TableStruct> getTabFieldsByPate(SearchCondition params, Page page);

	void savFields(EditableGridDataHelper gridData) throws Exception ;

}
