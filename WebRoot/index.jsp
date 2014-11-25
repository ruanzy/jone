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
<link rel="stylesheet" type="text/css" href="css/contextmenu.css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/cache.js"></script>
<script type="text/javascript" src="js/ajax-pushlet-client.js"></script>
<script type="text/javascript" src="js/Chart.min.js"></script>
<script type="text/javascript" src="js/box.js"></script>
<script type="text/javascript" src="js/tip.js"></script>
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
<script type="text/javascript" src="js/contextmenu.js"></script>
<script type="text/javascript" src="js/drag.js"></script>
<script type="text/javascript" src="js/multiradio.js"></script>
<!--[if lt IE 9]>
		<script language="javascript" type="text/javascript" src="js/excanvas.js"></script>
	<![endif]-->
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