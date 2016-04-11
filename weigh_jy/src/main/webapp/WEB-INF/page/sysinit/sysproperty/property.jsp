<!-- 
	位置：系统管理-属性管理
	说明：属性配置管理
	路径：main/webapp/WEB-INF/page/sysinit/sysproperty/property.jsp
	作者：tanhf
	时间：2009-07-01
-->

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<%@ include file="/include.jsp"%>
	</head>
	<body>
	</body>
	<script type="text/javascript">
	/*
		Ext.onReady在页面body加载结束之后执行
	*/
	Ext.onReady(function(){
	
	var selPropertyId;
	var propertySubListUrl='';		
	
/*＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝【主】属性管理部分开始＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝*/
	var colsType = {id: 'propertyid', 
		cols: [
			{dataIndex:'propertyid',header:'属性ID',wdith:0, name:'propertyid',type:'int',hidden:true},
			{dataIndex:'propertyname', header: '属性名称', width: 200, sortable: true, name:'propertyname',type:'string',editor: new Ext.form.TextField({allowBlank: false})},
			{dataIndex:'propertycode', header: '属性编码',width: 100, sortable: true, name:'propertycode',type:'string',editor: new Ext.form.TextField({allowBlank: false})}
		]};
	
	var _MainProperty = Ext.data.Record.create(colsType.cols);
	
	var gridMainPropertyPanel = {
		xtype: 'estlayout',
		title: '属性列表',
		id: 'gridmainpanel',
		tbar: [{
				text: '添加',
				handler:function(){
					var newMainProperty = new _MainProperty({
	                   propertyid: 0,
	                   propertyname: '',
	                   propertycode: ''
					});
			                
					var gridproperty = Ext.getCmp('gridproperty');
					gridproperty.addRow(newMainProperty);
				}
			},
			{
				text: '删除',
				handler:function(){
					var gridproperty = Ext.getCmp('gridproperty');
					gridproperty.deleteRow();
				}
			},{
				text: '保存',
				handler:function(){
					var gridproperty = Ext.getCmp('gridproperty');
					gridproperty.onSave('<%=basePath%>est/sysinit/sysproperty/SysProperty/savPropertyList');
				}
			}
		],
		items: [
			{xtype: 'esteditgrid', id:'gridproperty', storeurl: '<%=basePath%>est/sysinit/sysproperty/SysProperty/getPropertyListByParentId', colstype: colsType, region: 'center'}
		]
	};
		
/*＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝【主】属性管理部分结束＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝*/
		
	
/*＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝【子】属性管理部分开始＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝*/
	var colsTypeSub = {id: 'propertyid', 
		cols: [
			{dataIndex:'propertyid',header:'属性ID',wdith:0, name:'propertyid',type:'int',hidden:true},
			{dataIndex:'sysProperty.propertyid',header:'上级属性ID',width:0, name:'sysProperty.propertyid',type:'int',hidden:true},
			{dataIndex:'propertyname', header: '属性名称', width: 200, sortable: true, name:'propertyname',type:'string',editor: new Ext.form.TextField({allowBlank: false})},
			{dataIndex:'parentpropertyname', header: '上级属性',width: 100, sortable: true, name:'sysProperty.propertyname',type:'string',readOnly:true},
			{dataIndex:'propertycode', header: '属性编码',width: 100, sortable: true, name:'propertycode',type:'string',hidden:true},
			{dataIndex:'ordernum', header: '属性顺序',width: 100, sortable: true, name:'ordernum',type:'string',editor: new Ext.form.TextField({allowBlank: false})}
		]};
	
	var _SubProperty = Ext.data.Record.create(colsTypeSub.cols);
	
	var gridSubPropertyPanel = {
		xtype: 'estlayout',
		title: '子属性列表',
		id: 'gridsubpanel',
		tbar: [{
				text: '添加',
				handler:function(){
					var newSubProperty = new _SubProperty({
	                   propertyid: 0,
	                   sysProperty: {propertyid:selPropertyId},
	                   propertyname: '',
	                   parentpropertyname: '',
	                   propertycode: '',
	                   ordernum: ''
					});
			        
					var gridproperty = Ext.getCmp('gridsubproperty');
					gridproperty.addRow(newSubProperty);
				}
			},
			{
				text: '删除',
				handler:function(){
					var gridproperty = Ext.getCmp('gridsubproperty');
					gridproperty.deleteRow();
				}
			},{
				text: '保存',
				handler:function(){
					var gridproperty = Ext.getCmp('gridsubproperty');
					gridproperty.onSave('<%=basePath%>est/sysinit/sysproperty/SysProperty/savPropertyList');
				}
			}
		],
		items: [
			{xtype: 'esteditgrid', id:'gridsubproperty', storeurl: '<%=basePath%>est/sysinit/sysproperty/SysProperty/getPropertyListByParentId?parentPropertyId=0', colstype: colsTypeSub, region: 'center'}
		]
	};
		
/*＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝【子】属性管理部分结束＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝*/
		

	/**
	模板管理tab
	*/
	var tabPanel = {
		id: 'tabpanel',
		region: 'center',
		xtype: 'esttab',
		items: [gridMainPropertyPanel,gridSubPropertyPanel]
	};
	
	/*
		Viewport组件，包括：
			renderTo是组件渲染到的DOM对象，Ext.getBody()== document.body
	*/
	var module= new Ext.Viewport({
		layout: 'border',
		items: [ menuPanel, {
			xtype: 'estlayout',
			region: 'center',
			items: [tabPanel]
		}],
		renderTo: Ext.getBody()
	});
	
	//加载事件
	Ext.getCmp('gridproperty').on({'rowdblclick': function(){
			//Ext.getCmp('tabpanel').setActiveTab('gridsubpanel');
		},
		'rowclick': function(){
			Ext.getCmp('gridsubpanel').purgeListeners();
			
			Ext.getCmp('gridsubpanel').on({'afterlayout': function(){
				var id = Ext.getCmp('gridproperty').getSelectionModel().getSelected().id;
				selPropertyId = id;
				
				propertySubListUrl  ='<%=basePath%>est/sysinit/sysproperty/SysProperty/getPropertyListByParentId?parentPropertyId=' + selPropertyId;
		   
				Ext.getCmp('gridsubproperty').store.proxy.conn.url=propertySubListUrl;
				Ext.getCmp('gridsubproperty').store.reload();
				Ext.getCmp('gridsubpanel').purgeListeners();
			}
		   });
		}
		});	
	
	});
	</script>
</html>