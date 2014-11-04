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
			width : 380,
			height : 100,
			content : '',
			onShow : function(){}
		};
		options = $.extend({}, defaults, options);
		var dialog = $('div.box');
		if (dialog.size() == 0) {
			dialog = $('<div/>').addClass('box').appendTo($('body'));
		}
		var html = [];
		html.push("<div class='box_head'><div class='box_title'><i class='icon-desktop'></i> ");
		html.push(options.title);
		html.push("</div>");
		html.push("<a class='box_min'>─</a><a class='box_max'>□</a><a class='box_close'>×</a>");
		html.push("</div>");
		html.push("<div class='box_body' style='height:", options.height);
		html.push("px;width:", options.width, "px;'></div>");
		html.push("<div class='box_foot'>");
		html.push("<div class='box_buttons'>");
		html.push("<button class='box_button_ok'><i class='icon-ok'></i> 保存</button>");
		html.push("<button class='box_button_cancel'><i class='icon-remove'></i> 取消</button>");
		html.push("</div>");
		html.push("</div>");
		dialog.append(html.join('')).show();
		var bd = $(".box_body", dialog).load(options.url, options.onShow);
		var head = $(".box_head", dialog).click(function(e) {
			e.preventDefault();
			e.stopPropagation();
			var target = $(e.target);
			if (target.hasClass('box_min')) {
				$.tip({
					msg : '我还没实现呢'
				});
			}
			if (target.hasClass('box_max')) {
				var ww = $(window).width() + $(window).scrollLeft();
				var wh = $(window).height() + $(window).scrollTop();
			}
			if (target.hasClass('box_close')) {
				close();
			}
		});
		function close() {
			dialog.hide().empty().remove();
			$.dialog.opened = false;
		}
		$(".box_button_ok", dialog).click(function() {
			var ret = options.ok(bd);
			if(ret){
				close();
			}
		});
		$(".box_button_cancel", dialog).click(function() {
			close();
		});
		
		var _x = 0, _y = 0;
		var ww = $(window).width() + $(window).scrollLeft();
		var wh = $(window).height() + $(window).scrollTop();
		var w = dialog.outerWidth();
		var h = dialog.outerHeight();
		var flag = false;
		head.bind("mousedown", function(e) {
			flag = true;
			if (window.getSelection()) {
				window.getSelection().removeAllRanges();
			}else{
				try {
					document.selection.empty();
				} catch (e) {};
			}
			_x = e.pageX - dialog.offset().left;
			_y = e.pageY - dialog.offset().top;
			$(document).bind('mousemove', move).bind('mouseup', up);
		});
		function move(e){
			var x = e.pageX - _x;
			var y = e.pageY - _y;
			x = (x <= 0) ? 0 : x;
			x = (x >= ww - w) ? (ww - w) : x;
			y = (y <= 0) ? 0 : y;
			y = (y >= wh - h) ? (wh - h) : y;
			dialog.css({
				top : y,
				left : x
			});
		}
		function up(){
			flag = false;
			$(document).unbind('mousemove', move).unbind('mouseup', up);
		}
		_dialog = dialog;
		resize();
		function resize() {
			if (!$.dialog.opened) {
				return false;
			}
			var l = ($(window).width() - _dialog.outerWidth()) / 2 + $(window).scrollLeft();
			var t = ($(window).height() - _dialog.outerHeight()) / 2 + $(window).scrollTop();
			_dialog.css("top", t).css("left", l);
		}
		$(window).resize(resize);
		return _dialog;
	};
})(jQuery);

