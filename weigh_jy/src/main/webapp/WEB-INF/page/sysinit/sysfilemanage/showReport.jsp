<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk" isELIgnored="false"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String templateid = request.getParameter("templateid");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<base href="<%=basePath%>/cellweb5/">
		<META name=VI60_defaultClientScript content=VBScript>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

		<!--Style Sheets First one to adjust fonts on input fields.-->
		<LINK rel=stylesheet type=text/css HREF="control/olstyle.css">

		<SCRIPT LANGUAGE=VBSCRIPT src="control/function.vbs"></SCRIPT>
		<SCRIPT LANGUAGE=JAVASCRIPT src="control/buttons.js"></SCRIPT>
		<!--
		<SCRIPT LANGUAGE=JAVASCRIPT src="control/function.js"></SCRIPT>
		-->
		<script type="text/javascript" src="../../js/ext/ext-base.js"></script>
		<script type="text/javascript" src="../../js/ext/ext-all-debug.js"></script>
		
		

		

		<title>报表</title>
		

		<SCRIPT LANGUAGE="javascript">
			
			var urlParamsStr = document.location.search; //前台传递的查询参数
			urlParamsStr = urlParamsStr.length > 0 ? urlParamsStr.substring(1) : "";
			var urlParamsJson = Ext.urlDecode(urlParamsStr);
			//alert('json='+Ext.encode(urlParamsJson));
			var templateid = "<%=templateid%>";	//报表id
			var fileno = "${requestScope.fileno}";		//文件序列号
			var templatecode ="${requestScope.templatecode}";		//报表编码
			var isReadOnly = urlParamsJson["isReadOnly"];						//是否只读
			
			var saveUrl = "<%=basePath%>/est/sysinit/sysfilemanage/SysTemplate/savReport?fileno="+fileno+"&templateid="+templateid;	//报表保存action地址
			//var reportUrl =  "<%=basePath%>/est/sysinit/sysfilemanage/SysTemplate/getTemplate?templateid="+templateid;		//报表url
			
			var _col,_row,_sheet,_data;
		
			function setObjectSize(){
				
				var lWidth = document.body.offsetWidth;
				if( lWidth <= 0) lWidth = 1;
				CellWeb1.style.width = lWidth;
				CellWeb1.style.height = document.documentElement.clientHeight-parseInt(CellWeb1.style.top)-10;
			
			}
			
			
			/** 窗口改变大小时修改cell控件大小*/
			function window_onresize() {
				setObjectSize();
			}
			/** 页面load时初始化处理*/
			function window_onload() {
				
				CellWeb1.EnableUndo(true);
				CellWeb1.Mergecell = true;
				CellWeb1.border = 0;
				CellWeb1.style.left = 0;	
				
				if(isReadOnly=='Y') {
					idTBGeneral.style.display = "none";
				}
				CellWeb1.style.top = idTBGeneral.offsetHeight;
				setObjectSize();
				CellWeb1.style.height = document.documentElement.clientHeight-parseInt(CellWeb1.style.top)-15
				CellWeb1.style.display="";
				
				
				openCell();
				
			}
			
			
			//保存到数据库
			function saveToDB() {
				if(CellWeb1.UploadFile(saveUrl)){
					alert('保存成功!');
				} else {
					alert('保存失败!');
				}
				
			}
			
			/**
			 * 读取文件
			 */
			function openCell() {
				
				var flag = true;
				window.filepath = "";		//reporttemplate中存放的报表路径
				var url = "";			//cll文件载入路径
				
				var isLogin = CellWeb1.Login("北京英思创科技有限公司","","13100104505","7460-0271-0216-4004");
				if(!isLogin) {
					alert("华表控件注册失败!");
					return;
				}
				CellWeb1.LocalizeControl(0x804);
				
				Ext.Ajax.request({
				   url: '<%=basePath%>est/sysinit/sysfilemanage/SysTemplate/getTemplateByCode',
				   params: { 'templatecode': templatecode },
				   success: function(response, options){
				   				 var responseJson = Ext.util.JSON.decode(response.responseText);  
				   				 
				   				 if(responseJson.success){
				   				 	flag = true;
				   				 	window.filepath = responseJson.data.templateurl;
				   				 	//alert(filepath);
				   				 	var url = "<%=basePath%>est/sysinit/file/File/fileDownload?filepath="+window.filepath;
					
									Ext.Ajax.request({
										   url: '<%=basePath%>est/sysinit/sysfilemanage/SysTemplate/downloadReport',
										   params: {
										   	 'filepath': window.filepath ,
										   	 templateid : window.templateid,
										   	 fileno : window.fileno
										   	 },
										   success: function(response, options){
										   		var json = Ext.util.JSON.decode(response.responseText);
										   		if(json.success){
										   			var b = CellWeb1.OpenFile('<%=basePath%>'+json.filepath, "");
										   			//alert(b);
										   			if(b>=0){
										   				loadVar();
										   			}
										   		}
										   },
										    failure: function(){
								   				flag= false;
								  			}
				   
									});
				   				 } else {
				   				 	flag = false;
				   				 }
				   			},
				   failure: function(){
				   				flag= false;
				  			}
				   
				});
				
				if(!flag){
					var url = '<%=basePath%>cellweb5/empty.cll';
					CellWeb1.OpenFile(url, "");
				}
			}
			
			//载入已定义参数
			function loadVar(){
				Ext.Ajax.request({
				   url: '<%=basePath%>est/sysinit/sysfilemanage/SysTemplateparam/getTemplateparamList',
				   params: {
				   			 'templateid': templateid,
				   			 'start': 0,
				   			 'limit': 1000000 
				   			 },
				   success: function(response, options){
				   				 var responseJson = Ext.util.JSON.decode(response.responseText); 
				   				 if(responseJson.rows.length > 0){
				   				 	var rows = responseJson.rows;
				   				 	
				   				 	for(var i=0;i<rows.length;i+=1) {
				   				 		
				   				 		var varObj = rows[i];
				   				 		if(varObj.paramtype == "String"){
				   				 			if(urlParamsJson[varObj.paramname]) {
				   				 				CellWeb1.DefineStringVar(varObj.paramname,urlParamsJson[varObj.paramname]);
				   				 			} else {
				   				 				CellWeb1.DefineStringVar(varObj.paramname, " ");
				   				 			}
				   				 		} else {
				   				 			if(urlParamsJson[varObj.paramname]) {
				   				 				CellWeb1.DefineDoubleVar(varObj.paramname, urlParamsJson[varObj.paramname]);
				   				 			} else {
				   				 				CellWeb1.DefineDoubleVar(varObj.paramname, 0);
				   				 			}
				   				 		}
				   				 		
				   				 	}
				   				 	loadFunc();
				   				 } else {
				   				 	//alert("加载自定义参数失败！");
				   				 }
				   			},
				   failure: function(){
				   				alert("加载自定义参数失败！");
				   			}
				   			
				});
				   
			}
			
			//载入已定义函数
			function loadFunc(){
				Ext.Ajax.request({
				   url: '<%=basePath%>est/sysinit/sysfilemanage/SysTemplateFunction/getTemplatefunctionList',
				   params: {
				   			 'templateid': templateid,
				   			 'start': 0,
				   			 'limit': 1000000 
				   			 },
				   success: function(response, options){
				   				 var responseJson = Ext.util.JSON.decode(response.responseText); 
				   				 if(responseJson.rows.length > 0){
				   				 	var rows = responseJson.rows;
				   				 	
				   				 	for(var i=0;i<rows.length;i+=1) {
				   				 		var varObj = rows[i];
				   				 		addCustomFun(varObj.functioncode,varObj.functionparam,varObj.functiondesc);
				   				 	}
				   				 	mnuFormulaReCalc_click();
				   				 } else {
				   				 	//alert("加载自定义函数失败！");
				   				 }
				   			},
				   failure: function(){
				   				alert("加载自定义函数失败！");
				   			}
				   			
				});
				   
			}			
			
			
			
			//增加自定义函数
			function addCustomFun(funName, funArg, funDes) {
				funArg = funArg?funArg:'';
				var tempStr = '';
				tempStr += '"自定义函数"' + "Any " + funName + "(" + funArg + ")";
				tempStr += 'BEGIN_HELP\n\b';
				tempStr += funName + "(" + funArg + ")";
				tempStr += "\n" + funDes;
				tempStr += 'END_HELP\n\b';
				CellWeb1.object.definefunctions(tempStr);
			};
			
			//计算自定义函数
	function runFun(col, row, sheet ,funName, funArg) {
		
		
		funArg = (funArg!='undefined')?funArg:'';
		//alert('funName='+funName);
		//alert('funArg='+funArg);
		var funContent = CellWeb1.GetFormula(col, row, sheet);
		var url =  '<%=basePath%>est/sysinit/sysfilemanage/SysTemplateFunction/calcFunc';
		var params =    {
			   				'templateid': templateid,
			   				'templatecode':templatecode,
			   				'funName': funName,
							'funArg': funArg,
							'funContent': funContent
			   			 };
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
        conn.open("POST", url, false);
        conn.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        conn.send(Ext.urlEncode(params));
        var responseJson = Ext.util.JSON.decode(conn.responseText);
        //alert(conn.responseText);
        if(responseJson.success){
        	
        	_col = col ;
        	_row = row ;
        	_sheet = sheet;
        	_data = responseJson.data;
        	//CellWeb1.CalculateAll();
        	
        	//fillCusFun(_col,_row,_sheet,_data);
        	//alert(Ext.encode(responseJson.data));
        	//fillCusFun(col, row, sheet ,responseJson.data)
        	//return Ext.encode(responseJson.data);
        } else {
        	return "";
        }
        
	};
	
	//将自定义函数结果填入cell
	function fillCusFun() {
		var cursheet = arguments[2]
		var curCol = _col; //当前列号
		var curRow = _row; //当前行号
		//alert(curCol+":"+curRow)
		if(_data === undefined){
			return;
		}
		var totalRow = _data.length;	//总行数
		var inserted = false;
		if(totalRow == 0) {
			CellWeb1.S(_col,_row,cursheet,'');
		}
		for(var r=0;r<totalRow;r+=1) { //遍历行
			var totalCol = _data[r].length;
			//alert(CellWeb1.getRows(cursheet))
			for(var c=0;c<totalCol;c+=1) {	//遍历列
				curcol = _col+c;
				currow = _row+r;
				cursheet = cursheet;
				sheetRows = CellWeb1.getRows(cursheet);
				//if(_row + totalRow -1 > sheetRows){ //如果结果行数>sheet总行数
				if(_row + r + 1 == sheetRows){ //如果结果行数>sheet总行数	
					if(!inserted){
						//在sheet最后添加需要的行
						CellWeb1.InsertRow( _row+r+1, totalRow-r-1, cursheet);
						inserted = true;	
					}
				} else {
					if(!inserted){
						var nextRowCellValue = CellWeb1.GetCellString2(_col+c, _row+r+1, cursheet);	//当前单元格下一行单元格的内容
						
						if(nextRowCellValue!=='' && totalRow-r-1>0){
								//alert(_row+r+1);
								//在sheet最后添加需要的行
								var currentRowHeight = CellWeb1.GetRowHeight(1,_row+r,cursheet); //得到当前行的height
								
								CellWeb1.InsertRow( _row+r+1,totalRow-r-1, cursheet);				//插入填充剩余行数
								
								for(var i=1;i<totalRow-r;i+=1) {
									 CellWeb1.SetRowHeight(1,currentRowHeight,_row+r+i,cursheet);
								}
								
								//alert("cursheet="+cursheet+" row="+currow+" col="+curcol)
								//根据上一行设置添加行的组合情况 add by jingpj 2010-1-27z
								var sheetcols = CellWeb1.GetCols(cursheet);
								var tmp = CellWeb1.GetCurSheet();
								CellWeb1.SetCurSheet(cursheet);
								
								for(var n=0;n<=sheetcols;n++){
									var startCol1 = CellWeb1.GetMergeRangeJ(n, _row, cursheet, 0);
									var startRow1 = CellWeb1.GetMergeRangeJ(n, _row, cursheet, 1);
									var endCol1 = CellWeb1.GetMergeRangeJ(n, _row, cursheet, 2);
									var endRow1 = CellWeb1.GetMergeRangeJ(n, _row, cursheet, 3);
									
									
									if(endCol1!==0){
										//alert(n+":合并单元格起始列:" + startCol1 + "；合并单元格起始行：" + startRow1+ "合并单元格结束列:" + endCol1 + "；合并单元格结束行：" + endRow1);
										
										for(var i=1;i<totalRow-r;i+=1) {
											CellWeb1.MergeCells(n,_row+r+i,endCol1,_row+r+i);
										}
										
										/*
										for(var m=_row+r+1;m<=totalRow+r;m++) {
											alert(m)
											CellWeb1.MergeCells(n,m,endCol1,m);
										}
										*/
										n = endCol1;
									}
								}
								CellWeb1.SetCurSheet(tmp)
								inserted = true;	
							
						}
					}
					//alert(nextRowCellValue);
				
				}
				
				var celldata = _data[r][c];
				//alert(curCol+":"+curRow);
				if(typeof celldata === 'string') {
					CellWeb1.S(curcol,currow,cursheet,celldata);
				} else {
					CellWeb1.D(curcol,currow,cursheet,celldata);
				}
				
			}
		
		}
		if(urlParamsJson['tableheadrows']){
			setTableHead(parseInt(urlParamsJson['tableheadrows']))
		}
		CellWeb1.ReDraw();
		
	}
	
	//设置表头行数（1 - 结束行号)
	function setTableHead(rownum) {
		CellWeb1.PrintSetTopTitle(1, rownum);
	}


