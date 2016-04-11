<%@ page language="java" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache"); 
response.setHeader("Cache-Control","no-cache"); 

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<base href="<%=basePath%>">
	<%@ include file="/include.jsp"%>
	<script type="text/javascript" src="<%=basePath%>js/est/Portal.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/est/PortalColumn.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/est/Portlet.js"></script>
    
    
    <style>
    	.bdy{
    		text-align: center;
    	}
    	
    	A.fastnav:link,A.fastnav:visited{        /*超链接正常状态*/
	       color:blue;      /*颜色*/
		   text-decoration:none;    /*下划线无*/
		   font-size:10pt;
		   font-weight:bold;
		   padding:4px 10px 4px 10px;
		   background-color: #ecd8db;
		   text-decoration: none;
		   border-top: 1px solid #EEEEEE;  边框实现阴影效果 
		   border-left: 1px solid #EEEEEE;
		   border-bottom: 1px solid #717171;
		   border-right: 1px solid #717171;
		   Line-height:1.8;
	    }
	    A.fastnav:hover{/*鼠标经过时候的超链接*/
	       color:#9900FF; /*改变文字颜色*/
	       padding:5px 8px 3px 12px; /*改变文字位置*/
		   background-color:#00FF00; /*改变背景颜色*/
		   border-top:1px solid #717171; /*边框变换，实现按下去的效果*/
		   border-left:1px solid #717171; /*边框变换，实现按下去的效果*/
		   border-bottom:1px solid #EEEEEE; /*边框变换，实现按下去的效果*/
		   sborder-right:1px solid #EEEEEE; /*边框变换，实现按下去的效果*/
	     }                          

    
    </style>
    
</head>
<body>

