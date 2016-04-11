Ext.namespace('Est.ux');
/**
 * Est.ux.Tree Extension Class
 * 
 * @author Smile_BuG
 * @version 1.0
 * 
 * @class Est.ux.Tree
 * @extends Ext.tree.TreePanel
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 * @cfg {String} loaderurl TreeLoad's url require
 * @cfg {Boolean} isctx contextMenu
 * @cfg {Object} ctx, if isctx is true,it's required For Example
 * 
 * {url: 'edittree.do', ctxitems:[{id:'modify',text:'Modify'},{id:'delete',
 * text:'Delete'},{id:'cus',text:'Custom',fun:'test'}], formitems:
 * [{id:'userId', fieldset:'Login',
 * items:[{fieldLabel:'UserName',name:'name',allowBlank:
 * true},{fieldLabel:'Password',name:'pwd',allowBlank: true}]}]}
 */
Est.ux.Tree = function(cfg) {

	this.border = false;
	this.root = new Ext.tree.AsyncTreeNode({id:'0', text:cfg.rootTxt?cfg.rootTxt:'所有'});
	this.rootVisible = true;
	this.useArrows = true;
	this.width = 200;
	this.autoScroll = true;
	
	this.parentFieldOfNewNode = cfg.parentFieldOfNewNode;	//修改树form中存放父node的id字段id名

	this.split = true;
	

	cfg.maxSize ? void (0) : cfg.maxSize = 250;
	cfg.minSize ? void (0) : cfg.minSize = 150;
	cfg.collapsible === false ? void (0) : cfg.collapsible = true;
	cfg.collapseMode ? void (0) : cfg.collapseMode = 'mini';
	
	this.layoutConfig = {
		animate : true,
		activeOndTop : false
	};
	this.loader = new Ext.tree.TreeLoader( {
		baseParams : {
			_r : new Date().getTime()
		},
		listeners : cfg.loaderlisteners?cfg.loaderlisteners:{},
		url : cfg.loaderurl
	});
	/*Ext.dump(this.menuItems);*/
	Est.ux.Tree.superclass.constructor.call(this, cfg);
	
	
	/* contextMenu */
	this.menuItems = [];
	if (cfg.ctx) {
		this.ctxitems = cfg.ctx.ctxitems;
		/* remote url, default is loaderurl */
		this.ctxurl = cfg.ctx.url || cfg.loaderurl;
		this.formitems = cfg.ctx.formitems;
		this.popWin = null;
		this.method = cfg.ctx.method;
		
		this._ctxInit(cfg.ctx);
	};


	/* contextMenu */
	if (cfg.isctx) {
		this.on( {
			contextmenu : {
				scope : this,
				fn : function(node, e) {
					var c = node.getOwnerTree().contextMenu();
					c.contextNode = node;
					c.showAt(e.getXY());
				}
			}
		});
		
		this.beforeDelete = cfg.ctx.beforeDelete || null;
		this.afterDelete = cfg.ctx.afterDelete || null;
		
		this.beforeAdd = cfg.ctx.beforeAdd || null;
		this.afterAdd = cfg.ctx.afterAdd || null;
		
		this.beforeModify = cfg.ctx.beforeModify || null;
		this.afterModify = cfg.ctx.afterModify || null;
		
		this.beforeDefHandler = cfg.ctx.beforeDefHandler || null;
	    this.defHandler = cfg.ctx.defHandler || null;
	    this.afterDefHandler = cfg.ctx.afterDefHandler || null;
		
	}
};

