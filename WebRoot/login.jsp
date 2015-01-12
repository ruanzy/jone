<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>">
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet"
	href="awesome/css/font-awesome.css" />
<link type="text/css" rel="stylesheet" href="css/login.css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript">
	$(function() {
		/**$.i18n.properties({
			name : 'message',
			path : 'i18n/',
			mode : 'map',
			callback : function() {
				$('[id^=i18n]').each(function() {
					var key = $(this).attr('id').substr(5);
					$(this).html($.i18n.prop(key));
				});
			}
		});**/
		document.oncontextmenu = function() {
			return false;
		};
		$(document).keydown(function(e) {
			if (e.keyCode == 13) {
				$('#submit').click();
			}
		});
		var loginfrm = $('#loginfrm');
		$('#submit', loginfrm).click(function() {
			var submiting = $('.loginsubmiting').show();
			var un = $("#username", loginfrm).val();
			var ps = $("#password", loginfrm).val();
			var vc = $("#vcinput", loginfrm).val();
			var go = $("#go", loginfrm).val();
			var data = {
				username : un,
				password : ps,
				vc : vc,
				go : go
			};
			$.ajax({
				url : 'login',
				type : 'post',
				data : data,
				dataType : 'json',
				success : function(result) {
					if (result.result) {
						//document.location = 'index.jsp';
						document.location = result.msg;
					} else {
						$('#error').html(result.msg);
						submiting.hide();
					}
				},
				error : function(response, b, c) {
					$('#error').html(response.responseText);
					submiting.hide();
				}
			});
		});
	});
</script>
</head>
<body>
	<div class="loginwrap">
		<h3
			style='height: 42px; background: #1da0d0 url(css/logo_rz.png) no-repeat center;'></h3>
		<h3 class='form-title'>请输入你的帐号和密码</h3>
		<form id='loginfrm'>
			<div class="logininput-icon">
				<i class='icon-user'></i> <input type="text" id="username"
					name='name' type="text" placeholder="用户名" value="admin"
					autocomplete="off" /><input type="text" id="go"
					name='go' value="${param.go}" />
			</div>
			<div class="logininput-icon">
				<i class='icon-lock'></i> <input type="password" id="password"
					name='password' type="password" placeholder="密码" value="162534" />
			</div>
			<div class="logininput">
				<div class="loginsubmit fr">
					<a id="submit">登&nbsp;&nbsp;&nbsp;&nbsp;录 <i
						class='icon-circle-arrow-right'></i>
					</a>
					<div class="loginsubmiting">登录中...</div>
				</div>
				<div class="fl">
					<input id="vcinput" name='vc' type="text" placeholder="验证码" /> <img
						src="captcha" id="VerifyImage" title="看不清？点击换一个"
						onclick="javascript:document.getElementById('VerifyImage').src='captcha?_r=' + new Date().getTime();return false;" />
				</div>
				<div class="clear"></div>
			</div>
		</form>
		<div id="error"></div>
	</div>
</body>
</html>