Ext.ns('Est.ux');
/**
 * Est.ux.SignButton Extension Class a form field with sign window
 * 
 * @author jingpj
 * @version 1.0
 * 
 * @class Est.ux.SignField
 * @extends Ext.form.TriggerField
 * @constructor
 * @param {Object}
 *          cfg Configuration options
 * 
 * @cfg signWin {String}
 * @cfg elms {Object} @ for example
 *      {xtype:'estsignfield',fieldLabel:'交待人',name:'remindby', allowBlank:
 *      false,readOnly:true,signWin:signWin, feedbackFormId:'formpanel',
 *      feedbackFields:{'username':'remindby','userid':'remindbyid'},
 *      login:'${loginUser.login}' }
 * 
 */
Est.ux.SignButton = function(cfg) {

	// public
	/** 签字window */
	this.signWin = cfg.signWin;
	/** 默认用户名称 */
	this.login = cfg.login;

	/** 验证成功后回填数据Form的id */
	this.feedbackFormId = cfg.feedbackFormId;
	/** 回填数据Form的字段列表，格式为[{"vo字段名1":"form字段名1"}，{"vo字段名2":"form字段名2"}....] */
	this.feedbackFields = cfg.feedbackFields;

	this.fieldLabel = cfg.fieldLabel;
	this.allowBlank = cfg.allowBlank ? cfg.allowBlank : false;
	this.readOnly = cfg.readOnly ? cfg.readOnly : true;
	this.width = cfg.width ? cfg.width : 180;

	this.callbackFun = cfg.callbackFun;
	Est.ux.SignButton.superclass.constructor.call(this, cfg);

};

Ext.extend(Est.ux.SignButton, Ext.Button, {
			onClick : function() {
				this.showSignWin();
			},

			showSignWin : function() {
				if (!this.disabled) {
					this.signWin.login = this.login ? this.login : '';
					this.signWin.feedbackFormId = this.feedbackFormId;
					this.signWin.feedbackFields = this.feedbackFields;
					this.signWin.callbackFun = this.callbackFun;
					this.signWin.show();
				}
			}
		});

Ext.reg('estsignbtn', Est.ux.SignButton);
