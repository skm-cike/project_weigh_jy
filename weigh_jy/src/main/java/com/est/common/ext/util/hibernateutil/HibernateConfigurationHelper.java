package com.est.common.ext.util.hibernateutil;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import com.est.common.ext.util.ContextUtil;

public class HibernateConfigurationHelper { 

	private static  Configuration hibernateConf = null; 
	private static  LocalSessionFactoryBean sessionFactory = null;
	
	
	static {
		HibernateConfigurationHelper.sessionFactory = (LocalSessionFactoryBean) ContextUtil.getBean("&sessionFactory");
		HibernateConfigurationHelper.hibernateConf = HibernateConfigurationHelper.sessionFactory.getConfiguration();
	}
	
       private static PersistentClass getPersistentClass(Class clazz) { 

              synchronized (HibernateConfigurationHelper.class) { 

                     PersistentClass pc = hibernateConf.getClassMapping(clazz.getName()); 

                     if (pc == null) { 

                            hibernateConf = hibernateConf.addClass(clazz); 

                            pc = hibernateConf.getClassMapping(clazz.getName()); 

                     } 

                     return pc; 

              } 

       } 

       public static  String getTableName(Class clazz) { 

              return getPersistentClass(clazz).getTable().getName(); 

       } 

       public static String getPkColumnName(Class clazz) { 

    	   
              return getPersistentClass(clazz).getTable().getPrimaryKey() 

                            .getColumn(0).getName(); 

       }
       
       public static String getPkName(Class clazz) { 

           return getPersistentClass(clazz).getIdentifierProperty().getName();

       }

} 