</SCRIPT>

		<!--BUTTON-->
		<SCRIPT FOR="cbButton" EVENT="onmousedown()" LANGUAGE="JavaScript">
			return onCbMouseDown(this);
		</SCRIPT>

		<SCRIPT FOR="cbButton" EVENT="onclick()" LANGUAGE="JavaScript">
			return onCbClickEvent(this);
		</SCRIPT>

		<SCRIPT FOR="cbButton" EVENT="oncontextmenu()" LANGUAGE="JavaScript">
			return(event.ctrlKey);
		</SCRIPT>

		<SCRIPT ID=clientEventHandlersVBS LANGUAGE=vbscript>



</SCRIPT>
	</HEAD>
	<BODY id="mainbody" class="mainBody" LANGUAGE=javascript onresize="return window_onresize()" onload="return window_onload()">
		
		<TABLE class="cbToolbar" id="idTBGeneral" cellpadding='0' cellspacing='0' width="100%">
			<TR id = 'menu'>		
			<!-- <TD NOWRAP><A class=tbButton onclick=javascript:mnuFormulaReCalc_click(); id=cmdFileOpen title=打开远程文档 href="javascript:void(0)"><IMG  title=打开远程文档 onclick=javascript:mnuFormulaReCalc_click(); align=absMiddle  src="<%=request.getContextPath()%>/cell/general/openweb.gif" width="16" height="16"></A></TD>  -->
				
				<% if(!"true".equals(request.getParameter("isReadOnly"))){%>
				<TD NOWRAP id='saveTD'><A class=tbButton id=saveToDBButt title=保存 href="javascript:void(0)" onclick="saveToDB()"><IMG align=absMiddle src="<%=request.getContextPath()%>/cellweb5/general/save.gif" width="16" height="16"></A></TD> 
				<%} %>
				<TD class="tbDivider" NOWRAP><A class=tbButton id=cmdFilePrintPreview title=打印预览 href="javascript:void(0)" name=cbButton><IMG align=absMiddle src="<%=request.getContextPath()%>/cellweb5/general/printpreview.gif" width="16" height="16"></A></TD>
				<TD NOWRAP><A class=tbButton id=cmdFilePrint title=打印 href="javascript:void(0)" name=cbButton><IMG align=absMiddle src="<%=request.getContextPath()%>/cellweb5/general/print.gif" width="16" height="16"></A></TD>
				<TD NOWRAP><A class=tbButton onclick=javascript:CellWeb1.ExportExcelDlg(); title=导出Excel href="javascript:void(0)"><IMG align=absMiddle src="<%=request.getContextPath()%>/img/xls.png" width="16" height="16"></A></TD>
				<TD width="90%"></TD>
			</TR>
		</TABLE>
		
		<div style="LEFT: 0px; POSITION: relative">
			正在装载......
		</div>

		<p>
		<p>
			<OBJECT id=CommonDialog1 style="DISPLAY: none; POSITION: relative"
				height=32 width=32
				classid=clsid:F9043C85-F6F2-101A-A3C9-08002B2F49FB ">
				<PARAM NAME="_ExtentX" VALUE="688">
				<PARAM NAME="_ExtentY" VALUE="688">
				<PARAM NAME="_Version" VALUE="393216">
				<PARAM NAME="CancelError" VALUE="1">
				<PARAM NAME="Color" VALUE="0">
				<PARAM NAME="Copies" VALUE="1">
				<PARAM NAME="DefaultExt" VALUE="">
				<PARAM NAME="DialogTitle" VALUE="">
				<PARAM NAME="FileName" VALUE="">
				<PARAM NAME="Filter" VALUE="">
				<PARAM NAME="FilterIndex" VALUE="0">
				<PARAM NAME="Flags" VALUE="0">
				<PARAM NAME="FontBold" VALUE="0">
				<PARAM NAME="FontItalic" VALUE="0">
				<PARAM NAME="FontName" VALUE="">
				<PARAM NAME="FontSize" VALUE="8">
				<PARAM NAME="FontStrikeThru" VALUE="0">
				<PARAM NAME="FontUnderLine" VALUE="0">
				<PARAM NAME="FromPage" VALUE="0">
				<PARAM NAME="HelpCommand" VALUE="0">
				<PARAM NAME="HelpContext" VALUE="0">
				<PARAM NAME="HelpFile" VALUE="">
				<PARAM NAME="HelpKey" VALUE="">
				<PARAM NAME="InitDir" VALUE="">
				<PARAM NAME="Max" VALUE="0">
				<PARAM NAME="Min" VALUE="0">
				<PARAM NAME="MaxFileSize" VALUE="260">
				<PARAM NAME="PrinterDefault" VALUE="1">
				<PARAM NAME="ToPage" VALUE="0">
				<PARAM NAME="Orientation" VALUE="1">
			</OBJECT>
			<OBJECT id=CellWeb1
				style="DISPLAY: none; LEFT: 184px; POSITION: absolute; TOP: 190px"
				height=183 width=367
				classid=clsid:3F166327-8030-4881-8BD2-EA25350E574A >
				<PARAM NAME="_Version" VALUE="65536">
				<PARAM NAME="_ExtentX" VALUE="9710">
				<PARAM NAME="_ExtentY" VALUE="4842">
				<PARAM NAME="_StockProps" VALUE="0">
			</OBJECT>
		</p>

	</BODY>
</HTML>