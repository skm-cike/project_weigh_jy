package com.est.sysinit.sysfilemanage.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysfilemanage.dao.ISysTemplateparamDao;
import com.est.sysinit.sysfilemanage.vo.SysTemplateparam;

/**
 *@desc 模板参数管理service
 *@author zhanglk
 *@date Jun 29, 2009
 *@path com.est.sysinit.sysfilemanage.service.SysTemplateparamServiceImp
 *@corporation Enstrong S&T 
 */
public class SysTemplateparamServiceImp implements ISysTemplateparamService{

	private ISysTemplateparamDao sysTemplateparamDao;
	
	public void setSysTemplateparamDao(ISysTemplateparamDao sysTemplateparamDao){
		this.sysTemplateparamDao = sysTemplateparamDao;
	}
	/**
	 * 分页查询模板参数list
	 *@desc 
	 *@date Jun 29, 2009
	 *@author zhanglk
	 *@param page
	 *@param sc
	 *@return
	 */
	public Result<SysTemplateparam> getTemplateparamResult(Page page,SearchCondition sc){
		StringBuffer hql = new StringBuffer(100);
		Long templateId = StringUtil.parseLong((String)sc.get("templateid"));
		List<Object> paramList = new ArrayList<Object>();
		hql.append("from SysTemplateparam t ");
		
		if(templateId!=null && templateId!=0){
			hql.append(" where t.sysTemplate.templateid=?");
			paramList.add(templateId);
		}
		hql.append(" order by t.paramorder");
		return sysTemplateparamDao.findByPage(page, hql.toString(),paramList.toArray());
	}
	/**
	 * 
	 *@desc 保存可编辑grid数据  
	 *@date Jun 29, 2009
	 *@author zhanglk
	 *@param changeData
	 */
	public void savChanges(EditableGridDataHelper changeData){
		List<SysTemplateparam> saveList = new ArrayList<SysTemplateparam>();
		List<SysTemplateparam> delList = new ArrayList<SysTemplateparam>();
		Iterator<Object> it = changeData.getSaveObjects().iterator();
		while(it.hasNext()) {
			SysTemplateparam sysTemplateparam = (SysTemplateparam)it.next();
			if(sysTemplateparam.getParamid()==0) {
				sysTemplateparam.setParamid(null);
			}
			saveList.add(sysTemplateparam);
		}
		sysTemplateparamDao.saveAll(saveList);
		
		it = changeData.getDelObjects().iterator();
		while(it.hasNext()) {
			delList.add((SysTemplateparam)it.next());
		}
		sysTemplateparamDao.delAll(delList);
	}
	
	/**
	 * 
	 *@desc 得到模板的参数列表 
	 *@date Jul 20, 2009
	 *@author jingpj
	 *@param templateId
	 *@return
	 */
	public List<SysTemplateparam> getTemplateparamsById(Long templateId){
		StringBuffer hql = new StringBuffer(100);
		List<Object> paramList = new ArrayList<Object>();
		hql.append("from SysTemplateparam t ");
		
		if(templateId!=null && templateId!=0){
			hql.append(" where t.sysTemplate.templateid=?");
			paramList.add(templateId);
		}
		hql.append(" order by t.paramorder");
		return sysTemplateparamDao.findByHql(hql.toString(),paramList.toArray());
	}
}
