<%@ page language="java" pageEncoding="UTF-8"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");

	String urlpath = request.getContextPath();
	String urlbasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ urlpath + "/";
%>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet" type="text/css" href="<%=urlbasePath %>js/ext/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=urlbasePath %>js/ext/resources/css/ext-patch.css" />
<link rel="stylesheet" type="text/css" href="<%=urlbasePath %>js/ext/resources/css/ext-extend.css" />	
<link rel="stylesheet" type="text/css" href="<%=urlbasePath %>js/ext/resources/css/summary.css" />

<script> 
	var contextPath = '<%=request.getContextPath() %>';
</script>

<script type="text/javascript" src="<%=urlbasePath %>js/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/ext/ext-all.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/ext/source/locale/ext-lang-zh_CN.js"></script>

<script type="text/javascript" src="<%=urlbasePath %>js/est/GroupSummary.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/workflow/workflow-config.js"></script>

<script type="text/javascript" src="<%=urlbasePath %>js/est/util-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.ToolbarButton-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.ComboBox-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.BasicForm-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.Form-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.Tree-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.Grid-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.Layout-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.Tab-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.SearchButton-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.Panel-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.SearchPanel-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.EditableGrid-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.SignWin-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.SignField-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.ChooserField-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.CheckSelectGrid-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.TreeComboBox-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.PropertyComboBox-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.TreeGridChooserField-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.ColumnForm-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.TreeGridWin-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.ClickTreeGridChooser-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.SignButton-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.Radiobox-debug.js"></script>

<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.LoginWin-debug.js"></script>

<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.FileUploadWin-debug.js"></script>

<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.GroupGrid-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.GroupEditableGrid-debug.js"></script>

<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.CustomToolbar-debug.js"></script>

<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.SearchToolbar-debug.js"></script>

<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.WfStartflowButton-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.WfApproveTab-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.WfApproveHistoryTab-debug.js"></script>

<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.Datetime-debug.js"></script>

<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.ChartPanel-debug.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Ext.ux.form.LovCombo.js"></script>
<script type="text/javascript" src="<%=urlbasePath %>js/est/Est.ux.FPValitateWin-debug.js"></script>
<!-- 
<script type="text/javascript" src="js/est/editorGridFix.js"></script>
 -->

<style>
.saveValid {
	display: ${ requestScope [ "isSaveValid" ]
}

}
.delValid {
	display: ${ requestScope [ "isDelValid" ]
}
}

.file_field {
	height: 25px;
}
.ext-ie7 .x-menu-item-icon, 
.ext-ie6 .x-menu-item-icon {left: -24px;} 
.ext-ie8 .x-menu-item-icon, 
.ext-ie7 .x-menu-item-icon, 
.ext-ie6 .x-menu-item-icon {left: -24px;} 
</style>

<link type="text/css" href="<%=urlbasePath%>/css/normal.css" rel="stylesheet"  />
<link type="text/css" href="<%=urlbasePath%>/css/datetime.css" rel="stylesheet"  />

