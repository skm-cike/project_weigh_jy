package com.est.common.ext.util.businessutil;

import java.util.Random;

/**
 * 生成随机字符帮助类
 * @author Administrator
 *
 */
public class RandomCodeGeneratorUtil {

	
	private static final int NUMBER =  0;
	private static final int ALPHABET =  1;
	private static final int ALPHABETNUMBER =  2;
	
	private static final char[] CHAR_NUMBER = new char[]{'0','1','2','3','4','5','6','7','8','9'};
	private static final char[] CHAR_ALPHABET = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	private static final char[] CHAR_ALPHABETNUMBER = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	
	/**
	 * 
	 *@desc 生成数字型的随机字符串
	 *@date Oct 25, 2010
	 *@author heb
	 *@param serialLength
	 *@return
	 */
	public static String getRandomNumberStr(int length){
		return getRandomString(length,NUMBER);
	}
	/**
	 * 
	 *@desc 随机生成a-z的随机字符串
	 *@date Oct 25, 2010
	 *@author heb
	 *@param length
	 *@return
	 */
	public static String getRandomAlphabetStr(int length){
		return getRandomString(length,ALPHABET);
	}
	/**
	 * 
	 *@desc 随机生成0-9和a-z混和的随机字符串
	 *@date Oct 25, 2010
	 *@author heb
	 *@param length
	 *@return
	 */
	public static String getRandomAlphabetNumberStr(int length){
		return getRandomString(length,ALPHABETNUMBER);
	}
	
	
	public static String getRandomString(int length,int type){
		char[] chars ;
		switch(type) {
			case NUMBER : chars = CHAR_NUMBER; break;
			case ALPHABET : chars = CHAR_ALPHABET; break;
			case ALPHABETNUMBER : chars = CHAR_ALPHABETNUMBER; break;
			default: throw new RuntimeException("随机字符类型指定错误");
		}
		
		Random rand = new Random();
		
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<length; i++){
			char c = chars[rand.nextInt(chars.length)];
			sb.append(c);
		}
		
		return sb.toString();
	}
	
}




