package com.est.sysinit.sysproperty.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysproperty.dao.ISysPropertyDao;
import com.est.sysinit.sysproperty.vo.SysProperty;


/**
 *@desc 属性配置管理---service
 *@author tanhf
 *@date Jul 1, 2009
 *@path com.est.sysinit.sysproperty.service.SysPropertyServiceImp
 *@corporation Enstrong S&T 
 */
public class SysPropertyServiceImp implements ISysPropertyService {
	private static final Object Integer = null;
	private ISysPropertyDao sysPropertyDao;
	
	public ISysPropertyDao getSysPropertyDao() {
		return sysPropertyDao;
	}

	public void setSysPropertyDao(ISysPropertyDao sysPropertyDao) {
		this.sysPropertyDao = sysPropertyDao;
	}

	/**
	 *@desc 
	 *@date Jul 1, 2009
	 *@author tanhf
	 *@param page
	 *@param parentId 属性父级ID，如果=null/0，则查询全部，否则查询指定ID的子属性list
	 *@return
	 *@see com.est.sysinit.sysproperty.service.ISysPropertyService#getParentPropertyList(com.est.common.ext.util.Page, java.lang.Long)
	 */
	public Result<SysProperty> getPropertyListByParentId(Page page, Long parentId)
	{
		List<Object> paramList = new ArrayList<Object>();
		
		String hql = "from SysProperty t where 1=1";
		if(parentId !=null && parentId.longValue() !=0)
		{
			hql += " and t.sysProperty.propertyid = ?";
			paramList.add(parentId);
		}
		else
		{
			hql += " and t.sysProperty.propertyid is null";
		}
		hql += " order by t.ordernum";
		return sysPropertyDao.findByPage(page, hql, paramList.toArray());
		
	}

	/**
	 *@desc 批量添加、修改、删除属性
	 *@date Jul 2, 2009
	 *@author tanhf
	 *@param data
	 *@see com.est.sysinit.sysproperty.service.ISysPropertyService#savPropertyList(com.est.common.ext.util.frontdatautil.EditableGridDataHelper)
	 */
	public void savPropertyList(EditableGridDataHelper data)
	{
		List<SysProperty> saveList = new ArrayList<SysProperty>();
		List<SysProperty> delList = new ArrayList<SysProperty>();
		
		//删除
		Iterator<Object> it = data.getDelObjects().iterator();
		while(it.hasNext()) {
			delList.add((SysProperty)it.next());
		}
		sysPropertyDao.delAll(delList);
		
		//添加、更新
		it = data.getSaveObjects().iterator();
		int i =1;
		while(it.hasNext()) {
			
			SysProperty obj = (SysProperty)it.next();
			
			//判断属性编码唯一性
			if(this.isUniquenessOfPropertyCode(obj.getPropertycode(), obj.getSysProperty()))
			{
				//需要添加重复编码grid的颜色区别
				System.out.println("属性编码重复 第i行=" + i + "行，编码为：" + obj.getPropertycode());
			}
			else
			{
				if(obj.getPropertyid()==0) {
					obj.setPropertyid(null);
				}
				saveList.add(obj);
			}
			i++;
		}
		sysPropertyDao.saveAll(saveList);
		
	}
	
	/**
	 *@desc 属性编码唯一性检查
	 *@date Jul 2, 2009
	 *@author tanhf
	 *@param propertyCode
	 *@return 
	 */
	private boolean isUniquenessOfPropertyCode(String propertyCode, SysProperty parentpro)
	{
		if (parentpro == null || parentpro.getPropertyid() == null || parentpro.getPropertyid() == 0l) {
			String hql ="select count(*) from SysProperty t where t.propertycode=?";
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(propertyCode);
			List list = sysPropertyDao.findByHql(hql, paramList.toArray());
			int cnt = ((Long)list.get(0)).intValue();
			return cnt>0?true:false;
		} else {
			String hql ="select count(*) from SysProperty t where t.propertycode=? and t.sysProperty=?";
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(propertyCode);
			paramList.add(parentpro);
			List list = sysPropertyDao.findByHql(hql, paramList.toArray());
			int cnt = ((Long)list.get(0)).intValue();
			return cnt>0?true:false;
		}
	}

	/**
	 *@desc 通过编码查询该类中的属性列表
	 *@date Aug 5, 2009
	 *@author jingpj
	 *@param propertyCode
	 *@return
	 *@see com.est.sysinit.sysproperty.service.ISysPropertyService#getPropertiesByCode(java.lang.String)
	 */
	public List<SysProperty> getPropertiesByCode(String propertyCode) {
		String hql = "from SysProperty t where t.sysProperty.propertycode=? order by t.ordernum ";
		return sysPropertyDao.findByHql(hql, propertyCode);
	}
	
	
}
