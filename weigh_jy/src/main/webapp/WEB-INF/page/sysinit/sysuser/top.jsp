<%@ page language="java"  pageEncoding="UTF-8"%>
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
	<style>
		* {
			margin: 0;
			padding: 0;
		}
		#head {
			background: #C0D5F0 url(img/head.jpg) no-repeat 0 0;
			height: 48px;
			min-width: 800px;
		}
		
	</style>
	
	<link rel="stylesheet" type="text/css" href="js/ext/resources/css/ext-all.css">
	<script type="text/javascript" src="js/ext/ext-base.js"></script>
	<script type="text/javascript" src="js/ext/ext-all-debug.js"></script>
	<script type="text/javascript" src="js/ext/source/locale/ext-lang-zh_CN.js"></script>

	<script type="text/javascript">
		Ext.onReady(function(){
			
			var mainFrame = top.frames['main'];
			/********************修改密码弹出窗口开始************************/
			function popModifyPwdWin(){
				if(!mainFrame.changepwdwin){
					mainFrame.changepwdwin = new mainFrame.Ext.Window({
						title:'修改密码',
						width:400,
						height:250,
						closeAction:'hide',
						resizable:false,
						html: "<iframe src='<%=basePath%>est/sysinit/sysuser/SysUser/toChangPwd' style='width:100%; height:100%;'></iframe>"
					});
				}
				mainFrame.changepwdwin.show();
			}
			/********************修改密码弹出窗口结束************************/
			
			
			var a = new Ext.Toolbar({
				width:30,
				height:20,
				style:'background:transparent none repeat scroll 0 0;border:0 none;',
				items:[
					{
						text:'主页',
						handler:function(){
							mainFrame.location.href = '<%= basePath%>est/sysinit/sysuser/SysUser/fwdBlank';
						}
					},
					{
						text:'待办事项',
						handler:function(){
							mainFrame.location.href = '<%= basePath%>est/workflow/process/WfProcess/fwdMyTask';
						}
					},
					{
				    	text:'初始化',
				    	handler: function(){ 
				    		var win = window.open('<%= basePath%>ieinit/ieinit.exe');
				    		setTimeout(function(){
				    			win.close();
				    		}, 2000);
				    	}
				    },/*{
				    	text:'初始化Office控件',
				    	handler: function(){ 
				    		var win = window.open('<%= basePath%>est/contract/edit/ContractEdit/downOcx');
				    		setTimeout(function(){
				    			//win.close();
				    		}, 2000);
				    	}
				    },*/
					{text: '密码修改',handler: popModifyPwdWin},
					{
						text:'个人信息修改',
						handler:function(){
							mainFrame.location.href = '<%= basePath%>est/sysinit/sysuser/SysUserModify/fwdSysuserModify?rw=MD';
						}
					},
					{text: '帮助',handler: function() {alert("coming soon !");}},
					{text:'退出',handler:function(){top.location="<%=basePath%>est/sysinit/sysuser/SysUser/cancelUser";}}
				],
				renderTo:Ext.get('commonToolbar')
			});
		})
	</script>
</head>
<body>
	<div id="head">
		<div style="float:right;margin-top:10px">
			<div id="commonToolbar"></div>
		</div>
	</div>
</body>
</html>