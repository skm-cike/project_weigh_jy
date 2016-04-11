
Ext.namespace('Est.ux');
/**
 * Est.ux.SearchButton Extension Class
 * 
 * @author Smile_BuG
 * @version 1.0
 * 
 * @class Est.ux.SearchButton
 * @extends Ext.Toolbar.Button
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 * @cfg {String} spId Search Panel's Id require
 * @cfg {String} pId  Search Panel's parent's Id require
 * 
 */
Est.ux.SearchButton = function(cfg) {
	
	this.spId = cfg.spId;
	this.pId = cfg.pId;
	this.enableToggle = true;
	Est.ux.SearchButton.superclass.constructor.call(this, cfg);
	
	this.on({
      click:{scope:this, fn:function() {
         this.toggleClk();
      }}
  });
	
}; 

Ext.extend(Est.ux.SearchButton, Ext.Toolbar.Button, {
	
	toggleClk: function() {
		var searchPanel = Ext.getCmp(this.spId);
		var containerPanel = Ext.getCmp(this.pId);
		if(searchPanel.hidden){
			searchPanel.show();
		} else {
			searchPanel.hide();
		}
		containerPanel.doLayout();
	}
	
}); 

Ext.reg('estsearchbtn', Est.ux.SearchButton);
