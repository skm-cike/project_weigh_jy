package com.est.workflow.processdefination.action;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.workflow.processdefination.service.IWfDfprocessService;
import com.est.workflow.processdefination.vo.WfDfprocess;
import com.est.workflow.processdefination.vo.WfDftask;

/**
 * 
 *@desc 工作流流程定义
 *@author jingpj
 *@date Oct 12, 2009
 *@path com.est.workflow.processdefination.action.WfProcessDefinationAction
 *@corporation Enstrong S&T
 */
public class WfProcessDefinationAction extends BaseAction {
	private static final long serialVersionUID = -5236565446298628901L;
	
	WfDftask wfDftask=new WfDftask();
	
	@Override
	public Object getModel() {
		return wfDftask;
	}

	/**
	 *@desc 得到模块下的定义的所有流程
	 *@date Oct 22, 2009
	 *@author jingpj
	 *@return
	 */
	public String getProcessDefinationListByModuleId(){
		try{
			//模块ID
			Long moduleId  = StringUtil.parseLong(req.getParameter("moduleId"));
			//流程名称
			String processname=req.getParameter("processname");
			
			SearchCondition condition=new SearchCondition();
			condition.set("moduleId", moduleId);
			condition.set("processname", processname);
			
			IWfDfprocessService service = (IWfDfprocessService) getBean("wfProcessDefinationService");
			Result<WfDfprocess> result = service.getProcessDefinationListByPage(condition,getPage());
			
			if(result!=null && result.getContent()!=null && result.getContent().size()>0) {
				return toJSON(result);
			} else {
				return toSTR("{success:true,rows:[]}");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return toSTR("{success:true,rows:[]}");
		}
	}
	/**
	 * 
	 *@desc 设置模块流程是否有效
	 *@date Nov 4, 2009
	 *@author hebo
	 *@return
	 */
	public String setProcessValid(){
		try{
			
			SearchCondition condition=new SearchCondition();
			//流程定义ID
			condition.set("processId", StringUtil.parseLong(req.getParameter("processId")));
			//是否有效 F标示无效，T标示有效
			condition.set("isvalid", req.getParameter("isvalid"));
			
			IWfDfprocessService service = (IWfDfprocessService) getBean("wfProcessDefinationService");
			service.savProcessValid(condition);
			
			return toSTR("{success:true}");
			
		}catch(Exception e){
			e.printStackTrace();
			return toSTR("{success:false}");
		}
		
	}
	
	/**
	 * 
	 *@desc 保存任务信息
	 *@date Nov 5, 2009
	 *@author hebo
	 *@return
	 */
	public String savWfDftask(){
		try{
			IWfDfprocessService service = (IWfDfprocessService) getBean("wfProcessDefinationService");
			service.savWfDftask(wfDftask);
			
			if(wfDftask!=null){
				return toJSON(wfDftask,"{success:true,data:", "}");
			}else{
				return toJSON("{success:true,data:{}}");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return toSTR("{success:false}");
		}
	}
	
	/**
	 * 
	 *@desc 获取任务详细信息
	 *@date Nov 5, 2009
	 *@author hebo
	 *@return
	 */
	public String getWfDftask(){
		
		if(wfDftask!=null && wfDftask.getTaskId()!=null){
			
			IWfDfprocessService service = (IWfDfprocessService) getBean("wfProcessDefinationService");
			wfDftask=service.getWfDftaskById(wfDftask.getTaskId());
			if(wfDftask!=null){
				return toJSON(wfDftask,"{success:true,data:", "}");
			}else{
				return toJSON("{success:true,data:{}}");
			}
			
		}else{
			return toJSON("{success:true,data:{}}");
		}
	}
	
	/**
	 * 
	 *@desc 删除任务信息
	 *@date Nov 5, 2009
	 *@author hebo
	 *@return
	 */
	public String delWfDftask(){
		try{
			if(wfDftask.getTaskId()!=null){
				
				IWfDfprocessService service = (IWfDfprocessService) getBean("wfProcessDefinationService");
				service.delWfDftaskById(wfDftask.getTaskId());
				
				return toSTR("{success:true}");
				
			}else{
				return toJSON("{success:false}");
			}
		}catch(Exception e){
			e.printStackTrace();
			return toSTR("{success:false}");
		}
	}
	
	/**
	 *@desc 读取流程定义json
	 *@date Nov 5, 2009
	 *@author jingpj
	 *@return
	 */
	public String  getProcessJson() {
		//流程定义ID
		Long processId = StringUtil.parseLong(req.getParameter("processId"));
		
		IWfDfprocessService service = (IWfDfprocessService) getBean("wfProcessDefinationService");
		String processJson = service.getProcessJson(processId);
		
		if(processJson == null) {
			return toERROR("读取错误！");
		} else {
			return toSTR("{success:true,processJson:'"+processJson+"'}");
		}
		
	}
	

	/**
	 * 转向到页头
	 * 
	 * @return
	 */
	public String fwdWorkflowDesigner() {
		return toJSP("workflowdesigner");
	}

}
