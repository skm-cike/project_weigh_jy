package com.est.sysinit.syslog.service;

import java.util.ArrayList;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.syslog.dao.ISysLogDao;
import com.est.sysinit.syslog.vo.SysLog;

/**
 * 
 *@desc 基础信息配置service实现
 *@author yzw
 *@date Jun 7, 2010
 *@path com.est.fuel.base.service.FuelBaseconfigServiceImp
 *@corporation Enstrong S&T
 */
public class SysLogServiceImp implements ISysLogService {
	
	private ISysLogDao sysLogDao;
	

	public void setSysLogDao(ISysLogDao sysLogDao) {
		this.sysLogDao = sysLogDao;
	}


	public Result<SysLog> getModuleList(Page page, SearchCondition condition) {
		
		
		String operator = (String) condition.get("operator");
		String loginname = (String) condition.get("loginname");
		
		String startTime = "";
		String endTime = "";
		
		if(!StringUtil.isEmpty((String)condition.get("startTime")))
			startTime = (String) condition.get("startTime")+ " 00:00";
		
		if(!StringUtil.isEmpty((String)condition.get("endTime")))
			endTime = (String) condition.get("endTime")+" 23:59";
		
		
		
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from SysLog t where 1=1");
		List params = new ArrayList();
		if (!StringUtil.isEmpty(startTime)) {
			hql.append(" and to_char(t.operateTime,'yyyy-mm-dd hh24:mi')>=?");
			params.add(startTime);
		}
		
		if (!StringUtil.isEmpty(endTime)) {
			hql.append(" and to_char(t.operateTime,'yyyy-mm-dd hh24:mi')<=?");
			params.add(endTime);
		}
		
		if (!StringUtil.isEmpty(operator)) {
			hql.append(" and t.operator like ?");
			params.add("%"+operator+"%");
		}
		
		if (!StringUtil.isEmpty(loginname)) {
			hql.append(" and t.loginname like ?");
			params.add("%"+loginname+"%");
		}
		
		hql.append(" order by t.operateTime desc");
		
		return sysLogDao.findByPage(page, hql.toString(), params.toArray());
	}

	
	public Result<SysLog> getModuleListById(Page page, SearchCondition condition){
		String logid = (String) condition.get("logid");
		
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from SysLog t where id=?");
		List params = new ArrayList();
		
		if (!StringUtil.isEmpty(logid)) 
			params.add(Long.parseLong(logid));
		
		hql.append(" order by t.operateTime desc");
		
		
		String sql = hql.toString();
		
		if (StringUtil.isEmpty(logid))
			sql = "from SysLog t where 1=2";
		
		return sysLogDao.findByPage(page, sql, params.toArray());
	}

	public void saveSysLog(SysLog sysLog) {

		sysLogDao.save(sysLog);
		
	}

}
