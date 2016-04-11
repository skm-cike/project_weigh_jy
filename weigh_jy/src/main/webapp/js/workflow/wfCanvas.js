/**
 * WfComponent
 * 工作流画布
 * @author jingpj
 * @version 1.0
 *
 */

function WfCanvas(object){
	this.type='canvas';
	this.nodes =  [];
	this.lines =  [];
	this.object = object;
	
}

WfCanvas.prototype.init = function(processjson) {
	
	//var str = {"processname":"ff","nodes":[{"id":"start001","type":"start","name":"开始","x":"781","y":"72","toNodes":[{"id":"start001-node001","node":"node001","from":{"x":782,"y":63},"to":{"x":685,"y":72}}]},{"id":"end001","type":"end","name":"结束","x":"129","y":"107","toNodes":[]},{"id":"node001","type":"node","name":"新任务1","x":"565","y":"82","toNodes":[{"id":"node001-node002","node":"node002","from":{"x":565,"y":87},"to":{"x":422,"y":304}}]},{"id":"node002","type":"node","name":"新任务2","x":"303","y":"330","toNodes":[{"id":"node002-end001","node":"end001","from":{"x":302,"y":304},"to":{"x":249,"y":112}}]}]}
	if(processjson){
		for(var i=0;i<processjson.nodes.length;i+=1) {
			this.addNode(new WfNode(processjson.nodes[i]));
		}
	}
	
	/*
	for(var i=0;i<this.nodes.length;i+=1) {
		this.nodes[i].drawLines();
	}
	*/
	
};

WfCanvas.prototype.addNode = function(o){
	this.nodes.push(o);
};

//清空画布
WfCanvas.prototype.clear = function(){
	while(this.nodes.length>0) {
		this.removeNode(this.nodes[0]);
	}
};


WfCanvas.prototype.removeNode = function(o){
	 //删除连线
	 
	 for(var i=0;i<this.nodes.length;i+=1) {
	 	var node = this.nodes[i];
	 	if(node.id == o.id){
	 		
	 		for(var j=node.Lines.length-1;j>=0;j-=1){
	 			document.body.removeChild(node.Lines[j].getDom());
	 			node.Lines[j] = null;
	 			//node.Lines.splice[j,1];
	 		}
	 		node.Lines.length = 0;
	 	} else {
	 		for(var j=node.Lines.length-1;j>=0;j-=1){
	 			
	 			if(node.Lines[j].node == o.id) {
	 				document.body.removeChild(node.Lines[j].getDom());
	 				var delobj = node.Lines[j];
	 				node.Lines.splice(j,1);
	 				delobj = null;
	 			}
	 			
	 		}
	 	}
	 }
	 
	 //删除节点
	 document.body.removeChild(o.getDom());
	 for(var i=0;i<this.nodes.length;i+=1) {
	 	if(this.nodes[i].id == o.id){
	 		this.nodes[i].ddproxy.unreg();
	 		this.nodes[i].ddproxy = null;
	 		this.nodes.splice(i,1);
	 		break;
	 	}
	 }
	 
};

//删除连线
WfCanvas.prototype.removeLine = function(line){
	 for(var i=0;i<this.nodes.length;i+=1) {
	 	var node = this.nodes[i];
 		for(var j=node.Lines.length-1;j>=0;j-=1){
 			
 			if(node.Lines[j].id == line.id) {
 				document.body.removeChild(node.Lines[j].getDom());
 				var delobj = node.Lines[j];
 				node.Lines.splice(j,1);
 				delobj = null;
 				break;
 			}
 		}
	 }
};

WfCanvas.prototype.toString = function(){
	 return "";
};

WfCanvas.prototype.getElementString = function(){
	return "";
};

//修改node的锁定状态
WfCanvas.prototype.toggleNodeLock = function(flag) {
	for(var i = 0 ;i<this.nodes.length ;i+=1){
		var n = this.nodes[i];
		flag ? n.ddproxy.lock() : n.ddproxy.unlock();
	}
};


//得到下个对象的id
WfCanvas.prototype.getNextNodeId = function(type) {
	var maxid = type;
	if(type === 'node' || type==='start' || type==='end' || type === 'fork' || type === 'join' || type === 'condition' || type === 'countersign') {
		for(var i = 0 ;i<this.nodes.length ;i+=1){
			var n = this.nodes[i];
			if(n.type === type && n.id > maxid) {
				maxid = n.id;
			}
		}
		maxid =  maxid.substring(type.length);
		if(maxid === '') {
			return type + "001" 
		} else {
			var nextvalue =  parseInt(maxid)+1;
			if(nextvalue<10) {
				return  type+'00'+ nextvalue;
			} else if(nextvalue<100) {
				return type+'0'+ nextvalue;
			} else {
				return type+''+nextvalue;
			}
		}
		return maxid;
	}
	
};


