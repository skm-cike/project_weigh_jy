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
	var jizucombo = [['31#_32#','31#_32#'],['33#_34#','33#_34#']];
    Ext.onReady(function(){
    var companyid='';
    var companyname='';
    var companycode='';
    var pagevendorCombox = {
   		xtype:'estcombos',
   		id:'pagevendorCombox',
   		editable:true,
		fieldLabel:'单位',
		displayField:'companyname',
		width:200,
		valueField:'companycode',
		hiddenName:'companycode',
		name:'companycode',
		listeners:{
			'select':function(combo,record){
				 	Ext.getCmp('formPanel').form.findField('companyname').setValue(record.companyname);
			}
		},
		readOnly:false,
		loadingText:'loading…',
		allowBlank:false,
		selectOnFocus: true,
		triggerAction:'all',
		enableKeyEvents:true,
		storeurl:'<%=basePath%>est/weigh/cfginfo/WeighCompany/getCompanyListByPz?pagesiz=max&pz=<%=request.getParameter("pz")%>'}

    	var colsType = {
			id:'priceid',
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
                    <%if ("fenmeihui".equals(request.getParameter("pz")) || "shigao".equals(request.getParameter("pz"))) {%>
					{dataIndex: 'grade', name:'grade', header:'品级'},
                    <%}%>
					{dataIndex: 'unit_price', name:'unit_price', header:'单价'},
					{dataIndex: 'remark', name:'remark', header:'备注'},
                    <%if ("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
                        {dataIndex:'jizu',header:'机组'},
                    <%}%>
					<%if("fenmeihui".equals(request.getParameter("pz"))) {%>
					{dataIndex: 'enableweighcfg',header:'启用重量配置价格', name:'enableweighcfg', renderer: function(v){
						if (v == 1) {
							return '使用';
						} else {
							return '禁用';
						}
					}},
					<%}%>
					{dataIndex: 'priceid', name:'priceid', hidden:true}
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
    		title:'价格列表',
            bbar:new Est.ux.SearchToolbar({id:'searchbar',searchFields:searchFields,searchMapping:searchMapping,gridId:'grid'}),
    		items:{
   				id:'grid',
				xtype:'esteditgrid',
				storeurl:'<%=basePath%>est/weigh/cfginfo/WeighPrice/getWeighPriceList?pz=<%=request.getParameter("pz")%>',
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
    	} else if (pz == 'huizha') {
    		pzdjField = 'HZPZDJ';
    	}
    	
    	var formcols = [
		{fieldset:'价格详细信息',id:'priceid',
			items: [
			    {fieldLabel:'priceid',xtype:'hidden',name:'priceid',hideLabel:true},
			    {fieldLabel:'companyname',xtype:'hidden',name:'companyname',hideLabel:true},
				 pagevendorCombox,
                <%if ("fenmeihui".equals(request.getParameter("pz")) || "shigao".equals(request.getParameter("pz"))) {%>
			    {fieldLabel:'品种等级',name:'grade',id:'grade_comb',hiddenName:'grade',displayField:'propertyname',valueField:'propertyname',xtype:'estpropcombos',propertycode:pzdjField, allowBlank:false},
                <%}%>
                <%if("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
                {fieldLabel:'机组',name:'jizu',hiddenName:'jizu',xtype:'estcombos',elms:jizucombo,allowBlank:false},
                <%}%>
                {fieldLabel:'单价(元/吨)',name:'unit_price', allowBlank:false},
		        {fieldLabel:'备注',name:'remark'},
		        <%if("fenmeihui".equals(request.getParameter("pz"))) {%>
		        {fieldLabel:'重量配置价格',name:'enableweighcfg',listeners:{
		        	'select':function(combo, record) {
		        		if(combo.getValue()==1) {
		        			Ext.getCmp('gridpricepanel').show();
		        		} else {
		        			Ext.getCmp('gridpricepanel').hide();
		        		}
		        	},'change': function(t, newval, oldval) {
		        		if (newval == 1) {
		        			Ext.getCmp('gridpricepanel').show();
		        		} else {
		        			Ext.getCmp('gridpricepanel').hide();
		        		}
		        	}
		        },hiddenName:'enableweighcfg',allowBlank:false,xtype:'estcombos',elms:[[1,'使用'],[0,'禁用']],value:0},
		        <%}%>
		        {fieldLabel:'品名',name:'breedtype',hidden:true,hideLabel:true,value:'<%=request.getParameter("pz")%>'}
                <%if ("huizha".equals(request.getParameter("pz"))) {%>
                ,{name:'grade',hidden:true,hideLabel:true,id:'grade_comb',value:'灰渣'}
                <%}%>
		]}];
		
		var formPanel = {
				xtype: 'estform',
				id: 'formPanel',
				region:'center',
				title:'详细信息',
		       	colnum:1,
		       	formurl: '<%=basePath%>est/weigh/cfginfo/WeighPrice',
				method: 'WeighPrice',
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
		
		var pricecolsType = {
			id:'id',
			cols:[
					{dataIndex: 'limitWeight', name:'limitWeight', header:'重量(吨)>=',editor:new Ext.form.NumberField()},
					{dataIndex: 'weightPrice', name:'weightPrice', header:'单价(元)',editor:new Ext.form.NumberField()},
					{dataIndex: 'priceid', name:'priceid',hidden:true},
					{dataIndex: 'id', name:'id',hidden:true}
			]
		}
		var _pricecolsType = Ext.data.Record.create(pricecolsType.cols);
		var gridpricepanel = {
			xtype:'estlayout',
			region:'south',
			id:'gridpricepanel',
			//title:'重量价格配置',
			tbar:[{text:'添加', handler:function() {
				var priceid = Ext.getCmp('formPanel').form.findField('priceid').getValue();
				if (!priceid || priceid == '') {
					Ext.Msg.alert('提示', '没有保存价格基础信息?');
					return;
				}
	
				var v_colsType = new _pricecolsType({
		                    limitWeight: '',
		                    weightPrice: '',
		                    priceid:priceid
		                });
				var grid = Ext.getCmp('pricegrid').addRow(v_colsType);
			}}, {
				text:'删除', handler:function() {
					var priceid = Ext.getCmp('formPanel').form.findField('priceid').getValue();
					if (!priceid || priceid == '') {
						Ext.Msg.alert('提示', '没有保存价格基础信息?');
						return;
					}
					Ext.getCmp('pricegrid').deleteRow();
				}
			}, {text:'保存', handler:function() {
					var priceid = Ext.getCmp('formPanel').form.findField('priceid').getValue();
					if (!priceid || priceid == '') {
						Ext.Msg.alert('提示', '没有保存价格基础信息?');
						return;
					}
					Ext.getCmp('pricegrid').onSave('<%=basePath%>est/weigh/cfginfo/WeighPrice/savWeighWeightPriceList', null, {priceid:priceid});
					Ext.getCmp('pricegrid').doSearch({priceid:priceid});
			}}],
			height:180,
			listeners:{
				'show':function() {
					var priceid = Ext.getCmp('formPanel').form.findField('priceid').getValue();
					if (!priceid || priceid == '') {
						//Ext.getCmp('gridpricepanel').hide();
						Ext.getCmp('pricegrid').doSearch();
						return;
					}
					Ext.getCmp('pricegrid').doSearch({priceid:priceid});
				}
			},
			items:{
   				id:'pricegrid',
				xtype:'esteditgrid',
				storeurl:'<%=basePath%>est/weigh/cfginfo/WeighPrice/getWeighWeightPriceList',
				colstype : pricecolsType,
				autoLoad:false,
				region:'center'
			}
		}
			
		var rightPanel = {
			xtype : 'estlayout',
			layout:'border',
			width:350,
			id:'rightPanel',
			region: 'east',
			items:[gridpricepanel, formPanel]
		}
		
		var tpanel = {
			xtype : 'estlayout',
			layout:'border',
			region : 'center',
			items : [gridPanel, rightPanel]
		}
		new Ext.Viewport({
			layout:'border',
			items: [menuPanel,tpanel]
		});
		Ext.getCmp('gridpricepanel').hide();
      	Ext.getCmp('grid').on({
	      		'rowclick': function(t, i, e) {
	      		    var type = Ext.getCmp('grid').getSelectionModel().getSelected().data['breedtype'];
				    //noneOrBlock(type,'fenmeihui','grade_comb');
	      			var priceid = t.getSelectionModel().getSelected().data['priceid'];
	      			//Ext.getCmp('pagevendorCombox').value=companyname;
	      			Ext.getCmp('formPanel').doLoad({priceid:priceid}, function(form,action) {
	      				<%if("fenmeihui".equals(request.getParameter("pz"))) {%>
		      				if(form.findField('enableweighcfg').getValue()==1) {
		      					Ext.getCmp('gridpricepanel').show();
		      					Ext.getCmp('pricegrid').doSearch({priceid:priceid});
		      				} else {
		      					Ext.getCmp('gridpricepanel').hide();
		      				}
	      				<%}%>
	      			});
      		}
      	});
      	//Ext.getCmp('grade_comb').getEl().up('.x-form-item').setDisplayed(false);
    
    })
    
   </script>
	</body>
</html>
