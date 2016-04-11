
Ext.namespace('Est.ux');
/**
 * Est.ux.Panel Extension Class
 * xtype is estpanel
 * 
 * @author Smile_BuG
 * @version 1.0
 * 
 * @class Est.ux.Panel
 * @extends Ext.Panel
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 */
Est.ux.Panel = function(cfg) {
	
	this.autoScroll = true;
	
	Est.ux.Panel.superclass.constructor.call(this, cfg);
	
}; 

Ext.extend(Est.ux.Panel, Ext.Panel, {

}); 

Ext.reg('estpanel', Est.ux.Panel);
