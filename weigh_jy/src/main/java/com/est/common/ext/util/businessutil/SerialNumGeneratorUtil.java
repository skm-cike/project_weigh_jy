package com.est.common.ext.util.businessutil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.est.common.base.IBaseDao;
import com.est.common.ext.util.classutil.StringUtil;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 29, 2009
 * @path com.est.common.ext.util.businessutil
 * @name com.est.common.ext.util.businessutil.SerialNumGeneratorUtil
 * @description 序列（流水）号生成工具类
 */
@SuppressWarnings("unchecked")
public class SerialNumGeneratorUtil {
	
	/**
	 * 
	 *@description 根据类名、字段名、流水号前部分、流水号长度生成编码
	 *@date May 29, 2009
	 *@author jingpj
	 *11:05:04 AM
	 *@param dao 			供查询使用的Dao
	 *@param clazz 			类名
	 *@param fieldName		字段
	 *@param prefix   		流水号前部分
	 *@param SearialLength 	流水号长度
	 *@return 
	 * @throws Exception 如果数据库查询失败，抛出异常
	 */
	
	public String getSerialNum(IBaseDao dao ,String clazz,String fieldName,String prefix,int serialLength) throws Exception{
		
		String serialNum = null;
		List<String> list = null;
		
		try {
			//得到原字段，流水号最大的编号
			list = getDataList(dao, clazz, fieldName, prefix,serialLength);
		} catch (Exception ex) {
			throw ex;
		}
		if(list== null || list.size() == 0 || list.get(0) == null) {
			//如果原来没有流水号，默认以[1]开始
			serialNum = "1";
		} else {
			//将最大的流水号，加1
			String maxstr = list.get(0);
			maxstr = maxstr.substring(prefix==null?0:prefix.length());
			Long num = StringUtil.parseLong(maxstr, 0L);
			num++;
			serialNum = String.valueOf(num);
		}
		
		//在流水号开始补齐位数
		serialNum = StringUtil.fixLength(serialNum, "0", serialLength, StringUtil.BEGIN);
		return (prefix==null?"":prefix) + serialNum;
	}
	
	/**
	 * 
	 *@description 根据类名、字段名、日期格式、流水号长度生成编码
	 *@date May 29, 2009
	 *@author jingpj
	 *11:05:04 AM
	 *@param dao 			供查询使用的Dao
	 *@param clazz 			类名
	 *@param fieldName		字段
	 *@param dateFormat   	流水号前部分日期的格式
	 *@param SearialLength 	流水号长度
	 *@return 
	 * @throws Exception 如果数据库查询失败，抛出异常
	 */
	public String getDateSerialNum(IBaseDao dao ,String clazz,String fieldName,String dateFormat,int serialLength) throws Exception{
		DateFormat df = new SimpleDateFormat(dateFormat);
		String prefix = df.format(new Date());
		return getSerialNum(dao, clazz, fieldName, prefix, serialLength);
	}	
	
	/**
	 * 
	 *@description 根据类名、字段名、日期格式、流水号长度生成编码
	 *@date May 29, 2009
	 *@author jingpj
	 *11:05:04 AM
	 *@param dao 			供查询使用的Dao
	 *@param clazz 			类名
	 *@param fieldName		字段
	 *@param dateFormat   	流水号前部分日期的格式
	 *@param SearialLength 	流水号长度
	 *@return 
	 * @throws Exception 如果数据库查询失败，抛出异常
	 */
	public String getDateSerialNum(IBaseDao dao ,String clazz,String fieldName,String prefix,String dateFormat,int serialLength) throws Exception{
		DateFormat df = new SimpleDateFormat(dateFormat);
		prefix = prefix==null?"":prefix;
		prefix += df.format(new Date());
		return getSerialNum(dao, clazz, fieldName, prefix, serialLength);
	}	
	
	/**
	 * 
	 *@description 查询最大流水号
	 *@date May 29, 2009
	 *@author jingpj
	 *2:31:05 PM
	 *@param dao
	 *@param clazz
	 *@param fieldName
	 *@param pattern
	 *@param serialLength
	 *@return
	 *@throws Exception
	 */
	private List<String> getDataList(IBaseDao dao ,String clazz,String fieldName,String pattern,int serialLength) throws Exception{
		try{
			StringBuffer buf = new StringBuffer(200);
			buf.append("SELECT max(" + fieldName + ") from " + clazz + " where ");
			if(pattern == null) { //如果pattern为null，表示为第一级，查询编码长度＝searialLength的
				buf.append("length("+fieldName + ") =  " + serialLength);
			} else { //否则查询编码llike pattern
				buf.append(fieldName + " like '" + pattern + "%'");
				buf.append(" and length("+fieldName+") = " + (pattern.length()+serialLength));
			}
			
			List<String> list = dao.findByHql(buf.toString());
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("query failed:"+ex.getMessage());
		}
	}
}
