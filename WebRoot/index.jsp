<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML>
<head>
<base href="<%=basePath%>">
<title>RDP</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/jone.css" />
<link rel="stylesheet" type="text/css"
	href="awesome/css/font-awesome.css" />
<link rel="stylesheet" type="text/css" href="css/sidebar.css" />
<link rel="stylesheet" type="text/css" href="css/tree.css" />
<link rel="stylesheet" type="text/css" href="css/userinfo.css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/sidebar.js"></script>
<script type="text/javascript" src="js/index.js"></script>
<script type="text/javascript" src="js/tree.js"></script>
<script type="text/javascript" src="js/grid2.js"></script>
<script type="text/javascript" src="js/msg.js"></script>
<script type="text/javascript" src="js/highlight.js"></script>
<script type="text/javascript" src="laydate/laydate.js"></script>
</head>
<BODY>
	<div id="header"></div>
	<div id='center'>
		<div id="sidebar"></div>
		<div id="content">
			<div id="pill">
				<i class='icon-home'></i> <span id='nav'>欢迎您</span>
			</div>
			<div id="main"></div>
		</div>
	</div>
</BODY>
<HTML>