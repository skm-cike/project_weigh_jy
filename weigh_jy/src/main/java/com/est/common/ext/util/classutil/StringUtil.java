package com.est.common.ext.util.classutil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 27, 2009
 * @path com.est.common.ext.util.classutil
 * @name com.est.common.ext.util.classutil.StringUtil
 * @description 字符串处理帮助类
 */
public class StringUtil {
	
	private static final Log log = LogFactory.getLog(StringUtil.class);
	
	public final static String NUM = "0123456789";
	public final static String LOWERLETTER = "abcdefghijklmnopqrstuvwxyz";
	public final static String UPPERLETTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public final static String NUM_LETTER = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public final static int BEGIN = 0;
	public final static int END = 1;
	
	public static boolean isEmpty(String str){
		return str==null||"".equals(str);
	}
	
	public static String reverse(String str){
		char[] chars = str.toCharArray();
		int left  = 0;         
		int right = chars.length-1; 
		  
		while (left < right) {
		      char temp = chars[left]; 
		      chars[left]  = chars[right]; 
		      chars[right] = temp;
		      left++;
		      right--;
		}
		return new String(chars);
	}
	
	public static Integer parseInt(String str , Integer def){
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			return def;
		}
	}
	
	public static Long parseLong(String str, Long def){
		try{
			return Long.parseLong(str);
		} catch (NumberFormatException ex){
			return def;
		}
	}
	
	public static Float parseFloat(String str, Float def){
		try{
			return Float.parseFloat(str);
		} catch (NumberFormatException ex){
			return def;
		}
	}
	
	public static Double parseDouble(String str, Double def){
		try{
			if(str==null){
				return def;
			}
			return Double.parseDouble(str);
		} catch (NumberFormatException ex){
			return def;
		}
	}

	public static int parseInt(String str){
		try{
			
			return parseInt(str,0);
			
		}catch(Exception e){
			return 0;
		}
		
	}
	
	public static long parseLong(String str){
		return parseLong(str,0L);
	}
	
	public static float parseFloat(String str){
		return parseFloat(str,0F);
	}
	
	public static double parseDouble(String str){
		return parseDouble(str,0D);
	}
	
	public static String getRandomString(int length,String type){
		Random rand = new Random();
		int poolsize = type.length();
		StringBuffer result = new StringBuffer(length);
		for(int i=0; i<length; i++ ){
			result.append(type.charAt(rand.nextInt(poolsize)));
		}
		return result.toString();
	}
	
	public static String fixLength(Object obj,Object addObject, int length, int position){
		String addStr = addObject.toString();
		String result = obj.toString();
		while(result.length()<length){
			if(position == StringUtil.BEGIN){
				result = addStr + result;
			} else {
				result += addStr;
			}
		}
		if(result.length() > length){
			if(position == StringUtil.BEGIN){
				result = result.substring(result.length()-length);
			} else {
				result = result.substring(0,length);
			}
		}
		return result;
	}
	
	public static String getMD5(Object obj) {
		
		byte[] srcByte = obj.toString().getBytes();
		String result = null;
		// 用来将字节转换成 16 进制表示的字符	
		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(srcByte);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, 
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			result = new String(str); // 换后的结果转换为字符串
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;

	}
	
	public static boolean md5Match(Object obj,String str){
		String tmp = (str == null) ? "" : str;
		return getMD5(obj).equals(tmp);
	}
	
	public static String encodingConvert(String str,String encoding){
		try {
			return new String(str.getBytes(),encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return str;
		}
	}
	
	public static String trim(String str) {
		if(str!=null) {
			return str.trim();
		} else {
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(StringUtil.getMD5("bbb"));
	}
	
}
