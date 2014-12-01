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
				title:'设置',
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
		$('ul.nav').delegate('li', 'click', function(){
			$(this).siblings().removeClass('selected');
			$(this).addClass('selected');
			var mid = $(this).attr('id').substring(1);
			var mname = $(this).attr('name');
			$('#sidebar').load('common/sidebar', {mid : mid, mname : mname}, function(){
				$('#sidebar dd a').click(function(){
					var url = $(this).attr('url');
					var title = $(this).text();
					var item = {url : url, title : title};
					$('#pill #nav').text(title);
					$('#main').load(url + '?_=' + new Date().getTime(), function(){
						//permit('#main');
					});
				});
				$('#sidebar dd:first a').click();
			});
		});
		$('ul.nav li:first').click();
	});
	$('#sidebar').height(WH - 45);
	/**$('#sidebar').Accordion({
		title : '导航菜单',
		url : 'common/menu',
		itemClick:function(item){
			var url = item['url'];
			var title = item['title'];
			$('#nav').text(title);
			$('#main').load(url + '?_=' + new Date().getTime(), function(){
				permit('#main');
			});
		}
	});**/
	$('#main').load('common/welcome');
	/**$('#content').load('common/center',function(){
	});**/
});