
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
Est.ux.FileUploadWin = function (cfg) {
  
    // private
	this.width = 400;
	this.height = 180;
	this.layout = "form";
	this.frame = true;
	this.plain = true;
	this.buttonAlign = "right";
	this.closeAction = "hide";
	this.resizable = false,
	this.id = "_fileUploadWin";
	this.title = "选择文件";
	this.iconCls = 'page_attach';
	this.callbackFun = cfg.callbackFun;
	
	this.fileUpload =  true;
	
	this.fileno = cfg.fileno;
	
	
	
	this.form = new Ext.FormPanel({
		 id:"_fileUploadForm",
		 fileUpload : true,
		 labelWidth : 70,
		 baseCls : "x-plain",
		 bodyStyle : "padding:20px 5px 5px 20px;",
		 defaults : {xtype:"textfield", width:"260", allowBlank:false,labelWidth:200},
		 items:[
			 {	
			 	fieldLabel:"文件名",
			 	name:"filename",
			 	id:'_fileUploadName'
			 },
			 {fieldLabel:"上传文件", name:"file", cls:'file_field',inputType:"file"}
		 ],
		 onSubmit: function(){
			if(this.form.isValid()){
				var _fileno=this.ownerCt.fileno;
				if(!_fileno){
					_fileno=null;
				}
				this.form.doAction("submit",{
					waitMsg:"上传中，请稍候...",
					url: "est/sysinit/sysfilemanage/File/fileUpload",
					params:{
						"_r":new Date(),
						"fileno":Ext.decode(_fileno),
						"moduleId":_moduleId
					},
					success:function (form, action) {
					 	/*
					 	var userInfo = action.result.data;
					 	var feedbackForm = Ext.getCmp(this.ownerCt.feedbackFormId).form;
					 	for(var key in this.ownerCt.feedbackFields) {
					 		var value = eval("(userInfo."+key+")");
					 		feedbackForm.findField(this.ownerCt.feedbackFields[key]).setValue(value);
					 	}
					 	*/
					 	console.log(form)
					 	console.log(action)
					 	var data = Ext.decode(action.response.responseText);
					 	if(data.success){
						 	if(typeof this.ownerCt.callbackFun === 'function' ){
						 		this.ownerCt.callbackFun(data.fileno);
						 	}
						 	this.form.reset();
						 	this.ownerCt.hide();
						 	
					 	}
					},
					failure:function () {
						msg("错误", "上传错误!");
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
		{text:"上传", type:"submit",handler:function () {
			this.form.onSubmit();
		
		},scope:this},
		{ text:"关闭", 
			handler:function () {
				this.form.form.reset();
				this.hide();
			},scope:this
		}		
	];			 
	Est.ux.FileUploadWin.superclass.constructor.call(this, cfg);
};

Ext.extend(Est.ux.FileUploadWin, Ext.Window, {
	show: function(param){
		Est.ux.SignWin.superclass.show.call(this, param);
		Ext.getCmp('_fileUploadName').focus(true,true);
	}
});
Ext.reg("extfileuploadwin", Est.ux.FileUploadWin);

