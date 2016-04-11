<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.est.common.ext.util.classutil.DateUtil"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
    String startTime = DateUtil.format(DateUtil.add(new Date(),
    DateUtil.DAY_OF_MONTH, -1));
    String endTime = DateUtil.format(new Date());
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>过磅数据录入</title>
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
    	var colsType = {
			id:'id',
			cols:[
			        {dataIndex: 'id', name:'id', header:'id',hidden:true},
					{dataIndex: 'pounds_number', name:'pounds_number',header:'磅单号',width:100},
					{dataIndex: 'vehicle_no', name:'vehicle_no', header:'车号',width:80},
					{dataIndex: 'vehicle_weight', name:'companycode', header:'车重',width:60},
					{dataIndex: 'gross_weight', name:'gross_weight', header:'毛重',width:60},
					{dataIndex: 'de_weight', name:'de_weight', header:'扣吨',width:60},
					{dataIndex: 'net_weight', name:'net_weight', header:'净重',width:60},
					{dataIndex: 'companycode', name:'companycode',hidden:true},
					{dataIndex: 'companyname', name:'companyname', header:'单位'},
					{dataIndex: 'type', name:'type', header:'品种',renderer:function(v){
					    if(v=='shigao')
			                return '石膏';
			             else if(v=='fenmeihui')
			                return '粉煤灰';
			             else if(v=='shihuishi')
			                return '石灰石';
			              else if (v=='yean') 
			              	return '液氨';
		              	  else if (v=='suan') 
			              	return '酸';
		              	  else if (v=='jian') 
			              	return '碱';
		              	  else if (v=='huizha') 
		              		  return '灰渣';
					},width:60},
					<%if ("fenmeihui".equals(request.getParameter("pz")) || "shigao".equals(request.getParameter("pz"))) {%>
					{dataIndex: 'grade', name:'grade',header:'品种等级',width:60},
					<%}%>
					<%if ("fenmeihui".equals(request.getParameter("pz"))) {%>
					{dataIndex: 'jizu', name:'jizu',header:'机组',width:60},
					<%}%>
					<%if ("shigao".equals(request.getParameter("pz"))) {%>
//					{dataIndex: 'water', name:'water', header:'水分',width:60},
					<%}%>
					<%if ("shigao".equals(request.getParameter("pz")) || "fenmeihui".equals(request.getParameter("pz"))) {%>
					{dataIndex: 'unit_price', name:'unit_price',header:'单价',width:60},
					{dataIndex: 'total_price', name:'total_price', header:'总价',width:60},
					<%}%>
					{dataIndex: 'gross_time', name:'gross_time', header:'毛重时间',width:130},
					{dataIndex: 'vehicle_time', name:'vehicle_time',header:'皮重时间',width:130},
					{dataIndex: 'weighman', name:'weighman',header:'毛重司磅人',width:70},
					{dataIndex: 'tareweightman', name:'tareweightman',header:'皮重司磅人',width:70},
					{dataIndex: 'inouttype', name:'inouttype',header:'类别',hidden:true},
					{dataIndex: 'isclosed', name:'isclosed',header:'isclosed',hidden:true},
					{dataIndex: 'remark', name:'remark', header:'备注'}
			]
		}
		
		 var companyCombox = {
    		xtype:'estcombos',
    		id:'companyCombox',
    		editable:true,
			fieldLabel:'单位',
			displayField:'companyname',
			valueField:'companyname',
			hiddenName:'companyname',
			name:'companyname',
			readOnly:true,
			loadingText:'loading…',
			selectOnFocus: true,
			triggerAction:'all',
			enableKeyEvents:true,
			storeurl:'<%=basePath%>est/weigh/cfginfo/WeighCompany/getCompanyListByPz?pagesiz=max&pz=<%=request.getParameter("pz")%>'
		}
		
		// 搜索字段
		var searchFields=[	   
			'时间:',{xtype:'datefield',id:'startTime',format:'Y-m-d',width:100,value:'<%=startTime%>'},
	        '-'	  ,{xtype:'datefield',id:'endTime',format:'Y-m-d',width:100,value:'<%=endTime%>'},
	        '单位:',companyCombox
	      ];
	  	// 搜索字段映射
	  	var searchMapping={'startTime':'startTime','endTime':'endTime','companyCombox':'companyCombox'};
		
		
    	var gridPanel = {
    		xtype:'estlayout',
    		region:'north',
    		height:350,
    		bbar:new Est.ux.SearchToolbar({id:'searchbar',searchFields:searchFields,searchMapping:searchMapping,gridId:'grid'}),
    		items:{
   				id:'grid',
				xtype:'esteditgrid',
				storeurl:'<%=basePath%>est/weigh/cfginfo/WeighData/getWeighDataList?pz=<%=request.getParameter("pz")%>',
				colstype : colsType,
				region:'center',
                autoLoad:false
			}
    	}
    	
    	 var pagevendorCombox = {
    		xtype:'estcombos',
    		id:'pagevendorCombox',
    		editable:true,
			fieldLabel:'单位',
			displayField:'companyname',
			valueField:'companycode',
			hiddenName:'companycode',
			name:'companycode',
			readOnly:true,
			loadingText:'loading…',
			allowBlank:false,
			selectOnFocus: true,
			triggerAction:'all',
			enableKeyEvents:true,
			listeners:{select:function(combo,record,index) {
				Ext.getCmp('formPanel').form.findField('companyname').setValue(record.data['companyname']);
			}},
			storeurl:'<%=basePath%>est/weigh/cfginfo/WeighCompany/getCompanyListByPz?pz=<%=request.getParameter("pz")%>'
		}
		
		
    	   
    	//控件隐藏或显示
    	function noneOrBlock(selec_val,compare_val,fieldname){
	    	    if(selec_val==compare_val){
				    Ext.getCmp(fieldname).getEl().up('.x-form-item').setDisplayed(true);
				    //Ext.getCmp(fieldname).show();
				  }else{
				    Ext.getCmp(fieldname).getEl().up('.x-form-item').setDisplayed(false);
				    //Ext.getCmp(fieldname).hide();
				  }
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
			{fieldset:'过磅数据详细信息',id:'id',
				items: [
				  {fieldLabel:'磅单号', name:'pounds_number',readOnly:true},
				   {fieldLabel:'车号',name:'vehicle_no',readOnly:true},
					{fieldLabel:'车重',name:'vehicle_weight',readOnly:true},
					{fieldLabel:'毛重',name:'gross_weight',readOnly:true},
					{fieldLabel:'扣吨(扣重)',name:'de_weight',readOnly:true},
					 <%if("fenmeihui".equals(request.getParameter("pz"))) {%>
					 {fieldLabel:'品种等级',name:'grade',
                     allowBlank:false,readOnly:true
		             },<%} %>

                    <%if("shigao".equals(request.getParameter("pz"))) {%>
                    {fieldLabel:'品种等级',name:'grade',id:'grade_comb',hiddenName:'grade',
                        displayField:'propertyname',
                        valueField:'propertyname',
                        xtype:'estpropcombos',
                        propertycode:pzdjField,
                        allowBlank:false
                    },<%} %>

                    <%if("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
                    {fieldLabel:'机组',name:'jizu',id:'jizu_comb',readOnly:true},
                    <%} %>
                    <%if("fenmeihui".equals(request.getParameter("pz")) || "shigao".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
			        //{fieldLabel:'水分',name:'water',id:'water'},
			        {fieldLabel:'单价(不填写自动匹配)',name:'unit_price'},
			         <%}%>
                    {fieldLabel:'单位',name:'companyname', readOnly:true},
					{fieldLabel:'毛重时间',name:'gross_time',readOnly:true},
					{fieldLabel:'皮重时间',name:'vehicle_time',readOnly:true},
			        {fieldLabel:'毛重司磅人',name:'weighman',readOnly:true},
			        {fieldLabel:'皮重司磅人',name:'tareweightman',readOnly:true},
			        {fieldLabel:'备注',name:'remark',readOnly:true},
			        {fieldLabel:'更新人',name:'transferman',hidden:true,hideLabel:true},
                   {name:'isclosed', hidden:true,hideLabel:true},
                   {name:'inouttype',hidden:true,hideLabel:true},
                   {fieldLabel:'总价',name:'total_price',hidden:true,hideLabel:true},	        	        
                   {name:'operatTime',hidden:true,hideLabel:true},
                   {name:'outno',hidden:true,hideLabel:true},
                   {name:'banbie',hidden:true,hideLabel:true},
                   {name:'dayreportMark',hidden:true,hideLabel:true},
                   {name:'monthreportMark',hidden:true,hideLabel:true}
                    <%if("huizha".equals(request.getParameter("pz"))) {%>
                    ,{fieldLabel:'品种等级',name:'grade',hidden:true,hideLabel:true}<%} %>

		        ]
		      }
		   ];
		
		var formPanel = {
				xtype: 'estform',
				id: 'formPanel',
				region:'center',
				title:'详细信息',
		       	height:250,
		       	colnum:4,
		       	formurl: '<%=basePath%>est/weigh/cfginfo/WeighData',
				method: 'WeighData',
				colstype: formcols,
				tbar:[
			    	{text:'保存',iconCls:'page_save', handler: function() {
			    		var tform = Ext.getCmp('formPanel');
			    		if ( tform.form.findField('isclosed').getValue() == 'Y')
			    		{
			    			Ext.Msg.alert('提示', '该数据已经月结，不能修改');
			    			return;
			    		}
			    		Ext.Msg.confirm('提示', '你确定要增加或修改吗?', function(btn) {
			    			if (btn == 'yes') {
			    				Ext.getCmp('formPanel').doSumbit({success:function(form, rep){
					       			Ext.getCmp('searchbar').doSearch();
					       		},params:{pz:'<%=request.getParameter("pz")%>'}})
			    			}
			    		});
			    	}}
			    ]
			}
		
		var tpanel = {
			xtype : 'estlayout',
			layout:'border',
			region : 'center',
			autoScroll:true,
			items : [gridPanel, formPanel]
		}
		new Ext.Viewport({
			layout:'border',
			items: [menuPanel,tpanel]
		});	
      	Ext.getCmp('grid').on({
	      		'rowclick': function(t, i, e) {
	      		  var type = Ext.getCmp('grid').getSelectionModel().getSelected().data['type'];
	      		  //noneOrBlock(type,'shigao','water');
				  //noneOrBlock(type,'fenmeihui','jizu_comb');
				 // noneOrBlock(type,'fenmeihui','grade_comb');
	      		  var id = t.getSelectionModel().getSelected().data['id'];
	      		  Ext.getCmp('formPanel').doLoad({id:id});
      		}
      	});
        Ext.getCmp('searchbar').doSearch();
      	//隐藏fieldLabel
      	//Ext.getCmp('water').getEl().up('.x-form-item').setDisplayed(false);
		//Ext.getCmp('jizu_comb').getEl().up('.x-form-item').setDisplayed(false);
		//Ext.getCmp('grade_comb').getEl().up('.x-form-item').setDisplayed(false);
    })
    
   </script>
	</body>
</html>
