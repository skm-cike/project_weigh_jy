package com.est.common.ext.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.est.common.ext.util.fileutil.FileClearCycleTimerTask;

/**
 *@desc init system configuration of web context
 *@author jingpj
 *@date Jul 30, 2009
 *@path com.est.common.ext.util.InitSystem
 *@corporation Enstrong S&T
 */
public class InitSystem extends HttpServlet {

	private static String systemPath ;  //系统部署路径
	
	
	
	public static String getSystemPath() {
		return systemPath;
	}

	public static void setSystemPath(String systemPath) {
		InitSystem.systemPath = systemPath;
	}


	/**
	 * Constructor of the object.
	 */
	public InitSystem() {
		super();
		
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}


	

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		ContextUtil.setContext(getServletContext());
		FileClearCycleTimerTask.setContext(this.getServletContext());
		InitSystem.systemPath = this.getServletContext().getRealPath("\\");
		
		
	}

}
