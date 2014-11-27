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
	$.fn.Datepicker2 = function(method) {
		if (typeof method == 'string') {
            return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
	$.fn.Datepicker2.methods = {	
		init: function(opt) {
			var defaults = {
					hideName:'t1',
					format:'yyyy-MM-dd'
				};
			var settings = $.extend({}, defaults, opt);
			var CY = new Date().getFullYear();
			var CM = new Date().getMonth();
			var box_markup = new Array();
			box_markup.push(
				"<table class=dp_title align=center>",
				"<tr height=28>" ,
				"<td><span class='prev_year'><<</span></td>" ,
				"<td><span class='prev_month'><</span></td>" ,
				"<td><div class='month_input'>",
				CM + 1,
				"</div></td>",
				"<td><input class='year_input'value='",
				CY,
				"' size=4 maxlength=4/></td>" ,
				"<td><span class='next_month'>></span></td>" ,
				"<td><span class='next_year'>>></span></td>" ,
				"</tr>" ,
				"</table>",
				"<table class=dp_body>" ,
				"<thead>" ,
				"<tr bgcolor='#bdebee'><td>日</td><td>一</td><td>二</td><td>三</td><td>四</td><td>五</td><td>六</td></tr>" ,
				"</thead>" ,
				"<tbody>" ,
				biuldBody(CY, CM + 1),
				"</tbody>" ,
				"</table>" ,
				"<div class=dp_foot>" ,
				"<button class='clear'>清空</button>&nbsp;&nbsp;&nbsp;<button class='today'>今天</button>",
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
				var rdm = new Date().getTime();
				var html = [];
				html.push("<dl class='rzy-datepicker'>");		
				html.push("<dt id='dt_");
				html.push(rdm);
				html.push("'><input type='text' name='");
				html.push(settings.hideName);
				html.push("' autocomplete='off' readonly='readonly'/>");
				html.push("</dt>");
				html.push("<dd>");
				html.push(box_markup.join(''));
				html.push(month_pop_markup.join(''));
				html.push(year_pop_markup.join(''));
				html.push("</dd>");
				html.push("</dl>");
				$(this).append(html.join(''));			
				var dt = $('dt', this);
				var dd = $('dd', this);
				$(document).bind("click",function(e){ 
					var target = $(e.target); 
					if(target.closest('#dt_' + rdm).length == 0){ 
						dd.hide(); 
					} else{
						dd.show(); 
					}
				}) 
				var month_input = $('.month_input', dd);
				var year_input = $('.year_input', dd);
				var dp_tbody = $('.dp_body tbody', dd);
				var prev_year = $('.prev_year', dd);
				var prev_month = $('.prev_month', dd);
				var next_month = $('.next_month', dd);
				var next_year = $('.next_year', dd);
				var clr = $('.clear', dd);
				var today = $('.today', dd);
				var year_pop_view = $('.dp_year_pop_view', dd);
				var year_left = $('.year_left', dd);
				var year_right = $('.year_right', dd);

				dd.bind('click', function(e){
					e.stopPropagation();
					var t = $(e.target);
					if(t.hasClass('curr_month_td')){
						var y = year_input.val();
						var m = month_input.text();
						var d = t.text();
						var t = new Date();
						t.setFullYear(y);
						t.setMonth(m - 1);
						t.setDate(d);
						var v = t.format(settings.format);
						dt.find('input[type=text]').val(v);
						dd.hide();
					}
					if(t.hasClass('prev_year')){
						var y = year_input.val();
						var m = month_input.text();
						year_input.val(y - 1);
						dp_tbody.empty().append(biuldBody(parseInt(y) - 1, m));
					}
					if(t.hasClass('prev_month')){
						var y = year_input.val();
						var m = month_input.text();
						dp_tbody.empty().append(biuldBody(y, m - 1));
						if(m == 1){
							month_input.val(12);
							year_input.val(y - 1);
						}else{
							month_input.text(m - 1);
						}
					}
					if(t.hasClass('next_month')){
						var y = year_input.val();
						var m = month_input.text();
						dp_tbody.empty().append(biuldBody(y, parseInt(m) + 1));
						if(m == 12){
							month_input.text(1);
							year_input.val(parseInt(y) + 1);
						}else{
							month_input.text(parseInt(m) + 1);
						}
					}
					if(t.hasClass('next_year')){
						var y = year_input.val();
						var m = month_input.text();
						year_input.val(parseInt(y) + 1);
						dp_tbody.empty().append(biuldBody(parseInt(y) + 1, m));
					}
					if(t.hasClass('month_input')){
						var L = $(this).position().left;
						var T = $(this).position().top + $(this).outerHeight(true) - 1;
						$('.dp_month_pop').css({left: 30, top: 30});
					}
					if(t.hasClass('year_input')){
						var L = $(this).position().left;
						var T = $(this).position().top + $(this).outerHeight(true) - 1;
						$('.dp_year_pop').css({left: 90, top: 30});
					}
				});

				year_pop_view.data('v', CY);
				year_pop_view.empty().append(biuldY(CY));

				$('.dp_month_pop').click(function(e){
					var me = $(this);
					var t = $(e.target);
					if(t.hasClass('pop_month')){
						var m = t.text();
						month_input.text(m);
						month_input.css('border-color', '#fff');
						me.css({left: '-9999px', top: 0});
						var y = year_input.val();
						dp_tbody.empty().append(biuldBody(y, m));
					}
				});
				$('.dp_year_pop').click(function(e){
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
					dt.find('input[type=text]').val('');
				});
				today.click(function(){
					dt.find('input[type=text]').val(new Date().format(settings.format));
					dd.hide();
				});
			});
        }
	}; 
})(jQuery);