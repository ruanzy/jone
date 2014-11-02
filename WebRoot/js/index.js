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
	/**$('#info').bind(
			'click',
			function(e) {
				e.preventDefault();
				e.stopPropagation();
				var pos = [ $(this).offset().left + $(this).outerWidth() - 122,
						$(this).offset().top + $(this).outerHeight() + 1 ];
				$.dropdown({
					pos : pos,
					items : [ {
						icon : 'icon-cog',
						text : '设置'
					}, {
						icon : 'icon-user',
						text : '个人资料'
					}, '-', {
						icon : 'icon-off',
						text : '退出',
						url : 'common/logout'
					} ]
				});

				$(document).click(function() {
					$.dropdown.close();
				});
			});**/
	var items = [ {
		icon : 'icon-cog',
		text : '设置'
	}, {
		icon : 'icon-user',
		text : '个人资料',
		action : info
	}, '-', {
		icon : 'icon-off',
		text : '退出',
		action : logout
	} ];
	
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
	var content = [];
	content.push('<ul class=r-dropdown>');
	for(var k in items){
		if(items[k] === '-'){
			content.push("<li class='divider'></li>");
		}else{
			var icon = items[k].icon||'';
			var text = items[k].text||'';
			var url = items[k].url;
			var action = items[k].action;
			var tag = "<li><a";
			if(url&&!action){
				tag += " href='" + url + "'";
			}
			tag += "><i class='" + icon + "'></i><span style='padding-left:8px;'>" + text + "</span></a></li>";
			content.push(tag);
			/**var row = $(tag).appendTo(ul);	
			if(action){									
				row.find('a').click(function(e){
					e.preventDefault();
					e.stopPropagation();
					action();
					$.dropdown.close();
				});
			}**/				
		}
	}
	content.push('</ul>');
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
				$('i', this).removeClass().addClass("icon-double-angle-right");
			},
			function(e) {
				$('#sidebar').show();
				$('#center').css('marginLeft', 190);
				$('i', this).removeClass().addClass("icon-double-angle-right");
			}
	);
});