Ext.namespace('Est.ux');
/**
 * Est.ux.WfStartflowButton Extension Class
 * 
 * @author jingpj
 * @version 1.0
 * 
 * @class Est.ux.WfStartflowButton
 * @extends Ext.Button
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 */
Est.ux.WfStartflowButton = function(cfg) {
	var self = this;
	this.bindGrid = cfg.bindGrid;
	this.isPrimaryTable = cfg.isPrimaryTable ? cfg.isPrimaryTable : true;
	this.moduleid = cfg.moduleid || _moduleId;

	this.beforeStart = cfg.beforeStart;

	this.afterStart = cfg.afterStart;
	this.afterWinShow = cfg.afterWinShow;
	
	this.topic = cfg.topic;
	
	this.topicfield = cfg.topicfield;

	this.masterid = 0;

	var groupTreePanel = {
		treeid : '_groupTreePanel',// 弹出窗口中树的名称
		treeLoderUrl : 'est/workflow/processdefination/WfDftaskgroup/getWfDftaskgroupTreeList',// 树的URL
		treeSCParam : {
			groupId : 'id',
			taskId : 'code'
		},// 树节点ID对应的gird的查询条件字段
		treeWidth : '150',// 树的宽度
		rootTxt : '可选流程角色'
	}
	var userCols = {
		id : 'userid',
		searchFields : ['姓名:', {
					width : 150,
					name : 'userName',
					xtype : 'textfield',
					id : 'userName'
				}],
		searchMapping : {
			'userName' : 'userName'
		},
		singleSelect : false,
		storeurl : 'est/workflow/processdefination/WfDftaskgroup/getUserList',
		cols : [{
					dataIndex : 'username',
					header : '姓名',
					width : 80,
					sortable : false,
					isReturn : true,
					fieldto : 'approvepeople'
				}, {
					dataIndex : 'sysDept.deptname',
					header : '所在部门',
					width : 100,
					sortable : true
				}, {
					dataIndex : 'duty',
					header : '职务',
					width : 100,
					sortable : true
				}, {
					dataIndex : 'userid',
					header : '用户id',
					width : 30,
					sortable : false,
					hidden : true,
					isReturn : true,
					fieldto : 'nextStepAssignUserid'
				}]
	}
	var feedBackFunc = function(data) {
		var grid = Ext.getCmp('usernodegrid');
		var store = grid.getStore();
		var count = store.getCount();
		var isRepeat = false;
		/*
		 * for (var k = 0; k < data.length; k++) {
		 * 
		 *  }
		 */
		Ext.getCmp('nodeuserpop').hide();
	}

	this.chooseFlowDefWin = new Ext.Window({
		id : '_chooseFlowDefWin',
		title : '请选择流程',
		width : 400,
		height : 238,
		closeAction : 'hide',
		frame : true,
		plain : true,
		items : [{
			xtype : 'form',
			id : '_chooseFlowDefForm',
			labelWidth : 80,
			baseCls : "x-plain",
			bodyStyle : "padding:20px 5px 5px 20px;",
			items : [{
				fieldLabel : '流程',
				xtype : 'estcombos',
				hiddenName : 'processdefinationId',
				allowBlank : false,
				width : 250,
				valueField : 'processId',
				displayField : 'name',
				storeurl : 'est/workflow/processdefination/WfProcessDefination/getProcessDefinationListByModuleId?moduleId='
						+ self.moduleid,
				listeners : {
					'select' : function() {
						var store = Ext.getCmp('_chooseFlowDefForm').form
								.findField('nextStepTaskNodeId').getStore();
						store.baseParams = {
							processId : this.getValue()
						};
						store.reload();
					}
				}
			}, {
				fieldLabel : '任务开始节点',
				xtype : 'estcombos',
				id : '_nextStepTaskNodeId',
				hiddenName : 'nextStepTaskNodeId',
				allowBlank : false,
				width : 250,
				valueField : 'taskId',
				displayField : 'name',
				storeurl : 'est/workflow/processdefination/WfProcessDefination/getWfDftaskListByProcessId'
			}, {
				fieldLabel : '审批人',
				name : 'approvepeople',
				allowBlank : false,
				xtype : 'treegridchooserfield',
				width : 250,
				title : '审批人选择',
				winid : '_nodeuserpop',
				winWidth : 500,
				winHeight : 500,
				treePanel : groupTreePanel,
				colstype : userCols,
				feedbackFormid : '_chooseFlowDefForm',
				onTriggerClick : function() {
					var taskId = Ext.getCmp('_nextStepTaskNodeId').getValue();
					if (!taskId) {
						alert("请先选择任务开始节点！");
						return;
					}

					var grouptree = Ext.getCmp('_groupTreePanel');
					grouptree.loader.baseParams = {
						taskId : taskId,
						_r : new Date().getTime()
					};
					var rootnode = grouptree.getRootNode();
					rootnode.attributes['code'] = taskId;
					rootnode.reload();

					this.win.gridPanel.doSearch({
								taskId : taskId
							});

					this.win.showWin();

				}
			},
					/*
					 * {fieldLabel:'签字类型',xtype:
					 * 'estcombos',hiddenName:'nextStepSignType',value:'签字',allowBlank:false,width:250,elms:[['签字','签字'],['会签','会签']]},
					 */
					{
						fieldLabel : '签字类型',
						xtype : 'radiogroup',
						width : 246,
						items : [{
									boxLabel : '签字',
									inputValue : '签字',
									name : 'nextStepSignType',
									checked : true
								}, {
									boxLabel : '会签',
									inputValue : '会签',
									name : 'nextStepSignType'
								}]
					}, {
						fieldLabel : '标题',
						xtype : 'hidden',
						name : 'topic',
						allowBlank : true,
						width : 250
					}, {
						xtype : 'hidden',
						name : 'moduleid',
						value : self.moduleid
					}, {
						xtype : 'hidden',
						name : 'nextStepAssignUserid'
					}, {
						xtype : 'hidden',
						name : 'masterid'
					}]
		}],
		buttons : [{
			text : '确定',
			handler : function() {
				var chooseform = Ext.getCmp('_chooseFlowDefForm');
				chooseform.form.findField('masterid').setValue("{masterids:"
						+ Ext.encode(self.masterid) + "}");
				// alert(chooseform.form.findField('masterid').getValue());
				if (chooseform.form.isValid()) {
					chooseform.form.submit({
								url : 'est/workflow/process/WfProcess/startProcess',
								method : 'POST',
								waitMsg : '保存中...',
								success : function(form, rep) {
									console.log(rep.result.data);
									/*
									 * for(var i=0; i<refobjs.length; i+=1) {
									 * if(refobjs[i].getXType()=='estgrid') {
									 * refobjs[i].getBottomToolbar().changePage(1);
									 * refobjs[i].getSelectionModel().clearSelections(); } }
									 */
									info('提示', '发起流程成功。');
									Ext.getCmp('_chooseFlowDefWin').hide();
									chooseform.form.reset();
									Ext.getCmp(self.bindGrid).getStore()
											.reload();
									if (self.afterStart) {
										self.afterStart();
									}
								},
								failure : function() {
									error('错误', '发起流程失败。');
								}
							});
				}

			}

		}, {
			text : '关闭',
			handler : function() {
				Ext.getCmp('_chooseFlowDefWin').hide();
				Ext.getCmp('_chooseFlowDefForm').form.reset();
			}
		}]

	});

	Est.ux.WfStartflowButton.superclass.constructor.call(this, cfg);
};

