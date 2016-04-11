<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.est.common.ext.util.classutil.DateUtil,com.est.sysinit.sysuser.vo.SysUser"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			String startTime = DateUtil.format(DateUtil.add(new Date(),
			DateUtil.DAY_OF_MONTH, -30));
	String salebuy = "1".equals(request.getParameter("inouttype"))?"买方":"卖方";
	SysUser user = (SysUser) request.getSession().getAttribute("loginUser");
	String deptname = user.getSysDept().getDeptname();
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
	var pz = ['fenmeihui', 'shigao', 'shihuishi', 'suan','jian','yean'];
	var buyer = [{boxLabel:'粉煤灰',name:'fenmeihui'},{boxLabel:'石膏',name:'shigao'},{boxLabel:'灰渣',name:'huizha'}];
	var saler = [{boxLabel:'石灰石',name:'shihuishi'},{boxLabel:'酸',name:'suan'},{boxLabel:'碱',name:'jian'},{boxLabel:'液氨',name:'yean'}];
	var inouttype;
	var thistype="<%=request.getParameter("pz")%>";
	if ("shigao"==thistype || "fenmeihui"==thistype || "huizha"==thistype) {
		inouttype=1;
	} else {
		inouttype=2;
	}
	
	var companycode = "";
    Ext.onReady(function(){
    	var colsType = {
			id:'companyid',
			cols:[
					{dataIndex: 'companycode', name:'companycode', header:'单位编码'},
					{dataIndex: 'companyname', name:'companyname', header:'单位名称'},
					{dataIndex: 'phone', name:'phone', header:'电话'},
					{dataIndex: 'atten', name:'atten', header:'联系人'},
					{dataIndex: 'address', name:'address', header:'地址'},
					{dataIndex: 'status', name:'status', header:'状态', renderer: function(v) {
						if (v =='0') {
							return '停用';
						} else {
							return '启用';
						}
					}},
					{dataIndex: 'inouttype', name:'inouttype',header:'类别',renderer:function(v){
						if (v==1) {
							return '买方';
						} else {
							return '卖方';
						}
					}},
					{dataIndex: 'type', name:'type',header:'品名',renderer:function(v){
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
		              	  else if (v == 'huizha') {
		              		return '灰渣';
		              	  }
			            }},
					{dataIndex: 'remark', name:'remark', header:'备注'},
					{dataIndex: 'companyid', name:'companyid', hidden:true}
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
				storeurl:'<%=basePath%>est/weigh/cfginfo/WeighCompany/getCompanyList?pz=<%=request.getParameter("pz")%>',
				colstype : colsType,
				region:'center'
			}
    	}
    	
    	
    	var formcols = [
		{fieldset:'单位详细信息',id:'companyid',
			items: [
				{fieldLabel:'单位编码',name:'companycode'},
				{fieldLabel:'单位名称',name:'companyname',allowBlank:false},
		        {fieldLabel:'状态',name:'status',hiddenName:'status',xtype:'estcombos',elms:[['1','启用'],['0','停用']],value:'1'},
		        {fieldLabel:'电话',name:'phone'},
		        {fieldLabel:'联系人',name:'atten'},
		        {fieldLabel:'地址',name:'address'},
		        {fieldLabel:'备注',name:'remark'},
		        {hidden:true,hideLabel:true,value:'<%=request.getParameter("pz")%>',name:'type'},
		        {name:'inouttype',hidden:true,hideLabel:true,value:inouttype}   //买方，卖方
		]}];
		
		var formPanel = {
				xtype: 'estform',
				id: 'formPanel',
				region:'east',
				title:'详细信息',
		       	width:350,
		       	colnum:1,
		       	formurl: '<%=basePath%>est/weigh/cfginfo/WeighCompany',
				method: 'WeighCompany',
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
			    	{text:'删除',iconCls:'page_gear', handler: function() {
			    		Ext.Msg.confirm('提示', '你确定要删除吗?', function(btn) {
			    			if (btn == 'yes') {
			    				Ext.getCmp('formPanel').doDelete({
					    			success:function(form, rep) {
                                        Ext.getCmp('searchbar').doSearch();
					    			}
					    		});
			    			}
			    		});
			    	}},
				    	{
				    		text:'完全更新客户端信息',iconCls:'page_refresh', handler: function() {
				    			Ext.Ajax.request({
				    				url:'<%=basePath%>est/weigh/cfginfo/WeighCompany/updateClientInfo',
				    				success:function(rep) {
				    					rep=Ext.decode(rep.responseText);
				    					if (rep.success) {
				    						info('信息', '更新成功!');
				    					} else {
				    						alert('错误', rep.error);
				    					}
				    				},
				    				failure:function() {
				    					alert('错误', '更新失败');
				    				}
				    			});
				    		}
				    	}
			    ]
			}
		
		var tpanel = {
			xtype : 'estlayout',
			layout:'border',
			region : 'center',
			items : [gridPanel, formPanel]
		}
		
		
		//=================车辆=======================
    	var v_colsType = {
			id:'vehicleid',
			cols:[
					{dataIndex: 'vehicleno', name:'vehicleno', header:'车牌号'},
					{dataIndex: 'remark', name:'remark', header:'备注'},
					{dataIndex: 'inputman', name:'inputman', header:'输入人'},
					{dataIndex: 'inputtime', name:'inputtime', header:'修改时间'},
					{dataIndex: 'sysDept.deptid', name:'sysDept.deptid', hidden:true},
					{dataIndex: 'vehicleweight', name:'vehicleweight', hidden:true},
					{dataIndex: 'vehicleid', name:'vehicleid', hidden:true}
			]
		}

    	var v_gridPanel = {
    		xtype:'estlayout',
    		region:'center',
    		title:'车辆信息',
    		items:{
   				id:'v_grid',
				xtype:'esteditgrid',
				storeurl:'<%=basePath%>est/weigh/cfginfo/WeighVehicle/getWeighVehicleList',
				colstype : v_colsType,
				region:'center',
				autoLoad:false
			}
    	}
    	
    	
    	var v_formcols = [
		{fieldset:'车辆详细信息',id:'vehicleid',
			items: [
				{fieldLabel:'车牌号',name:'vehicleno',allowBlank:false},
				{fieldLabel:'备注',name:'remark'},
		        {name:'inputtime', hidden:true,hideLabel:true},  //更新时间
		        {name:'inputman', hidden:true,hideLabel:true},  //录入人
		        {name:'sysDept.deptid', hidden:true,hideLabel:true},  //部门id
		        {name:'vehicleweight', hidden:true,hideLabel:true},  //车重
		        {name:'companycode', hidden:true,hideLabel:true},  //单位代码
		        {name:'companyname', hidden:true,hideLabel:true},  //单位名称
		        {name:'companyid', hidden:true,hideLabel:true}  //单位id
		]}];
		
		var v_formPanel = {
				xtype: 'estform',
				id: 'v_formPanel',
				region:'east',
				title:'详细信息',
		       	width:350,
		       	colnum:1,
		       	formurl: '<%=basePath%>est/weigh/cfginfo/WeighVehicle',
				method: 'WeighVehicle',
				colstype: v_formcols,
				tbar:[
		       		{text:'重置/增加',iconCls:'page_add', handler:function(){
						Ext.getCmp('v_formPanel').doReset();
			    	}},
			    	{text:'保存',iconCls:'page_save', handler: function() {
			    		var selected = Ext.getCmp('grid').getSelectionModel().getSelected();
			    		if (!selected) {
			    			Ext.Msg.alert('提示', "请先选择一个单位!");
			    			return;
			    		}
			    		if (!companycode || companycode == '') {
			    			Ext.Msg.alert('提示', "单位编码错误!");
			    			return;
			    		}
			    		var form  = Ext.getCmp('v_formPanel');
			    		form.form.findField('companycode').setValue(companycode);
			    		form.form.findField('companyname').setValue(companyname);
			    		form.form.findField('companyid').setValue(companyid);
			    		Ext.Msg.confirm('提示', '你确定要增加或修改吗?', function(btn) {
			    			if (btn == 'yes') {
			    				form.doSumbit({success:function(form, rep){
					       			Ext.getCmp('v_grid').store.reload();
					       		},failure:function() {error('错误','保存失败~!')}})
			    			}
			    		});
			    	}},
			    	{text:'删除',iconCls:'page_gear', handler: function() {
			    		Ext.Msg.confirm('提示', '你确定要删除吗?', function(btn) {
			    			if (btn == 'yes') {
			    				Ext.getCmp('v_formPanel').doDelete({
					    			success:function(form, rep) {
					    				Ext.getCmp('v_grid').store.reload();
					    			}
					    		});
			    			}
			    		});
			    	}}
			    ]
			}
		
		var v_tpanel = {
				xtype : 'estlayout',
				layout:'border',
				region : 'center',
				items : [v_gridPanel, v_formPanel]
		}
		
		var tpanel = {
			xtype : 'estlayout',
			layout:'border',
			region: 'north',
			height:300,
			items : [gridPanel, formPanel]
		}
		
		var warppanel = {
			xtype:'estlayout',
			layout:'border',
			region:'center',
			items:[tpanel,v_tpanel]
		}
		
		new Ext.Viewport({
			layout:'border',
			items: [menuPanel,warppanel]
		});	
      	Ext.getCmp('grid').on({
	      		'rowclick': function(t, i, e) {
	      			var selected = t.getSelectionModel().getSelected();
	      			window.companyid = selected.data['companyid'];
	      			window.companycode = selected.data['companycode'];
	      			window.companyname=selected.data['companyname'];
	      			Ext.getCmp('formPanel').doLoad({companyid:companyid});
	      			Ext.getCmp('v_grid').doSearch({companyid:window.companyid},true);
      		}
      	});
      	Ext.getCmp('v_grid').on({
      		'rowclick': function(t, i, e) {
      			var vehicleid = t.getSelectionModel().getSelected().data['vehicleid'];
      			Ext.getCmp('v_formPanel').doLoad({vehicleid:vehicleid});
  		}
  	});
    })
    
   </script>
	</body>
</html>
