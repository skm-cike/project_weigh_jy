package com.est.common.ext.util.jsonlibhelper.propertyfilter;

import java.util.HashMap;
import java.util.Set;

/**
 * jsonlib转换黑名单，黑名单中的字段将被过滤
 * @author Administrator
 *
 */
public class BlackListPropertyFilter implements JSONPropertyFilter {

	HashMap<String,String> whitelistMap = new HashMap<String,String>();
	
	public BlackListPropertyFilter(){
		
	}
	
	public BlackListPropertyFilter(String[] fieldNames){
		for(String s : fieldNames){
			whitelistMap.put(s, s);
		}
	}
	
	public void setList(String[] fieldNames){
		for(String s : fieldNames){
			whitelistMap.put(s, s);
		}
	}
	
	public boolean apply(Object obj, String s, Object obj1) {
		
		
		if(obj.getClass().getName().contains("CGLIB")) {
			Set<String> keySet = whitelistMap.keySet();
			for(String key : keySet) {
				if(key.contains(".")){
					String[] fieldsPart = key.split("\\.");
					String className = fieldsPart[0];
					if(key.contains(s) && obj.getClass().getName().contains(className)){
						return true;
					}
				} 
			} 
			return false;
		} else {
			return whitelistMap.containsKey(s);
		}
		
	}

}
