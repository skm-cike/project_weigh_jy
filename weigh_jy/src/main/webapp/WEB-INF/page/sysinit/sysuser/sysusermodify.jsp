<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>用户信息修改</title>
		<%@ include file="/include.jsp"%>
		<script type="text/javascript">
		
		   Ext.onReady(function(){
		   
		   var formcols = [{id:'userid',fieldset:'详细',
			items: [
					{fieldLabel:'登录名',name:'login', readOnly: true},
					
					{fieldLabel:'用户名',name:'username',colspan:2,readOnly:true},
					
					new Ext.form.DateField({fieldLabel:'生日',name:'birthday',format:'Y-m-d',altFormats:'Y-m-d|Y-m-d H:i:s'}),
					{fieldLabel:'性別', xtype: 'estcombos', hiddenName:'sex', elms: [['男','男'],['女','女']]},
					{fieldLabel:'职务',name:'duty'},
					{fieldLabel:'电子邮件',name:'email',vtype:'email'},
					{fieldLabel:'电话',name:'mobile'},
					{fieldLabel:'办公电话',name:'officetel'},
					{fieldLabel:'用户密码',name:'password', inputType: 'password', hidden: true,hideLabel:true},
					{fieldLabel:'部门',name:'sysDept.deptid', hidden:true,hideLabel:true}
					
			]}];
		   
		   
			var formPanel = {
			xtype: 'estform',
			id: 'formpanel',
			formurl: '<%=basePath%>est/sysinit/sysuser/SysUserModify',
			method: 'User',
			colstype: formcols,
			tbar: [
			       {text:'保存', id:'sav',cls:'saveValid', xtype:'esttbbtn',handler: function(){Ext.getCmp('formpanel').doSumbit()}
					}]
			};
			
			var tabPanel = {
			id: 'tabpanel',
			region: 'center',
			xtype: 'esttab',
			items: [{xtype:'estlayout',frame:true,title: '个人信息修改',id:'detail', layout:'fit', items:[formPanel]
			
				,listeners:{'afterlayout':function(){
							if(Ext.getCmp('formpanel')){
								Ext.getCmp('formpanel').doLoad()}
							}
						}
			}]
			};
			
			
			var user= new Ext.Viewport({
			layout: 'border',
			id:'user',
			items: [ menuPanel,tabPanel],
			renderTo: Ext.getBody()
			});
			});
		
		</script>
  </head>
  
  <body>
  </body>
</html>
