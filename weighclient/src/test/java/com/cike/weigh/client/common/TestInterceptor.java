package com.cike.weigh.client.common;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class TestInterceptor implements MethodInterceptor {
	private Object targetObject;
    public Object createCglibIntenc(Object targetObject) {  
    	this.targetObject = targetObject; 
        Enhancer enhancer = new Enhancer();//通过类Enhancer创建代理对象  
        enhancer.setSuperclass(this.targetObject.getClass());//传入创建代理对象的类  
        enhancer.setCallback(this);//设置回调  
        return enhancer.create();//创建代理对象  
    }  
  
    public Object intercept(Object arg0, Method method, Object[] args, MethodProxy arg3) throws Throwable {
    	System.out.println("前");
    	Object obj = arg3.invoke(this.targetObject, args);
    	System.out.println("后");
       return obj;
    }  
}
