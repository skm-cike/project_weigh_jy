package com.est.weigh.table.service;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.weigh.table.dao.ITableDao;
import com.est.weigh.table.vo.TableStruct;

@Service
public class TableServiceImp implements ITableService{
	@Autowired
	private ITableDao tableDao;
	public TableStruct createTable(SearchCondition params) throws HibernateException, SQLException {
		String tableName = (String) params.get("tableName");
		String tableAttribute = "ID";
		String tableAttrtype = "VARCHAR2(32)";
		String tableDesc = (String) params.get("tableDesc");
		String tableId = "ID";
		if (StringUtil.isEmpty(tableName)) {
			throw new BaseBussinessException("必须输入表名!");
		}
		//String dropSql = "drop table " + tableName + " cascade constraints;";
		String ctableSql = "create table " + tableName + "(" +
			tableAttribute + " " + tableAttrtype + " NOT NULL, constraint PK_" + tableName + "_ID primary key (" + tableAttribute + ")" + 
		")";
		String commentSql = "comment on table " + tableName + " is '" + tableDesc + "'";
		if (StringUtil.isEmpty(tableDesc)) {
			commentSql = null;
		}
		Statement sm = tableDao.getSessionObj().connection().createStatement();
		sm.addBatch(ctableSql);
		if (commentSql != null) {
			sm.addBatch(commentSql);
		}
		sm.executeBatch();
		
		TableStruct ts = new TableStruct();
		ts.setTableAttribute(tableAttribute);
		ts.setTableAttrtype(tableAttrtype);
		ts.setTableDesc(tableDesc);
		ts.setTableId(tableId);
		ts.setTableName(tableName);
		tableDao.save(ts);
		return ts;
	}
	
	/**
	 * @描述: 更改列
	 * @param ts
	 * @return
	 */
	private List<String> alertColumnSql(TableStruct ts) {
		TableStruct oldTs = tableDao.findById(ts.getId());
		List<String> sqlList = new ArrayList();
		if (!oldTs.getTableAttribute().equals(ts.getTableAttribute()) || !oldTs.getTableAttrtype().equals(ts.getTableAttrtype())) {
			String sql = "alter table " + oldTs.getTableName() + " modify " + oldTs.getTableAttribute() + " to "
					 	+ ts.getTableAttribute();
			sqlList.add(sql);
		}
		if (!StringUtil.isEmpty(ts.getTableDesc()) && !ts.getTableDesc().equals(oldTs.getTableDesc())) {
			String sql = "comment on column " + ts.getTableName() + "." + ts.getTableAttribute() + " is '" + ts.getTableDesc() + "'";
			sqlList.add(sql);
			if (ts.getTableAttribute().equals(ts.getTableId())) {
				sql = "comment on table " + ts.getTableName() + " is '" + ts.getTableDesc() + "'";
				sqlList.add(sql);
			}
		}
		return sqlList;
	}
	
	/**
	 * @描述: 添加列
	 * @param ts
	 * @return
	 */
	private List<String> addColumnSql(TableStruct ts) {
		List<String> sqlList = new ArrayList();
		String sql = "alter table " + ts.getTableName() + " add " + ts.getTableAttribute() + " " + ts.getTableAttrtype();
		sqlList.add(sql);
		sql = "comment on column " + ts.getTableName() + "." + ts.getTableAttribute() + " is '" + ts.getTableDesc() + "'";
		sqlList.add(sql);
		if (ts.getTableAttribute().equals(ts.getId())) {
			sql = "comment on table " + ts.getTableName() + " is '" + ts.getTableDesc() + "'";
			sqlList.add(sql);
		}
		return sqlList;
	}
	
	/**
	 * @描述: 删除列
	 * @param ts
	 * @return
	 */
	private List<String> delColumnSql(TableStruct ts) {
		List<String> list = new ArrayList();
		if (ts.getTableId().equals(ts.getTableAttribute())) {  //如果删除的是主键，则删除表
			String sql = "drop table " + ts.getTableName() + " cascade constraints";
			list.add(sql);
		} else {                                               //删除列
			String sql = "alter table " + ts.getTableName() + " drop column " + ts.getTableAttribute();
			list.add(sql);
		}
		return list;
	}
	
	public Result<TableStruct> getTabFieldsByPate(SearchCondition params,
			Page page) {
		String tableName = (String) params.get("tableName");
		return tableDao.findByPage(page, "from TableStruct where tableName=?", tableName);
	}


	public void savFields(EditableGridDataHelper gridData) throws Exception {
		List _s_list = gridData.getSaveObjects();
		List _d_list = gridData.getDelObjects();
		List<TableStruct> ts_s = new ArrayList(_s_list.size());
		List<TableStruct> ts_d = new ArrayList(_d_list.size());
		for (Object o: _s_list) {
			ts_s.add((TableStruct)o);
		}
		for (Object o: _d_list) {
			ts_d.add((TableStruct)o);
		}
		
		if (ts_s.size() != 0) {
			List<String> sqls = new ArrayList(ts_s.size());
			for (int i = 0; i < ts_s.size(); i++) {
				TableStruct ts = ts_s.get(i);
				if (StringUtil.isEmpty(ts.getId())) {
					ts.setId(null);
				}
				if (ts.getId() == null) {
					sqls.addAll(this.addColumnSql(ts));
				} else {
					sqls.addAll(this.alertColumnSql(ts));
				}
			}
			
			Statement sm = tableDao.getSessionObj().connection().createStatement();
			for (int i = 0; i < sqls.size(); i++) {
				sm.addBatch(sqls.get(i));
			}
			sm.executeBatch();
			tableDao.getSessionObj().clear();
			tableDao.saveAll(ts_s);
		}
		
		if (ts_d.size() != 0) {
			List<String> sqls = new ArrayList(ts_d.size());
			List<String> delTableName = new ArrayList();
			for (int i = 0; i < ts_d.size(); i++) {
				TableStruct ts = ts_d.get(i);
				if (StringUtil.isEmpty(ts.getId())) {
					continue;
				}
				if (ts.getTableAttribute().equals(ts.getTableId())) {
					delTableName.add(ts.getTableName());
				}
				sqls.addAll(this.delColumnSql(ts));
			}
			Statement sm = tableDao.getSessionObj().connection().createStatement();
			for (int i = 0; i < sqls.size(); i++) {
				sm.addBatch(sqls.get(i));
			}
			sm.executeBatch();
			tableDao.delAll(ts_d);
			for (String s: delTableName) {
				tableDao.updateByHql("delete from TableStruct where tableName=?", s);
			}
		}
	}
}
