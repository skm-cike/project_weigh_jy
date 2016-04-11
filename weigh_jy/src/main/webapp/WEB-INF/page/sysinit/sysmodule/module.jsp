<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!-- 
位置：系统管理-模块管理
说明：设置模块
路径：/fuel_jt/est/sysinit/sysmodule/SysModule/fwdModule
作者：smile-bug
时间：2009
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<base href="<%=basePath %>">
	<%@ include file="/include.jsp"%>
</head>
<body>
</body>
<script type="text/javascript">
Ext.onReady(function(){
	Ext.QuickTips.init();
	var moduleId = 0;
	var searchPanel  = {
		xtype: 'estsearchpanel',
		id: 'searchpanel',
		region: 'north',
		formitems:  [{fieldset:'搜索',items:[{fieldLabel:'模块名称',name:'modulename'}]}]
	};
	var formcols = [{id:'moduleid',fieldset:'详细',
		items: [{fieldLabel:'模块名称',name:'modulename', allowBlank: false},
		        {fieldLabel:'模块地址', colspan: 2, width: 400, name:'url'},
		        {fieldLabel:'模块排序',name:'orderindex',allowBlank: false},
				{fieldLabel:'是否公共',xtype: 'estcombos', hiddenName:'publicflag', elms: [['是','是'],['否','否']]},
				{fieldLabel:'是否有效',xtype: 'estcombos', hiddenName:'isvalidity', elms: [['是','是'],['否','否']]},
				{fieldLabel:'是否工作流',xtype: 'estcombos', hiddenName:'iswfflag', elms: [['是','是'],['否','否']]},
				{fieldLabel:'显示子菜单',xtype: 'estcombos', hiddenName:'showsubmenu', elms: [['是','是'],['否','否']]},
				{
			       	fieldLabel:'父级模块',xtype:'esttreecombo',	//下拉树选择框
			       	id:'newParentNodeInfo',
			       	height:200,
			       	treeWidth:300,
			       	displayField:'sysModule.modulename',
			       	valueField:'sysModule.moduleid',
			       	hiddenName:'sysModule.moduleid',
			       	dataUrl:'<%=basePath%>est/sysinit/sysmodule/SysModule/getModuleTree',	//树加载url
			       	rootId:'0',	//根结点id
			       	rootText:'模块树', //根结点text
			        allowedempty: true,
			        alwaysReload : true
			        },
				
				{name:'modulecode',xtype:'hidden'},
				{fieldLabel:'文件存放地址', name:'fileurl', colspan: 2,width:300}
				
				]}];

	var formPanel = {
		xtype: 'estform',
		id: 'formpanel',
		formurl: '<%=basePath%>est/sysinit/sysmodule/SysModule/',
		method: 'Module',
		title: '详细',
		colstype: formcols,
		tbar: [{text:'重置/增加',id:'add', xtype: 'esttbbtn', handler:function(){Ext.getCmp('formpanel').doReset();}},
		       {text:'保存', id:'sav', xtype:'esttbbtn', handler: function(){Ext.getCmp('formpanel').doSumbit()}},
				{text:'刪除', xtype:'esttbbtn', id:'del', isconfirm: true, handler:function(){Ext.getCmp('formpanel').doDelete()}}]
	};
	var treePanel = {xtype: 'esttree',isctx: false, id:'treepanel', region:'west',rootTxt:'所有模块',
	loaderurl: '<%=basePath%>est/sysinit/sysmodule/SysModule/getModuleTree'};
	
	var colsType = {id: 'moduleid', 
			cols: [{dataIndex:'modulename', header: '模块名称', width: .3, sortable: false},
					{dataIndex:'url', header: '模块地址',width: .5, sortable: true},
					{dataIndex:'orderindex', header: '排序',width: .2, sortable: true}
			]}
	var gridPanel = {
		xtype: 'estlayout',
		title: '模块列表',
		id: 'gridpanel',
		tbar: [{
			text: '搜索',
			xtype: 'estsearchbtn',
			spId: 'searchpanel',
			pId: 'module'
		},'-'],
		items: [
			{xtype: 'estgrid', id:'grid', storeurl: '<%=basePath%>est/sysinit/sysmodule/SysModule/getModuleList', colstype: colsType, region: 'center'}, searchPanel
		]
	};
	
	
	/****** 模块字段 */
	var colsTypeField = {id: 'fieldid', 
		cols: [
			{dataIndex:'fieldid',header:'ID',name:'fieldid',type:'int',hidden:true},
			{dataIndex:'sysModule.moduleid', header:'模块ID',name:'sysModule.moduleid',type:'int',hidden:true},
			{dataIndex:'fieldcode', header: '字段名称', width: 300, sortable: true, name:'fieldcode',type:'string',editor: new Ext.form.TextField({allowBlank: false})},
			{dataIndex:'fieldname', header: '字段描述',width: 200, sortable: true, name:'fieldname',type:'string',editor: new Ext.form.TextField({allowBlank: false})},
			{dataIndex:'ordernum', header: '排序号',width: 200, sortable: true, name:'ordernum',type:'int',editor: new Ext.form.NumberField()}
		]};
	var _ModuleField = Ext.data.Record.create(colsTypeField.cols);
	
	var gridFieldPanel = {
		xtype: 'estlayout',
		title: '模块字段列表',
		id: 'gridfieldpanel',
		tbar: [{
				text: '添加',
				handler:function(){
					var newModuleField = new _ModuleField({
	                   fieldid: 0,
	                   sysModule: {moduleid:moduleId},
	                   fieldcode: '',
	                   fieldname: '',
	                   ordernum:''
					});
			        
					var gridField = Ext.getCmp('gridmodulefield');
					gridField.addRow(newModuleField);
				}
			},
			{
				text: '删除',
				handler:function(){
					var gridField = Ext.getCmp('gridmodulefield');
					gridField.deleteRow();
				}
			},{
				text: '保存',
				handler:function(){
					var gridField = Ext.getCmp('gridmodulefield');
					gridField.onSave('<%=basePath%>est/sysinit/sysmodule/SysModule/savSysModulefieldList');
				}
			}
		],
		items: [
			{xtype: 'esteditgrid', id:'gridmodulefield', storeurl: '<%=basePath%>est/sysinit/sysmodule/SysModule/getFieldListByModuleId', baseParams:{moduleid:0},colstype: colsTypeField, region: 'center'}
		]
	};
	
	
	/****** 模块字段结束 */
	var tabPanel = {
		id: 'tabpanel',
		region: 'center',
		xtype: 'esttab',
		items: [gridPanel,{xtype:'estlayout',frame:true,title: '详细信息', id:'detail', layout:'fit', items:[formPanel]},gridFieldPanel]
	};
	
	
	var module= new Ext.Viewport({
		layout: 'border',
		id: 'module',
		items: [ menuPanel, {
			xtype: 'estlayout',
			region: 'center',
			items: [tabPanel,treePanel]
		}],
		renderTo: Ext.getBody()
	});


	/*handle event*/
	Ext.getCmp('grid').on({'rowdblclick': function(){
			Ext.getCmp('tabpanel').setActiveTab('detail');
		},
		'rowclick': function(){
			var id = Ext.getCmp('grid').getSelectionModel().getSelected().id;
			if(id) {
				moduleId = id;
				Ext.getCmp('detail').purgeListeners();
				Ext.getCmp('detail').on({'afterlayout': function(){
							console.log(Ext.getCmp('grid').getSelectionModel().getSelected());
							
							Ext.getCmp('formpanel').doLoad({moduleid: id});
							Ext.getCmp('detail').purgeListeners();
						}
						
					
				});
				
				Ext.getCmp('gridfieldpanel').purgeListeners();
				Ext.getCmp('gridfieldpanel').on({'afterlayout': function(){
							Ext.getCmp('gridmodulefield').doSearch({moduleid: id});
							Ext.getCmp('gridfieldpanel').purgeListeners();
						}
						
					
				});
		    }
		}
	});
	
	Ext.getCmp('treepanel').on({'click': function(node,e){
				Ext.getCmp('grid').doSearch({'parentModuleId':node.id},true);
			}
		});
	
	Ext.getCmp('formpanel').addRefObjs(Ext.getCmp('grid'));
	Ext.getCmp('grid').addRefObjs(Ext.getCmp('formpanel'));
	Ext.getCmp('searchpanel').addRefObjs(Ext.getCmp('grid'));
});
</script>
</html>