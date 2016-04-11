<%@ page language="java" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache"); 
response.setHeader("Cache-Control","no-cache"); 

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<base href="<%=basePath%>">
	<%@ include file="/include.jsp"%>
	
</head>
<body>
</body>
<script type="text/javascript">
		
		// 发送同步请求
		function doSendPost(url){
		    var	conn = Ext.lib.Ajax.getConnectionObject().conn; 
				conn.open("GET", url,false); 
				conn.send(null); 
			return 	conn.responseText;
		}
		
		var lst = [];
		
		function redirect(url){
			
		}
		
		function addTip(tip){
			lst.push(tip);
		}
		
		function getTipContent(){
			return '';
		}
		
		//获取事故备品报警数目
		function alertNum(){
			var	alertNum = doSendPost('<%=basePath%>est/material/query/MtAlert/getAlertMtCodeNum');
			if(alertNum=='0'){
				return "";
			}else{
				return '<img src="img/workflow/node.gif"/>事故备品报警数量：(<a href=est/sysinit/sysuser/SysUser/login?fwd=est/material/query/MtAlert/fwdMtAlert target=_top><font color=red>' + alertNum+ '</font></a>)';
			}
		}
		
		//获取紧急到货物资领用记录数
		function getMtInorderAlertNum(){
		    var alertNum=doSendPost('<%=basePath%>est/material/query/MtInorderAlert/getMtInorderAlertNum');
			    if(alertNum!='0'){
			        return '<img src="img/workflow/node.gif"/>紧急到货物资领用数量：(<a href=est/sysinit/sysuser/SysUser/login?fwd=est/material/query/MtInorderAlert/fwdMtInorderAlert><font color=red>'+alertNum+"</font></a>)";
			    }else{
			      return "";
			    }
		}
		
		//获取需要润滑的设备的数量
		function getLubricationNum(){
			var _aheadDays = 7; //提前提醒天数
			var alertNum=doSendPost('<%=basePath%>est/asset/lubrication/AssetLubricationsinit/getShouldBeLibricatedNum?days='+_aheadDays);
			    if(alertNum!='0'){
			        return "<img src='img/workflow/node.gif'/>"+_aheadDays+"天内需要被润滑的设备数量：(<a href=est/sysinit/sysuser/SysUser/login?fwd=est/asset/lubrication/AssetLubricationhistories/fwdAssetLubricationhistories><font color=red>"+alertNum+"</font></a>)";
			    }else{
			      return "";
			    }
		}

		//获取当班定期工作的数量
		function getCycleworkNum(){
			var responseText = doSendPost('<%=basePath%>est/cuspage/cyclework/CycleworkLog/getOnDutyUserCycleWorkCount');
			var response = Ext.decode(responseText);
			if(responseText && response.success){
				if(response.allCnt !== 0) {
					var nums = "<font color=red>"+response.notCompleteCnt+"</font>/<font color=green>"+response.allCnt+"</font>";
				 	return "<img src='img/workflow/node.gif'/>当班定期工作数量：(<a href=<%=basePath%>est/cuspage/cyclework/CycleworkLog/fwdCycleworkLogQuery>"+nums+"</a>)";
				} 
			} 
			return "";
		}
		
		var tipList = [alertNum,getMtInorderAlertNum,getLubricationNum,getCycleworkNum];
		
		//获取提醒的记录
		function showAlertNum(){
			var tips = "";
			for(var i=0;i<tipList.length;i++) {
				tips += tipList[i]() + "<br/>";
			}
			return tips;
			//return alertNum()+"<br>"+getMtInorderAlertNum()+"<br/>"+getLubricationNum();
		}

		var module= new Ext.Viewport({
			id: 'module',
			layout: 'border',
			items: [
				 {
				 	xtype:'panel',
				 	layout:'fit',
				 	region:'center',
				 	html:showAlertNum()
				 }
			],
			renderTo: Ext.getBody()
		});

	
</script>
</html>