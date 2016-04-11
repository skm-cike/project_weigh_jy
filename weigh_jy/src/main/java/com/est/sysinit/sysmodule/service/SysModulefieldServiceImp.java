package com.est.sysinit.sysmodule.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysmodule.dao.ISysModulefieldDao;
import com.est.sysinit.sysmodule.vo.SysModulefield;

/**
 *@desc 模块对应字段service
 *@author jingpj
 *@date Feb 4, 2010
 *@path com.est.sysinit.sysmodule.service.ISysModulefieldService
 *@corporation Enstrong S&T
 */
public class SysModulefieldServiceImp implements ISysModulefieldService {

	private ISysModulefieldDao sysModulefieldDao;
	
	public void setSysModulefieldDao(ISysModulefieldDao sysModulefieldDao) {
		this.sysModulefieldDao = sysModulefieldDao;
	}

	/**
	 *@desc 查询模块下配置的字段
	 *@date Feb 4, 2010
	 *@author jingpj
	 *@param page
	 *@param moduleId
	 *@return
	 */
	@SuppressWarnings("unchecked")
	public Result<SysModulefield> getFieldListByModuleId(Page page, Long moduleId) {
		String hql = "from SysModulefield t where t.sysModule.moduleid = ? order by t.ordernum";
		Result<SysModulefield> result = sysModulefieldDao.findByPage(page, hql, moduleId);
		return result;
	}

	/**
	 *@desc 保存模块对应字段 
	 *@date Feb 4, 2010
	 *@author jingpj
	 *@param editGridData
	 */
	public void savSysModulefileList(EditableGridDataHelper editGridData) {
		List<SysModulefield> saveList = new ArrayList<SysModulefield>();
		List<SysModulefield> delList = new ArrayList<SysModulefield>();
		
		//删除
		Iterator<Object> it = editGridData.getDelObjects().iterator();
		while(it.hasNext()) {
			SysModulefield obj = (SysModulefield)it.next();
			obj.setSysModule(null);
			delList.add(obj);
		}
		sysModulefieldDao.delAll(delList);
		
		//添加、更新
		it = editGridData.getSaveObjects().iterator();
		while(it.hasNext()) {
			SysModulefield obj = (SysModulefield)it.next();
			if(obj.getFieldid()==0) {
				obj.setFieldid(null);
			}
			if(obj.getSysModule()!=null && (obj.getSysModule().getModuleid()==null ||obj.getSysModule().getModuleid()==0 )){
				obj.setSysModule(null);
			}
			saveList.add(obj);
		}
		sysModulefieldDao.saveAll(saveList);
		
	}

}
