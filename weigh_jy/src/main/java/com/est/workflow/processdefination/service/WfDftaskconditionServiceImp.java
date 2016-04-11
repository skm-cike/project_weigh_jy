package com.est.workflow.processdefination.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.workflow.processdefination.dao.IWfDftaskconditionDao;
import com.est.workflow.processdefination.vo.WfDftaskcondition;

public class WfDftaskconditionServiceImp implements IWfDftaskconditionService {
	
	private IWfDftaskconditionDao wfDftaskconditionDao;

	public void setWfDftaskconditionDao(IWfDftaskconditionDao wfDftaskconditionDao) {
		this.wfDftaskconditionDao = wfDftaskconditionDao;
	}
	
	/**
	 * 
	 *@desc 获取任务条件列表
	 *@date Nov 3, 2009
	 *@author jingpj
	 *@param condition
	 *@param page
	 *@return
	 *@throws Exception
	 *@see com.est.workflow.processdefination.service.IWfDftaskconditionService#getWfDftaskconditionListByPage(com.est.common.ext.util.SearchCondition, com.est.common.ext.util.Page)
	 */
	public Result<WfDftaskcondition> getWfDftaskconditionListByPage(
			SearchCondition condition,Page page) throws Exception {
		try{
			
			Long taskId=(Long)condition.get("taskId");
			String hql="from WfDftaskcondition t where t.wfDftask.taskId=?";
			
			return wfDftaskconditionDao.findByPage(page,hql, taskId);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
	}
    
	/**
	 * 
	 *@desc 批量删除、保存列表信息
	 *@date Nov 3, 2009
	 *@author jingpj
	 *@param data
	 * @throws Exception 
	 *@see com.est.workflow.processdefination.service.IWfDftaskconditionService#savWfDftaskconditionList(com.est.common.ext.util.frontdatautil.EditableGridDataHelper)
	 */
	public void savWfDftaskconditionList(EditableGridDataHelper data) throws Exception {
		List<WfDftaskcondition> saveList = new ArrayList<WfDftaskcondition>();
		List<WfDftaskcondition> delList = new ArrayList<WfDftaskcondition>();
		try {
			// 删除
			Iterator<Object> it = data.getDelObjects().iterator();
			while (it.hasNext()) {
				delList.add((WfDftaskcondition) it.next());
			}
			wfDftaskconditionDao.delAll(delList);
			
			// 添加，更新
			it = data.getSaveObjects().iterator();
			while (it.hasNext()) {
				WfDftaskcondition wfDftaskcondition = (WfDftaskcondition) it.next();
				
				if(wfDftaskcondition.getTaskconditionId()!=null && wfDftaskcondition.getTaskconditionId()==0){
					wfDftaskcondition.setTaskconditionId(null);
				}

				saveList.add(wfDftaskcondition);
			}
			wfDftaskconditionDao.saveAll(saveList);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} 
		
	}
	
	
	
}
