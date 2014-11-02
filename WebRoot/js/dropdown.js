(function($) {
    var _dropdown = false;
    $.dropdown = function(cfg) {
        if(!$.dropdown.opened) {
            $.dropdown.opened = true;
        } else {
            return _dropdown;
        }		
		var defaults = {
        	width: 120,
            pos: [200,200],
            items:[]
        };
        cfg = $.extend({}, defaults, cfg);
        var items= cfg.items;       
		var ul = $('<ul/>').addClass('r-dropdown').width(cfg.width).css("top",  cfg.pos[1]).css("left", cfg.pos[0]);
		var arr = [];
		for(var k in cfg.items){
			if(items[k] === '-'){
				ul.append("<li class='divider'></li>");
			}else{
				var icon = items[k].icon||'';
				var text = items[k].text||'';
				var url = items[k].url;
				var action = items[k].action;
				var tag = "<li><a";
				if(url&&!action){
					tag += " href='" + url + "'";
				}
				tag += "><i class='" + icon + "'></i><span style='padding-left:8px;'>" + text + "</span></a></li>";
				var row = $(tag).appendTo(ul);	
				if(action){									
					row.find('a').click(function(e){
						e.preventDefault();
						e.stopPropagation();
						action();
						$.dropdown.close();
					});
				}				
			}
		}
		ul.appendTo('body');
		_dropdown = ul;
		return _dropdown;
    };
    $.dropdown.close = function() {
        if(!$.dropdown.opened || _dropdown == undefined) {
            return false;
        }
        $.dropdown.opened = false;
        _dropdown.hide().remove();
    };
})(jQuery);