
Ext.namespace('Est.ux');
/**
 * Est.ux.MtModifyLogHistoryTab
 * 
 * @author hebo
 * @version 1.0
 * 
 * @class Est.ux.MtModifyLogHistoryTab
 * @extends Ext.Button
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 */
Est.ux.MtModifyLogHistoryTab = function(cfg) {
	var self = this;
	this.bindGrid = cfg.bindGrid;
	this.title = '修改记录';
	this.iconCls='table_row_insert';
	this.id = 'mtModifyLogHistoryTab';
	this.layout = 'fit';
	this.bindRecField = cfg.bindRecField;
	this.bindTablename = cfg.bindTablename;
	this.title = cfg.title == undefined ? '修改记录' : cfg.title ;
	this.items = [{ xtype:'estgrid',
					id:'mtModifyLogHistoryGrid',
					title:this.title,
					region:'center',
					storeurl: 'est/materiallz/log/MtModifyLog/getMtModifiedLog?dc='+new Date().getTime(),
					colstype: {id: 'id', 
							   cols: [ {dataIndex:'modifer', header: '操作人', width: 100},
							   			{dataIndex:'modfiydate', header: '操作时间', width: 100},
							   			{dataIndex:'modifidcontent', header: '操作记录', width: 300}]
							  }
			    }];
		Est.ux.MtModifyLogHistoryTab.superclass.constructor.call(this, cfg);
	
		Ext.getCmp(this.bindGrid).on({
		'rowclick': function(grid,rowIndex,e){
			this.purgeListeners();
			this.on({'afterlayout': function(){
					//var selectedRow = Ext.getCmp(this.bindGrid).getSelectionModel().getSelected();
				   var selectedRow = Ext.getCmp(this.bindGrid).getStore().getAt(rowIndex);
					if(selectedRow) {
						var oldrecid = selectedRow.data[self.bindRecField];
						var store = Ext.getCmp('mtModifyLogHistoryGrid').doSearch({oldrecid:oldrecid,tablename:self.bindTablename});
						//alert(Ext.encode({oldrecid:oldrecid,tablename:self.bindTablename}));
					}
					this.purgeListeners();
				}
			});
			
		},scope:this

		});
}; 

Ext.extend(Est.ux.MtModifyLogHistoryTab,Est.ux.Layout,{}); 

Ext.reg('estmtmodifyloghistorytab', Est.ux.MtModifyLogHistoryTab);
