Ext.ns('Est');

Ext.BLANK_IMAGE_URL = 'js/ext/resources/images/default/s.gif';

/** fix the bug of submenu-clicking cause all menu hide * */
Ext.override(Ext.menu.Menu, {
			// private
			onClick : function(e) {
				var t;
				if (t = this.findTargetItem(e)) {
					if (t.menu && this.ignoreParentClicks) {
						t.expandMenu();
					} else {
						if (t.menu) {
							if (t.handler) {
								t.onClick(e);
								e.stopEvent();
							}
							e.stopEvent();
						} else {
							t.onClick(e);
							this.fireEvent("click", this, t, e);
						}
					}
				}
			}
		});

Ext.override(Ext.data.Connection, {

			// private
			handleResponse : function(response) {
				this.transId = false;
				var options = response.argument.options;
				response.argument = options ? options.argument : null;
				this.fireEvent("requestcomplete", this, response, options);

				try {
					var responseJson = Ext.decode(response.responseText);
					if (responseJson.isLogin === undefined
							|| responseJson.isLogin === true) {
						Ext.callback(options.success, options.scope, [response,
										options]);
						Ext.callback(options.callback, options.scope, [options,
										true, response]);
					} else {
						response.status = 500;
						Ext.callback(options.success, options.scope, [response,
										options]);
						Ext.callback(options.callback, options.scope, [options,
										true, response]);

						var loginWin = Ext.getCmp('loginwin');
						if (!loginWin) {
							loginWin = new Est.ux.LoginWin({
										id : 'loginwin',
										login : curUser
									});
						}
						loginWin.show();
						error("错误", "您的连接已超时断开，请重新登录！");
					}
				} catch (e) {
					Ext.callback(options.success, options.scope, [response,
									options]);
					Ext.callback(options.callback, options.scope, [options,
									true, response]);
				}
			}
		});

Ext.apply(Ext.util.Format, {

			// private
			formatMoney : function(v, prefix, s) {
				var _prefix = prefix ? prefix : '￥';

				s = s == undefined ? 100 : s;
				v = (Math.round((v - 0) * s)) / s;
				v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v
						* 10)) ? v + "0" : v);

				v = String(v);
				var ps = v.split('.');
				var whole = ps[0];
				var sub = ps[1] ? '.' + ps[1] : '.00';
				var r = /(\d+)(\d{3})/;
				while (r.test(whole)) {
					whole = whole.replace(r, '$1' + ',' + '$2');
				}
				v = whole + sub;
				if (v.charAt(0) == '-') {
					return '-' + _prefix + v.substr(1);
				}
				return _prefix + v;
			}
		});

Ext.override(Ext.data.JsonReader, {

	readRecords : function(o) {
		/**
		 * After any data loads, the raw JSON data is available for further
		 * custom processing. If no data is loaded or there is a load exception
		 * this property will be undefined.
		 * 
		 * @type Object
		 */
		this.jsonData = o;
		if (o.metaData) {
			delete this.ef;
			this.meta = o.metaData;
			this.recordType = Ext.data.Record.create(o.metaData.fields);
			this.onMetaChange(this.meta, this.recordType, o);
		}
		var s = this.meta, Record = this.recordType, f = Record.prototype.fields, fi = f.items, fl = f.length;

		// Generate extraction functions for the totalProperty, the root, the
		// id, and for each field
		if (!this.ef) {
			if (s.totalProperty) {
				this.getTotal = this.getJsonAccessor(s.totalProperty);
			}
			if (s.successProperty) {
				this.getSuccess = this.getJsonAccessor(s.successProperty);
			}
			this.getRoot = s.root ? this.getJsonAccessor(s.root) : function(p) {
				return p;
			};
			if (s.id) {
				var g = this.getJsonAccessor(s.id);
				this.getId = function(rec) {
					var r = g(rec);
					return (r === undefined || r === "") ? null : r;
				};
			} else {
				this.getId = function() {
					return null;
				};
			}
			this.ef = [];
			for (var i = 0; i < fl; i += 1) {
				f = fi[i];
				var map = (f.mapping !== undefined && f.mapping !== null)
						? f.mapping
						: f.name;
				this.ef[i] = this.getJsonAccessor(map);
			}
		}

		var root = this.getRoot(o), c = root.length, totalRecords = c, success = true;
		if (s.totalProperty) {
			var v = parseInt(this.getTotal(o), 10);
			if (!isNaN(v)) {
				totalRecords = v;
			}
		}
		if (s.successProperty) {
			var v = this.getSuccess(o);
			if (v === false || v === 'false') {
				success = false;
			}
		}
		var records = [];
		for (var i = 0; i < c; i += 1) {
			var n = root[i];
			var values = {};
			var id = this.getId(n);
			for (var j = 0; j < fl; j += 1) {
				try {
					f = fi[j];
					var v = this.ef[j](n);
					values[f.name] = f.convert((v !== undefined)
									? v
									: f.defaultValue, n);
				} catch (e) {
					values[f.name] = f.convert('', n);
				}
			}
			var record = new Record(values, id);
			record.json = n;
			records[i] = record;
		}
		return {
			success : success,
			records : records,
			totalRecords : totalRecords
		};
	}
});

Est.syncRequest = function(cfg) {
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", cfg.url, false);
	conn.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded;charset=UTF-8");
	conn.send(Ext.urlEncode(cfg.params));
	var responseJson = Ext.util.JSON.decode(conn.responseText);
	if (responseJson.success && cfg.success) {
		cfg.success(conn);
	} else {
		if (cfg.failure) {
			cfg.failure(conn);
		}
	}
}

