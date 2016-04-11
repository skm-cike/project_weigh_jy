<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
	
</head>
<body>
</body>
<script type="text/javascript">
	Ext.onReady(function(){
		
		Ext.QuickTips.init();
		
		var colsType = {id: 'taskId', 
				cols: [
						{dataIndex:'taskId', header: 'taskId', width: '30',hidden:true},
						{dataIndex:'taskDefinitionId', header: 'taskDefinitionId', width: '30',hidden:true},
						{dataIndex:'processTitle', header: '流程描述',width: '40'},
						{dataIndex:'taskName', header: '当前任务', width: '30'},
						{dataIndex:'draftby', header: '任务发起人', width: '30'},
						{dataIndex:'creatDate', header: '开始时间',width: '40'},
						{dataIndex:'startDate', header: '接收时间',width: '40'},
						{dataIndex:'endDate', header: '结束时间',width: '40'},
						{dataIndex:'wfProcessInstanceId', header: 'wfProcessInstanceId', width: '30',hidden:true},
						{dataIndex:'taskStatus', header: '任务状态',width: '40',hidden:true},
						{dataIndex:'masterId', header: 'masterId', width: '30',hidden:true}
					]};
					
		
					
		var doSearchAssignedTask = function(){
			var topic = Ext.getCmp('a_topic').getValue();
			var draftby = Ext.getCmp('a_draftby').getValue();
			var draftdatestart = Ext.getCmp('a_draftdatestart').getValue();
			var draftdateend = Ext.getCmp('a_draftdateend').getValue();
			Ext.getCmp('gridAssignedTask').doSearch({topic:topic,draftby:draftby,draftdatestart:draftdatestart,draftdateend:draftdateend});
		}
		
		var doSearchRecievedTask = function(){
			var topic = Ext.getCmp('r_topic').getValue();
			var draftby = Ext.getCmp('r_draftby').getValue();
			var draftdatestart = Ext.getCmp('r_draftdatestart').getValue();
			var draftdateend = Ext.getCmp('r_draftdateend').getValue();
			Ext.getCmp('gridRecievedTask').doSearch({topic:topic,draftby:draftby,draftdatestart:draftdatestart,draftdateend:draftdateend});
		}
		
		var doSearchMyDraftProcess = function(){
			var topic = Ext.getCmp('m_topic').getValue();
			var status = Ext.getCmp('m_status').getValue();
			var draftdatestart = Ext.getCmp('m_draftdatestart').getValue();
			var draftdateend = Ext.getCmp('m_draftdateend').getValue();
			Ext.getCmp('gridMyDraftProcess').doSearch({topic:topic,status:status,draftdatestart:draftdatestart,draftdateend:draftdateend});
			
		}
					
		var gridPanelAssignedTask = {
			xtype: 'estlayout',
			title: '待接收任务列表',
			id: 'gridpanelAssignedTask',
			tbar: [
				'主题:', {xtype:'textfield',id:'a_topic'},
				'发起人:',{xtype:'textfield',id:'a_draftby',width:80},
				'发起时间:从',{xtype:'datefield',id:'a_draftdatestart',format:'Y-m-d'},
				'到',{xtype:'datefield',id:'a_draftdateend',format:'Y-m-d'},
				{
					text:'查询',
					handler:doSearchAssignedTask
				},'-',{
				text:'接收',
				handler: function(){
					var selectedRow = Ext.getCmp('gridAssignedTask').getSelectionModel().getSelected();
					if(selectedRow){
						var taskid = selectedRow.id;
						Ext.Ajax.request({
							url:'<%=basePath%>est/workflow/process/WfProcess/recieveTask',
							params:{taskid:taskid},
							success:function(){
								Ext.getCmp('gridAssignedTask').doSearch();
								Ext.getCmp('gridRecievedTask').doSearch();
							},
							failure:function(){
								error('错误','任务接收失败！');
							}
						});
					} else {
						error('错误','您还未选择任务，请先选择一条待接收任务。');
					}
					
					
				}
			}],
			items: [
				{xtype: 'estgrid', id:'gridAssignedTask',storeurl: '<%=basePath%>est/workflow/process/WfProcess/getAssignedTaskList', colstype: colsType, region: 'center'}
			]
		};
		var gridPanelRecievedTask = {
			xtype: 'estlayout',
			title: '待处理任务列表',
			id: 'gridpanelRecievedTask',
			tbar: [
				'主题:', {xtype:'textfield',id:'r_topic'},
				'发起人:',{xtype:'textfield',id:'r_draftby',width:80},
				'发起时间:从',{xtype:'datefield',id:'r_draftdatestart',format:'Y-m-d'},
				'到',{xtype:'datefield',id:'r_draftdateend',format:'Y-m-d'},
				{
					text:'查询',
					handler:doSearchRecievedTask
				},'-',{
				text:'处理',
				handler:function(){
					var selectedRow = Ext.getCmp('gridRecievedTask').getSelectionModel().getSelected();
					
					if(selectedRow){
						var taskid = selectedRow.id;
						var taskDefinitionId = selectedRow.data['taskDefinitionId']; 
						var wfProcessInstanceId = selectedRow.data['wfProcessInstanceId'];
						var masterId = selectedRow.data['masterId']; 
						Ext.Ajax.request({
							url:'<%=basePath%>est/workflow/process/WfProcess/getHandleUrl',
							params:{taskid:taskid,taskDefinitionId:taskDefinitionId,wfProcessInstanceId:wfProcessInstanceId,masterId:masterId},
							success:function(response, options){
								 var responseJson = Ext.util.JSON.decode(response.responseText);  
				   				 if(responseJson.success){
				   				 	var url = responseJson.url;
				   				 	fwdmodule(url);
				   				 }
							
							},
							failure:function(){
								error('错误','连接失败！');
							}
						});
					} else {
						error('错误','您还未选择任务，请先选择一条待处理任务。');
					}
				}
			}],
			items: [
				{xtype: 'estgrid', id:'gridRecievedTask',storeurl: '<%=basePath%>est/workflow/process/WfProcess/getRecievedTaskList', colstype: colsType, region: 'center'}
			]
		};
		
		var myDraftProcessColsType = {id: 'processinstanceId', 
				cols: [
						{dataIndex:'processinstanceId', header: 'wfProcessInstanceId',hidden:true},
						{dataIndex:'taskDefinitionId', header: 'taskDefinitionId', width: '30',hidden:true},
						{dataIndex:'topic', header: '流程描述',width: '40'},
						{dataIndex:'draftby', header: '任务发起人', width: '30'},
						{dataIndex:'draftdate', header: '发起时间',width: '40'},
						{dataIndex:'enddate', header: '结束时间',width: '40'},
						{dataIndex:'flowstate', header: '状态',width: '40'}
					]};
		
		var gridPanelMyDraftProcess = {
			xtype: 'estlayout',
			title: '我发起的流程列表',
			id: 'gridpanelMyDraftProcess',
			tbar: [
				'主题:', {xtype:'textfield',id:'m_topic'},
				'状态:',{xtype: 'estcombos', id:'m_status', elms: [['流程中','流程中'],['已完成','已完成']],width:80},
				'发起时间:从',{xtype:'datefield',id:'m_draftdatestart',format:'Y-m-d'},
				'到',{xtype:'datefield',id:'m_draftdateend',format:'Y-m-d'},
				{
					text:'查询',
					handler:doSearchMyDraftProcess
				},'-'
			],
			items: [
				{xtype:'estlayout',id:'mydraftprocesspanel',region:'center',
					items:[
						{xtype:'estgrid', id:'gridMyDraftProcess',storeurl: '<%=basePath%>est/workflow/process/WfProcess/getMyDraftProcessList', colstype: myDraftProcessColsType, region: 'center'}
						
					]
				}
			]
		};

		var tabPanel = {
			id: 'tabpanel',
			region: 'center',
			xtype: 'esttab',
			items: [gridPanelAssignedTask,gridPanelRecievedTask,gridPanelMyDraftProcess]
		};
		
		var treePanel = {xtype: 'esttree',isctx: true, rootTxt:'所有部门', id:'treepanel', region:'west',loaderurl: 'est/sysinit/sysdept/SysDept/getDepartTree',
				ctx: {url: '<%=basePath%>est/sysinit/sysdept/SysDept', method: 'Dept', ctxitems:[{id:'add',text:'添加'},{id:'modify',text:'修改'},{id:'delete',
					text:'刪除'}],
					formitems:
					 [{id:'deptid', fieldset:'Login', colnum: 1,
					 items:[{fieldLabel:'部门名称',name:'deptname',allowBlank: false},
					        {fieldLabel:'父级部门',xtype:'estcombos',hiddenName:'sysDept.deptid', valueField:'deptid', displayField:'deptname', storeurl:'<%=basePath%>est/sysinit/sysdept/SysDept/getAllDepart',  allowedempty: true},
							{fieldLabel:'部门编号',name:'deptcode',hidden:true,hideLabel:true}
						]}]}
				};
		
		
		new Ext.Viewport({
			layout: 'border',
			id:'viewport',
			items: [ menuPanel, {
				xtype: 'estlayout',
				region: 'center',
				items: [tabPanel]
			}],
			renderTo: Ext.getBody()
		});
		
		var approveHistoryTab = new Est.ux.WfApproveHistoryTab({bindGrid:'gridMyDraftProcess',bindflag : false,region:'south',height:200});
		Ext.getCmp('mydraftprocesspanel').add(approveHistoryTab);
		
		Ext.getCmp('gridMyDraftProcess').on({
			'rowclick': function(){
						var selectedRow = Ext.getCmp('gridMyDraftProcess').getSelectionModel().getSelected();
						if(selectedRow) {
							var piid = selectedRow.data['processinstanceId'] ;
							var store = Ext.getCmp('wfApproveHistoryGrid').doSearch({piid: piid});
						}
					}
				});
		
	
		/*
		Ext.getCmp('grid').on({'rowdblclick': function(){
				Ext.getCmp('tabpanel').setActiveTab('detail');
			},
			'rowclick': function(){
				Ext.getCmp('detail').purgeListeners();
				Ext.getCmp('detail').on({'afterlayout': function(){
						var id = Ext.getCmp('grid').getSelectionModel().getSelected().id;
						if(id) {
							console.log(Ext.getCmp('grid').getSelectionModel().getSelected());
							Ext.getCmp('formpanel').doLoad({userid: id});
						}
						Ext.getCmp('detail').purgeListeners();
					}
				});
			}
		});
		*/
		/*
		Ext.getCmp('treepanel').on({'click': function(node,e){
			Ext.getCmp('grid').doSearch({'deptId':node.id},true);
			}
		});

		Ext.getCmp('formpanel').addRefObjs(Ext.getCmp('grid'));
		Ext.getCmp('grid').addRefObjs(Ext.getCmp('formpanel'));
		Ext.getCmp('searchpanel').addRefObjs(Ext.getCmp('grid'));
		*/
	});
	</script>
</html>