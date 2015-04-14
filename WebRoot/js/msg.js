(function($) {
	var _alert = false;
	$.alert = function(type, msg, callback) {
		if (!$.alert.opened) {
			$.alert.opened = true;
		} else {
			return _alert;
		}
		var icon = {
			'success' : 'check',
			'info' : 'info-sign',
			'warning' : 'warning-sign',
			'danger' : 'fire'
		};
		var title = {
			'success' : 'Success',
			'info' : 'Info',
			'warning' : 'Warning',
			'danger' : 'Error'
		};
		var alert = $('div.message');
		if (alert.size() == 0) {
			alert = $("<div class='message'></div>").appendTo($('body'));
		}
		var html = [];
		html.push("<div class='message-dialog'>");
		html.push("<div class='message-header message-header-" + type + "'>");
		html.push("<i class='icon-" + icon[type] + "'></i> ");
		html.push(title[type]);
		html.push("</div>");
		html.push("<div class='message-body'>");
		html.push(msg);
		html.push("</div>");
		html.push("<div class='message-footer'>");
		html.push("<a class='btn btn-" + type + "'>");
		html.push("OK</a>");
		html.push("</div>");
		alert.append(html.join('')).append("<div class='message-mask'></div>");
		var mask = $(".message-mask", alert);
		mask.click(function() {
			alert.close();
		});
		alert.close = function() {
			alert.hide().empty().remove();
			$.alert.opened = false;
			if (callback) {
				callback();
			}
		};
		$('.message-footer .btn', alert).click(function() {
			alert.close();
		});
		alert.show();
		_alert = alert;
		return _alert;
	}
})(jQuery);

(function($) {
	var _confirm = false;
	$.confirm = function(msg, callback) {
		if (!$.confirm.opened) {
			$.confirm.opened = true;
		} else {
			return _confirm;
		}
		var confirm = $('div.message');
		if (confirm.size() == 0) {
			confirm = $("<div class='message'></div>").appendTo($('body'));
		}
		var html = [];
		html.push("<div class='message-dialog'>");
		html.push("<div class='message-header message-header-success'>");
		html.push("<i class='icon-info-sign'></i> 确认");
		html.push("</div>");
		html.push("<div class='message-body'>");
		html.push(msg);
		html.push("</div>");
		html.push("<div class='message-footer'>");
		html.push("<a class='btn btn-success'>YES</a>");
		html.push("<a class='btn btn-default'>NO</a>");
		html.push("</div>");
		confirm.append(html.join(''))
				.append("<div class='message-mask'></div>");
		var mask = $(".message-mask", confirm);
		mask.click(function() {
			confirm.hide().empty().remove();
			$.confirm.opened = false;
			if (callback) {
				callback('CLOSE');
			}
		});
		$(".message-footer").delegate('a', 'click', function(e) {
			var btn = $(this).text();
			confirm.hide().empty().remove();
			$.confirm.opened = false;
			if (callback) {
				callback(btn);
			}
		});
		confirm.show();
		_confirm = confirm;
		return _confirm;
	}
})(jQuery);

