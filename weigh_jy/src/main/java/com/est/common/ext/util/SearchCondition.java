package com.est.common.ext.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SearchCondition {
	
	protected HashMap<String,Object> conditions ;
	
	
	
	public SearchCondition(){
		conditions = new HashMap<String, Object>();
	}
	
	
	public SearchCondition(Map<String,String[]> params){
		conditions = new HashMap<String,Object>(params);
	}
	
	public void set(String key,Object value) {
		this.conditions.put(key, value);
	}
	
	public Object get(String key){
		return this.conditions.get(key);
	}
	
	public boolean contains(String key){
		return this.conditions.containsKey(key);
		
	}
	
	/**
	 *@desc  得到条件名称迭代器
	 *@date Aug 11, 2009
	 *@author jingpj
	 *@return
	 */
	public Iterator<String> getConditionNameIterator(){
		return this.conditions.keySet().iterator();
	}
	
	
	
	
}
