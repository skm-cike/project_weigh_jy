package com.est.common.ext.util.classutil;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ClassUtils {
	private static final Log log = LogFactory.getLog(ClassUtils.class);

	private ClassUtils() {
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or <code>Object.class</code> if
	 *         cannot be determined
	 */
	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
	 * 
	 * @param clazz   The class to introspect
	 * @param index   the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or <code>Object.class</code> if
	 *         cannot be determined
	 */
	public static Class getSuperClassGenricType(Class clazz, int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			log.warn("Index: " + index + ", Size of " + clazz.getSimpleName()
					+ "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			log.warn(clazz.getSimpleName()
							+ " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class) params[index];
	}
	
	/**
	 *@desc 得到属性的get方法的方法名 
	 *@date Dec 7, 2009
	 *@author jingpj
	 *@param fieldname
	 *@return
	 */
	public static String getGetMethodName(String fieldname) {
		if(fieldname == null) {
			return null;
		} else {
			return "get"+fieldname.substring(0,1).toUpperCase()+fieldname.substring(1);
		}
	}
	/**
	 *@desc 得到属性的set方法的方法名 
	 *@date Dec 7, 2009
	 *@author jingpj
	 *@param fieldname
	 *@return
	 */
	public static String getSetMethodName(String fieldname) {
		if(fieldname == null) {
			return null;
		} else {
			return "set"+fieldname.substring(0,1).toUpperCase()+fieldname.substring(1);
		}
	}
	
	public static Method getMethodByName(Class clazz,String methodName){
		Method[] methods = clazz.getMethods();
		for(Method method : methods) {
			if(method.getName().equals(methodName)) {
				return method;
			}
		}
		return null;
	}
}
