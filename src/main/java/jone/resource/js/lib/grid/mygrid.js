(function($) {
	var privateFunction = function() {
		// 执行代码
	}

	var methods = {
		init : function(options) {
			return this.each(function() {
				var $this = $(this);
				var settings = $.extend({}, defaults, options);
				$this.data('pluginName', settings);
				$this.on('click', function() {
					$('div').css({
						background : settings.bgc
					});
				});
			});
		},
		val : function(options) {
			var someValue = this.eq(0).html();
			return someValue;
		}
	};

	$.fn.pluginName = function() {
		var method = arguments[0];
		if (methods[method]) {
			method = methods[method];
			arguments = Array.prototype.slice.call(arguments, 1);
		} else if (typeof (method) == 'object' || !method) {
			method = methods.init;
		} else {
			$.error('Method ' + method + ' does not exist on jQuery.pluginName');
			return this;
		}
		return method.apply(this, arguments);
	}
})(jQuery);