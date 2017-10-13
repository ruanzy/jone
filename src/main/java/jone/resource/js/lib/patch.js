define(['toastr' ], function(toastr) {
	var username = sessionStorage["username"];
	if(!username){
		//location.href = 'login.html';
	}
	toastr.options = {  
			closeButton: true,  
			debug: false,  
			//progressBar: true,  
			positionClass: "toast-top-center",  
			onclick: null,  
			showDuration: "300",  
			hideDuration: "500",  
			timeOut: "1500",  
			extendedTimeOut: "1000",  
			showEasing: "swing",  
			hideEasing: "linear",  
			showMethod: "fadeIn",  
			hideMethod: "fadeOut"  
	};
	$.i18n.properties({
		name : 'i18n',
		path : 'i18n/',
		mode : 'map',
		language : localStorage.getItem('lang') || 'zh',
		//cache : true,
		encoding : 'UTF-8'
	});
	var sessionOut = false;
	$.ajaxSetup({
		//timeout: 3000,
		complete: function(xhr, status){
			$('#loading').hide();
			if(status == 'timeout'){
				console.log("超时");
				xhr.abort();
			}
			if(xhr.status == 0){
	        	/**
				var d = dialog({
	        	    title: '提示',
	        	    content: '服务异常或停止',
	        	    okValue: '确定',
	        	    ok: function () {}
	        	});
	        	d.showModal();
	        	**/
			}
			if(xhr.status == 401){
				xhr.abort();
				if(!sessionOut){			
					sessionOut = true;
					sessionStorage.token = '';
		        	var d = dialog({
		        	    title: '提示',
		        	    content: '您的登录已经失效，请重新登录!',
		        	    okValue: '确定',
		        	    ok: function () {
						    location.href = 'login.html';
						    sessionOut = false;
		        	    }
		        	});
		        	d.showModal();
				}
			}
			if(xhr.status == 500){
				$.alert('服务器处理异常!<br>' + xhr.statusText);
			}
		}
	});
	$(document).ajaxSend(function(ev, xhr, settings) {
		var server = "";
		if(settings.url.indexOf('.html') > -1){
			//settings.url = "http://localhost:8089/" + settings.url;
		}else{
			settings.url = server + settings.url;
		}
		xhr.setRequestHeader('Authorization', sessionStorage.token);
	});
	$.ajax({
		url : "user/getPermission",
		type : 'post',
		async: false,
		data: {username: sessionStorage.username},
		dataType : 'json',
		success : function(result) {
			sessionStorage.setItem('permission', result);
		}
	});
	$.fn.serializeObject = function() {
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function() {
	        if (o[this.name] !== undefined) {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	            o[this.name] = this.value || '';
	        }
	    });
	    return o;
	};
});