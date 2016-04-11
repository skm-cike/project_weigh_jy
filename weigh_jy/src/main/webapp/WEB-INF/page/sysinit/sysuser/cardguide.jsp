<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.est.sysinit.sysauthority.service.ISysUserModuleService,org.springframework.web.context.WebApplicationContext" %>
<%@page import="com.est.sysinit.sysuser.vo.SysUser;"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	WebApplicationContext wac = (WebApplicationContext)config.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	ISysUserModuleService moduleService = (ISysUserModuleService) wac.getBean("userModuleService");
	String moduleStr = moduleService.getGuideModuleForAccordion(((SysUser)session.getAttribute("loginUser")).getUserid());
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<%@ include file="/include.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
	
	<style type="text/css">
		.submodulebtn:hover{/*鼠标经过时候的超链接*/
		   color:#9900FF;      /*颜色*/
		   text-decoration:none;    /*下划线无*/
		   font-size:10pt;
		   font-weight:bold;
		   padding:5px 8px 3px 12px;
		   background-color: #00FF00;
		   text-decoration: none;
		   border-top:1px solid #717171; /*边框变换，实现按下去的效果*/
		   border-left:1px solid #717171; /*边框变换，实现按下去的效果*/
		   border-bottom:1px solid #EEEEEE; /*边框变换，实现按下去的效果*/
		   sborder-right:1px solid #EEEEEE; /*边框变换，实现按下去的效果*/
		   //cursor:hand;
		   cursor:pointer;
	     }     
	     
	     .submodulebtn,.submodulebtn:link,.submodulebtn:visited{        /*超链接正常状态*/
	       color:green;      /*颜色*/
		   text-decoration:none;    /*下划线无*/
		   font-size:10pt;
		   font-weight:bold;
		   padding:4px 10px 4px 10px;
		   background-color: white;
		   text-decoration: none;
		   //border-top: 1px solid #EEEEEE;  边框实现阴影效果 
		   //border-left: 1px solid #EEEEEE;
		   border-bottom: 1px solid #717171;
		  // border-right: 1px solid #717171;
		   Line-height:1.8;
	    }
	    
	</style>
	<script type="text/javascript">
		var menu = <%=moduleStr%>;
		//alert(Ext.encode(menu))
		Ext.onReady(function(){
			new Ext.Viewport({
				layout: 'border',
				items: [{
					title:'导航',
					xtype: 'estlayout',
					layout:'accordion',
					region: 'west',
					width:185,
					collapsible:true,
					items: menu
				},{region:'center'}],
				renderTo: Ext.getBody()
			});	
		})
		
		function fwdPage(url) {
			window.parent.mainframe.location.href='<%=basePath%>'+url;
		}
	</script>
	</head>
	<body style="border:0px;"></body>
</html>