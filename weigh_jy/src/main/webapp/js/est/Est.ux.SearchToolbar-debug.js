/*
 * Ext JS Library 2.2.1 Copyright(c) 2006-2009, Ext JS, LLC. licensing@extjs.com
 * 
 * http://extjs.com/license
 */

/**
 * @class Est.ux.SearchToolbar
 * @extends Ext.BoxComponent Basic Toolbar class. Toolbar elements can be
 *          created explicitly via their constructors, or implicitly via their
 *          xtypes. Some items also have shortcut strings for creation.
 * @constructor Creates a new Toolbar
 * @param {Object/Array}
 *            config A config object or an array of buttons to add
 */
Est.ux.SearchToolbar = function(cfg) {
	if (Ext.isArray(cfg)) {
		config = {
			buttons : cfg
		};
	}

	this.searchFields = cfg.searchFields;
	this.afterSearch = cfg.afterSearch;
	this.searchMapping = cfg.searchMapping;
	this.gridId = cfg.gridId;
	this.searchAdditionalCondition = {};
	this.searchConditions = {};
	this._preInit(cfg);

	Est.ux.SearchToolbar.superclass.constructor.call(this, cfg);
};

Ext.extend(Est.ux.SearchToolbar, Est.ux.CustomToolbar, {
			_preInit : function(cfg) {
				this.searchFields.push('-');
				this.searchFields.push({
							text : '查询',
							id : 'est_SearchButt',
							handler : this.doSearch,
							scope : this
						});
				this.searchFields.push({
							text : '重置',
							id : 'est_ResetButt',
							handler : function() {
								for (var key in this.searchMapping) {
									if (Ext.getCmp(key)['notReset']) {
										continue;
									}
									Ext.getCmp(key).reset();
								}
							},
							scope : this
						});
				this.items = this.searchFields;

			},
			setAdditionalConditions : function(conditions) {
				Ext.apply(this.searchAdditionalCondition, conditions);
			},
			removeAddtinalCondition : function(key) {
				this.searchAdditionalCondition[key] = undefined;
			},
			removeAllAddtinalConditions : function() {
				this.searchAdditionalCondition = {};
			},
			reset: function() {
				for (var key in this.searchMapping) {
									if (Ext.getCmp(key)['notReset']) {
										continue;
									}
									Ext.getCmp(key).reset();
				}
			},
			doSearch : function() {

				for (var key in this.searchMapping) {
					var field = Ext.getCmp(key);
					var value = field.getValue();
					
					if (field.isXType('datefield')) {
						if (field.format && field.applyformat=='Y') {
							value = Ext.util.Format.date(value, field.format);
						} else {
							value = field.isXType('datefield')
									? Ext.util.Format.date(value, 'Y-m-d')
									: value;
						}
					}

					this.searchConditions[this.searchMapping[key]] = value;
				}

				Ext.apply(this.searchConditions,
								this.searchAdditionalCondition);
				Ext.getCmp(this.gridId).doSearch(this.searchConditions, true);
				if (this.afterSearch) {
					this.afterSearch();
				}
			},
			setSearchButtonDisable : function() {
				Ext.getCmp('est_SearchButt').disable();
				Ext.getCmp('est_ResetButt').disable();
			},
			setSearchButtonEnable : function() {
				Ext.getCmp('est_SearchButt').enable();
				Ext.getCmp('est_ResetButt').enable();
			},
			getSearchConditions : function() {
				for (var key in this.searchMapping) {
					var field = Ext.getCmp(key);
					var value = field.getValue();
					if (field.isXType('datefield')) {
						if (field.format && field.applyformat=='Y') {
							value = Ext.util.Format.date(value, field.format);
						} else {
							value = field.isXType('datefield')
									? Ext.util.Format.date(value, 'Y-m-d')
									: value;
						}
					}

					this.searchConditions[this.searchMapping[key]] = value;
				}
				Ext
						.apply(this.searchConditions,
								this.searchAdditionalCondition);
				return this.searchConditions;
			}

		});
Ext.reg('searchtoolbar', Est.ux.SearchToolbar);
