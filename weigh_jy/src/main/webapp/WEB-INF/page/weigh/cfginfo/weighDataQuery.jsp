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
					{dataIndex: 'water', name:'water', header:'水分',width:60},
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
    		region:'center',
    		height:350,
    		bbar:[new Est.ux.SearchToolbar({id:'searchbar',searchFields:searchFields,searchMapping:searchMapping,gridId:'grid'}), {text:'导出',iconCls:'page_excel', handler:function() {
                var val = Ext.getCmp('companyCombox').getValue();
                if (val == null || val == '') {
                    Ext.Msg.alert("提示", "请先选择商家");
                    return;
                }
                var startTime = Ext.getCmp("startTime").getValue().format('Y-m-d');
                var endTime = Ext.getCmp("endTime").getValue().format('Y-m-d');

                var url='<%=basePath%>est/weigh/cfginfo/WeighData/exportWeighDetail?pz=<%=request.getParameter("pz")%>&startTime='+startTime+'&endTime='+endTime+'&companyCombox='+val;
                frames('filedown').window.location = url;
            }}],
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
			valueField:'companyname',
			hiddenName:'companyname',
			name:'companyname',
			readOnly:true,
			loadingText:'loading…',
			allowBlank:false,
			selectOnFocus: true,
			triggerAction:'all',
			enableKeyEvents:true,
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

		
		var tpanel = {
			xtype : 'estlayout',
			layout:'border',
			region : 'center',
			autoScroll:true,
			items : [gridPanel]
		}
		new Ext.Viewport({
			layout:'border',
			items: [menuPanel,tpanel]
		});
        Ext.getCmp('searchbar').doSearch();
      	//隐藏fieldLabel
      	//Ext.getCmp('water').getEl().up('.x-form-item').setDisplayed(false);
		//Ext.getCmp('jizu_comb').getEl().up('.x-form-item').setDisplayed(false);
		//Ext.getCmp('grade_comb').getEl().up('.x-form-item').setDisplayed(false);
    })
    
   </script>
	</body>
    <iframe id="filedown" style="display: none" src="" />
</html>
