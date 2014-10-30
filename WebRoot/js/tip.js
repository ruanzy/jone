(function($) {
	$.fn.Tip = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this,
					Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return arguments.callee.methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist');
		}
	};
	$.fn.Tip.defaults = {
		pos : 'r',
		width : 200,
		content : 'Hi，知道吗？ <br>CSS常用浮出提示层对三角的写法！'
	};
	$.fn.Tip.methods = {
		init : function(options) {
			var settings = $.extend({}, $.fn.Tip.defaults, options);
			return this
					.each(function() {
						var offset = this.offset();
						var h1 = this.outerHeight();
						var w1 = this.outerWidth();
						var t = offset.top;
						var l = offset.left;
						var css = 'r-tip-arrow-left';
						if (cfg.pos == 't') {
							css = 'r-tip-arrow-bottom';
						}
						if (cfg.pos == 'r') {
							css = 'r-tip-arrow-left';
						}
						if (cfg.pos == 'b') {
							css = 'r-tip-arrow-top';
						}
						if (cfg.pos == 'l') {
							css = 'r-tip-arrow-right';
						}
						var span = '<span class="r-tip-arrow '
								+ css
								+ '"><em>&#9670;</em><i>&#9670;</i></span><span class="r-tip-content">'
								+ cfg.content + '</span>';
						var tip = $('<div/>').addClass('r-tip')
								.width(cfg.width).append(span).appendTo('body')
								.hide();
						var h2 = tip.outerHeight();
						var w2 = tip.outerWidth();
						if (cfg.pos == 't') {
							t = t - h2 - 8;
						}
						if (cfg.pos == 'r') {
							l = l + w1 + 8;
						}
						if (cfg.pos == 'b') {
							t = t + h1 + 8;
						}
						if (cfg.pos == 'l') {
							l = l - w2 - 8;
						}
						tip.css({
							top : t,
							left : l
						});
						this.focus(function(e) {
							e.stopPropagation();
							tip.fadeIn('slow');
						}).blur(function(e) {
							e.stopPropagation();
							// tip.fadeOut('slow');
						});
					});
		}
	};
})(jQuery);