$(function() {	
	var body = $('body');
	var W1 = $(window).width();
	var sw = 190;
	body.addClass('layout');
	$('#sidebar', body).width(sw);
	var NH = $('#header').outerHeight();
	$('#sidebar', body).css('margin-top', NH);
	$('#center', body).css('margin-top', NH);
	$('#center', body).css('margin-left', sw);
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
	$('#sidebar').Accordion({
		title : '导航菜单',
		url : 'common/menu'
	});
	$('#center').load('common/center',function(){
		$('#main').load('common/welcome');
	});
});