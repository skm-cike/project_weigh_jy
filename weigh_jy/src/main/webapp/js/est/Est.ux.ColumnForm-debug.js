
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
Est.ux.ColumnForm = function(cfg) {
	
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
	

	
	/*form load jsonData map*/
	this.renderMap = [];
	this.formurl = cfg.formurl = /.*\/$/.test(cfg.formurl)?cfg.formurl:cfg.formurl+'/';

	this.colnum = cfg.colnum?cfg.colnum:3;
	
	this._preInit(cfg);
	
	
	Est.ux.Form.superclass.constructor.call(this, cfg);
	
}; 

Ext.extend(Est.ux.ColumnForm, Ext.FormPanel, {
	
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
	
	//初始化form中的元素和布局
	_initElem: function(colstype) {
		var defaultColWidth = Math.floor(10/this.colnum)/10;
		
		
		//遍历fieldset
		for(var k=0; k<colstype.length; k+=1) {
			var colnum = colstype[k].colnum?colstype[k].colnum:3;
			/*fieldset var*/
			var fieldsetItems = { xtype: 'fieldset',
								title: colstype[k].fieldset, 
								autoHeight: true, 
								border: 'none',
								layout:'column',
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
			
			
			
			for(var i=0; i<colstype[k].items.length; i+=1){
				var colwidth = colstype[k].items[i].columnWidth || defaultColWidth;
				var item = colstype[k].items[i];
				item['columnWidth']=undefined;
				var colItems = {columnWidth:colwidth, layout: 'form', border: false,defaultType: 'textfield', items: [item]};
				fieldsetItems.items[i] = colItems; 
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
    
    doLoad : function(condition) {
		if(condition!=null) {
			condition._r=new Date().getTime();
		} else {
			condition = {'_r': new Date().getTime()};
		}
		console.log(this.formurl);
		this.load({url: this.formurl+this.getmethod,method:'POST',params:condition,
			failure: estfailure
		});
	},
	
	doSumbit: function() {
		var obj = this;
		var refobjs = this.refobjs;
		if(this.form.isValid()){
			this.form.submit({url: this.formurl+this.savmethod, method:'POST', waitMsg: '保存中',
				success: function(form, rep){
					console.log(rep.result.data);
					obj.form.setValues(rep.result.data);
					for(var i=0; i<refobjs.length; i++) {
						if(refobjs[i].getXType()=='estgrid') {
							refobjs[i].getBottomToolbar().changePage(1);
							refobjs[i].getSelectionModel().clearSelections();
						}
					}
				},
				failure: estfailure
			});
		}
	},
	
	doDelete: function() {
		var obj =  this;
		this.form.submit({url: this.formurl+this.delmethod, method:'GET', waitMsg: '刪除中',
			success: function() {obj.doReset();},
			failure: estfailure
		});
	},
	
	doReset: function() {
		for(var i=0; i<this.refobjs.length; i++) {
			if(this.refobjs[i].getXType()=='estgrid') {
				this.refobjs[i].getBottomToolbar().changePage(1);
				this.refobjs[i].getSelectionModel().clearSelections();
			}
		}
		this.form.reset();
	},
	
	addRefObjs: function(objs) {
		if(typeof objs==='array'){
			for(var i = 0; i<objs.length; i++) {
				this.refobjs[this.refobjs.length] = objs[i];
			}
		}else {
			this.refobjs[this.refobjs.length] = objs;
		}
	}
    
}); 

Ext.reg('estcolform', Est.ux.ColumnForm);
