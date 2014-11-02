(function($) {
	$.fn.SideDown = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this,
					Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return arguments.callee.methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist');
		}
	};
	$.fn.SideDown.defaults = {
		width : 120,
		trigger : 'click'
	};
	$.fn.SideDown.methods = {
		init : function(options) {
			var opts = $.extend({}, $.fn.SideDown.defaults, options);
			return this.each(function() {
				var me = $(this);
				var dt = $('dt', this);
				var dd = $('dd', this);
				var rdm = new Date().getTime() + '_' + Math.floor(Math.random()*(100 - 1 + 1) + 1);
				dt.attr('id', 'dt_' + rdm);
				var items = opts.items;
				if (items){
					var html = [];
					for(var k in items){
						if(items[k] === '-'){
							html.push("<div class='divider'></div>");
						}else{
							var icon = items[k].icon||'';
							var text = items[k].text||'';
							var url = items[k].url;
							var action = items[k].action;
							var str = "<div class='item'><a";
							if(url&&!action){
								str += " href='javascript:" + url + "'";
							}
							str += "><i class='" + icon + "'></i><span style='padding-left:8px;'>" + text + "</span></a></div>";
							html.push(str);
						}
					}
					dd.html(html.join(''));
					
					for(var k in items){
						if(items[k] === '-'){
							continue;
						}else{
							var action = items[k].action;
							if(action){
								var div = $('div:nth(' + k + ')', dd);
								div.click(function(e){
									e.preventDefault();
									e.stopPropagation();
									action();
								});
							}
							var str = "<div class='item'><a";
							if(url&&!action){
								str += " href='javascript:" + url + "'";
							}
							str += "><i class='" + icon + "'></i><span style='padding-left:8px;'>" + text + "</span></a></div>";
							html.push(str);
						}
					}
				}	
				dt.click(function(e){
					//var t = e.target;
					//if(t.tagName == 'DT'){
						dd.show();
					//}
						e.stopPropagation();
				});
				$(document).bind("click",function(e){ 
					var target = $(e.target); 
					if(target.closest('#dt_' + rdm).length == 0){ 
						dd.hide(); 
					} else{
						dd.show(); 
					}
				});
			});
		}
	};
})(jQuery);