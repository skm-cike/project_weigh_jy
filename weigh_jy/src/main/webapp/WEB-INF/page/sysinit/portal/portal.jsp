<%@ page language="java"  pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="s" uri="/struts-tags"%> 
<%
    response.setHeader("Pragma","No-cache"); 
    response.setHeader("Cache-Control","no-cache"); 
    response.setDateHeader("Expires", 0); 
    
    response.setHeader("Pragma","No-cache"); 
    response.setHeader("Cache-Control","no-cache"); 
    response.setDateHeader("Expires", 0); 
	
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	
	Object loginUser=(Object)session.getAttribute("loginUser");	
	String login="N"; 
	if(loginUser != null){
		login="Y";
	}
%>

<html>
<head>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<base href="<%=basePath%>" />
<title>欢迎使用江油电厂过磅信息管理系统</title>
<link href="<%=basePath%>img/portal/login.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=basePath %>js/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=basePath %>js/ext/ext-all-debug.js"></script>
<script type="text/javascript" src="<%=basePath %>js/ext/source/locale/ext-lang-zh_CN.js"></script>

<script type="text/javascript">
	if('<%=login%>'=='Y'){
			window.location= '<%=basePath%>est/sysinit/sysuser/SysUser/login'
	}
	function doLogin(){
		var login=document.getElementById("login").value;
    	var password=document.getElementById("password").value;
    	if(login==""){
    		alert("请输入用户登录名");
    		document.getElementById("login").focus();
    	}
    	if(password==""){
    		alert("请输入密码");
    		document.getElementById("password").focus();
    	}
    	
    	//登陆
		Ext.Ajax.request({
				url:"<%=basePath%>est/sysinit/sysuser/SysUser/vefUser?login="+login+"&password="+password,
				success:function(response){
					var responseJson = Ext.decode(response.responseText);
					if(responseJson.success){
						 window.top.location = "<%=basePath%>est/sysinit/sysuser/SysUser/login";
					}else{
						alert("密码或用户名错误!");
						reset();
					}
				},
				failure:function(){
					alert("服务器无响应,请确认服务器是否启动!");
				}
			});
	}
	//重置
	function reset(){
		//document.getElementById("login").value="";
		document.getElementById("password").value="";
	}
	
	function keypressCheck(e,v){
    	var keynum;
		if(window.event) // IE
		{
			keynum = e.keyCode
		} 
		else if(e.which) // Netscape/Firefox/Opera
		{
			keynum = e.which
		}
		if(keynum==13) {
			if(v=="login"){
				document.getElementById("password").focus();
			}else{
				doLogin();
			}
		}
		//keychar = String.fromCharCode(keynum)
		//return !numcheck.test(keychar)
    
    }

</script>

</head>
<body onload="document.getElementById('login').focus();" scroll="no">
<table cellpadding="0" cellspacing="0" class="table-1">
	<tr>
		<td style="text-align:center; vertical-align:middle;">
			<div style="background:url(<%=basePath%>img/portal/1.jpg) no-repeat; text-align:center; vertical-align:bottom; height:106px; width:776px;" ><img src="<%=basePath%>img/portal/0.gif"></div>
			<div style="background:url(<%=basePath%>img/portal/2.gif) no-repeat; text-align:center; vertical-align:bottom; height:62px; width:776px;" ><img src="<%=basePath%>img/portal/0.gif"></div>
			<table cellpadding="0" cellspacing="0" width="776">
				<tr>
					<td style="background:url(<%=basePath%>img/portal/3.gif) no-repeat; float:left; padding-left:388px;" valign="top" height="270">
						<table cellpadding="0" cellspacing="0" height="111" width="264">
							<tr>
								<td width="5"><img src="<%=basePath%>img/portal/5.gif" /></td>
								<td width="254" style="background:url(<%=basePath%>img/portal/7.gif) repeat-x;">
									<table cellpadding="0" cellspacing="0" width="100%" style="font-size:12px; text-align:center; vertical-align:middle;">
										<tr>
											<td height="17" colspan="4"><img src="<%=basePath%>img/portal/0.gif" /></td>
										</tr>
										<tr>
											<td height="38" width="69" style="text-align:right;">用户名：</td>
											<td style="text-align:center; vertical-align:middle;"><input id="login" name="login" type="text" tabIndex="10" onkeypress="keypressCheck(event,'login')" style="border:1px solid #7EA7DB; width:90px; height:20px; background-color:#F5F8FD;"/></td>
											<td width="43" style=" background-image:url(<%=basePath%>img/portal/4.gif); background-position:center; background-repeat:no-repeat;"><a tabIndex="13" href="javascript:reset()">重置</a></td>
											<td width="27"><img src="<%=basePath%>img/portal/0.gif" /></td>
										</tr>
										<tr>
											<td height="38" width="69" style="text-align:right;">密　码：</td>
											<td style="text-align:center; vertical-align:middle;"><input id="password" name="password" type="password" tabIndex="11" onkeypress="keypressCheck(event,'pwd')" style="border:1px solid #7EA7DB; width:90px; height:20px; background-color:#F5F8FD;"/></td>
											<td width="43" style=" background-image:url(<%=basePath%>img/portal/4.gif); background-position:center; background-repeat:no-repeat;"><a tabIndex="12" href="javascript:doLogin()">登录</a></td>
											<td width="27"><img src="<%=basePath%>img/portal/0.gif" /></td>
										</tr>
										<tr>
											<td height="18" colspan="4"><img src="<%=basePath%>img/portal/0.gif" /></td>
										</tr>
									</table>
								</td>
								<td width="5"><img src="<%=basePath%>img/portal/6.gif" /></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>
