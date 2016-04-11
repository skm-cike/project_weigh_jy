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
	
	/*
	var chartpanel = {
			xtype:'estchartpanel',
			region:'fit',
			chartUrl:'est/asset/index/RealtimeIndex/fwdChart',
			id:'chartpanel',
			title:'数量统计',
			autoScroll :true
		}
		
		
	//2级tab列表
	var lvltwotabPanel = {
		id: 'tabpanel',
		region: 'center',
		xtype: 'esttab',
		items: [chartpanel]
	};
	*/
	
	
	var pagemappings = {
		'comparation':'fwdFaultComparation',
		'quantity': 'fwdFaultQuantity',
		'clearrate':'fwdFaultClearRate',
		'intimerate':'fwdFaultIntimeRate',
		'onetimepass':'fwdFaultOnepassRate'
	}
	
	var fwdTabs = function(tabname){
		
		window.document.frames[0].window.location = '<%=basePath %>est/sysinit/portal/FaultPortal/'+pagemappings[tabname];
	}
	
	var panel2 = {
		title:'缺陷',
		id:'panel2',
		region:'center',
		layout:'fit',
		xtype:'estlayout',
		html:'<iframe src="<%=basePath%>est/sysinit/portal/FaultPortal/fwdFaultComparation" width=100% height=100% />'
		//items:lvltwotabPanel
	}
	
	
	
	//1级tab列表
	var lvlonetabPanel = {
		id: 'tabpanel',
		region: 'center',
		xtype: 'esttab',
		tbar:[
			{text:'同期对比',id:'btnComparation',handler:function(){fwdTabs('comparation')},enableToggle:true ,toggleGroup:'btngroup'},
			{text:'数量',handler:function(){fwdTabs('quantity')},enableToggle:true ,toggleGroup:'btngroup'},
			{text:'消缺率',handler:function(){fwdTabs('clearrate')},enableToggle:true ,toggleGroup:'btngroup'},
			{text:'及时率',handler:function(){fwdTabs('intimerate')},enableToggle:true ,toggleGroup:'btngroup'},
			{text:'一次通过率',handler:function(){fwdTabs('onetimepass')},enableToggle:true ,toggleGroup:'btngroup'}
		],
		items: [panel2]
	};
	//主界面
	var view= new Ext.Viewport({
		layout: 'border',
		id: 'view',
		items: [{
			xtype: 'estlayout',
			region: 'center',
			items: [lvlonetabPanel]
		}],
		renderTo: Ext.getBody()
	});
	
	Ext.getCmp('btnComparation').toggle();


});
</script>
</html>