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
	$.fn.AutoComplete2.methods = {
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
				var hideName = settings.hideName;
				var rdm = new Date().getTime();
				var html = [];
				html.push("<dl class='rzy-select'>");		
				html.push("<dt id='dt_");
				html.push(rdm);
				html.push("'><input type='text' name='");
				html.push(hideName + '_text');
				html.push("' autocomplete='off'/>");
				html.push("<input type='hidden' name='" + hideName + "'/>");
				html.push("<span><i class='icon-caret-down'></i></span></dt>");
				html.push("<dd></dd>");
				html.push("</dl>");
				$(this).append(html.join(''));
				var url = settings.url;

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
				
				var dh = [];
				$(data).each(function(i){
					var item = this;
					var a = "<a v='" + item[settings.valueField] + "'>" + item[settings.textField] + "</a>" ;						
					dh.push(a);
				});

				var dt = $('dt', this);
				var dd = $('dd', this);
				dd.append(dh.join(''));
				dd.click(function(e){
					var t = e.target;
					if(t.tagName == 'A'){
						$(this).siblings('dt').find('input[type=text]').val($(t).text());
						$(this).siblings('dt').find('input[type=hidden]').val($(t).attr('v'));
						$(this).hide();
						settings.select({text:$(t).text(), value:$(t).attr('v')});
					}
				});
				
				$(document).bind("click",function(e){ 
					var target = $(e.target); 
					if(target.closest('#dt_' + rdm).length == 0){ 
						dd.hide(); 
					} else{
						dd.show(); 
					}
				}) 
				
				
				
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