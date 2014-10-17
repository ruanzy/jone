(function($){        
	$.fn.Accordion = function(method) {
		if (typeof method == 'string') {
            return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
	$.fn.Accordion.methods = {
		init: function(options) {
			var defaults = {
				title:'menu',
				data:[],
				active: 0,
				url: '',
				format:function(bar){
					return "<div style='color:#585858;text-align:center;font-size:15px;'><i class='icon-list-alt'></i> " + bar.name + "</div> ";
				}
			};
			var settings = $.extend({}, defaults, options);
			return this.each(function(){
				$(this).data('options', settings);
				var el = $(this).addClass('accordion');
				var data = new Array();
				if(settings.url){
					$.ajax({
						url:settings.url,
						cache: false,
						async:false,
						dataType:'json',
				        success:function(result){
				        	data = result;
						}
					});
				}else{
					data = settings.data;
				}
				data = p2s(data);
				var html = new Array();
				//html.push("<h3 class=title>" + settings.title + "</h3><ul>");
				$(data).each(function(){
					var text = this.name;
					var icon = this.icon;
					html.push("<li class=item><div class=header><span class='icon-list-alt' style='font-size:18px;'></span> " + text + "</div><ul class=body>");
					$(this.children).each(function(){
						var url = this.url;
						html.push("<li url='" + url + "'>");
						html.push(settings.format(this));
						html.push("</li>");
					});
					html.push("</ul></li>");
				});
				html.push("</ul>");
				el.append(html.join(''));
				var headers = $('.header', el);
				var H;
				//var padding = parseInt(el.css('padding'), 10);
				//var border = parseInt(el.css('border-width'), 10);
				var h = el[0].style.height;
				if(!h){
					if(el.parent().is('body')){
						H = $(window).height() - 42 - headers.size()*(headers.outerHeight()) - $('.title', el).outerHeight() - 40;
					}else{
						H = $(window).height() - 131 - headers.size()*(headers.outerHeight());
					}
				}else{
					H = el.height() - headers.size()*(headers.outerHeight());
				}
				var H = $(window).height()-$('#head').outerHeight()-42 - headers.size()*(headers.outerHeight()) -2;
				headers.next('.body').height(H).hide();
				headers.click(function(){
					$('.ddd', el).removeClass('ddd').next('.body').hide();
					$(this).addClass('ddd');
					$(this).next('.body').show();
				});
				headers.eq(0).trigger('click');
				$('ul.body li', el).click(function(){
					var url = $(this).attr('url');
					var title = $(this).text();
					$('#pill').html("<i class='icon-home'></i> " + title);
					$('#main').load(url + '?_=' + new Date().getTime(), function(){
						permit('#main');
					});
					/**var exists = $('#center').Tabs('exists', title);
					if(!exists){
						$('#center').Tabs('add', {title: title, closable: true, url: url});
					}else{
						$('#center').Tabs('active', title);
					}**/
				});
				$(window).resize(function(){
					var headers = $('.header', el);
					var padding = parseInt(headers.css('padding'), 10);
					var H = $(window).height()-$('#head').outerHeight()-42 - headers.size()*(headers.outerHeight()) -2;
					headers.next('.body').height(H);
				}); 
			});
        }
	}; 
})(jQuery);
function p2s(data) {
	var i,l;
	if (!data) return [];
	var r = [];
	var t = [];
	for (i=0, l=data.length; i<l; i++) {
		t[data[i]["id"]] = data[i];
	}
	for (i=0, l=data.length; i<l; i++) {
		if (t[data[i]["pid"]]) {
			if (!t[data[i]["pid"]]["children"])
				t[data[i]["pid"]]["children"] = [];
			t[data[i]["pid"]]["children"].push(data[i]);
		} else {
			r.push(data[i]);
		}
	}
	return r;
}