Ext.namespace("Est.ux");
/**
 * Est.ux.FPValitateWin Extension Class xtype is extsignWin
 * 
 * @author hebo
 * @modified by jingpj 2009.7.7
 * @version 2.0
 * 
 * @class Est.ux.FPValitateWin
 * @extends Ext.Window
 * 
 * 
 * @cfg {String} id,formId, require
 * @cfg {Array[]} getFields,inputFields require
 * 
 * for example: var toby = Ext.get('toby');//弹出窗口的位置 var win; if(!win){ win=new
 * Est.ux.FPValitateWin({ feedbackFormId:'checkform', feedbackFields :
 * {'username':'checkby','userid':'checkbyID'}, username: 'admin', title: '登录'
 * }); } win.show(toby)
 * 
 * 
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 */
Est.ux.FPValitateWin = function(cfg) {

	// public
	/** 验证成功后回填数据Form的id */
	this.feedbackFormId = cfg.feedbackFormId;
	this.feedbackGridid = cfg.feedbackGridid;
	/** 回填数据Form的字段列表，格式为[{"vo字段名1":"form字段名1"}，{"vo字段名2":"form字段名2"}....] */
	this.feedbackFields = cfg.feedbackFields;
	/** 签字框标题,默认为"签名" */
	this.title = cfg.title || "指纹认证";
	/** 默认（当前）用户登录名 */
	this.login = cfg.login;
	// private
	this.width = 400;
	this.height = 150;
	this.layout = "form";
	this.frame = true;
	this.plain = true;
	this.buttonAlign = "right";
	this.closeAction = "hide";
	this.resizable = false, this.id = cfg.id;
	this.callbackFun = cfg.callbackFun;

	this.form = new Ext.FormPanel({
		id : "fpValitateForm",
		labelWidth : 50,
		baseCls : "x-plain",
		bodyStyle : "padding:20px 5px 5px 20px;",
		defaults : {
			xtype : "textfield",
			width : "250",
			allowBlank : false
		},
		items : [{
					fieldLabel : "姓&nbsp;&nbsp;名",
					name : "username",
					value : cfg.username || "",
					enableKeyEvents : true,
					listeners : {
						"keyup" : function(obj, e) {
							if (e.getKey() == e.ENTER
									&& this.getValue().length > 0) {
								this.ownerCt.onSubmit();
							}

						}
					}
				}, {
					fieldLabel : "指&nbsp;&nbsp;纹",
					name : "fp",
					inputType : "password",
					readOnly : true,
					enableKeyEvents : true,
					listeners : {
						"keyup" : function(obj, e) {
							if (e.getKey() == e.ENTER
									&& this.getValue().length > 0) {
								this.ownerCt.onSubmit();
							}
						},
						'focus' : function(objs) {
							if (this.ownerCt.form.findField('username')
									.getValue() == '') {
								this.ownerCt.form.findField('username').focus();
								alert('请输入姓名');
								return;
							}
							if (objs.getValue() == '') {
								var fpcode = validateFp();
								objs.setValue(fpcode);
								if (objs.getValue() != '') {
									this.ownerCt.onSubmit();
								}
							}
						}
					}
				}],
		onSubmit : function() {
			if (this.form.isValid()) {
				this.form.doAction("submit", {
					waitMsg : "指纹验证中...",
					url : "est/common/business/SysUser/verfyUserFp",
					params : {
						"_r" : new Date()
					},
					method : "post",
					success : function(form, action) {
						// alert(this.feedbackFields);
						var userInfo = action.result.data;

						if (this.ownerCt.feedbackFormId) {
							var feedbackForm = Ext
									.getCmp(this.ownerCt.feedbackFormId).form;
							for (var key in this.ownerCt.feedbackFields) {
								var value = eval("(userInfo." + key + ")");
								feedbackForm
										.findField(this.ownerCt.feedbackFields[key])
										.setValue(value);
								// feedbackForm.findField(this.ownerCt.feedbackFields[key]).setValue(userInfo[key]);
							}
						}

						if (this.ownerCt.feedbackGridid) {
							var selectedRecord = Ext
									.getCmp(this.ownerCt.feedbackGridid)
									.getSelectionModel().getSelected();
							if (selectedRecord) {
								for (var key in this.ownerCt.feedbackFields) {
									var value = eval("(userInfo." + key + ")");
									selectedRecord.set(
											this.ownerCt.feedbackFields[key],
											value);
									// 修改record修改状态 add by jingpj
									selectedRecord["notChange"] = false; // 新建后已修改标识
									if (selectedRecord["modifystatus"] != "a"
											&& selectedRecord["modifystatus"] != "d") {
										selectedRecord["modifystatus"] = "m";
									}
								}
							}
						}

						this.form.reset();
						this.ownerCt.hide();
					},
					failure : function() {
						Ext.Msg.alert("错误", "指纹验证失败!");
						if (this.ownerCt.feedbackFormId) {
							var feedbackForm = Ext
									.getCmp(this.ownerCt.feedbackFormId).form;
							for (var key in this.ownerCt.feedbackFields) {
								feedbackForm
										.findField(this.ownerCt.feedbackFields[key])
										.setValue('');
							}
						}
						if (this.ownerCt.feedbackGridid) {
							var selectedRecord = Ext
									.getCmp(this.ownerCt.feedbackGridid)
									.getSelectionModel().getSelected();
							if (selectedRecord) {
								for (var key in this.ownerCt.feedbackFields) {
									selectedRecord.set(
											this.ownerCt.feedbackFields[key],
											'');
									// 修改record修改状态 add by jingpj
									selectedRecord["notChange"] = false; // 新建后已修改标识
									if (selectedRecord["modifystatus"] != "a"
											&& selectedRecord["modifystatus"] != "d") {
										selectedRecord["modifystatus"] = "m";
									}
								}
							}
						}
						Ext.getCmp('fpValitateForm').form.findField('fp')
								.reset();
						this.ownerCt.hide();
					},
					scope : this
				});
			}
		}
	});

	var getFields = cfg.getFields;
	var inputFields = cfg.inputFields;

	this.items = [this.form];

	this.buttons = [{
				text : "按指纹",
				handler : function() {
					this.form.form.findField('fp').reset();
					this.form.form.findField('fp').focus();
				},
				scope : this
			}, {
				text : "关闭",
				handler : function() {
					this.form.form.reset();
					this.hide();
				},
				scope : this
			}];
	Est.ux.FPValitateWin.superclass.constructor.call(this, cfg);
};

Ext.extend(Est.ux.FPValitateWin, Ext.Window, {
			show : function(param) {
				Est.ux.FPValitateWin.superclass.show.call(this, param);
				this.form.form.findField('username').setValue(this.login);
				if (this.callbackFun) {
					var value = this.callbackFun();
					this.form.form.findField('username').setValue(value);
				}
				// this.setActive();
				this.form.form.findField('username').focus(true, true);
			}
		});
Ext.reg("fpvalitate", Est.ux.FPValitateWin);
