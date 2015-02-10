(function($) {
	var params = {
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
	$.fn.drag = function(handler) {
    	var target = $(this);
    	var bar = $(handler, target) || target;
		params.left = target.css("left");
		params.top = target.css("top");
		var tw = target.outerWidth();
		var th = target.outerHeight();
		var maxw = $(document).width() - target.outerWidth();
	    var maxh = $(document).height() - target.outerHeight();
		$(document).mousemove(function(e) {
    		if (params.move) {
				var nowX = e.clientX, nowY = e.clientY;
				var disX = nowX - params.currentX, disY = nowY - params.currentY;
				var tX = parseInt(params.left) + disX;
				var tY = parseInt(params.top) + disY;
				var maxw = $(document).width() - tw;
				var maxh = $(document).height() - th;
				target.css("left", Median(tX, 0, maxw) + "px");
				target.css("top", Median(tY, 0, maxh) + "px");
			}
    	});
    	$(document).mouseup(function(e) {
    		params.move = false;	
			params.left = target.css("left");
			params.top = target.css("top");
    	});
    	bar.mousedown(function(e) {
    		params.move = true;
    		if(!e){
    			e = window.event;
    			this.onselectstart = function(){
    				return false;
    			}  
    		}
    		params.currentX = e.clientX;
    		params.currentY = e.clientY;
			e.preventDefault();
    	});
    };
})(jQuery);