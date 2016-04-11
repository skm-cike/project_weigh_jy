Ext.namespace('Est.ux');
Est.ux.WfApproveButton = function(cfg) {
	var self = this;
	this.bindGrid = cfg.bindGrid;
	this.piid = cfg.piid;
	this.text = '审批'
	// this.handler=function(){};
	// 物资审批定义列
	this.approveColumn = cfg.approveColumn;
	// 审批状态定义列
	this.stateColum = cfg.stateColum;
	this.title = '审批';
	this.id = 'approveTab';
	this.layout = 'fit';
	this.login = cfg.login;
	this.username = cfg.login;
	this.appoveinfo;
	this.result = "通过";
	this.nodetype = "node";
	
	this.beforeResultCallback = cfg.beforeResultCallback;// 审批结论选中之前的回调处理函数
	this.afterResultCallback = cfg.afterResultCallback;// 审批结论选中后的回调处理函数

	this.beforeNodeCallback = cfg.beforeNodeCallback;// 签字节点选中后的回调
	this.afterNodeCallback = cfg.afterNodeCallback;// 签字节点选中后的回调

	this.beforeSendCallback = cfg.beforeSendCallback;// 发送之前的回调函数
	this.afterSendCallback = cfg.afterSendCallback;// 发送之前的回调函数

	this.beforeSignCallback = cfg.beforeSignCallback;// 处理人签名之前的回调处理函数
	this.afterSignCallback = cfg.afterSignCallback;// 处理人签名之后的回调处理函数

	this.beforeWinShowCallback = cfg.beforeWinShowCallback;// 审批窗口弹出之前的回调函数
	this.afterWinShowCallback = cfg.afterWinShowCallback;// 审批窗口弹出之后的回调函数
	this.appylyValuesFn = cfg.appylyValuesFn;//外部函数，用于审批时需要额外向工作流表单提交信息中添加额外信息

	

	this.checkedRows = [];

	this.rejectWin = function(fieldset) {
		var win = new Ext.Window({
			id : 'rejectWin',
			title : '驳回流程选择',
			width : 950,
			height : 500,
			closeAction : 'hide',
			autoScroll : true,
			items : [{
						xtype : 'estform',
						id : 'rejectform',
						formurl : '',
						method : '',
						autoScroll : true,
						labelWidth : 50,
						colstype : fieldset
					}],
			buttons : [{
				text : '确定',
				handler : function() {
					var values = Ext.getCmp('rejectform').getForm().getValues();
					var subdata = {};
					var piids = [];
					var data = {};
					for (var key in values) {
						var value = "'" + values[key] + "'";
						var index = value.indexOf(",");
						piids.push(key);
						if (index != -1) {
							subdata[key] = value.slice(index + 1, value.length
											- 1);
						} else {
							subdata[key] = values[key];
						}
					}
					data['piidsStr'] = piids.join();
					data['pm'] = subdata;
					// alert(Ext.encode(data));
					Ext.getCmp('wfApproveForm').form.findField('piids')
							.setValue(Ext.encode(data));
					win.close();
				},
				scope : this

			}, {
				text : '关闭',
				handler : function() {
					win.close();
				},
				scope : this
			}]
		});
		win.show();

	}

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
	/*
	 * var deptTreePanel={ treeid:'_deptTreePanel',//弹出窗口中树的名称
	 * treeLoderUrl:'est/sysinit/sysdept/SysDept/getDepartTree',//树的URL
	 * treeSCParam:'deptId',//树节点ID对应的gird的查询条件字段 treeWidth:'150',//树的宽度
	 * rootTxt:'部门' } var userCols={id:'userid', lable:'姓名:', scname:'userName',
	 * singleSelect:false, storeurl:'est/sysinit/sysuser/SysUser/getUserList',
	 * cols:[{dataIndex:'username', header: '姓名', width: 80, sortable:
	 * false,isReturn:true,fieldto:'approvepeople'},
	 * {dataIndex:'sysDept.deptname', header: '所在部门',width: 100, sortable:
	 * true}, {dataIndex:'duty', header: '职务',width: 100, sortable: true},
	 * {dataIndex:'userid', header: '用户id', width: 20, hidden:true,sortable:
	 * false,isReturn:true,fieldto:'nextStepAssignUserid'} ] }
	 */
	var feedBackFunc = function(data) {
		var grid = Ext.getCmp('usernodegrid');
		var store = grid.getStore();
		var count = store.getCount();
		var isRepeat = false;
		Ext.getCmp('nodeuserpop').hide();
	}

	this.sendtoNextStep = function(formvalues) {

		Ext.Ajax.request({
					url : 'est/workflow/process/WfProcess/sendMultiToNextStep',
					params : formvalues,
					success : function(response, options) {

						var responseJson = Ext.decode(response.responseText);
						if (responseJson.success) {
							msg("提示", "流程发送成功！");
							self.removeAppoveinfo();
						} else {
							msg("错误", responseJson.error);
						}

						// setTimeout(function(){window.history.go(-1)},3000);
					},
					failure : function() {

						error("错误", "流程发送失败！");
					}
				});
	}
	// 驳回任务
	this.rejectTask = function(formvalues) {
		Ext.Ajax.request({
					url : 'est/workflow/process/WfProcess/rejectTask',
					params : formvalues,
					success : function(response, options) {
						var responseJson = Ext.decode(response.responseText);
						if (responseJson.success) {
							msg("提示", "流程驳回成功！");
							self.removeAppoveinfo();
						} else {
							msg("错误", "流程驳回失败！");
						}
					},
					failure : function() {
						error("错误", "流程驳回失败！");
					}
				});

	}
	this.terminateStep = function(formvalues) {
		
		
		Ext.Ajax.request({
					url : 'est/workflow/process/WfProcess/endProcess',
					params : formvalues,
					success : function(response, options) {
						var responseJson = Ext.decode(response.responseText);
						if (responseJson.success) {
							msg("提示", "流程作废成功！");
							self.removeAppoveinfo();
						} else {
							msg("错误", responseJson.error);
						}
					},
					failure : function() {
						error("错误", "流程作废失败！");
					}
				});
	}
	this.removeAppoveinfo = function() {
		var store = Ext.getCmp(self.bindGrid).getStore();
		for (var i = 0; i < self.appoveinfo.length; i++) {
			store.each(function(rec) {
						if (self.appoveinfo[i] == rec.data[self.piid]) {
							store.remove(rec);
						}
					});
		}
	}

	this.checkResult = function(result) {

		if (this.beforeResultCallback) {
			if (!this.beforeResultCallback(result)) {
				return;
			}
		}

		var form = Ext.getCmp('wfApproveForm').getForm();
		if (result == '通过') {
			// form.findField('approvepeople').allowBlank=false;
			form.findField('approvepeople').setDisabled(false);
			Ext.getCmp('nextStepSignTypeGroup').setDisabled(false);
			// form.findField('nextStepTaskNodeId').allowBlank=false;
			form.findField('nextStepTaskNodeId').setDisabled(false);
			form.findField('opinion').allowBlank = true;
			self.result = "通过";
		} else if (result == '驳回') {
			form.findField('approvepeople').clearInvalid();
			form.findField('approvepeople').reset();
			form.findField('nextStepTaskNodeId').reset();
			var piidsStr = '';
			var piids = [];
			var names = [];
			for (var i = 0; i < self.checkedRows.length; i++) {
				piids.push(self.checkedRows[i].data[self.piid]);
				names.push(self.checkedRows[i].data[self.approveColumn]);
			}
			Est.syncRequest({
						url : 'est/workflow/process/WfProcess/buildingRegjectByPiids',
						params : {
							piids : piids.join(),
							names : names.join()
						},
						success : function(conn) {
							var responseJson = Ext.decode(conn.responseText);
							var win = Ext.getCmp('rejectWin');
							if (win) {
								win.destroy();
							}
							self.rejectWin(responseJson.fieldset);
						},
						failure : function(conn) {
							error("错误", "获取流程驳回信息失败！");
						}
					});
			form.findField('approvepeople').allowBlank = true;
			form.findField('approvepeople').setDisabled(true);
			Ext.getCmp('nextStepSignTypeGroup').setDisabled(true);
			form.findField('nextStepTaskNodeId').allowBlank = true;
			form.findField('nextStepTaskNodeId').setDisabled(true);
			form.findField('opinion').allowBlank = false;
			self.result = "驳回";
		} else if (result == '作废') {
			form.findField('approvepeople').clearInvalid();
			form.findField('approvepeople').reset();
			form.findField('nextStepTaskNodeId').reset();
			form.findField('approvepeople').allowBlank = true;
			form.findField('approvepeople').setDisabled(true);
			Ext.getCmp('nextStepSignTypeGroup').setDisabled(true);
			form.findField('nextStepTaskNodeId').allowBlank = true;
			form.findField('nextStepTaskNodeId').setDisabled(true);
			form.findField('opinion').allowBlank = false;
			self.result = "作废";
		}
		
		if (this.afterResultCallback) {
			this.afterResultCallback(result);
		}
	}
	//发送
	var sendFn=function(){
				
				var signForm = Ext.getCmp('signForm').form;
				var wfForm = Ext.getCmp('wfApproveForm').getForm();

				var formvalues = wfForm.getValues();
				Ext.apply(formvalues, {
							login : signForm.findField('login').getValue(),
							password : signForm.findField('password')
									.getValue(),
							username : self.username
						});
				// var result = wfForm.findField('result').getValue();
				// alert(wfForm.findField('piids').getValue());
				// return;
				if (self.beforeSignCallback) {
					if (!self.beforeSignCallback(formvalues)) {
						return;
					}
				}
				
				Ext.Ajax.request({
					url : 'est/workflow/process/WfProcess/checkIsCountersign',
					params : formvalues,
					success : function(reps) {
						var json = Ext.decode(reps.responseText);
						var flag = json.success;
						var form = Ext.getCmp('wfApproveForm').getForm();
						if (self.result == '通过' && self.nodetype != 'end') {
							if (flag) {
								form.findField('approvepeople').allowBlank = true;
								// form.findField('approvepeople').setDisabled(true);
								// Ext.getCmp('nextStepSignTypeGroup').setDisabled(true);
								form.findField('nextStepTaskNodeId').allowBlank = true;
								// form.findField('nextStepTaskNodeId').setDisabled(true);
							} else {
								form.findField('approvepeople').allowBlank = false;
								// form.findField('approvepeople').setDisabled(false);
								// Ext.getCmp('nextStepSignTypeGroup').setDisabled(false);
								form.findField('nextStepTaskNodeId').allowBlank = false;
								// form.findField('nextStepTaskNodeId').setDisabled(false);
								form.findField('opinion').allowBlank = true;
							}
						}
						if (json.type == 'countersign' && flag === true) {
							error('提示', '当前是会签任务，您不是最后一个审批人，因此选择的下一流程选择无效!');
						}
						
						if(self.appylyValuesFn){//如果定义了appylyValuesFn函数，则必须返回json对象字符。如果返回为false，则流程不会发送。
							
							var appylyValuesFn = self.appylyValuesFn;
							var appylyValues = appylyValuesFn(formvalues);
							
							
							if(typeof (appylyValues)=='object'){
								formvalues = Ext.apply(formvalues,appylyValuesFn(formvalues));
							}
							if(typeof(appylyValues)=='boolean' && appylyValues==false){
								return;							
							}
							
						}
						if (Ext.getCmp('wfApproveForm').getForm().isValid()) {
							// return;
							if (self.result == '作废') {
								self.terminateStep(formvalues);
							} else if (self.result == '通过') {
								self.sendtoNextStep(formvalues);
							} else if (self.result == '驳回') {
								self.rejectTask(formvalues);
							}
							Ext.getCmp('wfApproveForm').getForm().reset();
							self.signWin.hide();
							self.wfapproveWin.hide();
						}
						if (self.afterSignCallback) {
							self.afterSignCallback(formvalues);
						}
					},
					failure : function() {
						error("错误", "流程发送失败！");
					}
				});
		
		}

	/** 签字窗口 */
	this.signWin = new Ext.Window({

		title : '流程签字',
		width : 300,
		height : 150,
		resizable : false,
		layout : 'fit',
		plain : true,
		closeAction : "hide",
		items : [{
					region : 'center',
					xtype : 'form',
					labelWidth : 60,
					frame : true,
					bodyStyle : 'padding:10px,10px,10px,10px',
					id : 'signForm',
					items : [{
								xtype : 'textfield',
								fieldLabel : '审批人',
								value : self.username,
								anchor : '100%',
								allowBlank : false,
								readOnly : true
							}, {
								xtype : 'textfield',
								fieldLabel : '密码',
								name : 'password',
								inputType : 'password',
								anchor : '100%',
								allowBlank : false,
								enableKeyEvents:true,
							 	listeners:{
							 		"keyup":function(obj,e){
							 		 	if (e.getKey() == e.ENTER && this.getValue().length > 0) {
							 		 		sendFn();
				                 		}  
							 	  }}
							}, {
								xtype : 'textfield',
								name : 'login',
								value : self.login,
								xtype : 'hidden'
							}

					]
				}],
		buttons : [{
			text : '确定',
			handler : sendFn,
			scope : this

		}, {
			text : '关闭',
			handler : function() {
				self.signWin.hide();
			},
			scope : this
		}],
		listener : {}

	});

	var approveItems = [{
		xtype : 'estform',
		id : 'wfApproveForm',
		labelWidth : 55,
		// title:'审批意见',
		region : 'center',
		formurl : 'est/workflow/process/WfApprove/',
		method : 'ApproveInfo',
		colstype : [{
			id : 'approveId',
			fieldset : '详细',
			items : [{
						fieldLabel : '审批结论',
						xtype : 'radiogroup',
						id : 'resultRadio',
						colspan : 3,
						width : 220,
						items : [{
									boxLabel : '通过',
									inputValue : '通过',
									name : 'result',
									checked : true,
									listeners : {
										'check' : function(obj, checked) {
											if (checked) {
												self.checkResult(this
														.getRawValue())
											}
										}
									}
								}, {
									boxLabel : '驳回',
									inputValue : '驳回',
									name : 'result',
									listeners : {
										'check' : function(obj, checked) {
											if (checked) {
												self.checkResult(this
														.getRawValue())
											}
										}
									}
								}, {
									boxLabel : '作废',
									inputValue : '作废',
									name : 'result',
									listeners : {
										'check' : function(obj, checked) {
											if (checked) {
												self.checkResult(this
														.getRawValue())
											}
										}
									}
								}]
					}, {
						fieldLabel : '审批意见',
						xtype : 'textarea',
						name : 'opinion',
						width : 220,
						colspan : 3
					}, {
						fieldLabel : 'piids',
						name : 'piids',
						xtype : 'hidden'
					}

			]
		}, {
			fieldset : '流程下一步选择',
			items : [{
				fieldLabel : '任务选择',
				xtype : 'estcombos',
				colspan : 3,
				id : '_nextStepTaskNodeId',
				hiddenName : 'nextStepTaskNodeId',
				allowBlank : true,
				width : 220,
				valueField : 'taskId',
				displayField : 'name',
				storeurl : 'est/workflow/processdefination/WfProcessDefination/getNextWfDftasksByProcessInstanId',
				listeners : {
					'select' : function() {
						var _self = this;
						Ext.Ajax.request({
							url : 'est/workflow/processdefination/WfProcessDefination/getWfDftask',
							params : {
								taskId : _self.getValue()
							},
							success : function(response) {
								var nodetype = Ext
										.decode(response.responseText).data.nodetype;
								var form = Ext.getCmp('wfApproveForm')
										.getForm();
								self.nodetype = nodetype;
								if (nodetype == 'end') {
									form.findField('approvepeople')
											.clearInvalid();
									form.findField('approvepeople').reset();
									form.findField('approvepeople').allowBlank = true;
									form.findField('approvepeople')
											.setDisabled(true);
									Ext.getCmp('nextStepSignTypeGroup')
											.setDisabled(true);
								} else {
									form.findField('approvepeople').allowBlank = false;
									form.findField('approvepeople')
											.setDisabled(false);
									Ext.getCmp('nextStepSignTypeGroup')
											.setDisabled(false);
								}
							},
							failure : function() {
								form.findField('approvepeople').allowBlank = false;
								form.findField('approvepeople')
										.setDisabled(false);
								Ext.getCmp('nextStepSignTypeGroup')
										.setDisabled(false);
								error("错误", "获取任务节点错误，请重新选择任务！");
							}
						});
					}
				}
			}, {
				fieldLabel : '签字类型',
				xtype : 'radiogroup',
				id : 'nextStepSignTypeGroup',
				colspan : 3,
				width : 220,
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
				fieldLabel : '处理人',
				name : 'approvepeople',
				colspan : 3,
				xtype : 'treegridchooserfield',
				width : 220,
				title : '审批人选择',
				winid : '_nodeuserpop',
				winWidth : 500,
				winHeight : 500,
				treePanel : groupTreePanel,
				colstype : userCols,
				feedbackFormid : 'wfApproveForm',
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

					if (this.afterSignCallback) {
						this.afterSignCallback(signername);
					}

				}
			}, {
				xtype : 'hidden',
				name : 'nextStepAssignUserid'
			}]
		}]
	}];

	/* 审批弹出窗口 */
	this.wfapproveWin = new Ext.Window({
		id : 'wfapproveWin',
		title : '审批意见',
		width : 350,
		height : 400,
		closeAction : 'hide',
		frame : true,
		plain : true,
		items : approveItems,
		buttons : [{
					text : '发送',
					handler : function() {

						var form = Ext.getCmp('wfApproveForm').getForm();
						var values = form.getValues();

						if (this.beforeSendCallback) {
							if (!this.beforeSendCallback(values)) {
								return;
							}
						}

						if (self.nodetype == 'end') {
							form.findField('approvepeople').clearInvalid();
							form.findField('nextStepTaskNodeId').clearInvalid();
							form.findField('approvepeople').allowBlank = true;
							// form.findField('approvepeople').setDisabled(true);
							// Ext.getCmp('nextStepSignTypeGroup').setDisabled(true);
						}
						if (self.result != '通过') {

							form.findField('approvepeople').clearInvalid();
							form.findField('nextStepTaskNodeId').clearInvalid();
							form.findField('approvepeople').allowBlank = true;
							form.findField('approvepeople').setDisabled(true);
							Ext.getCmp('nextStepSignTypeGroup')
									.setDisabled(true);
						}

						if (form.isValid()) {
							self.signWin.show();
						}

						if (this.afterSendCallback) {
							this.afterSendCallback(values);
						}
					},
					scope : this
				}, {
					text : '关闭',
					handler : function() {
						Ext.getCmp('wfApproveForm').getForm().reset();
						self.wfapproveWin.hide();
					},
					scope : this
				}

		]

	});

	Est.ux.WfApproveButton.superclass.constructor.call(this, cfg);

};

