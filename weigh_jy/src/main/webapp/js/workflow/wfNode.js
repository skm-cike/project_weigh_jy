/**
 * WfComponent
 * 工作流流程组件对象基类
 * @author jingpj
 * @version 1.0
 *
 */
var NodeConfig = {
						'node':{'minin':1,'minout':1,'mininerror':'当前节点应该有且只有一个输入节点','minouterror':'当前节点应该有且只有一个输出节点'},
						'start':{'in':0,'out':1,'inerror':'开始节点不能有一个输入','outerror':'开始节点只能有一个输出'},
						'end':{'minin':1,'out':0,'mininerror':'结束节点应该有不少于一个输入节点','outerror':'结束节点不能有输入'},
						'fork':{'in':1,'minout':2,'inerror':'当前节点应该有且只有一个输入节点','minouterror':'当前节点不能少于2个输出'},
						'join':{'minin':2,'out':1,'mininerror':'当前节点不能少于2个输出','outerror':'当前节点应该有且只有一个输出节点'},
						'condition':{'minin':1,'minout':1,'mininerror':'当前节点不能少于2个输出','outerror':'当前节点应该有且只有一个输出节点'},
						'countersign':{'minin':1,'minout':1,'mininerror':'当前节点不能少于2个输出','outerror':'当前节点应该有且只有一个输出节点'}
				};