(function($) {
	var _dialog = false;
	$.dialog = function(options) {
		/**if (!$.dialog.opened) {
			$.dialog.opened = true;
		} else {
			return _dialog;
		}**/
		options = $.extend({}, $.dialog.defaults, options);
		var dialog;
			dialog = $("<div class='dialog-wrap'></div>").appendTo($('body'));
		var html = [];
		html.push("<div class='dialog'>");
		if(options.title){
			html.push("<div class='dialog-header dialog-header-success'>");
			html.push("<i class='icon-desktop'></i> ");
			html.push(options.title);
			html.push("</div>");
		}
		html.push("<a class='dialog-close'><i class='icon-remove'></i></a>");
		html
				.push("<div class='dialog-body'><div class='dialog-loading'><i class='icon-spinner icon-spin'></i>loading...</div></div>");
		if (options.buttons) {
			html.push("<div class='dialog-footer align-rignt'>");
			$(options.buttons).each(function() {
				var txt = this.text;
				var icon = this.icon;
				var cls = this.cls;
				html.push("<a class='btn " + cls + "'>");
				if (icon) {
					html.push("<i class='icon-" + icon + "'></i> ");
				}
				html.push(txt + "</a>");
			});
			html.push("</div>");
		}
		dialog.append(html.join('')).append("<div class='dialog-mask'></div>");
		var mask = $(".dialog-mask", dialog);
		mask.click(function() {
			//dialog.close();
		});
		var d = $(".dialog", dialog).css('zIndex', _nextZ());
		var bd = $(".dialog-body", dialog).css({
			padding : options.padding,
			minWidth : options.minWidth,
			width : options.width
		});
		if (options.content) {
			bd.html(options.content);
			options.onShow();
		}
		if (options.url) {
			bd.load(options.url, options.params, function(response,status,xhr){
				if(status == 'error'){
					dialog.close();
				}else{
					var ww = $(window).width();
					var wh = $(window).height();
					var w = d.outerWidth();
					var h = d.outerHeight();
					var l = (ww - w) / 2 + $(window).scrollLeft();
					var t = (wh - h) / 2;
					d.css("top", t).css("left", l);
					options.onShow();
					d.css("visibility","visible");
					if(options.drag == true) {
					   var titbar = $('.dialog-header', d);
					   titbar.css({"cursor":"move"}); 
					   d.drag({handler:titbar}); 
					} 
				}
			});
		}
		var close = $(".dialog-close", dialog);
		$(".dialog-close", dialog).click(function(e) {
			dialog.close();
		});
		var footer = dialog.find(".dialog-footer");
		var btns = footer.find("a");
		footer.delegate('a', 'click', function(e) {
			var index = btns.index(this);
			options.buttons[index].action(dialog);
		});
		dialog.close = function() {
			dialog.hide().empty().remove();
			$.dialog.opened = false;
		};
		
		dialog.text = function(content) {
			$(".dialog-body", dialog).html(content);
		};
		dialog.show();
		_dialog = dialog;
		return _dialog;
	};
	$.dialog.defaults = {
		//title : 'Window',
		minWidth : 300,
		height : 100,
		padding : 30,
		drag : false,
		content : '',
		params : null,
		onShow : function() {
		}
	};
	function _nextZ() {
        return $.dialog.zIndex++;
    }
	$.dialog.zIndex = 2015;
})(jQuery);

(function($) {
	$.pop = function(msg, callback) {
		var pop = $('p.message-pop').empty();
		if (pop.size() == 0) {
			pop = $("<p class='message-pop bg-red bg-inverse'></p>").appendTo(
					$('body'));
		}
		pop.append("<i class='icon-ok'></i> " + msg).slideDown().slideUp(800,
				function() {
					if (callback) {
						callback();
					}
				});
	}
})(jQuery);

function ddresize() {
	var _x = 0, _y = 0;
	var ww = $(window).width() + $(window).scrollLeft();
	var wh = $(window).height() + $(window).scrollTop();
	var w = alert.outerWidth();
	var h = alert.outerHeight();
	var flag = false;
	head.bind("mousedown", function(e) {
		flag = true;
		if (window.getSelection()) {
			window.getSelection().removeAllRanges();
		} else {
			try {
				document.selection.empty();
			} catch (e) {
			}
		}
		_x = e.pageX - alert.offset().left;
		_y = e.pageY - alert.offset().top;
		$(document).bind('mousemove', move).bind('mouseup', up);
	});
	function move(e) {
		var x = e.pageX - _x;
		var y = e.pageY - _y;
		x = (x <= 0) ? 0 : x;
		x = (x >= ww - w) ? (ww - w) : x;
		y = (y <= 0) ? 0 : y;
		y = (y >= wh - h) ? (wh - h) : y;
		confirm.css({
			top : y,
			left : x
		});
	}
	function up() {
		flag = false;
		$(document).unbind('mousemove', move).unbind('mouseup', up);
	}
	_alert = alert;
	resize();
	function resize() {
		if (!$.confirm.opened) {
			return false;
		}
		var l = ($(window).width() - _alert.outerWidth()) / 2
				+ $(window).scrollLeft();
		var t = ($(window).height() - _alert.outerHeight()) / 2
				+ $(window).scrollTop();
		_alert.css("top", t).css("left", l);
	}
	$(window).resize(resize);
}

function permit(context) {
	var opts = $(context).find('[funcid]');
	if (opts.size() > 0) {
		var powers = [];
		$.ajax({
			url : 'common/op',
			cache : false,
			async : false,
			dataType : 'json',
			success : function(result) {
				$.each(result, function() {
					powers.push(this.id);
				});
			}
		});
		opts.each(function() {
			var funcid = parseInt($(this).attr('funcid'));
			if ($.inArray(funcid, powers) == -1) {
				$(this).hide();
			}
		});
	}
}

