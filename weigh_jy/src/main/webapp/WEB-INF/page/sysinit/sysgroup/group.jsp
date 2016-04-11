<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!-- 
位置：系统管理-角色管理
说明：角色管理，为人员设置角色
路径：/fuel_jt/est/sysinit/sysgroup/SysGroup/fwdGroupMain
作者：jingpj
时间：2009-6-15
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
	var groupId=0;
	var selectedUserUrl;
	var grouptype = '<%=(String)request.getAttribute("grouptype")%>';
	
	/******************************第二个tab页开始 by zhanglk***************************/
	var colsUserType = {id: 'userid', 
				cols: [{dataIndex:'username', header: '用户名', width: '30', sortable: false},
						{dataIndex:'sysDept.deptname', header: '所在部门',width: '40', sortable: true},
						{dataIndex:'duty', header: '职务',width: '40', sortable: true}
						]};

	var gridUserPanel = {
		xtype: 'estlayout',
		title: '用户列表',
		id: 'griduserpanel',
		region:'center',
		layout:'border',
		width:620,
		tbar:[
			{xtype:'label',text:'部门:'},
			{name:'deptchoose',xtype:'esttreecombo',		//下拉树选择框
				  id:'deptchoose',
		       	  width:150,
		       	  height:200,
		       	  treeWidth:300,			
		       	  dataUrl:'<%=basePath%>est/sysinit/sysdept/SysDept/getDepartTree',	//树加载url
		       	  listeners : { 'afterchange':function(cmp,node,oldnode){  //树选择事件
		       	  		//Ext.getCmp('usergrid').doSearch({'deptId':node.id},true);
		       	  		//Ext.getCmp('selectedusergrid').doSearch({'deptId':node.id},true);
		       	  	}
		       	  },
		       	  rootId:'0',	//根结点id
		       	  rootText:'部门选择'  //根结点text
			},
			'-',
			{xtype:'label',text:'姓名:'},
			{xtype:'textfield',id:'userName',name:'userName'},
			'-',
			{text:'查询',
				handler:function(){
					Ext.getCmp('usergrid').doSearch({
							'deptId':Ext.getCmp('deptchoose').value,
							'userName':Ext.getCmp('userName').getValue(),
							'includeSubDept':'true' //包含子部门的人员
						},true);
				}
			},
			'-',
			{
				text: '保存',
				handler:function(){
					
					if(groupId==-1 || groupId==0){
						alert('请选择用户组！');
						return;
					}
					var usergrid = Ext.getCmp('usergrid')
					var userJson = usergrid.getSelectionModel().getSelections();
					var strUserid='';
					var i;
					for(i=0;i<userJson.length;i++){
						var obj = userJson[i];
						if(strUserid==''){
							strUserid=obj.id;
						}
						else{
							strUserid += ","+obj.id;
						}
					}
					var savUrl='<%=basePath%>est/sysinit/sysgroup/SysGroupuser/savGroupuserList';
					Ext.Ajax.request({url: savUrl,
						params: {groupid: groupId,userid:strUserid},
							waitMsg: '正在保存...',
							success: function(){
								showMsg('保存成功！');
								reloadSelectedUser();
							},
							failure: function(){
								showMsg('保存失败！');
							}
						});
				}
			}
		],
		items: [
			{xtype: 'estcheckgrid', id:'usergrid', storeurl: '<%=basePath%>est/sysinit/sysuser/SysUser/getUserList', colstype: colsUserType, region: 'center'}
		]
	};
	
	/*******************已选择用户列表开始**************************/
	var colsSelectedUserType = {id: 'groupuserid', 
				cols: [{dataIndex:'sysUser.username', header: '用户名', width: '30', sortable: false},
						{dataIndex:'sysUser.sysDept.deptname', header: '所在部门',width: '40', sortable: true},
						{dataIndex:'sysUser.duty', header: '职务',width: '40', sortable: true}
						]};
	var gridSelectedUserPanel = {
		xtype: 'estlayout',
		title: '已选择用户列表',
		id: 'gridselecteduserpanel',
		region:'west',
		width:500,
		tbar:[
			{
				text: '删除',
				handler:function(){
					if(groupId==-1){
						alert('请选择用户组！');
						return;
					}
					
					var selectedusergrid = Ext.getCmp('selectedusergrid');
					var groupuserJson = selectedusergrid.getSelectionModel().getSelections();
					var strGroupuserid='';
					var i;
					for(i=0;i<groupuserJson.length;i++){
						var obj = groupuserJson[i];
						if(strGroupuserid==''){
							strGroupuserid=obj.id;
						}
						else{
							strGroupuserid += ","+obj.id;
						}
					}
					var delUrl='<%=basePath%>est/sysinit/sysgroup/SysGroupuser/delGroupuserList';
					Ext.Ajax.request({url: delUrl,
						params: {groupuserid: strGroupuserid},
							waitMsg: '正在删除...',
							success: function(){
								reloadSelectedUser();
								showMsg('删除成功！');
							},
							failure: function(){
								showMsg('删除失败！');
							}
						});
				}
			}
		],
		items: [
			{xtype: 'estcheckgrid', id:'selectedusergrid', storeurl: '<%=basePath%>est/sysinit/sysgroup/SysGroupuser/getGroupuserList?groupid=-1', colstype: colsSelectedUserType, region: 'center'}
		]
	};
	
	/*******************已选择用户列表结束**************************/
	
	//第二个tab组
	var tabGroupPanel = {
		id: 'tabgrouppanel',
		title:'权限分配',
		region: 'center',
		xtype: 'panel',
		layout:'border',
		tbar:[
			{xtype:'label',id:'labelSelectedGroup',text:'当前选择的角色: 无'}
		],
		items: [gridUserPanel,gridSelectedUserPanel]
	};
	/******************************第二个tab页结束***************************/
	//搜索panel
	var searchPanel  = {
		xtype: 'estsearchpanel',
		id: 'searchpanel',
		region: 'north',
		formitems:  [{fieldset:'搜索',items:[{fieldLabel:'角色名称',name:'groupname'}]}]
	};
	
	//grid列设置
	var colsType = {id: 'groupid', 
			cols: [
					{dataIndex:'groupid',header:'ss',wdith:0, name:'groupid',type:'int',hidden:true},
					{dataIndex:'groupname', header: '角色名称', width: 200, sortable: true, name:'groupname',type:'string',editor: new Ext.form.TextField({allowBlank: false})},
					{dataIndex:'grouptype', header: '角色类型',width: 100, sortable: true, name:'grouptype',type:'string'
					<% if((String)request.getAttribute("grouptype")==null || "".equals((String)request.getAttribute("grouptype"))){%>
						,editor: new Ext.form.TextField({allowBlank: false})
					<%}%>
					},
					{dataIndex:'groupdesc', header: '角色描述',width: 300, sortable: true, name:'groupdesc',type:'string',editor: new Ext.form.TextField()},
					{dataIndex:'orderindex', header: '排序',width: 100, sortable: true, name:'orderindex',type:'string',editor: new Ext.form.TextField({allowBlank: false})}
			]};
	

	
	
	//store中的数据类型
	var _GROUP = Ext.data.Record.create(colsType.cols);
	
	//可编辑grid
	var gridPanel = {
		xtype: 'estlayout',
		title: '角色列表',
		id: 'gridpanel',
		tbar: [{
					text: '搜索',
					xtype: 'estsearchbtn',
					spId: 'searchpanel',
					pId: 'group'
				},'-',{
					text: '添加',
					handler:function(){
						
						var newGroup = new _GROUP({
		                    groupid: 0,
		                    groupname: '新角色',
		                    grouptype: grouptype,
		                    groupdesc: '',
		                    orderindex: 0
		                });
		                
					   var grid = Ext.getCmp('grid');
		               grid.addRow(newGroup);
					
					}
				},{
					text: '删除',
					handler:function(){
						var grid = Ext.getCmp('grid');
						grid.deleteRow();
					}
				},{
					text: '保存',
					handler:function(){
						var grid = Ext.getCmp('grid');
						grid.onSave('<%=basePath%>est/sysinit/sysgroup/SysGroup/savGroupList');
					}
				}
		],
		items: [
			{xtype: 'esteditgrid', id:'grid', groupField:'grouptype',storeurl: '<%=basePath%>est/sysinit/sysgroup/SysGroup/getGroupList?grouptype='+grouptype, colstype: colsType,hasPagerBar:true,region: 'center'}
			,searchPanel
		]
	};
	
	//tab列表
	var tabPanel = {
		id: 'tabpanel',
		region: 'center',
		xtype: 'esttab',
		items: [gridPanel,tabGroupPanel]
	};

	//主界面
	var group= new Ext.Viewport({
		layout: 'border',
		id: 'group',
		items: [ menuPanel, {
			xtype: 'estlayout',
			region: 'center',
			items: [tabPanel]
		}],
		renderTo: Ext.getBody()
	});


	/*handle event获取group列表中，选中行的groupid*/
	Ext.getCmp('grid').getSelectionModel().on({'rowselect': function(){
			var id = Ext.getCmp('grid').getSelectionModel().getSelected().id;
			groupId = id?id:0;
			
			var gridselecteduserpanel = Ext.getCmp('gridselecteduserpanel');
			gridselecteduserpanel.purgeListeners();
			gridselecteduserpanel.on({'afterlayout': function(){
				reloadSelectedUser();
				
				 //为第二个tab页设置已选择角色提示信息
				try{
					var groupname =  Ext.getCmp('grid').getSelectionModel().getSelected().get('groupname');
					Ext.getCmp('labelSelectedGroup').el.dom.innerHTML ='当前选择的角色: <font color=blue><b>' + (groupname?groupname:'无') +'</b></font>';
				}catch(e){
					Ext.getCmp('labelSelectedGroup').el.dom.innerHTML ='当前选择的角色: <font color=blue><b>无</b></font>'; 
				}
				gridselecteduserpanel.purgeListeners(); 
				
			}
		   });
		}
	});
	
	/** 刷新已选择用户列表*/
	function reloadSelectedUser(){
		selectedUserUrl  ='<%=basePath%>est/sysinit/sysgroup/SysGroupuser/getGroupuserList?groupid='+groupId;
		Ext.getCmp('selectedusergrid').store.proxy.conn.url=selectedUserUrl;
		Ext.getCmp('selectedusergrid').store.reload();
		Ext.getCmp('selectedusergrid').getSelectionModel().clearSelections();
	}
	
	/*
	Ext.getCmp('treepanel').on({'click': function(node,e){
			Ext.getCmp('usergrid').doSearch({'deptId':node.id},true);
		}
	});
	*/
	//Ext.getCmp('formpanel').addRefObjs(Ext.getCmp('grid'));
	Ext.getCmp('usergrid').addRefObjs(Ext.getCmp('gridselecteduserpanel'));
	Ext.getCmp('searchpanel').addRefObjs(Ext.getCmp('grid'));
});
</script>
</html>