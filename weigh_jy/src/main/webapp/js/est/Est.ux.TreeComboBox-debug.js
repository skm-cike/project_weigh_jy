Ext.namespace('Est.ux');
/**
 * Est.ux.Tree Extension Class
 * 
 * @author jingpj
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



Ext.ux.ComboBoxTree = function(cfg){
	
	
	this.treeId = Ext.id()+'-tree';
	
	this.maxHeight = cfg.maxHeight || cfg.height || this.maxHeight;
	
	this.tpl = new Ext.Template('<tpl for="."><div style="height:'+(this.maxHeight-6)+'px"><div id="'+this.treeId+'"></div></div></tpl>');
	
	this.store = new Ext.data.SimpleStore({fields:[],data:[[]]});
	
	this.selectedClass = '';
	
	this.mode = 'local';
	
	this.triggerAction = 'all';
	
	this.onSelect = Ext.emptyFn;
	
	this.editable = false;
	
	this.resizable = false;
	this.setText=cfg.setText!==undefined ? cfg.setText : false;
	
	this.alwaysReload = cfg.alwaysReload!==undefined ? cfg.alwaysReload : false;
	
	
	this.tree = new Ext.tree.TreePanel({
						  id: this.treeId,
						  width: cfg.treeWidth-6,   
			              loader: new Ext.tree.TreeLoader({dataUrl:cfg.dataUrl}),   
			              root : new Ext.tree.AsyncTreeNode({id:cfg.rootId,text:cfg.rootText}) 
			    });
	
	/*selectNodeModel:
	*	all:所有结点都可选中
	*	exceptRoot：除根结点，其它结点都可选（默认）
	*	folder:只有目录（非叶子和非根结点）可选
	*	leaf：只有叶子结点可选
	*/
	this.selectNodeModel = cfg.selectNodeModel || 'all';
	
	this.addEvents('afterchange');

	Ext.ux.ComboBoxTree.superclass.constructor.apply(this, arguments);

}

Ext.extend(Ext.ux.ComboBoxTree,Ext.form.ComboBox, {

	expand : function(){
		Ext.ux.ComboBoxTree.superclass.expand.call(this);
		if(this.tree.rendered){
			return;
		}

		Ext.apply(this.tree,{height:this.maxHeight-6, border:false, autoScroll:true});
		if(this.tree.xtype){
			this.tree = Ext.ComponentMgr.create(this.tree, this.tree.xtype);
		}
		this.tree.render(this.treeId);
		
		var root = this.tree.getRootNode();
		//if(!root.isLoaded())
		root.reload();

		this.tree.on('click',function(node){
			var selModel = this.selectNodeModel;
			var isLeaf = node.isLeaf();
			
			if((node == root) && selModel != 'all'){
				return;
			}else if(selModel=='folder' && isLeaf){
				return;
			}else if(selModel=='leaf' && !isLeaf){
				return;
			}
			
			var oldNode = this.getNode();
			if(this.fireEvent('beforeselect', this, node, oldNode) !== false) {
				this.setValue(node);
				this.collapse();

				this.fireEvent('select', this, node, oldNode);
				(oldNode !== node) ? this.fireEvent('afterchange', this, node, oldNode) : '';
			}
		}, this);
    },
    
    onTriggerClick:function(e){
    	var rootNode = Ext.getCmp(this.treeId).root
    	if(this.alwaysReload && rootNode){
    		rootNode.reload();
    	}
    	Ext.ux.ComboBoxTree.superclass.onTriggerClick.call(this, e);
    	
    },
    
	setValue : function(node){
		this.node = node;
        var text = node.text;
        this.lastSelectionText = text;
        if(this.hiddenField){
            this.hiddenField.value = node.id;
        }
        Ext.form.ComboBox.superclass.setValue.call(this, text);
        if(this.setText){
        	this.value = text;
        }else{
        	this.value = node.id;
        }
    },
    
    getValue : function(){
    	return typeof this.value != 'undefined' ? this.value : '';
    },

	getNode : function(){
		return this.node;
	},

	clearValue : function(){
		Ext.ux.ComboBoxTree.superclass.clearValue.call(this);
        this.node = null;
    },
	
	
    
	// private
    destroy: function() {
		Ext.ux.ComboBoxTree.superclass.destroy.call(this);
		Ext.destroy([this.node,this.tree]);
		delete this.node;
    }
    
});

Ext.reg('esttreecombo', Ext.ux.ComboBoxTree);
