<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!-- 
位置：系统管理-用户管理
说明：设置部门与角色
路径：/fuel_jt/est/sysinit/sysuser/SysUser/fwdUser
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
			formitems:  [{fieldset:'搜索',items:[{fieldLabel:'用户名',name:'userName'}]}]
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
		var formcols = [{id:'userid',fieldset:'详细',
			items: [
					{fieldLabel:'登录名',name:'login', allowBlank: false},
					{fieldLabel:'用户密码',name:'password', inputType: 'password', allowBlank: false},
					{fieldLabel:'用户名',name:'username'},
					{fieldLabel:'部门',xtype: 'estcombos',hiddenName:'sysDept.deptid', valueField:'deptid', displayField:'deptname', storeurl:'<%=basePath%>est/sysinit/sysdept/SysDept/getAllDepart'},
					new Ext.form.DateField({fieldLabel:'生日',name:'birthday',format:'Y-m-d',altFormats:'Y-m-d|Y-m-d H:i:s'}),
					{fieldLabel:'性別', xtype: 'estcombos', hiddenName:'sex', elms: [['男','男'],['女','女']]},
					{fieldLabel:'职务',name:'duty'},
					{fieldLabel:'电子邮件',name:'email',vtype:'email'},
					{fieldLabel:'电话',name:'mobile'},
					{fieldLabel:'办公电话',name:'officetel'}
					
			]}];
		/*
			form组件，包括
				formurl,form请求的地址，不包括具体方法
				method为请求的方法后缀，定义为User,刪除／保存／加载方法名分別为addUser/savUser/getUser
				colstype里面是form中的具体元素见formcols
				tbar为top bar，位于form顶端的按钮,其中的元素包括
					text按钮显示的名称，
					handler为点击按钮时触发的事件
				bbar为bottom bar，位于form底端的按钮
		*/
		var formPanel = {
			xtype: 'estform',
			id: 'formpanel',
			formurl: '<%=basePath%>est/sysinit/sysuser/SysUser',
			method: 'User',
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
		var colsType = {id: 'userid', 
				cols: [{dataIndex:'username', header: '用户名', width: '30', sortable: false},
						{dataIndex:'sysDept.deptname', header: '所在部门',width: '40', sortable: true},
						{dataIndex:'duty', header: '职务',width: '40', sortable: true}
					]};
					

		
		addWorkflowCols(colsType);

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
			title: '用户列表',
			id: 'gridpanel',
			tbar: [{
				text: '搜索',
				xtype: 'estsearchbtn',
				spId: 'searchpanel',
				pId: 'user'
			},'-'],
			items: [
				{xtype: 'estgrid', id:'grid', groupField:'sysDept.deptname',baseParams:{wfMasterId:_masterid},storeurl: '<%=basePath%>est/sysinit/sysuser/SysUser/getUserList?wfMasterId='+_masterid, colstype: colsType, region: 'center'}, searchPanel
			]
		};
		
		addWorkflowGridpanelQueryCombos(gridPanel);
		
		
		/*
		var approveTab = {
			title:'审批',
			id:'approveTab',
			layout:'fit',
			xtype:'estlayout',
			items:[{
				xtype:'estform',
				id:'wfApproveForm',
				title:'审批意见',
				formurl:'est/workflow/process/WfApprove/',
				method:'ApproveInfo',
				colstype:[{id:'approveId',fieldset:'详细',
					items: [
							{fieldLabel:'审批结论', xtype: 'estcombos', hiddenName:'result', elms: [['通过','通过'],['不通过','不通过']],colspan:3},
							{fieldLabel:'审批意见',xtype:'textarea',name:'opinion',width:500,colspan:3},
							{fieldLabel:'备注',xtype:'textarea',name:'remark',width:500,colspan:3},
							{fieldLabel:'审批人',name:'nameapproveby',xtype:'estsignfield',signWin:signWin,width:150,allowBlank:false,colspan:2,
          	  			    	feedbackFormId:'wfApproveForm',feedbackFields:{'username':'nameapproveby','userid':'approveby'}
          	  			    },
							{fieldLabel:'审批时间',xtype:'datefield',name:'approvedate',format:'Y-m-d',altFormats:'Y-m-d|Y-m-d H:i:s'},
							{fieldLabel:'wfProcessinstance.processinstanceId', name:'wfProcessinstance.processinstanceId',value:_wfProcessInstanceId},
							{fieldLabel:'processId', name:'processId',value:_taskDefid},
							{fieldLabel:'jbpmpiid',  name:'jbpmpiid', value:_wfProcessInstanceId},
							{fieldLabel:'jbpmtiid',  name:'jbpmtiid',value:_taskId},
							{fieldLabel:'approveby', name:'approveby'}
					]}
				],
				tbar:[
					{text:'保存',handler:function(){
						Ext.getCmp('wfApproveForm').doSumbit();
					}},
					{text:'发送',handler:function(){
						
						Ext.Ajax.request({
							url:'est/workflow/process/WfProcess/sendToNextStep',
							params:{tiid:_taskId},
							success:function(){
								msg("提示","流程发送成功！");
							},
							failure:function(){
								error("错误","流程发送失败！");
							}
						});
					}},
					{text:'驳回',handler:function(){
						
					}},
					{text:'终止',handler:function(){
						Ext.Ajax.request({
							url:'est/workflow/process/WfProcess/endProcess',
							params:{tiid:_taskId},
							success:function(){
								msg("提示","流程终止成功！");
							},
							failure:function(){
								error("错误","流程终止失败！");
							}
						});
					}}
				]
			
			}],
			listeners:{'afterlayout':function(){
				Ext.getCmp('wfApproveForm').doLoad({taskinstanceId: _taskId,_wfProcessInstanceId:_wfProcessInstanceId});
			}}
		
		};
		*/
		

		/*
			tab组件，只需把组件放入容器的items中就会自动形成Tab页。
				layout为fit为自适应式布局
		*/
		var tabPanel = {
			id: 'tabpanel',
			region: 'center',
			xtype: 'esttab',
			items: [gridPanel,{xtype:'estlayout',frame:true,title: '详细信息', id:'detail', layout:'fit', items:[formPanel]}]
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
		var treePanel = {xtype: 'esttree',isctx: true, rootTxt:'所有部门', id:'treepanel', region:'west',loaderurl: 'est/sysinit/sysdept/SysDept/getDepartTree',
				ctx: {url: '<%=basePath%>est/sysinit/sysdept/SysDept', method: 'Dept', ctxitems:[{id:'add',text:'添加'},{id:'modify',text:'修改'},{id:'delete',
					text:'刪除'}],
					formitems:
					 [{id:'deptid', fieldset:'Login', colnum: 1,
					 items:[{fieldLabel:'部门名称',name:'deptname',allowBlank: false},
					        {fieldLabel:'父级部门',xtype:'estcombos',hiddenName:'sysDept.deptid', valueField:'deptid', displayField:'deptname', storeurl:'<%=basePath%>est/sysinit/sysdept/SysDept/getAllDepart',  allowedempty: true},
							{fieldLabel:'部门编号',name:'deptcode',hidden:true,hideLabel:true}
						]}]}
				};
		/*
			Viewport组件，包括：
				renderTo是组件渲染到的DOM对象，Ext.getBody()== document.body
		*/
		var user= new Ext.Viewport({
			layout: 'border',
			id:'user',
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
							Ext.getCmp('formpanel').doLoad({userid: id});
						}
						Ext.getCmp('detail').purgeListeners();
					}
				});
				
			}
		});
		Ext.getCmp('treepanel').on({'click': function(node,e){
			Ext.getCmp('grid').doSearch({'deptId':node.id},true);
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
		
		
		
			
			
		//Ext.getCmp('gridpanel').getTopToolbar().insertButton(0,wfStatusquery) ;
		
		
		if(_isWf === '1'){
			//流程中
			var approveTab = new Est.ux.WfApproveTab({bindGrid:'grid'});
			Ext.getCmp('tabpanel').add(approveTab);
			
			var wfStatusquery = Ext.getCmp('wfStatusquery');
			wfStatusquery.setValue(_WF_IN_FLOW);
			wfStatusquery.readOnly = true;
			wfStatusquery.trigger.un('click',wfStatusquery.onTriggerClick ,wfStatusquery);
			
			
		} else {
			//非流程
			var gridpanel = Ext.getCmp('gridpanel');
			var flowstartbtn = new Est.ux.WfStartflowButton({text:'发起流程',bindGrid:'grid'});
			gridpanel.getTopToolbar().add(flowstartbtn); 
		
		}
		var approveHistoryTab = new Est.ux.WfApproveHistoryTab({bindGrid:'grid'});
		Ext.getCmp('tabpanel').add(approveHistoryTab);
		
		
		//Ext.getCmp('tabpanel').hideTabStripItem('detail');
		
		//Ext.getCmp('tabpanel').showTabStripItem('detail');
		
	});
	</script>
</html>