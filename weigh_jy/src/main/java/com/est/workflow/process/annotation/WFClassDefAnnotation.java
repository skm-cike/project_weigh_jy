package com.est.workflow.process.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *@desc 工作流流程对象定义声明
 *@author jingpj
 *@date Oct 23, 2009
 *@path com.est.workflow.process.annotation.WFBusinessClassAnnotation
 *@corporation Enstrong S&T
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME) 

public @interface WFClassDefAnnotation {
	
	/** 主表vo，需要完整的包名+类名 */
	String masterClassName();
	
	/** 从表vo，需要完整的包名+类名 */
	String slaveryClassName();
	
	/** 从表vo关联主表的外键字段 **/
	String slaveryFKName() default "";
	
	/** 实现callback接口的类的spring配置bean */
	String callbackBeanName();
	
}
