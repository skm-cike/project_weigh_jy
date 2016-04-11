package com.est.weigh.kindcfg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.weigh.kindcfg.dao.IKindDao;
import com.est.weigh.kindcfg.dao.IKindNeedFieldDao;
import com.est.weigh.kindcfg.vo.TableAndKindNeedField;
import com.est.weigh.kindcfg.vo.WeighKindneedfield;
import com.est.weigh.kindcfg.vo.WeighVockind;
import com.est.weigh.table.service.ITableService;
import com.est.weigh.table.vo.TableStruct;
@Service
public class KindServiceImp implements IKindService {
	@Autowired
	private IKindDao kindDao;
	@Autowired
	private IKindNeedFieldDao needFieldDao;
	@Autowired
	private ITableService tableService;
	
	public void delKinds(List<WeighVockind> kinds) {
		kindDao.delAll(kinds);
	}

	public void savKinds(List<WeighVockind> kinds) {
		kindDao.saveAll(kinds);
	}

	public Result<WeighVockind> getKindsByPage(SearchCondition params, Page page) {
		return kindDao.findByPage(page, "from WeighVockind");
	}

	public TableStruct createTable(SearchCondition params) throws Exception {
		TableStruct ts = tableService.createTable(params);
		String kindid = (String)params.get("kindid");
		String tableName = (String)params.get("tableName");
		WeighVockind wv = kindDao.findById(kindid);
		wv.setTableName(tableName);
		kindDao.save(wv);
		this.createKindNeedField(ts, wv, params);
		return ts;
	}
	
	public void createKindNeedField(TableStruct ts, WeighVockind wv, SearchCondition params) {
		String kindCode = wv.getKindCode();
		String fieldName = ts.getTableAttribute();
		String fieldType = (String)params.get("fieldType");
		String readonly = ((String)params.get("readonly")) == null ? "0": ((String)params.get("readonly"));
		String propertyCode = (String)params.get("propertyCode");
		String isInput = ((String)params.get("isInput")) == null ? "0": ((String)params.get("isInput"));
		String isQuery = ((String)params.get("isQuery")) == null ? "0": ((String)params.get("isQuery"));
		String isShow = ((String)params.get("isShow")) == null ? "0": ((String)params.get("isShow"));
		
		WeighKindneedfield wkf = new WeighKindneedfield();
		wkf.setKindCode(kindCode);
		wkf.setFieldName(fieldName);
		wkf.setFieldType(fieldType);
		wkf.setReadonly(Short.parseShort(readonly));
		wkf.setPropertyCode(propertyCode);
		wkf.setIsInput(Short.parseShort(isInput));
		wkf.setIsQuery(Short.parseShort(isQuery));
		wkf.setIsShow(Short.parseShort(isShow));
		needFieldDao.save(wkf);
	}

	public void clearTable(SearchCondition params) throws Exception {
		String kindid = (String) params.get("kindid");
		WeighVockind wv = kindDao.findById(kindid);
		wv.setTableName(null);
		kindDao.save(wv);
		needFieldDao.updateByHql("delete from WeighKindneedfield where kindCode=?", wv.getKindCode());
	}

	public void savFields(EditableGridDataHelper editGridData2) throws Exception {
		List<Object> s_List = editGridData2.getSaveObjects();
		List<Object> d_List = editGridData2.getDelObjects();
		
		List<WeighKindneedfield> savList = new ArrayList(s_List.size());
		List<WeighKindneedfield> delList = new ArrayList(d_List.size());
		for (int i = 0; i < s_List.size(); i++) {
			WeighKindneedfield wkf = (WeighKindneedfield)s_List.get(i);
			if (StringUtil.isEmpty(wkf.getId())) {
				wkf.setId(null);
			}
			savList.add(wkf);
		}
		for (int i = 0; i < d_List.size(); i++) {delList.add((WeighKindneedfield)d_List.get(i));}
		if (savList.size() != 0) {
			needFieldDao.saveAll(savList);
		}
		if (delList.size() != 0) {
			String kindCode = delList.get(0).getKindCode();
			needFieldDao.delAll(delList);
			Long val = (Long)needFieldDao.findUniqueByHql("select count(*) from TableStruct where tableName=(select distinct tableName from WeighVockind where kindCode=?)", kindCode);
			if (val == 0) {
				needFieldDao.updateByHql("delete from WeighKindneedfield where kindCode=?" + kindCode);
			}
		}
	}
	
	public List<WeighKindneedfield> getKindNeedField() {
		return kindDao.findByHql("from WeighKindneedfield");
	}

	public Result<TableAndKindNeedField> merge(Result<TableStruct> tsrst) {
		List<TableStruct> list = tsrst.getContent();
		if (list.size() == 0) {
			Result<TableAndKindNeedField> rst = new Result();
			rst.setContent(new ArrayList());
			rst.setPage(tsrst.getPage());
			return rst;
		}
		
		String fieldNameStrs = "";
		for (int i = 0; i < list.size(); i++) {
			fieldNameStrs += "'" + list.get(i).getTableAttribute() + "',";
		}
		fieldNameStrs = fieldNameStrs.substring(0, fieldNameStrs.length() - 1);
		List<WeighKindneedfield> wkfList = kindDao.findByHql("from WeighKindneedfield t where t.kindCode=" +
				"(select distinct kindCode from WeighVockind where tableName=?) and t.fieldName in (" + fieldNameStrs + ")", list.get(0).getTableName());
		Map<String, WeighKindneedfield> map = new HashMap();
		for (WeighKindneedfield s: wkfList) {
			map.put(list.get(0).getTableName() + "_" + s.getFieldName(), s);
		}
		
		List<TableAndKindNeedField> taknfList = new ArrayList(list.size());
		for (int i = 0; i < list.size(); i++) {
			TableStruct ts = list.get(i);
			WeighKindneedfield wk = map.get(ts.getTableName() + "_" + ts.getTableAttribute());
			TableAndKindNeedField taknf = new TableAndKindNeedField();
			taknf.setFieldName(wk.getFieldName());
			taknf.setFieldType(wk.getFieldType());
			taknf.setId(ts.getId());
			taknf.setIsInput(wk.getIsInput());
			taknf.setIsQuery(wk.getIsQuery());
			taknf.setIsShow(wk.getIsShow());
			taknf.setKindCode(wk.getKindCode());
			taknf.setKindid(wk.getKindid());
			taknf.setPropertyCode(wk.getPropertyCode());
			taknf.setReadonly(wk.getReadonly());
			taknf.setTableAttribute(ts.getTableAttribute());
			taknf.setTableAttrtype(ts.getTableAttrtype());
			taknf.setTableDesc(ts.getTableDesc());
			taknf.setTableId(ts.getTableId());
			taknf.setTableName(ts.getTableName());
			taknfList.add(taknf);
		}
		Result<TableAndKindNeedField> rst = new Result();
		rst.setPage(tsrst.getPage());
		rst.setContent(taknfList);
		
		return rst;
	}
}
