(function($){        
	$.fn.Tabs = function(method) {
		if (typeof method == 'string') {
            return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
	$.fn.Tabs.methods = {
		init: function(options) {
			var defaults = {
				ajax: false,
				active: 0,
				tabs: []
			};
			var settings = $.extend({}, defaults, options);
			return this.each(function(){
				$(this).data('options', settings);
				var el = $(this).addClass('tabs');
				var html = new Array();
				html.push("<ul class=tabs-title></ul>");
				html.push("<div class=tabs-content>");
				if(!settings.ajax){
					html.push("<iframe id='main' name='main' src='' frameborder=0 width=100% height=100%></iframe>");
				}
				html.push("</div>");
				el.append(html.join(''));
				
				$('li', el).live('click', function(){   
					var url = $(this).attr('url') + '?_=' + new Date().getTime();   
					$('li.active', el).removeClass('active');   
					$(this).addClass('active');   
					if(settings.ajax){
						$('.tabs-content', el).load(url);
					}else{
						$('#main').attr('src', url);
					}
				});
				$('li button', el).live('click', function(e){   
					e.stopPropagation();
					var p = $(this).parent(); 
					var tab = {title: p.text()};
					var idx = el.Tabs('getTabIndex', tab);
					el.data('options').tabs.splice(idx, 1);
					if(p.hasClass('active')){
						el.Tabs('active', 0);
					}
					p.remove(); 
				}); 
				var padding = parseInt($('.tabs-content', el).css('padding'), 10); 
				var th = parseInt($('ul.tabs-title', el).outerHeight()); 
				$('.tabs-content', el).height(el.height() - th - padding*2);   
				$(window).resize(function(){
					var th = parseInt($('ul.tabs-title', el).outerHeight()); 
					$('.tabs-content', el).height(el.height() - th - padding*2);   
				});   
			});
        },
        add: function(tab) {
			return this.each(function(){
				var el = $(this);
				var opts = el.data('options');
				opts.tabs.push(tab);
				el.data('options', opts);
				var url = tab.url;
				$('li.active', el).removeClass('active');   
				var html = new Array();
				html.push("<li class='active' url='" + url + "'>");
				html.push(tab.title);
				if(tab.closable){
					html.push("<button class='btn btn-icon'><i class='icon-remove-sign'></i></button>");
				}
				html.push("</li>");
				$('.tabs-title', el).append(html.join(''));
				if(opts.ajax){
					$('.tabs-content', el).load(url + '?_=' + new Date().getTime()); 
				}else{
					$('#main').attr('src', url + '?_=' + new Date().getTime()); 
				}
			});
        },
        active: function(index) {
			return this.each(function(){
				var idx = index;
				if(isNaN(index)){
					var opts = $(this).data('options');
					$(opts.tabs).each(function(i){
						if(this.title == index){
							idx = i;
							return;
						}
					});
				}
				$('li', this).eq(idx).trigger('click');   
			});
        },
        exists: function(title) {
        	var ret = false;
        	this.each(function(){
				var opts = $(this).data('options');
				$(opts.tabs).each(function(){
					if(this.title == title){
						ret = true;
						return;
					}
				});
        	});
        	return ret;
        },
        tabs: function() {
        	var ret = [];
        	this.each(function(){
        		ret = $(this).data('options').tabs;
        	});
        	return ret;
        },
        getTabIndex: function(tab) {
        	var idx = -1;
        	this.each(function(){
				var opts = $(this).data('options');
				$(opts.tabs).each(function(i){
					if(this.title == tab.title){
						idx = i;
						return;
					}
				});
        	});
        	return idx;
        }
	}; 
})(jQuery);