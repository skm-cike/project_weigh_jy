package com.est.workflow.processdefination.service;

import java.util.ArrayList;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.workflow.processdefination.dao.IWfDfprocessDao;
import com.est.workflow.processdefination.dao.IWfDftaskDao;
import com.est.workflow.processdefination.vo.WfDfprocess;
import com.est.workflow.processdefination.vo.WfDftask;

public class WfDfprocessServiceImp implements IWfDfprocessService {

	private IWfDfprocessDao wfDfprocessDao;
	private IWfDftaskDao wfDftaskDao;

	public void setWfDfprocessDao(IWfDfprocessDao wfDfprocessDao) {
		this.wfDfprocessDao = wfDfprocessDao;
	}

	public WfDfprocess getWfDfprocess(Long processId) {
		return wfDfprocessDao.findById(processId);
	}

	public void setWfDftaskDao(IWfDftaskDao wfDftaskDao) {
		this.wfDftaskDao = wfDftaskDao;
	}

	/**
	 * 
	 * @desc 获取流程定义列表
	 * @date Nov 5, 2009
	 * @author hebo
	 * @param condition
	 * @param page
	 * @return
	 * @throws Exception
	 * @see com.est.workflow.processdefination.service.IWfDfprocessService#getProcessDefinationListByPage(com.est.common.ext.util.SearchCondition,
	 *      com.est.common.ext.util.Page)
	 */
	@SuppressWarnings("unchecked")
	public Result<WfDfprocess> getProcessDefinationListByPage(
			SearchCondition condition, Page page) throws Exception {

		try {
			// 模块ID
			Long moduleId = (Long) condition.get("moduleId");
			// 流程名称
			String processname = (String) condition.get("processname");

			if (moduleId == null || moduleId == 0L) {
				return null;
			}

			StringBuffer hql = new StringBuffer();
			hql.append("from WfDfprocess t where 1=1");

			List<Object> paramList = new ArrayList<Object>();
			if (moduleId != null && moduleId != 0L) {
				hql.append(" and t.moduleId = ?");
				paramList.add(moduleId);
			}
			if (processname != null && !"".equals(processname)) {
				hql.append(" and t.processname like ?");
				paramList.add("%" + processname + "%");
			}
			hql.append(" order by t.deployDate asc");

			return wfDfprocessDao.findByPage(page, hql.toString(), paramList
					.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @desc 设置流程定义是否有效
	 * @date Nov 4, 2009
	 * @author hebo
	 * @param condition
	 * @throws Exception
	 * @see com.est.workflow.processdefination.service.IWfDfprocessService#savProcessValid(com.est.common.ext.util.SearchCondition)
	 */
	public void savProcessValid(SearchCondition condition) throws Exception {
		try {

			String hql = "update WfDfprocess t set t.isvalid=? where t.processId=?";

			List<Object> paramList = new ArrayList<Object>();
			// 是否有效 F标示无效，T标示有效
			paramList.add((String) condition.get("isvalid"));
			// 流程定义ID
			paramList.add((Long) condition.get("processId"));

			wfDfprocessDao.updateByHql(hql, paramList.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @desc 保存任务
	 * @date Nov 5, 2009
	 * @author hebo
	 * @param wfDftask
	 * @throws Exception
	 * @see com.est.workflow.processdefination.service.IWfDfprocessService#savWfDftask(com.est.workflow.processdefination.vo.WfDftask)
	 */
	public void savWfDftask(WfDftask wfDftask) throws Exception {
		try {
			if(wfDftask.getWfDfprocess()!=null && wfDftask.getWfDfprocess().getProcessId()==null){
				wfDftask.setWfDfprocess(null);
			}
			wfDftaskDao.save(wfDftask);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 *@desc 任务详细信息
	 *@date Nov 5, 2009
	 *@author hebo
	 *@param taskId
	 *@return
	 *@see com.est.workflow.processdefination.service.IWfDfprocessService#getWfDftaskById(java.lang.Long)
	 */
	public WfDftask getWfDftaskById(Long taskId) {
		return wfDftaskDao.findById(taskId);
	}
	
	/**
	 * 
	 *@desc 删除任务
	 *@date Nov 5, 2009
	 *@author hebo
	 *@param taskId
	 * @throws Exception 
	 *@see com.est.workflow.processdefination.service.IWfDfprocessService#delWfDftaskById(java.lang.Long)
	 */
	public void delWfDftaskById(Long taskId) throws Exception {
		try{
			wfDftaskDao.delById(taskId);
		}catch(Exception e){
				e.printStackTrace();
				throw e;
		}
	}

	/**
	 *@desc 读取流程配置json 
	 *@date Nov 5, 2009
	 *@author jingpj
	 *@param processId
	 *@return
	 */
	public String getProcessJson(Long processId) {
		try {
			WfDfprocess process = wfDfprocessDao.findById(processId);
			String processJson = process.getDfjson();
			return processJson;
		} catch (Exception ex) {
			return null;
		}
	}
	
	
}
