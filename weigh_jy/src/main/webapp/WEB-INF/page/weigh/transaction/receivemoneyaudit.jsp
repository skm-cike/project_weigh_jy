<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.est.common.ext.util.classutil.DateUtil, com.est.sysinit.sysuser.vo.SysUser"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":"
			+ request.getServerPort() + path + "/";
	String startTime = DateUtil.format(DateUtil.add(new Date(), DateUtil.DAY_OF_MONTH, -30));
	String login = ((SysUser) request.getSession().getAttribute("loginUser")).getLogin();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>单位录入</title>
		<%@ include file="/include.jsp"%>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	</head>

	<body>
		<script type="text/javascript">
    Ext.onReady(function(){
    //审批签名窗口
	var win = new Ext.Window({
		width : 400,
		height : 180,
		id: 'win',
		title : '签名',
		modal: true,
		closeAction: 'hide',
		listeners:{show:function() {
			Ext.getCmp('login_').setValue("<%=login%>");
			Ext.getCmp('password_j').focus();
		}},
		items : new Ext.FormPanel({
			id : "form",
			labelWidth : 60,
			baseCls : "x-plain",
			bodyStyle : "padding:20px 5px 5px 20px;",
			defaults : {
				xtype : "textfield",
				width : "230",
				allowBlank : false
			},
			items : [{
						id:'login_',
						fieldLabel : "用 户",
						name : "login",
						enableKeyEvents : true,
						readOnly:true,
						listeners : {
							"keyup" : function(obj, e) {
								if (e.getKey() == e.ENTER
										&& this.getValue().length > 0) {
									Ext.getCmp('form').onSubmit();
								}
							}
						}
					}, {
						fieldLabel : "密 码",
						id:'password_j',
						name : "password",
						inputType : "password",
						enableKeyEvents : true,
						listeners : {
							"keyup" : function(obj, e) {
								if (e.getKey() == e.ENTER
										&& this.getValue().length > 0) {
									Ext.getCmp('form').onSubmit();
								}
	
							}
						}
					}],
			onSubmit : function() {
				if (this.form.isValid()) {
					this.form.doAction("submit", {
						waitMsg : "验证中...",
						url : "<%=basePath%>est/common/business/SysUser/verfyUser",
						params : {
							"_r" : new Date()
						},
						method : "post",
						success : function(form, action) {
							submitAudit(action.result.data);
							Ext.getCmp('form').getForm().reset();
							Ext.getCmp('win').hide();
						},
						failure : function() {
							Ext.Msg.alert("错误", "用户名或者密码错误!");
						}
					});
				}
			},
		buttons:[
			{text:"通过", type:"submit",handler:function () {
				Ext.getCmp('form').onSubmit();
			}},
			{ text:"取消", 
				handler:function () {
					Ext.getCmp('form').getForm().reset();
					Ext.getCmp("win").hide();
				}
			}		
		]
		})
	});

	//提交审批
var submitAudit = function(userInfo) {
				var records = Ext.getCmp('grid').getCheckedRecords();
				var ids = '';
				for (var i = 0; i < records.length; i++) {
			   				ids += records[i].data['id'] + ',';
			   	}
			   	if (ids=='') {
			   		return;
			   	}
			   	ids = ids.substring(0, ids.length-1);
				Ext.Ajax.request({
					url: '<%=basePath%>est/weigh/transaction/ReceiveMoney/auditAccept',
					waitMsg : '正在保存...',
  				    success: function(rep) {
  				  	    var store = Ext.getCmp('grid').getStore();
  				    	var msgObj = Ext.decode(rep.responseText);
							if (msgObj.success) {
								showMsg('提交成功');
							} else if (msgObj.error) {
									error('提示', msgObj.error);
							}
						store.reload();
  				    },
                    failure: function() {
                    	error('提示', '提交失败');
                    },
				    params: {data:ids}
				});
	}		
    
    //收款数据
    var companyid='';
    var companyname='';
    var companycode='';
    var pagevendorCombox = {
	   		xtype:'estcombos',
	   		id:'pagevendorCombox',
	   		editable:true,
			fieldLabel:'来煤单位',
			displayField:'companyname',
			//width:100,
			valueField:'companycode',
			hiddenName:'companycode',
			name:'companycode',
			listeners:{
				'select':function(combo,record){
					 	Ext.getCmp('formPanel').form.findField('companyname').setValue(record.data.companyname);
				}
			},
			readOnly:false,
			loadingText:'loading…',
			allowBlank:false,
			selectOnFocus: true,
			triggerAction:'all',
			enableKeyEvents:true,
			storeurl:'<%=basePath%>est/weigh/cfginfo/WeighCompany/getCompanyListByPz?pz=<%=request.getParameter("pz")%>'
		}

    	var colsType = {
			id:'id',
			cols:[
			        {dataIndex: 'breedtype', name:'breedtype', header:'品名', renderer: function(v) {
						if (v =='shigao') {
							return '石膏';
						}else if(v=='fenmeihui') {
							return '粉煤灰';
						}else if(v=='shihui') {
							return '石灰';
						}
					}},
					{dataIndex: 'companyname', name:'companyname', header:'单位名称'},
					{dataIndex: 'companycode', name:'companycode', header:'单位编码',hidden:true},
                    <%if("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
                    {dataIndex: 'jizu', name:'jizu', header:'机组'},
                    <%}%>
					{dataIndex: 'money', name:'money', header:'货款'},
					{dataIndex: 'receiptno', name:'receiptno', header:'单号'},
					{dataIndex: 'operator', name:'operator', header:'操作人'},
					{dataIndex: 'receivedate', name:'receivedate', header:'收款日期'},
					{dataIndex: 'auditor', name:'auditor', header:'审核人'},
					{dataIndex: 'audittime', name:'audittime', header:'审核日期'},
					{dataIndex: 'id', name:'id', hidden:true}
			]
		}
		
    	var gridPanel = {
    		xtype:'estlayout',
    		region:'center',
    		tbar:[{text:'通过审核',handler:function() {
    			var records = Ext.getCmp('grid').getSelectionModel().getSelections();
    			if (!records || records.length==0) {
    				Ext.Msg.alert('提示', '请先选择要审核的数据!');
    			}
    			win.show();
    		}}],
    		items:{
   				id:'grid',
				xtype:'esteditgrid',
				storeurl:'<%=basePath%>est/weigh/transaction/ReceiveMoney/getReceiveMoneyList?pz=<%=request.getParameter("pz")%>',
				colstype : colsType,
				checkColumn:true,
				region:'center'
			}
    	}
    		//控件隐藏或显示
    	function noneOrBlock(selec_val,compare_val,fieldname){
    			/*
	    	    if(selec_val==compare_val){
				    Ext.getCmp(fieldname).getEl().up('.x-form-item').setDisplayed(true);
				  }else{
				    Ext.getCmp(fieldname).getEl().up('.x-form-item').setDisplayed(false);
				  }
				  */
    	  }
    	
    	var pzdjField;
    	var pz='<%=request.getParameter("pz")%>';
    	if (pz=='fenmeihui') {
    		pzdjField = 'PZDJ';
    	} else if (pz == 'shigao') {
    		pzdjField = 'SGPZDJ';
    	}
    	
    	var formcols = [
		{fieldset:'收货详细信息',id:'id',
			items: [
			    {xtype:'hidden',name:'companyname'},
				 pagevendorCombox,
				 {fieldLabel:'单号',name:'receiptno'},
			    {fieldLabel:'货款(￥)',name:'money', allowBlank:false,xtype:'numberfield'},
		        {fieldLabel:'备注',name:'remark'},
		        {fieldLabel:'品名',name:'breedtype',hidden:true,hideLabel:true,value:'<%=request.getParameter("pz")%>'}
		]}];
		
		var formPanel = {
				xtype: 'estform',
				id: 'formPanel',
				region:'east',
				title:'详细信息',
		       	width:350,
		       	colnum:1,
		       	formurl: '<%=basePath%>est/weigh/transaction/ReceiveMoney',
				method: 'ReceiveMoney',
				colstype: formcols,
				tbar:[
		       		{text:'重置/增加',iconCls:'page_add', handler:function(){
						Ext.getCmp('formPanel').doReset();
			    	}},
			    	{text:'保存',iconCls:'page_save', handler: function() {
			    		Ext.Msg.confirm('提示', '你确定要增加或修改吗?', function(btn) {
			    			if (btn == 'yes') {
			    				Ext.getCmp('formPanel').doSumbit({success:function(form, rep){
					       			Ext.getCmp('grid').store.reload();
					       		}})
			    			}
			    		});
			    	}},
			    	{text:'删除',iconCls:'page_delete', handler: function() {
			    		Ext.Msg.confirm('提示', '你确定要删除吗?', function(btn) {
			    			if (btn == 'yes') {
			    				Ext.getCmp('formPanel').doDelete({
					    			success:function(form, rep) {
					    				Ext.getCmp('grid').store.reload();
					    			}
					    		});
			    			}
			    		});
			    		
			    	}}
			    ]
			}
		
		var tpanel = {
			xtype : 'estlayout',
			layout:'border',
			region : 'center',
			items : [gridPanel]
		}
		new Ext.Viewport({
			layout:'border',
			items: [menuPanel,tpanel]
		});	
		/*
      	Ext.getCmp('grid').on({
	      		'rowclick': function(t, i, e) {
	      		    var type = Ext.getCmp('grid').getSelectionModel().getSelected().data['breedtype'];
	      			var id = t.getSelectionModel().getSelected().data['id'];
	      			Ext.getCmp('formPanel').doLoad({id:id});
      		}
      	});
      	*/
      	//Ext.getCmp('grade_comb').getEl().up('.x-form-item').setDisplayed(false);
    
    })
    
   </script>
	</body>
</html>
