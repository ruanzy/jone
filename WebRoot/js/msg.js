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
		alert.append(html.join('')).show();
		$('.message-footer .btn', alert).click(function() {
			alert.hide().remove();
			$.alert.opened = false;
			if(callback){
				callback();
			}
		});
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
		html.push("<a class='btn btn-success'>OK</a>");
		html.push("<a class='btn btn-default'>NO</a>");
		html.push("</div>");
		confirm.append(html.join('')).show();
		$('.message-footer', confirm).click(function(e) {
			var btn = false;
			var t = $(e.target);
			if (t.hasClass('btn-success')) {
				btn = true;
			}
			confirm.hide().remove();
			$.confirm.opened = false;
			callback(btn);
		});
		_confirm = confirm;
		return _confirm;
	}
})(jQuery);

(function($) {
	var _dialog = false;
	$.dialog = function(options) {
		if (!$.dialog.opened) {
			$.dialog.opened = true;
		} else {
			return _dialog;
		}
		var defaults = {
			title : 'Window',
			width : 350,
			height : 100,
			padding : '20px',
			content : '',
			params : null,
			onShow : function() {
			}
		};
		options = $.extend({}, defaults, options);
		var mask = $('div.mask');
		if (mask.size() == 0) {
			mask = $("<div class='mask'></div>").appendTo($('body'));
		}
		mask.click(function(){
			dialog.close();
		});
		var dialog = $('div.message-dialog-window');
		if (dialog.size() == 0) {
			dialog = $("<div class='message-dialog-window' style='width:"
					+ options.width + "px'></div>").appendTo($('body'));
		}
		var html = [];
		html.push("<div class='message-header message-header-success'>");
		html.push("<a class='message-close'><i class='icon-remove'></i></a>");
		html.push("<i class='icon-desktop'></i> ");
		html.push(options.title);
		html.push("</div>");
		html.push("<div class='message-body'></div>");
		if(options.buttons){
			html.push("<div class='message-footer-bg align-rignt'>");
			$(options.buttons).each(function(){
				var txt = this.text;
				var icon = this.icon;
				var cls = this.cls;
				html.push("<a class='btn " + cls + "'>");
				if(icon){
					html.push("<i class='icon-" + icon + "'></i> ");
				}
				html.push(txt + "</a>");
			});
			html.push("</div>");
		}
		dialog.append(html.join(''));
		var bd = $(".message-body", dialog).css({padding: options.padding});
		if (options.content) {
			bd.html(options.content);
		}
		if (options.url) {
			bd.load(options.url, options.params);
		}
		$(".message-close", dialog).click(function(e) {
			dialog.close();
		});
		$(".message-footer-bg", dialog).click(function(e) {
			var t = e.target;
			var btns = $('a', this);
			if(t.tagName == 'A'){
				var index = btns.index(t);
				options.buttons[index].action(dialog);
			}
		});
		dialog.close = function() {
			mask.hide();//.remove();
			dialog.hide().empty().remove();
			$.dialog.opened = false;
		};
		dialog.text = function(content) {
			$(".message-body", dialog).html(content);
		};
		mask.show();
		dialog.show(options.onShow());
		_dialog = dialog;
		return _dialog;
	};
})(jQuery);

(function($) {
	$.pop = function(msg, callback) {
		var pop = $('div.message-pop').empty();
		if (pop.size() == 0) {
			pop = $("<div class='message-pop'></div>").appendTo($('body'));
		}
		pop.append("<i class='icon-ok'></i> " + msg).show().delay(1000).hide(0, function(){
			if(callback){
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
		$.alert('success', '您访问的资源不存在！');
	} else if (xhr.status == 3333) {
		R.alert({
			msg : '业务接口请求异常！'
		});
	} else if (xhr.status == 5555) {
		$.alert({
			msg : '业务接口连接异常！'
		});
	} else {
		$.alert('success', xhr.responseText);
	}
});
$.extend({
	close : function(cfg) {
		top.$.close(cfg);
	}
});
function dic(type) {
	var ret = null;
	$.ajax({
		url : 'common/dic',
		type : 'post',
		async : false,
		data : {
			"type" : type
		},
		dataType : 'json',
		success : function(data) {
			ret = data;
		}
	});
	return ret;
}