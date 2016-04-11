
Ext.namespace('Est.ux');
/**
 * Est.ux.Layout Extension Class
 * 
 * @author Smile_BuG
 * @version 1.0
 * 
 * @class Est.ux.Layout
 * @extends Ext.Panel
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 */
Est.ux.Layout = function(cfg) {
	this.region = 'fit';
	this.layout = 'border';
	this.autoScroll = false;
	Est.ux.Layout.superclass.constructor.call(this, cfg);
	
}; 

Ext.extend(Est.ux.Layout, Ext.Panel, {
	

}); 

Ext.reg('estlayout', Est.ux.Layout);
