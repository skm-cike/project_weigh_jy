<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.est.common.ext.util.classutil.DateUtil,com.est.sysinit.sysuser.vo.SysUser"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			String startTime = DateUtil.format(DateUtil.add(new Date(),
			DateUtil.DAY_OF_MONTH, -30));
	String salebuy = "1".equals(request.getParameter("inouttype"))?"买方":"卖方";
	SysUser user = (SysUser) request.getSession().getAttribute("loginUser");
	String deptname = user.getSysDept().getDeptname();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>单位录入</title>
		<%@ include file="/include.jsp"%>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	</head>

	<body>
	<script type="text/javascript">
    Ext.onReady(function(){
    	var colsType = {
			cols:[
					{dataIndex: 'companycode', name:'companycode', header:'单位编码'},
					{dataIndex: 'companyname', name:'companyname', header:'单位名称'},
					{dataIndex: 'jzje', name:'jzje', header:'上期余额'},
					{dataIndex: 'remainMoney', name:'remainMoney', header:'剩余金额'}
				<%if("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
				,{dataIndex: 'jizu', name:'jizu', header:'机组'}
					<%}%>
			]
		}
		
    	var gridPanel = {
    		xtype:'estlayout',
    		region:'center',
    		items:{
   				id:'grid',
				xtype:'esteditgrid',
				hasPagerBar:false,
				storeurl:'<%=basePath%>est/weigh/report/RemainMoney/getRemainMoneyCompanys?pz=<%=request.getParameter("pz")%>',
				colstype : colsType,
				region:'center'
			}
    	}

		new Ext.Viewport({
			layout:'border',
			items: [menuPanel,gridPanel]
		});	
    })
    
   </script>
	</body>
</html>
