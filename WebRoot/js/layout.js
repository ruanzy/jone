(function($){        
	$.fn.Layout = function(method) {
		if (typeof method == 'string') {
            return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
	$.fn.Layout.methods = {
		init: function(options) {
			var defaults = {
				w_w: 190
			};
			var settings = $.extend({}, defaults, options);
			return this.each(function(){
				var me = $(this);
				var W1 = $(window).width();
				me.addClass('layout');
				$('#sidebar', me).css('float', 'left').width(settings.w_w);
				$('#center', me).css('margin-left', settings.w_w);
				me.data('options', settings);
				function resize() {
					var W = $(window).width();
					var H = $(window).height();
					var NH = $('#head').outerHeight();
					var WW = $('#sidebar').outerWidth();
					$('#sidebar').height(H - NH - 2);
					$('#center').height(H - NH - 1);
					$('#main').height(H - NH - 101);
				}
				$(window).resize(resize);
				resize();
			});
        }
	}; 
})(jQuery);