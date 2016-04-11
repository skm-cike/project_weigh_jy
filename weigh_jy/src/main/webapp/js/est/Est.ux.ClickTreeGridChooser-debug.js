Ext.namespace('Est.ux');
/**
 * desc 用于在弹出窗口的grid中选择数据，可用于form字段回填，以及grid字段回填 Est.ux.ClickTreeGridChooser
 * Extension Class xtype is treegridchooserfield
 * 
 * @author hebo
 * @version 1.0
 * 
 * @class Est.ux.ClickTreeGridChooser
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
Est.ux.ClickTreeGridChooser = function(cfg) {
	
	// 弹出的窗口
	this.win;
    //回调函数
    this.callback=cfg.callback;
    this.beforeWinShow = cfg.beforeWinShow;
    this.afterWinShow = cfg.afterWinShow;
    
    // 弹出窗口的按钮
   this._preInit(cfg);
	Est.ux.ClickTreeGridChooser.superclass.constructor.call(this, cfg);
}
Ext.extend(Est.ux.ClickTreeGridChooser, Ext.Toolbar.Button, {
			// 初始化
			_preInit : function(cfg) {
				var buttons = [{
							text : '确定',
							handler : function() {
                                var record = this.getSeletedRecords();
								this.callback(record);
								this.cancelSeletedRecords();
							},
							scope : this
						}, {
							text : '取消/关闭',
							handler : function() {
								this.win.closeWin();
							},
							scope : this
						}];
				cfg.buttons = buttons;
				this.win = new Est.ux.TreeGridWin(cfg);
			},
		    // 弹出框
			onClick : function(e) {
				if(this.beforeWinShow){
					if(this.beforeWinShow()){
							this.win.showWin();
					}
				}else{
						this.win.showWin();
				}
			
				if(this.afterWinShow){
					this.afterWinShow(this);
				}
			},
           //返回已选择的行
           getSeletedRecords:function(){
              return this.win.getSeletedRecords();
           },
           //取消已选择的行
           cancelSeletedRecords:function(){
           	  this.win.cancelSeletedRecords();
           },
           //关闭窗口
           closeWin:function(){
              this.win.closeWin();
           }
		});
Ext.reg('clicktreegridchooser', Est.ux.ClickTreeGridChooser);
