package com.est.workflow.process.service;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Stack;

import org.jbpm.bytes.ByteArray;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

import com.est.common.exception.BaseBussinessException;
import com.est.workflow.process.config.WorkFlowConfig;
import com.est.workflow.processdefination.dao.IWfDftaskDao;
import com.est.workflow.processdefination.vo.WfDftask;

/**
 *@desc 创建会签任务handler
 *@author jingpj
 *@date Oct 30, 2009
 *@path com.est.workflow.process.service.CreateTaskInstanceCountersignHandler
 *@corporation Enstrong S&T
 */
@SuppressWarnings("serial")
public class CreateTaskInstanceCountersignHandler implements ActionHandler {


	private IWfProcessService wfProcessService;
	
	private IWfDftaskDao wfDftaskDao;
	
	public void setWfProcessService(IWfProcessService wfProcessService) {
		this.wfProcessService = wfProcessService;
	}
	
	public void setWfDftaskDao(IWfDftaskDao wfDftaskDao) {
		this.wfDftaskDao = wfDftaskDao;
	}

	public void execute(ExecutionContext executionContext) throws Exception {
		savCreateTaskInstanceCountersign(executionContext);
	}
	
	@SuppressWarnings("unchecked")
	private void savCreateTaskInstanceCountersign(ExecutionContext executionContext){
		
		Long nodeId = executionContext.getNode().getId();
		Long piid = executionContext.getProcessInstance().getId();
		
		WfDftask dfTask = wfDftaskDao.findById(nodeId);

		String taskName = dfTask.getName();
		
		try {
			wfProcessService.savBussinessObjectStatus(piid, "流程中-"+taskName,WorkFlowConfig._CALLBACK_BEFORENODEENTRY);
		} catch (Exception e1) {
			throw new BaseBussinessException("保存流程状态失败！");
		}
		
		Token token = executionContext.getToken();
		TaskMgmtInstance tmi = executionContext.getTaskMgmtInstance();
		TaskNode taskNode = (TaskNode) executionContext.getNode();
		Task task = taskNode.getTask(taskNode.getName());
		//String[] s = wfProcessService.getCountersignActorsByJbpmTask(task.getId());
		//String[] s = {"1","2"};
		String[] actors = wfProcessService.getActIdsByTaskId(taskNode.getId());
		for (String i : actors) {
			TaskInstance ti = tmi.createTaskInstance(task, token);
			ti.setActorId(i);
			//ti.setDescription(Constants.TASK_STATUS_DCL);
		}
		
		/* 添加流程步骤历史记录*/
		System.out.println(executionContext.getContextInstance().getVariable("historyNodes").getClass().getName());
		
		
		Stack<String> historyNodes =  null; 
		if (executionContext.getContextInstance().getVariable("historyNodes") instanceof java.util.Stack) {
			historyNodes = (Stack<String>) executionContext.getContextInstance().getVariable("historyNodes");
		} else {
			ByteArray byteArr = (ByteArray)executionContext.getContextInstance().getVariable("historyNodes");
			if(byteArr == null) {
				historyNodes = new java.util.Stack<String>();
			} else {
				ObjectInputStream ois;
				try {
					ois = new ObjectInputStream(new ByteArrayInputStream(byteArr.getBytes()));
					historyNodes =  (Stack<String>) ois.readObject(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		historyNodes.push(executionContext.getNode().getName());
		executionContext.getContextInstance().setVariable("historyNodes", historyNodes);
	}

	

}