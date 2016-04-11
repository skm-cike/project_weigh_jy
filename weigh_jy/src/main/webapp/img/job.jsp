<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>标准作业</title>
    <%@ include file="/include.jsp"%>
    <script type="text/javascript">
    	
    	var jobtypeid = 0; // 被选中的作业分类的id
    	var jobplanid = 0; // 被选中的计划的id
    	
   	Ext.onReady(function(){
   	
   	// 资源列表grid
   	var resurceGridPanel={
   		id:'resurceGridPanel',
   		title:'资源列表'
   	}
   	
   	// 任务列表的要显示的字段
   	var jobtaskColstype ={
   	
   		id:'jobtaskColstype',
				cols:[
					 {dataIndex:'jobtaskid',   name:'jobtaskid',  header: '任务ID',width:100,hidden:true},
					 {dataIndex:'taskno', name:'taskno',header: '序号',width:100},
                     {dataIndex:'meternameid', name:'meternameid',header: '任务编号',width:300},
                     {dataIndex:'taskdesc', name:'taskdesc',header: '作业描述',width:300}
				]
   	
   	};
   	
   	// 标准作业下的任务列表
   	var taskGridPanel={
   		id:'taskGridPanel',
		region:'center',
		xtype:'panel',
		title:'标准作业计划',
		ayout:'fit',
			tbar:[
				  {text:'增加'},
				  {text:'导入'},
				  {text:'保存',handler:function(){Ext.getCmp('taskdetailGrid').onSave('est/asset/jobplan/JobTask/savJobTaskList')}},
				  {text:'删除',handler:function(){Ext.getCmp('taskdetailGrid').deleteRow();}}
				],
				items:[
					{xtype: 'esteditgrid', id:'taskdetailGrid', storeurl: 'est/asset/jobplan/JobTask/getJobTaskPageList', colstype: jobtaskColstype}
				]
   	}
   	
   	//标准作业详细页面提示的字段
   	var formcols = [{id:'jobplanid',fieldset:'标准作业',
				items: [
					{fieldLabel:'作业编码',name:'jobplancode',width:200, allowBlank: false},
					{fieldLabel:'作业类别ID',name:'jobType.jobtypeid',width:200, hidden:true},
					{fieldLabel:'作业描述',name:'jobplandesc',width:500, allowBlank: false}
			]}];
   	
   		//标准作业详细页面的提示
   		var planFormPanel={
				xtype: 'estform',
				id: 'planFormPanel',
				formurl: 'est/asset/jobplan/JobPlan/',
				method: 'JobPlan',
				colstype: formcols,
				tbar: [{text:'重置/增加',id:'add', xtype: 'esttbbtn', handler:function(){Ext.getCmp('planFormPanel').doReset();}},
			       {text:'保存', id:'sav', xtype:'esttbbtn', handler: function(){
			       			if(jobtypeid==0)
			       			{
			       				alert("没有选择作业类别");
			       				return;
			       			}
			      			var planFormPanelForm=Ext.getCmp('planFormPanel').form;
			      				if(jobtypeid);
			      				{
									planFormPanelForm.findField('jobType.jobtypeid').setValue(jobtypeid);
								}
			       			Ext.getCmp('planFormPanel').doSumbit()}},
			       			
					{text:'刪除', xtype:'esttbbtn', id:'del', isconfirm: true, handler:function(){Ext.getCmp('planFormPanel').doDelete()}}]
			};
   		
   		//标准作业列表要显示的字段
   		var colsType = {id: 'jobplanid', 
				cols: [ {dataIndex:'jobplancode',	header: '计划编码', width:200, sortable: false},
					 	{dataIndex:'jobType.jobtypename', 	header: '计划类型',width:200, sortable: true},
						{dataIndex:'jobplandesc', 	header: '计划描述',width:800, sortable: true},
						{dataIndex:'jobplanid', 	header: '计划ID',width: .5, sortable: true,hidden:true}
 					  ]}
   		
   		//标准作业列表中的搜索Panel
   		var searchPanel  = {
				xtype: 'estsearchpanel',
				id: 'searchpanel',
				region: 'north',
				formitems:  [{fieldset:'搜索',items:[{fieldLabel:'计划编码',name:'jobplancode'},
							 						{fieldLabel:'计划描述',name:'jobplandesc'}]}]
			};
   		
   		//标准作业列表
   		var planGridPanel={
   			id:'planGridPanel',
   			xtype: 'estlayout',
   			title: '列表',
   			tbar: [{
					text: '搜索',
					xtype: 'estsearchbtn',
					spId: 'searchpanel',
					pId: 'jobtype'
				},'-'],
				items: [
					{xtype: 'estgrid', id:'grid', storeurl: 'est/asset/jobplan/JobPlan/getJobPlanPageList', colstype: colsType, region: 'center'},
					 searchPanel
				]
   		};
   	
   		// 分类树
    	var treePanel = {    
    		xtype:'esttree',
    		rootText:'标准作业类别',
    		isctx: true,
    		id:'treepanel',
    		region:'west',
    		loaderurl: 'est/asset/jobplan/JobType/getJobTypeTree',
    		parentFieldOfNewNode:'newParentNodeInfo',
	                   ctx: {url: 'est/asset/jobplan/JobType/', method: 'JobType', 
	                   ctxitems:[{id:'add',text:'添加'},{id:'modify',text:'修改'},{id:'delete',text:'刪除'}],
	                   formitems:[{id:'jobtypeid', fieldset:'标准作业分类树', colnum: 1,
							items:[{fieldLabel:'分类名称',name:'jobtypename',allowBlank: false},
								   {fieldLabel:'父级类别',xtype:'esttreecombo',	 //下拉列表
								       id:'newParentNodeInfo',
								       width:150,
								       height:200,
								       treeWidth:300,
								       displayField:'jobType.jobtypename',
								       valueField:'jobType.jobtypeid',
								       hiddenName:'jobType.jobtypeid',
								       dataUrl:'est/asset/jobplan/JobType/getJobTypeTree',	
								       rootId:'0',	
								       rootText:'标准作业分类树', 
								       allowedempty: true,
								       alwaysReload : true
								      }
									 ]}
							]}
    	};
    	
    	
    	var tabPanel = {
			id: 'tabpanel',
			region: 'center',
			xtype: 'esttab',
			items: [planGridPanel,{xtype:'estlayout',frame:true,title: '标准作业', id:'plandetail', layout:'fit', items:[planFormPanel,taskGridPanel,resurceGridPanel]}]
		};
    	
    	// 页面布局
    	var jobtype= new Ext.Viewport({
			layout: 'border',
			id:'jobtype',
			items: [ menuPanel, {
				xtype: 'estlayout',
				region: 'center',
				items: [treePanel,tabPanel]
			}],
			renderTo: Ext.getBody()
		});
		
		
		
		Ext.getCmp('grid').on({'rowdblclick': function(){
			Ext.getCmp('tabpanel').setActiveTab('plandetail');
		},
		'rowclick': function(){
			Ext.getCmp('plandetail').purgeListeners();
			var id = Ext.getCmp('grid').getSelectionModel().getSelected().id;
			jobplanid = id;  // 把选中的id赋值给全局的计划id
			Ext.getCmp('plandetail').on({'afterlayout': function(){
				if(id) {
					Ext.getCmp('planFormPanel').doLoad({jobplanid: id});
					Ext.getCmp('taskdetailGrid').doSearch({jobplanid: id});
				}
				Ext.getCmp('plandetail').purgeListeners();
			}
			});
		}
		});
		Ext.getCmp('treepanel').on({'click': function(node,e){
			jobtypeid = node.id;
			Ext.getCmp('grid').doSearch({'jobtypeid':node.id},true);
			Ext.getCmp('tabpanel').setActiveTab('planGridPanel');
		}
	});
		
		
			Ext.getCmp('searchpanel').addRefObjs(Ext.getCmp('grid'));
			Ext.getCmp('planFormPanel').addRefObjs(Ext.getCmp('grid'));
    	
     })
    	
    </script>
  </head>
  
  <body>
  </body>
</html>
