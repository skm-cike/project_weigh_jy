package com.est.common.ext.util.jsonlibhelper.propertyfilter;

import java.util.HashMap;
import java.util.Set;

/**
 * jsonlib转换白名单，白名单中的字段将被转换成json，其他的field被过滤
 * @author Administrator
 *
 */
public class WhiteListPropertyFilter implements JSONPropertyFilter {

	HashMap<String,String> whitelistMap = new HashMap<String,String>();
	
	public WhiteListPropertyFilter(){
		
	}
	
	public WhiteListPropertyFilter(String[] fieldNames){
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
						return false;
					}
				} 
			} 
			return true;
		} else {
			return !whitelistMap.containsKey(s);
		}
		
		
	}

}
