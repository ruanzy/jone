define(['template'], function(template){
//var template = require('./template');
localStorage.setItem('lang', 'zh');
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
var util = {
	lang : function(v) {
		if(v){
			localStorage.setItem('lang', v);
		}
		return localStorage.getItem('lang');
	},
	i18n : function(key, params) {
		return $.i18n.prop(key);
	},
	tpl : function(view, d) {
		var tpl = $.ajax({
			url : view,
			cache : false,//此处不能缓存
			async : false
		}).responseText;
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
	log : function(msg) {
		var now = new Date();
		var time = now.Format('yyyy-MM-dd HH:mm:ss.S');
		console.log(time + ' ' + msg);
	}
};
//module.exports = obj;
Date.prototype.Format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"H+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};
return util;
});