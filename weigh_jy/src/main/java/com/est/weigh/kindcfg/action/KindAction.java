package com.est.weigh.kindcfg.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.weigh.kindcfg.service.IKindService;
import com.est.weigh.kindcfg.vo.TableAndKindNeedField;
import com.est.weigh.kindcfg.vo.WeighKindneedfield;
import com.est.weigh.kindcfg.vo.WeighVockind;
import com.est.weigh.table.service.ITableService;
import com.est.weigh.table.vo.TableStruct;

/**
 * @描述: 种类配置
 * @author 陆华
 *
 */
@SuppressWarnings("serial")
public class KindAction extends BaseAction {
	@Autowired
	private IKindService kindService;
	@Autowired
	private ITableService tableService;
	@Override
	public Object getModel() {
		return null;
	}

	public String fwdKindcfg() {
		return toJSP("kindcfg");
	}
	
	public String savKinds() {
		String data = req.getParameter("data");
		try {
			data = java.net.URLDecoder.decode(data,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		EditableGridDataHelper editGridData = EditableGridDataHelper.data2bean(data, WeighVockind.class);
		List<Object> savList = editGridData.getSaveObjects();
		List<Object> delList = editGridData.getDelObjects();
		try {
			if (savList != null && savList.size() != 0) {
				List<WeighVockind> kinds = new ArrayList(savList.size());
				for (Object o: savList) {
					WeighVockind t = (WeighVockind)o;
					if ("".equals(t.getId())) {
						t.setId(null);
					}
					kinds.add(t);
				}
				kindService.savKinds(kinds);
			}
			if (delList != null && delList.size() != 0) {
				List<WeighVockind> kinds = new ArrayList(delList.size());
				for (Object o: delList) {
					kinds.add((WeighVockind)o);
				}
				kindService.delKinds(kinds);
			}
			return toSTR("{success:true}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			return toSTR("{success:false,msg:'" + e.getMessage() + "'}");
		}
	}
	
	public String getKindsByPage() {
		Result<WeighVockind> rst = kindService.getKindsByPage(this.params, this.getPage());
		return toJSON(rst);
	}
	
	public String createTable() {
		try {
			TableStruct ts = kindService.createTable(this.params);
			return toJSON(ts, "{success: true, data:", "}");
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "{success:false,error:'" + e.getMessage().trim() + "'}";
			return toSTR(msg);
		}
	}
	
	public String clearTable() {
		try {
			kindService.clearTable(this.params);
			return toSTR("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "{success:false,error:'" + e.getMessage().trim() + "'}";
			return toSTR(msg);
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
		EditableGridDataHelper editGridData2 = EditableGridDataHelper.data2bean(data, WeighKindneedfield.class);
		
		try {
			tableService.savFields(editGridData);
			kindService.savFields(editGridData2);
			return toSTR("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
			return toSTR("{success:false,error:'" + e.getMessage() + "'}");
		}
	}
	
	public String getTabFields() {
		Result<TableStruct> tsrst = tableService.getTabFieldsByPate(this.params, this.getPage());
		Result<TableAndKindNeedField> rst = kindService.merge(tsrst);
		return toJSON(rst);
	}
}
