Ext.namespace('Est.ux');
/**
 * Est.ux.EditableGrid Extension Class xtype is esteditgrid
 * 
 * @author jingpj
 * @version 1.0
 * 
 * @class Est.ux.Grid
 * @extends Ext.grid.GridPanel
 * 
 * 
 * @cfg {String} storeurl require
 * @cfg {Object} colstype require for example: {id: 'id', cols:
 *      [{dataIndex:'pwd', header: 'Password', width: '30', sortable: false},
 *      {dataIndex:'name', header: 'UserName',width: '40', sortable: true}]}
 * 
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 */
Est.ux.GroupEditableGrid = function(cfg) {

	this.store = '';
	this.columns = [];
	this.autoScroll = true;
	this.checkedRec;// 用于存放已经勾选的记录
	this.loadMask = true;
	this.loadCallBack = cfg.loadCallBack;
	// this.removeData = [];

	Ext.each(cfg.colstype.cols, function(e) {
				e['sortable'] = true;
			}); // 设置所有列为可排序

	/* columnModule */
	this.cm = new Ext.grid.ColumnModel(cfg.colstype.cols);

	/* use to search */
	this.searchCondition = {};
	/* init something */
	this._preInit(cfg);

	this.refobjs = [];

	this.viewConfig = new Ext.grid.GroupingView({
				// forceFit:true,
				sortAscText : '正序',
				sortDescText : '倒序',
				columnsText : '列显示/隐藏',
				groupByText : '依本列分组',
				showGroupsText : '分组显示',
				groupTextTpl : '{text} ({[values.rs.length]} 条记录)',
				getRowClass : function(record, rowIndex, rowParams, store) {
					if (record["modifystatus"] == "a") {
						return 'x-grid-record-add';
					} else if (record["modifystatus"] == "m") {
						return 'x-grid-record-modified';
					} else if (record["modifystatus"] == "d") {
						return 'x-grid-record-delete';
					}
				}
			}),

	this.sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	/* click times to invoke cell edit */
	this.clicksToEdit = 1;

	this.hasPagerBar = typeof cfg.hasPagerBar === 'undefined'
			? true
			: cfg.hasPagerBar;

	// this.store = this.gridStore,
	/* add page bar */
	if (this.hasPagerBar) {
		this.bbar = new Ext.PagingToolbar({
					store : this.store,
					displayInfo : true
				});
	}

	this.checkColumn = cfg.checkColumn;
	/** use to selected rows* */
	if (this.checkColumn) {
		var checkColumn = new Ext.grid.CheckColumn({
					name : 'est-grid-checkbox',
					dataIndex : 'est-grid-checkbox',
					type : 'bool',
					width : 20
				});
		cfg.colstype.cols.unshift(checkColumn);
		this.plugins = checkColumn;
	}

	Est.ux.GroupEditableGrid.superclass.constructor.call(this, cfg);

};

