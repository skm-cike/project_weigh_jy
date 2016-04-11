package com.est.sysinit.sysproperty.action;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysproperty.service.ISysPropertyService;
import com.est.sysinit.sysproperty.vo.SysProperty;


/**
 *@desc 属性配置管理---Action
 *@author tanhf
 *@date Jul 1, 2009
 *@path com.est.sysinit.sysproperty.action.SysPropertyAction
 *@corporation Enstrong S&T 
 */
public class SysPropertyAction extends BaseAction {
	private static final long serialVersionUID = -5236565446298628901L;

	// model object
	private SysProperty property = new SysProperty();

	public void setProperty(SysProperty property) {
		this.property = property;
	}
	
	@Override
	public Object getModel() {
		return property;
	}

	/**
	 *@desc 转向属性配置主页面
	 *@date Jul 1, 2009
	 *@author tanhf
	 *@return 
	 */
	public String fwdProperty() {
		return toJSP("property");
	}

	/**
	 *@desc 获取父级属性list
	 *@date Jul 1, 2009
	 *@author tanhf
	 *@return 
	 */
	public String getPropertyListByParentId(){
		
		String strParentId = req.getParameter("parentPropertyId");
		Long parentId = StringUtil.parseLong(strParentId);
		
		ISysPropertyService service = (ISysPropertyService) getBean("sysPropertyService");
		Result<SysProperty> result = null;
		try{
			result = service.getPropertyListByParentId(getPage(), parentId);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return toJSON(result);
	}
	
	
	/**
	 *@desc 批量提交属性添加、修改、删除
	 *@date Jul 2, 2009
	 *@author tanhf
	 *@return 
	 */
	public String savPropertyList(){
	
		ISysPropertyService service = (ISysPropertyService) getBean("sysPropertyService");
		String data = req.getParameter("data");
		try {
			data = java.net.URLDecoder.decode(data,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		EditableGridDataHelper editGridData = EditableGridDataHelper.data2bean(data, SysProperty.class);
		try {
			service.savPropertyList(editGridData);
			return toSTR("{success:true}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			String ms = null;
			if (e.getLocalizedMessage().contains("org.hibernate.exception.ConstraintViolationException")) {
				ms = "有子记录，无法删除!";
			}
			return toSTR("{success:false,error:'" + ms + "'}");
		}
	}
	
	/**
	 *@desc  通过编码来得到属性列表
	 *@date Aug 5, 2009
	 *@author jingpj
	 *@url  est/sysinit/sysproperty/SysProperty/getPropertiesByCode
	 *@return
	 */
	public String getPropertiesByCode(){
		String propertyCode = property.getPropertycode();
		ISysPropertyService service = (ISysPropertyService) getBean("sysPropertyService");
		List<SysProperty> propertyList = service.getPropertiesByCode(propertyCode);
		if(propertyList.size()>0) {
			return toJSON(propertyList ,"{success:true,rows:","}");
		} else {
			return toSTR("{success:true,rows:[]}");
		}
	}
}
