package com.est.sysinit.syslog.vo;

import java.util.Date;

public class SysLog {

	private Long id;
	private String operator;
	private String operate;
	private String operateresult;
	private String loginname;
	private Date operateTime;
	
	private String details;
	
	public SysLog() {
		
	}
	
	public SysLog(String operator,String operate,Date operateTime,String loginname,String operateresult) {
		
		this.setOperate(operate);
		this.setOperator(operator);
		this.setOperateTime(operateTime);
		this.setLoginname(loginname);
		this.setOperateresult(operateresult);
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getOperateresult() {
		return operateresult;
	}

	public void setOperateresult(String operateresult) {
		this.operateresult = operateresult;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	

}
