
Ext.namespace('Est.ux');
/**
 * Est.ux.ChooseField Extension Class
 * 
 * @author jingpj
 * @version 1.0
 * 
 * @class Est.ux.ChooserField
 * @extends Ext.Panel
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 *            
 * @cfg storeurl {String}
 * @cfg elms {Object}
 * 
 */
Est.ux.ChooserField = function(cfg) {
	
	
	this.fieldLabel = cfg.fieldLabel;
    this.allowBlank = cfg.allowBlank?cfg.allowBlank:false;
    this.readOnly = cfg.readOnly?cfg.readOnly:true;
    this.width = cfg.width?cfg.width:180;
	this.triggerClass =  'x-form-search-trigger';
	this.selected = [];
	
	//this.title='请选择';
    //this.width= 500;
    //this.height= 300;
    //this.layout= 'fit';
    this.plain= true;
    this.bodyStyle= 'padding:5px;';
    this.buttonAlign ='center';
    this.formId=cfg.formId;
    var getFields=cfg.getFields;
    var inputFields=cfg.inputFields;
    var id=cfg.id;

	
	this.grid = new Est.ux.CheckSelectGrid({
			id:Ext.id(),
			singleSelect:'false',
			storeurl: cfg.colstype.storeurl,
			colstype: cfg.colstype,
			region: 'center',
			chooseField:this
	});
	
	this.grid.store.reload();
	
	this.chooseWin = new Ext.Window({
		 
		title:cfg.title,
		width:500,
		height:300,
		layout:'fit',
		plain:true,
		closeAction:"hide",
		items:[this.grid],
		buttons:[
			{
				text:'确定',
				handler:function(){
					var data = this.grid.getSelect();
					var cols = cfg.colstype.cols;
					var destForm = Ext.getCmp(cfg.colstype.formid).form;
					for(var i=0;i<cols.length;i++) {
						if(cols[i]['isReturn']){
							var fieldname = cols[i]['dataIndex'];
							var tmpstr = '';
							for(var j=0;j<data.length;j+=1) {
								tmpstr += data[j][fieldname];
								if(j<data.length-1) {
									tmpstr+=',';
								}
							}
							destForm.findField(cols[i]['fieldto']).setValue(tmpstr);
						
						}
					}
					this.chooseWin.hide();
				},
				scope:this
			
			},{
				text:'关闭',
				handler:function(){
					this.chooseWin.hide();
				},
				scope:this
				
			}
		],
		listener:{}
		
	});
	
	
	/*
    this.typeAhead= true;
    this.mode= 'local';
    this.forceSelection= true;
    this.triggerAction= 'all';
    this.emptyText='请选择...';
    this.selectOnFocus=true;
    this.width = 180;
    this.readOnly = true;
    this.store = '';
    this.allowBlank = true;
    
    this.allowedempty = cfg.allowedempty?true:false;
    */
    /*this.lazyInit = false; */
    
    
    //this._preInit(cfg);

	Est.ux.ChooserField.superclass.constructor.call(this, cfg);
	
}; 

Ext.extend(Est.ux.ChooserField, Ext.form.TriggerField, {
	_preInit: function(cfg) {
		this._initStore(cfg.storeurl, cfg.elms, cfg);
	},
	_initStore: function(url, elms, cfg) {
		var val = cfg.valueField;
		var dis = cfg.displayField;
		if(url) {
			this.store = new Ext.data.Store({
				/*autoLoad: true,*/
				url: url,
				reader: new Ext.data.JsonReader({
					root: 'rows',
					fields: [val,dis]
				})
			})
			if(this.allowedempty) {
				this.store.on({'load':
					function(store) {
						console.log('Est.ux.ComboBox=========>insert store:'+ store.getCount());
						var blankrec = new Ext.data.Record(eval('({'+val+':"",'+dis+':"[空]"})'));
						store.insert(0,blankrec);
					}
				});
			}
			this.store.load();
		} else if (elms) {
			this.valueField = 'val';
			this.displayField = 'dis';
			if(this.allowedempty) {
				elms.unshift(['','[空]']);
			}
			this.store = new Ext.data.SimpleStore({
				fields: ['val', 'dis'],
				data: elms
				})
			
		} else {
			console.log('Est.ux.ComboBox=====>initStore Error!!!');
		}
	},
	
	getStore: function() {
		return this.store;
	},
	
	onTriggerClick : function(){
		this.grid.store.reload();
		this.chooseWin.show(Ext.get(this.id));
	}
	
}); 

Ext.reg('estchooser', Est.ux.ChooserField);
