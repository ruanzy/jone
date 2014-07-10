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
				var me = $(this);
				$(this).data('options', settings);
				var hideName = settings.hideName;
				var rdm = new Date().getTime();
				var html = [];
				html.push("<dl class='rzy-select'><dt id='dt_");
				html.push(rdm);
				html.push("'><input type='text' name='");
				html.push(hideName);
				html.push("_text' autocomplete='off'/>");
				html.push("<input type='hidden' name='");
				html.push(hideName);
				html.push("'/><span><i class='icon-caret-down'></i></span></dt><dd></dd></dl>");
				$(this).append(html.join(''));
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
				var dh = [];
				$(data).each(function(i){
					var a = "<li v='" + this[settings.valueField] + "' _idx=" + i + ">" + this[settings.textField] + "</li>" ;						
					dh.push(a);
				});
				var dd = $('dd', this);
				dd.append(dh.join(''));
				var dt = $('dt', this);
				var txt = $('input[type=text]', dt);
				var val = $('input[type=hidden]', dt);
				dd.click(function(e){
					var t = e.target;
					if(t.tagName == 'LI'){
						txt.val($(t).text());
						val.val($(t).attr('v'));
						$(this).hide();
						settings.select({text:$(t).text(), value:$(t).attr('v')});
					}
				});
				var lis = $("li", dd);
				lis.live('mouseenter',function(){
				   $(this).addClass("hovers");
				});
				lis.live('mouseleave',function(){
				   $(this).removeClass("hovers");    
				});
				$(document).bind("click",function(e){ 
					var target = $(e.target); 
					if(target.closest('#dt_' + rdm).length == 0){ 
						dd.hide(); 
					} else{
						lis.each(function(){
							if(txt.val() && (txt.val()== $(this).text())){
								$('li.hovers',dd).removeClass("hovers");  
								$(this).addClass("hovers");
							}							
						});
						dd.show(); 
					}
				});
				var num = lis.size();
				var H = lis.eq(0).outerHeight();
				var _idx = 0;
				
				txt.keyup(function(e){
					if(e.which==37||e.which==38||e.which==39||e.which==13||e.which==40){
						return false;
					}
					setTimeout(function(){
						var data = me.data('list');
						var k = $.trim(txt.val());
						if(k.length>0){
							var arr = grep(k, data, settings.filter);
							loadItems(arr);
						}else{
							//loadItems(data);
						}
					}, 10);
				});
				
				
				function loadItems(data){
					var dh = [];
					$(data).each(function(i){
						var a = "<li v='" + this[settings.valueField] + "' _idx=" + i + ">" + this[settings.textField] + "</li>" ;						
						dh.push(a);
					});
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
		val: function(){
    		return $('dt', this).find('input[type=hidden]').val();
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