Ext.extend(Est.ux.Tree, Ext.tree.TreePanel, {
	/*context init*/
	_ctxInit : function(ctx) {
		this._initPop(this.ctxurl, this.formitems);
		this._initMenu(this.ctxurl, this.ctxitems);
		/*this._initMethod(ctx);*/
	},
	/*init pop window*/
	_initPop : function(url, items) {
		var formId = this.id + 'popform';
		var winId = this.id + 'win';
		var tree = this;
		var method = this.method;
		
		this.popWin = new Ext.Window({
			closeAction: 'hide',
			id: winId,
			minWidth: 340,
			width: 340,
			/*autoWidth: true,*/
			items : [new Est.ux.Form({
				colstype : items,
				formurl : url,
				id: formId,
				method: method,
				colnum: 1,
				xtype : 'estform'
			})],
			buttons: [{id:'savebutton',text:'保存', handler:function() {
							Ext.getCmp(formId).doSumbit();
							Ext.getCmp(formId).form.on({'actioncomplete':function(form){
								console.log('actioncomplete');
								tree.getPop().hide();
								tree.root.reload();
								
								Ext.getCmp(formId).form.purgeListeners();
								//scope.reloadTreeCombo();
								
							}});
					},scope:this},
			          {text:'关闭', handler:function(){
							Ext.getCmp(winId).hide();
					}}]
		})
	},
	
	


	/*init contextMenu*/
	_initMenu : function(url, items) {
		for ( var i = 0; i < items.length; i++) {
			this.menuItems[i] = {
				id : items[i].id,
				text : items[i].text
			};
		};
		/* this.contextMenu.items = this.menuItems[i]; */
	},
	
	/**
	 * get node's qtip value
	 * @param {} node
	 */
	getQtip:function(node){
			 return node.attributes.qtip;		
	},

/*	_initMethod: function(ctx) {
		!ctx.getmethod?this.getmethod='get'+ctx.method:this.getmethod=ctx.getmethod;
		!ctx.savmethod?this.savmethod='sav'+ctx.method:this.savmethod=ctx.savmethod;
		!ctx.delmethod?this.delmethod='del'+ctx.method:this.delmethod=ctx.delmethod;
	},*/
	/*get pop window*/
	getPop : function() {
		return this.popWin;
	},
	
	getPopForm : function() {
		return Ext.getCmp(this.id + 'popform');
	},
	
	
	reloadTreeCombo: function(){
		var parentfield = Ext.getCmp(this.parentFieldOfNewNode);
		if(parentfield !== undefined && parentfield.xtype === 'esttreecombo'){
			alert(parentfield.tree.xtype);
		}
	},

	/*ctx menu default method*/
	_ctxDefault : function(item, n, p, scope) {
		/*Ext.dump(scope.getPop());*/
		var nId = n.id;
		var idname = scope.formitems[0].id;
		switch (item.id) {
		case 'delete':
			if(scope.beforeDelete!=null && !scope.beforeDelete(nId)){
				break;
			}
			
			if(this.root == n) {
				return ;
			}
			Ext.Ajax.request({
				url: scope.ctxurl+'/del'+scope.method,
				params: eval('({'+idname+': '+nId+'})'),
				success: function(response, options){
					var responseJson = Ext.util.JSON.decode(response.responseText);
					if(responseJson.success){
						if (n.parentNode) {
							n.remove();
						}
						
						if(scope.afterDelete){
							scope.afterDelete();
						}
							
					} else {
                        var errorStr=responseJson.error;
                        if(errorStr){
                           error('提示',errorStr);
                        }else{
                           estfailure();
                        }
					}
					//scope.reloadTreeCombo();
				},
				failure: estfailure
			});
			
			break;
		case 'add':
			if(scope.beforeAdd!=null && !scope.beforeAdd(nId)){break;}
			scope.getPop().show();
			scope.getPopForm().doReset();
			var parentfield = Ext.getCmp(this.parentFieldOfNewNode);
			if(parentfield !== undefined){
				if(parentfield.xtype === 'esttreecombo') {
					parentfield.setValue(n);
				} else {
					parentfield.setValue(n.id);
				}
			}
			if(scope.afterAdd!=null){
				var responsejson = {}
				Ext.Ajax.request({
					url:scope.getPopForm().formurl+'/'+scope.getPopForm().getmethod,
					params:eval('({'+idname+': "'+nId+'"})'),
					success: function(response ,options){
						responsejson = Ext.decode(response.responseText);
						scope.afterAdd(scope.getPopForm().form,responsejson.data);
					}
				});
			}
			break;
		case 'modify':
			if(scope.beforeModify!=null && !scope.beforeModify(nId)){break;}
			//scope.getPop().show();
			if(this.root == n) {
				return ;
			}
			scope.getPopForm().doLoad(eval('({'+idname+': '+nId+'})'));
			scope.getPop().show();
			if(scope.afterModify!=null){
				var responsejson = {}
				Ext.Ajax.request({
					url:scope.getPopForm().formurl+'/'+scope.getPopForm().getmethod,
					params:eval('({'+idname+': "'+nId+'"})'),
					success: function(response ,options){
						responsejson = Ext.decode(response.responseText);
						scope.afterModify(scope.getPopForm().form,responsejson.data);
					}
				});
			}
			break;
		case 'customer':
			if(scope.beforeDefHandler != null){}
			if(scope.defHandler){scope.defHandler(n);}
			if(scope.afterDefHandler != null){};
			break;
		default:
			{	
				if(item.fun){
					item.fun(n);				
				}
				console.log('err:esttree, no such method' + item.id + ' for' + n);
				break;
			}
		}
	},
	contextMenu : function() {
		var menuItems = this.menuItems;
		var ctxitems = this.ctxitems;
		var scope = this;
		return new Ext.menu.Menu( {
			items : menuItems,
			
			listeners : {
				itemclick : function(item) {
					/* current node */
					var n = item.parentMenu.contextNode;
					/* parent node */
					var p = item.parentNode;
					for ( var i = 0; i < ctxitems.length; i++) {
						var ctxitem = ctxitems[i];
						if (item.id == ctxitem.id) {
							if (typeof ctxitem.fun === 'function') {
								/*call custom function*/
								ctxitem.fun.call(this, n, p);
							} else if (typeof ctxitem.fun === 'string') {
								eval(window[ctxitem.fun](n, p));
							} else {
								scope._ctxDefault(item, n, p, scope);
							}
						}
					}
				}
			}
		})
	}
});

Ext.reg('esttree', Est.ux.Tree);
