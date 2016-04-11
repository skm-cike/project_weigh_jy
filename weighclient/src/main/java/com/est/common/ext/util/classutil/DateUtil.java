package com.est.common.ext.util.classutil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * date & time utils
 * 
 * @author jingpj
 * @version 0.1
 */
public class DateUtil {

	private static final Log log = LogFactory.getLog(DateUtil.class);

	public final static String YMD = "yyyy-MM-dd";

	public final static String YMDHMS = "yyyy-MM-dd HH:mm:ss";

	public static final String YMDHM = "yyyy-MM-dd HH:mm";

	public final static int ERA = 0;

	public final static int YEAR = 1;

	public final static int MONTH = 2;

	public final static int WEEK_OF_YEAR = 3;

	public final static int WEEK_OF_MONTH = 4;

	public final static int DATE = 5;

	public final static int DAY_OF_MONTH = 5;

	public final static int DAY_OF_YEAR = 6;

	public final static int DAY_OF_WEEK = 7;

	public final static int DAY_OF_WEEK_IN_MONTH = 8;

	public final static int HOUR = 10;

	public final static int HOUR_OF_DAY = 11;

	public final static int MINUTE = 12;

	public final static int SECOND = 13;

	public final static int MILLISECOND = 14;

	// 账务月映射

	private final static Map<String, String> ACCOUNT_MONTH_MAPPING = new HashMap<String, String>();

	static {

		ACCOUNT_MONTH_MAPPING.put("1", "1-25");
		ACCOUNT_MONTH_MAPPING.put("2", "26-25");
		ACCOUNT_MONTH_MAPPING.put("3", "26-25");
		ACCOUNT_MONTH_MAPPING.put("4", "26-25");
		ACCOUNT_MONTH_MAPPING.put("5", "26-25");
		ACCOUNT_MONTH_MAPPING.put("6", "26-25");
		ACCOUNT_MONTH_MAPPING.put("7", "26-25");
		ACCOUNT_MONTH_MAPPING.put("8", "26-25");
		ACCOUNT_MONTH_MAPPING.put("9", "26-25");
		ACCOUNT_MONTH_MAPPING.put("10", "26-25");
		ACCOUNT_MONTH_MAPPING.put("11", "26-25");
		ACCOUNT_MONTH_MAPPING.put("12", "26-31");

	}

	/**
	 * 将日期转换为字符串，格式为"yyyy-MM-dd"
	 * 
	 * @param date
	 *            转换的日期
	 * @return 日期字符串
	 */
	public static String format(Date date) {
		return DateUtil.format(date, DateUtil.YMD);
	}

	/**
	 * 将日期转换为字符串，格式为"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param date
	 *            转换的日期
	 * @return 日期字符串
	 */
	public static String formatTime(Date date) {
		return DateUtil.format(date, DateUtil.YMDHMS);
	}

	/**
	 * 将日期转换为字符串，可自定义格式
	 * 
	 * @param date
	 *            转换的日期
	 * @param pattern
	 *            转换格式
	 * @return 日期字符串
	 */
	public static String format(Date date, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		if (date != null) {
			return df.format(date);
		}
		return null;
	}

	/**
	 * 将字符串转换为日期，格式为"yyyy-MM-dd"
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String strDate) {
		return parse(strDate, DateUtil.YMD);
	}

	/**
	 * 将字符串转换为日期，格式为"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTime(String strDate) {
		return parse(strDate, DateUtil.YMDHMS);
	}

	/**
	 * 将字符串转换为日期，自定义格式
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String strDate, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.parse(strDate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 判断 d1是否早于d2
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean before(Date d1, Date d2) {
		return d1.before(d2);
	}

	/**
	 * 判断 d1是否晚于d2
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean after(Date d1, Date d2) {
		return d1.after(d2);
	}

	/**
	 * 设置日期某一部分的值
	 * 
	 * @param date
	 *            日期
	 * @param field
	 *            日期改变部分,如DateUtil.YEAR,DateUtil.MONTH。。。
	 * @param value
	 *            设置的值
	 * @return
	 */
	public static Date set(Date date, int field, int value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(field, value);
		return cal.getTime();
	}

