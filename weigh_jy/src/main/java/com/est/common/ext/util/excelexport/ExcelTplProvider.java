package com.est.common.ext.util.excelexport;

import java.io.IOException;

import com.est.common.ext.util.fileutil.PropertiesProvider;

/**
 * 
 *@desc excel模版属性配置解析器
 *@author Administrator
 *@date May 31, 2012
 *@path com.est.common.ext.util.excelexport.ExcelTplProvider
 *@corporation Enstrong S&T
 */
public class ExcelTplProvider {

	/**
	 * excel 报表报表导出模版文件路径
	 */
	private final static String EXCELTPL_CONFIG_FILE = "exceltplpath.properties";

	/**
	 * 
	 * @desc 查询Excel 模版配置文件路径
	 * @date May 31, 2012
	 * @author heb
	 * @param key
	 * @return
	 */
	public static String getExcelTplPath(String key) {

		PropertiesProvider provider = PropertiesProvider.getInstance();

		try {
			provider.load(EXCELTPL_CONFIG_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return provider.getProperty(key);

	}

}
