package com.est.webservice.client;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * 客户端程序
 * 
 * @author RenWeigang
 */
public class UCenterServiceClientTest {

	public void testCreateUser() throws Exception {
		Object ucenterService = getUCenterService();
		boolean result = ucenterService.equals("123456@test.com");
		System.out.println(result);
	}
	
	private Object getUCenterService() {
		return getNotSSLUCenterService();
	}
	
	private Object getNotSSLUCenterService()
	{
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(Object.class);
//此处的地址为对方发布的 webService的地址。
		factory.setAddress("http://uc.cun365.com/ucenter/ucenter/services/UCenterService");
		factory.setUsername("username");
		factory.setPassword("admin");
		Object ucenterService = (Object) factory.create();
//		ClientProxy proxy = (ClientProxy) Proxy.getInvocationHandler(ucenterService);
//		Client client = proxy.getClient();
		// ③添加流模型和DOM模型转换的Handler
	
		//client.getOutInterceptors().add(new SAAJOutInterceptor());
		
//		Map<String,Object> properties = new HashMap<String,Object>();
//		// ④-1动作
//		properties.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN + " " + WSHandlerConstants.TIMESTAMP);
//		// PW_TEXT 明文 ,PW_DIGEST 摘要
//		// PasswordDigest是通过非保密渠道发送用户名和口令的最佳方法。即使使用XML加密对<wsse:Password>元素进行加密，PasswordText依然可以使用
//	
//		properties.put(WSHandlerConstants.USER, "services-test");// ④-3指定用户
//		properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
//		properties.put(WSHandlerConstants.PW_CALLBACK_CLASS, UtPasswordHandler.class.getName());
//		WSS4JOutInterceptor wss4j = new WSS4JOutInterceptor(properties);
//		client.getOutInterceptors().add(wss4j);
		
		
		return ucenterService;
	}
}