<script type="text/javascript">
	Ext.BLANK_IMAGE_URL = '<%=urlbasePath%>js/ext/resources/images/default/s.gif';
	
	var _isWf = '<%=request.getParameter("_isWf")%>';
	var _taskId = '<%=request.getParameter("_taskId")%>';
	var _masterid = '<%=request.getParameter("_masterid")%>';
	var _taskDefid = '<%=request.getParameter("_taskDefid")%>';
	var _wfProcessInstanceId = '<%=request.getParameter("_wfProcessInstanceId")%>';
	
	var cyclewin;
	var changepwdwin;
	
	var addWorkflowCols = function(colsType){
		colsType.cols.push({dataIndex:'wfstatus', header: '流程状态',width: 100});
		colsType.cols.push({dataIndex:'wfpiid', header: 'piid', hidden:true});
	}
	var addWorkflowGridpanelQueryCombos = function(gridPanel,searchpanel){
		
		searchpanel = searchpanel!==undefined?searchpanel:'searchpanel';
		
		gridPanel.tbar.splice(0,0,{
				xtype:'estcombos',
				id:'wfStatusquery', emptyText:'流程状态',width:120,elms: [['','全部'],[_WF_NOT_START,'未开始'],[_WF_IN_FLOW,'流程中'],[_WF_FLOW_DONE,'已完成']],
				listeners:{'select':function(){
					Ext.getCmp(searchpanel).doSearch();
				}}
			});
	}
	
	var signWin=new Est.ux.SignWin({id:'signWin'});//签名窗口
	
	var _moduleId = ${requestScope._moduleId} /**当前模块id */
	
	var curUser = '${sessionScope.loginUser.login}';
	var menuPanel = new Ext.Toolbar({
		id: 'menupanel',
		region: 'north',
		items:['${requestScope["moduleFullPath"]}','->',
		       '<img src="<%=urlbasePath%>img/windows.gif" style:"height:25px"/>',
		       ${sessionScope["menu"]},'-'
	    ]
	});
	
	function fwdmodule(url) {
		top.frames['main'].location.href='<%=urlpath %>/'+url;
	}
	
	
	function loadBtn(mid){
		var returnValue ;
		mid = mid?mid:_moduleId;
		Est.syncRequest({
			url:'est/sysinit/sysauthority/SysUserModule/getModuleButtons',
			params:{_moduleId:mid},
			success:function(response){
				var resJson = Ext.decode(response.responseText);
				returnValue = resJson.data;
			},
			failure:function(){
				returnValue = [];
			}
		});
		return returnValue;
	}
	
	
	function showReport(params) {
		var paramsStr = Ext.urlEncode(params);
		window.open('<%=urlbasePath%>/est/sysinit/sysfilemanage/ShowReport/fwdShowReport?'+paramsStr,
					 "报表",
					 "height="+window.screen.height+", width="+window.screen.width+", top=0,left=0,toolbar=no, menubar=no, scrollbars=no, resizable=yes, location=no, status=yes"
		);
	}
	
	function showReportInFrame(frameid,params) {
		var paramsStr = Ext.urlEncode(params); 
		Ext.getDom(frameid).src = '<%=urlbasePath%>/est/sysinit/sysfilemanage/ShowReport/fwdShowReport?'+paramsStr;
	}
	

	
	/**定期工作对话框 start**/
	/*
	function showCyclework(){
		Ext.Ajax.request({
			url:'est/cuspage/cyclework/CycleworkLog/getOnDutyUserCycleWorkLog',
			success:function(response,options){
				var responseJson = Ext.decode(response.responseText);
				if(responseJson.total > 0) {
					if(!cyclewin) {
						cyclewin = new Ext.Window({
							title:'定期工作',
							width:800,
							height:500,
							closeAction:'hide',
							html: "<iframe src='est/cuspage/cyclework/CycleworkLog/fwdOnWatchCyclework' style='width:100%; height:100%;'></iframe>"
						});
					}
					cyclewin.show();
				}
			}
		});
	}
	*/
	/**定期工作对话框 end**/
	
</script>

<script type="text/javascript">
	/*指纹设备参数初始化开始*/
	var _spDeviceType="0";//设备类型0：有驱动USB设备，1：串口设备，2：无驱UDISK设备
	var _spComPort="1";//当设备类型值为1时有效
	var _spBaudRate="6";//波特率
	var _spCharLen=512;//特征码长度
	/*指纹设备参数初始化结束*/
	//采集指纹
	function collectFp(){
		fingerprintform.finger.CharLen=_spCharLen;
		fingerprintform.finger.ZAZGetImgCode();
		var fpcode="";
		if('完成'==fingerprintform.finger.Msg){
			fpcode=fingerprintform.finger.FingerCode;
			alert('指纹采集成功');
		}else{
			alert(fingerprintform.finger.Msg);
		}
		return fpcode;
	}
	
	function validateFp(){
		fingerprintform.finger.CharLen=_spCharLen;
		fingerprintform.finger.ZAZGetImgCode();
		var fpcode="";
		if('完成'==fingerprintform.finger.Msg){
			fpcode=fingerprintform.finger.FingerCode;
		}else{
			alert(fingerprintform.finger.Msg);
		}
		return fpcode;
	}
	
	
	//重写方法
	
	//重写排序，不参与排序的行
	 var noSortField = ['companyshortname','company'];
	 Ext.data.Store.prototype.applySort = function() {
        if (this.sortInfo && !this.remoteSort) {
            var s = this.sortInfo, f = s.field;
            var st = this.fields.get(f).sortType;
            var fn = function(r1, r2) {
            	for (var i = 0; i < noSortField.length; i++) {
            		var temp = r1.get(noSortField[i]);
            		var temp2 = r2.get(noSortField[i]);
           			 if((temp && temp.indexOf('合计')>=0)
           			 	 || (temp2 && temp2.indexOf("合计")>=0)){
	                    return 0;  
	                 }
            	}         
                var v1 = st(r1.data[f]), v2 = st(r2.data[f]);
                return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
            };       
            this.data.sort(s.direction, fn);
            if(this.snapshot && this.snapshot != this.data) {
                this.snapshot.sort(s.direction, fn);
            }
        }
    }; 
</script>
<body>
	<div style="display:none;" >
		<form id="fingerprintform" >
		     	<object classid="clsid:35515A76-3049-4D2A-8457-FD83173037E9" name="finger" width="0"  height="0" id="finger" accesskey="a" tabindex="0"  title="finger">
		        	<embed width="0" height="0"></embed>
		     	</object>
		</form>
	</div>
</body>

