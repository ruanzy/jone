$(function() {
	/***************************************************************************
	 * document.oncontextmenu = function() { return false; };
	 **************************************************************************/
	$(document).keydown(function(e) {
		if (e.keyCode == 13) {
			$('#submit').click();
		}
	});
	var loginfrm = $('#loginfrm');
	loginfrm.find('input').focus(function() {
		$(this).addClass('logininput-focus');
		$(this).siblings('i').addClass('loginlabel-focus');
	}).blur(function() {
		$(this).removeClass('logininput-focus');
		$(this).siblings('i').removeClass('loginlabel-focus');
	});
	$('#submit', loginfrm).click(function() {
		var un = $("#username", loginfrm);
		var ps = $("#password", loginfrm);
		var vc = $("#vcinput", loginfrm);
		var go = $("#go", loginfrm);
		if (!un.val()) {
			un.addClass('logininput-focus');
			$('#error').html('请输入用户名!');
			return;
		}
		if (!ps.val()) {
			ps.addClass('logininput-focus');
			$('#error').html('请输入密码!');
			return;
		}
		if (!vc.val()) {
			vc.addClass('logininput-focus');
			$('#error').html('请输入验证码!');
			return;
		}
		debugger;
		var data = {
			username : un.val(),
			password : ps.val(),
			vc : vc.val(),
			go : go.val()
		};
		var submiting = $('.loginsubmiting').show();
		debugger;
		$(this).button('loading');
		$.ajax({
			url : 'login',
			type : 'post',
			data : data,
			dataType : 'json',
			success : function(result) {
				if (result.result) {
					$(this).button('reset');
					document.location = './';
					// document.location = result.msg;
				} else {
					$('#error').html(result.msg);
					submiting.hide();
				}
			},
			error : function(response, b, c) {
				$('#error').html(response.responseText);
				submiting.hide();
			}
		});
	});
});