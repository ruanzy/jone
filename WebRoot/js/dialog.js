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
			padding : 20,
			content : '',
			params : null,
			onShow : function() {
			}
		};
		options = $.extend({}, defaults, options);
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
})(jQuery);