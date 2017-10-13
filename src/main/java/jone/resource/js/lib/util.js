define(['template'], function(template){
	//var template = require('./template');
	template.helper('i18n', function(key, params) {
		return $.i18n.prop(key);
	});
	template.helper("allow",function(id){  
	    var ret = false;
	    var username = sessionStorage["username"];
	    if(username == 'admin'){
	    	return true;
	    }
	    var arr = sessionStorage.getItem('permission');
	    ret = ($.inArray(id, arr) >= 0);
	    return ret;  
	}); 
	template.helper("isAdmin",function(){  
	    return sessionStorage["username"] == 'admin';
	});
	Date.prototype.Format = function (fmt) { //author: meizz 
	    var o = {
	        "M+": this.getMonth() + 1, //月份 
	        "d+": this.getDate(), //日 
	        "h+": this.getHours(), //小时 
	        "m+": this.getMinutes(), //分 
	        "s+": this.getSeconds(), //秒 
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	        "S": this.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	}
	var util = {
		lang : function(v) {
			if(v){
				localStorage.setItem('lang', v);
			}
			return localStorage.getItem('lang');
		},
		i18n : function(key) {
			var args = Array.prototype.slice.call(arguments, 1);
			return $.i18n.prop(key, args);
		},
		tpl : function(view, d) {
			/**var cache = sessionStorage.getItem(view);
			if(cache){
				return template.compile(cache)(d);
			}**/
			var tpl = $.ajax({
				url : view,
				cache : false,//此处不能缓存
				async : false
			}).responseText;
			//sessionStorage.setItem(view, tpl);
			return template.compile(tpl)(d);
		},
		render : function(view, container, d) {
			var tpl = this.tpl(view, d);
			if(typeof container == 'string'){
				document.getElementById(container).innerHTML = tpl;
			}else{
				$(container).html(tpl);
			}
		},
		allow : function(id) {
			var ret = false;
		    var username = sessionStorage["username"];
		    if(username == 'admin'){
		    	return true;
		    }
		    var arr = sessionStorage.getItem('permission');
		    ret = ($.inArray(id, arr) >= 0);
		    return ret;
		},
		selectMenu : function(id) {
			sessionStorage.setItem('current', id);
			var lis = $('.navbar-nav >li');
			lis.removeClass('active');
			$(id).addClass('active');
		},
		bytesToSize : function(bytes) {
		    if (bytes == 0) return '0 B';
		    var k = 1000; // or 1024
		    var sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
		    var i = Math.floor(Math.log(bytes) / Math.log(k));
		    return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
		},
		dateFormat : function(date, fmt) {
			var o = {   
					"M+" : date.getMonth()+1,                 //月份   
					"d+" : date.getDate(),                    //日   
					"H+" : date.getHours(),                   //小时   
					"m+" : date.getMinutes(),                 //分   
					"s+" : date.getSeconds(),                 //秒   
					"q+" : Math.floor((date.getMonth()+3)/3), //季度   
					"S"  : date.getMilliseconds()             //毫秒   
			};   
			if(/(y+)/.test(fmt))   
			fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));   
			for(var k in o)   
				if(new RegExp("("+ k +")").test(fmt))   
					fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
			return fmt;   
		}
	};
	return util;
});
function dateFormat( cellvalue, options, rowObject ){  
    return new Date(cellvalue).pattern("yyyy-MM-dd hh:mm:ss");  
}  