package com.est.sysinit.syslog.action;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.syslog.service.ISysLogService;
import com.est.sysinit.syslog.vo.SysLog;

public class SysLogAction extends BaseAction{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2228018987967697426L;
	private SysLog sysLog = new SysLog();
	

	@Override
	public Object getModel() {
		return sysLog;
	}
	/**
	 * 
	 *@desc 跳转到修改用户信息的页面 
	 *@date Oct 14, 2009
	 *@author yzw
	 *@return
	 */
	public String fwdSysLog()
	{
		return toJSP("log");
	}
	
	
	public String getModuleList(){
		String operator = req.getParameter("operator"); 		
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String loginname = req.getParameter("loginname");
		
		SearchCondition condition = new SearchCondition();
		condition.set("operator", operator);
		condition.set("startTime", startTime);
		condition.set("endTime", endTime);
		condition.set("loginname", loginname);
		
		Page page = getPage();
		
		ISysLogService sysLogService = (ISysLogService) getBean("sysLogService");
		Result<SysLog> result = null;
		try{
			result = sysLogService.getModuleList(page, condition);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		//log.debug("result========>" + result);
		return toJSON(result);
	}
	
	
	public String getModuleListById(){
		
		String logid = req.getParameter("logid"); 	
		SearchCondition condition = new SearchCondition();
		condition.set("logid", logid);
		Page page = getPage();
		
		ISysLogService sysLogService = (ISysLogService) getBean("sysLogService");
		Result<SysLog> result = null;
		try{
			result = sysLogService.getModuleListById(page, condition);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		//log.debug("result========>" + result);
		return toJSON(result);
	}
}
