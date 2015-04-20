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
		ds : [],
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
				var li = $("<li class='selectbox'></li>");
				var p1 = [];
				p1.push("<div class='text'></div><i class='icon-angle-down'></i>");
				p1.push("<input type='text' class='searchbox'/><ul class='items'></ul><div class='mask'></div>");
				me.wrap(li).after(p1.join(''));
				var dropdown = me.parent("li");
				var txt = dropdown.find("div.text").width(me.outerWidth() - 2).height(me.outerHeight() - 2).css('line-height', (me.outerHeight() - 2) + 'px');
				var W = dt.width();
				var H = dt.outerWidth();
				var dropdown_toggle = dt.siblings("a.dropdown-toggle").css('min-width', me.outerWidth() - 2);
				var dd = dropdown.find('.items').css('min-width', me.outerWidth() - 2);
				var searchbox = dropdown.find('.searchbox').width(W - 22).hide();
				if(opts.searchbox){
					searchbox.show();
					//解决点击searchbox 冒泡到$(document).bind("mouseup")的问题
					searchbox.bind("mouseup", function(e) {
						e.stopPropagation();
					});
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
							var opts = $(this).data('options');
							$.each(opts.ds, function(){
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
				if(opts.url){
					$.ajax({
						url : opts.url,
						cache : false,
						async : false,
						dataType : 'json',
						success : function(result) {
							opts.ds = result;
						}
					});
				}
				$(this).data('options', opts);
				dropdown.bind("click", function(e) {
					$(this).addClass('expand');
					searchbox.val('');
					var opts = me.data('options');
					me.selectbox('reload', opts.ds);
					dd.show();
					e.stopPropagation();
				});
				dd.delegate('li', "click", function(e) {
					var as = $('li', dd);
					var idx = as.index(this);
					var t = $(this).text();
					txt.html(t);
					var v = $(this).attr('v');
					me.val(v);
					$(this).siblings("li.selected").removeClass("selected");
					$(this).addClass("selected");
					dd.hide();
					var opts = me.data('options');
					var data = me.data('data');
					opts.change(data[idx]);
					dt.removeClass('expand');
					e.stopPropagation();
				});
				$(document).click(function() {
					dd.hide();
					dt.removeClass('expand');
				});
				//解决点击其他下拉dd不隐藏的问题
				$(document).bind("mouseup", function(e) {
					var t = $(e.target);
					var tp = t.parents('#dt_' + rdm);
					if(tp.length == 0){
						dd.hide();
						dt.removeClass('expand');
					}
				});
			});
		},
		options : function() {
			return this.data('options');
		},
		setValue : function(val) {
			if (val) {
				this.val(val);
				var ds = $(this).data('options').ds;
				var opts = this.selectbox('options');
				var textField = opts.textField;
				var valueField = opts.valueField;
				var item;
				$.each(ds, function(){
					if(this[valueField] == val){
						item = this;
						return false;
					}
				});
				if(item){
					this.siblings("div.text").html(item[textField]);
					this.siblings(".items").find("li[v='" + item[valueField] + "']").addClass("selected");
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
			this.siblings('.items').empty().append(items.join(''));
			$(this).data('data', data);
		}
	};
})(jQuery);