function WfNode(cfg){
	var self = this;
	this.propertiesList = ['id','type','name','x','y','width','height'];
	cfg.type = cfg.type || 'node';
	this.type = cfg.type;
	cfg.x = cfg.x || 0;
	cfg.y = cfg.y || 0;
	this.x = cfg.x;
	this.y = cfg.y;
	cfg.height = cfg.height || 30;
	this.height = cfg.height;
	cfg.width = cfg.width || 120;
	this.width = cfg.width;
	this.name = cfg.name || '';
	
	//cfg.id = cfg.id ||  Ext.getId();
	this.id = cfg.id ;
	
	this.linein ;
	
	
	cfg.toNodes = cfg.toNodes || [];
	
	this.Lines = this.initLines(cfg.toNodes) ;
	
	//移动前的位置
	this.orginPostion = {x:0,y:0};
	
	
	var template = new Ext.Template(
	    "<v:RoundRect id='{id}' style='position:absolute;width:{width};height:{height};color:red;left:{x};top:{y};margin:'5px;'  fillcolor='#efd'>",
		   	'<v:shadow on="T" type="single" color="#777" offset="1px,1px"/>',
			'<v:fill type="gradient" color="white" color2="#efd" />',
			'<img src="/fuel_jt/img/workflow/{type}.gif"  style="position:absolute;top:5px;left:5px;"/>',
			'<v:TextBox id="text{id}" inset="15pt,5pt,5pt,5pt" style="font-size:9pt;font-bold:true">{name}</v:TextBox>',
		'</v:RoundRect>'
	);
	
	template.append(document.body, cfg);
	
	this.ddproxy  = new Ext.dd.DDProxy(this.id);
	this.ddproxy.lock();
	
	this.object = this.getDom();
	
	
	var drawLines = function(x,y){
		
		
		//修改连接
		for(var i=0;i<self.Lines.length;i+=1) {
			
			var line = self.Lines[i];
			var linedom =  line.getDom();
			for(var j=0;j<CANVAS.nodes.length;j+=1){
				var n = CANVAS.nodes[j];
				if(line.node === n.id){
					var nodedom = n.getDom();
					var nodedom_left = parseInt(nodedom.style.left.replace('px',''));
					var nodedom_width = parseInt(nodedom.style.width.replace('px',''));
					var nodedom_top = parseInt(nodedom.style.top.replace('px',''));
					var nodedom_height = parseInt(nodedom.style.height.replace('px',''));
					
					var fx,fy,tx,ty;
					if(nodedom_left + nodedom_width <= x ) {
						tx = nodedom_left + nodedom_width;
						fx = x;
					} else if(nodedom_left >= x + self.width ) {
						tx = nodedom_left;
						fx = x + self.width;
					} else {
						tx = nodedom_left + nodedom_width/2;
						fx = x + self.width/2;
					}
					
					if(nodedom_top + nodedom_height <= y ) {
						ty = nodedom_top + nodedom_height- offset_top;
						fy = y- offset_top;
					} else if(nodedom_top >= y + self.height){
						ty = nodedom_top- offset_top;
						fy = y + self.height- offset_top;
					} else {
						ty = nodedom_top + nodedom_height/2- offset_top;
						fy = y + self.height/2- offset_top;
					}
					
					linedom.to = tx+","+ty;
	  				linedom.from = fx+","+fy;
	  				
	  				line.to = {x:tx,y:ty};
	  				line.from = {x:fx,y:fy};
					
					//linedom.to = nodedom.style.left+","+nodedom.style.top;
	  				//linedom.from = x+","+y;			
				}
			}
		}
		//修改被连接
		for(var i=0;i<CANVAS.nodes.length;i+=1){
			var n = CANVAS.nodes[i];
			for(var j=0;j<n.Lines.length;j+=1) {
				var line = n.Lines[j];
				var linedom =  line.getDom();
				if(line.node === self.id){
					var nodedom = n.getDom();
					
					var nodedom_left = parseInt(nodedom.style.left.replace('px',''));
					var nodedom_width = parseInt(nodedom.style.width.replace('px',''));
					var nodedom_top = parseInt(nodedom.style.top.replace('px',''));
					var nodedom_height = parseInt(nodedom.style.height.replace('px',''));
					
					var fx,fy,tx,ty;
					if(nodedom_left + nodedom_width <= x ) {
						fx = nodedom_left + nodedom_width;
						tx = x;
					} else if(nodedom_left >= x + self.width ) {
						fx = nodedom_left;
						tx = x + self.width;
					} else {
						fx = nodedom_left + nodedom_width/2;
						tx = x + self.width/2;
					}
					
					if(nodedom_top + nodedom_height <= y ) {
						fy = nodedom_top + nodedom_height - offset_top;
						ty = y - offset_top;
					} else if(nodedom_top >= y + self.height){
						fy = nodedom_top - offset_top;
						ty = y + self.height - offset_top;
					} else {
						fy = nodedom_top + nodedom_height/2 - offset_top;
						ty = y + self.height/2 - offset_top;
					}
					
					linedom.to = tx+","+ty;
	  				linedom.from = fx+","+fy;
	  				
	  				line.to = {x:tx,y:ty};
	  				line.from = {x:fx,y:fy};
					//linedom.from = nodedom.style.left+","+nodedom.style.top;
	  				//linedom.to = x+","+y;
				}
			}
		}
	
	}
	
	this.drawLines = drawLines;
	
	this.ddproxy.onDrag = function(e){
		
		var dom = self.getDom();
		var x = parseInt(dom.style.left.replace("px",""));
		var y = parseInt(dom.style.top.replace("px",""));
		
		x = x + e.getPageX() -self.orginPostion.x;
		y = y + e.getPageY() -self.orginPostion.y;
  		drawLines(x,y);
  		
  	}
  	
  	this.ddproxy.onMouseDown = function(e){
  		moveflag = true;
  		self.orginPostion = {x:e.getPageX(),y:e.getPageY()};
  		//self.showProperties();
  	}
  	
  	this.ddproxy.onMouseUp = function(e){
  		//setTimeout(function(){moveflag = false;},1500);
  		moveflag = false;
  		
  		var dom = self.getDom();
		var x = parseInt(dom.style.left.replace("px",""));
		var y = parseInt(dom.style.top.replace("px",""));
		x = x + e.getPageX() -self.orginPostion.x;
		y = y + e.getPageY() -self.orginPostion.y;
  		
  		self.orginPostion = {x:e.getPageX(),y:e.getPageY()};
  		self.showProperties();
  		//e.stopEvent();
  	}
  	
  	this.object.onmousedown = function(event){
  		
  		if(type == 'transition' && addTransBeginNode == null) {
  			addTransBeginNode = self;
  			
  			var x = window.event.x - offset_left ;
  			var y = window.event.y - offset_top;
  			
  			
  			tmpLine = new WfLine({id:'tmpLine'+new Date(),from:{x:x,y:y},to:{x:x,y:y} });
  		}else if(type == 'select') {
  			//selectedObject = self;
  			if(selectedObject != self){
  				selectObject(self);
  				self.showProperties();
  			} else {
  				selectObject();
  			}
  			
  		}
  	}

  	this.object.onmouseup = function(){
  		
  		if(type == 'transition' && addTransBeginNode != null && addTransBeginNode != self) {
  			document.body.removeChild(tmpLine.getDom());
  			tmpLine = null;
  			var lines = addTransBeginNode.Lines;
  			for(var i = 0; i<lines.length; i+=1) {
  				if(lines[i].node === self.id) {
  					addTransBeginNode = null;
  					error("提示","已经存在该流转，不用重复添加！");
  					return;
  				} 
  			}
  			
  			var lineid = addTransBeginNode.id + '-' + self.id;
  			var from = addTransBeginNode.getDom().style.left.replace("px",'')+','+addTransBeginNode.getDom().style.top.replace("px",'');
  			
  			var to = self.x+','+self.y;
			
			addTransBeginNode.addLine({id:lineid,node:self.id,from:from,to:to});
			drawLines(self.x,self.y);
  			addTransBeginNode = null;
  			self.showProperties();
  		}
  	}  
  	
  	this.object.ondblclick = function(){
  		
  		
  		if(processid){
  			
  			Ext.Ajax.request({
				url:'est/workflow/process/WfProcess/getTaskNodeId',
				params:{processId:processid,nodeName:self.name},
				success: function(response,options){
					var json = Ext.decode(response.responseText);
					jbpmnodeid = json.nodeId;
					if(jbpmnodeid){
						if(self.type === 'node' || self.type === 'countersign'){
							
							
							
							//Ext.getCmp('signcolumn').store.baseParams.processId = processid;
							
							
							Ext.getCmp('usernodegrid').doSearch({taskId:jbpmnodeid});
							Ext.getCmp('fieldnodegrid').doSearch({taskId:jbpmnodeid});
							Ext.getCmp('taskFormPanel').doLoad({taskId:jbpmnodeid});
							nodeWindow.show();
							
							
							
						} else if (self.type === 'condition') {
							Ext.getCmp('taskconditionGrid').doSearch({taskId:jbpmnodeid});
							taskconditionWindow.show();
						}
						
					} else {
						error("提示","您还未部署该流程，不能修改流程中配置！");
					}
					
				},
				failure: function(){
					error("错误","网络连接失败");
				}
			});
  			
  			
  		}
  		
  	}
	
};

