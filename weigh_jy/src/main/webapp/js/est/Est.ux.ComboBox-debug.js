
Ext.namespace('Est.ux');
/**
 * Est.ux.Layout Extension Class
 * 
 * @author Smile_BuG
 * @version 1.0
 * 
 * @class Est.ux.ComboBox
 * @extends Ext.Panel
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 *            
 * @cfg storeurl {String}
 * @cfg elms {Object}
 * 
 */
Est.ux.ComboBox = function(cfg) {
	
    this.typeAhead= true;
    this.mode= 'local';
    this.forceSelection= true;
    this.triggerAction= 'all';
    this.emptyText='请选择...';
    this.selectOnFocus=true;
    this.width = 180;
    if(cfg.readOnly=='undefined'){
    	this.readOnly = true;
    }
    this.store = '';
    this.allowBlank = true;
    
    this.allowedempty = cfg.allowedempty?true:false;
    /*this.lazyInit = false; */
    
    this._preInit(cfg);

	Est.ux.ComboBox.superclass.constructor.call(this, cfg);
	
}; 

Ext.extend(Est.ux.ComboBox, Ext.form.ComboBox, {
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
	
	getValue: function(){
		var value = Est.ux.ComboBox.superclass.getValue.call(this);
		return value === '[空]'?'':value;
	}
	
	
	/*
	initComponent: function(){
        Est.ux.ComboBox.superclass.initComponent.call(this);
        this.addEvents('clear');
        
        this.triggerConfig = {
            tag:'span',
            cls:'x-form-twin-triggers',
            style:'padding-right:2px',
            cn:[
                {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger"},
                {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger x-form-clear-trigger"}
            ]
        };
    },

    getTrigger: function(index){
        return this.triggers[index];
    },

    initTrigger: function(){
        var ts = this.trigger.select('.x-form-trigger', true);
        this.wrap.setStyle('overflow', 'hidden');
        var triggerField = this;
        ts.each(function(t, all, index){
            t.hide = function(){
                var w = triggerField.wrap.getWidth();
                this.dom.style.display = 'none';
                triggerField.el.setWidth(w-triggerField.trigger.getWidth());
            };
            t.show = function(){
                var w = triggerField.wrap.getWidth();
                this.dom.style.display = '';
                triggerField.el.setWidth(w-triggerField.trigger.getWidth());
            };
            var triggerIndex = 'Trigger'+(index+1);

            if(this['hide'+triggerIndex]){
                t.dom.style.display = 'none';
            }
            t.on("click", this['on'+triggerIndex+'Click'], this, {preventDefault:true});
            t.addClassOnOver('x-form-trigger-over');
            t.addClassOnClick('x-form-trigger-click');
        }, this);
        this.triggers = ts.elements;
    },

    onTrigger1Click: function() {this.onTriggerClick()},
    onTrigger2Click: function() {this.clearValue(); this.fireEvent('clear', this)}
	
	*/
	
}); 

Ext.reg('estcombos', Est.ux.ComboBox);
