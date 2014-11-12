$(function() {	
	var body = $('body');
	var WW = $(window).width();
	var WH = $(window).height();
	var sw = 190;
	$('#sidebar', body).width(sw);
	var NH = $('#header').outerHeight();
	$('#center', body).css('margin-top', NH);
	$('#content', body).css('margin-left', sw);
	$('#content', body).height(WH - NH);
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
					$('#content', body).css('margin-left', 0);
				},
				function(e) {
					$('#sidebar').show();
					$('#content', body).css('margin-left', sw);
				}
		);
	});
	$('#sidebar').Accordion({
		title : '导航菜单',
		url : 'common/menu'
	});
	var CH = $('#content', body).outerHeight();
	$('#main', body).height(CH - 41 - 60);
	$('#main').load('common/welcome');
	/**$('#content').load('common/center',function(){
	});**/
});