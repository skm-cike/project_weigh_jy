package com.est.workflow.processdeploy.service;

import java.util.Date;
import java.util.List;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;

import com.est.sysinit.sysuser.vo.SysUser;
import com.est.workflow.processdefination.dao.IWfDfprocessDao;
import com.est.workflow.processdefination.vo.WfDfprocess;
import com.est.workflow.processdefination.vo.WfDftask;

public class WfProcessDeployServiceImp implements IWfProcessDeployService {
	
	JbpmConfiguration jbpmConfiguration;
	private IWfDfprocessDao wfDfprocessDao;
	
	
	public void setWfDfprocessDao(IWfDfprocessDao wfDfprocessDao) {
		this.wfDfprocessDao = wfDfprocessDao;
	}


	public void setJbpmConfiguration(JbpmConfiguration jbpmConfiguration) {
		this.jbpmConfiguration = jbpmConfiguration;
	}


	/**
	 * 
	 *@desc 部署流程
	 *@date Oct 12, 2009
	 *@author jingpj
	 *@param jbpmContext
	 *@param processXml
	 *@see com.est.workflow.processdeploy.service.IWfProcessDeployService#savDeploy(org.jbpm.JbpmContext, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public void savDeploy(WfDfprocess wfDfprocess,SysUser user) {
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		ProcessDefinition processDefination = ProcessDefinition.parseXmlString(wfDfprocess.getDfxml());
		jbpmContext.deployProcessDefinition(processDefination);
		wfDfprocess.setJbpmpid(processDefination.getId());
		wfDfprocess.setProcessversion(""+processDefination.getVersion());
		wfDfprocess.setProcessname(processDefination.getName());
		wfDfprocess.setProcessId(processDefination.getId());
		wfDfprocess.setDeployDate(new Date());
		wfDfprocess.setDeployer(user.getUsername());
		wfDfprocessDao.save(wfDfprocess);
		
		List<Node> nodeList = processDefination.getNodes();
		for(Node node : nodeList) {
			WfDftask wfDftask = new WfDftask();
			wfDftask.setTaskId(node.getId());
			wfDftask.setJbpmtid(node.getId());
			wfDftask.setWfDfprocess(wfDfprocess);
			wfDftask.setName(node.getName());
			//task.getTaskNode().getId()
			wfDfprocessDao.save(wfDftask);
		}
		
//		
//		Map<String,Task> taskmap = processDefination.getTaskMgmtDefinition().getTasks();
//		Iterator<String> it = taskmap.keySet().iterator();
//		while(it.hasNext()) {
//			Task task = taskmap.get(it.next());
//			WfDftask wfDftask = new WfDftask();
//			wfDftask.setTaskId(task.getId());
//			wfDftask.setJbpmtid(task.getId());
//			wfDftask.setWfDfprocess(wfDfprocess);
//			wfDftask.setName(task.getName());
//			//task.getTaskNode().getId()
//			wfDfprocessDao.save(wfDftask);
//		}
		
	}

}
