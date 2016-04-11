<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%  response.setHeader("Pragma","No-cache"); 
    response.setHeader("Cache-Control","no-cache"); 

	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<title>注册-欢迎使用江油电厂过磅信息系统</title>
	<head>
		<base href="<%=basePath%>"/>
		<%@ include file="/include.jsp"%>
		<link href="css/css.css" rel="stylesheet" type="text/css" />
			<script language="javascript">
					
					//用户注册函数
					function savRegUser(stat){ 
					   //登录名检测
					    var  username=document.getElementById("username").value;
					    var  login=document.getElementById("login").value;
					    var  password=document.getElementById("password").value;
					    var  usercode=document.getElementById("usercode").value;
					    var  sex=document.getElementById("sex").value;
					    var  birthday=document.getElementById("birthday").value;
					    var  duty=document.getElementById("duty").value;
					    var  email=document.getElementById("email").value;
					    var  mobile=document.getElementById("mobile").value;
					    var  officetel=document.getElementById("officetel").value;
					    
					    if(username.length==0){
					       document.getElementById("UNMsg").style.visibility="visible";
					       document.forms[0].username.select();
					       return false;
					    }else{
					       document.getElementById("UNMsg").style.visibility="hidden";
					    }
					    if(stat=="sav"){
						    if(login.length<4||login.length>16){
						       var objDiv=document.getElementById("LMsg");
						           objDiv.innerHTML="--请输入正确的登陆名--";
						           objDiv.style.visibility="visible";
						           document.forms[0].login.select();
						       return false;
						    }else{
						       document.getElementById("LMsg").style.visibility="hidden";
						    }
					    }
					   if(password.length<6||password.length>16){
					      document.getElementById("PMsg").style.visibility="visible";
					      return false;
					    }else{
					      document.getElementById("PMsg").style.visibility="hidden";
					    }
					    var cfmpwd=document.getElementById("cfmpwd").value;
					    if(cfmpwd!=password){
					      document.getElementById("CFMsg").style.visibility="visible";
					      return false;
					    }else{
					      document.getElementById("CFMsg").style.visibility="hidden";
					    }
					    
					    var  reg=/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/; 
					    
					    if(mobile.length>0){
					     if(!reg.test(mobile)){
					           document.getElementById("MBMsg").style.visibility="visible";
					           return false;
					       }else{
					        document.getElementById("MBMsg").style.visibility="hidden";
					       }
					    }else{
					           document.getElementById("MBMsg").style.visibility="visible";
					           return false;
					          }
					    if(birthday.length>0){
					       var reg = /^((((((0[48])|([13579][26])|([2468][048]))00)|([0-9][0-9]((0[48])|([13579][26])|([2468][048]))))-02-29)|(((000[1-9])|(00[1-9][0-9])|(0[1-9][0-9][0-9])|([1-9][0-9][0-9][0-9]))-((((0[13578])|(1[02]))-31)|(((0[1,3-9])|(1[0-2]))-(29|30))|(((0[1-9])|(1[0-2]))-((0[1-9])|(1[0-9])|(2[0-8]))))))$/i;
					       if(!reg.test(birthday)){
					           document.getElementById("BDMsg").style.visibility="visible";
					           return false;
					       }else{
					          document.getElementById("BDMsg").style.visibility="hidden";
					       }
					    }
					    if(email.length>0){
					       var reg=/(\S)+[@]{1}(\S)+[.]{1}(\w)+/;
					       if(!reg.test(email)){
					          document.getElementById("EMMsg").style.visibility="visible";
					          return false;
					       }else{
					         document.getElementById("EMMsg").style.visibility="hidden";
					       }
					    }
					    if(stat=="sav"){
					     var data={"username":username,"login":login,"password":password,"usercode":usercode,"sex":sex,
					                 "birthday":birthday,"duty":duty,"email":email,"mobile":mobile,"officetel":officetel
					              };
					     Ext.Ajax.request({
					       url:'<%=basePath%>est/sysinit/sysuser/SysUser/savUser',
					       waitTitle:'提示',
					       waitMsg:'正在提交数据，请稍后...',
					       method:'post',
					       params:data,
					       success:function(){
					                 Ext.MessageBox.show({
										  minWidth : 300,
										  title : '提示',
										  msg : "<div align=center>注册成功!&nbsp;&nbsp;&nbsp;&nbsp;<a href='./index.jsp'>登 陆</div>",
										  buttons : Ext.Msg.OK 
										 });
					               },
					       failure:function(){
					                 Ext.MessageBox.show({
										  minWidth : 300,
										  title : '提示',
										  msg : '<div align=center>注册失败!</div>',
										  buttons : Ext.Msg.OK
										 });
					                }
					     });
					    }
					    return false;
					}
					
					//登录名检测函数
					function chkUser(){
					   var login=document.getElementById("login").value;
					   if(login.length<4||login.length>16){
					       var objDiv=document.getElementById("LMsg");
					           objDiv.innerHTML="请输入正确的登陆名(4-16位)";
					           objDiv.style.visibility="visible";
					       return false;
					    }else{
					           document.getElementById("LMsg").style.visibility="hidden";
					     }
					    Ext.Ajax.request({
					       url:'<%=basePath%>est/sysinit/sysuser/SysUser/chkUser',
					       method:'post',
					       params:{login:login},
					       success:function(rep){
					                  var result=Ext.decode(rep.responseText);
					                  if(result['success']){
					                     var objDiv=document.getElementById("LMsg");
					                         objDiv.innerHTML="--登录名可用--";
					                         objDiv.style.visibility="visible";
					                    }else{
					                       var objDiv=document.getElementById("LMsg");
					                           objDiv.innerHTML="--登录名不可用--";
					                           objDiv.style.visibility="visible";
					                           document.forms[0].login.select();
					                          //return false ;
					                    }
					               }
					     });
					}
					Ext.onReady(function(){
						
						new Ext.ux.ComboBoxTree({
							fieldLabel:'部门',
					       	//id:'newParentNodeInfo',
					       	width:150,
					       	height:200,
					       	treeWidth:300,
					       	displayField:'sysDept.deptname',
					       	valueField:'sysDept.deptid',
					       	hiddenName:'sysDept.deptid',
					       	dataUrl:'<%=basePath%>est/sysinit/sysdept/SysDept/getDepartTree',	//树加载url
					       	rootId:'0',	//根结点id
					       	rootText:'部门列表', //根结点text
					        allowedempty: false,
					        renderTo:'treecombo',
					        alwaysReload : true
						
						});
						
					});
			</script>
			
	</head>


	<body >
		<form name="user" onsubmit="return savRegUser('sav')" >
			<div class="login_head"></div>
				<div class="login_jbxx">
                  
					<input type="text" name="username" id="username"
						class="login_imput" />
					<span class="test_yhm" id="UNMsg" style="visibility: hidden">--请输入正确的用户名--</span>
					<br />

					<input type="text" name="login" id="login" class="login_imput1"
						onblur="chkUser()" />

					<span class="test_dlm" id="LMsg" style="visibility: hidden">--请输入正确的登陆名(4-16位)--</span>
				</div>
				<div class="login_mm">
					<input type="password" name="password" id="password"
						class="login_imput1" onblur="savRegUser('c')" />
					<span class="test_mm" id="PMsg" style="visibility: hidden">--请输入正确的密码--</span>
					<br />
					<input type="password" name="cfmpwd" id="cfmpwd"
						class="login_imput1" onblur="savRegUser('c')" />
					<span class="test_qrmm" id="CFMsg" style="visibility: hidden">--请确认您的密码--</span>
					<br />
					<input type="text" name="mobile" id="mobile" class="login_imput1"
						onblur="savRegUser('c')" />
					<span class="test_dh" id="MBMsg" style="visibility: hidden">--请输入正确的手机号码--</span>
				</div>
				<div class="login_qtxx">
					<input type="text" name="usercode" id="usercode"
						class="login_imput1" />
					<br />
					<input type="text" name="birthday" id="birthday"
						class="login_imput2" onblur="savRegUser('c')" />
					<span class="test_day" id="BDMsg" style="visibility: hidden">--请输入正确的日期--</span>
					<br />
					<br />
					<input type="radio" name="sex" id="sex" value="1" checked="checked"
						class="login_imput3" />
					<input type="radio" name="sex" id="sex" value="2"
						class="login_imput4" />
					<br />
					<input type="text" name="duty" id="duty" class="login_imput2" />
					<br />
					<input type="text" name="email" id="email" class="login_imput2"
						onblur="savRegUser('c')" />
					<span class="test_day" id="EMMsg" style="visibility: hidden">--输入正确的Email--</span>
					<br />
					<input type="text" name="officetel" id="officetel"
						class="login_imput2" />

				</div>
				<div id="treecombo"></div>
				<div class="login_bottom">
				    <div>
				    <input type="button" name="rss" value="返回"  class="button" onclick="history.back(-1)"/>
					<input type="reset" name="rss" value="重新填写" class="button" />
					<input type="submit" name="rss" value="提交注册信息" class="button1"/>
				    </div>
				</div>
		</form>
	</body>
</html>