Ext.extend(Est.ux.GroupEditableGrid, Ext.grid.EditorGridPanel, {

	_preInit : function(cfg) {
		try {
			this._initStore(cfg.storeurl, cfg.colstype, cfg.groupField,
					cfg.loadCallBack);
			/*
			 * this.store.on('load',function(){
			 * this.getEl().select("table[class=x-grid3-row-table]").each(function(x) {
			 * x.addClass('x-grid3-cell-text-visible'); }) });
			 */
			// this._initCols(cfg.colstype.cols);
		} catch (e) {
			Ext.error('Est.ux.EditableGrid init error!!' + e);
		}
	},

	/* init grid store */
	_initStore : function(url, type, groupField, loadCallBack) {

		var id = type.id;
		var cols = type.cols;
		var fields = [];
		for (var i = 0; i < cols.length; i += 1) {
			fields[i] = cols[i].dataIndex;
		}

		this.store = new Ext.data.GroupingStore({
					autoLoad : true,
					url : url,
					groupField : groupField,
					sortInfo : {
						field : groupField,
						direction : "ASC"
					},
					/* baseParams: {limit: 20}, */
					reader : new Ext.data.JsonReader({
								totalProperty : 'total',
								root : 'rows',
								id : id,
								fields : fields
							}),
					listeners : {
						'load' : function(store, records, options) {
							if (loadCallBack) {
								loadCallBack(store, records, options);
							}
						}
					}
				})
	},

	/* init grid columns */
	_initCols : function(cols) {
		for (var i = 0; i < cols.length; i += 1) {
			var col = {};
			/*
			 * col.dataIndex = cols[i].field; col.header = cols[i].header;
			 * col.width = cols[i].width; col.sortable = cols[i].sortable;
			 */
			col = cols[i];
			col['sortable'] = true;
			alert(forin(col));
			this.columns[i] = col;
		}
	},

	doSearch : function(condition, isReset) {

		if (isReset === true) {
			this.searchCondition = {
				_r : new Date().getTime()
			};
		}

		Ext.apply(this.store.baseParams, condition);
		/*
		 * for(var key in condition) { this.searchCondition[key] =
		 * condition[key] } console.log(this.searchCondition);
		 * //this.store.load({params: this.searchCondition});
		 * this.store.baseParams = this.searchCondition;
		 */
		this.getBottomToolbar().changePage(1);

		for (var i = 0; i < this.refobjs.length; i += 1) {
			if (this.refobjs[i].getXType() == 'estform') {
				this.refobjs[i].doReset();
			}
		}

	},

	listeners : {
		"afteredit" : function(e) {
			e.record["notChange"] = false; // 新建后已修改标识
			if (e.record["modifystatus"] != "a"
					&& e.record["modifystatus"] != "d") {
				e.record["modifystatus"] = "m";
				this.view.addRowClass(e.row, "x-grid-record-modified");
				// this.doRender();
			}
		},
		"beforeedit" : function(e) {
			if (e.record["modifystatus"] === 'd') {
				e.cancel = true;
			}

		},
		"rowclick" : function(grid, rowIndex, e) {
			if (this.checkColumn) {
				try {

					var selected = grid.getSelectionModel().getSelected();
					selected.set('est-grid-checkbox', true);

					if (this.checkedRec) {
						this.checkedRec.set('est-grid-checkbox', false);
					}

					this.checkedRec = selected;

				} catch (e) {

				}

			}
		}
	},

	/* delete selected row */
	deleteRow : function() {
		this.stopEditing();
		var record = this.getSelectionModel().getSelected();
		if (record) {
			record["modifystatus"] = "d";
			// var data = record.data;
			// data["modifystatus"] = "d";
			// this.removeData.push(data);
			this.view.addRowClass(this.getStore().indexOf(record),
					"x-grid-record-delete");
			// this.getStore().remove(record);
		}
		// this.view.doRender();
	},

	/* append new row to specified location(bottom default ) */
	addRow : function(obj, location) {
		var n = location;
		if (typeof n !== 'number' || n < 0) {
			n = this.getStore().getCount();
		}
		this.stopEditing();
		this.getStore().insert(n, obj);
		var record = this.getStore().getAt(n);
		record["modifystatus"] = "a";
		this.startEditing(n, 0);
		var row = this.getSelectionModel().getSelected();
		this.view.addRowClass(n, "x-grid-record-add");
	},
	/*
	 * insert a row before current row(if not select any row,insert before first
	 * row)
	 */
	insertRow : function(obj) {
		var row = this.getSelectionModel().getSelected();
		var rowIndex = this.getStore().indexOf(row);
		// 没有选择，插入到首行
		if (rowIndex == -1) {
			rowIndex = 0;
		}
		this.addRow(obj, rowIndex);
	},

	/* batch save */
	onSave : function(/* 保存地址 */saveUrl) {
		var changedRows = [];
		var store = this.getStore();
		var cm = this.getColumnModel();
		this.stopEditing();// 停止编辑
		var validateFlag = {
			validate : true,
			row : 0,
			col : 0
		}; // 验证标示，并暂存第一个验证失败cell
		store.each(function(record) {
			if (record['modifystatus']) { // 得到所有修改过的数据
				var rowIndex = store.indexOf(record);
				for (var i = 0; i < cm.getColumnCount(); i += 1) {
					var columnName = cm.getColumnById(i).name;
					var columnValue = record.data[columnName];
					var editor = cm.getCellEditor(i, rowIndex);

					// 如果列有验证，进行验证
					if (editor) {
						if (!editor.field.validateValue(columnValue)) {
							validateFlag = {
								validate : false,
								row : rowIndex,
								col : i
							};
							return;
						}
					}
				}

				var fields = record.fields.keys;

				// 行封装对象
				var tmpobj = {
					'modifystatus' : record['modifystatus']
				};

				// 对行进行封装
				for (var key in record.data) {
					if (key.indexOf('.') > -1) {
						// 如果列是主从表的数据，将 【主表名称.主表id:value】 封装成
						// 【主表名称:{主表id:value}】的格式
						var arrName = key.split('.');
						var obj = tmpobj;
						// obj[arrName[arrName.length-1]]=record.data[key];

						for (var i = 0; i < arrName.length - 1; i += 1) {
							if (obj[arrName[i]] === undefined) {
								obj[arrName[i]] = {};
							}
							obj = obj[arrName[i]];
						}

						if (Ext.isDate(record.data[key])) {
							obj[arrName[arrName.length - 1]] = record.data[key]
									.format('Y-m-d H:i:s');
						} else {
							obj[arrName[arrName.length - 1]] = record.data[key];
						}

						/*
						 * for(var i=arrName.length-2 ;i>=0; i-=1) { var t =
						 * obj; if(i!=0){ obj = {}; obj[arrName[i]]=t; } else {
						 * tmpobj[arrName[i]]=t; } }
						 */

					} else {
						if (Ext.isDate(record.data[key])) {
							tmpobj[key] = record.data[key]
									.format('Y-m-d H:i:s');
						} else {
							tmpobj[key] = record.data[key];
						}
						// 不是主从表，直接复制

					}
				}
				if (record['modifystatus'] === 'a'
						|| (record['modifystatus'] !== 'a' && record['notChange'] !== undefined)) {
					changedRows.push(tmpobj);
				} else if (record['modifystatus'] !== undefined) {
					changedRows.push(tmpobj);
				}

			}
		});

		if (validateFlag.validate) {

			// var b = grid.getStore().getModifiedRecords();

			if (changedRows.length == 0) {
				Ext.Msg.alert('提示', '数据没有任何更改！');
				return;
			}

			var data = encodeURIComponent(Ext.encode(changedRows));
			Ext.Ajax.request({
						url : saveUrl,
						params : {
							data : data
						},
						waitMsg : '正在保存...',
						success : function(rep) {
							var msgObj = Ext.decode(rep.responseText);
							if (msgObj.success) {
								showMsg('保存成功');
								store.commitChanges();
								store.reload();
							} else {
								if (msgObj.error) {
									error('提示', msgObj.error);
								} else {
									estfailure();
								}

							}
						},
						failure : estfailure
					});
		} else {
			Ext.Msg.alert('提示', '请检查输入！', function() {
						this.startEditing(validateFlag.row, validateFlag.col);
					}, this);

		}

	},

	/**
	 * 获取勾选的数据
	 */
	getCheckedRecords : function(
			flush/* 是否需要将当前grid中的数据刷新到数据库 true 标示是，否则为不是 */, showmsg) {
		var changedRows = [];
		var store = this.getStore();
		var cm = this.getColumnModel();
		var recordId = this.recordId;
		var flag = true;
		this.stopEditing();// 停止编辑
		var validateFlag = {
			validate : true,
			row : 0,
			col : 0
		};
		store.each(function(record) {
					if (flush === true && record['modifystatus']) { // 得到所有修改过的数据
						error('提示', '存在已修改数据，请先保存！');
						flag = false;
						return;
					} else {
						for (var key in record.data) {
							if (key == 'est-grid-checkbox') {
								if (record.data[key] === true) {
									changedRows.push(record);
								};
							}
						}
					}
				});

		if (changedRows.length == 0 && flag
				&& (showmsg == undefined || showmsg == true)) {
			Ext.Msg.alert('提示', '没有勾选任何记录！');
			return;
		} else {
			return changedRows;
		}
	},

	addRefObjs : function(objs) {
		if (typeof objs === 'array') {
			for (var i = 0; i < objs.length; i += 1) {
				this.refobjs[this.refobjs.length] = objs[i];
			}
		} else {
			this.refobjs[this.refobjs.length] = objs;
		}
	}

});

Ext.reg('estgroupeditgrid', Est.ux.GroupEditableGrid);

Ext.grid.CheckColumn = function(config) {
	Ext.apply(this, config);
	if (!this.id) {
		this.id = Ext.id();
	}
	this.renderer = this.renderer.createDelegate(this);
};

Ext.grid.CheckColumn.prototype = {
	init : function(grid) {
		this.grid = grid;
		this.grid.on('render', function() {
					var view = this.grid.getView();
					view.mainBody.on('mousedown', this.onMouseDown, this);
				}, this);
	},
	onMouseDown : function(e, t) {
		if (t.className && t.className.indexOf('x-grid3-cc-' + this.id) != -1) {
			e.stopEvent();
			var index = this.grid.getView().findRowIndex(t);
			var record = this.grid.store.getAt(index);
			record.set(this.dataIndex, !record.data[this.dataIndex]);
		}
	},

	renderer : function(v, p, record) {
		p.css += ' x-grid3-check-col-td';
		return '<div class="x-grid3-check-col' + (v ? '-on' : '')
				+ ' x-grid3-cc-' + this.id + '">&#160;</div>';
	}
};
