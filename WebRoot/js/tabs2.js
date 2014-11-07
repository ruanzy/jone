(function($) {
	$.fn.Tabs = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this,
					Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return arguments.callee.methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist');
		}
	};
	$.fn.Tabs.defaults = {
		active : 0,
		tabs : [],
		select : function(index, me) {
		}
	};
	$.fn.Tabs.methods = {
		init : function(options) {
			var opts = $.extend({}, $.fn.Tabs.defaults, options);
			return this.each(function() {
				var me = $(this);
				me.data('options', opts);
				var tabs = $("dt a", me);
				var panels = $("dd", me);
				me.Tabs('active', opts.active);
				tabs.click(function() {
					var index = tabs.index(this);
					me.Tabs('active', index, opts.select);
				});
			});
		},
		add : function(tab) {
			return this
					.each(function() {
						var el = $(this);
						var opts = el.data('options');
						opts.tabs.push(tab);
						el.data('options', opts);
						var url = tab.url;
						$('li.active', el).removeClass('active');
						var html = new Array();
						html.push("<li class='active' url='" + url + "'>");
						html.push(tab.title);
						if (tab.closable) {
							html
									.push("<button class='btn btn-icon'><i class='icon-remove-sign'></i></button>");
						}
						html.push("</li>");
						$('.tabs-title', el).append(html.join(''));
						if (opts.ajax) {
							$('.tabs-content', el).load(
									url + '?_=' + new Date().getTime());
						} else {
							$('#main').attr('src',
									url + '?_=' + new Date().getTime());
						}
					});
		},
		active : function(index, callback) {
			var tabs = $("dt a", this);
			var panels = $("dd", this);
			tabs.removeClass("active").eq(index).addClass("active");
			panels.removeClass("active").eq(index).addClass("active");
			callback && callback.call(this, index, this);
		},
		exists : function(title) {
			var ret = false;
			this.each(function() {
				var opts = $(this).data('options');
				$(opts.tabs).each(function() {
					if (this.title == title) {
						ret = true;
						return;
					}
				});
			});
			return ret;
		},
		tabs : function() {
			var ret = [];
			this.each(function() {
				ret = $(this).data('options').tabs;
			});
			return ret;
		},
		getTabIndex : function(tab) {
			var idx = -1;
			this.each(function() {
				var opts = $(this).data('options');
				$(opts.tabs).each(function(i) {
					if (this.title == tab.title) {
						idx = i;
						return;
					}
				});
			});
			return idx;
		}
	};
})(jQuery);