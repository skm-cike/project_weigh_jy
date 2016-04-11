package com.est.webservice.client;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class ClientTest {

	/**
	 *@desc 
	 *@date Aug 29, 2013
	 *@author Administrator
	 *@param args 
	 */
	public static void main(String[] args) {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(SI_03H3_FUEL2ERP_GOOMV.class);
		//此处的地址为对方发布的 webService的地址。
		factory.setAddress("http://10.128.143.109:50000/dir/wsdl?p=sa/073a860913e43c21930a888d665a2828");
		factory.setUsername("zzleixp");
		factory.setPassword("welcome");
		SI_03H3_FUEL2ERP_GOOMV testService = (SI_03H3_FUEL2ERP_GOOMV) factory.create();
		System.out.println(testService);
	}

}
