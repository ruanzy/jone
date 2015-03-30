$(function() {	
	$.goPage = function(url, params){
		$('#main').load(url, params);	
	};
	var body = $('body');
	var WW = $(window).width();
	var WH = $(window).height();
	var h = $('#content').outerHeight();
	var padding = $('#main').css('padding-top');
	var H = h - 2*parseInt(padding) - 40;
	$('#main').height(H);
	$(window).resize(function(){
		var h = $('#content').outerHeight();
		var padding = $('#main').css('padding-top');
		var H = h - 2*parseInt(padding) - 40;
		$('#main').height(H);
	});
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
				buttons:[
				    {
				    	text : 'OK',
				    	cls : 'btn-primary',
				    	action : function(d) {
				    		
				    	}
				    },
				    {
				    	text : 'Close',
				    	cls : 'btn-default',
				    	action : function(d) {
				    		dialog.close();
				    	}
				    	
				    }
				],
				onShow : function(){
					
				}
			});
		}
		function info(){
			var dialog = $.dialog({
					title:'个人资料',
					width: 350,
					//url:'view/income/list.html'
					url:'common/userinfo',
					buttons:[
					    {
					    	text : 'OK',
					    	cls : 'btn-primary',
					    	action : function(d) {
					    		
					    	}
					    },
					    {
					    	text : 'Close',
					    	cls : 'btn-default',
					    	action : function(d) {
					    		dialog.close();
					    	}
					    	
					    }
					],
					onShow:function(){
						$('#sex').MultiRadio();
					}
				});
		}
		function logout(){
			document.location = 'logout';
		}
		$('#asd').SideDown({
			items:items
		});
		$('#theme').SideDown({
			width: 'auto',
			url:'theme.html'
		});
		$('#toggle').toggle(
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
	$('#sidebar').load('common/sidebar', function(){
		var dt = $('dt', '#accordion1');
		var dd = $('dd', '#accordion1');
		var a = $('a', dd);
		dt.click(function(){
			if($(this).hasClass('active')){
				return false;
			}else{
				$('dt.active', '#accordion1').removeClass('active').next('dd').slideUp();
				$(this).addClass('active').next('dd').slideDown();
			}
		});
		a.click(function(){
			var url = $(this).attr('url');
			var title = $(this).text();
			$('#nav').text(title);
			$(this).addClass('active').siblings('a').removeClass('active');
			var page = url + '?_=' + new Date().getTime();
			//page = url;
			$('#main').load(page, function(){
				permit('#main');
			});
		});
	});
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
	$.goPage('common/welcome');
	/**$('#content').load('common/center',function(){
	});**/
});

/**
 * 时间对象的格式化
 */
Date.prototype.format = function(format) {
	/*
	 * format="yyyy-MM-dd hh:mm:ss";
	 */
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	}

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}

	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
};