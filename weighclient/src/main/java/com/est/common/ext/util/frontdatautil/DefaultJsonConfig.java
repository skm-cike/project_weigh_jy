package com.est.common.ext.util.frontdatautil;

import java.util.Date;
import java.util.List;
import java.util.Set;

import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date Jun 3, 2009
 * @path com.est.common.ext.util.frontdatautil
 * @name com.est.common.ext.util.frontdatautil.DefaultJsonConfig
 * @description Json-lib 转换 默认配置
 */
public class DefaultJsonConfig extends JsonConfig{

	public DefaultJsonConfig(){
		// json-lib默认不支持java.sql.Date的序列化，要序列化自己的类，实现一个BeanProcessor处理即可
        this.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));  
        
		
        this.setCycleDetectionStrategy(CycleDetectionStrategy.NOPROP);
		this.setJsonPropertyFilter(new PropertyFilter(){
			public boolean apply(Object source, String name, Object value) {
				 
				if( value instanceof Set || value instanceof List){
					return true;  
				} 
				if("hibernateLazyInitializer".equals(name)){
					return true;
				}
				if(value != null && ((source+"").contains("cglib") ||(source+"").contains("Session") ) ){
					return true;
				}
				return false;
			}
		});
		
		
		//去掉Hibernate延迟加载造成的警告
		this.setExcludes(new String[]{"handler","hibernateLazyInitializer"});
		
	}
}
