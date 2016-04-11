<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="s" uri="/struts-tags"%> 

<%
    response.setHeader("Pragma","No-cache"); 
    response.setHeader("Cache-Control","no-cache"); 
    response.setDateHeader("Expires", 0); 
    
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	
	String LOGINID = request.getParameter("LOGINID");
	String Timestamp = request.getParameter("Timestamp");
	
	session.setAttribute("LOGINID",LOGINID);
	session.setAttribute("Timestamp",Timestamp);
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 <script>
	function redirect(){
		window.location= '<%=basePath%>est/sysinit/portal/Portal/fwdPortal';
	}
	
</script>  

<base href="<%=basePath%>" />
<title>欢迎使用江油电厂燃料管理系统</title>

</head>


<body onload="redirect();">

</body>


</html>

