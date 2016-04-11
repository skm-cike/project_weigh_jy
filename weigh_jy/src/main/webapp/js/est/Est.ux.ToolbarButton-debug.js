Ext.namespace('Est.ux');
/**
 * Est.ux.ToolbarButton Extension Class xtype is estpanel
 * 
 * @author Smile_BuG
 * @version 1.0
 * 
 * @class Est.ux.ToolbarButton
 * @extends Ext.Toolbar.Button
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 * @cfg {Boolean} isconfirm
 * 
 */
Est.ux.ToolbarButton = function(cfg) {

	this.addEvents('beforeclick');
	Est.ux.ToolbarButton.superclass.constructor.call(this, cfg);

	if (cfg.isconfirm) {
		this.on( {
			beforeclick : {
				scope : this,
				fn : function(e) {
					return this.chkclk(e, cfg.isconfirm);
				}
			}
		});
	}
};

Ext.extend(Est.ux.ToolbarButton, Ext.Toolbar.Button, {
	onClick : function(e) {
		var evt = {};
		for(var k in e) {
			evt[k] = e[k];
		};
		var obj = {};
		for(var k in this) {
			obj[k] = this[k];
		};
		if (this.fireEvent("beforeclick", this, e) === false) {
			/*Ext.MessageBox.confirm('Confirm',
					'Are you sure you want to do that?', function(btn) {
						if (btn == 'yes') {
							try{*/
								confirm("确定此操作?")?Est.ux.ToolbarButton.superclass.onClick.call(obj, evt):'';
							/*}catch(event){
								alert(event.message);
							}
						}
					},this);*/
		} else {
			Est.ux.ToolbarButton.superclass.onClick.call(this, e);
		};
		
		/*if(this.fireEvent("beforeclick", this, e) === true){
		    return false;
		};
		Est.ux.ToolbarButton.superclass.onClick.call(this, e);*/

	},
	chkclk : function(e, confirm) {
		console.log('beforeclick');
		return !confirm;
	}
});

Ext.reg('esttbbtn', Est.ux.ToolbarButton);
