package com.est.sysinit.sysproperty.service;

import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysproperty.vo.SysProperty;


/**
 *@desc 属性配置管理---service
 *@author tanhf
 *@date Jul 1, 2009
 *@path com.est.sysinit.sysproperty.service.ISysPropertyService
 *@corporation Enstrong S&T 
 */
public interface ISysPropertyService {

	/**
	 *@desc 
	 *@date Jul 1, 2009
	 *@author tanhf
	 *@param page
	 *@param parentId 属性父级ID，如果=null，则查询全部，否则查询指定ID的子属性list
	 *@return 
	 */
	public Result<SysProperty> getPropertyListByParentId(Page page, Long parentId);

	/**
	 *@desc 批量添加、修改、删除属性
	 *@date Jul 2, 2009
	 *@author tanhf
	 *@param data 
	 */
	public void savPropertyList(EditableGridDataHelper data);

	/**
	 *@desc 通过编码查询该类中的属性列表
	 *@date Aug 5, 2009
	 *@author jingpj
	 *@param propertyCode
	 *@return
	 */
	public List<SysProperty> getPropertiesByCode(String propertyCode);

	
}
