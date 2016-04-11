package com.cike.weigh.client;

import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Main {
	public static void main(String[] args) {
		DOMConfigurator.configure("log4j.xml");
		ApplicationContext ac = new FileSystemXmlApplicationContext(new String[]{"applicationContext.xml", "applicationContext-timer.xml"});
//		ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml", "applicationContext-timer.xml"});
	}
}
