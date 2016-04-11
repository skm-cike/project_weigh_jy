<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!-- 
位置：指标实时数据图表
路径：/fuel_jt/est/asset/index/Realtime/fwdRealtime
作者：jingpj
时间：2010-3-11
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<base href="<%=basePath %>">
	<%@ include file="/include.jsp"%>
	<script type="text/javascript" src="FusionCharts/FusionCharts.js"></script>
</head>
<body>

</body>
<script type="text/javascript">
Ext.onReady(function(){
	
	var chartpanel = {
			xtype:'estchartpanel',
			region:'fit',
			chartUrl:'<%=basePath%>est/sysinit/portal/RealtimeIndex/fwdChart',
			id:'chartpanel',
			title:'图表',
			autoScroll :true
		}
	var gridpanel = {
		xtype:'panel',
		region:'fit',
		title:'表格',
		html:'ssss'
	}
	
	
	//tab列表
	var tabPanel = {
		id: 'tabpanel',
		region: 'center',
		xtype: 'esttab',
		items: [chartpanel,gridpanel]
	};

	//主界面
	var group= new Ext.Viewport({
		layout: 'border',
		id: 'group',
		items: [{
			xtype: 'estlayout',
			region: 'center',
			items: [tabPanel]
		}],
		renderTo: Ext.getBody()
	});


});
</script>
</html>