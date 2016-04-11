
Ext.namespace('Est.ux');
/**
 * Est.ux.Layout Extension Class
 * 
 * @author jingpj
 * @version 1.0
 * 
 * @class Est.ux.ComboBox
 * @extends Ext.Panel
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 *            
 * @cfg propertycode {String}
 * @cfg elms {Object}
 * 
 */
Est.ux.PropertyComboBox = function(cfg) {
	
	
    this.propertycode = cfg.propertycode;
    //this.valueField = 'propertyname';
    //this.displayField = 'propertyname';
    this.valueField = cfg.valueField;
    this.displayField = cfg.displayField;
    this.name=cfg.hiddenName;
    this.allowedempty = cfg.allowedempty?true:false;
    /*this.lazyInit = false; */
    
    this._preInit(cfg);
	Est.ux.PropertyComboBox.superclass.constructor.call(this, cfg);
	
}; 

Ext.extend(Est.ux.PropertyComboBox,Est.ux.ComboBox, {
	_initStore: function(url, elms, cfg) {
		//var val = 'propertyid';
		//var dis = 'propertyname';
		if(!this.valueField){
			this.valueField='propertycode';
		}
		if(!this.displayField){
			this.displayField='propertyname';
		}
		var val = this.valueField;
		var dis = this.displayField;
		var self = this;
		this.store = new Ext.data.Store({
			/*autoLoad: true,*/
			url: 'est/sysinit/sysproperty/SysProperty/getPropertiesByCode',
		//	baseParams:{'propertycode':self.propertycode},
			reader: new Ext.data.JsonReader({
				root: 'rows',
				fields: [val,dis]
			})
		})
		if(this.allowedempty) {
			this.store.on({'load':
				function(store) {
					var blankrec = new Ext.data.Record(eval('({'+val+':"",'+dis+':"[空]"})'));
					
					store.insert(0,blankrec);
				}
			});
		}
	//	this.store.load();
		this.store.load({params:{'propertycode':self.propertycode}});
	},
	
	resetProp :function(propertycode,isReset) {
		this.store.load({params:{'propertycode':propertycode}});
		!isReset || this.reset();
	},
	
    onSelect : function(record, index){
        if(this.fireEvent('beforeselect', this, record, index) !== false){
            this.setValue(record.data[this.valueField || this.displayField]);
            this.collapse();
            if(record.data[this.displayField]!=='[空]') {
            	this.fireEvent('select', this, record, index);
            }else{
            	this.reset();
            }
        }
      }
	}); 

Ext.reg('estpropcombos', Est.ux.PropertyComboBox);
