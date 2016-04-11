package com.est.workflow.process.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *@desc 工作流流程业务对象类声明
 *@author jingpj
 *@date Oct 23, 2009
 *@path com.est.workflow.process.annotation.WFBusinessClassAnnotation
 *@corporation Enstrong S&T
 */
@Target(ElementType.FIELD)   
@Retention(RetentionPolicy.RUNTIME) 
public @interface WFSignFieldAnnotation {
	String value() default "";
}
	