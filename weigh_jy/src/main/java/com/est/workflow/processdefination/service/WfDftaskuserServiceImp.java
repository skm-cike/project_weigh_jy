package com.est.workflow.processdefination.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.workflow.processdefination.dao.IWfDftaskuserDao;
import com.est.workflow.processdefination.vo.WfDftaskuser;

public class WfDftaskuserServiceImp implements IWfDftaskuserService {
	
	private IWfDftaskuserDao wfDftaskuserDao;

	public void setWfDftaskuserDao(IWfDftaskuserDao wfDftaskuserDao) {
		this.wfDftaskuserDao = wfDftaskuserDao;
	}

	@SuppressWarnings("unchecked")
	public Result<WfDftaskuser> getWfDftaskuserList(Page page,SearchCondition condition) {
		
		Long taskId = (Long) condition.get("taskId");
		
		String hql = "from WfDftaskuser t where t.wfDftask.taskId = ? ";
		return wfDftaskuserDao.findByPage(page, hql, taskId);
	}

	public boolean savWfDftaskuserList(EditableGridDataHelper data) {
		List<WfDftaskuser> saveList = new ArrayList<WfDftaskuser>();
		List<WfDftaskuser> delList = new ArrayList<WfDftaskuser>();
		try {
			// 删除
			Iterator<Object> it = data.getDelObjects().iterator();
			while (it.hasNext()) {
				WfDftaskuser taskuser = (WfDftaskuser) it.next();
				WfDftaskuser tmp = new WfDftaskuser();
				tmp.setTaskuserId(taskuser.getTaskuserId());
				delList.add(tmp);
			}
			wfDftaskuserDao.delAll(delList);
			// 添加，更新
			it = data.getSaveObjects().iterator();
			while (it.hasNext()) {
				WfDftaskuser wfDftaskuser = (WfDftaskuser) it.next();
				wfDftaskuser.setGroup(null);
				
				saveList.add(wfDftaskuser);
			}
			wfDftaskuserDao.saveAll(saveList);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			//throw e;
			return false;
		} 
		
	}

	

	
	
	
	
}
