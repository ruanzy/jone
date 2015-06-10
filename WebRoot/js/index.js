$(function() {	
	$.goPage = function(url, params){
		$('#main').load(url, params);	
	};
	/**var body = $('body');
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
	});**/
	//$('#header').load('ftl/header.html', function(){

		/**$('ul.nav').delegate('li', 'click', function(){
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
		$('ul.nav li:first').click();**/
	//});
	//$('#sidebar').load('common/sidebar', function(){
	//});
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