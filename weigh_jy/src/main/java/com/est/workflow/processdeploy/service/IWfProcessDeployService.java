package com.est.workflow.processdeploy.service;

import com.est.sysinit.sysuser.vo.SysUser;
import com.est.workflow.processdefination.vo.WfDfprocess;

public interface IWfProcessDeployService {
	public void savDeploy(WfDfprocess wfDfprocess,SysUser user);
}
