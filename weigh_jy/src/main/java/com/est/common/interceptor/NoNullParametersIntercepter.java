package com.est.common.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.est.common.ext.util.classutil.ClassUtils;
import com.est.common.ext.util.hibernateutil.HibernateConfigurationHelper;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@SuppressWarnings({ "unchecked", "serial" })
public class NoNullParametersIntercepter extends AbstractInterceptor {


	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		try{
			if (invocation.getAction() instanceof ModelDriven) {
	
				
				Map<String, String[]> params = invocation.getInvocationContext()
						.getParameters();
	
				// 已处理属性
				Map<String, String> handledParams = new HashMap<String, String>();
	
				Object model = ((ModelDriven) invocation.getAction()).getModel();
				
				if(model == null) {
					return invocation.invoke();
				}
	
				Class modelClazz = model.getClass();
	
				Set<String> paramNameSet = params.keySet();
	
				Iterator<String> it = paramNameSet.iterator();
	
				while (it.hasNext()) {
	
					String paramsname = it.next();
	
					if (paramsname.contains(".")) {
	
						// 主对象中属性名称
						String mainPropName = paramsname.substring(0, paramsname
								.indexOf("."));
	
						if (handledParams.containsKey(mainPropName)) {
							continue;
						}
	
						String mainPropSetterName = ClassUtils
								.getSetMethodName(mainPropName);
	
						String mainPropGetterName = ClassUtils
								.getGetMethodName(mainPropName);
	
						Method[] methods = modelClazz.getMethods();
	
						for (Method m : methods) {
	
							if (m.getName().equals(mainPropGetterName)) {
								Class objclazz = m.getReturnType();
								String refPKName = HibernateConfigurationHelper.getPkName(objclazz);
								
								String refPkGetterName = ClassUtils
										.getGetMethodName(refPKName);
								Method refPkGetterMethod = objclazz
										.getMethod(refPkGetterName);
	
								Object refObject = m.invoke(model);
								Object refPkObject = refPkGetterMethod
										.invoke(refObject);
	
								Method mainPropSetterMethod = modelClazz.getMethod(mainPropSetterName,objclazz);
								if (refPkObject == null) {
									mainPropSetterMethod.invoke(model, new Object[]{null});
								} else if (refPkObject instanceof Long || ((Long) refPkObject == 0)) {
									mainPropSetterMethod.invoke(model, new Object[]{null});
								} else if (refPkObject instanceof String || ((String)refPkObject).equals("")) {
									mainPropSetterMethod.invoke(model, new Object[]{null});
								}
								handledParams.put(mainPropName, mainPropName);
								break;
							}
						}
	
					}
				}
	
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			return invocation.invoke();
		}

	}

}