$(document).ajaxError(function(event, xhr, options, exc) {
	if (xhr.status == 1111) {
		$.alert('success', '您的登录已过期,请重新登录！', function() {
			top.document.location = './';
		});
	} else if (xhr.status == 1112) {
		$.alert({
			msg : '请求异常！'
		});
	} else if (xhr.status == 2222) {
		alert('您没有权限！');
	} else if (xhr.status == 404) {
		$.alert('warning', '您访问的资源' + options.url + '不存在！');
	} else if (xhr.status == 3333) {
		R.alert({
			msg : '业务接口请求异常！'
		});
	} else if (xhr.status == 5555) {
		$.alert({
			msg : '业务接口连接异常！'
		});
	} else if (xhr.status == 0) {
		$.alert('warning', '服务已停止');
	} else {
		$.alert('success', xhr.responseText);
	}
});

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
		width : 100,
		trigger : 'click'
	};
	$.fn.SideDown.methods = {
		init : function(options) {
			var opts = $.extend({}, $.fn.SideDown.defaults, options);
			return this.each(function() {
				var me = $(this);
				var dt = $('dt', this);
				var dd = $('dd', this).width(opts.width);
				var rdm = new Date().getTime() + '_'
						+ Math.floor(Math.random() * (100 - 1 + 1) + 1);
				dt.attr('id', 'dt_' + rdm);
				var items = opts.items;
				var url = opts.url;
				if (items) {
					var html = [];
					for ( var k in items) {
						html.push("<a href='javascript:'");
						if (items[k] === '-') {
							html.push(" class='divider'>");
						} else {
							html.push(">");
							var icon = items[k].icon || '';
							var text = items[k].text || '';
							html.push("<i class='", icon, "'></i>");
							html.push("<span style='padding-left:8px;'>", text,
									"</span>");
						}
						html.push("</a>");
					}
					dd.html(html.join(''));
					var as = $('a', dd);
					dd.delegate('a', 'click', function(e) {
						var idx = as.index(this);
						var action = items[idx].action;
						if (action) {
							action.call();
						}
						dd.hide();
					});
				} else if (url) {
					dd.load(url);
				}
				if (opts.trigger == 'click') {
					me.click(function(e) {
						dd.show();
					});
					$(document).bind("click", function(e) {
						var target = $(e.target);
						if (target.closest('#dt_' + rdm).length == 0) {
							dd.hide();
						} else {
							dd.show();
						}
					});
				}
				if (opts.trigger == 'hover') {
					me.hover(function(e) {
						dd.show();
					}, function(e) {
						dd.hide();
					});
				}
			});
		}
	};
})(jQuery);

(function($) {
	$.fn.MultiRadio = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this,
					Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return arguments.callee.methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist');
		}
	};
	$.fn.MultiRadio.defaults = {
		active : 0,
		items : [],
		select : function(index, me) {
		}
	};
	$.fn.MultiRadio.methods = {
		init : function(options) {
			var opts = $.extend({}, $.fn.MultiRadio.defaults, options);
			return this.each(function() {
				var me = $(this);
				var name = me.attr('id');
				me.append("<input type='hidden' name='" + name
						+ "' value='0'/>");
				me.data('options', opts);
				var radioes = $("a", me);
				me.MultiRadio('active', opts.active);
				radioes.click(function() {
					var index = radioes.index(this);
					me.MultiRadio('active', index, opts.select);
				});
			});
		},
		active : function(index, callback) {
			var radioes = $("a", this);
			var valctr = $("input[type=hidden]", this);
			radioes.removeClass("active").eq(index).addClass("active");
			valctr.val(radioes.eq(index).attr('v'));
			callback && callback.call(this, index, this);
		}
	};
})(jQuery);


