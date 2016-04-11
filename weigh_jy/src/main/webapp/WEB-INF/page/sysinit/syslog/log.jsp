<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
	
    <title>系统日志</title>
    <%@ include file="/include.jsp"%>	
    <script type="text/javascript">
    
    
    	Ext.onReady(function(){
    	
    		
    		var logid;
    		
    		var searchFields=[	   '操作人:',{xtype:'textfield', id:'operator',width:130},
    		                  	 '登录名称:',{xtype:'textfield', id:'loginname',width:100},
	                               '操作时间:',{xtype:'estdatetime',id:'startTime',format:'Y-m-d H:i',width:100,readOnly:false},
	                               '—',{xtype:'estdatetime',id:'endTime',format:'Y-m-d H:i',width:100,readOnly:false}
	                              ];
	  		// 搜索字段映射
	  		var searchMapping={'operator':'operator','startTime':'startTime','endTime':'endTime','loginname':'loginname'};
			
			var colsType = {
				cols:[
					{dataIndex:'operator',	header:'操作人',name:'operator',width:130},
    				{dataIndex:'loginname',	header:'登录名称',name:'loginname'/*,hidden:true*/},
					{dataIndex:'operate', header:'操作', name:'operate',width:150},
					{dataIndex:'operateresult', header:'操作结果', name:'operateresult',width:150},
					{dataIndex:'operateTime',	header:'操作时间',name:'operateTime',width:150},
					
					{dataIndex:'id',	header:'id',width:130,hidden:true}
				]
			}	
			
    			var GridPanel = {
	    			id:'GridPanel',
	   				xtype: 'estlayout',
	   				title: '系统日志',
    			bbar:new Est.ux.SearchToolbar({id:'searchbar',searchFields:searchFields,searchMapping:searchMapping,gridId:'grid'}),
	   			items:[
	   				{xtype: 'esteditgrid',id:'grid', autoLoad:false,storeurl: '<%=basePath%>est/sysinit/syslog/SysLog/getModuleList', colstype: colsType, region: 'center'}
	   			]
	    			
    		}
    	
			
			
			var col_opinion = {
					id:'col_opinion',
					cols:[
						{dataIndex:'details',	header:'数据变化',name:'details',width:800}
					]
				}



				var tab_opinion = {
					id: 'tab_opinion',
					title: '查看数据变化',
					xtype: 'estlayout',
					items: {
								xtype : 'esteditgrid',
								id : 'opinion',
								colstype : col_opinion,
								region : 'center',
								storeurl : '<%=basePath%>est/sysinit/syslog/SysLog/getModuleListById'
					}
				}
			
    		var tabPanel = {
				id: 'tabPanel',
				region: 'center',
				xtype: 'esttab',
				items: [GridPanel,tab_opinion]
			};
    	
    		var firstcodeprint= new Ext.Viewport({
				layout: 'border',
				id:'firstcodeprint',
				items: [ menuPanel, {
					xtype: 'estlayout',
					region: 'center',
					items: [tabPanel]
				}],
				renderTo: Ext.getBody()
			}); 
			Ext.getCmp('searchbar').doSearch();
			
			Ext.getCmp('grid').on({'rowdblclick':function() {
				Ext.getCmp('tabPanel').setActiveTab('tab_opinion');
			},'rowclick':function() {
				logid = Ext.getCmp('grid').getSelectionModel().getSelected().data['id'];
				Ext.getCmp('opinion').doSearch({logid:logid});
			}});
			
    	});
    	
    	
    </script>	

  </head>
  <body>
  </body>
</html>
