package com.est.workflow.process.annotation;

import com.est.common.ext.util.ContextUtil;

@SuppressWarnings("unchecked")
public class WFAnnotationHelper {
	
	
	private Class masterClass;
	
	private Class slaveryClass;
	
	private Object callbackBean;
	
	
	
	
	/** 主表vo，需要完整的包名+类名 */
	private String masterClassName;
	
	/** 从表vo，需要完整的包名+类名 */
	private String slaveryClassName;
	
	/** 从表vo关联主表的外键字段 **/
	private String slaveryFKName;
	
	/** 实现callback接口的类的spring配置bean */
	private String callbackBeanName;
	
	
	private String actionName;
	
	
	public WFAnnotationHelper(String actionName){
		super();
		this.actionName = actionName;
		try {
			Class actionClass = Class.forName(actionName);
			if (actionClass.isAnnotationPresent(WFClassDefAnnotation.class)) {
				
				WFClassDefAnnotation wFBusinessClassAnnotation = (WFClassDefAnnotation) actionClass.getAnnotation(WFClassDefAnnotation.class);
				this.callbackBeanName = wFBusinessClassAnnotation.callbackBeanName();
				this.masterClassName = wFBusinessClassAnnotation.masterClassName();
				this.slaveryClassName = wFBusinessClassAnnotation.slaveryClassName();
				this.slaveryFKName = wFBusinessClassAnnotation.slaveryClassName();
				
				if(this.masterClassName != null){
					this.masterClass = Class.forName(this.masterClassName);
				}
				
				if(this.slaveryClassName!=null) {
					this.slaveryClass = Class.forName(this.slaveryClassName);
				}
				
				if(this.callbackBeanName!=null){
					this.callbackBean = ContextUtil.getBean(this.callbackBeanName);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	
	public Class getMasterClass() {
		return masterClass;
	}

	public void setMasterClass(Class masterClass) {
		this.masterClass = masterClass;
	}

	public Class getSlaveryClass() {
		return slaveryClass;
	}

	public void setSlaveryClass(Class slaveryClass) {
		this.slaveryClass = slaveryClass;
	}

	public Object getCallbackBean() {
		return callbackBean;
	}

	public void setCallbackBean(Object callbackBean) {
		this.callbackBean = callbackBean;
	}
	
	
	
	
	

}
