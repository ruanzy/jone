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
	var changeVC = function() {
		$('#vc').attr('src', "captcha?_=" + new Date().getTime());
	};
	$(document).keydown(function(e) {
		if (e.keyCode == 13) {
			$('#submit').click();
		}
	});
	$("#username").focus();
	$("#username").Tip({mode : 'focus', content : 'asdhg'});
	$('#vc').css('border', '1px solid #999');
	changeVC();
	$('#vc').click(changeVC);
	$('#submit').click(function() {
		var un = $("#username").val();
		var ps = $("#password").val();
		var vc = $("#vcinput").val();
		var data = {
			username : un,
			password : ps,
			vc : vc
		};
		$.ajax({
			url : 'common/login',
			type : 'post',
			data : data,
			dataType : 'json',
			success : function(result) {
				if (result.result) {
					document.location = './';
				} else {
					$('#error').html(result.msg);
				}
			}
		});
	});
});