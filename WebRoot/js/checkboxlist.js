(function($){        
	$.fn.CheckboxList = function(method) {
		if (typeof method == 'string') {
            return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
	$.fn.CheckboxList.methods = {
		init: function(opt) {
			var defaults = {
				name: 'chks',
				textField: 'name',
				valueField: 'value',
				columns: 3,
				columnWidth: 100,
				data: null,
				dataUrl: null,
				valueUrl: null,
				values: [3,4]
			};
			var options = $.extend({}, defaults, opt);
			return this.each(function(){
				var me = $(this);
				if(options.dataUrl){
					$.ajax({
						url:options.dataUrl,
						cache: false,
						async:false,
						dataType:'json',
				        success:function(data){
				        	options['data'] = data;
						}
					});
				}
				if(options.valueUrl){
					$.ajax({
						url:options.valueUrl,
						cache: false,
						async:false,
						dataType:'json',
						success:function(data){
							options['values'] = data;
						}
					});
				}
				$(this).data('options', options);
				var html = new Array();
				html.push("<table>");
				$(options.data).each(function(i){
					var item = this;
					if(i%options.columns == 0){
						html.push("<tr>");
					}
					html.push("<td width=", options.columnWidth, ">");
					html.push("<input type='checkbox' name='", options.name, "' value='", item[options.valueField], "' hideFocus=true");
					if(options.values&&options.values.length>0){
						if($.inArray(item[options.valueField].toString(), options.values)!=-1){
							html.push(" checked=checked");
						}
					}
					html.push("/>");
					html.push(item[options.textField]);
					html.push("</td>");
					if((i + 1)%options.columns == 0){
						html.push("</tr>");
					}
	        	});
				html.push("</table>");
				me.append(html.join(''));
			});
        },
        val: function(v) {
			if(v){
				$('input[type=checkbox]', this).each(function(){
					if($(this).val() == v){
						$(this).attr("checked", true);
					}
				});
			}
        	var ret = new Array();
			$('input[type=checkbox]:checked', this).each(function(){
				ret.push($(this).val());
			});
			return ret;
        }
	}; 
})(jQuery);