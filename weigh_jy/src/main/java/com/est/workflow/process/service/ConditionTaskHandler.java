package com.est.workflow.process.service;

import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

import com.est.workflow.process.annotation.WFBusinessClassAnnotation;
import com.est.workflow.process.dao.IWfProcessinstanceDao;
import com.est.workflow.process.vo.WfProcessinstance;
import com.est.workflow.processdefination.dao.IWfDfprocessDao;
import com.est.workflow.processdefination.vo.WfDfprocess;
import com.est.workflow.processdefination.vo.WfDftaskcondition;

/**
 *@desc 创建会签任务handler
 *@author jingpj
 *@date Oct 30, 2009
 *@path com.est.workflow.process.service.CreateTaskInstanceCountersignHandler
 *@corporation Enstrong S&T
 */
public class ConditionTaskHandler implements ActionHandler {


	private IWfProcessService wfProcessService;
	
	private IWfDfprocessDao wfDfprocessDao;
	
	private IWfProcessinstanceDao wfprocessinstanceDao;
	
	
	public void setWfprocessinstanceDao(IWfProcessinstanceDao wfprocessinstanceDao) {
		this.wfprocessinstanceDao = wfprocessinstanceDao;
	}

	public void setWfProcessService(IWfProcessService wfProcessService) {
		this.wfProcessService = wfProcessService;
	}
	
	public void setWfDfprocessDao(IWfDfprocessDao wfDfprocessDao) {
		this.wfDfprocessDao = wfDfprocessDao;
	}



	public void execute(ExecutionContext executionContext) throws Exception {
		savConditionTransition(executionContext);
	}
	
	@SuppressWarnings("unchecked")
	private void savConditionTransition(ExecutionContext executionContext) throws ClassNotFoundException{
		Token token = executionContext.getToken();
		TaskMgmtInstance tmi = executionContext.getTaskMgmtInstance();
		TaskInstance ti = executionContext.getTaskInstance();
		
		ProcessDefinition processDefinition = executionContext.getProcessDefinition();
		Long processDefinitionId = processDefinition.getId();
		Long processId = executionContext.getProcessInstance().getId();
		WfProcessinstance wfProcessInstance = wfprocessinstanceDao.findUniqueBy("processinstanceId", processId);
		Long masterId = wfProcessInstance.getMasterid();
		
		TaskNode taskNode = (TaskNode) executionContext.getNode();
		
		
		
		String nodeName = taskNode.getName();
		
		
		List<WfDftaskcondition> taskConditions = wfProcessService.getTaskConditions(processDefinitionId,nodeName);
		
		WfDfprocess wfDfprocess  = wfDfprocessDao.findById(processDefinitionId);
		String classPath = wfProcessService.getActionClassname(wfDfprocess.getModuleId());
		Class clazz = null;
		try {
			clazz = Class.forName(classPath);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		if (clazz.isAnnotationPresent(WFBusinessClassAnnotation.class)) {
			WFBusinessClassAnnotation wFBusinessClassAnnotation = (WFBusinessClassAnnotation) clazz
					.getAnnotation(WFBusinessClassAnnotation.class);
			String wfBusinessClassString = wFBusinessClassAnnotation
					.value();
			Class voClass = Class.forName(wfBusinessClassString);
			String idFieldName = wfDfprocessDao.getIdName(voClass);
			for(WfDftaskcondition condition : taskConditions) {
				
				String conditionExpression  = condition.getConditionexpression();
				String hql = "select count(*) from " + wfBusinessClassString + " where " + conditionExpression + " and "+idFieldName+"="+ masterId;
				Long cnt = (Long) wfDfprocessDao.findUniqueByHql(hql);
				if(cnt > 0) {
					//ti.end(condition.getTransitionname());
					Node fromNode = token.getNode();
					String transitionname = condition.getTransitionname();
					
					if(!fromNode.hasLeavingTransition(transitionname)) {
						Transition transition = new Transition(transitionname);
						Node toNode = processDefinition.getNode(transitionname);
						transition.setTo(toNode);
						transition.setProcessDefinition(processDefinition);
						transition.setFrom(fromNode);
						fromNode.addLeavingTransition(transition);
					}
					
					token.unlock();
					token.signal(transitionname);
					return;
				}
			}
		}
		//ti.end();
		token.unlock();
		token.signal();
		
		
		
		
		
	}

	

}