package com.est.workflow.process.service;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.bytes.ByteArray;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

import com.est.common.base.BaseDaoImp;
import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.sysmodule.dao.ISysModuleDao;
import com.est.sysinit.sysmodule.vo.SysModule;
import com.est.sysinit.sysuser.vo.SysUser;
import com.est.workflow.process.annotation.WFAnnotationHelper;
import com.est.workflow.process.annotation.WFSignFieldAnnotation;
import com.est.workflow.process.callback.IWFCallback;
import com.est.workflow.process.config.WorkFlowConfig;
import com.est.workflow.process.dao.IWfApproveDao;
import com.est.workflow.process.dao.IWfProcessinstanceDao;
import com.est.workflow.process.exception.WfTaskIsAlreadyRecievedException;
import com.est.workflow.process.vo.WfProcessinstance;
import com.est.workflow.process.vo.WfSignFieldVO;
import com.est.workflow.process.vo.WfTodoWorkVO;
import com.est.workflow.processdefination.dao.IWfDfprocessDao;
import com.est.workflow.processdefination.dao.IWfDftaskDao;
import com.est.workflow.processdefination.vo.WfDfprocess;
import com.est.workflow.processdefination.vo.WfDftask;
import com.est.workflow.processdefination.vo.WfDftaskcondition;
import com.est.workflow.processdefination.vo.WfDftaskfield;
import com.est.workflow.processdefination.vo.WfDftaskuser;

public class WfProcessServiceImp implements IWfProcessService {

	private final Log log = LogFactory.getLog(WfProcessServiceImp.class);

	private JbpmConfiguration jbpmConfiguration;
	private IWfDfprocessDao wfDfprocessDao;
	private IWfDftaskDao wfDftaskDao;
	private IWfProcessinstanceDao wfprocessinstanceDao;
	private IWfApproveDao wfApproveDao;
	private ISysModuleDao sysModuleDao;

