package com.est.common.hibernate.dialect;

import java.sql.Types;

import org.hibernate.Hibernate;

public class Oracle9iDialect extends org.hibernate.dialect.Oracle9iDialect{
	
	public Oracle9iDialect(){
		super();
		registerHibernateType(Types.CHAR, Hibernate.STRING.getName());
	}

}
