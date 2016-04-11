package com.est.common.ext.util.classutil;

import java.util.ArrayList;
import java.util.List;

public class StatusUtils {

	public static List<String> quantityStatusList = new ArrayList<String>();
	public static List<String> quantityChecktypeList = new ArrayList<String>();
	public static List<String> assayStatusList = new ArrayList<String>();
	public static List<String> roleList=new ArrayList<String>();
	static{
		roleList.add("专工");
		assayStatusList.add("待审核");
		
		quantityChecktypeList.add("检尺");
		quantityChecktypeList.add("过衡");
		quantityChecktypeList.add("未收车");
		
		quantityStatusList.add("待提交");
		quantityStatusList.add("待审核");
		quantityStatusList.add("已审核");
		quantityStatusList.add("驳回");
	}
	
}
