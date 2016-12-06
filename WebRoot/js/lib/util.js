define(['template'], function(template){
//var template = require('./template');
localStorage.setItem('lang', 'zh');
$.i18n.properties({
	name : 'i18n',
	path : 'i18n/',
	mode : 'map',
	language : localStorage.getItem('lang'),
	cache : false,
	encoding : 'UTF-8'
});
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
		var cache = sessionStorage.getItem(view);
		if(cache){
			return template.compile(cache)(d);
		}
		var tpl = $.ajax({
			url : view,
			cache : false,//此处不能缓存
			async : false
		}).responseText;
		sessionStorage.setItem(view, tpl);
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
	selectMenu : function(id) {
		sessionStorage.setItem('current', id);
		var lis = $('header.layout .nav >li');
		lis.removeClass('active');
		$(id).addClass('active');
	}
};
return util;
});