(function($) {
	var _alert = false;
	$.alert = function(options) {
		if (!$.alert.opened) {
			$.alert.opened = true;
		} else {
			return _alert;
		}
		var defaults = {
			title : '提示',
			width : 350,
			content : '',
			ok : close
		};
		options = $.extend({}, defaults, options);
		var alert = $('div.box');
		if (alert.size() == 0) {
			alert = $('<div/>').addClass('box').appendTo($('body'));
		}
		var html = [];
		html.push("<div class='box_head'><div class='box_title'><i class='icon-desktop'></i> ");
		html.push(options.title);
		html.push("</div>");
		html.push("<a class='box_close'>×</a>");
		html.push("</div>");
		html.push("<div class='box_body' style='height:", options.height);
		html.push("px;width:", options.width, "px;padding:20px;'>", options.content, "</div>");
		html.push("<div class='box_foot'>");
		html.push("<div class='box_buttons'>");
		html.push("<button class='box_button_ok'><i class='icon-ok'></i> 确定</button>");
		html.push("<button class='box_button_cancel'><i class='icon-remove'></i> 取消</button>");
		html.push("</div>");
		html.push("</div>");
		alert.append(html.join('')).show();
		var head = $(".box_head", alert).click(function(e) {
			e.preventDefault();
			e.stopPropagation();
			var target = $(e.target);
			if (target.hasClass('box_close')) {
				close();
			}
		});
		function close() {
			alert.hide().empty().remove();
			$.alert.opened = false;
		}
		$(".box_button_ok", alert).click(function() {
			options.ok();
			close();
		});
		$(".box_button_cancel", alert).click(function() {
			close();
		});
		
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
			}else{
				try {
					document.selection.empty();
				} catch (e) {};
			}
			_x = e.pageX - alert.offset().left;
			_y = e.pageY - alert.offset().top;
			$(document).bind('mousemove', move).bind('mouseup', up);
		});
		function move(e){
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
		function up(){
			flag = false;
			$(document).unbind('mousemove', move).unbind('mouseup', up);
		}
		_alert = alert;
		resize();
		function resize() {
			if (!$.confirm.opened) {
				return false;
			}
			var l = ($(window).width() - _alert.outerWidth()) / 2 + $(window).scrollLeft();
			var t = ($(window).height() - _alert.outerHeight()) / 2 + $(window).scrollTop();
			_alert.css("top", t).css("left", l);
		}
		$(window).resize(resize);
		return _alert;
	};
})(jQuery);

(function($) {
	var _confirm = false;
	$.confirm = function(options) {
		if (!$.confirm.opened) {
			$.confirm.opened = true;
		} else {
			return _confirm;
		}
		var defaults = {
			title : '确认',
			width : 350,
			content : '',
			ok : close
		};
		options = $.extend({}, defaults, options);
		var confirm = $('div.box');
		if (confirm.size() == 0) {
			confirm = $('<div/>').addClass('box').appendTo($('body'));
		}
		var html = [];
		html.push("<div class='box_head'><div class='box_title'><i class='icon-desktop'></i> ");
		html.push(options.title);
		html.push("</div>");
		html.push("<a class='box_close'>×</a>");
		html.push("</div>");
		html.push("<div class='box_body' style='height:", options.height);
		html.push("px;width:", options.width, "px;padding:20px;'>", options.content, "</div>");
		html.push("<div class='box_foot'>");
		html.push("<div class='box_buttons'>");
		html.push("<button class='box_button_ok'><i class='icon-ok'></i> 确定</button>");
		html.push("<button class='box_button_cancel'><i class='icon-remove'></i> 取消</button>");
		html.push("</div>");
		html.push("</div>");
		confirm.append(html.join('')).show();
		var head = $(".box_head", confirm).click(function(e) {
			e.preventDefault();
			e.stopPropagation();
			var target = $(e.target);
			if (target.hasClass('box_close')) {
				close();
			}
		});
		function close() {
			confirm.hide().empty().remove();
			$.confirm.opened = false;
		}
		$(".box_button_ok", confirm).click(function() {
			options.ok();
			close();
		});
		$(".box_button_cancel", confirm).click(function() {
			close();
		});
		
		var _x = 0, _y = 0;
		var ww = $(window).width() + $(window).scrollLeft();
		var wh = $(window).height() + $(window).scrollTop();
		var w = confirm.outerWidth();
		var h = confirm.outerHeight();
		var flag = false;
		head.bind("mousedown", function(e) {
			flag = true;
			if (window.getSelection()) {
				window.getSelection().removeAllRanges();
			}else{
				try {
					document.selection.empty();
				} catch (e) {};
			}
			_x = e.pageX - confirm.offset().left;
			_y = e.pageY - confirm.offset().top;
			$(document).bind('mousemove', move).bind('mouseup', up);
		});
		function move(e){
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
		function up(){
			flag = false;
			$(document).unbind('mousemove', move).unbind('mouseup', up);
		}
		_confirm = confirm;
		resize();
		function resize() {
			if (!$.confirm.opened) {
				return false;
			}
			var l = ($(window).width() - _confirm.outerWidth()) / 2 + $(window).scrollLeft();
			var t = ($(window).height() - _confirm.outerHeight()) / 2 + $(window).scrollTop();
			_confirm.css("top", t).css("left", l);
		}
		$(window).resize(resize);
		return _confirm;
	};
})(jQuery);

(function($) {
    var _tip = false;
    $.tip = function(cfg) {
    	if(!$.tip.opened) {
            $.tip.opened = true;
        } else {
            return _tip;
        }
        var defaults = {
            content: '',
            width: 400,
            height: 100,
			draggable: false
        };
        cfg = $.extend({}, defaults, cfg);       
		var mask = $('<div/>').addClass('mask');
		var wrap_out = $('<div/>').addClass('box');
		var box = $('<div/>').addClass('tip tip-danger');
		
		
			$("<button/>").addClass("close").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				$.tip.close();
			}).html("<i class='icon-remove'></i>").appendTo(box);
			
		box.append("<i class='icon-warning-sign'></i> <strong>Warning</strong>&nbsp;");
		box.append(cfg.content);
		wrap_out.append(box);
		mask.appendTo($('body'));
		wrap_out.appendTo($('body'));
		wrap_out.click(function(e){
			e.stopPropagation();
		});	
        if(cfg.draggable) {
        	var z_idx = wrap_out.css('z-index');
        	var _x = 0;
        	var _y = 0;
        	var ww = $(window).width() +  $(window).scrollLeft();
        	var wh = $(window).height() +  $(window).scrollTop();
        	var w = wrap_out.outerWidth();
        	var h = wrap_out.outerHeight();
        	caption.bind("mousedown", function(e) {
                $.box.drag = true;
                caption.css('cursor', 'move');
                _x= e.pageX - wrap_out.offset().left;
                _y= e.pageY - wrap_out.offset().top;                
                wrap_out.css('z-index', 99999).bind("mousemove", function(e) {
                    if ($.box.drag) {
                        var x = e.pageX - _x;
                        var y = e.pageY - _y;
                        x = (x <= 0) ? 0 : x;
                        x = (x >= ww - w) ? (ww - w) : x;
                        y = (y <= 0) ? 0 : y;
                        y = (y >= wh - h) ? (wh - h) : y;                        
                        wrap_out.css({position:'absolute',top:y,left:x});
                    }
                    wrap_out.bind("mouseup", function() {
                        $(this).removeClass('draggable').css('z-index', z_idx);
                        $.tip.drag = false;
                        caption.css('cursor', 'default');
                    });
                });
                e.preventDefault();
            }).bind("mouseup", function() {
            	wrap_out.removeClass('draggable');
                $.tip.drag = false;
                caption.css('cursor', 'default');
            });
        }	
		_tip = wrap_out;
		resize();
		function resize(){
	    	if(!$.tip.opened) {
	            return false;
	        }
	        var l = ($(window).width() - _tip.outerWidth())/2 + $(window).scrollLeft();
	        var t = ($(window).height() - _tip.outerHeight())/2 + $(window).scrollTop();
	        _tip.css("top",  t).css("left", l);
	    }
		$(window).resize(resize);
		return _tip;
    };
    $.tip.close = function() {
        if(!$.tip.opened || _tip == undefined) {
            return false;
        }
        $.tip.opened = false;
        _tip.remove();
        var mask = $('div.mask');
        mask.fadeOut(function(){
            $(this).remove();
        });
    };
})(jQuery);

