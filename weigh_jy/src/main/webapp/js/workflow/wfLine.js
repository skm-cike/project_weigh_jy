/**
 * WfComponent
 * 工作流流程组件对象基类
 * @author jingpj
 * @version 1.0
 *
 */

WfLine = function(cfg){
	var self = this;
	this.type = 'line';
	
	this.id = cfg.id;
	this.x = cfg.x;
	this.y = cfg.y;
	this.height = cfg.height;
	this.width = cfg.width;
	

	
	this.from = cfg.from;
	this.to = cfg.to;
	
	cfg.from = this.from.x + ',' + this.from.y;
	cfg.to = this.to.x + ',' + this.to.y;
	
	this.node = cfg.node;
	
	
	var template = new Ext.Template(
	    '<v:line id="{id}" from={from} to={to} style="position:absolute;">',
			 '<v:stroke StartArrow="none" EndArrow="Classic"/>',
		'</v:line>'
	);
	
	
	template.append(document.body, cfg);
	
	this.object = this.getDom();
	this.object.onclick = function(){
		
		
		if(type == 'select') {
  			if(selectedObject != self){
  				selectObject(self);
  			} else {
  				selectObject();
  			}
  		}
	}
};



WfLine.prototype.toString = function(){
	 return ""; 
};

WfLine.prototype.toXMLString = function(){
	var buf = new Est.StringBuffer();
	buf.append('<transition');
	buf.append(' to="');
	buf.append(WfNode.prototype.getNodeById(this.node).name);
	buf.append('"></transition>');
	return buf.toString(); 
};


WfLine.prototype.makeJSON = function(){
	var json = {id:this.id,node:this.node,from:this.from,to:this.to};
	/*
	var from = this.getDom().from.split(',');
	json.from.x = from[0];
	json.from.y = from[1];
	var to = this.getDom().to.split(',');
	json.to.x = to[0];
	json.to.y = to[1];
	*/
	return json;
} 

WfLine.prototype.getDom = function(){
	 return document.getElementById(this.id); 
};

WfLine.prototype.redraw = function(){
	 return document.getElementById(this.id); 
};