/* 得到开始节点 */
WfCanvas.prototype.getStartNode = function(){
	for(var i=0;i<this.nodes.length;i+=1){
		var n = this.nodes[i];
		if(n.type == 'start') {
			return n;
		}
	}
}

/* 得到开始节点的个数 */
WfCanvas.prototype.getStartNumber = function(){
	var cnt = 0;
	for(var i=0;i<this.nodes.length;i+=1){
		var n = this.nodes[i];
		if(n.type == 'start') {
			cnt += 1;
		}
	}
	return cnt;
}

/* 得到结束节点的个数 */
WfCanvas.prototype.getEndNumber = function(){
		var cnt = 0;
		for(var i=0;i<this.nodes.length;i+=1){
			var n = this.nodes[i];
			if(n.type == 'end') {
				cnt += 1;
			}
		}
		return cnt;
}

/* 显示信息 */
WfCanvas.prototype.alertInfo = function(node,msg){
		//Ext.get(node.id).highlight();
		alert(node.name+':'+msg);
}

/* 检查有效性 */
WfCanvas.prototype.checkValid = function(){
		
		var startCnt  = this.getStartNumber();
		if(startCnt > 1) {
			alert('不能超过一个开始节点。');
			return false;
		}  else if(startCnt == 0) {
			alert('必须有一个开始节点。');
			return false;
		}
		
		var endCnt  = this.getEndNumber();
		if(endCnt ==0) {
			alert('必须有一个结束节点。');
			return false;
		}
		
		for(var i=0;i<this.nodes.length;i+=1){
			var n = this.nodes[i];
			var inCnt = n.getInNumber();
			var outCnt = n.getOutNumber();
			var type = n.type;
			var config = NodeConfig[type];
			
			
			if(config['in']) {
				if(inCnt!=config['in']) {
					this.alertInfo(n,config.inerror);
					
					//alert(n.name+':'+config.inerror);
					return false;
				}
			}
			if(config['out']) {
				if(outCnt!=config['out']) {
					this.alertInfo(n,config.outerror);
					//alert(n.name+':'+config.outerror);
					return false;
				}
			}
			if(config['minin']) {
				if(inCnt<config['minin']) {
					this.alertInfo(n,config.mininerror);
					//alert(n.name+':'+config.mininerror);
					return false;
				}
			}
			if(config['minout']) {
				if(outCnt<config['minout']) {
					this.alertInfo(n,config.minouterror);
					//alert(n.name+':'+config.minouterror);
					return false;
				}
			}
			if(config['maxin']) {
				if(inCnt>config['maxin']) {
					this.alertInfo(n,config.mininerror);
					//alert(n.name+':'+config.mininerror);
					return false;
				}
			}
			if(config['maxout']) {
				if(outCnt>config['maxout']) {
					this.alertInfo(n,config.maxouterror);
					//alert(n.name+':'+config.maxouterror);
					return false;
				}
			}
			
		}
		return true;
}


/* 生成xml */
WfCanvas.prototype.makeXML = function(name){
	
	var buf = new Est.StringBuffer();
	buf.append('<process-definition name="'+name+'">');
	
		
	for(var i=0;i<this.nodes.length;i+=1){
		var node = this.nodes[i];
		buf.append(node.toXMLString());
	}
		
	buf.append('</process-definition>');

	return buf.toString();
	
}

/* 生成xml */
WfCanvas.prototype.makeJSON = function(name){
	
	var jsontmp = {processname:name,nodes:[]};
	
	for(var i=0;i<this.nodes.length;i+=1){
		var node = this.nodes[i];
		jsontmp.nodes.push(node.makeJSON()); 
	}
		
	var jsonStr = Ext.encode(jsontmp);
	return jsonStr;
	
}


/* 部署流程 */
WfCanvas.prototype.deployProcess = function(){
	
	
	deployWindow.show();
	
	/*
	Ext.Ajax.request({
		url:'est/workflow/processdeploy/WfProcessDeploy/deployProess',
		params:{processXml:processXml},
		success: function(response,options){
			
			var json = Ext.decode(response.responseText);
			
			
		},
		failure: function(){
			error("错误","网络连接失败");
		}
	
	
	});
	*/
	
		

}

