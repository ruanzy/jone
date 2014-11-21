(function($) {
    $.fn.contextmenu = function(cfg) {
    	var defaults = {
            width: 100,
            items: []
        };
        cfg = $.extend({}, defaults, cfg);
        $(this).bind('contextmenu',
        function(e) {
            e.preventDefault();
            e.stopPropagation();
            $('.contextmenu-root').remove();
            var x = e.pageX;
            var y = e.pageY;
            var items = cfg.items;
            var arr = [];
            function build(items, isroot) {
            	arr.push("<ul class='contextmenu");
            	if(isroot){
            		arr.push(" contextmenu-root");
            	}else{
            		arr.push(" contextmenu-sub");
            	}
            	arr.push("'>");
                for(var k in items){
                	var item = items[k];
	            	if (item == '-') {
	                    arr.push("<li class='divider'></li>");
	                } else {
	                    var icon = item.icon || '';
	                    var text = item.text || '';
	                    var action = item.action;
	                    if(item.subitems){
	                    	arr.push("<li><a href='javascript:;'>");
	                    	arr.push("<span class='ellipsis'><i class='" + icon + "'></i> " + text + "</span>");
	                    	arr.push("<span class='arrow'><i class='icon-caret-right'></i></span></a>");
	                    }else{
	                    	arr.push("<li><a href='javascript:;'><i class='" + icon + "'></i> " + text + "</a>");
	                    }
	                    if(item.subitems){
	                    	build(item.subitems, false);
	                    }
	                    arr.push("</li>");
	                }
                }
                arr.push("</ul>");
            }
            build(items, true);
            $('body').append(arr.join(''));
            var cm = $('ul.contextmenu-root').width(cfg.width).css({
                top: y,
                left: x
            });
            $('li').hover(
            		function(e){
            			e.stopPropagation();
            			$(this).find('ul.contextmenu-sub:first').show();
            		},
            		function(e){
            			e.stopPropagation();
            			$(this).find('ul.contextmenu-sub:first').hide();
            		}
            );
            $(document).click(function(e) {
                var t = $(e.target);
                if (t.closest('ul.contextmenu-root').size() == 0) {
                	$('ul.contextmenu-root').hide();
                }
            });
            var lis = $('li', cm);
            var trigger = $(this);
            cm.delegate('li', 'click', function() {
                var idx = lis.index(this);
                var item = items[idx];
                cm.hide().remove();
                if (item.action) {
                    item.action(trigger);
                }
            });
        });
    };
})(jQuery);