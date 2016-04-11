<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
    
    <title>EAM系统</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css"
			href="<%=basePath%>js/ext/resources/css/ext-all.css">
	<script type="text/javascript" src="<%=basePath%>js/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/ext/ext-all-debug.js"></script>
	
	<script type="text/javascript" src="<%=basePath%>js/ext/source/locale/ext-lang-zh_CN.js"></script>
	
	<script type="text/javascript" src="<%=basePath%>js/est/util-debug.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>js/est/Est.ux.ToolbarButton-debug.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/est/Est.ux.ComboBox-debug.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/est/Est.ux.BasicForm-debug.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/est/Est.ux.Form-debug.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/est/Est.ux.Tree-debug.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/est/Est.ux.Grid-debug.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/est/Est.ux.Layout-debug.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/est/Est.ux.Tab-debug.js"></script>
	<script type="text/javascript"
		src="js/est/Est.ux.SearchButton-debug.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/est/Est.ux.Panel-debug.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>js/est/Est.ux.SearchPanel-debug.js"></script>
	<script type="text/javascript">
		Ext.BLANK_IMAGE_URL = 'js/ext/resources/images/default/s.gif';
	</script>
	
	<style>
		#head {
			background: #C0D5F0 url(img/head.jpg) no-repeat 0 0;
			height: 48px;
			min-width: 800px;
		}
	</style>

  </head>
  
  <body>
   <script type="text/javascript">
   Ext.onReady(function(){
		Ext.QuickTips.init();
		
		var topPanel = {
				id: 'toppanel',
				region: 'north',
				html: '<div id="head"></div>',
				height: 48
		};
		var bottomPanel = new Ext.StatusBar({
				id:'statusbar',
				region: 'south',
				items: [{
		            text: 'Button'
		        }, '-', 'Text']
		});
		var modulPanel = {
			id:'modul',
			region: 'fit',
			html: 'hello world'
		};
		var mainPanel = new Ext.Panel({
				id: 'mainpanel',
				border: false,
				//title: '>modul>submodul',
				layout: 'fit',
		        region: 'center',
				items: [modulPanel]
		});
		
		var onmenuclk = function(btn) {
			Ext.get('modul').load({
				url: "<%=basePath%>est/sysinit/sysuser/SysUser/fwdUser",
				timeout: 30,
				nocache: true,
			    scripts: true
			});
		};
		var submenu = new Ext.menu.Menu({
			id:'sub menu',
			items: [{
				text: 'submenu1',
				menu: {
					items: [{
						text: 'subsubmenu11',
						handler: onmenuclk
					}]
				}
			},{
				text: '系统管理',
				menu: {
					items: [{
						text: '人员管理',
						handler: onmenuclk
					},{
						text: '模块管理',
						handler: function() {
							Ext.get('modul').load({
								url: "<%=basePath%>est/sysinit/sysmodule/SysModule/fwdModule",
								timeout: 30,
								nocache: true,
							    scripts: true
							});
						}
					}]
				}
			}]
		});
		
		var menuPanel= new Ext.Toolbar({
				id: 'menupanel',
				region: 'north',
				items:['->',{
					text: 'index'
				},'-',{
					text: '主菜单',
					tooltip: {text:'Hava sub menu!!!'},
					menu: submenu
				}]
		});
		var border = new Ext.Viewport({
		    layout:'border',
		    items: [
		            menuPanel,{
					layout: 'border',
					region: 'center',
					items: [topPanel, mainPanel]
				},
		            bottomPanel
			],
		    renderTo: Ext.getBody()
		});
	})
   </script>
  </body>
</html>
