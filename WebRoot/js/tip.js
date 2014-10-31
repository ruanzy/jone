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
		width : 120,
		mode : 'hover',
		content : 'Hi，知道吗？ <br>CSS常用浮出提示层对三角的写法！'
	};
	$.fn.Tip.methods = {
		init : function(options) {
			var opts = $.extend({}, $.fn.Tip.defaults, options);
			return this
					.each(function() {
						var me = $(this).css({position:'relative'});
						var offset = me.offset();
						var h1 = me.outerHeight();
						var w1 = me.outerWidth();
						var padding = parseInt(me.attr('padding'));
						var t = offset.top;
						var l = offset.left;
						var css = 'r-tip-arrow-left';
						var pos = opts.pos;
						if (pos == 't') {
							css = 'r-tip-arrow-bottom';
						}
						if (pos == 'r') {
							css = 'r-tip-arrow-left';
						}
						if (pos == 'b') {
							css = 'r-tip-arrow-top';
						}
						if (pos == 'l') {
							css = 'r-tip-arrow-right';
						}
						var span = '<span class="r-tip-arrow '
								+ css
								+ '"><em>&#9670;</em><i>&#9670;</i></span><span class="r-tip-content">'
								+ opts.content + '</span>';
						var tip = $('<div/>').addClass('r-tip')
								.width(opts.width).append(span).appendTo(me);//.hide();
						var h2 = tip.outerHeight();
						var w2 = tip.outerWidth();
						if (pos == 't') {
							t = t - h2 - 8;
						}
						if (pos == 'r') {
							l = l + w1 + 8;
						}
						if (pos == 'b') {
							t = h1 + 8;
							l = 0;
						}
						if (pos == 'l') {
							l = l - w2 - 8;
						}
						tip.css({
							top : t,
							left : l
						});
						var mode = opts.mode;
						if (mode == 'hover') {
							me.bind('mouseenter', function(e) {
								e.stopPropagation();
								tip.show();
							});
							me.bind('mouseleave', function(e) {
								e.stopPropagation();
								tip.hide();
							});
						}
						if (mode == 'click') {
							me.bind('click', function(e) {
								//e.stopPropagation();
								tip.fadeIn('slow');
							});
							me.bind('blur', function(e) {
								//e.stopPropagation();
								tip.fadeOut('slow');
							});
						}
					});
		}
	};
})(jQuery);