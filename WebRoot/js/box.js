(function($) {
    var _tip = false;
    $.tip = function(cfg) {
    	if(!$.tip.opened) {
            $.tip.opened = true;
        } else {
            return _tip;
        }
        var defaults = {
            content: '',
            width: 400,
            height: 100,
            type:'info',
			draggable: false
        };
        cfg = $.extend({}, defaults, cfg);       
		var mask = $('<div/>').addClass('mask');
		var wrap_out = $('<div/>').addClass('box');
		var cls = "tip tip-" + cfg.type;
		var box = $('<div/>').addClass(cls);
		
		
			$("<button/>").addClass("close").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				$.tip.close();
			}).html("<i class='icon-remove'></i>").appendTo(box);
		
		var icon = "<i class='icon-" + cfg.type + "'></i>";
		var txt = " <strong>" + cfg.type + "!</strong>&nbsp;";
		box.append(icon + txt);
		box.append(cfg.content);
		wrap_out.append(box);
		mask.appendTo($('body'));
		wrap_out.appendTo($('body'));
		wrap_out.click(function(e){
			e.stopPropagation();
		});	
        if(cfg.draggable) {
        	var z_idx = wrap_out.css('z-index');
        	var _x = 0;
        	var _y = 0;
        	var ww = $(window).width() +  $(window).scrollLeft();
        	var wh = $(window).height() +  $(window).scrollTop();
        	var w = wrap_out.outerWidth();
        	var h = wrap_out.outerHeight();
        	caption.bind("mousedown", function(e) {
                $.box.drag = true;
                caption.css('cursor', 'move');
                _x= e.pageX - wrap_out.offset().left;
                _y= e.pageY - wrap_out.offset().top;                
                wrap_out.css('z-index', 99999).bind("mousemove", function(e) {
                    if ($.box.drag) {
                        var x = e.pageX - _x;
                        var y = e.pageY - _y;
                        x = (x <= 0) ? 0 : x;
                        x = (x >= ww - w) ? (ww - w) : x;
                        y = (y <= 0) ? 0 : y;
                        y = (y >= wh - h) ? (wh - h) : y;                        
                        wrap_out.css({position:'absolute',top:y,left:x});
                    }
                    wrap_out.bind("mouseup", function() {
                        $(this).removeClass('draggable').css('z-index', z_idx);
                        $.tip.drag = false;
                        caption.css('cursor', 'default');
                    });
                });
                e.preventDefault();
            }).bind("mouseup", function() {
            	wrap_out.removeClass('draggable');
                $.tip.drag = false;
                caption.css('cursor', 'default');
            });
        }	
		_tip = wrap_out;
		resize();
		function resize(){
	    	if(!$.tip.opened) {
	            return false;
	        }
	        var l = ($(window).width() - _tip.outerWidth())/2 + $(window).scrollLeft();
	        var t = ($(window).height() - _tip.outerHeight())/2 + $(window).scrollTop();
	        _tip.css("top",  t).css("left", l);
	    }
		$(window).resize(resize);
		return _tip;
    };
    $.tip.close = function() {
        if(!$.tip.opened || _tip == undefined) {
            return false;
        }
        $.tip.opened = false;
        _tip.remove();
        var mask = $('div.mask');
        mask.fadeOut(function(){
            $(this).remove();
        });
    };
})(jQuery);