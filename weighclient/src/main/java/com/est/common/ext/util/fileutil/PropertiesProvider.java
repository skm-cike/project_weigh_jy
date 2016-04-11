/**
 *@desc
 *@author Administrator
 *@date May 31, 2012
 *@path com.est.common.ext.util.excelexport.ExcelTplProvider
 *@corporation Enstrong S&T 
 */
package com.est.common.ext.util.fileutil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @desc 资源文件路径解析器
 * @author Administrator
 * @date May 31, 2012
 * @path com.est.common.ext.util.excelexport.ExcelTplProvider
 * @corporation Enstrong S&T
 */
public class PropertiesProvider {

	private Properties pro = new Properties();
	private static PropertiesProvider provider = null;
	/**
	 * 
	 *@desc 单例服务
	 *@date May 31, 2012
	 *@author heb
	 *@return
	 */
	public synchronized static PropertiesProvider getInstance() {

		if (provider == null) {
			provider = new PropertiesProvider();
		}

		return provider;
	}

	private PropertiesProvider() {

	}

	/**
	 * 
	 * @desc 查询Excel 模版配置文件路径
	 * @date May 31, 2012
	 * @author heb
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return pro.getProperty(key);
	}

	/**
	 * 
	 * @desc 载入资源文件
	 * @date May 31, 2012
	 * @author heb
	 * @param propertiesName
	 */
	public void load(String propertiesName) throws IOException {

		InputStream inStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(propertiesName);

		pro.load(inStream);

	}

}
