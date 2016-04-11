package com.est.workflow.process.action;

import java.util.List;

import org.jbpm.graph.exe.ProcessInstance;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysauthority.service.ISysUserModuleService;
import com.est.sysinit.sysuser.vo.SysUser;
import com.est.workflow.process.exception.WfTaskIsAlreadyRecievedException;
import com.est.workflow.process.service.IWfProcessService;
import com.est.workflow.processdefination.vo.WfDfprocess;
import com.est.workflow.processdefination.vo.WfDftaskfield;

/**
 * 
 *@desc 工作流流程定义
 *@author jingpj
 *@date Oct 12, 2009
 *@path com.est.workflow.processdefination.action.WfProcessDefinationAction
 *@corporation Enstrong S&T
 */
public class WfProcessAction extends BaseAction {
	private static final long serialVersionUID = -5236565446298628901L;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 发起流程
	 *@desc 
	 *@date Oct 15, 2009
	 *@author jingpj
	 *@return
	 */
	public String startProcess() {
		try{
			IWfProcessService wfProcessService = (IWfProcessService) getBean("wfProcessService");
			ProcessInstance processInstance = null;
			Long processdefinationId = StringUtil.parseLong(req.getParameter("processdefinationId"));
			Long moduleId = StringUtil.parseLong(req.getParameter("moduleid"));
			Long masterId = StringUtil.parseLong(req.getParameter("masterid"));
			String topic = req.getParameter("topic");
			
			SearchCondition condition = new SearchCondition();
			condition.set("processdefinationId", processdefinationId);
			condition.set("topic", topic);
			condition.set("draftby", getCurrentUser().getUsername());
			condition.set("moduleId", moduleId);
			condition.set("masterId", masterId);
			
			try{
				//processInstance = wfProcessService.savNewProcess(new Long(518),"the baby process",100L,1L,"测试流程",1L);
				processInstance = wfProcessService.savStartProcess(condition);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			/*
			Token token = processInstance.getRootToken();
			token.signal();
			
			Collection<TaskInstance> tasks = token.getProcessInstance().getTaskMgmtInstance().getUnfinishedTasks(token);
			while(tasks.size() > 0) {
				TaskInstance ti = (TaskInstance) tasks.iterator().next();
				Set a = ti.getPooledActors();
				
				System.out.println("=========================>actors:");
				Iterator it = a.iterator();
				while(it.hasNext()) {
					System.out.println(it.next());
				}
				System.out.println("=========================>actors end");
				wfProcessService.savSendNext(ti);
				tasks = token.getProcessInstance().getTaskMgmtInstance().getUnfinishedTasks(token);
			}
			*/
			
			/*
			while(!"end".equals(token.getNode().getName())) {
				wfProcessService.savSendNext(token);
			}
			*/
			
			return toSTR("{success:true}");
		}catch(Exception ex) {
			ex.printStackTrace();
			return toSTR("{success:false}");
		}
		
	}
	
	
	/**
	 *@desc 得到已分配(待接收)任务列表
	 *@date Oct 15, 2009
	 *@author jingpj
	 *@return
	 */
	public String getAssignedTaskList() {
		
		SearchCondition condition = new SearchCondition();
		condition.set("topic", req.getParameter("topic"));
		condition.set("draftby", req.getParameter("draftby"));
		condition.set("draftdatestart", req.getParameter("draftdatestart"));
		condition.set("draftdateend", req.getParameter("draftdateend"));
		
		condition.set("loginUser", getCurrentUser());
		
		IWfProcessService wfProcessService = (IWfProcessService) getBean("wfProcessService");
		Result result = wfProcessService.getAssignedTaskList(getPage(), condition);
		
		return toJSON(result);
	}
	
	/**
	 *@desc 得到已接收任务列表
	 *@date Oct 15, 2009
	 *@author jingpj
	 *@return
	 */
	public String getRecievedTaskList() {
		SearchCondition condition = new SearchCondition();
		condition.set("topic", req.getParameter("topic"));
		condition.set("draftby", req.getParameter("draftby"));
		condition.set("draftdatestart", req.getParameter("draftdatestart"));
		condition.set("draftdateend", req.getParameter("draftdateend"));
		
		condition.set("loginUser", getCurrentUser());
		
		IWfProcessService wfProcessService = (IWfProcessService) getBean("wfProcessService");
		Result result = wfProcessService.getRecievedTaskList(getPage(),condition);
		
		return toJSON(result);
	}	
	
	/**
	 *@desc 得到我发起的流程列表
	 *@date Nov 24, 2009
	 *@author jingpj
	 *@return
	 */
	public String getMyDraftProcessList(){
		SearchCondition condition = new SearchCondition();
		condition.set("loginUser", getCurrentUser());
		condition.set("topic",req.getParameter("topic"));
		condition.set("status",req.getParameter("status"));
		condition.set("draftdatestart",req.getParameter("draftdatestart"));
		condition.set("draftdateend",req.getParameter("draftdateend"));
		
		IWfProcessService wfProcessService = (IWfProcessService) getBean("wfProcessService");
		Result result = wfProcessService.getMyDraftProcessList(getPage(),condition);
		
		return toJSON(result);
	}
	
	

	/**
	 *@desc 接收任务
	 *@date Oct 15, 2009
	 *@author jingpj
	 *@return
	 */
	public String recieveTask() {
		SysUser loginUser = this.getCurrentUser();
		Long taskId = StringUtil.parseLong(req.getParameter("taskid"));
		IWfProcessService wfProcessService = (IWfProcessService) getBean("wfProcessService");
		try{
			wfProcessService.savRecieveTask(taskId,loginUser);
			return toSTR("{success:true}");
		} catch(WfTaskIsAlreadyRecievedException ex) {
			return toERROR(ex.getMessage());
		} catch(Exception ex) {
			return toERROR(ex.getMessage());
		}
	}	

	
	/**
	 * 
	 *@desc 得到流程处理的url
	 *@date Oct 20, 2009
	 *@author jingpj
	 *@return
	 */
	public String getHandleUrl(){
		
		Long taskId = StringUtil.parseLong(req.getParameter("taskid"));
		Long taskDefinitionId = StringUtil.parseLong(req.getParameter("taskDefinitionId"));
		Long masterId = StringUtil.parseLong(req.getParameter("masterId"));
		Long wfProcessInstanceId = StringUtil.parseLong(req.getParameter("wfProcessInstanceId"));
		
		IWfProcessService wfProcessService = (IWfProcessService) getBean("wfProcessService");
		
		try {
			WfDfprocess wfDfprocess = wfProcessService.getWfDfprocessBytaskDefId(taskDefinitionId);
			Long moduleId = wfDfprocess.getModuleId();
			ISysUserModuleService service = (ISysUserModuleService) getBean("userModuleService");
			
			SearchCondition condition = new SearchCondition();
			condition.set("taskId", taskId );
			condition.set("taskDefinitionId", taskDefinitionId);
			condition.set("masterId", masterId);
			condition.set("userId", getCurrentUser().getUserid());
			condition.set("moduleId", moduleId);
			condition.set("wfProcessInstanceId", wfProcessInstanceId);
			
			String url = service.getWfHandleUrl(condition);
			return toSTR("{success:true,url:'" + url + "'}");
		} catch (Exception e) {
			return toERROR("发生错误！");
		}
		
	}
	
	
	/**
	 *@desc 提交到下一个流程
	 *@date Oct 15, 2009
	 *@author jingpj
	 *@return
	 */
	public String sendToNextStep() {
		Long tiid = StringUtil.parseLong(req.getParameter("tiid"));
		String type = req.getParameter("type");
		if(tiid == null || tiid == 0L) {
			return toERROR("请选择一条流程中的记录！");
		} else {
			IWfProcessService wfProcessService = (IWfProcessService) getBean("wfProcessService");
			wfProcessService.savSendToNextStep(tiid,type,getCurrentUser());
		}
		return null;
	}
	
	/**
	 *@desc 驳回
	 *@date Oct 15, 2009
	 *@author jingpj
	 *@return
	 */
	public String rejectTask() {
		Long taskId = StringUtil.parseLong(req.getParameter("tiid"));
		String toOldActor = req.getParameter("toOldActor");
		Boolean rejectToOldUser = false;
		if("T".equals(toOldActor)) {
			rejectToOldUser = true;
		}
		IWfProcessService wfProcessService = (IWfProcessService) getBean("wfProcessService");
		try{
			wfProcessService.savReject(taskId,rejectToOldUser);
			return toSTR("{success:true}");
		} catch(Exception ex) {
			return toERROR(ex.getMessage());
		}
	}
	
	
	/**
	 *@desc 强制终止流程
	 *@date Oct 15, 2009
	 *@author jingpj
	 *@return
	 */
	public String endProcess() {
		Long tiid = StringUtil.parseLong(req.getParameter("tiid"));
		if(tiid == null || tiid == 0L) {
			return toERROR("请选择一条流程中的记录！");
		} else {
			try{
				IWfProcessService wfProcessService = (IWfProcessService) getBean("wfProcessService");
				wfProcessService.savEndProcess(tiid);
				return toSTR("{success:true}");
			} catch(IllegalStateException ex){
				ex.printStackTrace();
				return toERROR("该流程已经被关闭，不能重复关闭");
			}catch(Exception ex) {
				ex.printStackTrace();
				return toSTR("{success:false}");
			}
		}
	}
	
	
	/**
	 * 得到任务节点id
	 *@desc 
	 *@date Nov 2, 2009
	 *@author jingpj
	 *@return
	 */
	public String getTaskNodeId(){
		Long processId = StringUtil.parseLong(req.getParameter("processId"));
		String nodeName = req.getParameter("nodeName");
		IWfProcessService wfProcessService = (IWfProcessService) getBean("wfProcessService");
		Long nodeId = wfProcessService.getTaskNodeId(processId,nodeName);
		return toSTR("{success:true,nodeId:"+nodeId+"}");
	}
	
	/**
	 * @desc 修改业务表对象的签字列
	 * @date Oct 27, 2009
	 * @author jingpj
	 */
	public String getBussinessObjectSignFields(){
		Long processId = StringUtil.parseLong(req.getParameter("processId"));
		if(processId == 0) {
			return toSTR("{success:true,rows:[]}");
		} else {
			try{
				IWfProcessService wfProcessService = (IWfProcessService) getBean("wfProcessService");
				List lst = wfProcessService.getBussinessObjectSignFields(processId);
				if(lst.size()>0) {
					return toJSON(lst ,"{success:true,rows:","}");
				} else {
					return toSTR("{success:true,rows:[]}");
				}
			} catch (Exception ex) {
				return toSTR("{success:true,rows:[]}");
			}
		}
	}
	
	/**
	 *@desc 得到设置的字段属性
	 *@date Feb 7, 2010
	 *@author jingpj
	 *@return
	 */
	public String getTaskFieldSetting(){
		Long tiid = StringUtil.parseLong(req.getParameter("tiid"));
		if(tiid == null || tiid == 0L) {
			return toERROR("请选择一条流程中的记录！");
		} else {
			try{
				IWfProcessService wfProcessService = (IWfProcessService) getBean("wfProcessService");
				List<WfDftaskfield> fields = wfProcessService.getTaskFieldByTaskId(tiid);
				return toJSON(fields,"{success:true,data:","}");
			} catch(Exception ex) {
				ex.printStackTrace();
				return toSTR("{success:false}");
			}
		}
	}
	
	
	public String fwdMyTask(){
		return toJSP("mytask");
	}
	
	
	
	
}
