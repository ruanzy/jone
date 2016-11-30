(function($){  
	//var util = require('../util');
    $.fn.dialog = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
	$.fn.dialog.defaults = {
		title: null,
		width: 'auto',
		icon : 'fa-table',
		closable: false,
		open: $.noop,
		beforeClose: $.noop,
		afterClose: $.noop
	};
	$.fn.dialog.methods = {
		init: function(options) {
			var options = $.extend({}, $.fn.dialog.defaults, options);
			return this.each(function(){
				var me = $(this);
				var uuid = 'rzy_' + new Date().getTime().toString(36);
				var wrapper = $('<div id="' + uuid + '" class="ui-dialog" tabIndex="0">').appendTo('body');
				wrapper.data('options', options);
				//wrapper.addClass('animated bounceIn'); 
				var content = $('<div class="ui-dialog-content">');
				if(options.padding){
					content.css('padding', options.padding);
				}
				content.append(me.show());
				content.appendTo(wrapper);
				//创建titlebar
				if(options.title){
					var _titlebar = $('<div class="ui-dialog-titlebar">');
					var _title = $('<span class="ui-dialog-title"><i class="fa ' + options.icon  + '"></i> ' + options.title + '</span>');
					if(options.closable){
						var _close = $('<a class="close ui-dialog-titlebar-close" type="button"><span>×</span></a>');
						_close.on('click', function(){
							me.dialog('close');
						});
						_close.prependTo(_titlebar);
					}
					_title.appendTo(_titlebar);
					_titlebar.prependTo(wrapper);
				}
				//创建buttons
				if(options.buttons){
					var buttons = options.buttons;
					var buttonPane = $('<div class="ui-dialog-buttonpane ui-helper-clearfix">');
					var buttonSet = $('<div class="ui-dialog-buttonset">');
					if(buttons){
						$.each(buttons, function(){
							var text = this.text;
							var cls = this.cls;
							var id = this.id;
							var action = this.action;
							var arr = [];
							arr.push('<a');
							if(id){
								arr.push(' id="', id, '"');
							}
							arr.push(' class="btn ', cls, '">', text, '</a>');
							var btn = $(arr.join('')).on('click', function(){
								action.call(me, arguments);
							});
							buttonSet.append(btn);
						});
						buttonSet.appendTo(buttonPane);
						buttonPane.appendTo(wrapper);
					}
				}
				var overlay = $('body').find('.ui-widget-overlay');
				if(overlay.length == 0){
					overlay = $('<div class="ui-widget-overlay">').appendTo('body');
				}
				me.dialog('size');
				me.dialog('position', options.pos);
				wrapper.fadeIn('fast');
				options.open&&options.open.call(me);
				dialogs().push(uuid);
			});
		},
		wrapper: function(){
			return this.parent().parent();
		},
		size: function(){
			var wrapper = this.dialog('wrapper');
			var width = this.dialog('options', 'width');
			wrapper.width(width);
		},
		position: function(pos){
			var wrapper = this.dialog('wrapper');
			var overlay = $('body').find('.ui-widget-overlay');
			var zIndex = 2000 + dialogs().length * 2;
			var W = $(document).width();
			var H = $(document).height();
			var w = wrapper.outerWidth();
			var h = wrapper.outerHeight();
			var top = Math.max(0, (H - h)/2);
			var left = Math.max(0, (W - w)/2);
			if(pos){
				if(pos == 'rl'){
					top = 60;
					left = W - w - 50;
				}
			}
			wrapper.css({
				zIndex : zIndex,
				top: top,
				left: left
			});
			var content = wrapper.find('.ui-dialog-content');
			var H = $(document).height();
			var th =  wrapper.find('.ui-dialog-titlebar').outerHeight();
			var height = this.dialog('options', 'height');
			var maxHeight = H-th;
			if(height){
				maxHeight = Math.min(H, height)-th;
			}
			content.css('maxHeight', maxHeight);
			overlay.css('zIndex', zIndex - 1);
		},
		options: function(k){
			var wrapper = this.dialog('wrapper');
			return wrapper.data('options')[k];
		},
		close: function(destroy){
			var wrapper = this.dialog('wrapper');
			var overlay = $('body').find('.ui-widget-overlay');
			var beforeClose = this.dialog('options', 'beforeClose');
			var afterClose = this.dialog('options', 'afterClose');
			var static = this.dialog('options', 'static');
			if(beforeClose){
				beforeClose.call(this);
			}
			if(static){
				wrapper.remove();
			}else{
				this.hide().appendTo('body');
				wrapper.remove();
			}
			dialogs().pop();
			if(dialogs().length == 0){ 
				overlay.remove(); 
			}
			if(afterClose){
				afterClose.call(this);
			}
		}
	};
	$.dialog = function(url, options){
		var div = $('<div style="padding:0px;">');
		options.static = true;
		util.render(url, div, options.data);
		div.dialog(options);
	};
	$.alert = function(msg, callback){
		var div = $('<div style="padding:20px;">').text(msg);
		div.dialog({
			title: '提示',
			width: 350,
			static: true,
			buttons: [
				{
					text: '确定',
					cls: 'btn-primary',
					action: function(){
						this.dialog('close');
						callback();
					}
				}
			]
		});
	};
	$.confirm = function(msg, yes, no){
		var div = $('<div style="padding:20px;">').text(msg);
		div.dialog({
			title: '确认',
			width: 350,
			static: true,
			buttons: [
				{
					text: '确定',
					cls: 'btn-primary',
					action: function(){
						this.dialog('close');
						yes&&yes();
					}
				},
				{
					text: '取消',
					cls: 'btn-default',
					action: function(){
						this.dialog('close');
						no&&no();
					}
				}
			]
		});
	};
	$.noty = function(msg){
		var arr = [];
		arr.push('<div style="color:#3c763d;padding:15px 20px;border:1px solid #3c763d;border-radius: 4px;">');
		//arr.push('<div style="font-size: 15px;font-weight:700;">成功</div>');
		arr.push('<i class="fa fa-check fa-lg"></i> ');
		arr.push('<span style="font-size: 14px;font-weight:600;">');
		arr.push(msg);
		arr.push('</span> ');
		arr.push('</div> ');
		var msg = $(arr.join(''));
		msg.dialog({
			static: true,
			//pos: 'rl',
			open: function(){
				var self = this;
				var wrapper = self.dialog('wrapper').css('backgroundColor', '#dff0d8').css('boxShadow', 'none');
				setTimeout(function(){
					self.fadeOut('fast', function(){
						self.dialog('close');
					});
				}, 1000);
			}
		});
	};
	$.tip = function(msg){
		var arr = [];
		arr.push('<div class="tip">');
		arr.push('<div class="tipinner">');
		arr.push('我是另外一个tips，只不过我长得跟之前那位稍有些不一样。吸附元素选择器');
		arr.push('<i class="layui-layer-TipsG layui-layer-TipsB" style="border-right-color: rgb(53, 149, 204);"></i>');
		arr.push('</div>');
		arr.push('</div>');
		var msg = $(arr.join(''));
		msg.dialog({
			static: true,
			pos: 'rl',
			open: function(){
				var self = this;
				setTimeout(function(){
					self.fadeOut('fast', function(){
						self.dialog('close');
					});
				}, 2000);
			}
		});
	};
	function dialogs(){
		var dialogs = $(document).data("ui-dialog-instances");
		if ( !dialogs ) {
			dialogs = [];
			$(document).data("ui-dialog-instances", dialogs);
		}
		return dialogs;
	}
})(jQuery);