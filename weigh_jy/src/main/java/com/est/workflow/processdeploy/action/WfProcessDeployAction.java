package com.est.workflow.processdeploy.action;

import com.est.common.base.BaseAction;
import com.est.workflow.processdefination.vo.WfDfprocess;
import com.est.workflow.processdeploy.service.IWfProcessDeployService;

/**
 * 
 *@desc 工作流流程定义
 *@author jingpj
 *@date Oct 12, 2009
 *@path com.est.workflow.processdefination.action.WfProcessDefinationAction
 *@corporation Enstrong S&T
 */
public class WfProcessDeployAction extends BaseAction {
	private static final long serialVersionUID = -5236565446298628901L;

	WfDfprocess wfDfprocess  = new WfDfprocess();
	
	
	public void setWfDfprocess(WfDfprocess wfDfprocess) {
		this.wfDfprocess = wfDfprocess;
	}


	@Override
	public Object getModel() {
		return wfDfprocess;
	}

	
	public String deployProess() {
		
		/*
		if(jbpmConfiguration == null) {
			WebApplicationContext webApplicationContext = (WebApplicationContext) ((HttpServletRequest) req)
	        .getSession()
	        .getServletContext()
	        .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	
			jbpmConfiguration = (JbpmConfiguration) webApplicationContext.getBean("jbpmConfiguration");
		}
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		*/
		
		/*
		String processXml = "<process-definition name='sss'>" +
	      "  <start-state>" +
	      "    <transition to='s' />" +
	      "  </start-state>" +
	      "  <state name='s'>" +
	      "    <transition to='end' />" +
	      "  </state>" +
	      "  <end-state name='end' />" +
	      "</process-definition>";
		*/
		
		
/*		String processXml =  "<process-definition name='the baby process'>" +
	      "  <start-state>" +
	      "    <transition name='baby cries' to='t1' />" +
	      "  </start-state>" +
	      "  <task-node name='t1'>" +
	      "    <task name='t1'>" +
	      "     <assignment  config-type='bean'  class='org.springmodules.workflow.jbpm31.JbpmHandlerProxy'> <targetBean>assignmentHandlerService</targetBean> </assignment>" +
	      "    </task>" +
	      "    <transition to='t2' />" +
	      "  </task-node>" +
	      "  <task-node name='t2'>" +
	      "    <task name='t2'>" +
	      "     <assignment  config-type='bean'  class='org.springmodules.workflow.jbpm31.JbpmHandlerProxy'> <targetBean>assignmentHandlerService</targetBean> </assignment>" +
	      "    </task>" +
	      "    <transition to='end' />" +
	      "  </task-node>" +
	      "  <end-state name='end' />" +
	      "</process-definition>";*/

				String processXml =  "<process-definition name='the baby process'>" +
	      "  <start-state>" +
	      "    <transition name='baby cries' to='t1' />" +
	      "  </start-state>" +
	      "  <task-node name='t1'>" +
	      "    <task name='t1'>" +
	      "     <assignment  config-type='bean'  class='org.springmodules.workflow.jbpm31.JbpmHandlerProxy'> <targetBean>assignmentHandlerService</targetBean> </assignment>" +
	      "    </task>" +
	      "    <transition to='c1' />" +
	      "  </task-node>" +
	      "  <task-node name='c1' create-tasks='false'>" +
	      "<event type='node-enter'>" +
			" <action name='choose' class='org.springmodules.workflow.jbpm31.JbpmHandlerProxy' config-type='bean'>" +
			"<targetBean>conditionTaskHandlerService</targetBean>" +
			"<factoryKey>jbpmConfiguration</factoryKey>" +
			"</action>" +
			"</event>" +
	      "    <transition to='t2' />" +
	      "  </task-node>" +
	      "  <task-node name='t2'>" +
	      "    <task name='t2'>" +
	      "     <assignment  config-type='bean'  class='org.springmodules.workflow.jbpm31.JbpmHandlerProxy'> <targetBean>assignmentHandlerService</targetBean> </assignment>" +
	      "    </task>" +
	      "    <transition to='end' />" +
	      "  </task-node>" +
	      "  <task-node name='t3'>" +
	      "    <task name='t3'>" +
	      "     <assignment  config-type='bean'  class='org.springmodules.workflow.jbpm31.JbpmHandlerProxy'> <targetBean>assignmentHandlerService</targetBean> </assignment>" +
	      "    </task>" +
	      "    <transition to='end' />" +
	      "  </task-node>" +
	      "  <end-state name='end' />" +
	      "</process-definition>";
		
/*		String processXml =  "<process-definition name='ffff2222'>" +
				"<start-state name='开始'>" +
				"<transition name='to t1' to='t1'/>" +
				"</start-state>" +
				"<end-state name='结束'/>" +
				 "  <task-node name='t1'>" +
			      "    <task name='t1'>" +
			      "     <assignment  config-type='bean'  class='org.springmodules.workflow.jbpm31.JbpmHandlerProxy'> <targetBean>assignmentHandlerService</targetBean> </assignment>" +
			      "    </task>" +
			      "    <transition to='会签任务' />" +
			      "  </task-node>" +
				"<task-node name='会签任务' signal='last-wait' create-tasks='false'>" +
				"<transition name='to 结束' to='结束'/>" +
				"<task name='会签任务' description='会签'>" +
				"<event type='task-end'>" +
				"<action class='org.springmodules.workflow.jbpm31.JbpmHandlerProxy' config-type='bean'>" +
				"<targetBean>taskEndCountersignHandlerService</targetBean>" +
				"<factoryKey>jbpmConfiguration</factoryKey>" +
				"</action>" +
				"</event>" +
				"</task>" +
				"<event type='node-enter'>" +
				" <action name='createInstance' class='org.springmodules.workflow.jbpm31.JbpmHandlerProxy' config-type='bean'>" +
				"<targetBean>createTaskInstanceCountersignHandlerService</targetBean>" +
				"<factoryKey>jbpmConfiguration</factoryKey>" +
				"</action>" +
				"</event>" +
				"</task-node>" +
				
				"</process-definition>";*/

		//wfDfprocess.setDfxml(processXml);
		
		try{
			
			IWfProcessDeployService wfProcessDeployService = (IWfProcessDeployService) getBean("wfProcessDeployService");
			wfProcessDeployService.savDeploy(wfDfprocess,this.getCurrentUser());
			
			return toJSON(wfDfprocess,"{success:true,data:","}");
		}catch(Exception ex) {
			ex.printStackTrace();
			return toSTR("{success:false}");
		}
		
	}




}
