Ext.namespace('Est.ux');

Est.ux.AuditWindow = function(cfg) {
	this.width = 300;
	this.height = 180;
	this.layout = "form";
	this.frame = true;
	this.plain = true;
	this.buttonAlign = "right";
	this.closeAction = "hide";
	this.resizable = false,
	this.id = cfg.id;
	this.url = cfg.url;
	this.form = new Ext.FormPanel({
		id:'estauditformid',
		labelWidth : 50,
		baseCls : "x-plain",
		bodyStyle : "padding:20px 5px 5px 20px;",
		defaults : {xtype:"textfield", width:"160", allowBlank:false},
		items:[{fieldLabel:"用 户",name:"login",value:cfg.login || "",enableKeyEvents:true,
			 	allowBlank:false,
			 	readOnly:true,
			 	listeners:{
			 		"keyup":function(obj,e){
			 		 	if (e.getKey() == e.ENTER && this.getValue().length > 0) {  
			 		 		this.ownerCt.onSubmit();
			 		 		
                 		}  
			 			
			 }}}, {fieldLabel:"密 码", name:"password", inputType:"password",enableKeyEvents:true,
			 	id:'_loginWinPwd',
			 	listeners:{
			 		"keyup":function(obj,e){
			 		 	if (e.getKey() == e.ENTER && this.getValue().length > 0) {  
			 		 		this.ownerCt.onSubmit();
                 		}  
			 			
			 	}}
			 }, 
		 	{fieldLabel:'意见',xtype:'textarea', name:'opinion'}],
		onSubmit: function(status) {
			if (this.form.isValid) {
				this.form.doAction('submit', {
					waitMsg:'验证中...',
					url:cfg.url,
					method:'post',
					params:{status:status},
					success: function(form, action) {
						if(typeof this.ownerCt.callbackFun === 'function' ){
					 		this.ownerCt.callbackFun(userInfo);
					 	}
					 	info("提示", "签名完成");
					 	this.form.reset();
					},
					failure: function (form, action) {
						error("警告", action.result.data.msg);
					}
				});
			}
		},
		scope:this
	});
	this.buttons = [{text:'通过', handler: function() {
		this.form.onSubmit('yes');
	}}, {text:'驳回', handler: function() {
		this.form.onSubmit('no');
	}}, {text:'取消', handler: function() {
		this.ownerCt.hide();
	}}];
	Est.ux.AuditWindow.superclass.constructor.call(this, cfg);
}

Ext.extends(Est.ux.AuditWindow, Ext.Window, {
	show: function(param) {
		Est.ux.LoginWin.superclass.show.call(this, param);
	}
});

Ext.reg('estauditwin', Est.ux.AuditWindow);