	/**
	 * 增加/减少日期某一部分的值
	 * 
	 * @param date
	 *            日期
	 * @param field
	 *            日期改变部分,如DateUtil.YEAR,DateUtil.MONTH。。。
	 * @param amount
	 *            增加/减少的值:正数为增加，负数为减少
	 * @return
	 */
	public static Date add(Date date, int field, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, amount);
		return cal.getTime();
	}

	/**
	 * 取得日期某一部分最大可能的数值（如某一月的最大天数）
	 * 
	 * @param date
	 *            日期
	 * @param field
	 *            日期部分，如DateUtil.YEAR,DateUtil.MONTH。。。
	 * @return
	 */
	public static int getMaximum(Date date, int field) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getMaximum(field);
	}

	/**
	 * 通过设置年月日来得到日期
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static Date getDate(int year, int month, int date) {
		return getDate(year, month, date, 0, 0, 0);
	}

	/**
	 * 通过设置年,月,日,小时,分来得到日期
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @param hourOfDay
	 * @param minute
	 * @return
	 */

	public static Date getDate(int year, int month, int date, int hourOfDay,
			int minute) {
		return getDate(year, month, date, hourOfDay, minute, 0);
	}

	/**
	 * 通过设置年,月,日,小时,分，秒来得到日期
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @param hourOfDay
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Date getDate(int year, int month, int date, int hourOfDay,
			int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month, date, hourOfDay, minute, second);
		return cal.getTime();
	}

	/**
	 * 
	 * @desc 得到星期
	 * @date 2011-6-14
	 * @author LuHua
	 * @return
	 */
	public static Integer getWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int i = cal.get(Calendar.DAY_OF_WEEK);
		if (i != 1) {
			i--;
		} else {
			i = 7;
		}
		return i;
	}

	/**
	 * 
	 * @desc 得到账务月
	 * @date Aug 3, 2012
	 * @author heb
	 * @param date[] :
	 *            date[0] 开始日期，date[1]截至日期
	 * @return
	 */
	public static Date[] getAccountMonth(Date date) {

		Date[] months = new Date[2];

		int month = StringUtil.parseInt(DateUtil.format(date, "MM"));
		String year = DateUtil.format(date, "yyyy");

		String monthPattenstr = ACCOUNT_MONTH_MAPPING
				.get(String.valueOf(month));

		String[] monthPatten = monthPattenstr.split("-");
		int beginDay = StringUtil.parseInt(monthPatten[0]);
		int endDay = StringUtil.parseInt(monthPatten[1]);
		Date preDate = null;
		if (beginDay == 26) {

			preDate = DateUtil.add(date, DateUtil.MONTH, -1);
			preDate = DateUtil.parse(DateUtil.format(preDate, "yyyy-MM") +"-" +26);

			months[0] = preDate;
			months[1] = DateUtil.parse(year +"-"+month +"-"+ endDay);

		} else {

			preDate = DateUtil.parse(DateUtil.format(date, "yyyy-MM")
					+"-"+ beginDay);
			months[0] = preDate;
			preDate = DateUtil.parse(DateUtil.format(preDate, "yyyy-MM")
					+"-"+ endDay);
			months[1] = DateUtil.parse(year +"-"+month +"-"+ endDay);

		}

		return months;

	}

	/**
	 * 
	 * @desc 返回字符串形式的开始截至日期
	 * @date Aug 3, 2012
	 * @author heb
	 * @param strDate
	 * @return
	 */
	public static String[] getAccountMonth(String strDate) {

		Date date = DateUtil.parse(strDate);

		Date[] months = getAccountMonth(date);

		return new String[] { DateUtil.format(months[0]),
				DateUtil.format(months[1]), };

	}
	
	
	public static Date getLastDayOfMonth(String firstDay){
		Calendar cal = Calendar.getInstance(); 
		
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(firstDay);
			cal.setTime(date); 
			cal.set(Calendar.DAY_OF_MONTH, 1); 
			cal.roll(Calendar.DAY_OF_MONTH, -1); 
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return cal.getTime();
		
	}
	
	  public static XMLGregorianCalendar convertToXmlGreCal(Calendar calendar) {
		    XMLGregorianCalendar cal = new XMLGregorianCalendarImpl();
		    cal.setYear(calendar.get(1));
		    cal.setMonth(calendar.get(2) + 1);
		    cal.setDay(calendar.get(5));
		    cal.setHour(calendar.get(11));
		    cal.setMinute(calendar.get(12));
		    cal.setSecond(calendar.get(13));
		    cal.setMillisecond(calendar.get(14));
		    cal.setTimezone(calendar.get(15) / 60000);
		    return cal;
		  }

      public static XMLGregorianCalendar convertToXmlGreCal(Date date) {
        if(date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return convertToXmlGreCal(calendar);
      }
}
