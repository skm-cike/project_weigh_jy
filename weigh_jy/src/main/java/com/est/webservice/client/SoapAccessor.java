package com.est.webservice.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
/**
 * 
 * @描述: 连接webService工具类
 * @作者: 陆华
 * @时间: 2013-8-9 下午01:14:23
 */
public class SoapAccessor {
	private SoapSupport soapSupport;
	private Map<String, Client> clientMap = new HashMap();

	public void setSoapSupport(SoapSupport soapSupport) {
		this.soapSupport = soapSupport;
	}
	
	public Client createClient(String name) {
		Client client = clientMap.get(name);
		if (client != null) {
			return client;
		}
		JaxWsDynamicClientFactory jwd = soapSupport.getJaxWsDynamicClientFactory();
		Map<String, String> map = soapSupport.getUrls();
		String url = map.get(name);
		if (url == null) {
			return null;
		}
		try {
			client = jwd.createClient(url);
			if (client != null) {
		        //设置超时单位为毫秒  
		        HTTPConduit http = (HTTPConduit) client.getConduit();        
		        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();        
		        httpClientPolicy.setConnectionTimeout(soapSupport.getConnectTimeout());  //连接超时      
		        httpClientPolicy.setAllowChunking(false);    //取消块编码   
		        httpClientPolicy.setReceiveTimeout(soapSupport.getAnswerTimeout());     //响应超时  
		        http.setClient(httpClientPolicy);  
				clientMap.put(name, client);
				return client;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}
}
