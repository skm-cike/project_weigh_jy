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
Est.ux.EditableGrid = function(cfg) {

	this.store = '';
	this.columns = [];
	this.autoScroll = true;
	this.loadMask = true;
	// this.recordId use to onSaveByChecked function
	this.recordId = cfg.colstype.id;
	// this.removeData = [];
	this.checkColumn = cfg.checkColumn;
	this.checkedRec;// 用于存放已经勾选的记录
	// alert('check='+Ext.encode(cfg));
	/** use to selected rows* */
	if (this.checkColumn) {
		var checkColumn = new Ext.grid.CheckColumn({
					header : '<div class="x-grid3-hd-checker">&#160;</div>',
					name : 'est-grid-checkbox',
					dataIndex : 'est-grid-checkbox',
					sortable : false,
					menuDisabled : true,
					type : 'bool',
					width : 20,
					fixed : true,
					id : 'checker'
				});
		cfg.colstype.cols.unshift(checkColumn);
		// this.plugins = checkColumn;

	}
	var i = 0;
	Ext.each(cfg.colstype.cols, function(e) {
				if (e['sortable']==undefined) {
					if (i == 0) {
						e['sortable'] = false;
					} else {
						e['sortable'] = true;
					}
				}
				i++;
			}); // 设置所有列为可排序
	/* columnModule */
	this.cm = new Ext.grid.ColumnModel(cfg.colstype.cols);
	if (this.checkColumn) {
		this.sm = new Ext.grid.CheckboxSelectionModel({
					onMouseDown : function(e, t) {
						if (e.button === 0) { // Only
							e.stopEvent();
							var row = e.getTarget('.x-grid3-row');
							if (row) {
								var index = row.rowIndex;
								var record = this.grid.getStore().getAt(index); // Get
								// var isChecked =
								// record.get('est-grid-checkbox');
								var _self = this;
								if (this.isSelected(index)) {
									_self.deselectRow(index);
									setTimeout(function() {
												record.set('est-grid-checkbox',
														false);
											}, 200);
								} else {
									_self.selectRow(index, true);
									setTimeout(function() {
												record.set('est-grid-checkbox',
														true);
											}, 200);
								}
							}
						}
					},

					// private
					onHdMouseDown : function(e, t) {
						if (t.className == 'x-grid3-hd-checker') {
							e.stopEvent();
							var hd = Ext.fly(t.parentNode);
							var isChecked = hd
									.hasClass('x-grid3-hd-checker-on');
							var store = this.grid.getStore();
							var size = store.getCount();
							if (isChecked) {
								hd.removeClass('x-grid3-hd-checker-on');
								this.clearSelections();
								for (var i = 0; i < size; i++) {
									store.getAt(i).set('est-grid-checkbox',
											false);
								}
							} else {
								hd.addClass('x-grid3-hd-checker-on');
								this.selectAll();
								for (var i = 0; i < size; i++) {
									store.getAt(i).set('est-grid-checkbox',
											true);
								}
							}
						}
					},listeners:{
						'selectionchange': function(/* SelectionModel*/ sm) {
							if (cfg && cfg.colstype && cfg.colstype.dataChangeCallback) {
								cfg.colstype.dataChangeCallback(sm);
							}
						}
					}
					
				});
	}else{
			this.sm = new Ext.grid.RowSelectionModel({singleSelect:true});
	}

	/*
	 * this.sm = new Ext.grid.CheckboxSelectionModel( { singleSelect : true } );
	 */
	// this.selModel =new Ext.grid.CheckboxSelectionModel();
	/* use to search */
	this.searchCondition = {};
	/* init something */
	this._preInit(cfg);

	this.refobjs = [];

	this.viewConfig = {
		// forceFit: true,
		getRowClass : function(record, rowIndex, rowParams, store) {
			if (record["modifystatus"] == "a") {
				return 'x-grid-record-add';
			} else if (record["modifystatus"] == "m") {
				return 'x-grid-record-modified';
			} else if (record["modifystatus"] == "d") {
				return 'x-grid-record-delete';
			}
		}
	};

	/* click times to invoke cell edit */
	this.clicksToEdit = 1;

	this.hasPagerBar = typeof cfg.hasPagerBar === 'undefined'
			? true
			: cfg.hasPagerBar;
    this.pageSize = typeof cfg.pageSize === 'undefined'
			? 20
			: cfg.pageSize;

	// this.store = this.gridStore,
	/* add page bar */
	if (this.hasPagerBar&&this.pageSize) {
		this.bbar = new Ext.PagingToolbar({
					store : this.store,
					pageSize:this.pageSize,
					displayInfo : true
				});
	}

	Est.ux.Grid.superclass.constructor.call(this, cfg);

};

