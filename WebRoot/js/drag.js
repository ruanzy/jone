(function($) {
	$.fn.drag = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this,
					Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return arguments.callee.methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist');
		}
	};
	$.fn.drag.defaults = {
			left: 0,
			top: 0,
			currentX: 0,
			currentY: 0,
			move: false
	};
	function Median(target,min,max) {
		if (target > max) return max;
		else if (target < min) return min;
		else return target;
	}
	$.fn.drag.methods = {
			init : function(options) {
				var opts = $.extend({}, $.fn.drag.defaults, options);
				return this.each(function() {
			    	var me = $(this);
			    	var bar = $(opts.handler, me) || me;
			    	opts.left = me.css("left");
			    	opts.top = me.css("top");
					var tw = me.outerWidth();
					var th = me.outerHeight();
					var maxw = $(document).width() - me.outerWidth();
				    var maxh = $(document).height() - me.outerHeight();
					$(document).mousemove(function(e) {
			    		if (opts.move) {
							var nowX = e.clientX, nowY = e.clientY;
							var disX = nowX - opts.currentX, disY = nowY - opts.currentY;
							var tX = parseInt(opts.left) + disX;
							var tY = parseInt(opts.top) + disY;
							var maxw = $(document).width() - tw;
							var maxh = $(document).height() - th;
							me.css("left", Median(tX, 0, maxw) + "px");
							me.css("top", Median(tY, 0, maxh) + "px");
						}
			    	});
			    	$(document).mouseup(function(e) {
			    		opts.move = false;	
			    		opts.left = me.css("left");
			    		opts.top = me.css("top");
			    	});
			    	bar.mousedown(function(e) {
			    		opts.move = true;
			    		if(!e){
			    			e = window.event;
			    			this.onselectstart = function(){
			    				return false;
			    			}  
			    		}
			    		opts.currentX = e.clientX;
			    		opts.currentY = e.clientY;
						e.preventDefault();
			    	});
				});
			}
    };
})(jQuery);