Est.Msg = function() {
	var msgCt;

	function createBox(t, s) {
		return [
				'<div class="msg">',
				'<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
				'<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>',
				t,
				'</h3>',
				s,
				'</div></div></div>',
				'<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
				'</div>'].join('');
	}
	return {
		msg : function(title, format) {
			if (!msgCt) {
				msgCt = Ext.DomHelper.insertFirst(document.body, {
							id : 'msg-div'
						}, true);
			}
			msgCt.alignTo(document, 'tr-tr'); // show in top right corner
			var s = String.format.apply(String, Array.prototype.slice.call(
							arguments, 1));
			var m = Ext.DomHelper.append(msgCt, {
						html : createBox(title, s)
					}, true);
			m.slideIn('t').pause(2).ghost("t", {
						remove : true
					});
		},

		info : function(title, format) {
			Est.Msg.msg(title, '<font color=green>' + format + '</font>')
		},

		error : function(title, format) {
			Est.Msg.msg(title, '<font color=red>' + format + '</font>')
		},

		init : function() {
			var t = Ext.get('exttheme');
			if (!t) { // run locally?
				return;
			}
			var theme = Cookies.get('exttheme') || 'aero';
			if (theme) {
				t.dom.value = theme;
				Ext.getBody().addClass('x-' + theme);
			}
			t.on('change', function() {
						Cookies.set('exttheme', t.getValue());
						setTimeout(function() {
									window.location.reload();
								}, 250);
					});

			var lb = Ext.get('lib-bar');
			if (lb) {
				lb.show();
			}
		}
	};
}();

Ext.onReady(Est.Msg.init, Est.Msg);

var msg = Est.Msg.msg;
var info = Est.Msg.info;
var error = Est.Msg.error;

/* convert colstype to Ext type */
function convertColStype(col, coltype) {
	col.fieldLabel = coltype.label;
	col.name = coltype.name;
	coltype.type ? col.vtype = coltype.type : void(0);
	coltype.xtype ? col.xtype = coltype.xtype : col.xtype = 'textfield';
	coltype.inputType ? col.inputType = 'password' : void(0);
	coltype.require ? col.allowBlank = false : void(0);
	col.anchor = coltype.anchor ? coltype.anchor : '95%';
}
if (typeof console == 'undefined') {
	console = {};
	console.log = Ext.emptyFn;
}

function forin(obj) {
	var tmp;
	for (var key in obj) {
		tmp += key + ":" + obj[key] + ',\t';
	}
	return tmp;
};

Array.prototype.insertAt = function(index, value) {
	return this.splice(index, 0, value);

};
Array.prototype.removeAt = function(index) {
	return this.splice(index, 1);
}

/* est ajax error method */
function estfailure() {
	error('错误', '错误');
}

function showMsg(msg) {
	info('提示', msg);
}
/**
 * 账务开始结束月
 * 
 * @param {}
 *            year
 * @param {}
 *            month
 * @param {}
 *            beginDateField
 * @param {}
 *            endDateField
 */
function setAcounttingBeginAndEndDate(year, month, beginDateField, endDateField) {

	if (!isNaN(month)) {
		var dt1, dt2;
		month = parseInt(month);
		var beginMonth;
		var day = '26';
		if (month == 1) {
			beginMonth = month;
			day = '01';
		} else {
			beginMonth = month - 1;
		}

		if (beginMonth < 10) {
			beginMonth = "0" + beginMonth;
		}
		dt1 = Date.parseDate(year + "-" + beginMonth + "-" + day, "Y-m-d");

		if (month == 12) {
			dt2 = Date.parseDate(year + "-" + month + "-31", "Y-m-d");
		} else {
			if (month < 10) {
				month = "0" + month;
			}
			dt2 = Date.parseDate(year + "-" + month + "-25", "Y-m-d");
		}
		Ext.getCmp(beginDateField).setValue(dt1);
		Ext.getCmp(endDateField).setValue(dt2);
	}

}
/**
 * 自然开始结束月
 * 
 * @param {}
 *            year
 * @param {}
 *            month
 * @param {}
 *            beginDateField
 * @param {}
 *            endDateField
 */
function setNormalgBeginAndEndDate(year, month, beginDateField, endDateField) {
	if (!isNaN(month) && !isNaN(year)) {
		var dt1, dt2;
		month = parseInt(month);
		year = parseInt(month);
		var beginMonth;
		var day = '28';

		if (month <= 8) {
			if (month == 7 || month == 8) {
				day = '31';
			} else if (month % 2 == 0) {
				day = '30'
			} else {
				day = '31';
			}
		} else {
			if (month % 2 == 0) {
				day = '31'
			} else {
				day = '30';
			}
		}
		if (year % 4 == 0 && month == 2) {
			day = '29';
		}
		if (month < 10) {
			month = "0" + month;
		}
		// alert(day);
		dt1 = Date.parseDate(year + "-" + month + "-01", "Y-m-d");
		dt2 = Date.parseDate(year + "-" + month + "-" + day, "Y-m-d");

		Ext.getCmp(beginDateField).setValue(dt1);
		Ext.getCmp(endDateField).setValue(dt2);
	}
}
Est.StringBuffer = function() {
	var buf = [];

	this.reset = function() {
		buf = [];
	};

	this.append = function(p) {
		buf.push(typeof p === 'string' ? p : p.toString());
	};

	this.toString = function() {
		return buf.join("");
	};
}

Est.Stack = function() {
	var buf = [];
	this.push = function(p) {
		buf.push(p);
	}
	this.pop = function() {
		return buf.pop();
	}
	this.reset = function() {
		buf = [];
	}
	this.length = function() {
		return buf.length;
	}
}

Est.Map = function() {
	var buf = {};
	this.set = function(k, v) {
		buf[k] = v;
	}
	this.get = function(k) {
		return buf[k];
	}
}