</body>
<script type="text/javascript">
	Ext.QuickTips.init();
	var changePage = 0;
	
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
	
	Ext.onReady(function(){
		var colsType = {id: 'taskId', 
				cols: [
						{dataIndex:'taskId', header: 'taskId',hidden:true},
						{dataIndex:'taskDefinitionId', header: 'taskDefinitionId',hidden:true},
						{dataIndex:'processTitle', header: '流程描述',width: 80},
						{dataIndex:'draftby', header: '任务发起人', width: 85},
						{dataIndex:'taskName', header: '当前任务', width: 80},
						{dataIndex:'creatDate', header: '开始时间',width: 80},
						{dataIndex:'startDate', header: '接收时间',width: 80},
						{dataIndex:'endDate', header: '结束时间',width: 80},
						{dataIndex:'wfProcessInstanceId', header: 'wfProcessInstanceId',hidden:true},
						{dataIndex:'taskStatus', header: '任务状态',hidden:true},
						{dataIndex:'masterId', header: 'masterId',hidden:true}
					]};
					
		
		var assignedTaskToolbar = new Ext.Toolbar({
			items:['-','发起时间:从',{xtype:'datefield',id:'a_draftdatestart',format:'Y-m-d'},
				'到',{xtype:'datefield',id:'a_draftdateend',format:'Y-m-d'},
				{
					text:'查询',
					handler:doSearchAssignedTask
				},'-',{
				text:'<font color="red">接收</font>',
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
								Ext.getCmp('tabpanel').setActiveTab('gridpanelRecievedTask');
							},
							failure:function(){
								error('错误','任务接收失败！');
							}
						});
					} else {
						error('错误','您还未选择任务，请先选择一条待接收任务。');
					}
					
					
				}
			}]
		});
					
		var gridPanelAssignedTask = {
			xtype: 'estlayout',
			title: '待接收任务列表',
			id: 'gridpanelAssignedTask',
			tbar: [
				'-','主题:', {xtype:'textfield',id:'a_topic'},'-',
				'发起人:',{xtype:'textfield',id:'a_draftby',width:80}
				],
			listeners:{
				'render':function() {
					assignedTaskToolbar.render(this.tbar);
				}
			},
			items: [
				{xtype: 'esteditgrid', id:'gridAssignedTask',autoScroll:true,storeurl: '<%=basePath%>est/workflow/process/WfProcess/getAssignedTaskList', colstype: colsType, region: 'center'}
			]
		};
		
		
		var recievedTaskTbarDateQuery = new Ext.Toolbar({
			   items : ['-','发起时间:从',{xtype:'datefield',id:'r_draftdatestart',format:'Y-m-d'},
				'到',{xtype:'datefield',id:'r_draftdateend',format:'Y-m-d'},
				{
					text:'查询',
					handler:doSearchRecievedTask
				},'-',{
				text:'<font color="red">处理</font>',
				handler:function(){
					var selectedRow = Ext.getCmp('gridRecievedTask').getSelectionModel().getSelected();
					
					if(selectedRow){
						var taskid = selectedRow.id;
						var taskDefinitionId = selectedRow.data['taskDefinitionId']; 
						var wfProcessInstanceId = selectedRow.data['wfProcessInstanceId'];
						var masterId = selectedRow.data['masterId']; 
						var params = {taskid:taskid,taskDefinitionId:taskDefinitionId,wfProcessInstanceId:wfProcessInstanceId,masterId:masterId};
						var url = '<%=basePath%>est/contract/edit/ContractEdit/fwdContractedit';
						url += '?_isWf=1&_taskId=' + taskid + '&_masterid=' + masterId + '&_taskDefid=' + taskDefinitionId + '&_wfProcessInstanceId=' + wfProcessInstanceId + '&isMainView=1';
						Ext.getCmp('chartPanel').setActiveTab('disposePanel');
						window.disposeFrame.location.replace(url);
					} else {
						error('错误','您还未选择任务，请先选择一条待处理任务。');
					}
				}
			}]
		});
		
		var gridPanelRecievedTask = {
			xtype: 'estlayout',
			title: '待处理任务列表',
			id: 'gridpanelRecievedTask',
			tbar: [
			
				'-','主题:', {xtype:'textfield',id:'r_topic'},'-',
				'发起人:',{xtype:'textfield',id:'r_draftby',width:80}
				],
			listeners:{
				'render':function() {
					recievedTaskTbarDateQuery.render(this.tbar);
				}
			},
			items: [
				{xtype: 'esteditgrid', id:'gridRecievedTask',storeurl: '<%=basePath%>est/workflow/process/WfProcess/getRecievedTaskList', colstype: colsType, region: 'center'}
			]
		};
		
		var myDraftProcessColsType = {id: 'processinstanceId', 
				cols: [
						{dataIndex:'processinstanceId', header: 'wfProcessInstanceId',hidden:true},
						{dataIndex:'taskDefinitionId', header: 'taskDefinitionId',hidden:true},
						{dataIndex:'topic', header: '流程描述',width: 150},
						{dataIndex:'draftby', header: '任务发起人', width: 70},
						{dataIndex:'draftdate', header: '发起时间',width:80},
						{dataIndex:'enddate', header: '结束时间',width: 80},
						{dataIndex:'flowstate', header: '状态',width: 80}
					]};
		
		var myDraftProcessTbarDateQuery = new Ext.Toolbar({
			   items : ['-','发起时间:从',{xtype:'datefield',id:'m_draftdatestart',format:'Y-m-d'},
				'到',{xtype:'datefield',id:'m_draftdateend',format:'Y-m-d'},{
					text:'查询',
					handler:doSearchMyDraftProcess
				}]
		});
		
		var gridPanelMyDraftProcess = {
			xtype: 'estlayout',
			title: '我发起的流程列表',
			id: 'gridpanelMyDraftProcess',
			tbar: [
				'-','主题:', {xtype:'textfield',id:'m_topic'},'-',
				'状态:',{xtype: 'estcombos', id:'m_status', elms: [['流程中','流程中'],['已完成','已完成']],width:100}
				/*'发起时间:从',{xtype:'datefield',id:'m_draftdatestart',format:'Y-m-d'},
				'到',{xtype:'datefield',id:'m_draftdateend',format:'Y-m-d'},
				{
					text:'查询',
					handler:doSearchMyDraftProcess
				},*/
			],
			listeners : {
				'render':function(){
					myDraftProcessTbarDateQuery.render(this.tbar);
				}
			},
			items: [
				{xtype:'estlayout',id:'mydraftprocesspanel',region:'center',
					items:[
						{xtype:'esteditgrid', id:'gridMyDraftProcess',storeurl: '<%=basePath%>est/workflow/process/WfProcess/getMyDraftProcessList', colstype: myDraftProcessColsType, region: 'center'}
						
					]
				}
			]
		};

		var tabPanel = {
			id: 'tabpanel',
			region: 'center',
			width:'35%',
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
				
		var disposeIframe = {
			id:'disposeIframe',
			region:'center',
			layout:'fit',
			xtype:'estlayout',
			html:'<iframe name="disposeFrame" src="est/contract/edit/ContractEdit/fwdContractedit?isMainView=1" width=100% height=100% />'
		}
				
		var disposePanel = {
			id:'disposePanel',
			region:'center',
			layout:'fit',
			title:'审批',
			xtype:'estlayout',
			items:[disposeIframe]
		}
				
		//==========================================图表===================================================
		var moneyIframe = {
			id:'quIframe',
			region:'center',
			layout:'fit',
			xtype:'estlayout',
			html:'<iframe src="est/contract/portal/ContractPortal/toMoneyPortalJsp" width=100% height=100% />'
		}
		
		var moneyPanel = {
			xtype:'estlayout',
			title:'合同金额',
			items:[
				moneyIframe
			]
		}
		
		
		
		//---------------------------------------------------来煤--------------------------------------------------------------------------
		var countIframe = {
			id:'quPanel',
			region:'center',
			layout:'fit',
			xtype:'estlayout',
			html:'<center><font style="font-size:36;color:blue;padding-top:120px;margin-top:120px;">欢迎使用过磅系统</font></center>'
		}
		
		//来煤panel
		var countPanel = {
			xtype:'estlayout',
			title:'首页',
			items:[
				countIframe
			]
		}
	
		
		
		//图形主panel
		var chartPanel = {
			xtype:'esttab',
			region:'center',
			id:'chartPanel',
			items:[
				countPanel/*,moneyPanel,disposePanel*/
			]
		}
		
	   		// 视图
		var module= new Ext.Viewport({
			id: 'module',
			layout: 'border',
			items: [
				 menuPanel, 
				 {
				 	xtype:'estlayout',
				 	layout:'fit',
				 	region:'center',
				 	items:{
				 		id:'_mainpanel',
				 		xypte:'panel',
				 		region:'center',
				 		layout:'border',
				 		items:[
				 		/*tabPanel,*/chartPanel
				 		]
				 	
				 	}
				 }
				
			],
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
		Ext.getCmp('disposePanel').hide();
	})
</script>
</html>