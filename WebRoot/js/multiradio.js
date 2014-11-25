(function($) {
	$.fn.MultiRadio = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this,
					Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return arguments.callee.methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist');
		}
	};
	$.fn.MultiRadio.defaults = {
		active : 0,
		items : [],
		select : function(index, me) {
		}
	};
	$.fn.MultiRadio.methods = {
		init : function(options) {
			var opts = $.extend({}, $.fn.MultiRadio.defaults, options);
			return this.each(function() {
				var me = $(this);
				var name = me.attr('id');
				me.append("<input type='hidden' name='" + name + "' value='0'/>");
				me.data('options', opts);
				var radioes = $("a", me);
				me.MultiRadio('active', opts.active);
				radioes.click(function() {
					var index = radioes.index(this);
					me.MultiRadio('active', index, opts.select);
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
			var radioes = $("a", this);
			var valctr = $("input[type=hidden]", this);
			radioes.removeClass("active").eq(index).addClass("active");
			valctr.val(radioes.eq(index).attr('v'));
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