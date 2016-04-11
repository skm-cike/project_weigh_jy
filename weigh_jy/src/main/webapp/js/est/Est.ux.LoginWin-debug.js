
Ext.namespace("Est.ux");
/**
 * Est.ux.LoginWin Extension Class
 * xtype is extsignWin
 * 
 * @author hebo
 * @modified by jingpj 2009.7.7
 * @version 2.0
 * 
 * @class Est.ux.LoginWin
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
        win=new Est.ux.LoginWin({
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
Est.ux.LoginWin = function (cfg) {
  
    
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
	this.title="请重新登录：";
	this.callbackFun = cfg.callbackFun;
	this.login = cfg.login;
	
	this.form = new Ext.FormPanel({
		 id:"signWinForm",
		 labelWidth : 50,
		 baseCls : "x-plain",
		 bodyStyle : "padding:20px 5px 5px 20px;",
		 defaults : {xtype:"textfield", width:"160", allowBlank:false},
		 items:[
		 	 {xtype:'label',text:'您的登录已超时断开，请重新登录！',style:'color:red',anchor:'90%'},
		 	 {xtype:'label',text:'',anchor:'90%'},
			 {fieldLabel:"用 户",name:"login",value:cfg.login || "",enableKeyEvents:true,
			 	allowBlank:false,
			 	readOnly:true,
			 	listeners:{
			 		"keyup":function(obj,e){
			 		 	if (e.getKey() == e.ENTER && this.getValue().length > 0) {  
			 		 		this.ownerCt.onSubmit();
			 		 		
                 		}  
			 			
			 }}},
			 {fieldLabel:"密 码", name:"password", inputType:"password",enableKeyEvents:true,
			 	id:'_loginWinPwd',
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
					url: "est/sysinit/sysuser/SysUser/vefUser",
					params:{"_r":new Date()},
					method:"post",
					success:function (form, action) {
					 	//alert(this.feedbackFields);
					 	var userInfo = action.result.data;
					 	
					 	
					 	if(typeof this.ownerCt.callbackFun === 'function' ){
					 		this.ownerCt.callbackFun(userInfo);
					 	}
					 	this.form.reset();
					 	this.ownerCt.hide();
					 	info("欢迎","重新登录成功！");
					},
					failure:function () {
						error("错误", "用户名或者密码错误!");
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
		},scope:this},
		{ text:"关闭", 
			handler:function () {
				this.form.form.reset();
				this.hide();
			},scope:this
		}		
	];
	Est.ux.LoginWin.superclass.constructor.call(this, cfg);
};

Ext.extend(Est.ux.LoginWin, Ext.Window, {
	show: function(param){
		Est.ux.LoginWin.superclass.show.call(this, param);
		this.form.form.findField('login').setValue(this.login);
		//this.setActive();
		Ext.getCmp('_loginWinPwd').focus(true,true);
	}
});
Ext.reg("extloginWin", Est.ux.LoginWin);

