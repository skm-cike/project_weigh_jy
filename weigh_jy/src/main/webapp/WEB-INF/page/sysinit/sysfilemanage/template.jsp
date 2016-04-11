<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<%@ include file="/include.jsp"%>

	</head>
	<body>
	</body>
	<script type="text/javascript">
	/*
		Ext.onReady在页面body加载结束之后执行
	*/
	Ext.onReady(function(){
		
	var templateId=0;//模板id，全局变量
	var funListUrl='';//查询模板函数的action地址
	var paramListUrl='';//查询模板参数的action地址
/*==================================参数管理部分开始==========================*/
	var colsType = {id: 'paramid', 
		cols: [
				{dataIndex:'paramid',header:'ss',wdith:0, name:'paramid',type:'int',hidden:true},
				{dataIndex:'sysTemplate.templateid',header:'templateid',wdith:0, name:'sysTemplate.templateid',type:'int',hidden:true},
				{dataIndex:'paramname', header: '参数名称', width: 200, sortable: true, name:'paramname',type:'string',editor: new Ext.form.TextField({allowBlank: false})},
				{dataIndex:'paramtype', header: '参数类型',width: 200, sortable: true, name:'paramtype',type:'string',editor: new Est.ux.ComboBox({ width: 80, hiddenName:'paramtype', elms: [['String','String'],['Double','Double']]})},
				{dataIndex:'paramdesc', header: '参数描述',width: 600, sortable: true, name:'paramdesc',type:'string',editor: new Ext.form.TextField()},
				{dataIndex:'paramorder', header: '执行顺序',width: 100, sortable: true, name:'paramorder',type:'string',editor: new Ext.form.TextField({allowBlank: false})}
		]};
	
	var _TemplateParam = Ext.data.Record.create(colsType.cols);
	
	var gridParamPanel = {
		xtype: 'estlayout',
		title: '模板参数列表',
		id: 'gridparampanel',
		tbar: [{xtype:'label',id:'labelParam',text:'当前选择的模板: 无'},'-',{
					text: '添加',
					handler:function(){
						
						var newTemplateParam = new _TemplateParam({
		                    paramid: 0,
		                    sysTemplate:{templateid:templateId},
		                    paramname: '',
		                    paramtype: '',
		                    paramdesc: '',
		                    paramorder: 0
		                });
		                
					   var gridparam = Ext.getCmp('gridparam');
		               gridparam.addRow(newTemplateParam);
					
					}
				},{
					text: '删除',
					handler:function(){
						var gridfun = Ext.getCmp('gridparam');
						gridparam.deleteRow();
					}
				},{
					text: '保存',
					handler:function(){
						if(templateId==0){
							alert('没有选择模板！');
							return;
						}
						var gridparam = Ext.getCmp('gridparam');
						gridparam.onSave('<%=basePath%>est/sysinit/sysfilemanage/SysTemplateparam/savTemplateparamList');
					}
				}
		],
		items: [
			{xtype: 'esteditgrid', id:'gridparam', storeurl: '<%=basePath%>est/sysinit/sysfilemanage/SysTemplateparam/getTemplateparamList?templateid=0', colstype: colsType, region: 'center'}
		]
	};
/*==================================参数管理部分结束==========================*/
/*＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝函数管理部分开始＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝*/
	var colsTypefun = {id: 'functionid', 
		cols: [
				{dataIndex:'functionid',header:'ss',wdith:0, name:'functionid',type:'int',hidden:true},
				{dataIndex:'sysTemplate.templateid',header:'templateid',width:0, name:'sysTemplate.templateid',type:'int',hidden:true},
				{dataIndex:'functioncode', header: '函数名称', width: 150, sortable: true, name:'functioncode',type:'string',editor: new Ext.form.TextField({allowBlank: false})},
				{dataIndex:'functiondesc', header: '函数描述',width: 150, sortable: true, name:'functiondesc',type:'string',editor: new Ext.form.TextField({allowBlank: false})},
				{dataIndex:'functionparam', header: '函数参数',width: 150, sortable: true, name:'functionparam',type:'string',editor: new Ext.form.TextField()},
				{dataIndex:'functioncontent', header: '函数体',width: 550, sortable: true, name:'functioncontent',type:'string',editor: new Ext.form.TextArea({allowBlank: true})},
				{dataIndex:'executeorder', header: '执行顺序',width: 80, sortable: true, name:'executeorder',type:'string',editor: new Ext.form.TextField({allowBlank: false})}
		]};
	
	var _TemplateFun = Ext.data.Record.create(colsTypefun.cols);
	
	var gridFunPanel = {
		xtype: 'estlayout',
		title: '模板函数列表',
		id: 'gridfunpanel',
		tbar: [{xtype:'label',id:'labelFun',text:'当前选择的模板: 无'},'-',{
					text: '添加',
					handler:function(){
						
						var newTemplateFun = new _TemplateFun({
		                    functionid: 0,
		                    sysTemplate:{templateid:templateId},
		                    functioncode: '',
		                    functiondesc: '',
		                    functionparam:'',
		                    functioncontent: '',
		                    executeorder: 0
		                });
		                
					   var gridfun = Ext.getCmp('gridfun');
		               gridfun.addRow(newTemplateFun);
					
					}
				},{
					text: '删除',
					handler:function(){
						var gridfun = Ext.getCmp('gridfun');
						gridfun.deleteRow();
					}
				},{
					text: '保存',
					handler:function(){
						if(templateId==0){
							alert('没有选择模板！');
							return;
						}
						var gridfun = Ext.getCmp('gridfun');
						gridfun.onSave('<%=basePath%>est/sysinit/sysfilemanage/SysTemplateFunction/savTemplateFunctionList');
					}
				}
		],
		items: [
			{xtype: 'esteditgrid', id:'gridfun', storeurl: '<%=basePath%>est/sysinit/sysfilemanage/SysTemplateFunction/getTemplatefunctionList?templateid=0', colstype: colsTypefun, region: 'center'}
		]
	};
	
	/*＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝函数管理部分结束＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝*/
		
		var formcols = [{id:'templateid',fieldset:'详细',
			items: [
					{fieldLabel:'模板名称',name:'templatename', allowBlank: false},
					{fieldLabel:'模板编码',name:'templatecode', allowBlank: false},
					{fieldLabel:'模板类别',xtype: 'estcombos',hiddenName:'sysDir.dirid', valueField:'dirid', displayField:'dirname', storeurl:'est/sysinit/sysfilemanage/SysDir/getAllSysDirs'},
					{fieldLabel:'有效标识', xtype: 'estcombos', width: '80', hiddenName:'validflag', elms: [['是','是'],['否','否']]},
					{fieldLabel:'文件URL',name:'file',inputType: 'file',width:'200',height:'20'},
					{fieldLabel:'更新标识', xtype: 'estcombos', width: '80', hiddenName:'updateflag', elms: [['N','否'],['Y','是']]},
					{fieldLabel:'',name:'fileno',hidden:'true',hideLabel:'true'}
			]}];
		var formPanel = {
			xtype: 'estform',
			id: 'formpanel',
			formurl: '<%=basePath%>est/sysinit/sysfilemanage/SysTemplate',
			method: 'Template',
			title: '详细',
			colstype: formcols,
			fileUpload: true,
			tbar: [{text:'重置/增加',id:'add', xtype: 'esttbbtn', handler:function(){Ext.getCmp('formpanel').doReset();}},
			       {text:'保存', id:'sav', xtype:'esttbbtn', handler: function(){Ext.getCmp('formpanel').doSumbit()}},
				   {text:'刪除', xtype:'esttbbtn', id:'del', isconfirm: true, handler:function(){Ext.getCmp('formpanel').doDelete()}},
				   {text:'编辑报表', xtype:'esttbbtn', id:'edit', isconfirm: true, handler:function(){
						if(templateId==0){
				   			alert("请选择一个模板或者保存当前记录后，再编辑报表！");
				   		} else {
				   			var fileno =  Ext.getCmp('grid').getSelectionModel().getSelected().get('fileno');
					   		window.open('<%=basePath%>/est/sysinit/sysfilemanage/SysTemplate/fwdTemplateInit?templateid='+templateId+"&fileno="+fileno,
					   					 "报表",
					   					  "height="+window.screen.height+", width="+window.screen.width+", top=0,left=0,toolbar=no, menubar=no, scrollbars=no, resizable=yes, location=no, status=yes"
					   					);
					   	}
				   }}
				  ]
		};
		var searchPanel  = {
			xtype: 'estsearchpanel',
			id: 'searchpanel',
			region: 'north',
			formitems:  [{fieldset:'搜索',
						items:[
						{fieldLabel:'模板名称',name:'templatename'},
						{fieldLabel:'类别编码',name:'dircode'}
						]}]
		};
		var colsType = {id: 'templateid', 
				cols: [{dataIndex:'templatename', header: '模板名称', width: .2, sortable: false},
						{dataIndex:'sysDir.dirname', header: '所属类别',width: .2, sortable: true},
						{dataIndex:'sysDir.dircode', header: '类别编码',width: .2, sortable: true},
						{dataIndex:'validflag', header: '是否有效',width: .2},
						{dataIndex:'updateflag', header: '是否更新',width: .2,
						 renderer:function(v){
						 	return v=='Y' ? '是' : '否';
						 }
						},
						{dataIndex:'fileno', header: '文件序列号',width: .2}
						]};
		var gridPanel = {
			xtype: 'estlayout',
			title: '模板列表',
			id: 'gridpanel',
			tbar: [{
				text: '搜索',
				xtype: 'estsearchbtn',
				spId: 'searchpanel',
				pId: 'modul'
			},'-'],
			items: [
				{xtype: 'estgrid', id:'grid', storeurl: '<%=basePath%>est/sysinit/sysfilemanage/SysTemplate/getTemplateList?', colstype: colsType, region: 'center'}, searchPanel
			]
		};
		/**
		模板管理tab
		*/
		var tabPanel = {
			id: 'tabpanel',
			region: 'center',
			xtype: 'esttab',
			items: [gridPanel,{xtype:'estlayout',frame:true,title: '详细信息', id:'detail', layout:'fit', items:[formPanel]},gridFunPanel,gridParamPanel]
		};
		var treePanel = {xtype: 'esttree',isctx: true, rootTxt:'目录管理', id:'treepanel', region:'west',loaderurl: '<%=basePath%>est/sysinit/sysfilemanage/SysDir/getSysDirTree?dirType=RPT',
				parentFieldOfNewNode:'newParentNodeInfo',	//新建时，父节点信息存放字段id
				ctx: {url: '<%=basePath%>est/sysinit/sysfilemanage/SysDir/', method: 'SysDir', 
						ctxitems:[{id:'add',text:'添加'},{id:'modify',text:'修改'},{id:'delete',text:'刪除'}],
						formitems:
							 [
							 	{id:'dirid', fieldset:'Login', colnum: 1,
							 		items:[{fieldLabel:'类别名称',name:'dirname',allowBlank: false},
								        //{fieldLabel:'父级类别',xtype:'estcombos',hiddenName:'sysDir.dirid', valueField:'dirid', displayField:'dirname', storeurl:'est/sysinit/sysfilemanage/SysDir/getAllSysDirs',  allowedempty: true},
								        {fieldLabel:'类别编码',name:'dircode',allowBlank: false},
								        {xtype:'hidden',name:'sysDir.dirid',id:'newParentNodeInfo'},
								        {xtype:'hidden',name:'dirType',value:'RPT'}
								   ]}
							 ]
					}
				};
		/*
			Viewport组件，包括：
				renderTo是组件渲染到的DOM对象，Ext.getBody()== document.body
		*/
		var module= new Ext.Viewport({
			layout: 'border',
			items: [ menuPanel, {
				xtype: 'estlayout',
				region: 'center',
				items: [tabPanel,treePanel]
			}],
			renderTo: Ext.getBody()
		});
		
		
		
		
		Ext.getCmp('grid').on({'rowdblclick': function(){
				Ext.getCmp('tabpanel').setActiveTab('detail');
			},
			'rowclick': function(){
				Ext.getCmp('detail').purgeListeners();
				var id = Ext.getCmp('grid').getSelectionModel().getSelected().id;
				templateId = id;
				
				funListUrl='<%=basePath%>est/sysinit/sysfilemanage/SysTemplateFunction/getTemplatefunctionList?templateid='+templateId;
				paramListUrl='<%=basePath%>est/sysinit/sysfilemanage/SysTemplateparam/getTemplateparamList?templateid='+templateId;
				//alert(paramListUrl);
				Ext.getCmp('detail').on({'afterlayout': function(){
					if(id) {
						console.log(Ext.getCmp('grid').getSelectionModel().getSelected());
						Ext.getCmp('formpanel').doLoad({templateid: id});
					}
					Ext.getCmp('detail').purgeListeners();
				}
				});
				//根据模板id查询函数grid数据
				Ext.getCmp('gridfunpanel').purgeListeners();
				Ext.getCmp('gridfunpanel').on({'afterlayout': function(){
						if(templateId==0){
							alert('没有选择模板！');
							return;
						}
						else{
							//alert(listUrl);
							Ext.getCmp('gridfun').store. proxy.conn.url=funListUrl;
							Ext.getCmp('gridfun').store.reload();
							Ext.getCmp('gridfunpanel').purgeListeners();
						}
						//为函数grid设置已选择模板提示信息
						try{
							var templatename =  Ext.getCmp('grid').getSelectionModel().getSelected().get('templatename');
							Ext.getCmp('labelFun').el.dom.innerHTML ='当前选择的模板: <font color=blue><b>' + (templatename?templatename:'无') +'</b></font>';
						}catch(e){
							Ext.getCmp('labelFun').el.dom.innerHTML ='当前选择的模板: <font color=blue><b>error</b></font>'; 
						}
					}
			   });
			   //根据模板id查询参数grid数据
				Ext.getCmp('gridparampanel').purgeListeners();
				Ext.getCmp('gridparampanel').on({'afterlayout': function(){
						if(templateId==0){
							alert('没有选择模板！');
							return;
						}
						else{
							//alert(paramListUrl);
							Ext.getCmp('gridparam').store. proxy.conn.url=paramListUrl;
							Ext.getCmp('gridparam').store.reload();
							Ext.getCmp('gridparampanel').purgeListeners();
						}
						//为函数grid设置已选择模板提示信息
						try{
							var templatename =  Ext.getCmp('grid').getSelectionModel().getSelected().get('templatename');
							Ext.getCmp('labelParam').el.dom.innerHTML ='当前选择的模板: <font color=blue><b>' + (templatename?templatename:'无') +'</b></font>';
						}catch(e){
							Ext.getCmp('labelParam').el.dom.innerHTML ='当前选择的模板: <font color=blue><b>error</b></font>'; 
						}
					}
			   });
		}
		});
		Ext.getCmp('treepanel').on({'click': function(node,e){
			Ext.getCmp('grid').doSearch({'dirid':node.id},true);
			}
		});
		/*
			addRefObjs增加受影响组件
			Ext.getCmp('formpanel').addRefObjs(Ext.getCmp('grid'));
				forme组件在doSubmit和doReset时影响gird容器，执行grid中的doSearch()方法，这个是在代码里封装的，如需在form执行方法时grid执行其它方法，不能使用该方法，直接使用事件监听的方式手写。
		*/
		Ext.getCmp('formpanel').addRefObjs(Ext.getCmp('grid'));
		Ext.getCmp('grid').addRefObjs(Ext.getCmp('formpanel'));
		Ext.getCmp('searchpanel').addRefObjs(Ext.getCmp('grid'));
	
	});
	</script>
</html>