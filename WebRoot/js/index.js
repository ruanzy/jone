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
		text : '个人资料'
	}, '-', {
		icon : 'icon-off',
		text : '退出',
		url : 'common/logout'
	} ];
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
	$('#info').Tip({
		pos:'b',
		content:content.join('')
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
});