	public void setWfprocessinstanceDao(
			IWfProcessinstanceDao wfprocessinstanceDao) {
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

	public void setWfDftaskDao(IWfDftaskDao wfDftaskDao) {
		this.wfDftaskDao = wfDftaskDao;
	}

	
	
	
	/**
	 * 开始一个新的流程实例
	 */
	public ProcessInstance savStartProcess(SearchCondition condition) {

		Long processdefinationId = (Long) condition.get("processdefinationId");
		String topic = (String) condition.get("topic");
		String draftby = (String) condition.get("draftby");
		Long draftbyid = (Long) condition.get("draftbyid");
		Long moduleId = (Long) condition.get("moduleId");
		Long masterId = (Long) condition.get("masterId");

		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		ProcessDefinition processDefinition = null;

		// ProcessDefinition processDefinition = jbpmContext.getGraphSession().findLatestProcessDefinition(processName);
		if (processdefinationId != null) {
			processDefinition = jbpmContext.getGraphSession().getProcessDefinition(processdefinationId);
		} else {
			throw new BaseBussinessException("没有指定发起的流程。");
		}

		ProcessInstance processInstance = processDefinition.createProcessInstance();
		
		jbpmContext.save(processInstance);
		
		WfDfprocess wfDfprocess = new WfDfprocess();
		wfDfprocess.setJbpmpid(processdefinationId);
		List<WfDfprocess> processlist = wfDfprocessDao.findByExample(wfDfprocess);
		if (processlist.size() == 0) {
			wfDfprocess = null;
		} else {
			wfDfprocess = processlist.get(0);
		}

		WfProcessinstance wfpi = new WfProcessinstance();
		wfpi.setProcessinstanceId(processInstance.getId());
		wfpi.setJbpmId(processInstance.getId());
		wfpi.setCurrenttask(null);
		wfpi.setFlowstate(WorkFlowConfig._WF_IN_FLOW);
		wfpi.setMasterid(masterId);
		wfpi.setDraftdate(new Date());
		wfpi.setTopic(topic);
		wfpi.setDraftby(draftby);
		wfpi.setDraftbyId(draftbyid);
		wfpi.setWfDfprocess(wfDfprocess);
		wfprocessinstanceDao.save(wfpi, BaseDaoImp.SAVE);
		
		
		processInstance.getRootToken().signal(); // 激活流程

		/*
		// 修改主表状态
		try {
			savBussinessObjectStatus(processInstance.getId(), WorkFlowConfig._WF_IN_FLOW);
		} catch (Exception e) {
			throw new RuntimeException("修改主表状态失败！");
		}
		*/

		return processInstance;
	}

	
	/**
	 * 修改业务表对象的流程状态
	 * 
	 * @desc
	 * @date Oct 27, 2009
	 * @author jingpj
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ClassNotFoundException
	 */
	public void savBussinessObjectStatus(Long piid, String status ,String callbackMethod) throws Exception {
		WfProcessinstance wfpi = wfprocessinstanceDao.findById(piid);
		wfpi.setFlowstate(status);
		wfprocessinstanceDao.save(wfpi);

		Long masterId = wfpi.getMasterid();
		Long moduleId = wfpi.getWfDfprocess().getModuleId();

		// 修改主表状态
		String classPath = getActionClassname(moduleId);

		WFAnnotationHelper annotationHelper = new WFAnnotationHelper(classPath);
		
		Class  masterClass = annotationHelper.getMasterClass();
		Object obj = wfprocessinstanceDao.findById(masterClass,masterId);
		
		IWFCallback callback = (IWFCallback) annotationHelper.getCallbackBean();
		
		try {
			Method method = masterClass.getMethod("setWfstatus",String.class);
			method.invoke(obj, status);
			method = masterClass.getMethod("setWfpiid", Long.class);
			method.invoke(obj, piid);
			wfprocessinstanceDao.save(obj);
			
			if(callback != null) {
				if (WorkFlowConfig._CALLBACK_BEFORESTART.equals(callbackMethod)) {
					callback.beforeProcessStart(masterId);
				} else if (WorkFlowConfig._CALLBACK_AFTERSTART.equals(callbackMethod)) {
					callback.afterProcessStart(masterId,status);
				} else if (WorkFlowConfig._CALLBACK_BEFORENODEENTRY.equals(callbackMethod)) {
					callback.beforeNodeEntry(masterId);
				} else if (WorkFlowConfig._CALLBACK_AFTERNODELEAVE.equals(callbackMethod)) {
					callback.afterNodeLeave(masterId,status);
				} else if (WorkFlowConfig._CALLBACK_BEFOREEND.equals(callbackMethod)) {
					callback.beforeProcessEnd(masterId);
				} else if (WorkFlowConfig._CALLBACK_AFTEREND.equals(callbackMethod)) {
					callback.afterProcessEnd(masterId,status);
				} else if (WorkFlowConfig._CALLBACK_TERMINATE.equals(callbackMethod)) {
					callback.afterProcessTerminate(masterId);
				} 
			}
				
			
		} catch (SecurityException e) {
			e.printStackTrace();
			throw e;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw e;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw e;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw e;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw e;
		}
		
		/*
		try {
			Class clazz = Class.forName(classPath);
			if (clazz.isAnnotationPresent(WFBusinessClassAnnotation.class)) {
				WFBusinessClassAnnotation wFBusinessClassAnnotation = (WFBusinessClassAnnotation) clazz
						.getAnnotation(WFBusinessClassAnnotation.class);
				String wfBusinessClassString = wFBusinessClassAnnotation
						.value();
				Class wfBusinessClass = Class.forName(wfBusinessClassString);

				Object obj = wfprocessinstanceDao.findById(wfBusinessClass,
						masterId);

				try {
					Method method = wfBusinessClass.getMethod("setWfstatus",
							String.class);
					method.invoke(obj, status);
					method = wfBusinessClass.getMethod("setWfpiid", Long.class);
					method.invoke(obj, piid);
					wfprocessinstanceDao.save(obj);
				} catch (SecurityException e) {
					e.printStackTrace();
					throw e;
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					throw e;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					throw e;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					throw e;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					throw e;
				}
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw e;
		}
		*/

	}

	/**
	 * 修改业务表对象的签字列
	 * 
	 * @desc
	 * @date Oct 27, 2009
	 * @author jingpj
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ClassNotFoundException
	 */
	private void setBussinessObjectSignField(Long piid, String filename,
			String username) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException,
			ClassNotFoundException {
		WfProcessinstance wfpi = wfprocessinstanceDao.findById(piid);

		Long masterId = wfpi.getMasterid();
		Long moduleId = wfpi.getWfDfprocess().getModuleId();

		// 修改主表状态
		String classPath = getActionClassname(moduleId);
		
		WFAnnotationHelper annotationHelper = new WFAnnotationHelper(classPath);
		
		Class  wfBusinessClass = annotationHelper.getMasterClass();

		Object obj = wfprocessinstanceDao.findById(wfBusinessClass,
				masterId);

		try {
			String methodname = "set"
					+ filename.substring(0, 1).toUpperCase()
					+ filename.substring(1);
			Method method = wfBusinessClass.getMethod(methodname,
					String.class); // 签字列必须为字符型
			method.invoke(obj, username);
			wfprocessinstanceDao.save(obj);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw e;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw e;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw e;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw e;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw e;
		}


	}
	
	
	
	/**
	 * 修改业务表对象的签字列
	 * 
	 * @desc
	 * @date Oct 27, 2009
	 * @author jingpj
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public List<WfSignFieldVO> getBussinessObjectSignFields(Long processid) {
		try {
		
			WfDfprocess wfprocess = wfDfprocessDao.findById(processid);
	
			Long moduleId = wfprocess.getModuleId();
	
			// 修改主表状态
			String classPath = getActionClassname(moduleId);
			
			WFAnnotationHelper annotationHelper = new WFAnnotationHelper(classPath);
			
			Class wfBusinessClass = annotationHelper.getMasterClass();
			
			Class clazz = Class.forName(classPath);
			
			
				Field[] fields = wfBusinessClass.getDeclaredFields();
				
				List<WfSignFieldVO> fieldList = new ArrayList<WfSignFieldVO>();  
				
				for(Field field : fields) {
					if(field.isAnnotationPresent(WFSignFieldAnnotation.class)) {
						
						WfSignFieldVO tmp = new WfSignFieldVO();
						
						String fieldname = field.getName();
						
						tmp.setSigncolumn(fieldname);
						
						//用数据库的备注字段标示描述
						String descrition =  wfprocessinstanceDao.getFieldMemo(wfBusinessClass, fieldname);
						
						/* 用annotation标示字段描述，已取消
						WFSignFieldAnnotation annotation = field.getAnnotation(WFSignFieldAnnotation.class);
						String descrition = annotation.value();
						*/
						if(descrition!=null && !"".equals(descrition)) {
							tmp.setFieldDescription(descrition);
						} else {
							tmp.setSigncolumn(fieldname);
						}
						
						fieldList.add(tmp);
					}
				}
			
				return fieldList;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBussinessException("读取签字列列表失败！");
		}

	}

	/**
	 * @desc 得到action对应类的类名路径
	 * @date Oct 27, 2009
	 * @author jingpj
	 * @param module
	 * @return
	 */
	public String getActionClassname(Long moduleId) {
		SysModule module = sysModuleDao.findById(moduleId);
		String url = module.getUrl();
		url = url.split("\\?")[0];
		String[] arrPath = url.split("/");
		StringBuilder path = new StringBuilder(200);
		for (int i = arrPath.length - 4; i < arrPath.length - 1; i++) {
			path.append(".");
			String tmp = arrPath[i];
			if (i == arrPath.length - 2) {
				path.append("action.");
				path.append(tmp);
				path.append("Action");
			} else {
				path.append(tmp);
			}

		}
		String classname = "com.est" + path.toString();
		return classname;
	}


	/**
	 * 
	 * @desc
	 * @date Oct 13, 2009
	 * @author jingpj
	 * @param token
	 */
	@Deprecated
	public void savSendNext(Token token) {

		System.out.println(token.getNode().getName());
		token.signal();
		return;
	}

	@Deprecated
	public void savSendNext(TaskInstance taskInstance) {
		System.out.println(taskInstance.getName());
		taskInstance.end();
		return;
	}

	
	
	
	/**
	 * @desc 得到已分配(待接收)任务列表
	 * @date Oct 15, 2009
	 * @author jingpj
	 * @return
	 */
	public Result getAssignedTaskList(Page page, SearchCondition condition){
		
		SysUser loginUser = (SysUser) condition.get("loginUser");
		String topic = (String) condition.get("topic");
		String draftby = (String) condition.get("draftby");
		String draftdatestart = (String) condition.get("draftdatestart");
		String draftdateend = (String) condition.get("draftdateend");
		
		
		StringBuilder hql = new StringBuilder(1000);
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		hql.append("SELECT ti FROM org.jbpm.taskmgmt.exe.PooledActor p");
		hql.append(" JOIN p.taskInstances ti ");
		
		hql.append(" WHERE p.actorId = ?");
		paramList.add(loginUser.getUserid().toString());
		
		hql.append(" AND ti.actorId IS NULL AND ti.isSuspended != true AND ti.isOpen = true ");
		
		if((topic!=null && !"".equals(topic))||(draftby!=null && !"".equals(draftby))|| (draftdatestart!=null && !"".equals(draftdatestart))||(draftdateend!=null && !"".equals(draftdateend))) {
			hql.append(" AND EXISTS ( FROM WfProcessinstance wfpi WHERE wfpi.processinstanceId = ti.token.processInstance.id ");
			if(topic!=null && !"".equals(topic)) {
				hql.append(" AND wfpi.topic like ? ");
				paramList.add("%"+topic+"%");
			}
			if(draftby!=null && !"".equals(draftby)) {
				hql.append(" AND wfpi.draftby like ? ");
				paramList.add("%"+draftby+"%");
			}
			if(draftdatestart!=null && !"".equals(draftdatestart)) {
				hql.append(" AND to_char(wfpi.draftdate,'yyyy-mm-dd') >= ? ");
				paramList.add(draftdatestart);
			}
			if(draftdateend!=null && !"".equals(draftdateend)) {
				hql.append(" AND to_char(wfpi.draftdate,'yyyy-mm-dd') <= ? ");
				paramList.add(draftdateend);
			}
			hql.append(")");
		}
		
		hql.append("  ORDER BY ti.create desc");
		
		
//		hql.append("SELECT DISTINCT ti FROM org.jbpm.taskmgmt.exe.PooledActor p");
//		hql.append(" JOIN p.taskInstances ti ");
//		hql.append(" WHERE p.actorId = ?");
//		hql.append(" AND ti.actorId IS NULL AND ti.isSuspended != true AND ti.isOpen = true ");
//		hql.append("  ORDER BY ti.create desc");
		try {
			Result result = wfDfprocessDao.findByPage(page, hql.toString(),
					paramList.toArray());
			List<TaskInstance> tmp = result.getContent();

			List<WfTodoWorkVO> lst = new ArrayList<WfTodoWorkVO>();

			for (TaskInstance ti : tmp) {
				WfTodoWorkVO todoWork = new WfTodoWorkVO();

				todoWork.setTaskId(ti.getId());
				todoWork.setTaskName(ti.getName());
				todoWork.setTaskDescription(ti.getDescription());
				todoWork.setCreatDate(ti.getCreate());
				todoWork.setStartDate(ti.getStart());
				todoWork.setEndDate(ti.getEnd());
				todoWork.setTaskDefinitionId(ti.getToken().getProcessInstance().getProcessDefinition().getId());

				WfProcessinstance wfpi = getWfProcessinstanceByTi(ti);
				todoWork.setProcessTitle(wfpi.getTopic());
				todoWork.setWfProcessInstanceId(wfpi.getProcessinstanceId());
				todoWork.setDraftby(wfpi.getDraftby());
				todoWork.setMasterId(wfpi.getMasterid());

				lst.add(todoWork);
			}

			result.setContent(lst);

			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		
	}

	/**
	 * @desc 得到已接收任务列表
	 * @date Oct 16, 2009
	 * @author jingpj
	 * @param page
	 * @param loginUser
	 * @return
	 * @see com.est.workflow.process.service.IWfProcessService#getRecievedTaskList(com.est.common.ext.util.Page,
	 *      com.est.sysinit.sysuser.vo.SysUser)
	 */
	@SuppressWarnings("unchecked")
	public Result getRecievedTaskList(Page page, SearchCondition condition) {
		
		
		SysUser loginUser = (SysUser) condition.get("loginUser");
		String topic = (String) condition.get("topic");
		String draftby = (String) condition.get("draftby");
		String draftdatestart = (String) condition.get("draftdatestart");
		String draftdateend = (String) condition.get("draftdateend");
		
		StringBuilder hql = new StringBuilder(1000);
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		hql.append(" SELECT ti  FROM org.jbpm.taskmgmt.exe.TaskInstance as ti ");
		
		hql.append(" WHERE ti.actorId = ?  AND ti.isSuspended != true AND ti.isOpen = true ");
		paramList.add(loginUser.getUserid().toString());
		
		
		if((topic!=null && !"".equals(topic))||(draftby!=null && !"".equals(draftby))|| (draftdatestart!=null && !"".equals(draftdatestart))||(draftdateend!=null && !"".equals(draftdateend))) {
			hql.append(" AND EXISTS ( FROM WfProcessinstance wfpi WHERE wfpi.processinstanceId = ti.token.processInstance.id ");
			if(topic!=null && !"".equals(topic)) {
				hql.append(" AND wfpi.topic like ? ");
				paramList.add("%"+topic+"%");
			}
			if(draftby!=null && !"".equals(draftby)) {
				hql.append(" AND wfpi.draftby like ? ");
				paramList.add("%"+draftby+"%");
			}
			if(draftdatestart!=null && !"".equals(draftdatestart)) {
				hql.append(" AND to_char(wfpi.draftdate,'yyyy-mm-dd') >= ? ");
				paramList.add(draftdatestart);
			}
			if(draftdateend!=null && !"".equals(draftdateend)) {
				hql.append(" AND to_char(wfpi.draftdate,'yyyy-mm-dd') <= ? ");
				paramList.add(draftdateend);
			}
			hql.append(")");
		}
		
		hql.append("  ORDER BY ti.create desc");
		
		
//		hql.append(" SELECT ti  FROM org.jbpm.taskmgmt.exe.TaskInstance as ti ");
//		hql.append(" WHERE ti.actorId = ?  AND ti.isSuspended != true AND ti.isOpen = true ");
//		
//		
//		hql.append(" ORDER BY ti.create desc");

		Result result = wfDfprocessDao.findByPage(page, hql.toString(),paramList.toArray());

		List<TaskInstance> tmp = result.getContent();

		List<WfTodoWorkVO> lst = new ArrayList<WfTodoWorkVO>();

		for (TaskInstance ti : tmp) {
			WfTodoWorkVO todoWork = new WfTodoWorkVO();

			todoWork.setTaskId(ti.getId());
			todoWork.setTaskName(ti.getName());
			todoWork.setTaskDescription(ti.getDescription());
			todoWork.setCreatDate(ti.getCreate());
			todoWork.setStartDate(ti.getStart());
			todoWork.setEndDate(ti.getEnd());
			todoWork.setTaskDefinitionId(ti.getToken().getProcessInstance().getProcessDefinition().getId());

			WfProcessinstance wfpi = getWfProcessinstanceByTi(ti);
			todoWork.setProcessTitle(wfpi.getTopic());
			todoWork.setWfProcessInstanceId(wfpi.getProcessinstanceId());
			todoWork.setDraftby(wfpi.getDraftby());
			todoWork.setMasterId(wfpi.getMasterid());

			lst.add(todoWork);
		}

		result.setContent(lst);

		return result;

	}
	
	/**
	 *@desc 得到我发起的流程 
	 *@date Nov 24, 2009
	 *@author jingpj
	 *@param page
	 *@param condition
	 *@return
	 *@see com.est.workflow.process.service.IWfProcessService#getMyDraftProcessList(com.est.common.ext.util.Page, com.est.common.ext.util.SearchCondition)
	 */
	public Result getMyDraftProcessList(Page page, SearchCondition condition) {
		SysUser loginUser = (SysUser) condition.get("loginUser");
		String topic = (String) condition.get("topic");
		String status = (String) condition.get("status");
		String draftdatestart = (String) condition.get("draftdatestart");
		String draftdateend = (String) condition.get("draftdateend");
		
		StringBuilder hql = new StringBuilder(1000);
		ArrayList<Object> paramList = new ArrayList<Object>();
		hql.append("FROM WfProcessinstance t where draftbyId = ?");
		paramList.add(loginUser.getUserid());
		if(topic!=null && !"".equals(topic)) {
			hql.append(" AND t.topic like ?");
			paramList.add("%"+topic+"%");
		}
		if(status!=null && !"".equals(status)) {
			hql.append(" AND t.flowstate like ?");
			paramList.add(status+"%");
		}
		if(draftdatestart!=null && !"".equals(draftdatestart)) {
			hql.append(" AND to_char(t.draftdate,'yyyy-mm-dd') >= ?");
			paramList.add(draftdatestart);
		}
		if(draftdateend!=null && !"".equals(draftdateend)) {
			hql.append(" AND to_char(t.draftdate,'yyyy-mm-dd') <= ?");
			paramList.add(draftdateend);
		}
		hql.append(" ORDER BY t.draftdate DESC");
		
		return wfDfprocessDao.findByPage(page, hql.toString(),paramList.toArray());
	}

	/**
	 * 
	 * @desc 接收任务
	 * @date Oct 16, 2009
	 * @author jingpj
	 * @param taskId
	 * @param loginUser
	 * @throws WfTaskIsAlreadyRecievedException
	 * @see com.est.workflow.process.service.IWfProcessService#savRecieveTask(java.lang.Long,
	 *      com.est.sysinit.sysuser.vo.SysUser)
	 */
	public void savRecieveTask(Long taskId, SysUser loginUser)
			throws WfTaskIsAlreadyRecievedException {
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		TaskInstance ti = jbpmContext.getTaskInstance(taskId);
		if (!ti.isOpen() || ti.getActorId() != null) {
			throw new WfTaskIsAlreadyRecievedException();
		} else {
			ti.setActorId(String.valueOf(loginUser.getUserid()));
			ti.start();
			// jbpmContext.save(ti);
		}
	}

	/**
	 * @desc 强制终止流程
	 * @date Oct 19, 2009
	 * @author jingpj
	 * @param tiid
	 * @see com.est.workflow.process.service.IWfProcessService#savEndProcess(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public void savEndProcess(Long taskInstanceId) {
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		TaskInstance ti = jbpmContext.getTaskInstance(taskInstanceId);
		ProcessInstance pi = ti.getTaskMgmtInstance().getProcessInstance();
		Collection<TaskInstance> taskCollection = ti.getTaskMgmtInstance().getSignallingTasks(new ExecutionContext(ti.getToken()));
		Iterator<TaskInstance> it = taskCollection.iterator();
		while(it.hasNext()) {
			TaskInstance taskInstance = (TaskInstance) it.next();
			taskInstance.setSignalling(false);
			taskInstance.cancel();
		}
		
		pi.end();
		jbpmContext.save(pi);

		try {
			savBussinessObjectStatus(pi.getId(), WorkFlowConfig._WF_TERMINATE, WorkFlowConfig._CALLBACK_TERMINATE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("保存主表状态失败！");
		}
		
		WfProcessinstance wfpi =  wfprocessinstanceDao.findById(pi.getId());
		wfpi.setEnddate(new Date());
		wfprocessinstanceDao.save(wfpi);

	}

	/**
	 * 
	 * @desc 发送到下一步
	 * @date Oct 19, 2009
	 * @author jingpj
	 * @param tiid
	 * @param type
	 * @see com.est.workflow.process.service.IWfProcessService#savSendToNextStep(java.lang.Long,
	 *      java.lang.String)
	 */
	public void savSendToNextStep(Long taskInstanceId, String type, SysUser user) {
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		TaskInstance ti = jbpmContext.getTaskInstance(taskInstanceId);
		ProcessInstance pi = ti.getTaskMgmtInstance().getProcessInstance();
		ti.getToken().getNode().getId();

		// 签字列修改
		Long defTaskid = ti.getToken().getNode().getId();//ti.getTask().getId();
		WfDftask wfDftask = wfDftaskDao.findUniqueBy("taskId", defTaskid);
		String signcolumn = wfDftask.getSigncolumn();

		if (signcolumn != null) {
			try {
				setBussinessObjectSignField(pi.getId(), signcolumn, user
						.getUsername());
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("保存主表状态失败！");
			}
		}
		ti.getContextInstance().setVariable("rejectToOldUser",false);

		try {
			if (type == null) {
				ti.end();
			} else {
				ti.end(type);
			}
			jbpmContext.save(pi);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (pi.getEnd() != null) {
			try {
				savBussinessObjectStatus(pi.getId(), WorkFlowConfig._WF_FLOW_DONE,WorkFlowConfig._CALLBACK_AFTEREND);
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("保存主表状态失败！");
			}
			WfProcessinstance wfpi =  wfprocessinstanceDao.findById(pi.getId());
			wfpi.setEnddate(new Date());
			wfprocessinstanceDao.save(wfpi);
		}
	}

	/**
	 * @desc 驳回
	 * @date Oct 31, 2009
	 * @author jingpj
	 * @param tiid
	 * @see com.est.workflow.process.service.IWfProcessService#savReject(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public void savReject(Long tiid,Boolean rejectToOldUser) {
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		TaskInstance ti = jbpmContext.getTaskInstance(tiid);

		TaskMgmtInstance tmi = ti.getTaskMgmtInstance();

		/** 普通签字任务驳回 */
		ProcessDefinition processDefinition = ti.getContextInstance()
				.getProcessInstance().getProcessDefinition();
		ByteArray byteArr = (ByteArray) ti.getContextInstance().getVariable("historyNodes");
		ObjectInputStream ois;
		try {
			//得到流程历史
			ois = new ObjectInputStream(new ByteArrayInputStream(byteArr.getBytes()));
			Stack<String> historyNodes = (Stack<String>) ois.readObject();
			// Stack<String> historyNodes = (Stack<String>)
			// ti.getVariable("historyNodes");
			historyNodes.pop();
			String preNodeName = historyNodes.pop();
			if (historyNodes.size() > 0) {
				ti.getContextInstance().setVariable("historyNodes",historyNodes);
			} else {
				ti.getContextInstance().setVariable("historyNodes", null);
			}

			//是否回退到原来的参与者
			ti.getContextInstance().setVariable("rejectToOldUser", rejectToOldUser);
			
			Collection<TaskInstance> c = tmi
					.getSignallingTasks(new ExecutionContext(ti.getToken()));
			if (c.size() > 1) {
				/** 会签任务驳回 */
				// 设置驳回标志位
				ti.setVariableLocally("disapprove", true);
				ti.setVariableLocally("notapprovetransitionname",preNodeName);
				ti.end();
			} else {
				if (preNodeName == null) {

				} else {
					String transitionName = "tmpTransition"
							+ new Date().getTime();
					Transition transition = new Transition(transitionName);

					Node toNode = processDefinition.getNode(preNodeName);
					transition.setTo(toNode);
					transition.setProcessDefinition(processDefinition);

					Node fromNode = ti.getToken().getNode();
					transition.setFrom(fromNode);
					fromNode.addLeavingTransition(transition);
					ti.end(transitionName);
				}

			}
		} catch (java.util.EmptyStackException ex) {
			throw new RuntimeException("流程已经到了开始，不能回退，请选择终止流程！");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * @desc 通过jbpm表的piid得到WfProcessinstance对象
	 * @date Oct 16, 2009
	 * @author jingpj
	 * @param ti
	 * @return
	 */
	private WfProcessinstance getWfProcessinstanceByTi(TaskInstance ti) {

		Long processId = ti.getContextInstance().getProcessInstance().getId();

		String hql = "from WfProcessinstance t where t.jbpmId=?";

		WfProcessinstance processinstance = (WfProcessinstance) wfDfprocessDao
				.findUniqueByHql(hql, processId);

		return processinstance;
	}

	/**
	 * @desc
	 * @date Oct 20, 2009
	 * @author jingpj
	 * @param taskDefinitionId
	 * @return
	 * @see com.est.workflow.process.service.IWfProcessService#getWfDfprocessBytaskDefId(java.lang.Long)
	 */
	public WfDfprocess getWfDfprocessBytaskDefId(Long taskDefinitionId) {
		String hql = "from WfDfprocess t where t.jbpmpid = ?";
		return (WfDfprocess) wfDfprocessDao.findUniqueByHql(hql,
				taskDefinitionId);
	}

	/**
	 *@desc 得到选择节点的 
	 *@date Nov 1, 2009
	 *@author jingpj
	 *@param processDefinitionId
	 *@param nodeName
	 *@return
	 *@see com.est.workflow.process.service.IWfProcessService#getTaskConditions(java.lang.Long, java.lang.String)
	 */
	public List<WfDftaskcondition> getTaskConditions(Long processDefinitionId, String nodeName) {
		WfDfprocess wfDfprocess = wfDfprocessDao.findById(processDefinitionId);
		String hql = "from WfDftaskcondition t where t.wfDftask.wfDfprocess.processId = ? and t.wfDftask.name = ? order by t.taskconditionId";
		List<WfDftaskcondition> wfDftaskconditions =  wfDfprocessDao.findByHql(hql, processDefinitionId,nodeName);
		return wfDftaskconditions;
	}

	/**
	 *@desc 得到node分配的流程用户 
	 *@date Nov 2, 2009
	 *@author jingpj
	 *@param taskId
	 *@return
	 *@see com.est.workflow.process.service.IWfProcessService#getActIdsByTaskId(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public String[] getActIdsByTaskId(Long taskId){
		String hql = "from WfDftaskuser t where t.wfDftask.taskId = ?";
		List<WfDftaskuser> users = wfDftaskDao.findByHql(hql, taskId);
		String[] lst = new String[users.size()];
		
		for(int i = 0; i<users.size(); i++){
			WfDftaskuser taskuser = users.get(i);
			lst[i] = taskuser.getUser().getUserid().toString();
		}
		return  lst;
	}
	

	/**
	 *@desc 得到node设置的字段属性
	 *@date Feb 7, 2010
	 *@author jingpj
	 *@param taskId
	 *@return
	 */
	@SuppressWarnings("unchecked")
	public List<WfDftaskfield> getTaskFieldByTaskId(Long taskId) {
		String hql = "from WfDftaskfield t where t.wfDftask.taskId = ?";
		List<WfDftaskfield> fields = wfDftaskDao.findByHql(hql, taskId);
		return fields;
	}
	
	/**
	 *@desc 得到tasknodeid 
	 *@date Nov 2, 2009
	 *@author jingpj
	 *@param processId
	 *@param nodeName
	 *@return
	 *@see com.est.workflow.process.service.IWfProcessService#getTaskNodeId(java.lang.Long, java.lang.String)
	 */
	public Long getTaskNodeId(Long processId, String nodeName) {
		String hql = "select t.taskId from WfDftask t where t.wfDfprocess.processId = ? and t.name = ?";
		return (Long) wfDftaskDao.findUniqueByHql(hql, processId,nodeName);
	}

	/**
	 *@desc 得到该节点上次的参与者 
	 *@date Nov 7, 2009
	 *@author jingpj
	 *@param taskId
	 *@see com.est.workflow.process.service.IWfProcessService#getTaskLastTimeHandleUser(java.lang.Long)
	 */
	public String getTaskLastTimeActor(Long taskId,Long piid) {
		String hql = "from TaskInstance t where t.task.id = ? and t.token.processInstance.id = ? and t.actorId is not null order by t.id desc";
		List<TaskInstance> tiList= wfDftaskDao.findByHql(hql, taskId, piid);
		if(tiList.size() > 0) {
			return tiList.get(0).getActorId();
		} else {
			throw new BaseBussinessException("未找到原处理者！");
		}
	}
	
	
	
	
	
	/**
	 * 
	 *@desc 得到流程定义下所有的任务节点
	 *@date May 4, 2010
	 *@author jingpj
	 *@param taskInstanceId
	 *@return
	 */
	public List<WfDftask> getDefinedTasks(Long processId) {
		String hql = "from WfDftask t where t.wfDfprocess.processId = ?";
		return wfDftaskDao.findByHql(hql, processId);
	}
	
}
