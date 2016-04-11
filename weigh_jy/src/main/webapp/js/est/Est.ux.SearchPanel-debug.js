Ext.namespace('Est.ux');
/**
 * Est.ux.SearchPanel Extension Class xtype is estpanel
 * 
 * @author Smile_BuG
 * @version 1.0
 * 
 * @class Est.ux.SearchPanel
 * @extends Ext.Panel
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 * @cfg {Array} formitems
 * 
 */
Est.ux.SearchPanel = function(cfg) {

	this.autoScroll = true;
	this.hidden = true;
	this._preInit(cfg);
	this.formId = cfg.id+'form';
	
	this.refobjs = [];
	/* this.baseCls = 'x-plain'; */
	Est.ux.Panel.superclass.constructor.call(this, cfg);
	

	this.addEvents('dosearch');

};

Ext.extend(Est.ux.SearchPanel, Ext.Panel, {
	initEvents : function(){
    	Ext.TabPanel.superclass.initEvents.call(this);
		this.on('dosearch',this.doSearch,this);
	},
	_preInit : function(cfg) {
		var items = cfg.formitems;
		var formId = cfg.id + 'form';
		console.log(formId);
		var panel = this;
		this.searchform = new Est.ux.Form( {
			colstype : items,
			id : formId,
			colnum : 3,
			layout:'fit',
			buttons : [
					{
						text : '查询',
						handler : function() {
							panel.fireEvent("dosearch", panel)
						}
					}, {
						text : '重置',
						handler : function() {
							Ext.getCmp(formId).doReset()
						}
					} ]
		});
		this.items = this.searchform;
	},
	doSearch: function(obj) {
		var vals ;
		if(obj === undefined) {
			var vals = this.searchform.form.getValues();
		} else {
			var vals = Ext.getCmp(obj.id + 'form').form.getValues();
		}
		if(Ext.getCmp('wfStatusquery')){
			vals.wfstatus = Ext.getCmp('wfStatusquery').getValue();		
		}
		for(var i=0; i<this.refobjs.length; i++) {
			if(this.refobjs[i].getXType()=='estgrid' || this.refobjs[i].getXType()=='esteditgrid' || this.refobjs[i].getXType()=='estgroupgrid' || this.refobjs[i].getXType()=='estgroupeditgrid'  || this.refobjs[i].getXType()=='estcheckgrid') {
				this.refobjs[i].doSearch(vals);
			}
		}
	},
	
	addRefObjs: function(objs) {
		if(typeof objs==='array'){
			for(var i = 0; i<objs.length; i++) {
				this.refobjs[this.refobjs.length] = objs[i];
			}
		}else {
			this.refobjs[this.refobjs.length] = objs;
		}
	},
	getForm: function(){
		return Ext.getCmp(this.formId);
	}
});

Ext.reg('estsearchpanel', Est.ux.SearchPanel);
