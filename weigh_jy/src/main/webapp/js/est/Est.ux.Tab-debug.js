
Ext.namespace('Est.ux');
/**
 * Est.ux.Tab Extension Class
 * 
 * @author Smile_BuG
 * @version 1.0
 * 
 * @class Est.ux.Tab
 * @extends Ext.TabPanel
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 */
Est.ux.Tab = function(cfg) {
	
	this.border = false;
	this.activeTab = 0;
	this.autoScroll = true;
	this.deferredRender = true; //true Lazy load Tab
	this.layoutOnTabChange = true; //fix Lazy load tab render problem
	
	Est.ux.Tab.superclass.constructor.call(this, cfg);
	
}; 

Ext.extend(Est.ux.Tab, Ext.TabPanel, {

}); 

Ext.reg('esttab', Est.ux.Tab);