Ext.extend(Est.ux.WfStartflowButton, Ext.Button, {
			onClick : function() {
				// Est.ux.WfStartflowButton.superclass.onClick.call(this);

				if (this.isPrimaryTable) {
					var grid = Ext.getCmp(this.bindGrid);
					var selectedRow = grid.getSelectionModel().getSelected();
					if (!selectedRow) {
						error('错误', '请先在列表中选择一条记录！');
						return;
					}

					if (selectedRow.data['wfstatus'] !== _WF_NOT_START
							&& selectedRow.data['wfstatus'] !== '') {
						error('提示', '已发起流程，不能再发起流程！');
						return;
					}
					var data  = Ext.getCmp(this.bindGrid)
							.getSelectionModel().getSelected();
							
					
					var tmp = [];
					
					var d = {};
					
					d['masterId'] = data.id;
					
					if(this.topic) {
						d[data.id] = data.data[this.topic]
						if(this.topicfield) {
							d[data.id] = d[data.id] + "-" + data.data[this.topicfield];
						}
					}  else {
						d[data.id] = '';
					}
					tmp.push(d)
					this.masterid = tmp;
					
				} else {
					this.masterid = this.beforeStart();
				}

				if (this.masterid) {
					this.chooseFlowDefWin.show();
					if (this.afterWinShow) {
						this.afterWinShow();
					}

				}
			}
		});

Ext.reg('estwfstartflowbtn', Est.ux.WfStartflowButton);