WfNode.prototype.recalXY = function(){
		var dom = this.getDom();
		this.x = parseInt(dom.style.left.replace("px",""));
		this.y = parseInt(dom.style.top.replace("px",""));
		
}

/* 得到输入节点的个数 */
WfNode.prototype.getInNumber = function(){
		var cnt = 0;
		for(var i=0;i<CANVAS.nodes.length;i+=1){
			var n = CANVAS.nodes[i];
			for(var j=0;j<n.Lines.length;j+=1) {
				var line = n.Lines[j];
				if(line.node === this.id){
					cnt += 1;
				}
			}
		}
		return cnt;
}

/* 得到输出节点的个数 */
WfNode.prototype.getOutNumber = function(){
		return this.Lines.length;
}




WfNode.prototype.addLine = function(tonode){
		this.Lines.push(new WfLine({id:tonode.id,from:tonode.from,to:tonode.to,node:tonode.node}));
}

WfNode.prototype.initLines = function(o){
	var toNodes = [];
	
	for(var i=0;i<o.length;i+=1) {
		var tonode = o[i];
		toNodes.push(new WfLine({id:tonode.id,from:tonode.from,to:tonode.to,node:tonode.node}));
	}
	return toNodes;

}
/*
WfNode.prototype.drawLines = function(x,y){
	//alert(forin(this));
	for(var i=0;i<this.Lines.length;i+=1) {
		var line = this.Lines[i];
		var linedom =  line.getDom();
		
		for(var j=0;j<CANVAS.nodes.length;j+=1){
			var n = CANVAS.nodes[j];
			if(line.node === n.id){
				var nodedom = n.getDom();
				linedom.from = nodedom.style.left+","+nodedom.style.top;
  				linedom.to = x+","+y;			
			}
		}
	}

}*/

WfNode.prototype.toString = function(){
	 return ""; 
};


