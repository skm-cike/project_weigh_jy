<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!-- 
位置：系统管理-用户管理
说明：修改用户密码
路径：/fuel_jt/est/sysinit/sysuser/SysUser/fwdUser
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
		
		var closeWindow = function(){
			window.parent.changepwdwin.hide();
		}
		
		var changePwd = function(){
			var pwdform = Ext.getCmp('modifyPwdForm').form;
			if(pwdform.findField('password').getValue()==pwdform.findField('confirm').getValue()){
	  	 		pwdform.doAction('submit',{
	  	 			url:'<%=basePath%>est/sysinit/sysuser/SysUser/modifyUserPwd',
	  	 			params:{"_r":new Date()},
	  	 			method:'post',
	  	 			waitMsg:'正在提交数据...',
	  	 			success:function(){
		  	 			 window.parent.info('提示','密码修改成功!');
		  	 			 closeWindow();
	  	 			},
	  	 			failure:function(form,action){
		  	 			 var msgObj=action.result;
		  	 			 if(msgObj){
		  	 			 	 window.parent.error('提示',msgObj['error']);
		  	 			 }else{
		  	 			 	window.parent.error('提示','请检查输入!');
		  	 			 }
	  	 			}
	  	 		})
	  	 	}else{
	  	 		 window.parent.error('提示','两次密码输入不一致，请重新录入!');
	  	 	} 
		};
		
		
		var modifyPwdPanel= {region:'center',items:[{
				xtype:'form',
				labelWidth:75,
				frame:true,
		        bodyStyle:'padding:10px,10px,10px,10px',
		        id:'modifyPwdForm',
		        closeAction:'hide',
		        items: [{xtype:'fieldset',
			             title: '密码信息',
			             autoHeight:true,
			             defaults: {width: 180},
			             defaultType: 'textfield',
			             items :[{fieldLabel: '登陆名',name: 'login',allowBlank:false,value:'${sessionScope.loginUser.login}',readOnly:true},
			            		 {fieldLabel: '旧密码',name: 'oldpassword', inputType:'password',allowBlank:false},
			            		 {fieldLabel: '新密码',name: 'password', inputType:'password',allowBlank:false},
			            		 {fieldLabel: '密码确认',name: 'confirm', inputType:'password',allowBlank:false}
			            	    ]
			           }],
		        buttons: [{text: '确认',handler:changePwd},
		        		  {text: '取消',handler:closeWindow}]
		
		
		}]};
		
		
		var viewport= new Ext.Viewport({
			layout: 'border',
			id:'viewport',
			items: [{
				xtype: 'estlayout',
				region: 'center',
				bodyStyle:'border:0',
				items: [modifyPwdPanel]
			}],
			renderTo: Ext.getBody()
		});
	
	});
	</script>
</html>