(function($){        
	function biuldBody(y,m){
		var html = [];
		var arr = [];
		var cls = [];
		var curr_weekday = new Date(y, m - 1, 1).getDay();
		var prev_month_days = new Date(y, m - 1, 0).getDate();
		var curr_month_days = new Date(y, m, 0).getDate();
		var CY = new Date().getFullYear();
		var CM = new Date().getMonth();
		var CD = new Date().getDate();
		for(var k = prev_month_days - curr_weekday + 1; k <= prev_month_days; k++){
			arr.push(k);
			cls.push('prev_month_td');
		}
		for(var k = 1; k <= curr_month_days; k++){
			arr.push(k);
			if(CY == y && CM == m - 1 && CD == k){
				cls.push('curr_month_td curr_date_td');
			}else{
				cls.push('curr_month_td');
			}
		}
		for(var k = 1; k <= 42 - curr_month_days - curr_weekday; k++){
			arr.push(k);
			cls.push('next_month_td');
		}
		for(var k = 0; k < arr.length; k++){
			if(k%7 == 0){
				html.push("<tr><td class='", cls[k], "'>", arr[k], '</td>');
			}else if(k%7 == 6){
				html.push("<td class='", cls[k], "'>", arr[k], '</td></tr>');
			}else{
				html.push("<td class='", cls[k], "'>", arr[k], '</td>');
			}
		}
		return html.join('');
	}
	function biuldY(v){
		var html = [];
		for(var k = v - 5; k < v; k++){
			html.push('<tr>');
			for(var m = k; m <= k + 5; m += 5){
				html.push('<td class=pop_year>', m, '</td>');
			}
			html.push('</tr>');
		}
		return html.join('');
	}
	Date.prototype.format = function(format){ 
		var o = { 
			"M+" : this.getMonth()+1, //month 
			"d+" : this.getDate(), //day 
			"h+" : this.getHours(), //hour 
			"m+" : this.getMinutes(), //minute 
			"s+" : this.getSeconds(), //second 
			"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
			"S" : this.getMilliseconds() //millisecond 
		}; 
		if(/(y+)/.test(format)) { 
			format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
		} 		
		for(var k in o) { 
			if(new RegExp("("+ k +")").test(format)) { 
				format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
			} 
		} 
		return format; 
	};
	$.fn.Datepicker = function(method) {
		if (typeof method == 'string') {
            return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
	$.fn.Datepicker.methods = {	
		init: function(opt) {
			var defaults = {
					format:'yyyy-MM-dd'
				};
			var settings = $.extend({}, defaults, opt);
			var box_markup = new Array();
			box_markup.push("<div class=dp_out>",
			"<div class=dp_in>",
				"<table class=dp_title align=center>",
				"<tr height=28>" ,
				"<td><span class='prev_year'><<</span></td>" ,
				"<td><span class='prev_month'><</span></td>" ,
				"<td><input class='month_input' value='9' size=2 maxlength=2 readonly/></td>" ,
				"<td><input class='year_input'value='2013' size=4 maxlength=4/></td>" ,
				"<td><span class='next_month'>></span></td>" ,
				"<td><span class='next_year'>>></span></td>" ,
				"</tr>" ,
				"</table>",
				"<table class=dp_body>" ,
				"<thead>" ,
				"<tr bgcolor='#bdebee'><td>日</td><td>一</td><td>二</td><td>三</td><td>四</td><td>五</td><td>六</td></tr>" ,
				"</thead>" ,
				"<tbody>" ,
				"</tbody>" ,
				"</table>" ,
				"<div class=dp_foot>" ,
				"<button class='clear'>清空</button>&nbsp;&nbsp;&nbsp;<button class='today'>今天</button>" ,
				"</div>",
				"</div>",
				"</div>");
			var year_pop_markup = new Array();
			year_pop_markup.push(
					"<table class=dp_year_pop>" ,
					'<tr><td colspan=2>',
					"<table class=dp_year_pop_view>" ,
					"<tbody>" ,
					"</tbody>" ,
					"</table>" ,
					'</td>',
					'</tr>',
					'<tr><td class=year_left><<</td><td class=year_right>>></td></tr>',
					"</table>");
			var month_pop_markup = new Array();
			month_pop_markup.push(
					"<table class=dp_month_pop>" ,
					'<tr><td class=pop_month>1</td><td class=pop_month>7</td></tr>',
					'<tr><td class=pop_month>2</td><td class=pop_month>8</td></tr>',
					'<tr><td class=pop_month>3</td><td class=pop_month>9</td></tr>',
					'<tr><td class=pop_month>4</td><td class=pop_month>10</td></tr>',
					'<tr><td class=pop_month>5</td><td class=pop_month>11</td></tr>',
					'<tr><td class=pop_month>6</td><td class=pop_month>12</td></tr>',
					"</tr>" ,
					"</table>");
			return this.each(function(){
				var me = $(this).attr('readonly', true).addClass('datebox');
				me.keydown(function(e){
		    		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode; 
		    		if(keyCode == 8){
		    			return false;
		    		}
		    	});
				var box = $(box_markup.join(''));
				var month_pop = $(month_pop_markup.join(''));
				var year_pop = $(year_pop_markup.join(''));
				me.after(box).after(month_pop).after(year_pop);
				var month_input = $('.month_input', box);
				var year_input = $('.year_input', box);
				var dp_tbody = $('.dp_body tbody', box);
				var prev_year = $('.prev_year', box);
				var prev_month = $('.prev_month', box);
				var next_month = $('.next_month', box);
				var next_year = $('.next_year', box);
				var clr = $('.clear', box);
				var today = $('.today', box);
				var year_pop_view = $('.dp_year_pop_view', year_pop);
				var year_left = $('.year_left', year_pop);
				var year_right = $('.year_right', year_pop);
				function insideBox(e){
					var offset = box.position();    
					offset.right = offset.left + box.outerWidth();    
					offset.bottom = offset.top + box.outerHeight();   
					return e.pageY < offset.bottom &&e.pageY > offset.top &&e.pageX < offset.right &&e.pageX > offset.left;
				}
				function insideMonthPop(e){
					var offset = month_pop.position();    
					offset.right = offset.left + month_pop.outerWidth();    
					offset.bottom = offset.top + month_pop.outerHeight();   
					return e.pageY < offset.bottom &&e.pageY > offset.top &&e.pageX < offset.right &&e.pageX > offset.left;
				}
				function insideYearPop(e){
					var offset = year_pop.position();    
					offset.right = offset.left + year_pop.outerWidth();    
					offset.bottom = offset.top + year_pop.outerHeight();   
					return e.pageY < offset.bottom &&e.pageY > offset.top &&e.pageX < offset.right &&e.pageX > offset.left;
				}
				function hideBox(e){
					e.stopPropagation();
					if(e.target != me[0] && !insideBox(e)){
						box.css({left: '-9999px', top: 0});
						$(document).unbind('click', hideBox);
					}
					var y = year_input.val();
					var m = month_input.val();
					dp_tbody.empty().append(biuldBody(y, m));
				}
				function hideYearPop(e){
					e.stopPropagation();
					if(e.target != year_input[0] && !insideYearPop(e)){
						year_pop.css({left: '-9999px', top: 0});
						$(document).unbind('click', hideYearPop);
					}
					var y = year_input.val();
					var m = month_input.val();
					dp_tbody.empty().append(biuldBody(y, m));
				}
				function hideMonthPop(e){
					e.stopPropagation();
					if(e.target != month_input[0] && !insideMonthPop(e)){
						month_pop.css({left: '-9999px', top: 0});
						$(document).unbind('click', hideMonthPop);
					}
					var y = year_input.val();
					var m = month_input.val();
					dp_tbody.empty().append(biuldBody(y, m));
				}
				dp_tbody.bind('click', function(e){
					var t = $(e.target);
					if(t.hasClass('curr_month_td')){
						var y = year_input.val();
						var m = month_input.val();
						var d = t.text();
						var t = new Date();
						t.setFullYear(y);
						t.setMonth(m - 1);
						t.setDate(d);
						me.val(t.format(settings.format));
						box.css({left: '-9999px', top: 0});
					}
				});
				var CY = new Date().getFullYear();
				var CM = new Date().getMonth();
				dp_tbody.empty().append(biuldBody(CY, CM + 1));
				year_pop_view.data('v', CY);
				year_pop_view.empty().append(biuldY(CY));
				me.bind('click', function(e){
					month_input.val(CM + 1);
					year_input.val(CY);
					var L = $(this).offset().left;
					var T = $(this).offset().top + $(this).outerHeight() + 1;
					box.css({left: L, top: T});
					$(document).bind('click', hideBox);
				});	
				prev_year.click(function(){
					var y = year_input.val();
					var m = month_input.val();
					year_input.val(y - 1);
					dp_tbody.empty().append(biuldBody(parseInt(y) - 1, m));
				});
				prev_month.click(function(){
					var y = year_input.val();
					var m = month_input.val();
					dp_tbody.empty().append(biuldBody(y, m - 1));
					if(m == 1){
						month_input.val(12);
						year_input.val(y - 1);
					}else{
						month_input.val(m - 1);
					}
				});
				next_month.click(function(){
					var y = year_input.val();
					var m = month_input.val();
					dp_tbody.empty().append(biuldBody(y, parseInt(m) + 1));
					if(m == 12){
						month_input.val(1);
						year_input.val(parseInt(y) + 1);
					}else{
						month_input.val(parseInt(m) + 1);
					}
				});
				next_year.click(function(){
					var y = year_input.val();
					var m = month_input.val();
					year_input.val(parseInt(y) + 1);
					dp_tbody.empty().append(biuldBody(parseInt(y) + 1, m));
				});
				month_input.click(function(){
					$(this).select();
					var L = $(this).offset().left;
					var T = $(this).offset().top + $(this).outerHeight(true) - 1;
					month_pop.css({left: L, top: T});
					$(document).bind('click', hideMonthPop);
				});
				year_input.focus(function(){
					$(this).select();
					year_pop_view.data('v', $(this).val());
					year_pop_view.empty().append(biuldY($(this).val()));
					var L = $(this).offset().left;
					var T = $(this).offset().top + $(this).outerHeight(true) - 1;
					year_pop.css({left: L, top: T});
					$(document).bind('click', hideYearPop);
				});
				month_pop.click(function(e){
					var me = $(this);
					var t = $(e.target);
					if(t.hasClass('pop_month')){
						var m = t.text();
						month_input.val(m);
						month_input.css('border-color', '#fff');
						me.css({left: '-9999px', top: 0});
						var y = year_input.val();
						dp_tbody.empty().append(biuldBody(y, m));
					}
				});
				year_pop.click(function(e){
					var me = $(this);
					var t = $(e.target);
					if(t.hasClass('pop_year')){
						var y = t.text();
						year_input.val(y);
						year_input.css('border-color', '#fff');
						me.css({left: '-9999px', top: 0});
						var m = month_input.val();
						dp_tbody.empty().append(biuldBody(y, m));
					}
				});
				year_left.click(function(e){
					e.stopPropagation();
					var v = year_pop_view.data('v');
					year_pop_view.empty().append(biuldY(parseInt(v) - 10));
					year_pop_view.data('v', parseInt(v) - 10);
				});
				year_right.click(function(e){
					e.stopPropagation();
					var v = year_pop_view.data('v');
					year_pop_view.data('v', parseInt(v) + 10);
					year_pop_view.empty().append(biuldY(parseInt(v) + 10));
				});
				clr.click(function(){
					me.val('');
				});
				today.click(function(){
					me.val(new Date().format(settings.format));
					box.css({left: '-9999px', top: 0});
				});
			});
        }
	}; 
})(jQuery);