Ext.extend(Est.ux.WfApproveButton, Ext.Toolbar.Button, {
	handler : function() {

		var gridObj = Ext.getCmp(this.bindGrid);
		
		if (gridObj.getXType()=='esteditgrid' &&  gridObj.getCheckedRecords(true) !== undefined) {
			this.checkedRows = gridObj.getCheckedRecords(true);
		} else {
			this.checkedRows.push( gridObj.getSelectionModel().getSelected() );
		}
		if (this.checkedRows && this.checkedRows.length > 0) {
			if (this.beforeWinShowCallback) {
				if (!this.beforeWinShowCallback()) {
					return;
				}
			}

			this.wfapproveWin.show();
			var wfApproveForm = Ext.getCmp('wfApproveForm');
			/*
			 * store.each( function(rec){
			 * 
			 * if(rec.data['name'].indexOf('结束')!=-1){
			 * form.findField('nextStepTaskNodeId').select(i); } } );
			 */

			var piids = [];
			var len = this.checkedRows.length;
			for (var i = 0; i < len; i++) {
				if (i < len - 1) {
					var nextindex = i + 1;
					var curent = this.checkedRows[i].data[this.stateColum];
					var next = this.checkedRows[nextindex].data[this.stateColum];
					if (curent === next) {
						piids.push(this.checkedRows[i].data[this.piid]);
					} else {
						error('提示', '只能选择相同审批状态的任务!');
						return;
					}
				} else {
					piids.push(this.checkedRows[i].data[this.piid]);
				}
			}
			this.appoveinfo = piids;
			// alert(piids.length);
			var piid = this.checkedRows[0].data[this.piid];
			// wfApproveForm.doLoad({_wfProcessInstanceId : piid});
			var form = wfApproveForm.form;
			form.reset()
			form.findField('nextStepTaskNodeId').getStore().load({
				params : {
					processinstanceId : piid
				},
				callback : function(r) {
					for (var i = 0; i < r.length; i++) {
						if (r[i].data.name.indexOf('结束') != -1) {
							form.findField('nextStepTaskNodeId')
									.setRawValue(r[i].data.name);
							form.findField('nextStepTaskNodeId')
									.setValue(r[i].data.taskId);
							form.findField('approvepeople').clearInvalid();
							form.findField('approvepeople').reset();
							form.findField('approvepeople').allowBlank = true;
							form.findField('approvepeople').setDisabled(true);
							Ext.getCmp('nextStepSignTypeGroup')
									.setDisabled(true);
							break;
						}
					}

				}
			});
			form.findField('piids').setValue(piids.join());
			form.findField('approvepeople').setDisabled(false);
			Ext.getCmp('nextStepSignTypeGroup').setDisabled(false);
			form.findField('nextStepTaskNodeId').setDisabled(false);
			if (this.afterWinShowCallback) {
				this.afterWinShowCallback();
			}
		}

	}
});

Ext.reg('estwfapprovebtn', Est.ux.WfApproveButton);