function permit(context){
	var opts = $(context).find('[funcid]');
	if(opts.size()>0){
		var powers = [];
		$.ajax({
			url:'common/op',
			cache: false,
			async:false,
			dataType:'json',
	        success:function(result){
				$.each(result,function(){
					powers.push(this.id);		
				});
			}
		});	
		opts.each(function(){
			var funcid = parseInt($(this).attr('funcid'));
			if($.inArray(funcid, powers)==-1){
				$(this).hide();
			}		
		});
	}
}

$(document).ajaxError(function(event, xhr, options, exc){
	alert(xhr.status);
	if(xhr.status==1111){
        $.alert({msg:'您的登录已过期,请重新登录！',fn:function(){
        	top.document.location = './';
        }});
    }else if(xhr.status==1112){
        $.alert({msg:'请求异常！'});
    }else if(xhr.status==2222){
        alert('您没有权限！');
    }else if(xhr.status==404){
        alert('您访问的资源不存在！');
    }else if(xhr.status==3333){
    	R.alert({msg:'业务接口请求异常！'});
    }else if(xhr.status==5555){
    	$.alert({msg:'业务接口连接异常！'});
    }else{
    	$.alert({msg:xhr.responseText});
    }
});
$.extend({
	close:function(cfg){
		top.$.close(cfg);
	}
});
function dic(type){
	var ret = null;
	$.ajax({
		url:'common/dic',
		type: 'post',
		async: false,
		data: {"type" : type},
		dataType: 'json',
        success: function(data){
        	ret = data;
		}
	});
	return ret;
}