(function($){        
	function grep(q, data, filter){
		var ret = new Array();
		$(data).each(function(i){
			if(filter(q, this, i)){
				ret.push(this);
			}
		});
		return ret;
	}
	$.fn.AutoComplete2 = function(method) {
		if (typeof method == 'string') {
            return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
    $.fn.AutoComplete2.defaults = {
		data: [],
		url: null,
		hideName: 'rzy',
		textField: 'text',
		valueField: 'value',
		filter: function(q, item, idx){
			return item['text'].indexOf(q) != -1;
		},
		select: function(item){}	
    };
	$.fn.AutoComplete2.methods = {
		init: function(options) {
			var settings = $.extend({}, $.fn.AutoComplete2.defaults, options);
			return this.each(function(){
				var me = $(this).hide();
				if(this.tagName != 'INPUT'){
					return;
				}
				$(this).data('options', settings);
				var disabled = me.attr('disabled');
				var rdm = new Date().getTime() + '_' + Math.floor(Math.random()*(100 - 1 + 1) + 1);
				var dl = $("<dl class='select'><dt id='dt_" + rdm + "'></dt><dd></dd><div class='disabled'></div></dl>");
				var p1 = [];
				p1.push("<input type='text' autocomplete='off'/>");
				p1.push("<i class='icon-angle-down'></i>");
				me.wrap(dl).after(p1.join(''));
				var dt = me.parent("dt");
				var H = dt.outerWidth();
				var dd = dt.siblings("dd").width(H - 2);
				var mask = dt.siblings("div.disabled");
				if(disabled){
					mask.show();
				}
				var txt = me.siblings("input");
				var url = settings.url;
				var data = settings.data;
				if(url){
					$.ajax({
						url:url,
						cache: false,
						async:false,
						dataType:'json',
				        success:function(result){
				        	data = result;
						}
					});
				}
				$(this).data('list', data);				
				dd.click(function(e){
					var t = e.target;
					if(t.tagName == 'A'){
						txt.val($(t).text());
						me.val($(t).attr('v'));
						$(this).hide();
						settings.select({text:$(t).text(), value:$(t).attr('v')});
					}
				});
				dt.bind("click",function(e){  
					loadItems();
					dd.show(); 
				});
				$(document).bind("click",function(e){ 
					var target = $(e.target); 
					if(target.closest('#dt_' + rdm).length == 0){ 
						dd.hide(); 
					}
				});
				var lis = $('a', dd);
				var num = lis.size();
				var H = lis.eq(0).outerHeight();
				var _idx = 0;
				
				txt.keyup(function(e){
					if(e.which==37||e.which==38||e.which==39||e.which==13||e.which==40){
						return false;
					}
					setTimeout(function(){
						loadItems();
					}, 10);
				});
				
				
				function loadItems(){
					var all = me.data('list');
					var data = all;
					var k = $.trim(txt.val());
					if(k.length>0){
						data = grep(k, all, settings.filter);
					}
					var dh = [];
					var span = $('<span>');
					$(data).each(function(i){
						var txt = this[settings.textField];
						txt = span.text(txt).html();
						var a = "<a href='javascript:;' v='" + this[settings.valueField] + "' _idx=" + i + ">" + txt + "</a>" ;						
						dh.push(a);
					});
					span.remove();
					dd.empty().append(dh.join(''));
				}
				
				txt.bind('keydown',function(e){
		    		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode; 
		    		if(keyCode == 40){
		    			var selected = $('li.hovers',dd);
		    			if(selected.size()>0){
							var next = selected.next('li');
							var _idx = selected.attr('_idx');
							selected.removeClass('hovers');
							if(next.size()==0){
								next = $('li:first',dd);
								_idx = 0;
								dd.scrollTop(0);
							}
							if(_idx*H>=(270 + dd.scrollTop())){
								dd.scrollTop(dd.scrollTop() + H);
							}
		    			}else{
		    				next = $('li:first',dd);
		    				dd.scrollTop(0);
						}
		    			next.addClass('hovers');
		    			$(this).val(next.text());
		    			val.val(next.attr('v'));
		    			$(this).focus();
		    		}
		    		if(keyCode == 38){
		    			var selected = $('li.hovers',dd);
		    			if(selected.size()>0){
							var prev = selected.prev('li');
							var _idx = selected.attr('_idx');
							selected.removeClass('hovers');
							if(prev.size()==0){
								prev = $('li:last',dd);
								_idx = num - 1;
								dd.scrollTop(dd[0].scrollHeight);
							}
							if((_idx - 1)*H < dd.scrollTop()){
								dd.scrollTop(dd.scrollTop() - H);
							}
		    			}else{
		    				prev = $('li:last',dd);
		    				dd.scrollTop(dd[0].scrollHeight);
						}
		    			prev.addClass('hovers');
		    			$(this).val(prev.text());
		    			val.val(prev.attr('v'));
		    			$(this).focus();
		    		}
					if(keyCode == 13){
						dd.hide();
						return false;
					}
				});
				
				
				
			});
        },
        reset: function(){
        	return this.each(function(){
        		var opts = $(this).data('options');
				var valueel = $("input[name=" + opts.hideName + "]");
				var data = $(this).data('list');
				$(this).val(data[0].text);
				valueel.val(data[0].value);
        	});
        },
		disabled: function(){
			return this.each(function(){
				$(this).attr('disabled','disabled');
				$(this).siblings('div.combox-trigger').unbind('click');
				$(this).parent().unbind('click');
			});
		},
        reload: function(data){
        	return this.each(function(){
        		var opts = $(this).data('options');
        		var valueel = $("input[name=" + opts.hideName + "]");
				opts.data = data;
				$(this).data('options', opts);
				$(this).data('list', data);
				$(this).val('');
				valueel.val('');
				if(data.length>0){
					$(this).val(data[0].text);
					valueel.val(data[0].value);
        		}
        	});
        },
		val: function(val){
			if(val){				
				var data = this.data('list');
				this.val(val);
				var txt = this.siblings("input");
				$(data).each(function(i){
					var t = this.text;
					var v = this.value;
					if(v == val){
						txt.val(t);
						return;
					}
				});
			}else{	
				return this.val();
			}
        }
	};  
})(jQuery);
String.prototype.contains = function(arr){
	for(var k = 0, len = arr.length; k < len; k++){
		if(this.indexOf(arr[k])==-1){
			return false;
		}
	}
	return true;
};