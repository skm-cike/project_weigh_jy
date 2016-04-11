<%@ page language="java"  pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns:v="urn:schemas-microsoft-com:vml">
  <head>
    <base href="<%=basePath%>">
    <%@ include file="/include.jsp"%>
    <title>工作流设计器</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	
	<script type="text/javascript" src="js/workflow/wfNode.js"></script>
	<script type="text/javascript" src="js/workflow/wfLine.js"></script>
	<script type="text/javascript" src="js/workflow/wfCanvas.js"></script>
	
	<style>
		v\:* { BEHAVIOR: url(#default#VML) }
		.node {background: url(/fuel_jt/img/workflow/node.gif) no-repeat left 0px !important; }
		.start {background: url(/fuel_jt/img/workflow/start.gif) no-repeat left 0px !important;}
		.end {background: url(/fuel_jt/img/workflow/end.gif) no-repeat left 0px !important;}
		.transition {background: url(/fuel_jt/img/workflow/transition.gif) no-repeat left 0px !important;}
		.select {background: url(/fuel_jt/img/workflow/select.gif) no-repeat left 0px !important;}
		.checkvalidity {background: url(/fuel_jt/img/workflow/checkvalidity.gif) no-repeat left 0px !important;}
		.fork {background: url(/fuel_jt/img/workflow/fork.gif) no-repeat left 0px !important;}
		.join {background: url(/fuel_jt/img/workflow/join.gif) no-repeat left 0px !important;}
		.save {background: url(/fuel_jt/img/workflow/save.gif) no-repeat left 0px !important;}
		.delete {background: url(/fuel_jt/img/workflow/delete.gif) no-repeat left 0px !important;}
		.home {background: url(/fuel_jt/img/workflow/home.gif) no-repeat left 0px !important;}
		.countersign {background: url(/fuel_jt/img/workflow/countersign.gif) no-repeat left 0px !important;}
		.open {background: url(/fuel_jt/img/workflow/open.gif) no-repeat left 0px !important;}
		
	</style>

	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <script type="text/javascript">
  
  </script>
  
  <body   style='padding:0px;background-image: url(/fuel_jt/img/workflow/canvasbg.gif);height:100%'>
		
  </body>
  <script>
  	
  	var processid ;
  	var moduleid;
  	var jbpmnodeid;
  	
  	var deployWindow = new Ext.Window({
		id:'_deployWindow',
		title:'请设置流程',
		width:350,
		height:250,
		closeAction:'hide',
		frame:true,
		plain:true,
		items:[{
			xtype:'form',
			id:'_deployWindowForm',
			labelWidth:80,
			baseCls : "x-plain",
			bodyStyle : "padding:20px 5px 5px 20px;",
			items:[
				{fieldLabel:'流程名称',xtype:'textfield',name:'processname',allowBlank:false,width:150},
				{fieldLabel:'流程描述',xtype:'textfield',name:'description',width:150},
				{fieldLabel:'版本',xtype:'textfield',name:'processversion',readOnly:true,width:150},
				{
			       	fieldLabel:'模块',xtype:'esttreecombo',	//下拉树选择框
			       	id:'newParentNodeInfo',
			       	height:200,
			       	name:'moduleId',
			       	treeWidth:300,
			       	width:150,
			       	displayField:'sysModule.modulename',
			       	valueField:'sysModule.moduleid',
			       	hiddenName:'moduleId',
			       	dataUrl:'<%=basePath%>est/sysinit/sysmodule/SysModule/getModuleTree',	//树加载url
			       	rootId:'0',	//根结点id
			       	rootText:'模块树', //根结点text
			        allowedempty: true,
			        alwaysReload : true,
			        allowBlank:false
			     },
			        
				{fieldLabel:'是否有效',xtype: 'estcombos', hiddenName:'isvalid', elms: [['T','是'],['F','否']],width:150,value:'T'},
				{name:'dfxml',xtype:'hidden'},
				{name:'dfjson',xtype:'hidden'},
				{name:'order',xtype:'hidden'},
				{name:'processId',xtype:'hidden'},
				{name:'jbpmpid',xtype:'hidden'},
				{name:'deployDate',xtype:'hidden'},
				{name:'deployer',xtype:'hidden'},
				{name:'modifyDate',xtype:'hidden'},
				{name:'modifier',xtype:'hidden'}
			]
		}],
		buttons:[{
			text:'部署新流程',
			handler:function(){
				var deployWindowForm = Ext.getCmp('_deployWindowForm');
				if(deployWindowForm.form.isValid()){
					if(!CANVAS.checkValid()) {
						error('提示','流程配置错误！');
						return;
					}
					
					var processname = deployWindowForm.form.findField('processname').getValue();
					
					var processXml = CANVAS.makeXML(processname);
					deployWindowForm.form.findField('dfxml').setValue(processXml);
					
					var processJSON = CANVAS.makeJSON(processname);
					deployWindowForm.form.findField('dfjson').setValue(processJSON);
					
					deployWindowForm.form.submit({url: '<%=basePath%>est/workflow/processdeploy/WfProcessDeploy/deployProess', method:'POST', waitMsg: '保存中...',
						success: function(form, rep){
							console.log(rep.result.data);
							deployWindowForm.form.setValues(rep.result.data);
							processid = rep.result.data['processId'];
							info('提示','流程部署成功。');
							deployWindow.hide();
							storeSignField.baseParams = {processId:processid};
						   	storeSignField.reload();
						},
						failure: function(){
							error('错误','发起流程失败。');
						}
					});
				}
				
				
			}
		},{
			text:'取消',
			handler:function(){
				deployWindow.hide();
				//Ext.getCmp('_deployWindowForm').form.reset();
			}
		}]
	
	});
	
	/***********************选择用户********************************************/
	//任务form列定义
	var storeSignField =   new Ext.data.Store({
         proxy: new Ext.data.HttpProxy({
                url : '<%=basePath%>est/workflow/process/WfProcess/getBussinessObjectSignFields' , 
				method: 'post',
				remoteSort:true,
				encode:'utf-8'
            }),
        reader: new Ext.data.JsonReader({
                root: 'rows',
                totalProperty: 'total',
                id:'signcolumn'
                },	
                [
                	{ name : 'signcolumn', mapping : 'signcolumn', type : 'string' },
                   	{ name : 'fieldDescription',  mapping : 'fieldDescription', type : 'string' }
              	]
           )

    }); 
	//storeSignField.load();
	
	var taskFormCols = [{id:'taskId',fieldset:'任务信息',
			items: [{fieldLabel:'任务名称',name:'name',allowBlank:false,colspan:3},
					{fieldLabel:'限定天数',name:'limitdays',xtype:'hidden',allowBlank:false},
					{fieldLabel:'是否同意',name:'isapproved',xtype: 'hidden', hiddenName:'isapproved',elms: [['T','是'],['F','否']],colspan:2},
					{
						xtype: 'combo',
						name:'signcolumn',
	   					id:'cboSigncolumn',
			   			fieldLabel: '签字列', 
				       	store: storeSignField,    
				       	displayField:'fieldDescription',    
					    valueField:'signcolumn',    
					    typeAhead: false,    
					    mode: 'local',    
					    triggerAction: 'all',    
					    emptyText:'请选择...',  
					    //tpl:'<tpl for="."><div class="x-combo-list-item" style="height:25px" ext:qtitle="值" ext:qtip="{libName}">{libName:ellipsis(15)}</div></tpl>',
					    selectOnFocus:true,   
					    hiddenName:'signcolumn',
					    //transform:'signcolumn',
					    allowBlank:true,
					    //forceSelection: true,
					    resizable:false
					
					},
					
					
					//{fieldLabel:'签字列',xtype: 'estcombos',id:'signcolumn',hiddenName:'signcolumn', valueField:'fieldName', displayField:'fieldDescription', storeurl:'est/workflow/process/WfProcess/getBussinessObjectSignFields'},
					{fieldLabel:'排序',name:'order',colspan:2,xtype:'numberfield'},
					{fieldLabel:'描述',name:'description',xtype:'textarea',width:385,colspan:3},
					//{fieldLabel:'任务ID',name:'taskId',xtype:'numberfield',value:'3000',xtype:'hidden'},
					{fieldLabel:'流程ID',name:'wfDfprocess.processId',xtype:'hidden'},
					{fieldLabel:'jbpmtid',name:'jbpmtid',xtype:'hidden'}
				   ]
		}];
	
	var taskFormPanel = {
			xtype: 'estform',
			id: 'taskFormPanel',
			formurl: '<%=basePath%>est/workflow/processdefination/WfProcessDefination',
			method: 'WfDftask',
			title: '任务',
			layout:'fit',
			frame:true,
			colstype: taskFormCols,
			tbar: [
			       {text:'保存', id:'sav', xtype:'esttbbtn', handler: function(){Ext.getCmp('taskFormPanel').doSumbit();}}
				  ]
		};
	
	
	//参与者grid列设置
	var nodeColsType = {id: 'taskuserId', 
			cols: [
					{dataIndex:'taskuserId',header:'ss',width:0, name:'groupid',type:'int',hidden:true},
					{dataIndex:'wfDftask.taskId',header:'ss',width:0, name:'wfDftask.taskId',type:'int',hidden:true},
					{dataIndex:'group.groupid', header: '角色id', width: 200, sortable: true, name:'groupid',type:'int',hidden:true},
					{dataIndex:'user.userid', header: '用户id',width: 100, sortable: true, name:'user.userid',type:'int',hidden:true,fieldto:'userid',isReturn:true},
					{dataIndex:'group.groupname', header: '角色名',width: 100, sortable: true, name:'groupname',type:'string',hidden:true},
					{dataIndex:'user.username', header: '用户名',width: 150, sortable: true, name:'username',type:'string',fieldto:'username',isReturn:true},
					{dataIndex:'remark', header: '备注',width: 150, sortable: true, name:'remark',type:'string',editor: new Ext.form.TextField()},
					{dataIndex:'ordernum', header: '序号', width: 50, sortable: true, name:'ordernum',type:'int',editor: new Ext.form.NumberField()}
			]};
	
	
	
	
		//弹出窗口中的树设置（物资树）
		var deptTreePanel={
	       treeid:'deptTreePanel',//弹出窗口中树的名称
	       treeLoderUrl:'<%=basePath%>est/sysinit/sysdept/SysDept/getDepartTree',//树的URL
	       treeSCParam:'deptId',//树节点ID对应的gird的查询条件字段
	       treeWidth:'150',//树的宽度
	       rootTxt:'部门'
	     }
	     
	     //弹出选择窗口的grid数据项列表（物资编码选择grid）
	var userCols={id:'userid',
	                            //搜索条件标题
	                            lable:'用户名:',
	                            //搜索条件,必须定义
	                            scname:'username',
	                            //是否单选
	                            singleSelect:false,
	                            storeurl:'<%=basePath%>est/sysinit/sysuser/SysUser/getUserList',
	                            cols:[
	                            		{dataIndex:'userid', header: '用户id', width: '30', sortable: false,isReturn:true,fieldto:'user.userid'},
	                            		{dataIndex:'username', header: '用户名', width: '30', sortable: false,isReturn:true,fieldto:'user.username'},
										{dataIndex:'sysDept.deptname', header: '所在部门',width: '40', sortable: true},
										{dataIndex:'duty', header: '职务',width: '40', sortable: true}
	                                 ]
	                            }
	                            
  	var _nodeUser = Ext.data.Record.create(nodeColsType.cols);

		//回填多条记录的回调函数
	 
	 var feedBackFunc=function(data){
	       var grid=Ext.getCmp('usernodegrid');
	       var store=grid.getStore();
	       var count=store.getCount();
	       var isRepeat=false;
           for (var k = 0; k < data.length; k++) {
           		var userid = data[k]['userid'];
           		var username = data[k]['username'];
                var newnodeUser=new _nodeUser({
                   'taskuserId':'',
	               'wfDftask.taskId':jbpmnodeid,
	               'group.groupid':'',
	               'group.groupname':'',
	               'user.userid':userid,
	               'user.username':username,
	               'remark':'',
	               'ordernum':''
	            });
	            if(count>0){//检查记录是否被重复添加
	              for(var j=0;j<count;j++){
                     var setecedRec=store.getAt(j);
                     var selectedId=setecedRec.data['user.userid'];
                     var msg='以下记录选择重复:<br/>'; 
                     if(userid==selectedId){
                      isRepeat=true;
                       msg+=' 用户:<font color=red>'+setecedRec.data['user.username']+'</font><br/>';
                       msg+='请取消勾选，然后点击【确定】';
	                   Ext.MessageBox.show({
						   minWidth : 300,
						   title : '提示',
						   msg : '<div align=left>'+msg+'</div>',
						   buttons : Ext.Msg.OK
					  });
					  return;
                     // break;
                     }
                  }
	            }
	            if(count<=0||!isRepeat){
	              grid.addRow(newnodeUser);
	            }
              }
              Ext.getCmp('nodeuserpop').hide();
	       }
	       
  	//流程参与者grid
	var nodeuserGridPanel = {
			xtype: 'estlayout',
			title:'流程参与者',
			id: 'nodeusergridpanel',
			region:'center',
			tbar: [
				   {text:'添加',xtype:'clicktreegridchooser',
			               title:'选择流程参与者',//弹出窗口名称
			               winid:'nodeuserpop',//窗口ID
			               winWidth:500,//弹出窗口的宽度
			               winHeight:500,//弹出窗口的高度
			               treePanel:deptTreePanel,
			               colstype:userCols,//弹出窗口中grid中的数据项
			               callback:feedBackFunc
	                },
				   {text:'删除',handler:function(){Ext.getCmp('usernodegrid').deleteRow();}},
				   {text:'保存',handler:function(){Ext.getCmp('usernodegrid').onSave('<%=basePath%>est/workflow/processdefination/WfDftaskuser/savWfDftaskuserList')}}
				  ],
			
			items: [
				{xtype: 'esteditgrid', id:'usernodegrid', storeurl: '<%=basePath%>est/workflow/processdefination/WfDftaskuser/getWfDftaskuserList', colstype: nodeColsType, region: 'center'}
			]
		};
		
	//======================	
		
	//流程中字段配置grid列
	var nodeFieldColsType = {id: 'taskfieldId', 
			cols: [
					{dataIndex:'taskfieldId',header:'ss',width:0, name:'taskfieldId',type:'int',hidden:true},
					{dataIndex:'wfDftask.taskId',header:'ss',width:0, name:'wfDftask.taskId',type:'int',hidden:true},
					{dataIndex:'sysModulefield.fieldid', header: '字段id', width: 200, sortable: true, name:'sysModulefield.fieldid',type:'int',hidden:true},
					{dataIndex:'fieldCode', header: '字段',width: 100, sortable: true, name:'fieldCode',fieldto:'userid',isReturn:true},
					{dataIndex:'fieldName', header: '字段名称',width: 100, sortable: true, name:'fieldName',type:'string'},
					{dataIndex:'isreadonly', header: '是否只读',width: 150, sortable: true, name:'isreadonly',type:'string',editor: new Est.ux.ComboBox({ hiddenName:'isreadonly', elms: [['Y','是'],['N','否']],width:150,value:'Y'}),
						renderer:function(v){
							return v==='Y'?'是':'否'
						}
					},
					{dataIndex:'isvisible', header: '是否可见',width: 150, sortable: true, name:'isvisible',type:'string',editor: new Est.ux.ComboBox({ hiddenName:'isvisible', elms: [['Y','是'],['N','否']],width:150,value:'Y'}),
						renderer:function(v){
							return v==='Y'?'是':'否'
						}
					},
					{dataIndex:'ordernum', header: '序号', width: 50, sortable: true, name:'ordernum',type:'int',editor: new Ext.form.NumberField()}
			]};
			
	//弹出选择field窗口的grid数据项列表
	var chooseFieldCols={id:'fieldid',
	                            //搜索条件标题
	                            //lable:'字段名:',
	                            //搜索条件,必须定义
	                            //scname:'username',
	                            //是否单选
	                            singleSelect:false,
	                            storeurl:'<%=basePath%>est/sysinit/sysmodule/SysModule/getFieldListByModuleId',
	                            cols:[
	                            		{dataIndex:'fieldid', header: 'id', width: '30',hidden:true, sortable: false,isReturn:true,fieldto:'sysModulefield.fieldid'},
	                            		{dataIndex:'fieldcode', header: '字段', width: '30', sortable: false,isReturn:true,fieldto:'fieldCode'},
										{dataIndex:'fieldname', header: '字段名',width: '40', sortable: true,isReturn:true,fieldto:'fieldName'},
										{dataIndex:'ordernum', header: '序号',width: '40', sortable: true}
	                                 ]
	                            }	
	                            
	var _nodeField = Ext.data.Record.create(nodeFieldColsType.cols);

		//回填多条记录的回调函数
	 
	 var fieldFeedBackFunc=function(data){
	       var grid=Ext.getCmp('fieldnodegrid');
	       var store=grid.getStore();
	       var count=store.getCount();
	       var isRepeat=false;
           for (var k = 0; k < data.length; k++) {
           		var fieldid = data[k]['fieldid'];
           		var fieldcode = data[k]['fieldcode'];
           		var fieldname = data[k]['fieldname'];
                var newnodeField=new _nodeField({
                   'taskfieldId':'',
	               'wfDftask.taskId':jbpmnodeid,
	               'sysModulefield.fieldid':fieldid,
	               'fieldCode':fieldcode,
	               'fieldName':fieldname,
	               'isreadonly':'Y',
	               'isvisible':'Y',
	               'ordernum':''
	            });
	            if(count>0){//检查记录是否被重复添加
	              for(var j=0;j<count;j++){
                     var setecedRec=store.getAt(j);
                     var selectedId=setecedRec.data['sysModulefield.fieldid'];
                     var msg='以下记录选择重复:<br/>'; 
                     if(fieldid==selectedId){
                      isRepeat=true;
                       msg+='字段:<font color=red>'+fieldname+'</font><br/>';
                       msg+='请取消勾选，然后点击【确定】';
	                   Ext.MessageBox.show({
						   minWidth : 300,
						   title : '提示',
						   msg : '<div align=left>'+msg+'</div>',
						   buttons : Ext.Msg.OK
					  });
					  return;
                     // break;
                     }
                  }
	            }
	            if(count<=0||!isRepeat){
	              grid.addRow(newnodeField);
	            }
              }
              Ext.getCmp('nodefieldpop').hide();
	       }
  	//流程字段grid
	var nodeFieldGridPanel = {
			xtype: 'estlayout',
			title:'流程字段配置',
			id: 'nodefieldgridpanel',
			region:'center',
			tbar: [
				   {text:'添加',xtype:'clicktreegridchooser',
			               title:'选择流程字段',//弹出窗口名称
			               winid:'nodefieldpop',//窗口ID
			               winWidth:500,//弹出窗口的宽度
			               winHeight:500,//弹出窗口的高度
			               //treePanel:deptTreePanel,
			               colstype:chooseFieldCols,//弹出窗口中grid中的数据项
			               callback:fieldFeedBackFunc,
			               onClick:function(e){
			               		var win = this.win;
			               		win.gridPanel.doSearch({moduleid:moduleid});
			               		win.showWin();
			               }
	                },
				   {text:'删除',handler:function(){Ext.getCmp('fieldnodegrid').deleteRow();}},
				   {text:'保存',handler:function(){Ext.getCmp('fieldnodegrid').onSave('<%=basePath%>est/workflow/processdefination/WfDftaskfield/savWfDftaskfieldList')}}
				  ],
			
			items: [
				{xtype: 'esteditgrid', id:'fieldnodegrid', storeurl: '<%=basePath%>est/workflow/processdefination/WfDftaskfield/getWfDftaskfieldList', colstype: nodeFieldColsType, region: 'center'}
			]
		};	
			
	//=======================		
		
		//任务tab
	var tasknodeTabPanel={id: 'tasknodeTabPanel',
			region: 'center',
			xtype: 'esttab',
			items:[taskFormPanel,nodeuserGridPanel,nodeFieldGridPanel]
		}
		
	
	
	var nodeWindow = new Ext.Window({
		id:'_nodeWindow',
		title:'请设置流程',
		width:550,
		height:450,
		closeAction:'hide',
		frame:true,
		plain:true,
		layout:'border',
		items:[tasknodeTabPanel]
	
	});
	
	/********************************************分配用户结束***********************/
	
	var getJbpmNodeId = function(processId,nodeName){
		var nodeid;
		Ext.Ajax.request({
			url:'<%=basePath%>est/workflow/process/WfProcess/getTaskNodeId',
			params:{processId:processId,nodeName:nodeName},
			success: function(response,options){
				var json = Ext.decode(response.responseText);
				nodeid = json.nodeId;
				//alert(nodeid);
			},
			failure: function(){
				error("错误","网络连接失败");
			}
		});
		return nodeid;
	}
	
	/*******************************任务条件开始************************************/
	
	
	
	//任务条件grid列定义
	var taskconditionCols={id:'taskconditionId',
			cols:[{dataIndex:'taskconditionId',header:'ss',width:0, name:'taskconditionId',type:'int',hidden:true},
				 {dataIndex:'wfDftask.taskId',header:'ss',width:0, name:'wfDftask.taskId',type:'int',hidden:true},
				 {dataIndex:'conditionexpression',header:'条件表达式',width:200, name:'conditionexpression',type:'string',editor:new Ext.form.TextField()},
				 {dataIndex:'transitionname',header:'转签名称',width:200, name:'transitionname',type:'string',editor:new Ext.form.TextField()}
				]
		}
	
	var _newTaskcondition=Ext.data.Record.create(taskconditionCols.cols);
	//执行添加一行操作
	var doAddNewTaskcondition=function(){
			var newTaskcondition=new _newTaskcondition({
					taskconditionId:0,
					'wfDftask.taskId':jbpmnodeid,
					conditionexpression:'',
					transitionname:''
				});
			Ext.getCmp('taskconditionGrid').addRow(newTaskcondition);	
		}
	//任务条件gridpanl
	var taskconditionGridPanel = {
			xtype: 'estlayout',
			id: 'taskconditionGridPanel',
			title: '任务条件',
			region:'center',
			tbar: [
				   {text:'添加',handler:doAddNewTaskcondition},
				   {text:'删除',handler:function(){Ext.getCmp('taskconditionGrid').deleteRow();}},
				   {text:'保存',handler:function(){Ext.getCmp('taskconditionGrid').onSave('<%=basePath%>est/workflow/processdefination/WfDftaskcondition/savWfDftaskconditionList')}}
				  ],
			
			items: [
				{xtype: 'esteditgrid', id:'taskconditionGrid', storeurl: '<%=basePath%>est/workflow/processdefination/WfDftaskcondition/getWfDftaskconditionListByPage', colstype: taskconditionCols, region: 'center'}
			]
		}; 
	
	var taskconditionWindow = new Ext.Window({
			id:'_taskconditionWindow',
			title:'请设置任务条件',
			width:550,
			height:350,
			closeAction:'hide',
			frame:true,
			plain:true,
			layout:'border',
			items:[taskconditionGridPanel]
	    })
	/*******************************任务条件结束************************************/
	
	/*****************************模块流程开始**************************************/
	
	//模块树
	var treePanel = {xtype: 'esttree',isctx: false, id:'treepanel', region:'west',rootTxt:'所有模块',width:150,
					 loaderurl: '<%=basePath%>est/sysinit/sysmodule/SysModule/getModuleTree'
					};
	//流程grid列定义
	var processCols={id:'processId',
			cols:[{dataIndex:'processId',header:'processId',width:0, name:'processId',type:'int',hidden:true},
				 {dataIndex:'moduleId',header:'moduleId',width:0, name:'moduleId',type:'int',hidden:true},
				 {dataIndex:'processname',header:'流程名称',width:200, name:'processname',type:'string'},
				 {dataIndex:'description',header:'描述',width:200, name:'description',type:'string'},
				 {dataIndex:'processversion',header:'流程版本',width:200, name:'description',type:'string'},
				 {dataIndex:'deployDate',header:'部署日期',width:200, name:'deployDate',type:'string'},
				 {dataIndex:'isvalid',header:'是否有效',width:100, name:'isvalid',type:'string',
				  renderer:function(v){
				  	if(v==='T'){
				  		return "<font color=green>有效</font>";
				  	}else{
				  		return "<font color=red>无效</font>";
				  	}
				  }
				 }
				]
		}
		
	//流程grid	
	var processGridPanel={
			xtype: 'estlayout',
			title: '流程列表',
			id: 'processGridPanel',
			region:'center',
			tbar:['流程名称：',{xtype:'textfield',id:'processname'},'&nbsp;',
				  {text:'搜索',handler:function(){
				  		var processname=Ext.getCmp('processname').getValue();
				  			if(moduleid){Ext.getCmp('processGrid').doSearch({'moduleId':moduleid},true);}
				  			if(moduleid && processname){Ext.getCmp('processGrid').doSearch({'moduleId':moduleid,'processname':processname},true);}
				  	}
				  },
				  {text:'重置',handler:function(){Ext.getCmp('processname').reset();}},'-',
				  {text:'有效',handler:function(){
						var selectedRec=Ext.getCmp('processGrid').getSelectionModel().getSelected();
						if(!selectedRec || !selectedRec.id){
							error('提示','请选择一条流程记录');
							return;
						}
						Ext.Ajax.request({
						   url: '<%=basePath%>est/workflow/processdefination/WfProcessDefination/setProcessValid',
						   success: function(response){
						   				var resJasonObj=Ext.decode(response.responseText);
						   				if(!resJasonObj.success){error('提示','流程定义设置失败!');}else{selectedRec.set('isvalid','T');}
						   			},
						   failure: function(){error('提示','数据提交失败,请重试!')},
						   params: {processId:selectedRec.id,isvalid:'T'}
						});
					}
				  },
				  {text:'无效',handler:function(){
				  		var selectedRec=Ext.getCmp('processGrid').getSelectionModel().getSelected();
				  		if(!selectedRec || !selectedRec.id){
							error('提示','请选择一条流程记录');
							return;
						}
				  		Ext.Ajax.request({
						   url: '<%=basePath%>est/workflow/processdefination/WfProcessDefination/setProcessValid',
						   success: function(response){
						   				var resJasonObj=Ext.decode(response.responseText);
						   				if(!resJasonObj.success){error('提示','流程定义设置失败!');}else{selectedRec.set('isvalid','F');}
						   			},
						   failure: function(){error('提示','数据提交失败,请重试!')},
						   params: {processId:selectedRec.id,isvalid:'F'}
						});
						
				  	}
				  },
				  {
				  	text:'编辑流程',
				  	handler:function(){
				  		CANVAS.clear();
				  		var selectedRec=Ext.getCmp('processGrid').getSelectionModel().getSelected();
				  		if(!selectedRec || !selectedRec.id){
							error('提示','请选择一条流程记录');
							return;
						}
						Ext.Ajax.request({
						   url: '<%=basePath%>est/workflow/processdefination/WfProcessDefination/getProcessJson',
						   success: function(response){
						   				var resJasonObj=Ext.decode(response.responseText);
						   				if(!resJasonObj.success){
						   					error('提示',resJasonObj.info);
						   				}else{
						   					var processJson = Ext.decode(resJasonObj.processJson);
						   					CANVAS.init(processJson);
						   					var selectedRec=Ext.getCmp('processGrid').getSelectionModel().getSelected();
						   					processid = selectedRec.data['processId'];
						   					moduleid = selectedRec.data['moduleId'];
						   					storeSignField.baseParams = {processId:processid};
						   					storeSignField.reload();
						   				}
						   			},
						   failure: function(){error('提示','数据提交失败,请重试!')},
						   params: {processId:selectedRec.id}
						});		
				  		
				  	}
				  }				
				 ],
			items: [
				{xtype: 'estgrid', id:'processGrid', storeurl: '<%=basePath%>est/workflow/processdefination/WfProcessDefination/getProcessDefinationListByModuleId', colstype: processCols, region: 'center'}
			]
		}
	//流程定义窗口
	var processWin = new Ext.Window({
						title : "流程定义窗口",
						width : 700,
						height : 400,
						id : "processWin",
						layout : 'border',
						plain : true,
						closeAction : 'hide',
						items:[treePanel,processGridPanel]
					});
	 Ext.getCmp('treepanel').on({'click': function(node,e){
	 					moduleid=node.id
						Ext.getCmp('processGrid').doSearch({'moduleId':moduleid},true);
					}
				});	
	 //打开流程定义窗口								
     var openProcessWin=function(){
			processWin.show();
	     }
	
	/*****************************模块流程结束**************************************/
	
  	var testProcess = function(){
	  	Ext.Ajax.request({
			url:'<%=basePath%>est/workflow/process/WfProcess/newProess',
			success: function(response,options){
				var json = Ext.decode(response.responseText);
			},
			failure: function(){
				error("错误","网络连接失败");
			}
		
		
		});
  	
  	}
  	
  	 var toolbar = new Ext.Toolbar({autoWidth:true,width:800,height:25,style:'z-index:-10000',
  		defaults:{enableToggle:true,allowDepress:true,handler:function(){}},
  		items:[
  			
  		'|',
	  		{id:'btnnode',iconCls:'node',tooltip:'签字节点',tooltipType:'title',enableToggle:true ,handler:function(){btnClick('node')} ,toggleGroup:'btngroup'},
	  		{id:'btncountersign',iconCls:'countersign',tooltip:'会签节点',tooltipType:'title',enableToggle:true ,handler:function(){btnClick('countersign')},toggleGroup:'btngroup'},
	  		{id:'btnstart',iconCls:'start',tooltip:'开始节点',tooltipType:'title',enableToggle:true ,handler:function(){btnClick('start')},toggleGroup:'btngroup'},
	  		{id:'btnend',iconCls:'end',tooltip:'结束节点',tooltipType:'title',enableToggle:true ,handler:function(){btnClick('end')},toggleGroup:'btngroup'},
	  		{id:'btntransition',iconCls:'transition',tooltip:'任务流转',tooltipType:'title',enableToggle:true ,handler:function(){btnClick('transition')},toggleGroup:'btngroup'},
	  		{id:'btncondition',iconCls:'fork',tooltip:'条件',tooltipType:'title',enableToggle:true ,handler:function(){btnClick('condition')},toggleGroup:'btngroup'},
	  		{id:'btnfork',iconCls:'fork',tooltip:'分支',tooltipType:'title',enableToggle:true ,handler:function(){btnClick('fork')},toggleGroup:'btngroup'},
	  		{id:'btnjoin',iconCls:'join',tooltip:'聚合',tooltipType:'title',enableToggle:true ,handler:function(){btnClick('join')},toggleGroup:'btngroup'},'|',
	  		{id:'btnselect',iconCls:'select',tooltip:'选择',tooltipType:'title',enableToggle:true ,handler:function(){clearTmp();btnClick('select')},toggleGroup:'btngroup'},
	  		{iconCls:'delete',tooltip:'删除',tooltipType:'title',handler:deleteObject}, '|',
	  		{iconCls:'open',tooltip:'打开...',tooltipType:'title',handler : function(){clearTmp();openProcessWin();}},
	  		{iconCls:'open',tooltip:'新建...',tooltipType:'title',handler : function(){
				processid = null;
				moduleid = null;
				CANVAS.clear();
				clearTmp();
			}},
	  		{iconCls:'checkvalidity',tooltip:'检查',tooltipType:'title',handler : function(){clearTmp();CANVAS.checkValid()}},
	  		{iconCls:'save',tooltip:'部署',tooltipType:'title',handler :function(){clearTmp();CANVAS.deployProcess()}},
	  		{iconCls:'home',tooltip:'返回',tooltipType:'title',handler : function(){window.history.go(-1)}}
  		]
  	});
  	toolbar.render(Ext.getBody());
  	
  	var CANVAS = new WfCanvas(document.body);
  	CANVAS.init();
  	
  	var type = 'select'; //操作按钮的类型
  	var flag = false; //按钮是否处理的标识（只在控制按钮点击事件使用）
  	var moveflag = false;
  	
  	var selectedObject; 	//选中对象 	
  	var addTransBeginNode; //添加
  	
  	var offset_top = 25;
	var offset_left = 0;
  	
  	
  	//划线的临时变量
  	var tmpLine = null;
  	
  	//清除临时线
  	var clearTmp = function(){
  		if(tmpLine!=null){
  			document.body.removeChild(tmpLine.getDom());
  		}
  		tmpLine = null;
  		addTransBeginNode=null;
  	
  	}
  	
  	
  	document.onmousemove = function(){
  		if(tmpLine!=null && addTransBeginNode!= null){
			var x = window.event.x-offset_left;
			var y = window.event.y-offset_top;  
			var orgX = tmpLine.from.x;	
			var orgY = tmpLine.from.y;
			x += orgX>x ? 2 : -2;
			y += orgY>y ? 2 : -2;
			
  			tmpLine.getDom().to = x+','+y;
  			
  		}
  			return false;
  	}
  	
  	//按钮事件处理
  	function btnClick(param) {
  		type = param;
  		flag = true;
  		
  		clearTmp();
  		
  		if(type != 'select') {
  			CANVAS.toggleNodeLock(true);
  		} else {
  			CANVAS.toggleNodeLock(false);
  		}
  	}
  	
  	
  	function clearflag(p){
  		type = "";
  		Ext.getCmp('btn'+p).toggle();
  	}
  	
  	
  	var mapping = {
		'node':'签字任务', 'countersign':'会签任务',
		'start':'开始','end':'结束','condition':'条件节点',
		'fork':'分支','join':'汇聚'
  	};
  	
  	function createNewElement(){
  		var id = CANVAS.getNextNodeId(type);
		if(id){
			var name = id.replace(type,mapping[type]);
			CANVAS.addNode(new  WfNode({type:type,id:id,name:name,x:event.x+document.body.scrollLeft,y:event.y+document.body.scrollTop}));
			clearflag(type);
		}
  	}
  	
  	
  	document.onclick = function(){
  		/********何波开始******/
  		//Ext.getCmp('_taskconditionWindow').show();
  		//openProcessWin();
  		/********何波结束******/
  		if(!flag){
	  		if(type && type !== 'transition') {
	  			
	  			if(type !== 'select' || type !== 'transition'){
	  				createNewElement();
	  			}
	  		}
	  		/*
	  		if(type == 'node') {
	  			createNewElement(type);
	  		}else if(type == 'countersign') {
	  			var id = CANVAS.getNextNodeId(type);
	  			CANVAS.addNode(new  WfNode({type:'countersign',id:id,name:'新会签',x:event.x+document.body.scrollLeft,y:event.y+document.body.scrollTop}));
	  			clearflag(type);
	  		} else if(type == 'start') {
	  			var id = CANVAS.getNextNodeId(type);
	  			CANVAS.addNode(new  WfNode({type:'start',id:id,name:'开始',x:event.x+document.body.scrollLeft,y:event.y+document.body.scrollTop}));
	  			clearflag(type);
	  		} else if(type == 'end') {
	  			var id = CANVAS.getNextNodeId(type);
	  			CANVAS.addNode(new  WfNode({type:'end',id:id,name:'结束',x:event.x+document.body.scrollLeft,y:event.y+document.body.scrollTop}));
	  			clearflag(type);
	  		} else if(type == 'condition') {
	  			var id = CANVAS.getNextNodeId(type);
	  			CANVAS.addNode(new  WfNode({type:'condition',id:id,name:'新条件',x:event.x+document.body.scrollLeft,y:event.y+document.body.scrollTop}));
	  			clearflag(type);
	  		} else if(type == 'fork') {
	  			var id = CANVAS.getNextNodeId(type);
	  			CANVAS.addNode(new  WfNode({type:'fork',id:id,name:'分支',x:event.x+document.body.scrollLeft,y:event.y+document.body.scrollTop}));
	  			clearflag(type);
	  		} else if(type == 'join') {
	  			var id = CANVAS.getNextNodeId(type);
	  			CANVAS.addNode(new  WfNode({type:'join',id:id,name:'汇聚',x:event.x+document.body.scrollLeft,y:event.y+document.body.scrollTop}));
	  			clearflag(type);
	  		} else if(type == 'transition') {
	  		} 
	  		*/
	  	} else {
	  		flag = false;
	  	}
  	}
  	
  	/*
  	document.onmousemove = function(){
  		return false;
  	}
  	*/
  	
  	document.onkeydown = function(){
  		switch(event.keyCode){
  			case 46 : //del
  				deleteObject();
  				break;
  		}
  	}
  	
  	function deleteObject() {
  		clearTmp();
  		if(selectedObject){
			if(confirm('你确定要删除吗？')){
				
					if(selectedObject.type != 'line' ) {
						CANVAS.removeNode(selectedObject);
					} else if(selectedObject.type == 'line' ) {
						CANVAS.removeLine(selectedObject);
					}
					selectedObject = undefined;
			}
		} else {
			alert('请先选择一个对象');
		}
  	
  	}
  	
  	function selectObject(obj){
  		
  		if(selectedObject){
			selectedObject.object.StrokeColor = '#0';
			selectedObject.object.strokeweight = 1;
  		}
  		
		selectedObject = obj;
		if(obj){
			selectedObject.object.StrokeColor = '#00f';
			selectedObject.object.strokeweight = 2;
		}
		
  	
  	}
  	
  	var propWin = new Ext.Window({
  		width:300,
  		height:500,
  		style:'z-index:999999999',
  		title:'属性',
  		frame:true,
  		style:'padding:0 5px 5px 0;',
  		collapsible :true,
  		closable:false,
	  	items:[new Ext.grid.PropertyGrid({
	        id : 'propertygrid',
	        enableHdMenu: false,
	        autoHeight: true,
	        viewConfig : {
	            //forceFit:true,
	            //scrollOffset:2 // the grid will never have scrollbars
	        },
	        source : {
	        	'id':'1',
	        	'name':''
	        
	        },
	        listeners:{
	        	'beforeedit':function(e){
	        		if(e.record.data.name !== 'name'){
	        			return false;
	        		} else if(processid !== undefined && processid != null){
	        			error('提示','部署后不能修改名称！');
	        			e.cancel = true;
	        		}
	        	},
	        	'afteredit':function(e){
		        	if(e.record.data.name === 'name') {
		        		selectedObject.name = e.record.data.value;
		        		selectedObject.setText(selectedObject.name );
		        	} else {
		        		return false;
		        	}
	        	}
	        }
	    })]
  	});
  	
  	propWin.show();
  	
  	propWin.alignTo(document.body,'tr',[-310,30]);
  	var propertygrid = Ext.getCmp('propertygrid');
  	with (propertygrid.getColumnModel()){
  		setColumnWidth(0,120);
  		setColumnWidth(1,150);
  	}
  	
  	
  	
  //	alert(Ext.getCmp('propertygrid').getSource().id);
  	//Ext.getCmp('propertygrid').setSource({'sa':'bb'})
  	
  	
  </script>
 
</html>
