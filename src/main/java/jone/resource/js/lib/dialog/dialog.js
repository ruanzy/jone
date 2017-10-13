(function($){  
	var defaults = {
		//title: '假设及贷款',
		//width: 300,
		content: '<div style="font-size: 13px; color: #777;">数据正在加载中,请稍后...</div>',
		icon : '',
		padding : 20,
		closable: true,
		open: $.noop,
		beforeClose: $.noop,
		afterClose: $.noop
	};
	$.dialog = function(opts) {
			var options = $.extend({}, defaults, opts);
		
				var uuid = 'rzy_' + new Date().getTime().toString(36);
				var wrapper = $('<div id="' + uuid + '" class="rui-dialog" tabIndex="0">').appendTo('body');
				wrapper.data('options', options);
				wrapper.close = function(){
					wrapper.remove();
					overlay.remove();
					dialogs().pop();
				};
				wrapper.title = function(t){
					if(t){
						this.find('.rui-dialog-title-content').text(t);
					}
					return this.find('.rui-dialog-title-content').text();
				};
				//wrapper.addClass('animated bounceIn'); 
				var uibody = $('<div class="rui-dialog-body">');
				var content = $('<div class="rui-dialog-content">');
				if(options.padding){
					content.css('padding', options.padding);
				}
				uibody.css('height', options.height);
				content.html(options.content);
				content.appendTo(uibody);
				uibody.appendTo(wrapper);
				//创建titlebar
				if(options.title){
					var _titlebar = $('<div class="rui-dialog-titlebar">');
					var _title = $('<span class="rui-dialog-title"><i class="fa ' + options.icon  + '"></i> <span class="rui-dialog-title-content">' + options.title + '</span></span>');
					if(options.closable){
						var _close = $('<a class="close rui-dialog-titlebar-close" type="button">&times;</a>');
						_close.on('click', function(){
							wrapper.close();
						});
						_close.prependTo(_titlebar);
					}
					_title.appendTo(_titlebar);
					_titlebar.prependTo(wrapper);
				}
				//创建buttons
				if(options.buttons){
					var buttons = options.buttons;
					var buttonPane = $('<div class="rui-dialog-buttonpane rui-helper-clearfix">');
					var buttonSet = $('<div class="rui-dialog-buttonset">');
					if(buttons){
						$.each(buttons, function(){
							var text = this.text;
							var cls = this.cls || 'btn-default';
							var id = this.id;
							var click = this.click;
							var arr = new Array();
							arr.push('<a');
							if(id){
								arr.push(' id="', id, '"');
							}
							arr.push(' class="btn ', cls, '">', text, '</a>');
							var btn = $(arr.join('')).on('click', function(){
								click.call(wrapper, arguments);
							});
							buttonSet.append(btn);
						});
						buttonSet.appendTo(buttonPane);
						buttonPane.appendTo(wrapper);
					}
				}
				var overlay = $('body').find('.rui-widget-overlay');
				if(overlay.length == 0){
					overlay = $('<div class="rui-widget-overlay">').appendTo('body');
				}
				dialogs().push(uuid);
				//me.dialog('size');
				wrapper.css({
					width: options.width
				});
				position(wrapper, options);
				wrapper.fadeIn('fast');
				options.open&&options.open.call();
				//wrapper.resizable();
				wrapper.draggable({ containment: "document", handle: ".rui-dialog-titlebar",cursor: "move" });
				wrapper.disableSelection();
				return wrapper;
			};
			
	$.alert = function(msg, callback){
		return $.dialog({
			title: '提示',
			width: 350,
			content: msg,
			buttons: [
				{
					text: '确定',
					cls: 'btn-primary',
					click: function(){
						this.close();
						callback && callback.call(this);
					}
				}
			]
		});
	};
	$.confirm = function(msg, callback){
		return $.dialog({
			title: '确认',
			width: 350,
			content: msg,
			buttons: [
				{
					text: '确定',
					cls: 'btn-primary',
					click: function(){
						this.close();
						callback && callback.call(this, true);
					}
				},
				{
					text: '取消',
					cls: 'btn-default',
					click: function(){
						this.close();
						callback && callback.call(this, false);
					}
				}
			]
		});
	};	
		function position(wrapper, options){
			var overlay = $('body').find('.rui-widget-overlay');
			var zIndex = 2000 + dialogs().length * 2;
			var W = document.documentElement.clientWidth;
			var H = document.documentElement.clientHeight;
			var w = wrapper.outerWidth();
			var h = wrapper.outerHeight();
			var top = Math.max(0, (H - h)/2);
			var left = Math.max(0, (W - w)/2);
			wrapper.css({
				zIndex : zIndex,
				top: top,
				left: left
			});
			var content = wrapper.find('.rui-dialog-content');
			var H = $(document).height();
			var th =  wrapper.find('.rui-dialog-titlebar').outerHeight();
			var height = options.height;
			var maxHeight = H-th;
			if(height){
				//content.css('height', height);
			}
			//content.css('maxHeight', maxHeight);
			if(dialogs().length == 1){
				overlay.css('zIndex', zIndex - 1);
			}
		}
		function options(k){
			var wrapper = this.dialog('wrapper');
			return wrapper.data('options')[k];
		}
		function close(destroy){
			var wrapper = this.dialog('wrapper');
			var overlay = $('body').find('.rui-widget-overlay');
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

	function dialogs(){
		var dialogs = $(document).data("rui-dialog-instances");
		if ( !dialogs ) {
			dialogs = [];
			$(document).data("rui-dialog-instances", dialogs);
		}
		return dialogs;
	}
})(jQuery);