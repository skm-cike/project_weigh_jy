<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.est.common.ext.util.classutil.DateUtil"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":"
			+ request.getServerPort() + path + "/";
	String startTime = DateUtil.format(DateUtil.add(new Date(), DateUtil.DAY_OF_MONTH, -30));
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
		width:'auto',
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
    };
        var jizucombo = [['31#_32#','31#_32#'],['33#_34#','33#_34#']];

    	var colsType = {
			id:'id',
			cols:[
			        {dataIndex: 'breedtype', name:'breedtype', header:'品名', renderer: function(v) {
						if (v =='shigao') {
							return '石膏';
						}else if(v=='fenmeihui') {
							return '粉煤灰';
						}else if(v=='huizha') {
							return '灰渣';
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

        // 搜索字段
        var searchFields=[
            '名称:',{id:'companyname',xtype:'textfield'}
        ];
        // 搜索字段映射
        var searchMapping={'companyname':'companyname'};
		
    	var gridPanel = {
    		xtype:'estlayout',
    		region:'center',
            bbar:new Est.ux.SearchToolbar({id:'searchbar',searchFields:searchFields,searchMapping:searchMapping,gridId:'grid'}),
    		items:{
   				id:'grid',
				xtype:'esteditgrid',
				storeurl:'<%=basePath%>est/weigh/transaction/ReceiveMoney/getReceiveMoneyList?pz=<%=request.getParameter("pz")%>',
				colstype : colsType,
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
                <%if("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
                {fieldLabel:'机组',name:'jizu',hiddenName:'jizu',xtype:'estcombos',elms:jizucombo,allowBlank:false},
                <%}%>
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
					       			Ext.getCmp('searchbar').doSearch();
					       		}})
			    			}
			    		});
			    	}},
			    	{text:'删除',iconCls:'page_delete', handler: function() {
			    		Ext.Msg.confirm('提示', '你确定要删除吗?', function(btn) {
			    			if (btn == 'yes') {
			    				Ext.getCmp('formPanel').doDelete({
					    			success:function(form, rep) {
                                        Ext.getCmp('searchbar').doSearch();
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
			items : [gridPanel, formPanel]
		}
		new Ext.Viewport({
			layout:'border',
			items: [menuPanel,tpanel]
		});	
      	Ext.getCmp('grid').on({
	      		'rowclick': function(t, i, e) {
	      		    var type = Ext.getCmp('grid').getSelectionModel().getSelected().data['breedtype'];
	      			var id = t.getSelectionModel().getSelected().data['id'];
	      			Ext.getCmp('formPanel').doLoad({id:id});
      		}
      	});
      	//Ext.getCmp('grade_comb').getEl().up('.x-form-item').setDisplayed(false);
    
    })
    
   </script>
	</body>
</html>
