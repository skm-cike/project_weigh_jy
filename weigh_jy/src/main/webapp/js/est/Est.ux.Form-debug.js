
Ext.namespace('Est.ux');
/**
 * Est.ux.Form Extension Class xtype is estform
 * 
 * @author Smile_BuG
 * @version 1.0
 * 
 * @class Est.ux.Form
 * @extends Ext.form.FormPanel
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 * @cfg {String} formurl form action url
 * @cfg {Object} colstype form's elements require for example: [{id:'userId',
 *      fieldset:'Login', items:[{fieldLabel:'UserName',name:'name',allowBlank:
 *      false}]}]
 * 
 * @cfg {Number} colnum how many col per row, default 3
 * @cfg {String} method default method's name
 * @cfg {String} getmethod load method's name
 * @cfg {String} delmethod delete method's name
 * @cfg {String} savmethod save method's name
 * 
 */
Est.ux.Form = function(cfg) {
	
	/* init default cfg*/
	this.frame = true;
	this.autoHeight = true;
	this.labelWidth = 75;
	/* this.labelAlign = 'top';*/
	this.autoScroll = true;
	this.collapsible = true;
	this.titleCollapse = true;
	
	
	
	this.items = [];
	/*form method name*/
	this.method = "";
	this.delmethod = "";
	this.savmethod = "";
	this.getmethod = "";
	
	
	this.refobjs = [];
	
	this.idfield = cfg.colstype[0]?cfg.colstype[0].id:undefined;
	
	/*form load jsonData map*/
	this.renderMap = [];
	this.formurl = cfg.formurl = /.*\/$/.test(cfg.formurl)?cfg.formurl:cfg.formurl+'/';

	this.colnum = cfg.colnum?cfg.colnum:3;
	
	this._preInit(cfg);
	
	
	Est.ux.Form.superclass.constructor.call(this, cfg);
	
}; 

