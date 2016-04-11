package com.est.common.ext.util.jsonlibhelper.propertyfilter;

/**
 * JSONPropertyFilter工厂类
 * @author Administrator
 *
 */
public class JSONPropertyFilterFactory {
	
	public static JSONPropertyFilter getFilter(String filtername,String[] fields){
		if(JSONPropertyFilter.BLACKLIST.equals(filtername)){
			return new BlackListPropertyFilter(fields);
		} else if(JSONPropertyFilter.WHITELIST.equals(filtername)){
			return new WhiteListPropertyFilter(fields);
		} else {
			return null;
		}
	}
}
