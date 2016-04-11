package com.est.common.ext.util.frontdatautil;

import java.util.Map;

public class JsonObjectTypeConfig {
	/**
	 * 输出json白名单
	 */
	private Map<String,JsonFieldFormat> includeMap;
	/**
	 * 输出json黑名单
	 */
	private Map<String,Object> excludeMap;
	
	/**
	 * 
	 *@description 添加字段到白名单中
	 *@date May 22, 2009
	 *@author jingpj
	 *2:52:18 PM
	 *@param fieldName
	 *@param format
	 */
	public void addInclude(String fieldName,JsonFieldFormat format){
		includeMap.put(fieldName, format);
	}
	
	/**
	 *@description 添加列表到黑名单中
	 *@date May 22, 2009
	 *@author jingpj
	 *2:52:50 PM
	 *@param fieldName
	 */
	public void addExclude(String fieldName){
		excludeMap.put(fieldName, null);
	}
	
	/**
	 * 
	 *@description 得到json-lib处理的
	 *@date May 22, 2009
	 *@author jingpj
	 *3:33:11 PM
	 *@param o
	 *@return
	 */
	public Map getTransferMap(Object o){
		o.getClass();
		return null;
	}
	
	
	
	
	
	
	
	
	

}
