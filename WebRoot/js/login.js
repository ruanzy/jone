$(function() {
	$.i18n.properties({
		name : 'message',
		path : 'i18n/',
		mode : 'map',
		callback : function() {
			$('[id^=i18n]').each(function() {
				var key = $(this).attr('id').substr(5);
				$(this).html($.i18n.prop(key));
			});
		}
	});
	document.oncontextmenu = function() {
		return false;
	};
	$(document).keydown(function(e) {
		if (e.keyCode == 13) {
			$('#submit').click();
		}
	});
	var loginfrm = $('#loginfrm');
	$('#submit', loginfrm).click(function() {
		var un = $("#username", loginfrm).val();
		var ps = $("#password", loginfrm).val();
		var vc = $("#vcinput", loginfrm).val();
		var data = {
			username : un,
			password : ps,
			vc : vc
		};
		$.ajax({
			url : 'login',
			type : 'post',
			data : data,
			dataType : 'json',
			success : function(result) {
				if (result.result) {
					document.location = 'index.jsp';
				} else {
					$('#error').html(result.msg);
				}
			}
		});
	});
});