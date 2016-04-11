package com.est.workflow.process.service;

import java.util.List;

import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.sysuser.vo.SysUser;
import com.est.workflow.process.exception.WfTaskIsAlreadyRecievedException;
import com.est.workflow.process.vo.WfSignFieldVO;
import com.est.workflow.processdefination.vo.WfDfprocess;
import com.est.workflow.processdefination.vo.WfDftaskcondition;
import com.est.workflow.processdefination.vo.WfDftaskfield;



@SuppressWarnings("unchecked")
public interface IWfProcessService {

	public ProcessInstance savStartProcess(SearchCondition condition);
	
	public void savSendNext(Token token);
	
	public void savSendNext(TaskInstance taskInstance);
	
	public Result getAssignedTaskList(Page page, SearchCondition condition);

	public Result getRecievedTaskList(Page page, SearchCondition condition);

	public void savRecieveTask(Long taskId, SysUser loginUser) throws WfTaskIsAlreadyRecievedException ;

	public void savEndProcess(Long tiid);
	
	public String getActionClassname(Long moduleId);
	
	public void savReject(Long tiid,Boolean rejectToOldUser);

	public void savSendToNextStep(Long tiid, String type, SysUser user);

	public WfDfprocess getWfDfprocessBytaskDefId(Long taskDefinitionId);

	public List<WfDftaskcondition> getTaskConditions(Long processDefinitionId,String nodeName);
	
	public String[] getActIdsByTaskId(Long taskId);

	public List<WfDftaskfield> getTaskFieldByTaskId(Long taskId);

	public Long getTaskNodeId(Long processId, String nodeName);

	public String getTaskLastTimeActor(Long taskId ,Long piid);
	
	public void savBussinessObjectStatus(Long piid, String status,String callbackMethod) throws Exception ;
	
	public List<WfSignFieldVO> getBussinessObjectSignFields(Long processid);

	public Result getMyDraftProcessList(Page page, SearchCondition condition);
	
	
}
