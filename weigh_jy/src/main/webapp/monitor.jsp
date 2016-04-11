<%@page import="com.est.fuel.base.service.TestMonitorServiceImp"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import= "org.springframework.web.context.support.WebApplicationContextUtils"%> 
<% 
ApplicationContext context =  WebApplicationContextUtils. getWebApplicationContext(application);
TestMonitorServiceImp service = (TestMonitorServiceImp)context.getBean("testMonitorServiceImp");
String s=service.test();

out.println(s); 
%>