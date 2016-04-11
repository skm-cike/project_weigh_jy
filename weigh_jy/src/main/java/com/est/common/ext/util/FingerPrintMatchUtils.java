package com.est.common.ext.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 
 * @desc 指纹识别匹配工具
 * @author heb
 * @date Nov 25, 2012
 * @path com.est.common.ext.util.FingerPrintMatchUtils
 * @corporation Enstrong S&T
 */
public class FingerPrintMatchUtils {
	
	static Log log = LogFactory.getLog(FingerPrintMatchUtils.class);

	/**
	 * 
	 * @desc 指纹对比工具
	 * @date Nov 25, 2012
	 * @author heb
	 * @param src
	 *            原指纹特征码
	 * @param dst
	 *            对比指纹特征码
	 * @return 成功/失败
	 */
	public static synchronized String match2FB(String src, String dst) {
		
		String res = "失败";
		try{
			
			ComThread.InitSTA();
			
			
			// Finger包对应的注册表中的注册名称
			ActiveXComponent activeX = new ActiveXComponent("ZAZFingerActivexU.FingerX");
			// 返回对比结果
			Variant var = Dispatch.call(activeX, "ZAZMatch", src.toString(), dst.toString());
			// 对比值大于50则比对成功
			if (var != null && var.getInt() > 50) {
				res = "成功";
			}
			
			ComThread.Release();
			
		}catch(Exception e){
			log.error(e.getMessage());
		}
		
		return res;

	}

}
