package com.est.workflow.process.service;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.JbpmConfiguration;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.sysmodule.dao.ISysModuleDao;
import com.est.workflow.process.dao.IWfApproveDao;
import com.est.workflow.process.dao.IWfProcessinstanceDao;
import com.est.workflow.process.vo.WfApprove;
import com.est.workflow.process.vo.WfProcessinstance;
import com.est.workflow.processdefination.dao.IWfDfprocessDao;

public class WfApproveServiceImp implements IWfApproveService {
	
	private final Log log = LogFactory.getLog(WfApproveServiceImp.class);
	
	private JbpmConfiguration jbpmConfiguration;
	private IWfDfprocessDao wfDfprocessDao;
	private IWfProcessinstanceDao wfprocessinstanceDao;
	private IWfApproveDao wfApproveDao;
	private ISysModuleDao sysModuleDao;
	
	

	public void setWfprocessinstanceDao(IWfProcessinstanceDao wfprocessinstanceDao) {
		this.wfprocessinstanceDao = wfprocessinstanceDao;
	}

	public void setWfDfprocessDao(IWfDfprocessDao wfDfprocessDao) {
		this.wfDfprocessDao = wfDfprocessDao;
	}
	
	public void setJbpmConfiguration(JbpmConfiguration jbpmConfiguration) {
		this.jbpmConfiguration = jbpmConfiguration;
	}
	
	

	public void setWfApproveDao(IWfApproveDao wfApproveDao) {
		this.wfApproveDao = wfApproveDao;
	}

	public void setSysModuleDao(ISysModuleDao sysModuleDao) {
		this.sysModuleDao = sysModuleDao;
	}

	
	/**
	 *@desc 通过任务实例得到审批信息
	 *@date Oct 16, 2009
	 *@author jingpj
	 *@param taskinstanceId
	 *@return
	 *@see com.est.workflow.process.service.IWfProcessService#getApproveInfoByTaskInstance(java.lang.Long)
	 */
	public WfApprove getApproveInfoByTaskInstance(SearchCondition condition) {
		Long taskinstanceId = (Long) condition.get("taskinstanceId");
		
		
		WfApprove approve = wfApproveDao.findUniqueBy("jbpmtiid", taskinstanceId);
		if (approve == null) {
			approve = new WfApprove();
			approve.setJbpmtiid(taskinstanceId);
			approve.setResult("同意");
			
			
			Long wfProcessInstanceId = (Long) condition.get("wfProcessInstanceId");
			
			approve.setJbpmpiid(wfProcessInstanceId);
			
			WfProcessinstance pi  = wfprocessinstanceDao.findById(wfProcessInstanceId);
			approve.setWfProcessinstance(pi);
			approve.setProcessId(pi.getWfDfprocess().getProcessId());
			approve.setApproveId(0L);
		}
		return approve;
	}

	/**
	 *@desc 保存审批意见 
	 *@date Oct 16, 2009
	 *@author jingpj
	 *@param approve
	 *@see com.est.workflow.process.service.IWfProcessService#savApproveInfo(com.est.workflow.process.vo.WfApprove)
	 */
	public void savApproveInfo(WfApprove approve){
		if(approve.getWfProcessinstance()!=null && (approve.getWfProcessinstance().getProcessinstanceId()==null || approve.getWfProcessinstance().getProcessinstanceId() == 0L))	{
			approve.setWfProcessinstance(null);
		}
		
		TaskInstance ti= jbpmConfiguration.createJbpmContext().getTaskInstance(approve.getJbpmtiid());
		approve.setTaskName(ti.getTask().getName());
		approve.setApprovedate(new Date());
		
		if(approve.getApproveId() == 0L) {
			approve.setApproveId(null);
		}
		wfApproveDao.save(approve);
		
	}

	/**
	 *@desc 得到审批历史记录 
	 *@date Nov 2, 2009
	 *@author jingpj
	 *@param page
	 *@param piid
	 *@return
	 *@see com.est.workflow.process.service.IWfApproveService#getApproveHistoryByPiid(com.est.common.ext.util.Page, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public Result<WfApprove> getApproveHistoryByPiid(Page page, Long piid) {
		if(piid == null || piid == 0L) {
			piid = -1L;
		}
		String hql = "from WfApprove t where t.wfProcessinstance.processinstanceId = ? order by t.approveId";
		Result<WfApprove> result = wfApproveDao.findByPage(page, hql, piid);
		return result;
	}
	
	
	
	

}
