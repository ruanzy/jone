$(function() {
	$('body').Layout();
	$('#west').Accordion({
		title : '导航菜单',
		url : 'common/menu'
	});
	$('#main').load('common/welcome');
	/***************************************************************************
	 * $('#center').Tabs(); $('#center').Tabs('add', { title : '欢迎使用', url :
	 * 'welcome.jsp' });
	 **************************************************************************/
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
		$.dialog({
			title:'set',
			width: 350,
			height:180,
			url:'view/user/set.html',
			onShow : function(){
				
			},
   		 	ok:function(){
   		 		return false;
   		 	}
		});
	}
	function info(){
		$.dialog({
			title:'asd',
			width: 800,
			height:390,
			url:'view/income/list.html'
		});
	}
	function logout(){
		document.location = 'common/logout';
	}
	$('#asd').SideDown({
		items:items
	});
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