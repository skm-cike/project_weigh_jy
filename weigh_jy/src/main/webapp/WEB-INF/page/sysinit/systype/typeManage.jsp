<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!-- 
位置：基础配置-合同类型管理
说明：
路径：/fuel_jt/est/sysinit/sysType/SysType/typeManage
作者：smile-bug
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
	/*
		Ext.onReady在页面body加载结束之后执行
	*/
	Ext.onReady(function(){
		/*
			初使化提示，直接调用
		*/
		Ext.QuickTips.init();
		/*
			搜索面板,默认隐藏
				xtype指定组件类型
				id为组件的ID，可通过Ext.getCmp(id)获取到组件
				region为layout布局中位置的定义可为west, north, south, east, center,其中一个layout布局必须至少有一个组件为center
				formitems和Est.formpanel的colstype内容一致，为要搜索的具体项
		*/
		var searchPanel  = {
			xtype: 'estsearchpanel',
			id: 'searchpanel',
			region: 'north',
			formitems:  [{fieldset:'搜索',items:[{fieldLabel:'类型名',name:'typename'}]}]
		};
		/*
			form里面的具体内容,可包含多个fieldset，每个fieldset里包含具体的items
				items里使用table布局
				其中的元素可使用colspan和rowspan来定义跨行或跨列，且元素类型默认为textfield
					fieldLabel输入框之前显示的名称
					name为提交到后台的名称
					allowBlank为是否为空，默认为true
					vtype为验证元素类型，用于验证
					inputType为元素输入的类型,为password时，不显示

					xtype为estcombos时，为下拉框包括
						hiddenName为提交到后台的名称
						valueField为提交数据的值
						displayField为显示的数据值
						storeurl为对应的取列表的地址

					Ext.form.DateField时，为日期选择包括
						format定义时间格式
		*/
		
		var formcols = [{id:'typeid',fieldset:'类型详细',
			items: [
					{fieldLabel:'类型名',name:'typename', allowBlank: false},
					{fieldLabel:'内容代码',name:'typecode',allowBlank: false},
					{fieldLabel:'上级类型',xtype:'estcombos',hiddenName:'parenttypeid',valueField:'typeid',displayField:'typename', storeurl:'<%=basePath%>est/sysinit/systype/SysType/findAllSysTypes',  allowedempty: true},
					{fieldLabel:'内容表达式',name:'typerule',allowBlank: true},
					{fieldLabel:'备注',name:'typeremark',allowBlank: true}
			]}];
		/*
			form组件，包括
				formurl,form请求的地址，不包括具体方法
				method为请求的方法后缀，定义为Type,刪除／保存／加载方法名分別为addType/savType/getType
				colstype里面是form中的具体元素见formcols
				tbar为top bar，位于form顶端的按钮,其中的元素包括
					text按钮显示的名称，
					handler为点击按钮时触发的事件
				bbar为bottom bar，位于form底端的按钮
		*/
		var formPanel = {
			xtype: 'estform',
			id: 'formpanel',
			formurl: '<%=basePath%>est/sysinit/systype/SysType',
			method: 'Type',
			title: '详细',
			colstype: formcols,	
			tbar: [{text:'重置/增加',id:'add', xtype: 'esttbbtn', handler:function(){Ext.getCmp('formpanel').doReset();}},
			       {text:'保存', id:'sav',cls:'saveValid', xtype:'esttbbtn', handler: function(){Ext.getCmp('formpanel').doSumbit()}},
					{text:'刪除',cls:'delValid', xtype:'esttbbtn', id:'del', isconfirm: true, handler:function(){Ext.getCmp('formpanel').doDelete()}}]
		};
		/*
			colsType里为grid中的显示项
			id为grid中唯一标识，一般对应VO中的ID
			cols为具体显示的项，
				包括dataIndex为VO中对应的字段名，
				header为用于显示的名称，
				width为宽度可使用.3表示30%这样的百分比来定义，
				sortable为是否可排序
		*/
		var colsType = {id: 'typeid', 
				cols: [ {dataIndex:'typeid', header: '类型ID', width: '30', sortable: false,hidden:true},
						{dataIndex:'typename', header: '类型名', width: '30', sortable: false},
						{dataIndex:'typecode', header: '类型代码', width: '30', sortable: false},
						{dataIndex:'typerule', header: '内容表达式', width: '30', sortable: false},
						{dataIndex:'typeremark', header: '备注',width: '40', sortable: false}
				]};
		/*
			grid容器
				tbar为top bar，位于grid顶端的按钮,
					'-'为一分隔符,'->'右对齐,'<-'左对齐
				bbar被分页占用，一般不应使用自定义元素覆盖
				items中为具体的grid组件，它包括：
					storeurl为grid请求的路径
					colstype为grid中数据显示格式，见colstype
		*/
		var gridPanel = {
			xtype: 'estlayout',
			title: '类型列表',
			id: 'gridpanel',
			tbar: [{
				text: '搜索',
				xtype: 'estsearchbtn',
				spId: 'searchpanel',
				pId: 'type'
			},'-'],
			items: [
				{xtype: 'estgrid', id:'grid',storeurl: '<%=basePath%>est/sysinit/systype/SysType/getSysTypesList', colstype: colsType, region: 'center'}, searchPanel
			]
		};

		/*
			tab组件，只需把组件放入容器的items中就会自动形成Tab页。
				layout为fit为自适应式布局
		*/
		var tabPanel = {
			id: 'tabpanel',
			region: 'center',
			xtype: 'esttab',
			items: [gridPanel,{xtype:'estlayout',frame:true,title: '类型详细信息', id:'detail', layout:'fit', items:[formPanel]}]
		};
		/*
			tree组件，包括：
				rootTxt为根节点显示的文字
				loaderurl为树请求的数据的地址
				isctx为是否有上下文菜单
				ctx为具体的上下文菜单项，包括：
					url为修改，新增时form请求的地址
					ctxitems为显示的菜单项
						id为内部识别标志，系统已经封装了add,del,modify三个方法，分別对应增加刪除修改，如需使用自定义方法，使用cus，再增加fun属性
						text为显示的菜单名称
					formitems为增加修改时的form，内容同form组件。
		*/
		var treePanel = {xtype: 'esttree',isctx: true, rootTxt:'所有类型', id:'treepanel', region:'west',loaderurl: '<%=basePath%>est/sysinit/systype/SysType/findTreeSysTypes',
				ctx: {url: '<%=basePath%>est/sysinit/systype/SysType', method: 'Type', ctxitems:[{id:'add',text:'添加'},{id:'modify',text:'修改'},{id:'del',text:'刪除'}],
					formitems:
					 [{id:'typeid', fieldset:'添加类型', colnum: 1,
						 items:[{fieldLabel:'类型名称',name:'typename',allowBlank: false},
						 		{fieldLabel:'类型代码',name:'typecode',allowBlank:false},
						        {fieldLabel:'上级类型',xtype:'estcombos',hiddenName:'parenttypeid', valueField:'typeid', displayField:'typename', storeurl:'<%=basePath%>est/sysinit/systype/SysType/findAllSysTypes?typeid='+id,  allowedempty: true},
						        {fieldLabel:'内容表达式',name:'typerule',allowBlank: true},
								{fieldLabel:'备注',name:'typeremark',allowBlank: true}
						 ]}
					]}
				};
		/*
			Viewport组件，包括：
				renderTo是组件渲染到的DOM对象，Ext.getBody()== document.body
		*/
		var type= new Ext.Viewport({
			layout: 'border',
			id:'type',
			items: [ menuPanel, {
				xtype: 'estlayout',
				region: 'center',
				items: [tabPanel,treePanel]
			}],
			renderTo: Ext.getBody()
		});
	
		/*
			事件绑定，使用on
				这里绑定了dblclick和click两个方法
				tab组件.setActiveTab('')，切换到哪个Tab页
				组件.purgeListeners()，移除所有方法监听
				grid组件.getSelectionModel().getSelected():
					getSelectionModel获取选择管理器
					getSelected获取当前选中项
		*/
		
		
		Ext.getCmp('grid').on({'rowdblclick': function(){
				Ext.getCmp('tabpanel').setActiveTab('detail');
			},
			'rowclick': function(){
				Ext.getCmp('detail').purgeListeners();
				Ext.getCmp('detail').on({'afterlayout': function(){
						var id = Ext.getCmp('grid').getSelectionModel().getSelected().id;
						if(id) {
							console.log(Ext.getCmp('grid').getSelectionModel().getSelected());
							Ext.getCmp('formpanel').doLoad({typeid: id});
						}
						Ext.getCmp('detail').purgeListeners();
					}
				});
			}
		});
		
		Ext.getCmp('treepanel').on({'click': function(node,e){
			Ext.getCmp('grid').doSearch({'typeid':node.id},true);
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