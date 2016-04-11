package com.est.workflow.process.service;

import java.util.Collection;
import java.util.Iterator;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

/**
 * @corporation Enstrong S&T
 * @author Administrator
 * @date 2007-11-12
 * @path com.enstrong.mis.workflow.engine.service
 * @description ��ǩ�������ʱ����
 */
public class TaskEndCountersignHandler implements ActionHandler {


	private IWfProcessService wfProcessService;
	
	public void setWfProcessService(IWfProcessService wfProcessService) {
		this.wfProcessService = wfProcessService;
	}

	@SuppressWarnings("unchecked")
	public void execute(ExecutionContext executionContext) throws Exception {

		TaskInstance ti = executionContext.getTaskInstance();

		boolean isDisapprove = false;
		if (ti.getVariableLocally("disapprove") != null) {
			isDisapprove = (Boolean) ti.getVariableLocally("disapprove");
		}
		
		if (isDisapprove) {
			String notapprovetransitionname = (String) ti.getVariableLocally("notapprovetransitionname");
			ProcessDefinition processDefinition = ti.getContextInstance().getProcessInstance().getProcessDefinition();
			
			Transition transition = new Transition(notapprovetransitionname);

			Node toNode = processDefinition.getNode(notapprovetransitionname);
			transition.setTo(toNode);
			transition.setProcessDefinition(processDefinition);

			Node fromNode = ti.getToken().getNode();
			transition.setFrom(fromNode);
			fromNode.addLeavingTransition(transition);
			
			TaskMgmtInstance tmi = executionContext.getTaskMgmtInstance();
			final String actorId = ti.getActorId();
			Collection<TaskInstance> c = tmi.getSignallingTasks(executionContext);
			for (Iterator<TaskInstance> it = c.iterator(); it.hasNext();) {
				TaskInstance task = (TaskInstance) it.next();
				if (!(actorId.equals(task.getActorId())) && (!task.hasEnded())) {
					//task.setDescription(Constants.TASK_STATUS_FJCX);
					task.end(notapprovetransitionname);
					//task.end();
				}
			}
			 ti.setVariableLocally("disapprove",false);
		}

	}


}
