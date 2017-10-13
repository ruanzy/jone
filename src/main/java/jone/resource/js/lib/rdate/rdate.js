(function($){  
	$.fn.rdate = function (options) {
        if (typeof options == "string") {
        	return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        }
        return this.each(function () {
        	var me = $(this);
        	var opts = $.extend({}, $.fn.rdate.defaults, options);
        	me.data('options', opts);
			var dl = $('<dl class="rdate"><dt></dt><dd class="box"></dd></dl>');
			me.wrap(dl);
			me.after('<div style="position:absolute;top:0;right:0;bottom:0;left:0;"></div>');
			me.after('<div class="d_icon"></div>');
			var dt = me.parent();
			var dd = dt.siblings();
			var picker = biuldPicker();
			dd.html(picker);
			bindEvent.call(me);
        });
	};
    $.fn.rdate.defaults = {
		format:'yyyy-MM-dd hh:mm:ss',
		onpicked:function(){
			
		}
    };
	$.fn.rdate.methods = {
        year : function(jq, v){
        	var y = this.find('.y_choose');
        	if(v){
        		y.val(v);
        	}
        	var v = y.val();
        	return parseInt(v);        	
        },
        month : function(v){
        	var m = this.find('.m_choose');
        	if(v){
        		m.val(v);
        	}
        	var v = y.val();
        	return parseInt(v);    
        },
        hour : function(v){
        	var h = this.find('.d_h');
        	if(v){
        		h.val(v);
        	}
        	var v = h.val();
        	return parseInt(v);
        },
        minute : function(v){
        	var m = this.find('.d_m');
        	if(v){
        		m.val(v);
        	}
        	var v = m.val();
        	return parseInt(v);
        },
        second : function(v){
        	var s = this.find('.d_s');
        	if(v){
        		s.val(v);
        	}
        	var v = s.val();
        	return parseInt(v);
        }
	}; 
	function format(date, format){ 
		if (!(date instanceof Date)) {
			return;
		}
		var o = { 
			"M+" : date.getMonth()+1,
			"d+" : date.getDate(),
			"h+" : date.getHours(),
			"m+" : date.getMinutes(),
			"s+" : date.getSeconds(),
			"q+" : Math.floor((date.getMonth()+3)/3),
			"S" : date.getMilliseconds()
		}; 
		if(/(y+)/.test(format)) { 
			format = format.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length)); 
		} 		
		for(var k in o) { 
			if(new RegExp("("+ k +")").test(format)) { 
				format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
			} 
		} 
		return format; 
	}
	function getDaysInMonth(year, month) {
		var isLeapYear = (((year % 4 === 0) && (year % 100 !== 0)) || (year % 400 === 0));
		return [31, (isLeapYear ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month];
	}
	function firstWeekDay(year, month) {
		return new Date(year, month, 1).getDay();
	}
	function lpad(v) {
		return v < 10 ? ('0' + v) : v;
	}
	function biuldTop() {
		var html = [];
		html.push('<div class="box-top">');
		html.push('<dl class="ybox">');
		html.push('<dt>');
		html.push('<a class="y_choose_prev"><cite></cite></a>');
		html.push('<input class="y_choose" readonly><label></label>');
		html.push('<a class="y_choose_next"><cite></cite></a>');
		html.push('<div style="clear:both;"></div>');
		html.push('</dt>');
		html.push('<dd class="yeardd">');
		html.push('<a class="yearup"><cite></cite></a>');
		html.push('<table class="yearlist">');
		html.push('</table>');
		html.push('<a class="yeardown"><cite></cite></a>');
		html.push('</dd>');
		html.push('</dl>');
		html.push('<dl class="mbox">');
		html.push('<dt>');
		html.push('<a class="m_choose_prev"><cite></cite></a>');
		html.push('<input class="m_choose" readonly><label></label>');
		html.push('<a class="m_choose_next"><cite></cite></a>');
		html.push('<div style="clear:both;"></div>');
		html.push('</dt>');
		html.push('<dd class="monthdd">');
		html.push(biuldMonthDD());
		html.push('</dd>');
		html.push('</dl>');
		html.push('<div style="clear:both;"></div>');
		html.push('</div>');
		return 	html.join('');	
	}
	function biuldYearDD(y) {
		var html = [];
		html.push('<a class="yearup"><cite></cite></a>');
		html.push('<table class="yearlist">');
		var begin = y - 5;
		for(var i = 0; i < 5; i++) {
			html.push('<tr>');
			for(var j = 0; j < 2; j++) {
				html.push('<td>', begin + (2*i + j), '</td>');
			}
			html.push('</tr>');
		}
		html.push('</table>');
		html.push('<a class="yeardown"><cite></cite></a>');
		return 	html.join('');	
	}
	function biuldMonthDD() {
		var html = [];
		html.push('<table class="monthlist">');
		for(var i = 0; i < 6; i++) {
			html.push('<tr>');
			for(var j = 1; j <= 2; j++) {
				html.push('<td>', 2*i + j, '</td>');
			}
			html.push('</tr>');
		}
		html.push('</table>');
		return 	html.join('');	
	}
	function biuldBottom() {
		var html = [];
		html.push('<div class="box-bottom">');
		html.push('<ul class="d_hms">');
		html.push('<li class="d_sj">时间</li>');
		html.push('<li class="d_h">00</li><li>:</li><li class="d_m">00</li><li>:</li><li class="d_s">00</li>');
		html.push('</ul>');
		html.push('<div class="d_btn">');
		html.push('<a class="d_clear">清空</a>');
		html.push('<a class="d_today">今天</a>');
		html.push('<a class="d_ok">确定</a>');
		html.push('</div>');
		html.push('</div>');
		return 	html.join('');	
	}
	function biuldCalender() {
		var html = [];
		var weekarr = ['日', '一', '二', '三', '四', '五', '六'];
		html.push('<table class="calender">');
		html.push('<thead><tr>');
		for(var i = 0, len = weekarr.length; i < len;i++) {
			html.push('<th>', weekarr[i], '</th>');
		}
		html.push('</tr></thead>');
		html.push('<tbody>');
		html.push('</tbody>');
		html.push('</table>');
		return 	html.join('');
	}
	function biuldPicker() {
		var t = biuldTop();
		var c = biuldCalender();
		var b = biuldBottom();
		return 	t + c + b;	
	}
	function getCalenderBody(y, m, d) {
		var html = [];
		var daysInMonth = getDaysInMonth(y, m);
		var _first_week_day = firstWeekDay(y, m);
		html.push('<tr>');	
		for(var i = 0; i < _first_week_day; i++) {
			html.push('<td class="empty"></td>');
		}
		var week_day = _first_week_day;
		var now = new Date();
		var ny = now.getFullYear();
		var nm = now.getMonth();
		for (var i = 1; i <= daysInMonth; i++) {
			week_day %= 7;
			if(week_day == 0){
				html.push('<tr>');	
			}
			html.push('<td class="day');
			if(ny == y && nm == m && i == d){
				html.push(' click');
			}
			html.push('"');
			html.push(' y=', y);
			html.push(' m=', m);
			html.push(' d=', i);
			html.push('>', i, '</td>');
			if(week_day == 6){
				html.push('</tr>');	
			}
			week_day++;
		}
		for(var i = week_day; i < 7; i++) {
			html.push('<td class="empty"></td>');
		}
		return 	html.join('');	
	}
	function setCalender(date){
		var dd = this;
		var y_choose = dd.find('.y_choose');
		var m_choose = dd.find('.m_choose');
		var y = date.getFullYear();
		var m = date.getMonth();
		var d = date.getDate();
		var hh = date.getHours();
		var mm = date.getMinutes();
		var ss = date.getSeconds();
		var data = {
			y : y,
			m : m,
			d : d,
			hh : hh,
			mm : mm,
			ss : ss
		};
		dd.data('data', data);
		y_choose.val(y + '年');
		m_choose.val((m + 1 < 10 ? ('0' + (m + 1)) : (m + 1)) + '月');
		var body = getCalenderBody(y, m, d);
		dd.find('.calender tbody').html(body);
		dd.find('.d_h').html(lpad(hh));
		dd.find('.d_m').html(lpad(mm));
		dd.find('.d_s').html(lpad(ss));
	}
	function setYearList(y){
		var html = [];
		var begin = y - 5;
		for(var i = 0; i < 5; i++) {
			html.push('<tr>');
			for(var j = 0; j < 2; j++) {
				html.push('<td>', begin + (2*i + j), '</td>');
			}
			html.push('</tr>');
		}
		this.find('.yearlist').html(html.join(''));
		this.data('y', y);
	}
	function changeYear(year){
		var me = this;
		var dt = me.parent();
		var dd = dt.siblings();
		var y_choose = dd.find('.y_choose');
		var m = dd.data('data').m;
		var d = dd.data('data').d;
		dd.data('data').y = year;
		y_choose.val(year + '年');
		var body = getCalenderBody(year, m, d);
		dd.find('.calender tbody').html(body);
	}
	function changeMonth(month){
		var me = this;
		var dt = me.parent();
		var dd = dt.siblings();
		var m_choose = dd.find('.m_choose');
		var y = dd.data('data').y;
		var d = dd.data('data').d;
		dd.data('data').m = month;
		m_choose.val(lpad(month + 1) + '月');
		var body = getCalenderBody(y, month, d);
		dd.find('.calender tbody').html(body);
	}
	function bindEvent(){
		var me = this;
		var opts = me.data('options');
		var dt = me.parent();
		var dd = dt.siblings();
		var y_choose = dd.find('.y_choose');
		var m_choose = dd.find('.m_choose');
		var y_choose_prev = dd.find('.y_choose_prev');
		var y_choose_next = dd.find('.y_choose_next');
		var m_choose_prev = dd.find('.m_choose_prev');
		var m_choose_next = dd.find('.m_choose_next');
		var d_clear = dd.find('.d_clear');
		var d_today = dd.find('.d_today');
		var d_ok = dd.find('.d_ok');
		var mboxdd = dd.find('.mbox dd');
		var yboxdd = dd.find('.ybox dd');
		var yearlist = dd.find('.yearlist');
		y_choose_prev.on('click', function(e){
			var y = dd.data('data').y;
			var m = dd.data('data').m;
			var d = dd.data('data').d;
			y--;
			dd.data('data').y = y;
			y_choose.val(y + '年');
			var body = getCalenderBody(y, m, d);
			dd.find('.calender tbody').html(body);
			e.stopPropagation();
		});
		y_choose_next.on('click', function(e){
			var y = dd.data('data').y;
			var m = dd.data('data').m;
			var d = dd.data('data').d;
			y++;
			dd.data('data').y = y;
			y_choose.val(y + '年');
			var body = getCalenderBody(y, m, d);
			dd.find('.calender tbody').html(body);
			e.stopPropagation();
		});
		m_choose_next.on('click', function(e){
			var y = dd.data('data').y;
			var m = dd.data('data').m;
			var d = dd.data('data').d;
			if(m == 11){
				m = 0;
				y++;
				dd.data('data').y = y;
			}else{
				m++;
			}
			dd.data('data').m = m;
			m_choose.val(lpad(m + 1) + '月');
			y_choose.val(y + '年');
			var body = getCalenderBody(y, m, d);
			dd.find('.calender tbody').html(body);
			e.stopPropagation();
		});
		m_choose_prev.on('click', function(e){
			var y = dd.data('data').y;
			var m = dd.data('data').m;
			var d = dd.data('data').d;
			if(m == 0){
				m = 11;
				y--;
				dd.data('data').y = y;
			}else{
				m--;
			}
			dd.data('data').m = m;
			m_choose.val(lpad(m + 1) + '月');
			y_choose.val(y + '年');
			var body = getCalenderBody(y, m, d);
			dd.find('.calender tbody').html(body);
			e.stopPropagation();
		});
		dd.find('.calender').on('click', function(e){
			var t = $(e.target);
			e.stopPropagation();
			if(t.hasClass('day')){
				dd.find('.click').removeClass('click');
				t.addClass('click');
				dd.data('data').d = t.text();
			}
			
		});
		dd.mouseup(function(e){
			e.stopPropagation();
		});
		d_clear.on('click', function(e){
			e.stopPropagation();
			me.val('');
			dd.hide();
		});
		d_today.on('click', function(e){
			e.stopPropagation();
			var now = new Date();
			me.val(format(now, opts.format));
			dd.hide();
		});
		d_ok.on('click', function(e){
			e.stopPropagation();
			var v = dd.data('data');
			var date = new Date();
			date.setFullYear(v.y);
			date.setMonth(v.m);
			date.setDate(v.d);
			date.setHours(v.hh);
			date.setMinutes(v.mm);
			date.setSeconds(v.ss);
			me.val(format(date, opts.format));
			dd.hide();
		});
		$(document).bind("mouseup", function(e) {
			dd.hide();
			mboxdd.hide();
			yboxdd.hide();
		});	
		dt.click(function(e){
			e.stopPropagation();
			var v = me.val();
			var date = new Date();
			if(v){
				date = new Date(Date.parse(v));
			}
			setCalender.call(dd, date);
			dd.show();
		});
		m_choose.on('click', function(e){
			e.stopPropagation();
			yboxdd.hide();
			mboxdd.show();
		});
		mboxdd.find('td').on('click', function(e){
			e.stopPropagation();
			var m = $(this).text();
			mboxdd.hide();
			changeMonth.call(me, m - 1);
		});
		y_choose.on('click', function(e){
			e.stopPropagation();
			mboxdd.hide();
			var y = parseInt(y_choose.val());
			setYearList.call(yboxdd, y);
			yboxdd.show();
		});
		yearlist.on('click', 'td', function(e){
			e.stopPropagation();
			var y = $(this).text();
			yboxdd.hide();
			changeYear.call(me, y);
		});
		yboxdd.find('.yearup').on('click', function(e){
			e.stopPropagation();
			var y = yboxdd.data('y');
			setYearList.call(yboxdd, y - 10);
		});
		yboxdd.find('.yeardown').on('click', function(e){
			e.stopPropagation();
			var y = yboxdd.data('y');
			setYearList.call(yboxdd, y + 10);
		});
	}
})(jQuery);