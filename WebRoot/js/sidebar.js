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
	$.fn.Accordion.defaults = {
		title:'menu',
		data:[],
		active: 0,
		url: '',
		format:function(bar){
			return "<i class='icon-list-alt'></i> " + bar.name;
		},
		itemClick:function(item){
			
		}
	};
	$.fn.Accordion.methods = {
		init: function(options) {
			var settings = $.extend({}, $.fn.Accordion.defaults, options);
			return this.each(function(){
				$(this).data('options', settings);
				var el = $(this);
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
				html.push("<dl class='accordion'>");
				//html.push("<h3 class=title>" + settings.title + "</h3><ul>");
				$(data).each(function(){
					var text = this.name;
					var icon = this.icon;
					html.push("<dt class=header><i class='icon-list-alt' style='font-size:18px;'></i> " + text + "</dt>");
					html.push("<dd>");
					$(this.children).each(function(){
						var url = this.url;
						html.push("<a url='" + url + "'>");
						html.push(settings.format(this));
						html.push("</a>");
					});
					html.push("</dd>");
				});
				html.push("</dl>");
				el.append(html.join(''));
				var dl = $('dl', el);
				var headers = $('dt', el);
				var dd = $('dd', el);
				headers.click(function(){
					var h = dl.height();
					var padding = parseInt(dd.css('padding'), 10);
					var H = h - headers.size()*(headers.outerHeight()) - padding*2;
					$('dd.active', el).removeClass('active').hide();
					$(this).next('dd').addClass('active').height(H).show();
				});
				headers.eq(0).trigger('click');
				$('dd a', el).click(function(){
					var url = $(this).attr('url');
					var title = $(this).text();
					var item = {url : url, title : title};
					settings.itemClick(item);
				});
				$(window).resize(function(){
					var headers = $('dt', el);
					var padding = parseInt(headers.css('padding'), 10);
					var H = $(window).height()-$('#header').outerHeight() - headers.size()*(headers.outerHeight()) -2;
					headers.next('dd').height(H);
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