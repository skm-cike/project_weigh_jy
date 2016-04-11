<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String fwd = request.getParameter("fwd");
	System.out.println(fwd);
	if(fwd==null) {
		fwd = "est/sysinit/sysuser/SysUser/fwdBlank";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<title>欢迎使用江油电厂过磅信息系统<%=fwd %></title>
	</head>
	
	<frameset id="mainframe" rows="48,*,26" cols="*" border="0" framespacing="0">
		<frame src="<%=basePath%>est/sysinit/sysuser/SysUser/fwdTop" name="top" scrolling="No" noresize="noresize" id="top" />
		<frame src="<%=fwd %>" name="main" id="main" scrolling="auto" />
		<frame src="<%=basePath%>est/sysinit/sysuser/SysUser/fwdBottom" name="bottom" scrolling="No" noresize="noresize" id="bottom" />
	</frameset> 
	<noframes>
		<body>
			Sorry！！！你的浏览器不支持框架!!
		</body>
	</noframes>
</html>