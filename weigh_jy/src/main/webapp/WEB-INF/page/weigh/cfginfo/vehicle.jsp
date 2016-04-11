<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.est.common.ext.util.classutil.DateUtil"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			String startTime = DateUtil.format(DateUtil.add(new Date(),
			DateUtil.DAY_OF_MONTH, -30));
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>车辆录入</title>
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
			id:'vehicleid',
			cols:[
					{dataIndex: 'vehicleno', name:'vehicleno', header:'车牌号'},
					{dataIndex: 'remark', name:'remark', header:'备注'},
					{dataIndex: 'inputman', name:'inputman', header:'输入人'},
					{dataIndex: 'inputtime', name:'inputtime', header:'修改时间'},
					{dataIndex: 'sysDept.deptid', name:'sysDept.deptid', hidden:true},
					{dataIndex: 'vehicleweight', name:'vehicleweight', hidden:true},
					{dataIndex: 'vehicleid', name:'vehicleid', hidden:true}
			]
		}
		
    	var gridPanel = {
    		xtype:'estlayout',
    		region:'center',
    		items:{
   				id:'grid',
				xtype:'esteditgrid',
				storeurl:'<%=basePath%>est/weigh/cfginfo/WeighVehicle/getWeighVehicleList',
				colstype : colsType,
				region:'center'
			}
    	}
    	
    	
    	var formcols = [
		{fieldset:'车辆详细信息',id:'vehicleid',
			items: [
				{fieldLabel:'车牌号',name:'vehicleno',allowBlank:false},
				{fieldLabel:'备注',name:'remark'},
		        {name:'inputtime', hidden:true,hideLabel:true},  //更新时间
		        {name:'inputman', hidden:true,hideLabel:true},  //录入人
		        {name:'sysDept.deptid', hidden:true,hideLabel:true},  //部门id
		        {name:'vehicleweight', hidden:true,hideLabel:true}  //车重
		]}];
		
		var formPanel = {
				xtype: 'estform',
				id: 'formPanel',
				region:'east',
				title:'详细信息',
		       	width:350,
		       	colnum:1,
		       	formurl: '<%=basePath%>est/weigh/cfginfo/WeighVehicle',
				method: 'WeighVehicle',
				colstype: formcols,
				tbar:[
		       		{text:'重置/增加',iconCls:'page_add', handler:function(){
						Ext.getCmp('formPanel').doReset();
			    	}},
			    	{text:'保存',iconCls:'page_save', handler: function() {
			    		Ext.Msg.confirm('提示', '你确定要增加或修改吗?', function(btn) {
			    			if (btn == 'yes') {
			    				Ext.getCmp('formPanel').doSumbit({success:function(form, rep){
					       			Ext.getCmp('grid').store.reload();
					       		},failure:function() {error('错误','保存失败~!')}})
			    			}
			    		});
			    	}},
			    	{text:'删除',iconCls:'page_gear', handler: function() {
			    		Ext.Msg.confirm('提示', '你确定要删除吗?', function(btn) {
			    			if (btn == 'yes') {
			    				Ext.getCmp('formPanel').doDelete({
					    			success:function(form, rep) {
					    				Ext.getCmp('grid').store.reload();
					    			}
					    		});
			    			}
			    		});
			    	}}
			    ]
			}
		
		var tpanel = {
			xtype : 'estlayout',
			layout:'border',
			region : 'center',
			items : [gridPanel, formPanel]
		}
		new Ext.Viewport({
			layout:'border',
			items: [menuPanel,tpanel]
		});	
      	Ext.getCmp('grid').on({
	      		'rowclick': function(t, i, e) {
	      			var vehicleid = t.getSelectionModel().getSelected().data['vehicleid'];
	      			Ext.getCmp('formPanel').doLoad({vehicleid:vehicleid});
      		}
      	});
    })
    
   </script>
	</body>
</html>
