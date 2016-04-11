<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String fwd = request.getParameter("fwd");
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
	</head>
		<frameset rows="*" cols="185,*" frameborder="no" framespacing="0">
			<frame src="<%=basePath%>est/sysinit/sysuser/SysUser/fwdCardguide" style="padding-top:0px;"  id="cardguide" name="cardguide" />
			<frame src="<%=fwd%>"  id="mainframe"  name="mainframe" />
		</frameset>
</html>