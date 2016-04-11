<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
	
    <title>种类配置</title>
    <%@ include file="/include.jsp"%>	
    <script type="text/javascript">
    	Ext.onReady(function(){
			var colsType = {
				id:'id',
				cols:[
					{dataIndex:'kindName',	header:'种类名称',name:'kindName',width:75, editor:new Ext.form.TextField()},
    				{dataIndex:'kindCode',	header:'种类代码',name:'kindCode',width:80, editor:new Ext.form.TextField()},
    				{dataIndex:'kindDesc', header:'描述', name:'kindDesc',width:150, editor:new Ext.form.TextField()},
    				{dataIndex:'tableName', hidden:true},
					{dataIndex:'id',hidden:true}
				]
			}
			
			var g_colsType = Ext.data.Record.create(colsType.cols);	
			
   			var GridPanel = {
    			id:'GridPanel',
   				xtype: 'estlayout',
   				region:'west',
   				split:true,//显示分隔条
   				collapsible:true,
   				width:300,
   				tbar:[{text:'添加', handler: function() {
   					var v_colsType = new g_colsType({
		                    dataIndex: '',
		                    kindCode: '',
		                    kindDesc: ''
		                });
				    var grid = Ext.getCmp('gridpanelid').addRow(v_colsType);
   				}},{text:'删除', handler: function() {
   					Ext.getCmp('gridpanelid').deleteRow();
   				}},{text:'保存', handler: function() {
   					Ext.getCmp('gridpanelid').onSave('<%=basePath%>est/weigh/kindcfg/Kind/savKinds');
   				}}],
	   			items:[
	   				{title: '过磅种类',xtype: 'esteditgrid',id:'gridpanelid', storeurl: '<%=basePath%>est/weigh/kindcfg/Kind/getKindsByPage', colstype: colsType, region: 'center'}
	   			]
    		}
    	
			
			var col_tablefield = {
					id:'id',
					cols:[
						{dataIndex:'tableAttribute', header:'字段名称', name:'tableAttribute', editor:new Ext.form.TextField()},
						{dataIndex:'tableAttrtype', header:'字段类型', name:'tableAttrtype', editor:new Est.ux.PropertyComboBox({propertycode:'LELEIXING'})},
						{dataIndex:'tableName', header:'表名', name:'tableName'},
						{dataIndex:'tableDesc', header:'描述', name:'tableDesc', editor:new Ext.form.TextField()},
						{dataIndex:'id', hidden:true, name:'id'},
						{dataIndex:'tableId', hidden:true, name:'tableId'},
						/*下为种类所需字段*/
						{dataIndex:'kindid', hidden:true, name:'kindid'},
						{dataIndex:'kindCode', hidden:true, name:'kindCode'},
						{dataIndex:'fieldName', hidden:true, name:'fieldName'},
						{dataIndex:'fieldType', header:'显示字段类型', name:'fieldType', editor:new Est.ux.PropertyComboBox({propertycode:'ZLPZZDLX'})},
						{dataIndex:'propertyCode', header:'字段配置代码', name:'propertyCode', editor:new Ext.form.TextField()},
						{dataIndex:'readonly', header:'是否只读', name:'readonly', editor:new Est.ux.PropertyComboBox({propertycode:'SFZD'}), renderer:function(v) {
							if (v == 0 || v == '0') {
								return '否';
							} else {
								return '是';
							}
						}},
						{dataIndex:'isInput', header:'作为录入', name:'isInput', editor:new Est.ux.PropertyComboBox({propertycode:'SFZD'}), renderer:function(v) {
							if (v == 0 || v == '0') {
								return '否';
							} else {
								return '是';
							}
						}},
						{dataIndex:'isQuery', header:'作为查询', name:'isQuery', editor:new Est.ux.PropertyComboBox({propertycode:'SFZD'}), renderer:function(v) {
							if (v == 0 || v == '0') {
								return '否';
							} else {
								return '是';
							}
						}},
						{dataIndex:'isShow', header:'作为列显示', name:'isShow', editor:new Est.ux.PropertyComboBox({propertycode:'SFZD'}), renderer:function(v) {
							if (v == 0 || v == '0') {
								return '否';
							} else {
								return '是';
							}
						}}
					]
			}
			var _col_tablefield = new Ext.data.Record.create(col_tablefield.cols);
				
			var win_newtable = new Ext.Window({
				id:'win_newtable',
				title:'新建表',
				width:350,
				height:180,
				closeAction:'hide',
				frame:true,
				plain:true,
				items:[{
					xtype:'estform',
					id:'form_newtable',
					//formurl: 'est/asset/jobplan/JobPlan/',
					//method: 'JobPlan',
					colnum:1,
					colstype:[{
							fieldset:'标准作业',items:[
							{fieldLabel:'表名',xtype:'textfield',name:'tableName',allowBlank:false,width:150},
							{fieldLabel:'描述',xtype:'textfield',name:'tableDesc',width:150}]
						}
					]
				}],
				buttons:[{
					text:'确定',
					handler:function(){
						var selected = Ext.getCmp('gridpanelid').getSelectionModel().getSelected();
						var _kindid = selected.data['id'];
						var form = Ext.getCmp('form_newtable');
						if(!form.form.isValid()){
							return;
						}
						form.doSumbit({url: '<%=basePath%>est/weigh/kindcfg/Kind/createTable', method:'POST', waitMsg: '新建中...',
							success: function(form, rep){
								if (rep.success){
									var tableName = form.findField('tableName').getValue();
									var selected = Ext.getCmp('gridpanelid').getSelectionModel().getSelected();
									selected.data['tableName']=tableName;
									Ext.getCmp('tablefield_panelid').doSearch({'tableName':tableName},true);
									form.reset();
									win_newtable.hide();
								} else {
									error('错误', rep.error);
								}
							   
							},
							failure: function(form, rep){
								error('错误',rep.error);
							},
							params:{kindid:_kindid}
						});
					}
				},{
					text:'取消',
					handler:function(){
						win_newtable.hide();
					}
				}]
			
			});

			var tablefield_panel = {
				id: 'tab_opinion',
				xtype: 'estlayout',
				region: 'center',
				tbar:[{text:'新建表', handler: function() {
					var selected = Ext.getCmp('gridpanelid').getSelectionModel().getSelected();
					if (!selected) {
						Ext.Msg.alert('消息', '请先选择一个种类!');
						return;
					}
					var kindid = selected.data['id'];
					if (!kindid || ''==kindid) {
						Ext.Msg.alert('提示', '该种类是否保存?');
						return;
					}
					var tableName = selected.data['tableName'];
					if (!tableName || tableName=='') {
						win_newtable.show();
					} else {
						var count = Ext.getCmp('tablefield_panelid').getStore().getCount();
						if (count != 0) {
							Ext.Msg.alert('提示', '该种类已经有主表！');
						} else {
							win_newtable.show();
						}
					}
					
					
				}},{text:'添加字段', handler:function() {
					var tableName = '';
					var tableId = '';
					var _clickTableName = Ext.getCmp('gridpanelid').getSelectionModel().getSelected().data['tableName'];
					if (!_clickTableName || _clickTableName == '') {
						Ext.Msg.alert('提示','请先建表!');
						return;
					}
					Ext.getCmp('tablefield_panelid').getStore().each(function(record) {
						if (record.data['tableId']) {
							tableName = record.data['tableName'];
							tableId = record.data['tableId'];
							return;
						}
					});
					var new_col_tablefield = new _col_tablefield({
		                    tableAttribute: '',
		                    tableAttrtype: '',
		                    tableName: tableName,
		                    tableDesc:'',
		                    id: '',
		                    tableId: tableId
		                });
				    Ext.getCmp('tablefield_panelid').addRow(new_col_tablefield);
				}},{text:'删除', handler:function() {
					Ext.getCmp('tablefield_panelid').deleteRow();
				}},{text:'保存', handler:function() {
					var selected = Ext.getCmp('gridpanelid').getSelectionModel().getSelected();
					if (!selected) {
						return;
					}
					var kindCode = selected.data['kindCode'];
					var isDelTable = false;
					Ext.getCmp('tablefield_panelid').getStore().each(function(record) {
						if ((record.data['tableId'] == record.data['tableAttribute']) && record["modifystatus"] == "d") {
							isDelTable = true;
						}
						record.data['kindCode'] = kindCode;
						record.data['fieldName'] = record.data['tableAttribute'];
					})
					if (isDelTable) {
						Ext.Msg.confirm('提示','你是否确实要删除整张表?', function(btn) {
							if (btn == 'yes') {
								Ext.getCmp('tablefield_panelid').onSave('<%=basePath%>est/weigh/table/Table/savFields', function(rep) {
									var msgObj = Ext.decode(rep.responseText);
									var kindid = Ext.getCmp('gridpanelid').getSelectionModel().getSelected().data['id'];
									if (msgObj.success) {
										Ext.Ajax.request({
											url : '<%=basePath%>est/weigh/kindcfg/Kind/clearTable',
											params :{kindid:kindid},
											waitMsg : '正在删除...',
											success: function(rep) {
												var rst = rep.responseText;
												if (rst.success) {
													Ext.getCmp('gridpanelid').getSelectionModel().getSelected().data['tableName']=null;
												}
											}
										});
									}
								});
							}
						});
					} else {
						Ext.getCmp('tablefield_panelid').onSave('<%=basePath%>est/weigh/kindcfg/Kind/savFields');
					}
					
				}}],
				items: {
					title: '主表拥有字段',
					xtype: 'esteditgrid',
					id: 'tablefield_panelid',
					colstype: col_tablefield,
					region: 'center',
					autoLoad: false,
					storeurl: '<%=basePath%>est/weigh/kindcfg/Kind/getTabFields'
				}
			}
			
			/*
			var totalGridPanel = {
				layout:'border',
				region:'center',
				items:[GridPanel,tablefield_panel]
			}
    		*/
    		new Ext.Viewport({
				layout: 'border',
				items: [ menuPanel, {
					xtype: 'estlayout',
					region: 'center',
					items: [GridPanel,tablefield_panel]
				}],
				renderTo: Ext.getBody()
			}); 
			
			Ext.getCmp('gridpanelid').on({'rowdblclick':function() {
				//Ext.getCmp('tabPanel').setActiveTab('tab_opinion');
			},'rowclick':function(t, i) {
				var tableName = t.getSelectionModel().getSelected().data['tableName'];
				Ext.getCmp('tablefield_panelid').doSearch({'tableName':tableName},true);
			}});
			
    	});
    	
    </script>	
  </head>
  <body>
  </body>
</html>
