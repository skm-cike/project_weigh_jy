Ext.namespace('Est.ux');
/**
 * desc 用于在弹出窗口的grid中选择数据，可用于form字段回填，以及grid字段回填 Est.ux.TreeGridChooserField
 * Extension Class xtype is treegridchooserfield
 * 
 * @author hebo
 * @version 1.0
 * 
 * @class Est.ux.TreeGridChooserField
 * @extends Ext.form.TriggerField
 * 
 * @cfg {Object} colstype require
 * @cfg {Object} feedbackfields {'被回填字段':'数据来源字段'} for example: {id: 'id', cols:
 *      [{dataIndex:'pwd', header: 'Password', width: '30', sortable: false},
 *      {dataIndex:'name', header: 'UserName',width: '40', sortable: true}]}
 * 
 * @constructor
 * @param {Object}
 *          cfg Configuration options
 */
Est.ux.TreeGridChooserField = function(cfg) {
	// 弹出窗口的按钮
	this._preInit(cfg);
	// 弹出的窗口
	this.win;
	// 回填form的ID
	this.feedbackFormid = cfg.colstype.feedbackFormid;
	this.singleSelect=cfg.colstype.singleSelect;
	// 列设置
	this.cols = cfg.colstype.cols;
    //回调函数
    this.callback=cfg.callback;
	// grid回填字段
	this.feedbackfields = cfg.feedbackfields;
    this.readOnly=true;
    this.triggerClass =  'x-form-search-trigger';
	Est.ux.TreeGridChooserField.superclass.constructor.call(this, cfg);
}
Ext.extend(Est.ux.TreeGridChooserField, Ext.form.TriggerField, {
			// 初始化
			_preInit : function(cfg) {
				var buttons = [{
							text : '确定',
							handler : function() {
								var record = this.getSeletedRecord()
								if (this.feedbackFormid) {
									this.feedBackToForm();
								}
								if (this.feedbackGridid) {
									this.feedBackGrid();
								}
                                //选中记录后的回调函数
                                if(this.callback){
                                   return this.callback(record);
                                   
                                }
							},
							scope : this
						}, {
							text : '关闭',
							handler : function() {
								this.win.closeWin();
							},
							scope : this
						}];
				cfg.buttons = buttons;
				this.win = new Est.ux.TreeGridWin(cfg);
			},
			// 弹出框
			onTriggerClick : function() {
				this.win.showWin();
			},
			// 回填form
			feedBackToForm : function() {
				var data = this.win.getSeletedRecords();
				this.win.cancelSeletedRecords();
				var destForm;
				for (var i = 0; i < this.cols.length; i++) {
					var tmpstr = '';
					if (this.cols[i]['isReturn']) {
						var fieldname = this.cols[i]['dataIndex'];
						for (var j = 0; j < data.length; j += 1) {
							if(this.singleSelect){
								tmpstr = data[0][fieldname];
							}else{
								tmpstr += data[j][fieldname];
								if (j < data.length - 1) {
									tmpstr += ',';
								}
							}
						}
						// 回填form
						destForm = Ext.getCmp(this.feedbackFormid).form;
						var field = destForm.findField(this.cols[i]['fieldto']);
						if (field) {
							field.reset();
							field.setValue(tmpstr);
						} else {
							alert('回填错误,未找到form回填字段:' + this.cols[i]['fieldto']);
						}
					}
				}
				this.win.closeWin();
			},
			// 回填gird
			feedBackGrid : function() {
				var data = this.win.getSeletedRecords();
				var selectedRecord;
				for (var i = 0; i < this.cols.length; i++) {
					if (this.cols[i]['isReturn']) {
						selectedRecord = Ext.getCmp(this.feedbackGridid)
								.getSelectionModel().getSelected();
						if (selectedRecord) {
							for (var key in this.feedbackfields) {
								for (var k = 0; k < data.length; k += 1) {
									var value = data[k][this.feedbackfields[key]];
									selectedRecord.set(key, value);
									
									//修改record修改状态 add by jingpj
									selectedRecord["notChange"] = false;	//新建后已修改标识
									if(selectedRecord["modifystatus"]!="a" && selectedRecord["modifystatus"]!="d"){
										selectedRecord["modifystatus"] = "m";
									}
									
								}
							}
						}
					}
				}
				this.win.closeWin();
			},
           //返回选中的数据
          getSeletedRecord:function(){
             return this.win.getSeletedRecords();
          },
          cancelSeletedRecords:function(){
          		this.win.cancelSeletedRecords();
          }
		});
Ext.reg('treegridchooserfield', Est.ux.TreeGridChooserField);