WfNode.prototype.toXMLString = function(){
	 var buf = new Est.StringBuffer();
	 if(this.type === 'start') {
	 	buf.append('<start-state name="'+this.name+'">');
		for(var i=0;i<this.Lines.length;i+=1){
			buf.append(this.Lines[i].toXMLString());
		}
		buf.append('</start-state>');
	 } else if(this.type === 'end') {
	 	buf.append('<end-state name="'+this.name+'">');
		for(var i=0;i<this.Lines.length;i+=1){
			buf.append(this.Lines[i].toXMLString());
		}
		buf.append('</end-state>');
	 } else if(this.type === 'node') {
	 	buf.append('<task-node name="'+this.name+'">');
	 	buf.append('<task name="'+this.name+'">');
	 	buf.append("<assignment config-type='bean' class='org.springmodules.workflow.jbpm31.JbpmHandlerProxy'><targetBean>assignmentHandlerService</targetBean></assignment>");
	    buf.append("</task>");
		for(var i=0;i<this.Lines.length;i+=1){
			buf.append(this.Lines[i].toXMLString());
		}
		buf.append('</task-node>');
	 } else if(this.type === 'countersign') {
	 	buf.append('<task-node name="'+this.name+'" signal="last-wait" create-tasks="false">');
	 	buf.append('<task name="'+this.name+'">');
	 	buf.append("<event type='task-end'>");
		buf.append("<action class='org.springmodules.workflow.jbpm31.JbpmHandlerProxy' config-type='bean'>");
		buf.append("<targetBean>taskEndCountersignHandlerService</targetBean>");
		buf.append("<factoryKey>jbpmConfiguration</factoryKey>");
		buf.append("</action>");
		buf.append("</event>");
	    buf.append("</task>");
	    
	    
	    buf.append("<event type='node-enter'>");
		buf.append(" <action name='createInstance' class='org.springmodules.workflow.jbpm31.JbpmHandlerProxy' config-type='bean'>" );
		buf.append("<targetBean>createTaskInstanceCountersignHandlerService</targetBean>");
		buf.append("<factoryKey>jbpmConfiguration</factoryKey>");
		buf.append("</action>");
		buf.append("</event>");
	    
		for(var i=0;i<this.Lines.length;i+=1){
			buf.append(this.Lines[i].toXMLString());
		}
		buf.append('</task-node>');
	 } else if(this.type === 'condition') {
	 	buf.append('<task-node name="'+this.name+'" create-tasks="false">');
	 	buf.append("<event type='node-enter'>");
	 	buf.append('<action name="'+this.name+'" class="org.springmodules.workflow.jbpm31.JbpmHandlerProxy" config-type="bean">');

		buf.append("<targetBean>conditionTaskHandlerService</targetBean>");
		buf.append("<factoryKey>jbpmConfiguration</factoryKey>");
		buf.append("</action>");
		buf.append("</event>");
		
		for(var i=0;i<this.Lines.length;i+=1){
			buf.append(this.Lines[i].toXMLString());
		}
		buf.append('</task-node>');
	 }
	 
	 return buf.toString(); 
};

WfNode.prototype.makeJSON = function(){   
	var nodeJSON = {};
	nodeJSON.id = this.id;
	nodeJSON.type = this.type;
	nodeJSON.name = this.name;
	nodeJSON.x = this.getDom().style.left.replace('px','');
	nodeJSON.y = this.getDom().style.top.replace('px','');
	
	nodeJSON.toNodes = [];
	for(var i=0;i<this.Lines.length;i+=1){
		nodeJSON.toNodes.push(this.Lines[i].makeJSON());
	}
	return nodeJSON;

}

WfNode.prototype.getElementString = function(){
	return "";
};

WfNode.prototype.getDom = function(){
	 return document.getElementById(this.id); 
};


WfNode.prototype.showProperties = function(){
	this.recalXY();
	var source = [];
	for(var i =0 ;i< this.propertiesList.length; i+=1) {
		var t = this.propertiesList[i];
		source[t] = this[t];
	}
	propertygrid.setSource(source);
};

WfNode.prototype.setText = function(text){
	this.object.childNodes[3].innerText = text;
};

WfNode.prototype.getNodeById = function(id){
	for(var j=0;j<CANVAS.nodes.length;j+=1){
			var n = CANVAS.nodes[j];
			if(id === n.id){
				return 	n;	
			}
	}
}

