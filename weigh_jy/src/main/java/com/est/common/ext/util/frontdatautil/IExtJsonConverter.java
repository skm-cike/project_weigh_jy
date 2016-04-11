package com.est.common.ext.util.frontdatautil;

import net.sf.json.JsonConfig;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 27, 2009
 * @path com.est.common.ext.util
 * @name com.est.common.ext.util.IExtJsonConverter
 * @description 对象转换ext－json 数据接口
 */
public interface IExtJsonConverter {
	
	/**
	 * 
	 *@description
	 *@date May 27, 2009
	 *@author jingpj
	 *12:32:04 PM
	 *@return 返回json对象
	 */
	public String getExtJson(JsonConfig jc);
}