Ext.extend(Est.ux.Form, Ext.FormPanel, {
	
	_preInit: function(cfg) {
		this._initElem(cfg.colstype);
		this._initMethod(cfg);
/* this._initBar(cfg.barstype); */
	},
	
	/* init method name*/
	_initMethod: function(cfg) {
		!cfg.getmethod?this.getmethod='get'+cfg.method:this.getmethod=cfg.getmethod;
		!cfg.savmethod?this.savmethod='sav'+cfg.method:this.savmethod=cfg.savmethod;
		!cfg.delmethod?this.delmethod='del'+cfg.method:this.delmethod=cfg.delmethod;
	},
	
/*	_initBar: function(barstype) {
		var tbar = barstype.tbar;
		var bbar = barstype.bbar;
		this.tbar = {xtype: 'toolbar', items: []}
		
		for(var i=0; i<tbar.length; i++) {
			tbar.items[i] = this._analBarstype();
		};
		
		build buttom bar
		for(var j=0; j<bbar.length; j++){
			
		};
		
	},
	_analBarstype: function(bartype) {
		var bar = {};
		bar.text = bartype.text;
		bartype.pressed?bar.pressed = bartype.pressed:void(0);
		bartype.enableToggle?bar.enableToggle = bartype.enableToggle:void(0);
		bartype.tooltip?bar.tooltip=bartype.tooltip: void(0);
        bar.iconCls: 'preview',
		return bar;
	},*/
	
	
	_initElem: function(colstype) {
		/*var colwidth = Math.floor(10/this.colnum)/10;*/
		
		for(var k=0; k<colstype.length; k+=1) {
			//var colnum = colstype[k].colnum?colstype[k].colnum:3; //sufei 注释(这个原来的不能动态设置form 显示的列数 此处写成了固定的colnum为3了)
			  var colnum = colstype[k].colnum==undefined?this.colnum:colstype[k].colnum; //sufei 修改（目的是:满足动态设置form 显示的列数） 
			/*fieldset var*/
			var fieldsetItems = { xtype: 'fieldset',
								title: colstype[k].fieldset, 
								autoHeight: true, 
								border: 'none',
								/*layout:'column', */
								layout: 'table',
								defaults: {
									layout: 'form',
									style: 'padding-left: 8px;',
									defaultType: 'textfield',
									
									border: false
								},
								layoutConfig: {
									columns: colnum
								},
								items:[]};
			/* build id */
			if(colstype[k].id){this.items[0]={xtype:'hidden', name:colstype[k].id}};
			
			
//			var colwidth = Math.floor(10/colnum)/10;
			
			/* build fieldset content */
//			for(var j=0; j<colnum; j++){
//				var colItems = {columnWidth:colwidth, layout: 'form', border: false,defaultType: 'textfield', items: []};
//				
//				for(var i=j; i<colstype[k].items.length; i+=colnum){
//					
//					var col = {};
//					/* convertColStype(col,colstype[k].items[i]); */
//					var coltype = colstype[k].items[i];
//					col = coltype;
//					/*coltype.xtype?void(0):col.xtype='textfield';*/
//					
//					/*render jsonData*/
//					var maplen = this.renderMap.length;
//					var omap = {name:coltype.name,mapping: coltype.name};
//					this.renderMap[maplen]=omap;
//					colItems.items[Math.floor(i/colnum)] = col;
//				}
//				
//				
//				if(colItems.items.length!=0){
//					fieldsetItems.items[j] = colItems;
//				}
//				
//			}
			for(var i=0; i<colstype[k].items.length; i+=1){
				fieldsetItems.items[i] = {};
                fieldsetItems.items[i].width = colstype[k].items[i].columnWidth;
                fieldsetItems.items[i]['defaults'] = {width:150};
				fieldsetItems.items[i].items = colstype[k].items[i];
				if(colstype[k].items[i].colspan) {
					fieldsetItems.items[i].colspan = colstype[k].items[i].colspan;
				}
				if(colstype[k].items[i].rowspan) {
					fieldsetItems.items[i].rowspan = colstype[k].items[i].rowspan;
				}
			}
			console.log(fieldsetItems);
			this.items[this.items.length] = fieldsetItems;
			
		}
	},
	createForm: function(){
        delete this.initialConfig.listeners;
        return new Est.ux.BasicForm(null, this.initialConfig);
    },
	
	/*fill form with jsonData*/
	reader : new Ext.data.JsonReader({
		successProperty: 'success',
        record : 'data'           
    }, this.renderMap),
    
    doLoad : function(condition,callbackFn) {
		if(condition!=null) {
			condition._r=new Date().getTime();
		} else {
			condition = {'_r': new Date().getTime()};
		}
		console.log(this.formurl);
		this.load({url: this.formurl+this.getmethod,method:'POST',params:condition,
			success: function(form,action){
				if(callbackFn){
					callbackFn(form,action);
				}
				//info('提示','载入成功。');
			},
			failure: function(){
				error('错误','载入失败。');
			}
		});
	},
	
	doSumbit: function(cfg) {
		var obj = this;
		var refobjs = this.refobjs;
		
		var params = {};
		if (cfg) {
			if (cfg.params) {
				params = cfg.params;
			}
		}
		
		var url = cfg&&cfg.url  ?  cfg.url : (this.formurl+this.savmethod);
		if(this.form.isValid()){
			this.form.submit({
				url: url, 
				method:'POST', 
				waitMsg: '保存中...',
				params:params,
				success: function(form, rep){
					console.log(rep.result.data);
					obj.form.setValues(rep.result.data);
					for(var i=0; i<refobjs.length; i+=1) {
						if(refobjs[i].getXType()=='estgrid') {
							refobjs[i].getBottomToolbar().changePage(1);
							refobjs[i].getSelectionModel().clearSelections();
						}
					}
					
					if(rep.result.info) {
						info('提示',rep.result.info);
					} else {
						if(cfg && cfg.successInfo) {
							info('提示',cfg.successInfo);
						}else {
							info('提示','保存成功。');
						}
					}
					
					if(cfg && cfg.success){
						cfg.success(form, rep);
					}
					
				},
				failure: function(form,action){
		                if(action.result && action.result.error){
		                        error('错误',action.result.error);
		                }else{
		                	if(cfg && cfg.failureInfo) {
								error('提示',cfg.failureInfo);
							}else {
								error('错误','保存失败。');
							}
		                }
                }
			});
		}
	},
	
	doDelete: function(cfg) {
		var obj =  this;
		if(this.idfield){
			if(!this.form.findField(this.idfield).getValue()) {
				error('错误','请选择一条删除的记录。');
				return;
			}
		}
		this.form.submit({url: this.formurl+this.delmethod, method:'GET', waitMsg: '刪除中...',
			success: function(form, rep) {
				obj.doReset();
				info('提示','删除成功。');
				
				if(cfg && cfg.success){
						cfg.success(form, rep);
					}
				
			},
			failure: function(form,action){
                if(action.result && action.result.error){
                        error('错误',action.result.error);
                }else{
                        error('错误','删除失败。');
                }
			}
		});
	},
	
	doReset: function() {
		for(var i=0; i<this.refobjs.length; i+=1) {
			if(this.refobjs[i].getXType()=='estgrid') {
				this.refobjs[i].getBottomToolbar().changePage(1);
				this.refobjs[i].getSelectionModel().clearSelections();
			}
		}
		this.form.reset();
	},
	
	addRefObjs: function(objs) {
		if(typeof objs==='array'){
			for(var i = 0; i<objs.length; i+=1) {
				this.refobjs[this.refobjs.length] = objs[i];
			}
		}else {
			this.refobjs[this.refobjs.length] = objs;
		}
	},
	
	hideField: function(fieldname){
		var field = this.form.findField(fieldname);
		if(field !== null){
			field.getEl().up('.x-form-item').setDisplayed(false);
		} else {
			(function(){field.getEl().up('.x-form-item').setDisplayed(false);}).defer(500);
		}
	},
	
	showField: function(fieldname){
		var field = this.form.findField(fieldname);
		if(field !== null){
			field.getEl().up('.x-form-item').setDisplayed(true);
		} else {
			(function(){field.getEl().up('.x-form-item').setDisplayed(true);}).defer(500);
		}
	},
    
    setFieldsetName : function(oldname,newname) {
    	var list = this.el.query("*[class=x-fieldset-header-text]");
    	for (var i=0 ; i<list.length ;i+=1) {
    		var fieldset = list[i];
    		if(fieldset.innerHTML===oldname){
    			fieldset.innerHTML = newname;
    		}
    	}
    },
    
    setFieldsetReadOnly:function(readOnlyFields,writeAbleFields){
    	if(readOnlyFields){
    		for(var i=0; i<readOnlyFields.length; i+=1){
	    		var field=this.getForm().findField(readOnlyFields[i]);
	    		if(field){
	    			field.getEl().dom.readOnly=true;
	    			if(field.trigger){
	    				//alert("readOnly="+"filed="+readOnlyFields[i]+"\n"+"xtype="+field.xtype+"\n"+"trigger="+field.trigger);
	    				field.trigger.un('click',field.onTriggerClick,field);
	    			}
	    		}
	    		else{break;}
    		}
    	}
    	if(writeAbleFields){
    		for(var i=0; i<writeAbleFields.length; i+=1){
	    		var field=this.getForm().findField(writeAbleFields[i]);
	    		if(field){
	    			//alert("filed="+writeAbleFields[i]+"\n"+"xtype="+field.xtype+"\n"+"trigger="+field.trigger);
	    			if(field.xtype==='textfield' || field.xtype===undefined || field.xtype==='textarea' || field.xtype==='numberfield'){
	    				field.getEl().dom.readOnly=false;
	    			}
					if(field.trigger){
						//alert("writeAble="+"filed="+writeAbleFields[i]+"\n"+"xtype="+field.xtype+"\n"+"trigger="+field.trigger);
	    				field.trigger.un('click',field.onTriggerClick,field);
	    				field.trigger.on('click',field.onTriggerClick,field);
	    			}
	    		}
	    		else{break;}
    		}
    	}
    	
    },
    
    showFieldsetByIndex : function(showableJson) {
    	var self = this;
    	(function(){
    		var fieldset=self.el.query("fieldset");
    		var show=showableJson.show;
    			for(var i=0;i<show.length;i+=1){
    				fieldset[show[i]].style.display="block";
    			}
    		var hide=showableJson.hide;
				for(var i=0;i<hide.length;i+=1){
    				fieldset[hide[i]].style.display="none";
    			}    		
    		
    	}).defer(500);
    	
    	
    	this.doLayout();
    },
    
    
    showFieldset : function(name,showable) {
    	var list = this.el.query("*[class=x-fieldset]");
    	for (var i=0 ; i<list.length ;i+=1) {
    		var fieldset = list[i];
    		if(fieldset.innerHTML===oldname){
    			(function(){fieldset.setDisplayed(showable);}).defer(500);
    		}
    	}
    },
    
    setFieldsetNameByIndex : function(index,newname) {
    	var list = this.el.query("*[class=x-fieldset-header-text]")[index].innerHTML = newname;
    },
    
	setLabel:function(fieldname,labelname){
		this.form.findField(fieldname).ownerCt.el.query('*[class=x-form-item-label]')[0].innerHTML = labelname + ":";
	}
}); 

Ext.reg('estform', Est.ux.Form);
