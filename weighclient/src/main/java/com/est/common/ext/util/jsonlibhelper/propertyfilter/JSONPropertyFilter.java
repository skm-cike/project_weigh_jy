package com.est.common.ext.util.jsonlibhelper.propertyfilter;

import net.sf.json.util.PropertyFilter;

public interface JSONPropertyFilter extends PropertyFilter {
	//黑名单
	public static final String BLACKLIST = "blacklist";
	//白名单
	public static final String WHITELIST = "whitelist";
	
	//设置名单
	public void setList(String[] fieldNames);
}
