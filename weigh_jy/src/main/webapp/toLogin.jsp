<%@ page language="java" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache"); 
response.setHeader("Cache-Control","no-cache"); 

String urlpath = request.getContextPath();
String urlbasePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+urlpath+"/";
%>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    

<body>
	<center>
	<H3 style='color:red'>
		你的连接已经超时，系统将在三秒后跳转到登录页面，请重新登录。
	</H3>
	</center>
</body>

<script type="text/javascript">
	setTimeout(function(){
		top.location="<%=urlpath%>/index.jsp";
		},3000);
</script>