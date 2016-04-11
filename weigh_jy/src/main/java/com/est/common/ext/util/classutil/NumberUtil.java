package com.est.common.ext.util.classutil;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 27, 2009
 * @path com.est.common.ext.util.classutil
 * @name com.est.common.ext.util.classutil.NumberUtil
 * @description Number工具类（主要进行精度控制操作）
 */
public class NumberUtil {
	private static final int DEF_DIV_SCALE = 10;

	public static Double round(Double value, int scale) {
		double val = 0D;
		val = value * Math.pow(10, scale);
		val = Math.round(val) / Math.pow(10, scale);
		return val;
	}

	/**
	 * 小数保留位数 四舍六入五成双
	 * 
	 * @author suf
	 * @param scale
	 * @return
	 */
	public static String getDigit(int scale) {
		String str = "#.#";
		for (int i = 0; i < scale - 1; i++) {
			str += "#";
		}
		if (scale == 0) {
			str = "#";
		}
		
		return str;
	}

	public static Double getdbnum(Double value, int scale) {
		DecimalFormat df = new DecimalFormat(getDigit(scale));
		Double val = Double.parseDouble(df.format(value));
		return val;
	}
	
	
	/**
	 * 替换getD方法
	 */
	public static Double getD(Double value, int scale) {
		DecimalFormat df = new DecimalFormat(getDigit(scale));
		Double val = Double.parseDouble(df.format(value));
		return val;
	}

//public static Double getD(Double d, int scale) {
//		
//		
//		String s = String.valueOf(d);
//		String tmpstr = "";
//		if (s.length() > (s.lastIndexOf(".") + scale + 2)) {  //有有效数字
//			int num1 = Integer.valueOf(String.valueOf(s.charAt(s.lastIndexOf(".") + scale + 1)));
//			//int num2 = Integer.valueOf(String.valueOf(s.charAt(s.lastIndexOf(".") + scale)));
//
//			//if (num1 <= 4 || num2==0) {	//小于4时抛位 或 5后无有效数字时抛位
//			if (num1 <= 4) {
//				tmpstr = s.substring(0, s.lastIndexOf(".") + scale + 1)+"0";
//				return Double.parseDouble(tmpstr);
//			} else {	//大于4时进位
//				int scale1 = scale;
//				if(scale == 0){//保留0位小数的特殊处理
//					tmpstr= tmpstr+".";
//					scale1 =scale-1;
//				}
//				tmpstr = s.substring(0, s.lastIndexOf(".") + scale1);
//				int num2 = Integer.valueOf(String.valueOf(s.charAt(s.lastIndexOf(".") + scale1)));
//				if(num2 == 9){//等于9时进位
//                    String num3="0";
//                    if(scale == 0){//保留0位小数的特殊处理
//                    	num3="1";
//    				}
//                    String num4="";
//                    for(int i=0;i<scale1-1;i++){
//                    	num4+="0";
//                    }
//                    if(num4.length()>0){
//                    	num3=num3+"."+num4+"1";
//                    }
//					tmpstr = s.substring(0,s.lastIndexOf(".") + scale1+1);
//					
//					return (Double.parseDouble(tmpstr)+Double.parseDouble(num3));
//				}
//				return Double.parseDouble(tmpstr + (num2 + 1));
//			}
//		} else if (s.length() == (s.lastIndexOf(".") + scale + 2)) { //无有效数字
//			int num1 = Integer.valueOf(String.valueOf(s.charAt(s.lastIndexOf(".") + scale + 1)));
//
//			if (num1 <= 4) {	//小于4时抛位
//				tmpstr = s.substring(0, s.lastIndexOf(".") + scale + 1)+"0";
//				return Double.parseDouble(tmpstr);
//			} else {
//				int scale1 = scale;
//				if(scale == 0) //保留0位小数的特殊处理
//				{
//					scale1 =scale-1;
//				}
//				int num3 = Integer.valueOf(String.valueOf(s.charAt(s
//						.lastIndexOf(".") + scale1)));
//				if (num3 % 2 == 0) {//判断为双数时抛位
//					tmpstr = s.substring(0, (s.lastIndexOf(".") + scale + 1))+"0";
//					return Double.parseDouble(tmpstr);
//				} else {
//					tmpstr = s.substring(0, s.lastIndexOf(".") + scale1);
//					int num2 = Integer.valueOf(String.valueOf(s.charAt(s
//							.lastIndexOf(".") + scale1)));
//					
//					if(num2 == 9){
//	                    String num5="0";
//	                    if(scale == 0){//保留0位小数的特殊处理
//	                    	num5="1";
//	    				}
//	                    String num4="";
//	                    for(int i=0;i<scale1-1;i++){
//	                    	num4+="0";
//	                    }
//	                    if(num4.length()>0){
//	                    	num5=num5+"."+num4+"1";
//	                    }
//						tmpstr = s.substring(0,s.lastIndexOf(".") + scale1+1);
//						
//						return (Double.parseDouble(tmpstr)+Double.parseDouble(num5));
//					}
//					return Double.parseDouble(tmpstr + (num2 + 1));
//				}
//				}
//			} else {
//				return d;
//		}
//	}

	public static Double sub(Double v1, Double v2) {
		
		if(null != v1 && null != v2){
			BigDecimal b1 = new BigDecimal(v1.toString());
			BigDecimal b2 = new BigDecimal(v2.toString());
			return new Double(b1.subtract(b2).doubleValue());
		}else
			return 0D;
		
	}

	public static double add(Double v1, Double v2) {
		
		if(null != v1 && null != v2 ){
			BigDecimal b1 = new BigDecimal(Double.toString(v1));
			BigDecimal b2 = new BigDecimal(Double.toString(v2));
			return b1.add(b2).doubleValue();
		}else
			return 0D;
		
	}

	public static double mul(double v1, double v2) {
		
			BigDecimal b1 = new BigDecimal(Double.toString(v1));
			BigDecimal b2 = new BigDecimal(Double.toString(v2));
			return b1.multiply(b2).doubleValue();
	}

	public static Double div(Double v1, Double v2) {
		
		if(null != v1 && null != v2){
			BigDecimal b1 = new BigDecimal(v1.toString());
			BigDecimal b2 = new BigDecimal(v2.toString());
			return new Double(b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue());
		}else
			return 0D;
		
	}
	
	public static Double getRoundDouble(Double v1,String round){
		
		Double returnD = 0D;
		
		if("向上".equals(round)){
			returnD = Math.ceil(v1);
		}else if("向下".equals(round)){
			returnD = Math.floor(v1);
		}
		return returnD;
	}
	
	
	  /**  
     * 使用java正则表达式去掉多余的.与0  
     * @param s  
     * @return   
     */  
    public static String subZeroAndDot(String s){   
        if(s.indexOf(".") > 0){   
            s = s.replaceAll("0+?$", "");//去掉多余的0   
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉   
        }   
        return s;   
    }  
}