Ext.extend(Est.ux.EditableGrid, Ext.grid.EditorGridPanel, {

	_preInit : function(cfg) {
		try {
			this._initStore(cfg.storeurl, cfg.colstype, cfg);
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
	_initStore : function(url, type, cfg) {

		var id = type.id;
		var cols = type.cols;
		var fields = [];
		for (var i = 0; i < cols.length; i += 1) {
			fields[i] = cols[i].dataIndex;
		}

		var baseparams = cfg.baseParams ? cfg.baseParams : {};
		var autoLoad = cfg.autoLoad === undefined ? true : cfg.autoLoad; 
		var callback = cfg.callback ? cfg.callback : function(store, rec, op) {
		};
		this.store = new Ext.data.Store({
					autoLoad : autoLoad ,
					url : url,
					baseParams : baseparams,
					listeners : {
						load : callback
					},
					reader : new Ext.data.JsonReader({
								totalProperty : 'total',
								root : 'rows',
								id : id,
								fields : fields
							})
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

	doSearch : function(condition, isReset,callbackFn) {

		if (isReset === true) {
			this.searchCondition = {
				_r : new Date().getTime()
			};
		}
		Ext.apply(this.store.baseParams, condition);
		// Ext.applyIf(this.store.baseParams,condition);
		/*
		 * for(var key in condition) { this.searchCondition[key] =
		 * condition[key] } console.log(this.searchCondition);
		 * //this.store.load({params: this.searchCondition});
		 * this.store.baseParams = this.searchCondition;
		 */
		if (this.hasPagerBar) {
			this.getBottomToolbar().changePage(1);
			if(callbackFn){
				//this.store.load({callback:callbackFn?callbackFn:Ext.emptyFn});
				setTimeout(callbackFn,1000);
				//callbackFn();
			}
		} else {
			this.store.reload({callback:callbackFn?callbackFn:Ext.emptyFn});
		}

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
		"rowclick" : function(grid, rowIndex, e, t) {
			if (this.checkColumn) {
				try {

					/*
					 * var selected = grid.getSelectionModel().getSelected();
					 * selected.set('est-grid-checkbox', true);
					 * 
					 * 
					 * if (this.checkedRec) {
					 * this.checkedRec.set('est-grid-checkbox', false); }
					 * 
					 * 
					 * this.checkedRec = selected;
					 */

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
		this.stopEditing();
		if (typeof n !== 'number' || n < 0) {
			n = 0;// this.getStore().getCount();
		}
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
	onSave : function(/* 保存地址 */saveUrl,/* 回调方法 */callbackFn,other_params) {
		var changedRows = this.wrapChangedRows();
 			if(changedRows &&  changedRows.length>0){
 				var store = this.getStore();
 				var data = encodeURIComponent(Ext.encode(changedRows));
 				var _params = {data : data};
				if (other_params) {
					if (other_params.data!=undefined) {
						Ext.Msg.alert('提示', '数据参数名不能为data');
						return;
					}
					Ext.apply(_params, other_params);
				}
				Ext.Ajax.request({
							url : saveUrl,
							params :_params,
							waitMsg : '正在保存...',
							success : function(rep) {
								var msgObj = Ext.decode(rep.responseText);
								if (msgObj.success) {
									showMsg('保存成功');
									store.commitChanges();
									store.reload();
									if (callbackFn) {
										callbackFn(rep);
									}
								} else {
									if (msgObj.error) {
										error('提示', msgObj.error);
									} else {
										estfailure();
									}
								}
							},
							failure :estfailure
						});
 			}
	},
	wrapChangedRows:function(){
		//alert("wrapChangedRows");
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
					if (cm.getColumnById('numberer')) {
						continue;
					}
					if (cm.getColumnById(i) === undefined) {
						continue;
					}
					var columnName = cm.getColumnById(i).name;
					var columnValue = (record.data[columnName] === undefined
							? ""
							: record.data[columnName]);
					
					if(columnValue === null){
							columnValue = '';
					 }
							
					// alert("columnName="+columnName+"\n"+"columnValue="+columnValue);
					var editor = cm.getCellEditor(i, rowIndex);
					var forceValidate = cm.getColumnById(i).forceValidate;
					if (forceValidate === undefined) {
						forceValidate = true;
					}
					// 如果列有验证，进行验证
					if (record['modifystatus'] !== 'd' && editor
							&& forceValidate) {
						// alert(i);
						// alert("modifystatus="+record['modifystatus']+"\n"+"forceValidate="+forceValidate+"\n"+"\n"+columnName+"\n"+"columnValue="+columnValue);
						// alert(editor.field.validateValue(columnValue))
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
			//var data = encodeURIComponent(Ext.encode(changedRows));
			
		} else {
			Ext.Msg.alert('提示', '请检查输入！', function() {
						this.startEditing(validateFlag.row, validateFlag.col);
					}, this);
		}
		
		return changedRows;
		
	},
 onValidate : function(/* 保存地址 */saveUrl,/* 回调方法 */callbackFn,other_params) {
 			var changedRows = this.wrapChangedRows();
 			if(changedRows &&  changedRows.length>0){
 					var data = encodeURIComponent(Ext.encode(changedRows));
 					var _params = {data : data};
					if (other_params) {
						if (other_params.data != undefined) {
							Ext.Msg.alert('提示', '数据参数名不能为data');
							return;
						}
						Ext.apply(_params, other_params);
					}
					Ext.Ajax.request({
								url : saveUrl,
								params :_params,
								waitMsg : '正在保存...',
								success : function(rep) {
									var msgObj = Ext.decode(rep.responseText);
									if (msgObj.success) {
										if (callbackFn) {
											callbackFn(rep);
										}
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
 			}
	},

	/* workflow submit slaverytable */
	onWFSubmitSlavery : function(/* 保存地址 */saveUrl,/* 回调方法 */callbackFn) {
		var changedRows = [];
		var store = this.getStore();
		var cm = this.getColumnModel();
		var recordId = this.recordId;
		this.stopEditing();// 停止编辑
		var validateFlag = {
			validate : true,
			row : 0,
			col : 0
		};
		store.each(function(record) {
					if (record['modifystatus']) { // 得到所有修改过的数据
						error('提示', '存在已修改数据，请先保存！');
						return;
					} else {
						for (var key in record.data) {
							if (key == 'est-grid-checkbox') {
								if (record.data[key] === true) {
									var tmpobj = {
										'modifystatus' : 'm'
									};
									tmpobj[recordId] = record.data[recordId]
									changedRows.push(tmpobj);
								};
							}
						}
					}
				});
		if (changedRows.length == 0) {
			Ext.Msg.alert('提示', '没有勾选任何记录！');
			return;
		}

		var data = encodeURIComponent(Ext.encode(changedRows));
		Est.syncRequest({
					url : saveUrl,
					params : {
						data : data
					},
					waitMsg : '正在保存...',
					success : function(rep) {
						var msgObj = Ext.decode(rep.responseText);
						if (msgObj.success) {

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

Ext.reg('esteditgrid', Est.ux.EditableGrid);

/*
 * Ext.grid.CheckColumn = function(config) { Ext.apply(this, config); if
 * (!this.id) { this.id = Ext.id(); }
 * 
 * this.renderer = this.renderer.createDelegate(this); };
 * 
 * 
 * Ext.grid.CheckColumn.prototype = { init : function(grid) { this.grid = grid;
 * this.grid.on('render', function() { var view = this.grid.getView();
 * view.mainBody.on('mousedown', this.onMouseDown, this); }, this); },
 * onMouseDown : function(e, t) { if (t.className &&
 * t.className.indexOf('x-grid3-cc-' + this.id) != -1) { e.stopEvent(); var
 * index = this.grid.getView().findRowIndex(t); var record =
 * this.grid.store.getAt(index); record.set(this.dataIndex,
 * !record.data[this.dataIndex]); } }, renderer : function(v, p, record) { p.css += '
 * x-grid3-check-col-td'; return '<div class="x-grid3-check-col' + (v ? '-on' :
 * '') + ' x-grid3-cc-' + this.id + '">&#160;</div>'; } };
 */
