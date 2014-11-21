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
		mask.click(function(){
			alert.close();
		});
		alert.close = function() {
			alert.hide().empty().remove();
			$.alert.opened = false;
			if(callback){
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
		confirm.append(html.join('')).append("<div class='message-mask'></div>");
		var mask = $(".message-mask", confirm);
		mask.click(function(){
			confirm.hide().empty().remove();
			$.confirm.opened = false;
			if(callback){
				callback('CLOSE');
			}
		});
		$(".message-footer").delegate('a', 'click', function(e) {
			var btn = $(this).text();
			confirm.hide().empty().remove();
			$.confirm.opened = false;
			if(callback){
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
		if (!$.dialog.opened) {
			$.dialog.opened = true;
		} else {
			return _dialog;
		}
		options = $.extend({}, $.dialog.defaults, options);
		var dialog = $('div.dialog-wrap');
		if (dialog.size() == 0) {
			dialog = $("<div class='dialog-wrap'></div>").appendTo($('body'));
		}
		var html = [];
		html.push("<div class='dialog' style='width:" + options.width + "px'>");
		html.push("<div class='dialog-header dialog-header-success'>");
		html.push("<a class='dialog-close'><i class='icon-remove'></i></a>");
		html.push("<i class='icon-desktop'></i> ");
		html.push(options.title);
		html.push("</div>");
		html.push("<div class='dialog-body'><div class='dialog-loading'><i class='icon-spinner icon-spin'></i>loading...</div></div>");
		if(options.buttons){
			html.push("<div class='dialog-footer align-rignt'>");
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
		dialog.append(html.join('')).append("<div class='dialog-mask'></div>");
		var mask = $(".dialog-mask", dialog);
		mask.click(function(){
			dialog.close();
		});
		$(".dialog", dialog).css({"margin-top": 45});
		var bd = $(".dialog-body", dialog).css({padding: options.padding});
		if (options.content) {
			bd.html(options.content);
		}
		if (options.url) {
			bd.load(options.url, options.params);
		}
		$(".dialog-close", dialog).click(function(e) {
			dialog.close();
		});
		var btns = $(".dialog-footer a");
		$(".dialog-footer").delegate('a', 'click', function(e) {
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
		dialog.show(options.onShow());
		_dialog = dialog;
		return _dialog;
	};
	$.dialog.defaults = {
		title : 'Window',
		width : 350,
		height : 100,
		padding : 20,
		content : '',
		params : null,
		onShow : function() {
		}
	};
})(jQuery);

(function($) {
	$.pop = function(msg, callback) {
		var pop = $('div.message-pop').empty();
		if (pop.size() == 0) {
			pop = $("<div class='message-pop'></div>").appendTo($('body'));
		}
		pop.append("<i class='icon-ok'></i> " + msg).slideDown().slideUp(800, function(){
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