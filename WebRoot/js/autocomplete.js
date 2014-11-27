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
	$.fn.AutoComplete = function(method) {
		if (typeof method == 'string') {
            return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
	$.fn.AutoComplete.methods = {
		init: function(options) {
			var defaults = {
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
			var settings = $.extend({}, defaults, options);
			return this.each(function(){
				$(this).data('options', settings);
				var el = $(this).attr('autocomplete', 'off').addClass('combox-input');
				var hideName = settings.hideName;
				el.attr('name', hideName + '_text');
				var url = el.attr('url')||settings.url;
				var vv = el.attr('value');
				var valueel = $("<input type='hidden' name='" + hideName + "'/>");
				var box = $('<span class=combox-wrapper></span>');
				var trigger = $('<div class=combox-trigger></div>');
				var dl = $('<ul class=combox-dl></ul>');
				var w = el.outerWidth();
				el.after(dl);
				el.wrap(box);
				el.after(valueel);
				trigger.height(el.outerHeight() - 2);
				trigger.width(el.outerHeight() - 2);
				el.width(w - el.outerHeight());
				el.after(trigger);
				dl.width(el.outerWidth() -2 + trigger.outerWidth());
				var data = new Array();
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
				}else{
					data = settings.data;
				}
				$(this).data('list', data);
				if(vv){
					var vs = $(this).data('list');
					$(vs).each(function(){
						var v = this[settings.valueField];
						if(vv == v){
							el.val(this[settings.textField]);
							valueel.val(vv);
						}
					});
				}else{
					el.val(settings.emptyText);
					valueel.val('');
				}
				
				dl.click(function(e){
					var t = e.target;
					if(t.tagName == 'LI'){
						el.val($(t).text());
						el.focus();
						valueel.val($(t).attr('v'));
						dl.hide();
						settings.select({text:$(t).text(), value:$(t).attr('v')});
					}
				});
				dl.mouseover(function(e){
					var t = e.target;
					if(t.tagName == 'LI'){
						$('li.selected',dl).removeClass('selected');
						$(t).addClass('selected');
					}
				});
				dl.mouseout(function(e){
					var t = e.target;
					if(t.tagName == 'LI'){
						$(t).removeClass('selected');
					}
				});
				el.keyup(function(event){
					$(document).bind('click', hideDL);
					if(event.which==37||event.which==38||event.which==39||event.which==13){
						return false;
					}
					if(event.which==40){
						//trigger.click();
					}else{
						setTimeout(function(){
							var data = el.data('list');
							var k = $.trim(el.val());
							if(k.length>0){
								var arr = grep(k, data, settings.filter);
								loadItems(arr);
							}else{
								loadItems(data);
							}
						}, 10);
					}
				});
				el.bind('keydown',function(e){
		    		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode; 
		    		if(keyCode == 40){
		    			var selecteds = $('li.selected',dl);
		    			if(selecteds.size()>0){
							var selected = $(selecteds[0]);
							var next = selected.next('li');
							var _idx = selected.attr('_idx');
							if(next.size()>0){
								selected.removeClass('selected');
								next.addClass('selected');
								if(_idx*27>=(270 + dl.scrollTop())){
									dl.scrollTop(dl.scrollTop() + 27);
								}
								el.val(next.text());
								el.focus();
								valueel.val(next.attr('v'));
							}
		    			}else{
		    				var selected = $('li:first',dl);
		    				selected.addClass('selected');
							el.val(selected.text());
							el.focus();
							valueel.val(selected.attr('v'));
		    			}
		    		}
		    		if(keyCode == 38){
		    			var selecteds = $('li.selected',dl);
		    			if(selecteds.size()>0){
							var selected = $(selecteds[0]);						
							var prev = selected.prev('li');
							var _idx = selected.attr('_idx');
							if(prev.size()>0){
								selected.removeClass('selected');
								prev.addClass('selected');
								if((_idx-1)*27<=(dl.scrollTop())){
									dl.scrollTop(dl.scrollTop() - 27);
								}
								el.val(prev.text());
								el.focus();
								valueel.val(prev.attr('v'));
							}
		    			}
		    		}
					if(keyCode == 13){
						var selecteds = $('li.selected',dl);
						if(selecteds.size()>0){
							var selected = $(selecteds[0]);
							el.val(selected.text());
							valueel.val(selected.attr('v'));
							el.focus();
							dl.hide();
							return false;
						}
					}
				});
				trigger.click(function(e){
					e.stopPropagation();
					e.preventDefault();
					$('.combox-dl').hide();
					var kk = new Array();
					if(settings.emptyText!=undefined){
						kk.push("<li v='' _idx='0'>");
						kk.push(settings.emptyText);
						kk.push("</li>");
					}
					var data = el.data('list');
					var q = $.trim(el.val());
					if(data.length>0){
						dl.empty();
						var span = $('<span>');
						$(data).each(function(i){
							var item = this;
							var temp = "<li v='" + item[settings.valueField] + "' _idx='" + (++i) + "'";						
							if(q.length>0&&item['text']==q){
								temp += " class=selected";
							}
							var html = span.text(item[settings.textField]).html();
							temp += ">" +  html + "</li>";
							kk.push(temp);
						});
						span.remove();
						dl.append(kk.join(''));
						if(data.length>10){
							dl.height(270);
						}else{
							if(settings.emptyText!=undefined){
								dl.height(27*(data.length + 1));
							}else{
								dl.height(27*(data.length));								
							}
						}
						var L = el.offset().left;
						var T = el.offset().top + el.outerHeight(true) + 1;
						dl.css({left: L, top: T});
						dl.width(el).show();
						dl.addClass('rzy-show');
						el.focus();
					}
					$(document).bind('click', hideDL);
				});
				
				function hideDL(e){
					e.stopPropagation();
					e.preventDefault();
					if(e.target != el[0] && !insideDL(e)){
						var selecteds = $('li.selected',dl);
						if(selecteds.size()>0){
							selected = $(selecteds[0]);
							el.val(selected.text());
							valueel.val(selected.attr('v'));
						}else{
							if(settings.emptyText!=undefined){
								el.val(settings.emptyText);
								valueel.val('');
							}else{
								var data = el.data('list');
								el.val(data[0].text);
								valueel.val(data[0].value);								
							}
						}
						dl.hide();
						$(document).unbind('click', hideDL);
					}
				}
				
				function insideDL(e){
					var offset = dl.position();    
					offset.right = offset.left + dl.outerWidth();    
					offset.bottom = offset.top + dl.outerHeight();   
					return 
					e.pageY < offset.bottom 
					&&e.pageY > offset.top 
					&&e.pageX < offset.right 
					&&e.pageX > offset.left;
				}
				
				function loadItems(d){
					var data = new Array();
					if(settings.emptyText!=undefined){
						data.push({text:settings.emptyText, value:''});
					}
					$.merge(data, d);
					if(data.length>0){
						dl.empty();
						var kk = new Array();
						var _idx = 1;
						var span = $('<span>');
						$(data).each(function(){
							var item = this;
							var html = span.text(item['text']).html();
							kk.push("<li v='" + item['value'] + "' _idx='" + (_idx++) + "'>" +  html + "</li>");
						});
						span.remove();
						dl.append(kk.join(''));
						var q = $.trim(el.val());
						if(q.length>0){
							$('li:first',dl).addClass('selected');
						}
						if(data.length>10){
							dl.height(270);
						}else{
							dl.height(27*(data.length));
						}
						var L = el.offset().left;
						var T = el.offset().top + el.outerHeight(true) + 1;
						dl.css({left: L, top: T});
						dl.show();
						dl.addClass('rzy-show');
					}else{
						dl.empty();
						dl.hide();
					}	
				}
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
    		var opts = $(this).data('options');
			var valueel = $("input[name=" + opts.hideName + "]");
			return valueel.val();
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