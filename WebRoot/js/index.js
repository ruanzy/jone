$(function() {	
	var body = $('body');
	var WW = $(window).width();
	var WH = $(window).height();
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
			document.location = 'logout';
		}
		$('#asd').SideDown({
			items:items
		});
		$('#dd1').toggle(
				function(e) {
					$('#sidebar').hide();
					$('#content', body).css('margin-left', 0);
					$('#pill', body).css('left', 0);
				},
				function(e) {
					$('#sidebar').show();
					var sw = $('#sidebar').outerWidth();
					$('#content', body).css('margin-left', sw);
					$('#pill', body).css('left', sw);
				}
		);
	});
	$('#sidebar').Accordion({
		title : '导航菜单',
		url : 'common/menu'
	});
	$('#main').load('common/welcome');
	/**$('#content').load('common/center',function(){
	});**/
});