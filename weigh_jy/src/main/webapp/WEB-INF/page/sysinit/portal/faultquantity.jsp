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
		var startTime = Ext.getCmp('startTime').getValue();
		if(startTime){
			startTime = Ext.util.Format.date(startTime,'Y-m-d');
		}
		var endTime = Ext.getCmp('endTime').getValue();
		if(endTime){
			endTime = Ext.util.Format.date(endTime,'Y-m-d');
		}
		var deptName = Ext.getCmp('deptName').getValue();
		var faultType = Ext.getCmp('faultType').getValue();
		var distence = Ext.getCmp('distence').getValue();
		
		var condition={deptName:deptName,startTime:startTime,endTime:endTime,faultType:faultType,distence:distence}
		Ext.getCmp('chartpanel').showChart(chartType,condition);
	}
		
	var chartpanel = {
			xtype:'estchartpanel',
			region:'fit',
			chartUrl:'<%=basePath%>est/sysinit/portal/FaultPortal/getFaultTotalForChart',
			id:'chartpanel',
			title:'图表',
			autoScroll :true
		}
	
	
	var colsType = {
				cols: [	
					{dataIndex:'dept', header: '消缺部门', width: 100},
					{dataIndex:'ATypeAmount',header:'A类数',width:50},	
					{dataIndex:'BTypeAmount',header:'B类数',width:50},	
					{dataIndex:'CTypeAmount',header:'C类数',width:50},	
					{dataIndex:'DTypeAmount',header:'D类数',width:50},	
					{header:'总数',width:50,renderer:function(value,metadata,record,rowIndex, colIndex, store){
						var A = record.get("ATypeAmount");
						var B = record.get("BTypeAmount");
						var C = record.get("CTypeAmount");
						var D = record.get("DTypeAmount");
						value = A+B+C+D;
						return value;
					}}
				]};

	var gridpanel = {
		xtype: 'estlayout',
		title: '表格',
		id: 'gridpanel',
		items: [
			{xtype: 'estgrid', id:'grid',storeurl: '<%=basePath%>est/sysinit/portal/FaultPortal/getFaultTotalForGrid', colstype: colsType, region: 'center'}
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

	//搜索
	var Search = function(){
		var startTime = Ext.getCmp('startTime').getValue();
		if(startTime){
			startTime = Ext.util.Format.date(startTime,'Y-m-d');
		}
		var endTime = Ext.getCmp('endTime').getValue();
		if(endTime){
			endTime = Ext.util.Format.date(endTime,'Y-m-d');
		}
		var deptName = Ext.getCmp('deptName').getValue();
		var faultType = Ext.getCmp('faultType').getValue();
		
		var condition={deptName:deptName,startTime:startTime,endTime:endTime,faultType:faultType}
		
		var gridestore = Ext.getCmp('grid').getStore();  
		Ext.apply(gridestore.baseParams,condition);
		gridestore.reload();
		
		
		
	}
	// 重置
	var Reset = function(){
		Ext.getCmp('startTime').reset();
		Ext.getCmp('endTime').reset();
		Ext.getCmp('deptName').reset();
		Ext.getCmp('faultType').reset();
		Ext.getCmp('distence').reset();
	}

	//主界面
	var group= new Ext.Viewport({
		layout: 'border',
		id: 'group',
		items: [{
			xtype: 'estlayout',
			region: 'center',
			bbar:[
				'时间:',{xtype:'datefield', id:'startTime',format:'Y-m-d',width:80},
				'--',{xtype:'datefield',id:'endTime',format:'Y-m-d',width:80},
				'部门:',{xtype:'estpropcombos',id:'deptName',propertycode:'QXJXDW',emptyText:'请选择',width:100},
				'类别:',{xtype:'estpropcombos',id:'faultType',propertycode:'QXLB',width:50},
				'间隔:',{xtype:'estpropcombos',id:'distence',propertycode:'SJJG',width:50},
				{text:'查询',handler:Search},
				{text:'重置',handler:Reset},
				'->',
				{text:'线图',handler:function(){showChart('Line')}},
				{text:'柱图',handler:function(){showChart('Column3D')}},
				{text:'饼图',handler:function(){showChart('Pie2D')}}
			],
			items: [tabPanel]
		}],
		renderTo: Ext.getBody()
	});
	
		
		
	});
	</script>
</html>