
Ext.namespace('Est.ux');
/**
 * Est.ux.WfStartflowButton Extension Class
 * 
 * @author jingpj
 * @version 1.0
 * 
 * @class Est.ux.WfStartflowButton
 * @extends Ext.Button
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 */
Est.ux.WfApproveHistoryTab = function(cfg) {
	var self = this;
	this.bindGrid = cfg.bindGrid;
	this.bindGridPiidField = cfg.bindGridPiidField;
	//this.moduleid = cfg.moduleid || _moduleId;
	//this.xtype = "estform";
	
	this.title = '审批历史';
	this.iconCls = 'icon_history';
	this.id = 'approveHistoryTab';
	this.layout = 'fit';
	this.isGridRelated = cfg.isGridRelated !== undefined ? cfg.isGridRelated : true;
	
	this.items = [{
				xtype:'estgrid',
				id:'wfApproveHistoryGrid',
				title:'审批意见',
				region:'center',
				storeurl: 'est/workflow/process/WfApprove/getApproveInfoList',
				colstype: {id: 'approveId', 
				cols: [{dataIndex:'taskName', header: '流程任务', width: 100},
						{dataIndex:'result', header: '结论',width: 100},
						{dataIndex:'opinion', header: '审批意见',width: 100},
						//{dataIndex:'remark', header: '备注',width: 100},
						{dataIndex:'nameapproveby', header: '审批人',width: 100},
						{dataIndex:'approvedate', header: '时间',width: 100},
						{dataIndex:'nexttaskname',header:'下一审批任务',width:100}
					]}
			
			}];
			
	
	
	
	Est.ux.WfApproveHistoryTab.superclass.constructor.call(this, cfg);
	
	
	if(this.isGridRelated === true) {
			Ext.getCmp(this.bindGrid).on({
			'rowclick': function(grid,rowIndex,e){
				this.purgeListeners();
				this.on({'afterlayout': function(){
						//var selectedRow = Ext.getCmp(this.bindGrid).getSelectionModel().getSelected();
					var selectedRow = Ext.getCmp(this.bindGrid).getStore().getAt(rowIndex);
						if(selectedRow) {
							var piid = selectedRow.data[this.bindGridPiidField] || selectedRow.data['wfpiid'] || selectedRow.data['wfProcessInstanceId'] ;
							var store = Ext.getCmp('wfApproveHistoryGrid').doSearch({piid: piid});
						}
						this.purgeListeners();
					}
				});
				
			},scope:this
	
			});
	} else {
		self.on({'afterlayout': function(){
			Ext.getCmp('wfApproveHistoryGrid').doSearch({piid:_wfProcessInstanceId});
		}});
	}

	
}; 

Ext.extend(Est.ux.WfApproveHistoryTab, Est.ux.Layout, {
}); 

Ext.reg('estwfapprovehistorytab', Est.ux.WfApproveHistoryTab);
