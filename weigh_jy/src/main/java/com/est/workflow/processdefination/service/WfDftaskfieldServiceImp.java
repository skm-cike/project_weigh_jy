package com.est.workflow.processdefination.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.workflow.processdefination.dao.IWfDftaskfieldDao;
import com.est.workflow.processdefination.vo.WfDftaskfield;

public class WfDftaskfieldServiceImp implements IWfDftaskfieldService {
	
	private IWfDftaskfieldDao wfDftaskfieldDao;

	public void setWfDftaskfieldDao(IWfDftaskfieldDao wfDftaskfieldDao){
		this.wfDftaskfieldDao = wfDftaskfieldDao;
	}
	
	/**
	 *@desc 分页查询任务关联的字段列表
	 *@date Feb 5, 2010
	 *@author jingpj
	 *@param page
	 *@param condition
	 *@return
	 *@see com.est.workflow.processdefination.service.IWfDftaskfieldService#getWfDftaskfieldList(com.est.common.ext.util.Page, com.est.common.ext.util.SearchCondition)
	 */
	@SuppressWarnings("unchecked")
	public Result<WfDftaskfield> getWfDftaskfieldList(Page page,SearchCondition condition) {
		
		Long taskId = (Long) condition.get("taskId");
		String hql = "from WfDftaskfield t where t.wfDftask.taskId = ? ";
		return wfDftaskfieldDao.findByPage(page, hql, taskId);
		
	}
	
	

	/**
	 *@desc 保存任务关联的字段列表
	 *@date Feb 5, 2010
	 *@author jingpj
	 *@param data
	 *@return
	 *@see com.est.workflow.processdefination.service.IWfDftaskfieldService#savWfDftaskfieldList(com.est.common.ext.util.frontdatautil.EditableGridDataHelper)
	 */
	public boolean savWfDftaskfieldList(EditableGridDataHelper data) {
		
		List<WfDftaskfield> saveList = new ArrayList<WfDftaskfield>();
		List<WfDftaskfield> delList = new ArrayList<WfDftaskfield>();
		try {
			
			// 删除
			Iterator<Object> it = data.getDelObjects().iterator();
			while (it.hasNext()) {
				WfDftaskfield taskfield = (WfDftaskfield) it.next();
				taskfield.setSysModulefield(null);
				taskfield.setWfDftask(null);
				delList.add(taskfield);
			}
			wfDftaskfieldDao.delAll(delList);
			
			// 添加，更新
			it = data.getSaveObjects().iterator();
			while (it.hasNext()) {
				WfDftaskfield taskfield = (WfDftaskfield) it.next();
				saveList.add(taskfield);
			}
			wfDftaskfieldDao.saveAll(saveList);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			//throw e;
			return false;
		} 
		
	}

	

	
	
	
	
}
