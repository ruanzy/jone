(function($) {
	$.fn.SideDown = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this,
					Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return arguments.callee.methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist');
		}
	};
	$.fn.SideDown.defaults = {
		width : 100,
		trigger : 'hover'
	};
	$.fn.SideDown.methods = {
		init : function(options) {
			var opts = $.extend({}, $.fn.SideDown.defaults, options);
			return this.each(function() {
				var me = $(this);
				var dt = $('dt', this);
				var dd = $('dd', this).width(opts.width);
				var rdm = new Date().getTime() + '_'
						+ Math.floor(Math.random() * (100 - 1 + 1) + 1);
				dt.attr('id', 'dt_' + rdm);
				var items = opts.items;
				var url = opts.url;
				if (items) {
					var html = [];
					for ( var k in items) {
						html.push("<a href='javascript:'");
						if (items[k] === '-') {
							html.push(" class='divider'>");
						} else {
							html.push(">");
							var icon = items[k].icon || '';
							var text = items[k].text || '';
							html.push("<i class='", icon, "'></i>");
							html.push("<span style='padding-left:8px;'>", text, "</span>");
						}
						html.push("</a>");
					}
					dd.html(html.join(''));
					var as = $('a', dd);
					dd.delegate('a', 'click', function(e) {
						var idx = as.index(this);
						var action = items[idx].action;
						if (action) {
							action.call();
						}
						dd.hide();
					});
				} else if (url) {
					dd.load(url);
				}
				if (opts.trigger == 'click') {
					me.click(function(e) {
						dd.show();
					});
					$(document).bind("click", function(e) {
						var target = $(e.target);
						if (target.closest('#dt_' + rdm).length == 0) {
							dd.hide();
						} else {
							dd.show();
						}
					});
				}
				if (opts.trigger == 'hover') {
					me.hover(function(e) {
						dd.show();
					}, function(e) {
						dd.hide();
					});
				}
			});
		}
	};
})(jQuery);