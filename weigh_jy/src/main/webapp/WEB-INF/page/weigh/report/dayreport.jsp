<%@ page language="java" import="java.util.*,com.est.common.ext.util.classutil.DateUtil" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String today = DateUtil.format(new Date(), "yyyy-MM-dd");
String startTime = DateUtil.format(DateUtil.add(new Date(),DateUtil.DAY_OF_MONTH, -15));
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
	
    <title>日报</title>
    <%@ include file="/include.jsp"%>
    <script type="text/javascript">
    	Ext.onReady(function(){
    		// 搜索字段
    		var searchFields=[	   
    			'时间:',{xtype:'datefield',id:'startTime',format:'Y-m-d',width:100,value:'<%=startTime%>'},
    	        '—',{xtype:'datefield',id:'endTime',format:'Y-m-d',width:100,value:'<%=today%>'}
    	      ];
    	  	// 搜索字段映射
    	  	var searchMapping={'startTime':'startTime','endTime':'endTime'};
    		
    		//=============日期start===============
    		var daycolstype= {
   				cols:[
   						{dataIndex:'day',	header:'日期',name:'day',width:135,sortable: false},
   						{dataIndex:'vehiclenums',	header:'车数',name:'vehiclenums',width:150,sortable: false}
   					]
    		}
    		var daygridpanel = {
       				xtype: 'estlayout',
       				region:'west',
       				split:true,//显示分隔条
       				collapsible:true,
       				width:260,
       				hasPagerBar:false,
       				bbar:new Est.ux.SearchToolbar({id:'searchbar',searchFields:searchFields,searchMapping:searchMapping,gridId:'daygridid'}),
    	   			items:[
    	   				{xtype: 'esteditgrid', id:'daygridid', storeurl: '<%=basePath%>est/weigh/report/DayReport/getDays?pz=<%=request.getParameter("pz")%>', colstype: daycolstype, region: 'center', autoLoad:false}
    	   			]
    		}
    		//=============日期end==============
    		
    		//===============日报start==============
			var colsType = {
				cols:[
					<%if ("fenmeihui".equals(request.getParameter("pz")) || "shigao".equals(request.getParameter("pz"))) {%>
					{dataIndex:'grade',	header:'品种等级',name:'grade',width:75,sortable: false},
					<%}%>
					{dataIndex:'companyname',	header:'单位名称',name:'companyname',width:135,sortable: false},
					{dataIndex:'outNumber',	header:'出门证号',name:'outNumber',width:100,sortable: false},
    				{dataIndex:'companycode',	header:'单位代码',name:'companycode',hidden:true,sortable: false},
    				{dataIndex:'poundsNumber', header:'磅单号', name:'poundsNumber',width:120,sortable: false},
    				{dataIndex:'vehicleNo', header:'车号', name:'vehicleNo',width:90,sortable: false},
    				{dataIndex:'netWeight', header:'重量(吨)', name:'netWeight',width:90,sortable: false},
    				{dataIndex:'vehicleNum', header:'车数', name:'vehicleNum',width:40,sortable: false}
    				<%if ("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
    				,{dataIndex:'jizu', header:'机组', name:'jizu',width:70,sortable: false}
    				<%}%>
                    <%if ("fenmeihui".equals(request.getParameter("pz")) || "shigao".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
                    ,{dataIndex:'unitPrice',	header:'单价(￥)',name:'unitPrice',width:75,sortable: false}
                    ,{dataIndex:'totalMoney',	header:'金额(￥)',name:'totalMoney',width:75,sortable: false}
                    <%}%>
				]
			}


   			var GridPanel = {
    			id:'GridPanel',
   				xtype: 'estlayout',
   				region:'center',
   				split:true,//显示分隔条
				<%if("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
				bbar:['机组', {id:'jizu',xtype: 'estcombos', width: 80, elms: [['31#_32#','31#_32#'],['33#_34#','33#_34#'],['全部','全部']],listeners:{'select':function(combo){
					var selected = Ext.getCmp('daygridid').getSelectionModel().getSelected();
					if (selected) {
						var day = selected.data['day'];
						Ext.getCmp('gridpanelid').doSearch({searchDate:day, jizu:combo.getValue()});
					}
				}}}],
				<%}%>
				tbar:[{text:'导出日报',iconCls:'page_excel',handler:function() {
					var selected = Ext.getCmp('daygridid').getSelectionModel().getSelected();
					if (!selected) {
						return;
					}
					var day = selected.data['day'];
					var jizu = '';
					<%if("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))) {%>
					var jizu = Ext.getCmp('jizu').getValue();
					if (jizu && jizu!='') {
						jizu = jizu.replace(/#/g,"%23");;
					}
					<%}%>
					var url='<%=basePath%>est/weigh/report/DayReport/exportDayReport?day='+day+'&jizu='+jizu+'&pz=<%=request.getParameter("pz")%>';
					frames('filedown').window.location = url;
				}},{text:'正常计算价格',iconCls:'page_attach',handler:function() {
					var selected = Ext.getCmp('daygridid').getSelectionModel().getSelected();
					if (!selected) {
						return;
					}
					var day = selected.data['day'];
					var jizu = '';
					<%if("fenmeihui".equals(request.getParameter("pz")) || "huizha".equals(request.getParameter("pz"))){%>
					var jizu = Ext.getCmp('jizu').getValue();
					if (!jizu || jizu == '' || jizu == '全部') {
						Ext.Msg.alert('提示', '先选择机组');
						return;
					}
					<%}%>
					Ext.Ajax.request({
						url:'<%=basePath%>est/weigh/report/DayReport/reCalculate?pz=<%=request.getParameter("pz")%>&day='+day+'&jizu='+jizu,
						success: function(rep) {
							rep = Ext.decode(rep.responseText);
							if (rep.success) {
								Ext.getCmp('gridpanelid').doSearch({searchDate:day});
							} else {
								error('错误', rep.error);
							}
						},
						failure: function() {
							error('错误', '计算失败!');
						}
					});
				}}<%if("fenmeihui".equals(request.getParameter("pz"))) {%>,{text:'按重量计算价格',iconCls:'page_attach',handler:function() {
                    var selected = Ext.getCmp('daygridid').getSelectionModel().getSelected();
                    if (!selected) {
                        return;
                    }
                    var day = selected.data['day'];
                    var jizu = '';
                    <%if("fenmeihui".equals(request.getParameter("pz"))){%>
                    var jizu = Ext.getCmp('jizu').getValue();
                    if (!jizu || jizu == '' || jizu == '全部') {
                        Ext.Msg.alert('提示', '先选择机组');
                        return;
                    }
                    <%}%>
                    Ext.Ajax.request({
                        url:'<%=basePath%>est/weigh/report/DayReport/reCalculateByWeight?pz=<%=request.getParameter("pz")%>&day='+day+'&jizu='+jizu,
                        success: function(rep) {
                            rep = Ext.decode(rep.responseText);
                            if (rep.success) {
                                Ext.getCmp('gridpanelid').doSearch({searchDate:day});
                            } else {
                                error('错误', rep.error);
                            }
                        },
                        failure: function() {
                            error('错误', '计算失败!');
                        }
                    });
                }}<%}%>
                    <%if("shigao".equals(request.getParameter("pz"))) {%>
                    ,{text:'设置水分',iconCls:'page_attach',handler:function() {
                        var win = Ext.getCmp('sfwin');
                        if (win == null || win == undefined) {
                            win = new Ext.Window({
                                title: '设置水分',
                                id:'sfwin',
                                width: 340,
                                height: 180,
                                resizable: false,
                                items: [{
                                    xtype:'estform',
                                    colnum:1,
                                    colstype:[{
                                        fieldset:'品种等级',items:[
                                            {fieldLabel:'品种等级',name:'grade',id:'grade_comb',hiddenName:'grade',
                                                displayField:'propertyname',
                                                valueField:'propertyname',
                                                xtype:'estpropcombos',
                                                propertycode:'SGPZDJ',
                                                allowBlank:false
                                            }]
                                         }
                                    ]
                                }],
                                buttons:[{
                                    text:'确定',
                                    handler:function(){
                                        var selected = Ext.getCmp('daygridid').getSelectionModel().getSelected();
                                        if (!selected) {
                                            Ext.Msg.alert('提示', '请选择日报');
                                            return;
                                        }
                                        var grade = Ext.getCmp('grade_comb').getValue();
                                        if (!grade || grade == null || grade == undefined) {
                                            return;
                                        }
                                        var day = selected.data['day'];
                                        Ext.Ajax.request({
                                            url:'<%=basePath%>est/weigh/cfginfo/WeighData/batchSetWater?day='+day+'&grade='+grade,
                                            success:function(res) {
                                                var resJson = Ext.decode(res.responseText);
                                                if (resJson.success) {
                                                    info('提示', '保存成功!');
                                                    var day = Ext.getCmp('daygridid').getSelectionModel().getSelected().data['day'];
                                                    Ext.getCmp('gridpanelid').doSearch({searchDate:day});
                                                } else {
                                                    error('错误', resJson.error);
                                                }
                                            },
                                            failure:function(res) {
                                                error('错误', '保存失败!');
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
                    }}
                    <%}%>],
   				collapsible:true,
	   			items:[
	   				{xtype: 'esteditgrid',pageSize:1000,id:'gridpanelid', storeurl: '<%=basePath%>est/weigh/report/DayReport/getReport?pz=<%=request.getParameter("pz")%>', colstype: colsType, region: 'center', autoLoad:false}
	   			]
    		}
			//=================日报end=========================
    		new Ext.Viewport({
				layout: 'border',
				items: [ menuPanel, {
					xtype: 'estlayout',
					region: 'center',
					items: [GridPanel, daygridpanel]
				}],
				renderTo: Ext.getBody()
			}); 
    		
    		Ext.getCmp('daygridid').on({
    			'rowclick':function() {
    				var day = Ext.getCmp('daygridid').getSelectionModel().getSelected().data['day'];
    				Ext.getCmp('gridpanelid').doSearch({searchDate:day});
    			}
    		});
    		
    		var search = Ext.getCmp('searchbar');
    		search.doSearch();
    	});
    	
    </script>	
  </head>
  <body>
  <iframe id="filedown" style="display: none" src="" />
  </body>
</html>