(function($) {
	$.fn.Tabs = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this,
					Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return arguments.callee.methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist');
		}
	};
	$.fn.Tabs.defaults = {
		active : 0,
		tabs : [],
		select : function(index, me) {
		}
	};
	$.fn.Tabs.methods = {
		init : function(options) {
			var opts = $.extend({}, $.fn.Tabs.defaults, options);
			return this.each(function() {
				var me = $(this);
				me.data('options', opts);
				var tabs = $("dt a", me);
				var panels = $("dd", me);
				me.Tabs('active', opts.active);
				tabs.click(function() {
					var index = tabs.index(this);
					me.Tabs('active', index, opts.select);
				});
			});
		},
		add : function(tab) {
			return this
					.each(function() {
						var el = $(this);
						var opts = el.data('options');
						opts.tabs.push(tab);
						el.data('options', opts);
						var url = tab.url;
						$('li.active', el).removeClass('active');
						var html = new Array();
						html.push("<li class='active' url='" + url + "'>");
						html.push(tab.title);
						if (tab.closable) {
							html
									.push("<button class='btn btn-icon'><i class='icon-remove-sign'></i></button>");
						}
						html.push("</li>");
						$('.tabs-title', el).append(html.join(''));
						if (opts.ajax) {
							$('.tabs-content', el).load(
									url + '?_=' + new Date().getTime());
						} else {
							$('#main').attr('src',
									url + '?_=' + new Date().getTime());
						}
					});
		},
		active : function(index, callback) {
			var tabs = $("dt a", this);
			var panels = $("dd", this);
			tabs.removeClass("active").eq(index).addClass("active");
			panels.removeClass("active").eq(index).addClass("active");
			callback && callback.call(this, index, this);
		},
		exists : function(title) {
			var ret = false;
			this.each(function() {
				var opts = $(this).data('options');
				$(opts.tabs).each(function() {
					if (this.title == title) {
						ret = true;
						return;
					}
				});
			});
			return ret;
		},
		tabs : function() {
			var ret = [];
			this.each(function() {
				ret = $(this).data('options').tabs;
			});
			return ret;
		},
		getTabIndex : function(tab) {
			var idx = -1;
			this.each(function() {
				var opts = $(this).data('options');
				$(opts.tabs).each(function(i) {
					if (this.title == tab.title) {
						idx = i;
						return;
					}
				});
			});
			return idx;
		}
	};
})(jQuery);


