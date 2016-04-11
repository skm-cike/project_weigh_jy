<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="utf-8"%>
<%
    response.setHeader("Pragma","No-cache"); 
    response.setHeader("Cache-Control","no-cache"); 
    response.setDateHeader("Expires", 0); 
    
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<title>登录-欢迎使用江油电厂过磅系统</title>
		<link href="/weigh_jy/css/css.css" rel="stylesheet" type="text/css" />
		<base href="<%=basePath%>" />
	</head>

	<body onload="document.user.login.focus();">
   <script language="javascript">
   var http_request = false;
      function send_request(url) {//初始化、指定处理函数、发送请求的函数
          http_request = false;
          //初始化XMLHttpRequest 对象
          if(window.XMLHttpRequest) { //Mozilla 浏览器
             http_request = new XMLHttpRequest();
                 if (http_request.overrideMimeType) {//设置MiME 类别
                      http_request.overrideMimeType("text/xml");
                  }
            }
             else if (window.ActiveXObject) { // IE 浏览器
                   try{
                       http_request = new ActiveXObject("Msxml2.XMLHTTP");
              } catch (e) {
             try{
                 http_request = new ActiveXObject("Microsoft.XMLHTTP");
                 } catch (e) {}
              }
            }
           if(!http_request) { // 异常，创建对象实例失败
                window.alert("不能创建XMLHttpRequest 对象实例.");
                return false;
            }
        http_request.onreadystatechange = processRequest;
        //确定发送请求的方式和URL 以及是否同步执行
        http_request.open("GET", url, true);
        http_request.send(null);
      }
        //处理返回信息的函数
   function processRequest() {
      if (http_request.readyState == 4) { // 判断对象状态
      if (http_request.status == 200) { // 信息已经成功返回，开始处理信息
           // alert(http_request.responseText);
             if(http_request.responseText=='{success:false}'){
               var objDiv=document.getElementById("msg");
                   objDiv.style.display="block";
             }else{
                var form=document.user;
                    form.action="est/sysinit/sysuser/SysUser/login";
                    form.submit();
            }
         } else { //页面不正常
         alert("您所请求的页面有错误。");
       // alert(http_request.status);
       }
    }
  }
//用户验证
function vefUser(form){
    var login=document.getElementById("login").value;
    var password=document.getElementById("password").value;
    var objDiv=document.getElementById("msg");
    if(login.length==0&&password.length==0){
        objDiv.style.display="block";
        return false;
    }else{
        objDiv.style.display="none";
        send_request("est/sysinit/sysuser/SysUser/vefUser?login="+login+"&password="+password);
        return false;
    }
   
}
function fwdReg(){
 var form=document.user;
     form.action="est/sysinit/sysuser/SysUser/fwdReg";
     form.submit();
}

</script>
		<form name="user" action="est/sysinit/sysuser/SysUser/vefUser"
			method="post" onsubmit="return vefUser(this)">
			<div class="land_head"></div>
			<div class="land_cont">
				<div class="cont_left"></div>
				<div class="cont_right">
					<div class="test_land" id="msg" style="display: none">
						*用户名或密码错误 请重新输入*
					</div>
					<input type="text" name="login" id="login" class="right_imput1" />
					<input type="password" name="password" id="password"
						class="right_imput2" />
					<div class="right_button1">

					</div>
					<input type="submit" name="rss" value="登陆" class="right_button" />
					<input type="button" name="rss" value="注册" class="right_button"
						onclick="fwdReg()" />
				</div>
			</div>
			<div class="land_bottom"></div>
		</form>
	</body>
</html>
