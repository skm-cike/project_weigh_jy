package com.est.workflow.process.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Stack;

import org.jbpm.bytes.ByteArray;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.exe.Assignable;

import com.est.common.exception.BaseBussinessException;
import com.est.workflow.process.config.WorkFlowConfig;
import com.est.workflow.processdefination.dao.IWfDftaskDao;
import com.est.workflow.processdefination.vo.WfDftask;

/**
 *@desc 任务分派用户
 *@author jingpj
 *@date Oct 14, 2009
 *@path com.est.workflow.process.service.AssignmentHandlerServiceImp
 *@corporation Enstrong S&T
 */
@SuppressWarnings("serial")
public class AssignmentHandlerServiceImp implements IAssignmentHandlerService {

	private IWfProcessService wfProcessService;
	
	private IWfDftaskDao wfDftaskDao;
	
	public void setWfProcessService(IWfProcessService wfProcessService) {
		this.wfProcessService = wfProcessService;
	}
	
	
	
	public void setWfDftaskDao(IWfDftaskDao wfDftaskDao) {
		this.wfDftaskDao = wfDftaskDao;
	}



	public void assign(Assignable assignable, ExecutionContext context) throws Exception {
		this.savAssign(assignable, context);
	}
	
	@SuppressWarnings("unchecked")
	private void savAssign(Assignable assignable, ExecutionContext context){
		Long nodeId = context.getNode().getId();
		Long piid = context.getProcessInstance().getId();
		Long taskId = context.getTask().getId();
		
		
		WfDftask dfTask = wfDftaskDao.findById(nodeId);

		String taskName = dfTask.getName();
		
		try {
			wfProcessService.savBussinessObjectStatus(piid, "流程中-"+taskName,WorkFlowConfig._CALLBACK_BEFORENODEENTRY);
		} catch (Exception e1) {
			throw new BaseBussinessException("保存流程状态失败！");
		}
		
		/* 添加流程步骤历史记录*/
		
		Stack<String> historyNodes =  null; 
		
		Object tmp = context.getContextInstance().getVariable("historyNodes");
		
		if(tmp instanceof java.util.Stack){
			historyNodes = (Stack<String>)tmp;
		} else {
			ByteArray byteArr = (ByteArray)tmp;
			if(byteArr == null) {
				historyNodes = new java.util.Stack<String>();
			} else {
				ObjectInputStream ois;
				try {
					ois = new ObjectInputStream(new ByteArrayInputStream(byteArr.getBytes()));
					historyNodes =  (Stack<String>) ois.readObject();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				 
			}
		}
		
			historyNodes.push(context.getNode().getName());
			
			context.getTaskInstance().setVariable("historyNodes", historyNodes);
			
			Boolean rejectToOldUser = (Boolean) context.getContextInstance().getVariable("rejectToOldUser");
			if(rejectToOldUser==null || !rejectToOldUser) {
				//重新分配角色
				String[] actors = wfProcessService.getActIdsByTaskId(nodeId);
				assignable.setPooledActors(actors);
			} else {
				//回退到原处理者
				String actorId = wfProcessService.getTaskLastTimeActor(taskId,piid);
				assignable.setActorId(actorId);
			}
			
			
			
	}
	


	

}