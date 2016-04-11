package com.est.common.ext.util.classutil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ObjectUtil {
	public static Object objcetMerge(Object target, Object source) {
		if (source == null) {
			return target;
		}

		if (target == null) {
			try {
				target = source.getClass().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		Class<?> clazz = getSameClass(target.getClass(), source.getClass());
		if (clazz == null) {
			return null;
		}
		
		Method[] methods = clazz.getMethods(); // 类中所有的方法
		Map<String, MethodGetSet> gsMethods = new HashMap<String, MethodGetSet>(); // 以去掉get,set字符的方法名为key,存储对应的get,set方法
		String name; // 方法全名
		String name_; // 去掉get,set字符的方法名
		MethodGetSet getSet; //存储一对get,set方法
		MethodGetSet tempGetSet; 
		
		// 按对存放所有的get,set方法
		for (Method m : methods) {
			name = m.getName();
			name_ = name.substring(3, name.length());
			if ((tempGetSet = gsMethods.get(name_)) == null) {
				getSet = new MethodGetSet();
			} else {
				getSet = tempGetSet;
			}
			
			if (name.startsWith("get")) {
				getSet.getMethod = m;
			}
			if (name.startsWith("set")) {
				getSet.setMethod = m;
			}
			if ((name.startsWith("set") || name.startsWith("get")) && !name.equals("getClass")) {
				gsMethods.put(name_, getSet);
			}
		}

		// 把对象source里的值付给对象target
		Method getMethod; // 临时get方法
		Method setMethod; // 临时set方法
		Object value; // 临时返回值
		Set<String> keys = gsMethods.keySet();
		try {
			for (String key : keys) {
				getSet = gsMethods.get(key);
				getMethod = getSet.getMethod;
				setMethod = getSet.setMethod;
				if (getMethod != null && setMethod != null) {
					value = getMethod.invoke(source);
					if (value != null && !"".equals(value)) {
						setMethod.invoke(target, value);
					}
				}
			}
			return target;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	//找到两个对象的类或其父类相同的类类型
	protected static Class<?> getSameClass(Class<?> c1, Class<?> c2) {
		if (c1.equals(Object.class)) {
			return null;
		} else if (c1.equals(c2)){
			return c2;
		} else {
			 return getSameClass(c1.getSuperclass(), c2);
		}	
	}
}

// 按对存放get,set方法，这对get,set方法操作同一属性或对象
class MethodGetSet {
	public Method setMethod;
	public Method getMethod;
}