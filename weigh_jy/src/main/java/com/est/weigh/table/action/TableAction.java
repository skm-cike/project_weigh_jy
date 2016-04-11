package com.est.weigh.table.action;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.weigh.kindcfg.vo.WeighVockind;
import com.est.weigh.table.service.ITableService;
import com.est.weigh.table.vo.TableStruct;

public class TableAction extends BaseAction{
	@Autowired
	private ITableService tableService;
	@Override
	public Object getModel() {
		return null;
	}
	
	public String createTable() {
		try {
			TableStruct ts = tableService.createTable(this.params);
			return toJSON(ts, "{success: true, data:", "}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			return toSTR("{success:false,error:'" + e.getMessage() + "'}");
		} catch (SQLException e) {
			e.printStackTrace();
			return toSTR("{success:false,error:'" + e.getMessage() + "'}");
		}
	}
	
	public String savFields() {
		String data = req.getParameter("data");
		try {
			data = java.net.URLDecoder.decode(data,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		EditableGridDataHelper editGridData = EditableGridDataHelper.data2bean(data, TableStruct.class);
		
		try {
			tableService.savFields(editGridData);
			return toSTR("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
			return toSTR("{success:false,error:'" + e.getMessage() + "'}");
		}
	}
	
	public String getTabFields() {
		Result<TableStruct> rst = tableService.getTabFieldsByPate(this.params, this.getPage());
		return toJSON(rst);
	}
}
