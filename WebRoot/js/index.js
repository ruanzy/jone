$(function() {	
	var body = $('body');
	var W1 = $(window).width();
	var sw = 190;
	body.addClass('layout');
	$('#sidebar', body).width(sw);
	$('#center', body).css('margin-left', sw);
	function resize() {
		var W = $(window).width();
		var H = $(window).height();
		var NH = $('#header').outerHeight();
		var WW = $('#sidebar').outerWidth();
		$('#sidebar').css({top : NH, left:0});
	}
	$(window).resize(resize);
	resize();
	$('#header').load('common/header', function(){
		var items = [ {
			icon : 'icon-cog',
			text : '设置',
			action : set
		}, {
			icon : 'icon-user',
			text : '个人资料',
			action : info
		}, '-', {
			icon : 'icon-off',
			text : '退出',
			action : logout
		} ];
		function set(){
			var dialog = $.dialog({
				title:'set',
				width: 350,
				height:180,
				url:'view/user/set.html',
				onShow : function(){
					
				},
	   		 	ok:function(){
	   		 		dialog.close();
	   		 	}
			});
		}
		function info(){
			$.dialog({
				title:'asd',
				width: 'auto',
				url:'view/income/list.html'
			});
		}
		function logout(){
			document.location = 'common/logout';
		}
		$('#asd').SideDown({
			items:items
		});
	});
	$('#sidebar').Accordion({
		title : '导航菜单',
		url : 'common/menu'
	});
	$('#center').load('common/center',function(){
		$('#main').load('common/welcome');
	});
	/***************************************************************************
	 * $('#center').Tabs(); $('#center').Tabs('add', { title : '欢迎使用', url :
	 * 'welcome.jsp' });
	 **************************************************************************/
	$('#dd').toggle(function(e) {
		$('#sidebar').css('width', 40);
		$('#center').css('marginLeft', 40);
	}, function(e) {
		$('#sidebar').css('width', 190);
		$('#center').css('marginLeft', 190);
	});

	$('#toggle').toggle(
			function(e) {
				$('#sidebar').hide();
				$('#toggle').css('left', 0);
				$('#toggle i').removeClass('icon-caret-left').addClass(
						'icon-caret-right');
				$('#center').css('marginLeft', 0);
			},
			function(e) {
				$('#sidebar').show();
				$('#toggle').css('left', 181);
				$('#toggle i').removeClass('icon-caret-right').addClass(
						'icon-caret-left');
				$('#center').css('marginLeft', 190);
			});
	
	$('#dd1').toggle(
			function(e) {
				$('#sidebar').hide();
				$('#center').css('marginLeft', 0);
			},
			function(e) {
				$('#sidebar').show();
				$('#center').css('marginLeft', 190);
			}
	);
});