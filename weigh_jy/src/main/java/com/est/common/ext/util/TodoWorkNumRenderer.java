package com.est.common.ext.util;
/**
 * 
 *@desc 格式话数字工具类
 *@author hebo
 *@date Mar 16, 2010
 *@path com.est.common.ext.util.TodoWorkNumRenderer
 *@corporation Enstrong S&T
 */
public class TodoWorkNumRenderer {
	/**
	 * 
	 *@desc 格式化数字
	 *@date Mar 16, 2010
	 *@author hebo
	 *@param num
	 *@return
	 */	
	public static String render(Long num) {
		long numL=num.longValue();
		if (num == 0) {
			return "";
		} else if (num < 10) {
			return "(<font color=green>" + numL + "</font>)";
		} else {
			return "(<font color=red>" + numL + "</font>)";
		}
	}
	
}
