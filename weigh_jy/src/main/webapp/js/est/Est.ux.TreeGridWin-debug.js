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
 *            cfg Configuration options
 */
Est.ux.TreeGridWin = function(cfg) {
	this.autoScroll = true;
	// 窗口弹出位置
	var winPopPosition = cfg.winPopPosition;
	// 窗口ID
	var winid = cfg.winid;
	// 窗口标题
	var title = cfg.title;
	// 是否单选
	this.singleSelect = cfg.colstype.singleSelect == true ? true : false;
	this.autoBuildSearchButton = cfg.colstype.autoBuildSearchButton;
	// 搜索条件的ID
	var scid = winid + "field";
	// 弹出窗口中的grid的ID
	var popGridid = winid + '-xx-grid';
	// 组件
	var items = [];
	// 树的ID
	this.treeid = '';
	// 搜索字段
	var searchFields = [];
	// 是否可用
	this.disabled = cfg.disabled;
	// 如果只设置了1个查询字段
	if (cfg.colstype.scname) {
		searchFields = [{
					xtype : 'label',
					text : cfg.colstype.lable
				}, {
					width : 150,
					xtype : 'textfield',
					id : scid
				}];
	} else if (cfg.colstype.searchFields) {// 如果自定义了查询字段
		searchFields = cfg.colstype.searchFields;
	}
	searchFields.push(' ');
	if (this.autoBuildSearchButton === true || this.autoBuildSearchButton === undefined) {

		searchFields.push({
			text : '查询',
			handler : function() {
				var searchCondition = '';
				if (cfg.colstype.scname) {
					var field = Ext.getCmp(scid);
					var key = cfg.colstype.scname;
					var value = field.getValue().trim();
					searchCondition = eval("({'" + key + "':'" + value + "'})");
				} else if (cfg.colstype.searchFields) {// 取出搜索条件映射
					var searchMapping = cfg.colstype.searchMapping;
					searchCondition = "{"
					for (var key in searchMapping) {
						var field = Ext.getCmp(key);
						var value = field.getValue();
						if (field.isXType('datefield')) {// 转化日期格式
							var initialConfig = field.initialConfig;
							var format = initialConfig['format'];
							if (format) {
								value = Ext.util.Format.date(value, format);
							} else {
								value = Ext.util.Format.date(value, 'Y-m-d');
							}
						}
						searchCondition += "'" + searchMapping[key] + "'"
								+ ":'" + value + "',";
					}
					searchCondition = Ext.decode(searchCondition.substring(0,
							searchCondition.length - 1)
							+ "}");
				}
				Ext.getCmp(popGridid).doSearch(searchCondition, true);
			}
		}, {
			text : '重置',
			hidden : cfg.colstype.hiddenRestButt
					? cfg.colstype.hiddenRestButt
					: false,
			handler : function() {
				if (cfg.colstype.scname) {
					Ext.getCmp(scid).reset();

				} else if (cfg.colstype.searchFields) {// 取出搜索条件映射
					var searchMapping = cfg.colstype.searchMapping;
					searchCondition = "{"
					for (var key in searchMapping) {
						Ext.getCmp(key).reset();
					}

				}
			}
		});

	}

	// 中间的grid
	this.checkColumn = cfg.colstype.checkColumn;
	this.autoLoad = cfg.colstype.autoLoad;
	if (this.checkColumn === undefined) {
		this.gridPanel = new Est.ux.CheckSelectGrid({
					id : popGridid,
					singleSelect : this.singleSelect,
					storeurl : cfg.colstype.storeurl,
					autoLoad: this.autoLoad === undefined?false:this.autoLoad,
					colstype : cfg.colstype,
					region : 'center',
					tbar : searchFields,
					viewConfig : {
						forceFit : false
					},
					chooseField : this,
					baseParams : cfg.colstype.baseParams,
					pageSize : cfg.colstype.pageSize
				});
	} else {
		this.gridPanel = new Est.ux.EditableGrid({
					id : popGridid,
					singleSelect : this.singleSelect,
					checkColumn:this.checkColumn,
					autoLoad: this.autoLoad === undefined?false:this.autoLoad,
					storeurl : cfg.colstype.storeurl,
					colstype : cfg.colstype,
					region : 'center',
					tbar : searchFields,
					viewConfig : {
						forceFit : false
					},
					chooseField : this,
					baseParams : cfg.colstype.baseParams,
					pageSize : cfg.colstype.pageSize
				});
	}
	// 如果定义了tree组件,添加tree组件
	if (cfg.treePanel) {
		this.treeid = cfg.treePanel.treeid;
		// 树的URL
		this.treeLoderUrl = cfg.treePanel.treeLoderUrl;
		// 树根节点名称
		this.rootTxt = cfg.treePanel.rootTxt;
		// 树的节点id对应的grid的查询条件字段
		var treeSCParam = cfg.treePanel.treeSCParam;
		// 左边的树
		this.treePanel = new Est.ux.Tree({
					isctx : false,
					id : this.treeid,
					width : cfg.treePanel.treeWidth,
					rootTxt : this.rootTxt,
					region : 'west',
					loaderurl : this.treeLoderUrl
				});
		items = [this.treePanel, this.gridPanel];
	} else {
		items = [this.gridPanel];
	}
	// 弹出的窗口
	this.chooseWin = new Ext.Window({
				title : title,
				iconCls : 'icon_winedit',
				width : cfg.winWidth,
				height : cfg.winHeight,
				id : winid,
				layout : 'border',
				plain : true,
				closeAction : 'hide',
				items : items,
				buttons : cfg.buttons,
				listener : {}
			});
	// 如果定义了tree,则设置点击树的时候，右边的grid联动
	if (cfg.treePanel) {
		var tree = Ext.getCmp(this.treeid);
		tree.on({
			'click' : function(node, e) {
				if (typeof treeSCParam == 'string') {
					var searchCondition = eval("({'" + treeSCParam + "':'"
							+ node.id + "'})");
					Ext.getCmp(popGridid).doSearch(searchCondition, true);
				} else {
					var searchCondition = {};
					for (var key in treeSCParam) {
						// alert(node.attributes[treeSCParam[key]]);
						if (typeof(treeSCParam[key]) === 'function') {
							searchCondition[key] = treeSCParam[key](node);
						} else {
							searchCondition[key] = node.attributes[treeSCParam[key]];
						}
					}
					Ext.getCmp(popGridid).doSearch(searchCondition, true);

				}
			}
		});
	}

	Est.ux.TreeGridWin.superclass.constructor.call(this, cfg);
}

Ext.extend(Est.ux.TreeGridWin, Ext.Window, {
			// 窗口显示
			showWin : function() {
				if (!this.disabled) {
					if (this.autoLoad === undefined || this.autoLoad === true) {
						this.gridPanel.getStore().reload();
					}
					this.chooseWin.show();
				}
			},
			// 窗口关闭
			closeWin : function() {
				this.chooseWin.hide();
			},
			// 获取已选择的record
			getSeletedRecords : function() {
				if (this.checkColumn === undefined) {
					return this.gridPanel.getSelect();
				}  else {
					return this.gridPanel.getSelectionModel().getSelections();
				}
			},
			// 取消勾选的行
			cancelSeletedRecords : function() {
				if (this.checkColumn === undefined) {
					this.gridPanel.cancelSelect();
				}
			},
			// 获取store
			getStore : function() {
				return this.gridPanel.getStore();
			},
			// 获取树
			getTree : function() {
				return Ext.getCmp(this.treeid);
			},
			// 获取grid
			getGrid : function() {
				return this.gridPanel;
			}

		});

Ext.reg('treegirdwin', Est.ux.TreeGridWin);
