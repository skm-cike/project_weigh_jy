package com.est.webservice.client;

import java.util.Map;
/**
 * 
 * @描述: 存放webservice服务基本信息
 * @作者: 陆华
 * @时间: 2013-8-10 下午12:47:11
 */
public class SoapSupport {
	private org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory jaxWsDynamicClientFactory;
	private Long connectTimeout = 3000l;
	private Long answerTimeout = 3000l;
	private Map<String, String> urls;
	public org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory getJaxWsDynamicClientFactory() {
		return jaxWsDynamicClientFactory;
	}
	public void setJaxWsDynamicClientFactory(
			org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory jaxWsDynamicClientFactory) {
		this.jaxWsDynamicClientFactory = jaxWsDynamicClientFactory;
	}
	public Map<String, String> getUrls() {
		return urls;
	}
	public void setUrls(Map<String, String> urls) {
		this.urls = urls;
	}
	public Long getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(Long connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public Long getAnswerTimeout() {
		return answerTimeout;
	}
	public void setAnswerTimeout(Long answerTimeout) {
		this.answerTimeout = answerTimeout;
	}
}
