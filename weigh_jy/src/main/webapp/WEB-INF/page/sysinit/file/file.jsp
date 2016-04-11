<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<base href="<%=basePath %>">
	<%@ include file="/include.jsp"%>
</head>
<body>
	<!-- 
	<form name="uploadform" action="est/sysinit/file/File/fileUpload" enctype="multipart/form-data" method="post" >
		文件名称<input type="text" name="filename"/><br/>
		文件地址<input type="file" name="file"/> <br/>
		<input type="submit" value="upload"> 
	
	
	</form>
	 -->

</body>
<script type="text/javascript">
Ext.onReady(function(){
	
	Ext.QuickTips.init();
	
	/*
	var formcols = [{id:'moduleid',fieldset:'详细',
			items: [
					{fieldLabel:'文件名称',name:'filename', allowBlank: true},
			        {fieldLabel:'文件地址', name:'file',inputType: 'file',colspan: 2, width: 300},
			        {fieldLabel:'test',name:'test',xtype:'esttreecombo',
			        	  width:300,
			        	  height:200,
			        	  dataUrl:'est/sysinit/sysdept/SysDept/getDepartTree',
			        	  rootId:'0',
			        	  rootText:'角色选择'
			        },
			        {
			        	fieldLabel:'sss',xtype:'timefield'
			        }
			]
		}];

	var formPanel = {
		xtype: 'estform',
		id: 'formpanel',
		url: 'est/sysinit/file/File/fileUpload',
		title: '详细',
		fileUpload: true,
		colstype: formcols,
		tbar: [{text:'重置/增加',id:'add', xtype: 'esttbbtn', handler:function(){Ext.getCmp('formpanel').doReset();}},
		       {text:'保存', id:'sav', xtype:'esttbbtn', handler: function(){
		       			Ext.getCmp('formpanel').getForm().getEl().dom.action='est/sysinit/file/File/fileUpload';
		       			Ext.getCmp('formpanel').getForm().getEl().dom.enctype ="multipart/form-data";
		       			Ext.getCmp('formpanel').getForm().getEl().dom.method ="post";
		       			Ext.getCmp('formpanel').getForm().submit();
		       			//alert(Ext.getCmp('formpanel').getForm().action);
		       		}
		       }
			  ]
	};
	
	*/
	
	
	var formcols = [{id:'moduleid',fieldset:'详细',
			items: [
					{fieldLabel:'文件名称',name:'filename', allowBlank: true,columnWidth:.3},
			        {fieldLabel:'文件地址', name:'file',inputType: 'file',columnWidth:.6},
			        {fieldLabel:'test',name:'test',xtype:'esttreecombo',
			        	  columnWidth:.3,
			        	  dataUrl:'<%=basePath%>est/sysinit/sysdept/SysDept/getDepartTree',
			        	  rootId:'0',
			        	  rootText:'角色选择',columnWidth:.3
			        },
			        {
			        	fieldLabel:'sss',xtype:'timefield',columnWidth:.3
			        }
			]
		}];

	var formPanel = {
		xtype: 'estcolform',
		id: 'formpanel',
		url: '<%=basePath%>est/sysinit/file/File/fileUpload',
		//plain:true,
		title: '详细',
		layout:'column',
		frame:true,
		fileUpload: true,
		/*
		items:[
			{xtype:'fieldset',title:'fieldset',border: 'none',style: 'margin-left: 8px;',columnWidth:.3,
				items:[{
					xtype:'textfield',fieldLabel:'111',name:'111'
				}]
			
			},
			{xtype:'fieldset',title:'fieldset2',layout:'column',style: 'margin-left: 8px;',columnWidth:.9,
				items:[
					{layout:'form',border:'none',columnWidth:.9,items:[
						{xtype:'textfield',fieldLabel :'111',name:'111',anchor:'80%'}
					]},
					{layout:'form',border:'none',columnWidth:.4,items:[
						{xtype:'textfield',fieldLabel :'222',name:'222'}
					]},
					{layout:'form',border:'none',columnWidth:.4,items:[
						{xtype:'textfield',fieldLabel :'333',name:'333'}
					]},
					{layout:'form',border:'none',columnWidth:.7,items:[
						{xtype:'textfield',fieldLabel :'444',name:'444',anchor:'90%'}
					]},
					{layout:'form',border:'none',columnWidth:.9,items:[
						{xtype:'textarea',fieldLabel :'555',name:'555',anchor:'90%'}
					]}
				]
			}
		
		
		],*/
		colstype: formcols,
		tbar: [{text:'重置/增加',id:'add', xtype: 'esttbbtn', handler:function(){Ext.getCmp('formpanel').doReset();}},
		       {text:'保存', id:'sav', xtype:'esttbbtn', handler: function(){
		       			Ext.getCmp('formpanel').getForm().getEl().dom.action='<%=basePath%>est/sysinit/file/File/fileUpload';
		       			Ext.getCmp('formpanel').getForm().getEl().dom.enctype ="multipart/form-data";
		       			Ext.getCmp('formpanel').getForm().getEl().dom.method ="post";
		       			Ext.getCmp('formpanel').getForm().submit();
		       			//alert(Ext.getCmp('formpanel').getForm().action);
		       		}
		       },{
		       	text:'报表',handler:function(){
		       		window.open('<%=basePath%>est/sysinit/systemplate/ShowReport/fwdShowReport?templatecode=DXCZP&var_aa=ssss',
					   					 "报表",
					   					  "height="+window.screen.height+", width="+window.screen.width+", top=0,left=0,toolbar=yes, menubar=no, scrollbars=no, resizable=yes, location=no, status=yes"
					   					);
		       	}
		       }
			  ]
	};
	
	var tabPanel = {
		id: 'tabpanel',
		region: 'center',
		xtype: 'esttab',
		items: [{xtype:'estlayout',title: '详细信息', id:'detail', layout:'fit', items:[formPanel]}]
	};
	
	
	var module= new Ext.Viewport({
		layout: 'border',
		id: 'file',
		items: [ menuPanel, {
			xtype: 'estlayout',
			region: 'center',
			items: [tabPanel]
		}],
		renderTo: Ext.getBody()
	});

	
	/*handle event*/
	
});
</script>
</html>