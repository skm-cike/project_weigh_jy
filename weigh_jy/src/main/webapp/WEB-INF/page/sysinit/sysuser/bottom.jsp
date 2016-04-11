<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.est.sysinit.sysuser.vo.SysUser,com.est.sysinit.sysdept.vo.SysDept;"%>
<%
response.setHeader("Pragma","No-cache"); 
response.setHeader("Cache-Control","no-cache"); 

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" type="text/css"
		href="<%=basePath%>js/ext/resources/css/ext-all.css">
	<script type="text/javascript" src="<%=basePath%>js/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/ext/ext-all-debug.js"></script>
	
	<script type="text/javascript"
		src="<%=basePath%>js/ext/source/locale/ext-lang-zh_CN.js"></script>
	
	<script type="text/javascript" src="<%=basePath%>js/est/util-debug.js"></script>

</head>
<body>
</body>
<%
   SysUser sysUser=(SysUser)session.getAttribute("loginUser");
   SysDept sysDept=sysUser.getSysDept();
   String dept="未分配";
   if(sysDept!=null){
     dept=sysDept.getDeptname();
   }
   String username=sysUser.getUsername();
   
%>

<script type="text/javascript">
	Ext.onReady(function(){
		var bottomPanel = new Ext.StatusBar({
			id:'statusbar',
			statusAlign:'right', 
			items: [
			'&nbsp;&nbsp;&nbsp;',
			'用户姓名: <%=username%>&nbsp;&nbsp;&nbsp;  所属部门: <%=dept%>  ',
			'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version:&nbsp;&nbsp;V&nbsp;0.1--alphal',
			'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Copyright©&nbsp;版权所有：北京英思创科技有限公司'
			
			
			/*
			{
	            text: 'Button'
	        }, '-', 'Text'
	        */
	        ]
	        
		});
		
		new Ext.Viewport({
			items: [bottomPanel],
			renderTo: Ext.getBody()
		});
		
		/*
		//定时弹出定期工作提醒对话框
		setTimeout(function(){
			top.frames['main'].showCyclework();
		},5000);
		setInterval(function(){
			top.frames['main'].showCyclework();
		
		},3600000);
		
		*/
		
		
		
	});

</script>
</html>