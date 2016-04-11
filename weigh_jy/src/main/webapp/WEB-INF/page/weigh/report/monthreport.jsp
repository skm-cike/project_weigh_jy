<%@ page language="java" import="java.util.*,com.est.common.ext.util.classutil.DateUtil" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String thismonth = DateUtil.format(new Date(), "yyyy-MM");
String thisdate = DateUtil.format(new Date(), "yyyy-MM-dd");
String thisyear = thismonth.substring(0, 4);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
	
    <title>月报</title>
    <%@ include file="/include.jsp"%>	
    <script type="text/javascript">
    	Ext.onReady(function(){
    			//供应商下拉列表
	    	    var pagevendorCombox = {
		   		xtype:'estcombos',
		   		id:'pagevendorCombox',
		   		editable:true,
				fieldLabel:'单位',
				displayField:'companyname',
				//width:100,
				valueField:'companycode',
				hiddenName:'companycode',
				name:'companycode',
				listeners:{
					'select':function(combo,record){
						 	//Ext.getCmp('formPanel').form.findField('companyname').setValue(record.data.companyname);
					}
				},
				readOnly:false,
				loadingText:'loading…',
				allowBlank:false,
				selectOnFocus: true,
				triggerAction:'all',
				enableKeyEvents:true,
				storeurl:'<%=basePath%>est/weigh/cfginfo/WeighCompany/getCompanyListByPz?pagesiz=max&pz=<%=request.getParameter("pz")%>'
			}
    	
			var colsType = {
				cols:[
					<%if ("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
    				{dataIndex:'jizu', header:'机组', name:'jizu',width:150},
    				<%}%>
					{dataIndex:'companyname',	header:'单位名称',name:'companyname',width:75},
    				{dataIndex:'companycode',	header:'单位代码',name:'companycode',hidden:true},
    				<%if ("fenmeihui".equals(request.getParameter("pz"))||"shigao".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
    				{dataIndex:'jzje', header:'结转金额(元)', name:'jzje',width:150, editor: new Ext.form.NumberField({decimalPrecision:2})},
    				{dataIndex:'yfk', header:'预收货款(元)', name:'yfk',width:150},
    				<%}%>
    				{dataIndex:'chl', header:'出货量(吨)', name:'chl',width:150}
    				<%if ("fenmeihui".equals(request.getParameter("pz"))||"shigao".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
    				,{dataIndex:'xsjezj', header:'销售金额合计(元)', name:'xsjezj',width:150},
    				{dataIndex:'hkye', header:'货款余额(元)', name:'hkye',width:150}
    				<%}%>
				]
			}
			
			/*
			var searchFields=[	   
				'日期:',{xtype:'datefield',id:'searchDate',format:'Y-m',width:100, value:'<%=thismonth%>', allowBlank:false}
				<%if ("fenmeihui".equals(request.getParameter("pz"))) {%>
				,'机组', {id:'jizu_comb',xtype: 'estcombos', width: 80, hiddenName:'jizu', elms: [['31#_32#','31#_32#'],['33#_34#','33#_34#']]}
				<%}%>
	      	];
  			var searchMapping={'searchDate':'searchDate'
  				<%if ("fenmeihui".equals(request.getParameter("pz"))) {%>
  				,'jizu_comb':'jizu_comb'
  				<%}%>
  			};
			*/
   			var GridPanel = {
    			id:'GridPanel',
   				xtype: 'estlayout',
   				region:'center',
   				split:true,//显示分隔条
   				collapsible:true,
   				tbar:[
   				{text:'生成月报', iconCls:'page_attach', handler: function() {
   					var win = Ext.getCmp('createReport');
   					if (!win) {
   						win = new Ext.Window({
							id:'createReport',
							title:'生成月报',
							width:300,
							height:220,
							closeAction:'hide',
							frame:true,
							plain:true,
							items:[{
								xtype:'estform',
								id:'dayreportform',
								colnum:1,
								colstype:[{
										items:[
										{fieldLabel:'报表月份',xtype:'datefield',format:'Y-m',name:'reportdate',allowBlank:false,width:150, value:"<%=thismonth%>"},
										{fieldLabel:'开始日期',xtype:'datefield',format:'Y-m-d',name:'startdate',allowBlank:false,width:150, value:"<%=thisdate%>"},
										{fieldLabel:'结束日期',xtype:'datefield',format:'Y-m-d',name:'enddate',allowBlank:false,width:150, value:"<%=thisdate%>"}]
									}
								]
							}],
							buttons:[{
								text:'确定',
								handler:function(){
									if (!Ext.getCmp('dayreportform').form.isValid()) {
										return;
									}
									var startdate = Ext.getCmp('dayreportform').form.findField('startdate').getValue();
									if (!startdate) {
										return;
									}
									startdate = startdate.format('Y-m-d');
									var enddate = Ext.getCmp('dayreportform').form.findField('enddate').getValue();
									if (!enddate) {
										return;
									}
									enddate = enddate.format('Y-m-d');
									var reportdate = Ext.getCmp('dayreportform').form.findField('reportdate').getValue();
									if (!reportdate) {
										return;
									}
									reportdate = reportdate.format('Y-m');
									<%if ("fenmeihui".equals(request.getParameter("pz"))) {%>
									var jizu = Ext.getCmp('dayreportform').form.findField('jizu');
									if (jizu) {
										jizu = jizu.getValue();
									} else {
										jizu = '';
									}
									<%}%>
									Ext.Ajax.request({
										url:"<%=basePath%>est/weigh/report/MonthReport/createReport?reportdate=" + reportdate + "&startdate=" + startdate + '&enddate=' + enddate + '&pz=<%=request.getParameter("pz")%>' <%if ("fenmeihui".equals(request.getParameter("pz"))) {%> +  '&jizu='+jizu<%}%>,
										success:function(response){
											var responseJson = Ext.decode(response.responseText);
											if(responseJson.success){
												info('提示', '生成成功');
												Ext.getCmp('leftpanelid').doSearch();
												win.close();
											}else{
												error("错误", responseJson.error);
											}
										},
										failure:function(){
											alert("服务器无响应,请确认服务器是否启动!");
										}
									});
								}
							},{
								text:'取消',
								handler:function(){
									win.hide();
								}
							}]
						
						});
   					}
   					win.show();
   				}}, '-',{
   					text:'导出月报',iconCls:'page_excel',handler:function() {
   						var year = Ext.getCmp('yearCombox').getValue();
						var selected = Ext.getCmp('leftpanelid').getSelectionModel().getSelected();
	   					var month='';
	   					var jizu='';
	   					if (!selected) {
	   						return;
	   					}
	   					month = selected.data['month'];
	   					var yearmonth = year + '-' + month;
	   					<%if("fenmeihui".equals(request.getParameter("pz"))) {%>
	   						var _jizu = Ext.getCmp('_leftjizu').getValue();
	   						jizu = _jizu.replace(/#/g,"%23");;
	   					<%}%>
   						var url='<%=basePath%>est/weigh/report/MonthReport/exportMonthReport?pz=<%=request.getParameter("pz")%>&yearmonth=' + yearmonth<%if ("fenmeihui".equals(request.getParameter("pz"))) {%> + "&jizu=" + jizu<%}%>;
       				    frames('filedown').window.location = url;
   					}
   				}<%if ("fenmeihui".equals(request.getParameter("pz")) || "shigao".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>, {
		   					text:'导出季报',iconCls:'page_excel',handler:function() {
			   					var win = Ext.getCmp('jibaowin');
			   					if (!win) {
			   						win = new Ext.Window({
										id:'jibaowin',
										title:'生成季报',
										width:300,
										height:180,
										closeAction:'hide',
										frame:true,
										plain:true,
										items:[{
											xtype:'estform',
											id:'jibaoform',
											colnum:1,
											colstype:[{
													items:[
													{fieldLabel:'报表年份',xtype:'datefield',format:'Y',name:'reportyear',allowBlank:false,width:150, value:"<%=thisyear%>"},
													{fieldLabel:'季度', name:'reporseason',allowBlank:false,width:150,xtype:'estcombos',elms:[['1','一季度'],['2','二季度'],['3','三季度'],['4','四季度']]}
													<%if ("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>,{fieldLabel:'机组',xtype:'estcombos',hiddenName:'jizu',elms: [['31#_32#','31#_32#'],['33#_34#','33#_34#']]}<%}%>
												]
										}]}],
										buttons:[{
											text:'确定',
											handler:function(){
												var form = Ext.getCmp('jibaoform').form;
												if (form.isValid()) {
													var reportyear = form.findField('reportyear').getValue().format('Y');
													var reporseason = form.findField('reporseason').getValue();
													<%if ("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
													var jizu = form.findField('jizu').getValue();
													jizu = jizu.replace(/#/g,"%23");
													<%}%>
													var url='<%=basePath%>est/weigh/report/MonthReport/exportQuarterReport?pz=<%=request.getParameter("pz")%>&reportyear=' 
																	+ reportyear + '&reporseason=' + reporseason<%if ("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%> + '&jizu=' + jizu<%}%>;
		       				   						 frames('filedown').window.location = url;
		       				   						 Ext.getCmp('jibaoform').form.reset();
													win.hide();
												}
											}
										},{
											text:'取消',
											handler:function(){
												Ext.getCmp('jibaoform').form.reset();
												win.hide();
											}
										}]
									
									});
			   					}
			   					win.show();
   					}
   				},{
   					text:'导出年报',iconCls:'page_excel',handler:function() {
   						var win = Ext.getCmp('nianbaowin');
			   					if (!win) {
			   						win = new Ext.Window({
										id:'nianbaowin',
										title:'生成年报',
										width:300,
										height:180,
										closeAction:'hide',
										frame:true,
										plain:true,
										items:[{
											xtype:'estform',
											id:'nianbaoform',
											colnum:1,
											colstype:[{
													items:[
													{fieldLabel:'报表年份',xtype:'datefield',format:'Y',name:'reportyear',allowBlank:false,width:150, value:"<%=thisyear%>"}
													<%if ("fenmeihui".equals(request.getParameter("pz"))) {%>,{fieldLabel:'机组',xtype:'estcombos',hiddenName:'jizu',elms: [['31#_32#','31#_32#'],['33#_34#','33#_34#']]}<%}%>
												]
										}]}],
										buttons:[{
											text:'确定',
											handler:function(){
												var form = Ext.getCmp('nianbaoform').form;
												if (form.isValid()) {
													var reportyear = form.findField('reportyear').getValue().format('Y');
													<%if ("fenmeihui".equals(request.getParameter("pz"))) {%>
													var jizu = form.findField('jizu').getValue();
													jizu = jizu.replace(/#/g,"%23");
													<%}%>
													var url='<%=basePath%>est/weigh/report/MonthReport/exportYearReport?pz=<%=request.getParameter("pz")%>&reportyear='+reportyear<%if ("fenmeihui".equals(request.getParameter("pz"))) {%> + '&jizu='+jizu<%}%>;
       				   								 frames('filedown').window.location = url;
		       				   						 Ext.getCmp('nianbaoform').form.reset();
													win.hide();
												}
											}
										},{
											text:'取消',
											handler:function(){
												Ext.getCmp('nianbaoform').form.reset();
												win.hide();
											}
										}]
									
									});
			   					}
			   					win.show();
   					}
   				},{text:'保存结转金额', iconCls:'table_save',handler: function() {
                        var selected = Ext.getCmp('leftpanelid').getSelectionModel().getSelected();
                        if (!selected) {
                            return;
                        }
                        var year = selected.data['year'];
                        var month = selected.data['month'];
                        var params = {yearmonth:year+'-'+month};
                        <%if("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
                        var jizu = Ext.getCmp('_leftjizu').getValue();
                        params.jizu = jizu;
                        <%}%>
                        Ext.getCmp('gridpanelid').onSave('<%=basePath%>est/weigh/report/MonthReport/trimJzje?pz=<%=request.getParameter("pz")%>', null, params);
   				}}<%}%>],
	   			items:[
	   				{xtype: 'esteditgrid',id:'gridpanelid', pageSize:1000,storeurl: '<%=basePath%>est/weigh/report/MonthReport/getReport?pz=<%=request.getParameter("pz")%>', colstype: colsType, region: 'center', autoLoad:false}
	   			]
    		}
   			
   			//年度下拉列表
   			var yearCombox = {
		   		xtype:'estcombos',
		   		id:'yearCombox',
		   		editable:true,
				fieldLabel:'年度',
				displayField:'year',
				width:70,
				valueField:'year',
				hiddenName:'year',
				name:'year',
				listeners:{
					'select':function(combo,record){
						Ext.getCmp('leftsearchbar').doSearch();
					}
				},
				readOnly:false,
				loadingText:'loading…',
				allowBlank:false,
				selectOnFocus: true,
				triggerAction:'all',
				enableKeyEvents:true,
				value:'<%=thisyear%>',
				storeurl:'<%=basePath%>est/weigh/report/MonthReport/getYearListByPz?pagesiz=max&pz=<%=request.getParameter("pz")%>'
			}
   			
   			var leftcolstype = {
				cols:[
					{dataIndex:'month',	header:'月份',name:'month',width:45},
    				{dataIndex:'vehicles',	header:'车数',name:'vehicles',width:45},
    				{dataIndex:'weight', header:'重量',name:'weight',width:45},
    				{dataIndex:'date', header:'日期',name:'date',width:150},
    				{dataIndex:'year', header:'年份',name:'date',hidden:true}
				]
			}
   			
   			var leftsearchFields=[	   
   							'年度:', yearCombox
   							<%if("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
   							 ,'机组', {id:'_leftjizu',xtype: 'estcombos', width: 80, hiddenName:'leftjizu',name:'leftjizu', elms: [['31#_32#','31#_32#'],['33#_34#','33#_34#']],value:'31#_32#',listeners:{'select':function(combo){
					Ext.getCmp('leftsearchbar').doSearch();
				}}}
   							<%}%>
   				      	];
	  		// 搜索字段映射
  			var leftsearchMapping={'yearCombox':'yearCombox'<%if("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>, '_leftjizu':'_leftjizu'<%}%>};
   			
   			var leftPanel = {
   	   				xtype: 'estlayout',
   	   				region:'west',
   	   				width:320,
					bbar:[new Est.ux.SearchToolbar({id:'leftsearchbar',searchFields:leftsearchFields,searchMapping:leftsearchMapping,gridId:'leftpanelid'}), {text:'删除',iconCls:'page_delete', handler:function() {
						var year = Ext.getCmp('yearCombox').getValue();
						var selected = Ext.getCmp('leftpanelid').getSelectionModel().getSelected();
	   					var month='';
	   					var jizu='';
	   					if (!selected) {
	   						return;
	   					}
	   					month = selected.data['month'];
	   					var yearmonth = year + '-' + month;
	   					<%if("fenmeihui".equals(request.getParameter("pz"))) {%>
	   						var _jizu = Ext.getCmp('_leftjizu').getValue();
	   						jizu = _jizu;
	   					<%}%>
	   					
	   					Ext.Msg.confirm('提示', '你确定删除' + yearmonth + '的数据吗?', function(btn) {
		   					 	if (btn == 'yes') {
			   					 		Ext.Ajax.request({
				   						url:'<%=basePath%>est/weigh/report/MonthReport/delReport?delDate='+yearmonth + '&pz=<%=request.getParameter("pz")%>&jizu='+jizu,
				   						success:function(response) {
				   							var responseJson = Ext.decode(response.responseText);
											if(responseJson.success){
												info('提示', '删除成功');
												Ext.getCmp('leftpanelid').doSearch();
											}else{
												error("错误", responseJson.error);
											}
				   						},
				   						failure:function() {
				   							error('错误','删除失败');
				   						}
				   					});
		   					 	}
	   					});
					}}],
					items:{xtype:'estgrid', id:'leftpanelid',autoLoad:false,storeurl:'<%=basePath%>est/weigh/report/MonthReport/getMonthReportByYear?pagesiz=max&pz=<%=request.getParameter("pz")%>', colstype: leftcolstype, region: 'center'}
   			}
   			
    		new Ext.Viewport({
				layout: 'border',
				items: [ menuPanel, {
					xtype: 'estlayout',
					region: 'center',
					items: [leftPanel, GridPanel]
				}],
				renderTo: Ext.getBody()
			}); 
			
   			Ext.getCmp('leftsearchbar').doSearch();
   			Ext.getCmp('leftpanelid').on({
   				'rowclick':function() {
   					var year = Ext.getCmp('yearCombox').getValue();
   					var month = Ext.getCmp('leftpanelid').getSelectionModel().getSelected().data['month'];
   					var params = {yearmonth:year+'-'+month};
   					<%if("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
   						var jizu = Ext.getCmp('_leftjizu').getValue();
   						params.jizu = jizu;
   					<%}%>
   					Ext.getCmp('gridpanelid').doSearch(params);
   				}
   			});
    	});
    	
    </script>	
  </head>
  <body>
  </body>
  <iframe id="filedown" style="display: none" src="" />
</html>
