
Ext.namespace('Est.ux');
/**
 * Est.ux.WfStartflowButton Extension Class
 * 
 * @author jingpj
 * @version 1.0
 * 
 * @class Est.ux.WfStartflowButton
 * @extends Ext.Button
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 */
Est.ux.WfApproveTab = function(cfg) {
	var self = this;
	this.bindGrid = cfg.bindGrid;
	this.piid = cfg.piid;
	//物资审批定义列
	this.approveColumn = cfg.approveColumn;
	//审批状态定义列
	this.stateColum=cfg.stateColum;
	//this.moduleid = cfg.moduleid || _moduleId;
	//this.xtype = "estform";
	
	this.title = '审批';
	this.id = 'approveTab';
	this.layout = 'fit';
	this.login = cfg.login;
	this.username = cfg.login;
	this.appoveinfo;
	this.result="通过";
	this.nodetype="node";
	this.checkedRows=[];
	this.rejectWin = function(fieldset){
			var win=new Ext.Window({
							id:'rejectWin',
							title:'驳回流程选择',
							width:950,
							height:500,
							closeAction:'hide',
							autoScroll:true,
							items:[{ 
								xtype: 'estform',
								id: 'rejectform',
								formurl: '',
								method: '',
								autoScroll:true,
								labelWidth:50,
								colstype:fieldset
							}],
							buttons:[
							{
								text:'确定',
								handler:function(){
									var values=Ext.getCmp('rejectform').getForm().getValues();
									var subdata={};
									var piids=[];
									var data={};
									for(var key in values){
										var value="'"+values[key]+"'";
										var index=value.indexOf(",");
										piids.push(key);
										if(index != -1){
											subdata[key]=value.slice(index+1,value.length-1);
										}else{
											subdata[key]=values[key];
										}
									}
									data['piidsStr']=piids.join();
									data['pm']=subdata;
									Ext.getCmp('wfApproveForm').form.findField('piids').setValue(Ext.encode(data));
									win.close();
								},
								scope:this
							
							},{
								text:'关闭',
								handler:function(){
									win.close();
								},
								scope:this
							}
						 ]
						});
						win.show();
	
	}
	
	var deptTreePanel={ treeid:'_deptTreePanel',//弹出窗口中树的名称
						 treeLoderUrl:'est/sysinit/sysdept/SysDept/getDepartTree',//树的URL
					     treeSCParam:'deptId',//树节点ID对应的gird的查询条件字段
					     treeWidth:'150',//树的宽度
					     rootTxt:'部门'
					  }
    var userCols={id:'userid',
                   lable:'姓名:',
                   scname:'userName',
                   singleSelect:false,
                   storeurl:'est/sysinit/sysuser/SysUser/getUserList',
                   cols:[{dataIndex:'username', header: '姓名', width: 80, sortable: false,isReturn:true,fieldto:'approvepeople'},
						 {dataIndex:'sysDept.deptname', header: '所在部门',width: 100, sortable: true},
						 {dataIndex:'duty', header: '职务',width: 100, sortable: true},
						 {dataIndex:'userid', header: '用户id', width: 20, hidden:true,sortable: false,isReturn:true,fieldto:'nextStepAssignUserid'}
	                    ]
                  }
    var feedBackFunc=function(data){
			       var grid=Ext.getCmp('usernodegrid');
			       var store=grid.getStore();
			       var count=store.getCount();
			       var isRepeat=false;
	          /* for (var k = 0; k < data.length; k++) {
	           		
		           
	              }*/
		              Ext.getCmp('nodeuserpop').hide();
	}
	this.sendtoNextStep = function(formvalues){
		Ext.Ajax.request({
			url:'est/workflow/process/WfProcess/sendMultiToNextStep',
			params:formvalues,
			success:function(response,options){
				var responseJson = Ext.decode(response.responseText);
				if(responseJson.success){
					msg("提示","流程发送成功！");
					self.removeAppoveinfo();
				} else {
					msg("错误",responseJson.error);
				}
				//setTimeout(function(){window.history.go(-1)},3000);
			},
			failure:function(){
				error("错误","流程发送失败！");
			}
		});
	}
	//驳回任务
	this.rejectTask=function(formvalues){
		Ext.Ajax.request({
						url:'est/workflow/process/WfProcess/rejectTask',
						params:formvalues,
						success:function(response,options){
							var responseJson = Ext.decode(response.responseText);
							if(responseJson.success){
								msg("提示","流程驳回成功！");
								self.removeAppoveinfo();
							} else {
								msg("错误",responseJson.error);
							}
							
							
							
						},
						failure:function(){
							error("错误","流程驳回失败！");
						}
					});
	
	}
	
	this.terminateStep=function(formvalues){
					Ext.Ajax.request({
						url:'est/workflow/process/WfProcess/endProcess',
						params:{piids:formvalues['piids']},
						success:function(){
							msg("提示","流程作废成功！");
							self.removeAppoveinfo();
						},
						failure:function(){
							error("错误","流程作废失败！");
						}
					});
	}
	this.removeAppoveinfo=function(){
			var store=Ext.getCmp(self.bindGrid).getStore();
					for(var i=0; i<self.appoveinfo.length;i++){
						store.each(
							function(rec){
								if(self.appoveinfo[i] == rec.data[self.piid]){
									store.remove(rec);
								}
							}
						);
					}
			var tabpanel = this.ownerCt;
			tabpanel.setActiveTab(tabpanel.getComponent(0));	
	}
	
	this.checkResult=function(result){
		var form=Ext.getCmp('wfApproveForm').getForm();
		if(result=='通过'){
				//form.findField('approvepeople').allowBlank=false;
		 		form.findField('approvepeople').setDisabled(false);
		 		Ext.getCmp('nextStepSignTypeGroup').setDisabled(false);
				//form.findField('nextStepTaskNodeId').allowBlank=false;
				form.findField('nextStepTaskNodeId').setDisabled(false);
				form.findField('opinion').allowBlank=true;
				self.result="通过";
		}else if(result=='驳回'){
				   form.findField('approvepeople').clearInvalid();
				   form.findField('approvepeople').reset();
				   form.findField('nextStepTaskNodeId').reset();
				   var piidsStr='';
			 		if(self.checkedRows && self.checkedRows.length > 0){
						var piids=[];
						var names = []
						for(var i=0;i < self.checkedRows.length;i++){
							piids.push(self.checkedRows[i].data[self.piid]);
							names.push(self.checkedRows[i].data[self.approveColumn]);
						}
					}
			 		
			 		Est.syncRequest({
						url:'est/workflow/process/WfProcess/buildingRegjectByPiids',
						params:{piids:piids.join(),names:names.join()},
						success:function(conn){
						 var responseJson = Ext.decode(conn.responseText);
						 var win = Ext.getCmp('rejectWin');
						 if(win){
						 	win.destroy();
						 }
						 self.rejectWin(responseJson.fieldset);
						},
						failure:function(conn){
							error("错误","获取流程驳回信息失败！");
						}
					});
					form.findField('approvepeople').allowBlank=true;
			 		form.findField('approvepeople').setDisabled(true);
					Ext.getCmp('nextStepSignTypeGroup').setDisabled(true);
					form.findField('nextStepTaskNodeId').allowBlank=true;
					form.findField('nextStepTaskNodeId').setDisabled(true);
					form.findField('opinion').allowBlank=false;
					self.result="驳回";
		}else if(result=='作废'){
				form.findField('approvepeople').clearInvalid();
				form.findField('approvepeople').reset();
				form.findField('nextStepTaskNodeId').reset();
				form.findField('approvepeople').allowBlank=true;
		 		form.findField('approvepeople').setDisabled(true);
				Ext.getCmp('nextStepSignTypeGroup').setDisabled(true);
				form.findField('nextStepTaskNodeId').allowBlank=true;
				form.findField('nextStepTaskNodeId').setDisabled(true);
				form.findField('opinion').allowBlank=false;
				self.result="作废";
		}
	
	
	}
	
	this.items = [{
				xtype:'estform',
				id:'wfApproveForm',
				labelWidth :55,
				title:'审批意见',
				region:'center',
				formurl:'est/workflow/process/WfApprove/',
				method:'ApproveInfo',
				colstype:[
					{id:'approveId',fieldset:'详细',
						items: [
								{fieldLabel:'审批结论',xtype:'radiogroup',id:'resultRadio',colspan:3,
		             			 items:[{boxLabel:'通过',inputValue:'通过',name:'result',checked:true,listeners:{'check':function(obj,checked){if(checked){self.checkResult(this.getRawValue())}}}},
		             			 		{boxLabel:'驳回',inputValue:'驳回',name:'result',listeners:{'check':function(obj,checked){if(checked){self.checkResult(this.getRawValue())}}}},
		             			 		{boxLabel:'作废',inputValue:'作废',name:'result',listeners:{'check':function(obj,checked){if(checked){self.checkResult(this.getRawValue())}}}}
		             			 	   ]
		            			},
								{fieldLabel:'审批意见',xtype:'textarea',name:'opinion',width:500,colspan:3},
								/*
								{fieldLabel:'备注',xtype:'textarea',name:'remark',width:500,colspan:3},
								{fieldLabel:'审批人',name:'nameapproveby',xtype:'estsignfield',signWin:signWin,width:150,allowBlank:false,colspan:2,
	          	  			     feedbackFormId:'wfApproveForm',feedbackFields:{'username':'nameapproveby','userid':'approveby'}
	          	  			    },
								{fieldLabel:'审批时间',name:'approvedate',readOnly:true},
								*/
								{fieldLabel:'piids', name:'piids',xtype:'hidden'}
								
						]
					},
					{
						fieldset:'流程下一步选择',
						items:[
							{fieldLabel:'任务选择',xtype: 'estcombos',colspan:2,hiddenName:'nextStepTaskNodeId',allowBlank:true,width:250, valueField:'taskId', displayField:'name', storeurl:'est/workflow/processdefination/WfProcessDefination/getNextWfDftasksByProcessInstanId',
							 listeners:{'select':function(){
							 		var _self=this;
							 		Ext.Ajax.request({
										url:'est/workflow/processdefination/WfProcessDefination/getWfDftask',
										params:{taskId:_self.getValue()},
										success:function(response){
											var nodetype=Ext.decode(response.responseText).data.nodetype;
											var form=Ext.getCmp('wfApproveForm').getForm();
											self.nodetype=nodetype;
											if(nodetype=='end'){
													form.findField('approvepeople').clearInvalid();
													form.findField('approvepeople').reset();
													form.findField('approvepeople').allowBlank=true;
													form.findField('approvepeople').setDisabled(true);
													Ext.getCmp('nextStepSignTypeGroup').setDisabled(true);
											}else{	
													form.findField('approvepeople').allowBlank=false;
													form.findField('approvepeople').setDisabled(false);
													Ext.getCmp('nextStepSignTypeGroup').setDisabled(false);
											}
										},
										failure:function(){
											form.findField('approvepeople').allowBlank=false;
											form.findField('approvepeople').setDisabled(false);
											Ext.getCmp('nextStepSignTypeGroup').setDisabled(false);
											error("错误","获取任务节点错误，请重新选择任务！");
										}
									});
							 	}
							 }
							},
							//{fieldLabel:'签字类型',xtype: 'estcombos',value:'签字',hiddenName:'nextStepSignType',width:80,elms:[['签字','签字'],['会签','会签']]},
							{fieldLabel:'签字类型',xtype:'radiogroup',id:'nextStepSignTypeGroup',
	             			 items:[{boxLabel:'签字',inputValue:'签字',name:'nextStepSignType',checked:true},
	             			 		{boxLabel:'会签',inputValue:'会签',name:'nextStepSignType'}
	             			 	   ]
	            			},
							{fieldLabel:'处理人',name:'approvepeople',colspan:3,xtype:'treegridchooserfield',width:500,
					             title:'审批人选择',
					             winid:'_nodeuserpop',
					             winWidth:500,
					             winHeight:500,
					             treePanel:deptTreePanel,
					             colstype:userCols,
					             feedbackFormid:'wfApproveForm'
				             },
				            {xtype:'hidden',name:'nextStepAssignUserid'}
						]
					}
				],
				tbar:[
					
					{text:'发送',handler:function(){
						var form=Ext.getCmp('wfApproveForm').getForm();
							
							if(self.nodetype=='end'){
								    form.findField('approvepeople').clearInvalid();
								    form.findField('nextStepTaskNodeId').clearInvalid();
									form.findField('approvepeople').allowBlank=true;
									//form.findField('approvepeople').setDisabled(true);
									//Ext.getCmp('nextStepSignTypeGroup').setDisabled(true);
							}
							if(self.result !='通过'){
								    form.findField('approvepeople').clearInvalid();
								    form.findField('nextStepTaskNodeId').clearInvalid();
									form.findField('approvepeople').allowBlank=true;
									form.findField('approvepeople').setDisabled(true);
									Ext.getCmp('nextStepSignTypeGroup').setDisabled(true);
							}
							
						if(form.isValid()){
							self.signWin.show();
						}
					}}/*,
					{text:'驳回',handler:function(){
						
						var success = function(){
							Ext.Ajax.request({
								url:'est/workflow/process/WfProcess/rejectTask',
								params:{tiid:_taskId},
								success:function(reponse,options){
									var reponseJson = Ext.decode(reponse.responseText);
									if(reponseJson.success){
										msg("提示","流程驳回成功！");
										//Ext.getCmp(self.bindGrid).getStore().reload();
										setTimeout(function(){window.history.go(-1)},3000);
									} else {
										error("提示" ,reponseJson.info);
									}
								},
								failure:function(){
									error("错误","流程驳回失败！");
								}
							});
						}
						Ext.getCmp('wfApproveForm').doSumbit({success:success});
						
					}},
					{text:'驳回到上一处理者',handler:function(){
						
						var success = function(){
							Ext.Ajax.request({
								url:'est/workflow/process/WfProcess/rejectTask',
								params:{tiid:_taskId,toOldActor:'T'},
								success:function(reponse,options){
									var reponseJson = Ext.decode(reponse.responseText);
									if(reponseJson.success){
										msg("提示","流程驳回成功！");
										//Ext.getCmp(self.bindGrid).getStore().reload();
										setTimeout(function(){window.history.go(-1)},3000);
									} else {
										error("提示" ,reponseJson.info);
									}
								},
								failure:function(){
									error("错误","流程驳回失败！");
								}
							});
						}
						Ext.getCmp('wfApproveForm').doSumbit({success:success});
						
					}},
							
					{text:'作废',handler:function(){
							self.operation='tema';
							Ext.getCmp('wfApproveForm').getForm().findField('result').setValue('作废');
							Ext.getCmp('wfApproveForm').getForm().clearInvalid();
							if(!Ext.getCmp('wfApproveForm').getForm().findField('opinion').getValue()){
								Ext.getCmp('wfApproveForm').getForm().findField('opinion').markInvalid('审批意见必填!');
							}else{
								self.signWin.show();
							}
						}
					}*/	
				]
			}];
			
			
	/** 签字窗口*/
   	this.signWin = new Ext.Window({
		 
		title:'流程签字',
		width:300,
		height:150,
		resizable:false,
		layout:'fit',
		plain:true,
		closeAction:"hide",
		items:[
			{region:'center',
				xtype:'form',
				labelWidth:60,
				frame:true,
		        bodyStyle:'padding:10px,10px,10px,10px',
		        id:'signForm',
		        items: [
		        	   {xtype:'textfield',fieldLabel: '审批人',value:self.username,anchor:'100%',allowBlank:false,readOnly:true},
		        	   {xtype:'textfield',fieldLabel: '密码',name: 'password',inputType:'password',anchor:'100%',allowBlank:false},
		        	   {xtype:'textfield',name:'login',value:self.login,xtype:'hidden'}
			           
			    ]
			  }
		],
		buttons:[
			{
				text:'确定',
				handler:function(){
					var signForm = Ext.getCmp('signForm').form;
					var wfForm = Ext.getCmp('wfApproveForm').getForm();
					var formvalues = wfForm.getValues();
					 	Ext.apply(formvalues,{login:signForm.findField('login').getValue(),password:signForm.findField('password').getValue()});
				//	var result = wfForm.findField('result').getValue();
					 	
					Ext.Ajax.request({
								url:'est/workflow/process/WfProcess/checkIsCountersign',
								params:formvalues,
								success:function(reps){
									var json= Ext.decode(reps.responseText);
									
									var flag=json.success;
									var form=Ext.getCmp('wfApproveForm').getForm();
									if(self.result=='通过' && self.nodetype!='end'){
										if(flag){
											form.findField('approvepeople').allowBlank=true;
									 		//form.findField('approvepeople').setDisabled(true);
											//Ext.getCmp('nextStepSignTypeGroup').setDisabled(true);
											form.findField('nextStepTaskNodeId').allowBlank=true;
											//form.findField('nextStepTaskNodeId').setDisabled(true);
										}else {
											    form.findField('approvepeople').allowBlank=false;
										 		//form.findField('approvepeople').setDisabled(false);
												//Ext.getCmp('nextStepSignTypeGroup').setDisabled(false);
												form.findField('nextStepTaskNodeId').allowBlank=false;
												//form.findField('nextStepTaskNodeId').setDisabled(false);
												form.findField('opinion').allowBlank=true;
										}
									}
									if(json.type =='countersign' && flag===true){
										error('提示','当前是会签任务，您不是最后一个审批人，因此选择的下一流程选择无效!');
									}
									
									if(Ext.getCmp('wfApproveForm').getForm().isValid()){
										//return;
										if(self.result == '作废'){
											self.terminateStep(formvalues);
										}else if(self.result == '通过'){
											self.sendtoNextStep(formvalues);
										}else if(self.result == '驳回'){
											self.rejectTask(formvalues);
										}
										self.signWin.hide();
									}
									
								},
								failure:function(){
									error("错误","流程发送失败！");
								}
							}); 	
					
				},
				scope:this
			
			},{
				text:'关闭',
				handler:function(){
					self.signWin.hide();
				},
				scope:this
			}
		],
		listener:{}
		
	});	
			
		this.listeners = {
				'activate':function(){
					self.checkedRows=Ext.getCmp(this.bindGrid).getCheckedRecords(true);
					var tabpanel = this.ownerCt;
					if(self.checkedRows && self.checkedRows.length > 0){
							var wfApproveForm=Ext.getCmp('wfApproveForm');
							var piids=[];
							var len = self.checkedRows.length;
							for(var i=0;i < len;i++){
								if(i < len-1){
									var nextindex=i+1;
									var curent = self.checkedRows[i].data[self.stateColum];
									var next = self.checkedRows[nextindex].data[self.stateColum];
									if(curent === next){
										piids.push(self.checkedRows[i].data[this.piid]);
									}else{
										tabpanel.setActiveTab(tabpanel.getComponent(0));	
										error('提示','只能选择相同审批状态的任务!');
										return;
									}
								}else{
									piids.push(self.checkedRows[i].data[this.piid]);
								}
							}
							self.appoveinfo = piids;
							//alert(piids.length);
							var piid = self.checkedRows[0].data[this.piid];
							//wfApproveForm.doLoad({_wfProcessInstanceId : piid});
							var form=wfApproveForm.form;
								form.findField('nextStepTaskNodeId').getStore().reload({params:{processinstanceId:piid}});
							    form.findField('piids').setValue(piids.join());
						 		form.findField('approvepeople').setDisabled(false);
								Ext.getCmp('nextStepSignTypeGroup').setDisabled(false);
								form.findField('nextStepTaskNodeId').setDisabled(false);
					}else{
						tabpanel.setActiveTab(tabpanel.getComponent(0));	
					}
					
				}
				
			};
	
	Est.ux.WfApproveTab.superclass.constructor.call(this, cfg);
}; 

Ext.extend(Est.ux.WfApproveTab, Est.ux.Layout, {
}); 

Ext.reg('estwfapprovetab', Est.ux.WfApproveTab);
