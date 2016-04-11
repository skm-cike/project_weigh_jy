Ext.namespace('Est.ux');
/**
 * Est.ux.Grid Extension Class
 * xtype is estgrid
 * 
 * @author Smile_BuG
 * @version 1.0
 * 
 * @class Est.ux.Grid
 * @extends Ext.grid.GridPanel
 * 
 * 
 * @cfg {String} storeurl require
 * @cfg {Object} colstype require
 * for example:
 * {id: 'id', cols: [{dataIndex:'pwd', header: 'Password', width: '30', sortable: false},
 * {dataIndex:'name', header: 'UserName',width: '40', sortable: true}]}
 * 
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 */
Est.ux.GroupGrid = function(cfg) {
	
	Ext.each(cfg.colstype.cols,function(e){e['sortable']=true;}); //设置所有列为可排序
	
	this.store = '';
	this.columns = [];
	this.autoScroll = true;
	
	this.loadMask = true;
	
	/*use to search*/
	this.searchCondition = {};
	
	/*init something*/
	this._preInit(cfg);
	
	this.refobjs=[];
	
	this.viewConfig =  new Ext.grid.GroupingView({
                forceFit:true,
                sortAscText :'正序',
                sortDescText :'倒序',
                columnsText:'列显示/隐藏',
                groupByText:'依本列分组',
                showGroupsText:'分组显示',
                groupTextTpl: '{text} ({[values.rs.length]} 条记录)'
            }),

	
	this.sm = new Ext.grid.RowSelectionModel({singleSelect:true}),
	
	//this.store = this.gridStore,
	
	this.hasPagerBar = typeof cfg.hasPagerBar==='undefined'?true:cfg.hasPagerBar;
	
	//this.store = this.gridStore,
	/*add page bar*/
	if(this.hasPagerBar) {
		this.bbar = new Ext.PagingToolbar({store: this.store,displayInfo:true});
	}
	
	
	Est.ux.GroupGrid.superclass.constructor.call(this, cfg);
	
}; 


Ext.extend(Est.ux.GroupGrid, Ext.grid.GridPanel, {
	
	_preInit: function(cfg){
		try {
			this._initStore(cfg.storeurl, cfg.colstype,cfg.groupField);
			this._initCols(cfg.colstype.cols);
		} catch(e) {
			Ext.error('Est.ux.Grid init error!!'+e);
		}
	},
	
	/*init grid store*/
	_initStore: function(url, type,groupField) {
		
		var id = type.id;
		var cols = type.cols;
		var fields = [];
		for(var i=0; i<cols.length; i++) {
			fields[i]=cols[i].dataIndex;
		}
		
		this.store = new Ext.data.GroupingStore({
			autoLoad: true,
			url: url,
			groupField: groupField,
			sortInfo:{field: groupField, direction: "ASC"},
			/*baseParams: {limit: 20},*/
	        reader: new Ext.data.JsonReader({
				totalProperty: 'total',
				root: 'rows',
				id: id,
				fields: fields
				
			})
		})
	},
	
	/*init grid columns*/
	_initCols: function(cols) {
		for(var i=0; i<cols.length; i++) {
			var col = {};
			/*col.dataIndex = cols[i].field;
			col.header = cols[i].header;
			col.width = cols[i].width;
			col.sortable = cols[i].sortable;*/
			col = cols[i];
			this.columns[i]=col;
		}
	},
	
	
	doSearch: function(condition, isReset) {
		if(isReset===true) {
			this.searchCondition={_r: new Date().getTime()};
		}
		for(var key in condition) {
			this.searchCondition[key] = condition[key]
		}
		console.log(this.searchCondition);
		//this.store.load({params: this.searchCondition});
		this.store.baseParams = this.searchCondition;
		if(this.hasPagerBar){
			this.getBottomToolbar().changePage(1);
		} else {
			this.store.reload();
		}
		
		for(var i=0; i<this.refobjs.length; i++) {
			if(this.refobjs[i].getXType()=='estform') {
				this.refobjs[i].doReset();
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
	}

});

Ext.reg('estgroupgrid', Est.ux.GroupGrid);
