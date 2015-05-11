(function($){
	$.box = function(params){
 		var defs = {
			title:'Window',
			content:'',
			padding : 20,
			width:350
		};
        var markup = ['<div class="boxwarp">', '<div class="box">'];
		if(params.title){
			markup.push('<p class="title">',params.title,'</p>');
		}
		if(params.closable){
			markup.push('<p class="closebtn">&times;</p>');
		}
		markup.push('<p class="content"></p>');
		if(params.buttons){							
			var buttonHTML = '';
			$.each(params.buttons,function(){	
				var cls = this['class']||'';
				buttonHTML += '<button class="button '+cls+'">'+this['text']+'<span></span></button>';
				if(!this.action){
					this.action = function(){};
				}
			});				
            markup.push('<p class="btns">',buttonHTML,'</p>');
		}
		markup.push('</div><div class="box-mask"></div></div>');
        var boxwarp = $(markup.join('')).appendTo('body');
        var mask = boxwarp.find('.dialog-mask');
        mask.css({backgroundColor: '#000', opacity: .1});
		var box = boxwarp.find('.box').css('zIndex', _nextZ());
		var title = box.find('.title');
		var closebtn = box.find('.closebtn');
		var content = box.find('.content');
		var btns = box.find('.btns');
		if(params.title){
			if(params.closable){
				closebtn.addClass('f_white');
			}
		}else{
			if(params.closable){
				content.css('margin-top', 32);
			}
		}
		if(params.padding === 0){
			content.css('padding', 0);
		}
		if(params.width){
			var wd = parseInt(params.width);
			var padding = content.css('padding');
			if(padding){
				wd = wd - 2*parseInt(padding);
			}
			content.width(wd);
		}
		var padding = 0;
		if(params.padding){
			padding = params.padding;
			wd = wd - 2*parseInt(params.padding);
		}
		content.css('padding', padding);
		if(params.content){
			var cnt = params.content;
			if(cnt.charAt(0) == '#'){
				content.append($(cnt).show());
			}else if(cnt.substr(0,4)=='ajax'){
				content.load(cnt.substring(5));
			}else if(cnt.substr(0,3)=='img'){
				var url = cnt.substring(4); 
				content.append("<img src='" + url + "'/>");
			}else{
				content.append(cnt);
			}
			var W = $(window).width();
			var H = $(window).height();
			var w = content.outerWidth();
			var h = content.outerHeight();
			var th = title.outerHeight();
			var bh = btns.outerHeight();
			box.css('top', '50%').css('left', '50%')
			box.css('margin-top', -(h + th + bh)/2).css('margin-left', -w/2);
			box.css("visibility","visible");
		}else if(params.url){
			content.load(params.url, function(){
				var W = $(window).width();
				var H = $(window).height();
				var w = box.outerWidth();
				var h = box.outerHeight();
				box.css('top', '50%').css('left', '50%')
				box.css('margin-top', -h/2).css('margin-left', -w/2);
				box.css("visibility","visible");
			});
		}
		if(params.buttons){
			var i = 0;
			$.each(params.buttons,function(){
				var action = this.action;
				btns.find('.button').eq(i++).click(function(){
					action.call(boxwarp, boxwarp);
					return false;
				});
			});
		}
		if(params.closable){
			closebtn.click(function(){
				boxwarp.close();
				return false;
			});
		}
		boxwarp.close = function(){
			boxwarp.hide(function(){	
				if(params.content){
					var cnt = params.content;
					if(cnt.charAt(0) == '#'){
						$(cnt).hide().appendTo('body');
					}
				}	
			}).empty().remove();
		};
		boxwarp.show();
		return boxwarp;
    }
	function _nextZ() {
        return $.box.zIndex++;
    }
	$.box.zIndex = 2015;	
	$.tip = function(msg, delay){
		var opts = {
			padding : 20,
			content:msg
		};
		var box = $.box(opts).addClass('tip');
		if(delay){
			var fnn = function(){
				box.close();
			}
			setTimeout(fnn, delay);
		};
    }	
	$.loadMask = function(msg, delay){
		var cnt = '<div class="loadmask-msg">' + msg + '</div>';
		var opts = {
			padding : 0,
			content:cnt
		};
		var box = $.box(opts).addClass('masked');
		if(delay){
			var fnn = function(){
				box.close();
			}
			setTimeout(fnn, delay);
		};
    }	
	$.alert = function(msg, fn){
		var opts = {
			title:'提示',
			closable:true,
			content:msg,
			padding : 20,
			width:350,
			buttons:[
				{
					text:'确定', 
					action:function(){
						this.close();
						fn&&fn();
					}
				}
			]
		
		};
		$.box(opts);
    }	
	$.confirm = function(msg, fn){
		var opts = {
			title:'确认',
			closable:true,
			content:msg,
			padding : 20,
			width:350,
			buttons:[
				{
					text:'确定', 
					action:function(){
						this.close();
						fn&&fn();
					}
				},
				{
					text:'取消', 
					action:function(){
						this.close();
					}
				}
			]
		
		};
		$.box(opts);
    }
	$.dialog = function(opts){
		var settings = {
			closable:true,
			padding : 20
		};
		$.extend(settings, opts);
		$.box(settings);
    }
})(jQuery);