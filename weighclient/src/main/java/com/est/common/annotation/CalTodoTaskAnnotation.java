package com.est.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *@desc 
 *@author jingpj
 *@date Mar 15, 2010
 *@path com.est.common.annotation.CalTodoTaskAnnotation
 *@corporation Enstrong S&T
 */


@Target(ElementType.METHOD)   
@Retention(RetentionPolicy.RUNTIME) 

public @interface CalTodoTaskAnnotation {
	
	String value();

}
