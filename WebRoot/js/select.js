(function($) {
	$.fn.selectbox = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this,
					Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return arguments.callee.methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist');
		}
	};
	$.fn.selectbox.defaults = {
		url : null,
		ds : null,
		textField : 'text',
		valueField : 'value',
		filter : function(q, item) {
			return item['text'].indexOf(q) != -1;
		},
		searchbox : null,
		change : function(item) {}
	};
	$.fn.selectbox.methods = {
		init : function(options) {
			var opts = $.extend({}, $.fn.selectbox.defaults, options);
			return this.each(function() {
				if (this.tagName != 'INPUT') {
					return;
				}
				var me = $(this).hide();
				$(this).data('options', opts);
				var disabled = me.attr('disabled');
				var rdm = new Date().getTime() + '_'
						+ Math.floor(Math.random() * (100 - 1 + 1) + 1);
				var dl = $("<dl class='selectbox'><dt id='dt_" + rdm
						+ "'></dt><dd><input type='text' class='searchbox'/><ul class='items'></ul></dd><div class='mask'></div></dl>");
				var p1 = [];
				p1.push("<div class='text'></div><i class='icon-angle-down'></i>");
				me.wrap(dl).after(p1.join(''));
				var txt = me.siblings("div.text");
				var dt = me.parent("dt").width(me.outerWidth() - 2).height(me.outerHeight() - 2).css('line-height', (me.outerHeight() - 2) + 'px');
				var W = dt.width();
				var H = dt.outerWidth();
				var dd = dt.siblings("dd").css('min-width', me.outerWidth() - 2);
				var searchbox = dd.find('.searchbox').width(W - 22).hide();
				if(opts.searchbox){
					searchbox.show();
					searchbox.bind("click", function(e) {
						e.stopPropagation();
					});
					searchbox.keyup(function(e) {
						if (e.which == 37 || e.which == 38 || e.which == 39
								|| e.which == 13 || e.which == 40) {
							return false;
						}
						var q = $.trim(searchbox.val());
						setTimeout(function() {
							var data2 = [];
							var data = me.data('data');
							$.each(data, function(){
								var item = this;
								if(opts.filter(q, item)) {
									data2.push(item);
								}
							});
							me.selectbox('reload', data2);
						}, 0);
					});
				}
				var ul = dd.find('.items');
				var mask = dt.siblings("div.mask");
				if (disabled) {
					mask.show();
				}
				var data = opts.ds;
				if(!data){
					$.ajax({
						url : opts.url,
						cache : false,
						async : false,
						dataType : 'json',
						success : function(result) {
							data = result;
						}
					});
				}
				$(this).data('data', data);
				dt.bind("click", function(e) {
					$(this).addClass('expand');
					searchbox.val('');
					me.selectbox('reload', data);
					dd.show();
					e.stopPropagation();
				});
				dd.delegate('li', "click", function(e) {
					var t = $(this).text();
					txt.html(t);
					var v = $(this).attr('v');
					me.val(v);
					$(this).siblings("li.selected").removeClass("selected");
					$(this).addClass("selected");
					dd.hide();
					opts.change({t:t,v:v});
					dt.removeClass('expand');
					e.stopPropagation();
				});
				$(document).click(function() {
					dd.hide();
					dt.removeClass('expand');
				});
			});
		},
		options : function() {
			return this.data('options');
		},
		setValue : function(val) {
			if (val) {
				this.val(val);
				var data = $(this).data('data');
				var opts = this.selectbox('options');
				var textField = opts.textField;
				var valueField = opts.valueField;
				var item;
				$.each(data, function(){
					if(this[valueField] == val){
						item = this;
						return false;
					}
				});
				if(item){
					this.siblings("div.text").html(item[textField]);
					this.parent("dt").siblings("dd").find("li[v='" + item[valueField] + "']").addClass("selected");
				}
			}
		},
		getValue : function() {
			return this.val();
		},
		reload : function(data) {
			var items = [];
			if(data && data.length > 0){
				var v0 = this.val();
				var opts = this.selectbox('options');
				$.each(data, function(){
					var t = this[opts.textField];
					var v = this[opts.valueField];
					items.push("<li v='", v, "'");
					if(v == v0){
						items.push(" class='selected'");
					}
					items.push(">");
					items.push(t);
					items.push('</li>');
				});
			}else{
				items.push("<li class='no-results'>没有找到 </li>");
			}
			this.parent("dt").siblings("dd").find('.items').empty().append(items.join(''));
		}
	};
})(jQuery);