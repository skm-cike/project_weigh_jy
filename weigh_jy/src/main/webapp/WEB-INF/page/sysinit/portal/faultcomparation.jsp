<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<%
    response.setHeader("Pragma","No-cache"); 
    response.setHeader("Cache-Control","no-cache"); 
    response.setDateHeader("Expires", 0); 
    
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!-- 
位置：系统管理-用户管理
说明：设置部门与角色
路径：/fuel_jt/est/sysinit/sysuser/SysUser/fwdUser
作者：smile-bug
时间：2009
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
	/*
		Ext.onReady在页面body加载结束之后执行
	*/
	Ext.onReady(function(){
	
	function showChart(chartType) {
		var deptName = Ext.getCmp('deptName').getValue();
		var faultType = Ext.getCmp('faultType').getValue();
		var distence = Ext.getCmp('distence').getValue();
		var condition={deptName:deptName,faultType:faultType,distence:distence}
		Ext.getCmp('chartpanel').showChart(chartType,condition);
	}
		
	var chartpanel = {
			xtype:'estchartpanel',
			region:'fit',
			chartUrl:'<%=basePath%>est/sysinit/portal/FaultPortal/getFaultComparationForChart',
			id:'chartpanel',
			title:'图表',
       		listeners:{
			'render':function(){
				Ext.getCmp('chartpanel').showChart('MSColumn3D');
			   }
			}, 
			autoScroll :true
		}
	
		var colsType = {
			cols: [
				{dataIndex:'dept', header: '消缺部门',width: 40},
				{dataIndex:'faultIntimeTotal', header: '及时完成的数',width: 40},
				{dataIndex:'needClearAmount', header: '需要消缺总数',width: 40},
				{dataIndex:'faultIntimeRate',header:'及时率(%)',width:40,renderer:function(v){
					return parseFloat(v).toFixed(0);
				}}
	]};

	var gridpanel = {
		xtype: 'estlayout',
		title: '表格',
		id: 'gridpanel',
		items: [
			{xtype: 'estgrid', id:'grid',storeurl: '<%=basePath%>est/sysinit/portal/FaultPortal/getFaultComparationForGrid', colstype: colsType, region: 'center'}
		]
	};
	
	
	//tab列表
	var tabPanel = {
		id: 'tabpanel',
		region: 'center',
		xtype: 'esttab',
		tabPosition:'bottom',
		items: [chartpanel,gridpanel]
	};
	

	//主界面
	var group= new Ext.Viewport({
		layout: 'border',
		id: 'group',
		items: [{
			xtype: 'estlayout',
			region: 'center',
			bbar:[
				
				'部门:',{xtype:'estpropcombos',id:'deptName',propertycode:'QXJXDW',emptyText:'请选择',width:100},
				'类别:',{xtype:'estpropcombos',id:'faultType',propertycode:'QXLB',width:80},
				'间隔:',{xtype:'estpropcombos',id:'distence',propertycode:'SJJG',width:80},
				'->',
				{text:'柱图',handler:function(){showChart('MSColumn3D')}}
			],
       		listeners:{
			'render':function(){
				this.bbar.dom.align='right';
			   }
			},   
			items: [tabPanel]
		}],
		renderTo: Ext.getBody()
	});
	
		
	});
	</script>
</html>