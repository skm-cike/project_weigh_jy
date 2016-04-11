<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!-- 
位置：系统管理-权限管理-人员权限
说明：为人员分配模块权限
路径：/fuel_jt/est/sysinit/sysauthority/SysUserModule/fwdUserModule
作者：smile-bug
时间：2009
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<base href="<%=basePath %>">
	<%@ include file="/include.jsp"%>
	<style type="text/css">
		#ulpanel li ul {
			display: none;
		}
		.msg,#ulpanel li {
			font-size: 12px;
			padding: 6px 14px;
		}
		#ulpanel ul li a {
			padding-left: 18px;
			background: url('<%=basePath%>js/ext/resources/images/default/tree/folder.gif') no-repeat 0 0;
		}
	</style>
</head>
<body>
</body>
<script type="text/javascript">
function toggleLi(subid) {
	Ext.get(subid).getStyle('display')==='none'?Ext.get(subid).setStyle({display:'block'}):Ext.get(subid).setStyle({display:'none'});	
}

function clkbox(subid, ischked) {
	
	if(typeof subid === 'string') {
		//not leaf node
		var ischk = ischked?'checked':'';
		Ext.each(Ext.get(subid).query('input'),function(domelm){
				domelm.checked = ischk;
			}, this);
	}
	
	var pnode = {};
	var curnode = {};
	if(typeof subid === 'string') {
		console.log('subid is str');
		curnode = Ext.get(subid).parent().first().dom;
		pnode = Ext.get(subid).parent().parent().dom;
	}else  {
		console.log('subid is obj');
		curnode = subid;
		pnode = subid.parentNode.parentNode;
	}

	/*关联新增刪除勾选*/
	if(curnode.nextSibling.nodeName != 'A'){
		curnode.nextSibling.nextSibling.checked = curnode.checked?'checked':'';
		curnode.nextSibling.nextSibling.nextSibling.nextSibling.checked = curnode.checked?'checked':'';
	}
	
	/*通过状态对同级节点进行检查*/
	var isupdate = true;
	for(var i=0; i<pnode.childNodes.length; i++) {
		/*点击的时候，如果勾选时有一个相同则不更新，取消时有一个不同则不更新*/
		console.log('curnode.checked ' +curnode.checked);
		console.log(curnode);
		if(!curnode.checked) {
			if(pnode.childNodes[i].firstChild.checked != curnode.checked && pnode.childNodes[i].firstChild != curnode){
				isupdate = false;
				break;
			}
		} else {
			if(pnode.childNodes[i].firstChild.checked == curnode.checked && pnode.childNodes[i].firstChild != curnode){
				isupdate = false;
				break;
			}
		}
	}
	console.log('is need check up level' + isupdate);
	/*如果有一个不同，向上查找*/
	if(isupdate) {
		var subupdate = true;
		while(pnode.nodeName==='UL' && pnode.parentNode.firstChild.nodeName === 'INPUT' && subupdate) {
			var fc = pnode.parentNode.firstChild;
			fc.checked = curnode.checked?'checked':'';
			console.log(fc.name+'<>'+fc.checked);
			curnode = pnode.parentNode.firstChild;
			pnode = pnode.parentNode.parentNode;
			
			
			for(var i=0; i<pnode.childNodes.length; i++) {
				/*点击的时候，如果勾选时有一个相同则不更新，取消时有一个不同则不更新*/
				if(!curnode.checked) {
					/*取消时执行*/
					if(pnode.childNodes[i].firstChild.checked != curnode.checked && pnode.childNodes[i].firstChild != curnode){
						console.log('cancel sub');
						subupdate = false;
						break;
					}
				} else {
					/*勾选时执行*/
					if(pnode.childNodes[i].firstChild.checked == curnode.checked && pnode.childNodes[i].firstChild != curnode){
						console.log('check sub');
						subupdate = false;
						break;
					}
				}
			}
		}
	}
	
}
function getData() {
	var data = [];
	Ext.each(Ext.get('ulpanel').query("input[name=chk]"), function(domelm){
		if(domelm.checked) {
			var tmp = {rwflag:''};
			tmp.moduleid = domelm.id;
			if(domelm.nextSibling.nodeName != 'A'){
				if(domelm.nextSibling.nextSibling.checked) tmp.rwflag = 'M';
				if(domelm.nextSibling.nextSibling.nextSibling.nextSibling.checked) tmp.rwflag += 'D';
			}
			data.push(Ext.util.JSON.encode(tmp));		
		}
	},this);
	return data.join(',');
}

Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var colsType = {id: 'userid', 
			cols: [{dataIndex:'username', header: '姓名', width: .5, sortable: true},
			       {dataIndex:'login', header: '登录名',width: .25, sortable: true},
			       {dataIndex:'sysDept.deptname', header: '部门',width: .25, sortable: true}
					
			]}
	var gridPanel = {
			title: '用户列表',
			region: 'west',
			xtype: 'estlayout',
			split: true,
			minSize: 300,
			width: 500,
			tbar: [
				
				'部门',{name:'query_deptchoose',xtype:'esttreecombo',		//下拉树选择框
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
			       	  rootId:'${requestScope.rootId}',	//根结点id
			       	  rootText:'${requestScope.rootText}'  //根结点text
				},'-',
				'用户名',{xtype:'textfield',id:'query_username',width:130},
				{
					text:'搜索',
					handler:function(){
						var username = Ext.get('query_username').dom.value;
						var deptId = Ext.getCmp('deptchoose').value;
						Ext.getCmp('grid').doSearch({'userName':username,'deptId':deptId});
					}
				}
			],
			items:[new Est.ux.Grid({
						id:'grid', 
						storeurl: '<%=basePath%>est/sysinit/sysuser/SysUser/getFirstLevelDepUserList', 
						colstype: colsType, 
						region: 'center'
			})]
	};
	var ulPanel = new Est.ux.Panel({
		id: 'ulpanel',
		region: 'center',
		title: '权限',
		html: '<span class="msg">请从左边选择用户</span>',
		tbar: [{text: '保存',
				handler: function() {
					var id = Ext.getCmp('grid').getSelectionModel().getSelected().id;

					if(id){
						Ext.Ajax.request({
							url: '<%=basePath%>est/sysinit/sysauthority/SysUserModule/savUserModuleList',
							params: {
								data: '['+getData()+']',
								userId: id
							},
							waitMsg: '正在保存',
							success: function(){showMsg('保存成功')},
							failure: estfailure
						});
					}
				}
			}]
	});
	
	var mainPanel = new Est.ux.Layout({
		id: 'mainpanel',
		region: 'center',
		items: [gridPanel,ulPanel]
	});

	
	var module= new Ext.Viewport({
		id: 'module',
		layout: 'border',
		items: [ menuPanel, mainPanel],
		renderTo: Ext.getBody()
	});

	Ext.getCmp('grid').on({'rowclick':function(e){
			var id = Ext.getCmp('grid').getSelectionModel().getSelected().id;
			if(id) {
				Ext.getCmp('ulpanel').load({
					url:'<%=basePath%>est/sysinit/sysauthority/SysUserModule/getUserModuleList',
					params: {userId: id, _r: new Date().getTime()}
				});
			}
		}});
});
</script>
</html>