(function($) {
	function grep(q, data, filter) {
		var ret = new Array();
		$(data).each(function(i) {
			if (filter(q, this, i)) {
				ret.push(this);
			}
		});
		return ret;
	}
	$.fn.AutoComplete2 = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this,
					Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return arguments.callee.methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist');
		}
	};
	$.fn.AutoComplete2.defaults = {
		data : [],
		url : null,
		textField : 'text',
		valueField : 'value',
		filter : function(q, item, idx) {
			return item['text'].indexOf(q) != -1;
		},
		select : function(item) {
		}
	};
	$.fn.AutoComplete2.methods = {
		init : function(options) {
			var settings = $.extend({}, $.fn.AutoComplete2.defaults, options);
			return this.each(function() {
				var me = $(this).hide();
				if (this.tagName != 'INPUT') {
					return;
				}
				$(this).data('options', settings);
				var disabled = me.attr('disabled');
				var rdm = new Date().getTime() + '_'
						+ Math.floor(Math.random() * (100 - 1 + 1) + 1);
				var dl = $("<dl class='select'><dt id='dt_" + rdm
						+ "'></dt><dd></dd><div class='mask'></div></dl>");
				var p1 = [];
				p1.push("<input type='text' autocomplete='off'");
				if(settings.width){
					p1.push(" style='width:50px;'");
				}
				p1.push("/>");
				p1.push("<i class='icon-angle-down'></i>");
				me.wrap(dl).after(p1.join(''));
				var dt = me.parent("dt");
				var H = dt.outerWidth();
				var dd = dt.siblings("dd").width(H - 2);
				var mask = dt.siblings("div.mask");
				if (disabled) {
					mask.show();
				}
				var txt = me.siblings("input");
				var url = settings.url;
				var data = settings.data;
				if (url) {
					$.ajax({
						url : url,
						cache : false,
						async : false,
						dataType : 'json',
						success : function(result) {
							data = result;
						}
					});
				}
				$(this).data('list', data);
				dd.click(function(e) {
					var t = e.target;
					if (t.tagName == 'A') {
						txt.val($(t).text());
						me.val($(t).attr('v'));
						$(this).hide();
						settings.select({
							text : $(t).text(),
							value : $(t).attr('v')
						});
					}
					return false;
				});
				dt.bind("click", function(e) {
					var all = me.data('list');
					loadItems(all);
					dd.show();
					return false;
				});
				$(document).bind("click", function(e) {
					var target = $(e.target);
					if (target.closest('#dt_' + rdm).length == 0) {
						dd.hide();
					}
				});
				var lis = $('a', dd);
				var num = lis.size();
				var H = lis.eq(0).outerHeight();
				var _idx = 0;

				txt.keyup(function(e) {
					if (e.which == 37 || e.which == 38 || e.which == 39
							|| e.which == 13 || e.which == 40) {
						return false;
					}
					setTimeout(function() {
						var all = me.data('list');
						var data = all;
						var k = $.trim(txt.val());
						if (k.length > 0) {
							data = grep(k, all, settings.filter);
						}
						loadItems(data);
					}, 10);
				});

				function loadItems(data) {
					var dh = [];
					var span = $('<span>');
					$(data).each(
							function(i) {
								var txt = this[settings.textField];
								txt = span.text(txt).html();
								var a = "<a href='javascript:;' v='"
										+ this[settings.valueField] + "' _idx="
										+ i + ">" + txt + "</a>";
								dh.push(a);
							});
					span.remove();
					dd.empty().append(dh.join(''));
				}

				txt.bind('keydown', function(e) {
					var keyCode = e.keyCode ? e.keyCode : e.which ? e.which
							: e.charCode;
					if (keyCode == 40) {
						var selected = $('li.hovers', dd);
						if (selected.size() > 0) {
							var next = selected.next('li');
							var _idx = selected.attr('_idx');
							selected.removeClass('hovers');
							if (next.size() == 0) {
								next = $('li:first', dd);
								_idx = 0;
								dd.scrollTop(0);
							}
							if (_idx * H >= (270 + dd.scrollTop())) {
								dd.scrollTop(dd.scrollTop() + H);
							}
						} else {
							next = $('li:first', dd);
							dd.scrollTop(0);
						}
						next.addClass('hovers');
						$(this).val(next.text());
						val.val(next.attr('v'));
						$(this).focus();
					}
					if (keyCode == 38) {
						var selected = $('li.hovers', dd);
						if (selected.size() > 0) {
							var prev = selected.prev('li');
							var _idx = selected.attr('_idx');
							selected.removeClass('hovers');
							if (prev.size() == 0) {
								prev = $('li:last', dd);
								_idx = num - 1;
								dd.scrollTop(dd[0].scrollHeight);
							}
							if ((_idx - 1) * H < dd.scrollTop()) {
								dd.scrollTop(dd.scrollTop() - H);
							}
						} else {
							prev = $('li:last', dd);
							dd.scrollTop(dd[0].scrollHeight);
						}
						prev.addClass('hovers');
						$(this).val(prev.text());
						val.val(prev.attr('v'));
						$(this).focus();
					}
					if (keyCode == 13) {
						dd.hide();
						return false;
					}
				});

			});
		},
		reset : function() {
			return this.each(function() {
				var opts = $(this).data('options');
				var valueel = $("input[name=" + opts.hideName + "]");
				var data = $(this).data('list');
				$(this).val(data[0].text);
				valueel.val(data[0].value);
			});
		},
		disabled : function() {
			return this.each(function() {
				$(this).attr('disabled', 'disabled');
				$(this).siblings('div.combox-trigger').unbind('click');
				$(this).parent().unbind('click');
			});
		},
		reload : function(data) {
			return this.each(function() {
				var opts = $(this).data('options');
				var valueel = $("input[name=" + opts.hideName + "]");
				opts.data = data;
				$(this).data('options', opts);
				$(this).data('list', data);
				$(this).val('');
				valueel.val('');
				if (data.length > 0) {
					$(this).val(data[0].text);
					valueel.val(data[0].value);
				}
			});
		},
		val : function(val) {
			if (val) {
				var data = this.data('list');
				this.val(val);
				var txt = this.siblings("input");
				$(data).each(function(i) {
					var t = this.text;
					var v = this.value;
					if (v == val) {
						txt.val(t);
						return;
					}
				});
			} else {
				return this.val();
			}
		}
	};
})(jQuery);
String.prototype.contains = function(arr) {
	for (var k = 0, len = arr.length; k < len; k++) {
		if (this.indexOf(arr[k]) == -1) {
			return false;
		}
	}
	return true;
};