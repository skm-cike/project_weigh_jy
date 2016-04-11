package com.est.workflow.process.vo;

public class BaseWfVo {
	protected String wfstatus;
	protected Long wfpiid;
	

	public String getWfstatus(){
		return this.wfstatus;
	}
	
	public void setWfstatus(String wfstatus) {
		this.wfstatus = wfstatus;
	}
	
	public Long getWfpiid(){
		return this.wfpiid;
	}
	
	public void setWfpiid(Long wfpiid) {
		this.wfpiid = wfpiid;
	}
	
	
}
