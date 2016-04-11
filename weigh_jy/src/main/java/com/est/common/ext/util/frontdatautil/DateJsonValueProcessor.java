package com.est.common.ext.util.frontdatautil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;


/**
 * @corporation Enstrong S&T
 * @name com.est.common.ext.util.core.DateJsonValueProcessor
 * @description 扩展json－lib 的日期转换
 */
public class DateJsonValueProcessor implements JsonValueProcessor{
	     public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";  
	     private DateFormat dateFormat;  
	   
	     /** 
	      * 构造方法. 
	      * 
	      * @param datePattern 日期格式 
	      */  
	     public DateJsonValueProcessor(String datePattern) {  
	         try {  
	             dateFormat = new SimpleDateFormat(datePattern);  
	         } catch (Exception ex) {  
	             dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);  
	         }  
	     }  
	   
	     public Object processArrayValue(Object value, JsonConfig jsonConfig) {  
	         return process(value);  
	     }  
	   
	     public Object processObjectValue(String key, Object value,  
	         JsonConfig jsonConfig) {  
	         return process(value);  
	     }  
	   
	     private Object process(Object value) {  
	         try{
	        	 return dateFormat.format((Date) value);
	         } catch(Exception e) {
	        	 return null;
	         }
	         
	     }  
}
