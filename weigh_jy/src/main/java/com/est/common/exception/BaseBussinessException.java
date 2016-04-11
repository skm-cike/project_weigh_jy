package com.est.common.exception;


/**
 *@desc 基本异常类
 *@author jingpj
 *@date Nov 7, 2009
 *@path com.est.common.exception.BaseBussinessException
 *@corporation Enstrong S&T
 */

public class BaseBussinessException extends RuntimeException {
	
	private static final long serialVersionUID = 5573672999134891219L;
	

	public BaseBussinessException() {
		super();
	}
	
	public BaseBussinessException(Exception e) {
		super(e);
	}
	
	public BaseBussinessException(String msg) {
		super(msg);
	}
	
	/**
	 *@desc 抛出异常
	 *@date Nov 7, 2009
	 *@author jingpj
	 *@param msg
	 */
	public static void throwException(String msg) {
		throw new BaseBussinessException(msg);
	}
	

}
