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
	<link rel="stylesheet" type="text/css" href="css/index.css" />
	<link rel="stylesheet" type="text/css"
		href="awesome/css/font-awesome.css" />
	<link rel="stylesheet" type="text/css" href="css/sidebar.css" />
	<link rel="stylesheet" type="text/css" href="css/datepicker.css" />
	<link rel="stylesheet" type="text/css" href="css/dt.css" />
	<link rel="stylesheet" type="text/css" href="css/dropdown.css" />
	<link rel="stylesheet" type="text/css" href="css/box.css" />
	<link rel="stylesheet" type="text/css" href="css/tree.css" />
	<link rel="stylesheet" type="text/css" href="css/grid.css" />
	<link rel="stylesheet" type="text/css" href="css/res.css" />
	<link rel="stylesheet" type="text/css" href="css/ac.css" />
	<link rel="stylesheet" type="text/css" href="css/au.css" />
	<link rel="stylesheet" type="text/css" href="css/tip.css" />
	<link rel="stylesheet" type="text/css" href="css/down.css" />
	<link rel="stylesheet" type="text/css" href="css/button.css" />
	<link rel="stylesheet" type="text/css" href="css/label.css" />
	<link rel="stylesheet" type="text/css" href="css/msg.css" />
	<link rel="stylesheet" type="text/css" href="css/well.css" />
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/cache.js"></script>
	<script type="text/javascript" src="js/box.js"></script>
	<script type="text/javascript" src="js/tip.js"></script>
	<script type="text/javascript" src="js/layout.js"></script>
	<script type="text/javascript" src="js/sidebar.js"></script>
	<script type="text/javascript" src="js/index.js"></script>
	<script type="text/javascript" src="js/autocomplete.js"></script>
	<script type="text/javascript" src="js/autocomplete2.js"></script>
	<script type="text/javascript" src="js/datepicker.js"></script>
	<script type="text/javascript" src="js/datepicker2.js"></script>
	<script type="text/javascript" src="js/tabs2.js"></script>
	<script type="text/javascript" src="js/dropdown.js"></script>
	<script type="text/javascript" src="js/tree.js"></script>
	<script type="text/javascript" src="js/grid2.js"></script>
	<script type="text/javascript" src="js/checkboxlist.js"></script>
	<script type="text/javascript" src="js/down.js"></script>
	<script type="text/javascript" src="js/msg.js"></script>
</head>
<BODY>
	<div id="head" class="station-icon">
		<div id="head-left">
			<i class="icon-desktop"></i> RDP后台管理系统		<span id='dd1'><i class="icon-reorder"></i></span>
		</div>
		<div id="head-right">
			<ul>
				<li class='headitem light-blue'>
				<dl id='asd' class='rzy-sidedown'><dt><i class="icon-user"></i>欢迎您,admin <i class="icon-angle-down"></i></dt><dd></dd></dl>
				</li>
				<li class='headitem purple'><a><i class="icon-envelope"></i> 信息</a>
					<span class='badge label-warning'>5</span>
				</li>
				<li class='headitem green'><a><i class="icon-bell"></i> 更换样式</a>
				</li>
			</ul>
		</div>
	</div>
	<div id="sidebar">
		<div id="west"></div>
	</div>
	<div id="center">
		<div id="pill">
		<span id='nav'>
			<i class='icon-home'></i> 欢迎您
		</span>
		</div>
		<div id="main"
			style='padding:30px;border:0px solid red;overflow: auto;'></div>
	</div>
</BODY>
<HTML>