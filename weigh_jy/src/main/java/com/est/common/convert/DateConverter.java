package com.est.common.convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.est.common.ext.util.classutil.DateUtil;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 30, 2009
 * @path com.est.common.convert
 * @name com.est.common.convert.DateConverter
 * @description 拦截时的日期转化类
 */
public class DateConverter extends StrutsTypeConverter {

	public static final String YMD = "yyyy-MM-dd";
	public static final String YMDHM = "yyyy-MM-dd HH:mm";
	public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
	
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		Date date = null;
		String dateString = null;
		if (values != null && values.length > 0) {
			dateString = values[0];
				if(dateString!=null && !"".equals(dateString)){
				try {
					SimpleDateFormat format = new SimpleDateFormat(YMDHMS);
					date = format.parse(dateString);
				} catch (ParseException e) {
					try {
						SimpleDateFormat format = new SimpleDateFormat(YMDHM);
						date = format.parse(dateString);
					} catch (ParseException e1) {
						SimpleDateFormat format = new SimpleDateFormat(YMD);
						try {
							date = format.parse(dateString);
						} catch (ParseException e2) {
							e2.printStackTrace();
							date = null;
						}
					}
				}
			}
		}
		return date;

		
	}

	@Override
	public String convertToString(Map context, Object o) {
		// 格式化为date格式的字符串
		if(o!=null) {
			Date date = (Date) o;
			String dateTimeString = DateUtil.format(date, DateUtil.YMDHMS);
			return dateTimeString;
		} else {
			return "";
		}
	}

}
