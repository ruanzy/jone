(function($){  
	var util = require('util');
	$.fn.tabs = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
    $.fn.tabs.defaults = {
		tabs : [], 
		direction :'h',
		selected :0,
		refresh: false,
		onRenderBefore : function(html){return html;}
    };
	$.fn.tabs.methods = {
		init: function(options) {
			var opts = $.extend({}, $.fn.tabs.defaults, options);
			return this.each(function(){
				var el = $(this);
				var html = new Array();
				var tabs = opts.tabs;
				var refresh = opts.refresh;
				var len = tabs.length;
				var H = 50 * (len + 1);
				var selected = opts.selected;
				html.push('<div class="ui-tab-container');
				if(opts.direction == 'h'){
					html.push(' ui-tab-horizontal">');
				}else{
					html.push(' ui-tab-vertical">');
				}
				html.push('<div class="ui-tab">');
				html.push('<ul class="nav nav-tabs">');
				for(var k in tabs){
					var title = tabs[k].title;
					var icon = tabs[k].icon;
					html.push('<li class="uib-tab');
					if(k == selected){
						html.push(' active');
					}
					html.push('">');
					html.push('<a href="javascript:void(0);"><i class="fa ', icon, '"></i> ', title, '</a>');
					html.push('</li>');
				}
				html.push('</ul></div>');
				html.push('<div class="tab-content">');
				for(var j in tabs){
					var content = tabs[j].content;
					var url = tabs[j].url;
					var data = tabs[j].data;
					html.push('<div class="tab-pane');
					if(j == selected){
						html.push(' active');
					}
					html.push('">');
//					if(content){
//						html.push(content);
//					}else if(url){
//						var tpl = util.tpl(url, data);
//						html.push(tpl);
//					}
					html.push('</div>');
				}
				//loading
				//html.push('<div style="display : block; position:absolute;z-index: 1000; border: none; margin: 0px; padding: 10px; top: 10%; left: 45%; opacity: .50; background-color: #ccc;"><div style="border: 1px solid red;">loading...</div></div>');
				html.push("</div>");
				html.push("</div>");
				html.push("</div>");
				var code = html.join('');
				if(opts.onRenderBefore){
					code = opts.onRenderBefore(code);
				}	
				el.html(code);
//				for(var k in tabs){
//					var open = tabs[k].open;
//					open && open();
//				}
				var navtabs = el.find('.nav-tabs');
				el.find('.tab-content').css({minHeight : H});
				var navpanes = el.find('.tab-pane');
				var lis = navtabs.find('li.uib-tab');
				navtabs.on('click', 'li', function (event) { 
					var idx = lis.index($(this));
					if(opts.onselect){
						opts.onselect.call(tabs[idx], idx);
					}
					$(this).siblings('li').removeClass('active');
					$(this).addClass('active');
					var content = tabs[idx].content;
					var url = tabs[idx].url;
					var data = tabs[idx].data;
					var open = tabs[idx].open;
					var cnt = '';
					if(content){
						cnt = content;
					}else if(url){
						cnt = util.tpl(url, data);
					}
					if(opts.onRenderBefore){
						cnt = opts.onRenderBefore(cnt);
					}
					var loaded = $(this).attr('loaded');
					if(!loaded){					
						navpanes.eq(idx).html(cnt);
						open && open();
						$(this).attr('loaded', 1);
					}
					if(loaded && refresh){					
						navpanes.eq(idx).html(cnt);
						open && open();
					}
					navpanes.removeClass('active');
					navpanes.eq(idx).addClass('active');
					opts.slected = idx;
				});
				lis[selected].click();
			});
        },
        getData : function(){
        	
        }
	}; 

	function drog(){            
       
    }  
})(jQuery);