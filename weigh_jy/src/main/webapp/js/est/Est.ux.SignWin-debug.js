
Ext.namespace("Est.ux");
/**
 * Est.ux.SignWin Extension Class
 * xtype is extsignWin
 * 
 * @author hebo
 * @modified by jingpj 2009.7.7
 * @version 2.0
 * 
 * @class Est.ux.SignWin
 * @extends Ext.Window
 * 
 * 
 * @cfg {String} id,formId, require
 * @cfg {Array[]} getFields,inputFields require
 * 
 * for example:
 * var toby = Ext.get('toby');//弹出窗口的位置
 * var win;
       if(!win){
        win=new Est.ux.SignWin({
          feedbackFormId:'checkform',
          feedbackFields : {'username':'checkby','userid':'checkbyID'},
          login: 'admin',
          title: '登录'
        });
        }
    win.show(toby)
 * 
 * 
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 */
Est.ux.SignWin = function (cfg) {
  
    //public
    /** 验证成功后回填数据Form的id*/
    this.feedbackFormId = cfg.feedbackFormId; 
    /** 回填数据Form的字段列表，格式为[{"vo字段名1":"form字段名1"}，{"vo字段名2":"form字段名2"}....]*/
	this.feedbackFields = cfg.feedbackFields;
	/** 签字框标题,默认为"签名"*/
	this.title = cfg.title || "签名";
	/** 默认（当前）用户登录名*/
	this.login = cfg.login;
    
    // private
	this.width = 300;
	this.height = 180;
	this.layout = "form";
	this.frame = true;
	this.plain = true;
	this.buttonAlign = "right";
	this.closeAction = "hide";
	this.resizable = false,
	this.id = cfg.id;
	this.callbackFun = cfg.callbackFun;
	
	this.form = new Ext.FormPanel({
		 id:"signWinForm",
		 labelWidth : 50,
		 baseCls : "x-plain",
		 bodyStyle : "padding:20px 5px 5px 20px;",
		 defaults : {xtype:"textfield", width:"160", allowBlank:false},
		 items:[
			 {fieldLabel:"用 户",name:"login",value:cfg.login || "",enableKeyEvents:true,
			 	id:'_signWinlogin',
			 	listeners:{
			 		"keyup":function(obj,e){
			 		 	if (e.getKey() == e.ENTER && this.getValue().length > 0) {  
			 		 		this.ownerCt.onSubmit();
			 		 		
                 		}  
			 			
			 }}},
			 {fieldLabel:"密 码", name:"password", inputType:"password",enableKeyEvents:true,
			 	listeners:{
			 		"keyup":function(obj,e){
			 		 	if (e.getKey() == e.ENTER && this.getValue().length > 0) {  
			 		 		this.ownerCt.onSubmit();
                 		}  
			 			
			 	}}
			 }
		 ],
		 onSubmit: function(){
			if(this.form.isValid()){
				this.form.doAction("submit",{
					waitMsg:"验证中...",
					url: "est/common/business/SysUser/verfyUser",
					params:{"_r":new Date()},
					method:"post",
					success:function (form, action) {
					 	//alert(this.feedbackFields);
					 	var userInfo = action.result.data;
					 	
					 	if(this.ownerCt.feedbackFormId){
					 		
					 		var feedbackForm = Ext.getCmp(this.ownerCt.feedbackFormId).form;
						 	for(var key in this.ownerCt.feedbackFields) {
						 		var value = eval("(userInfo."+key+")");
						 		feedbackForm.findField(this.ownerCt.feedbackFields[key]).setValue(value);
						 		//feedbackForm.findField(this.ownerCt.feedbackFields[key]).setValue(userInfo[key]);
						 	}
					 	
					 	}
					 	
					 	if(typeof this.ownerCt.callbackFun === 'function' ){
					 		this.ownerCt.callbackFun(userInfo);
					 	}
					 	this.form.reset();
					 	this.ownerCt.hide();
					},
					failure:function () {
						Ext.Msg.alert("错误", "用户名或者密码错误!");
					},
					scope:this
				});
			}


		}
	
	});
	
	var getFields = cfg.getFields;
	var inputFields = cfg.inputFields;
	
	this.items = [this.form];
				 
	this.buttons = [
		{text:"确定", type:"submit",handler:function () {
			this.form.onSubmit();
			/*
			if(this.form.form.isValid()){
				this.form.form.doAction("submit",{
					waitMsg:"验证中...",
					url: "est/common/business/SysUser/verfyUser",
					params:{"_r":new Date()},
					method:"post",
					success:function (form, action) {
					 	//alert(this.feedbackFields);
					 	var userInfo = action.result.data;
					 	var feedbackForm = Ext.getCmp(this.feedbackFormId).form;
					 	for(var key in this.feedbackFields) {
					 		feedbackForm.findField(this.feedbackFields[key]).setValue(userInfo[key]);
					 	}
					 	this.form.form.reset();
					 	this.hide();
					},
					failure:function () {
						Ext.Msg.alert("错误", "用户名或者密码错误!");
					},
					scope:this
				});
			}
			*/
			
		
		},scope:this},
		{ text:"关闭", 
			handler:function () {
				this.form.form.reset();
				this.hide();
			},scope:this
		}		
	];			 
	Est.ux.SignWin.superclass.constructor.call(this, cfg);
};

Ext.extend(Est.ux.SignWin, Ext.Window, {
	show: function(param){
		Est.ux.SignWin.superclass.show.call(this, param);
		this.form.form.findField('login').setValue(this.login);
		//this.setActive();
		Ext.getCmp('_signWinlogin').focus(true,true);
	}
});
Ext.reg("extsignWin", Est.ux.SignWin);

