
Ext.namespace('Est.ux');
/**
 * Est.ux.BasicForm Extension Class
 * xtype is estpanel
 * 
 * @author Smile_BuG
 * @version 1.0
 * 
 * @class Est.ux.BasicForm
 * @extends Ext.form.BasicForm 
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 */
Est.ux.BasicForm = function(cfg) {
	
	
	Est.ux.BasicForm.superclass.constructor.call(this, cfg);
	
}; 

Ext.extend(Est.ux.BasicForm, Ext.form.BasicForm, {
	reset: function() {
		console.log('est reset');
		Est.ux.BasicForm.superclass.reset.call(this);
		return this;
	},
	
	recursionSetValues : function(scope,values){
		if(typeof values =='object'){

			var field, id;
	        for(id in values){
	        			
	            if(typeof values[id] == 'object' && values[id] !== null){
	            	
	            	console.log(values[id]);
	            	for(var k in values[id]){
	            		if(field = this.findField(id+'.'+k)){
			                if(field.xtype=='esttreecombo'){
			                	var node = {};
			                	node["id"] = eval('(values.'+field.hiddenName+')');
			                	node["text"] = eval('(values.'+field.displayField+')');
			                	field.setValue(node);
			                	
			                } else {
			                	field.setValue(values[id][k]);
			                }
			                if(this.trackResetOnLoad){
			                    field.originalValue = field.getValue();
			                }
	            		}
	            	}
	            }
	        }
	     }
	
	},
	
	
	findField : function(id){
        var field = this.items.get(id);
       
        if(!field){
            this.items.each(function(f){
                if(f.isFormField && (f.dataIndex == id || f.id == id || f.getName() == id)){
                    field = f;
                    return false;
                }
            });
        }
        return field || null;
    },
    
    
	
	setValues: function(values) {
		
		this.reset();
		if(typeof values =='object'){
			
			//edit by jingpj ,fix the bug of 3rd level json object can NOT be fill in 
			Ext.each(this.items.items,function(v){
				var name = v.name || v.hiddenName;
				if(field = this.findField(name)){
					
					
					if(field.xtype=='esttreecombo'){
	                	var node = {};
	                	try{
	                	node["id"] = eval('(values.'+field.hiddenName+')') ;
	                	node["text"] = eval('(values.'+field.displayField+')') ;
	                	}catch(e){
	                		node = {id:'',text:''};
	                	}
	                	field.setValue(node);
	                	
	                } else if(field.xtype=='estcombos') {
	                	try{
	                		field.setValue(eval('(values.'+field.hiddenName+')'));
	                	}catch(e){
	                		field.setValue(undefined);
	                	}
	                } else {
	                	try{
	                		field.setValue(eval('(values.'+field.name+')'));
	                	}catch(e){
	                		field.setValue(undefined);
	                	}
	                }
	                if(this.trackResetOnLoad){
	                    field.originalValue = field.getValue();
	                }
				}
			},this)
			
			/*
			var field, id;
	        for(id in values){
	        			
	            if(typeof values[id] == 'object' && values[id] !== null){
	            	
	            	console.log(values[id]);
	            	for(var k in values[id]){
	            		if(field = this.findField(id+'.'+k)){
			                if(field.xtype=='esttreecombo'){
			                	var node = {};
			                	node["id"] = eval('(values.'+field.hiddenName+')');
			                	node["text"] = eval('(values.'+field.displayField+')');
			                	field.setValue(node);
			                	
			                } else {
			                	field.setValue(values[id][k]);
			                }
			                if(this.trackResetOnLoad){
			                    field.originalValue = field.getValue();
			                }
	            		}
	            	}
	            }
	        } 
	        */
		}else {
	        	Est.ux.BasicForm.superclass.setValues.call(this,values);
		};
		
		//Est.ux.BasicForm.superclass.setValues.call(this,values);
		return this;
	}
}); 
