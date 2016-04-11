Ext.namespace('Est.ux');
/**
 * Est.ux.CheckSelectGrid Extension Class
 * xtype is estcheckgrid
 * 
 * @author jingpj
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
Est.ux.CheckSelectGrid = function(cfg) {
	
	this.store = '';
	this.columns = [];
	this.autoScroll = true;
	this.chooseField = cfg.chooseField;
	this.baseParams= cfg.baseParams === undefined ? {} : cfg.baseParams ;
	this.pageSize = cfg.pageSize === undefined ? 20 : cfg.pageSize;
	/*use to search*/
	this.searchCondition = {};
	this.sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect: cfg.singleSelect===true?true:false,
				listeners:{
					'rowdeselect':function(/* SelectionModel*/ sm, /*Number*/ rowIndex, /*Record*/ record){
									this.selectedRecord.pop(record.data);
									for(var i=0;i<this.selectedRecord.length;i++) {
										var r = this.selectedRecord[i];
										if(r[cfg.colstype.id] == record.id){
											this.selectedRecord.splice(i,1);
											break;
										}
									}
								  },
					'rowselect':function(/* SelectionModel*/ sm, /*Number*/ rowIndex, /*Record*/ record){
									
									var flag = true;
									for(var i=0;i<this.selectedRecord.length;i++) {
										var r = this.selectedRecord[i];
										if(r[cfg.colstype.id] == record.id){
											flag = false;
											break;
											
										}
									}
									if(flag){
										this.selectedRecord.push(record.data);
									}
								  },
					'selectionchange': function(/* SelectionModel*/ sm) {
						if (cfg.colstype.dataChangeCallback) {
							cfg.colstype.dataChangeCallback(sm);
						}
					}
				}
		
			
			});
			
	//this.sm.selectedRecord = [];
	
	/*init something*/
	this._preInit(cfg);
	
	this.refobjs=[];
	
	this.viewConfig = {
		forceFit: true
	},
	
	
	//this.store = this.gridStore,
	/*add page bar*/
	this.bbar = new Ext.PagingToolbar({store: this.store,pageSize :this.pageSize,displayInfo : true}),
	this.bbar.sm = this.sm;
	
	this.bbar.on("change",function(){
					var sm = this.getSelectionModel();
					for(var i=0;i<sm.selectedRecord.length;i+=1){
						var records = [];
						var r = this.store.getById(sm.selectedRecord[i][cfg.colstype.id]);
						records.push(r);
						sm.selectRecords(records,true);
					}
				
				},this);
	
	Est.ux.CheckSelectGrid.superclass.constructor.call(this, cfg);
	this.getSelectionModel().selectedRecord = [];
	
}; 


Ext.extend(Est.ux.CheckSelectGrid, Ext.grid.GridPanel, {
	
	_preInit: function(cfg){
		try {
			this._initStore(cfg.storeurl, cfg.colstype);
			this._initCols(cfg.colstype.cols);
			
		} catch(e) {
			Ext.error('Est.ux.Grid init error!!'+e);
		}
		
	},
	
	/*init grid store*/
	_initStore: function(url, type) {
		
		var id = type.id;
		var cols = type.cols;
		var fields = [];
		for(var i=0; i<cols.length; i++) {
			fields[i]=cols[i].dataIndex;
		}
		this.store = new Ext.data.Store({
			autoLoad: true,
			url: url,
			baseParams:this.baseParams,
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
		this.columns = cols;
		this.columns.splice(0,0,this.sm);
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
		this.store.baseParams = Ext.apply(this.store.baseParams,this.searchCondition);
		this.getBottomToolbar().changePage(1);
		/*
		for(var i=0;i<this.sm.selectedRecord;i+=1){
			alert(his.sm.selectedRecord[i].id);
			var index = this.store.indexOf(this.sm.selectedRecord[i]);
			if(index!=-1){
				
				this.sm.selectRow(index);
			}
		}*/
		
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
	},
		
	getSelect: function(){
		return this.getSelectionModel().selectedRecord;
	},
	cancelSelect:function(){
		this.getSelectionModel().selectedRecord = []
		this.getSelectionModel().clearSelections();
	}

});

Ext.reg('estcheckgrid', Est.ux.CheckSelectGrid);
