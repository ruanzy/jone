define(['util'], function(util){
	var obj = {
		init: function(){
			var me = this;
			//util.render('html/nav.html', 'app-header', {username: sessionStorage.username});
			util.render('html/sidebar2.html', 'sidebar');
			$('#menu1').metisMenu();
			$('#menu1').slimScroll({ width: '220px', height: '100%'});
			$('#menu1 a[data-url]').on('click', function(){	
				$('a.active').removeClass('active');
				$(this).addClass('active');
				var url = $(this).attr('data-url');
				if(url){
					$("body").scrollTop(0);
					require([url], function(o) {
						o.init();
						
					});
				}
			});
			$('#menu1 a:first').trigger('click');
	       // me.initMenu();  
	        //me.layout();
	        $('.dropdown-toggle').dropdown();
	        $("#sidebarsw").on('click', function(){
	        	$('body').toggleClass('sidebar-collapse');
			});
	        $("#setting").on('click', function(){me.setting();});
	        $("#exit").on('click', function(){me.exit();});
		},
		initMenu: function(){
			$.navigation = $('.sidebar > ul');
			  // Add class .active to current link
			  $.navigation.on('click', '.nav-dropdown-items a', function(){
				$('.nav-link.active').removeClass('active');
			      $(this).addClass('active');
			  });
			  
			  
			  $.navigation.on('click', 'a', function(e){

				   
				      e.preventDefault();

				    if ($(this).hasClass('nav-dropdown-toggle')) {
						$(this).parent().siblings('.nav-dropdown.open').removeClass('open');
				      $(this).parent().toggleClass('open');
				    }else{
					$('.nav-link.active').removeClass('active');
				      $(this).addClass('active');
						var path = $(this).attr('path');
						$("body").scrollTop(0);
						require([path], function(o) {
							o.init();
							
						});
					}

				  });
			  
			  $('.sidebar .nav-link:first').trigger('click');
		},
		layout: function(){
			function a(){
				//初始化layout_center的高度
				var layout_center_h = $(document).height() - 90;
				//设置最小高度是200
				if( layout_center_h < 200 ){
					layout_center_h = 200;
				}
				$("#container").height(layout_center_h);
			}
	        $(window).resize(function(){
	            a();
	        });
	        a();
		},
		setting: function(){
			var me = this;
			var html = util.tpl('html/setting/choosesetting.html');
        	var d = null;
        	d = dialog({
        	    title: util.i18n('SETTING'),
        	    content: html,
        	    onshow: function () {
        	    	$(".settingmenu.languagesetting").on("click", function(){
        	    		me.languagesetting();
        	    		d.close().remove();
        	    	});
        	    	$(".settingmenu.passwordsetting").on("click", function(){
        	    		d.close().remove();
        	    		me.passwordsetting();
        	    	});
        	    }
        	});
        	d.showModal();
		},
		languagesetting: function(){
			var html = util.tpl('html/setting/language.html');
        	var d = dialog({
        	    title: util.i18n('LANGUAGE'),
        	    content: html,
        	    okValue: util.i18n('DONE'),
        	    ok: function () {
					var lang = $("input[name=language]:checked").val();
					util.lang(lang);
					location.reload();
        	    },
        	    cancelValue: util.i18n('CANCEL'),
        	    cancel: $.noop,
        	    onshow: function () {
        	    	var lang = localStorage.getItem('lang') || 'zh';
        	    	$("input[name=language][value=" + lang + "]").attr("checked",'checked'); 
        	    }
        	});
        	d.showModal();
		},
		passwordsetting: function(){
			var me = this;
			var html = util.tpl('html/setting/changepassword.html');
        	var d = dialog({
        	    title: util.i18n('MODIFYPASSWORD'),
        	    content: html,
        	    okValue: util.i18n('DONE'),
        	    ok: function () {
        	    	var flag = me.updatepwdvalid();
        	    	if(flag){
        	    		me.modifypwd(this);
        	    	}
        	    	return false;
        	    },
        	    cancelValue: util.i18n('CANCEL'),
        	    cancel: $.noop
        	});
        	d.showModal();
		},
		modifypwd: function(d){
			var url = "user/modifyPwd";
			$('#changepwdform').ajaxSubmit({
				url: url,
				type: 'post',  
				dataType: 'json',
				data: {username: sessionStorage.username},
				success: function(data){
					if(data == 0){
						d.close();
						dialog().title('提示').content('密码修改成功').showModal();
					}else if(data == 10000){
						$('#errormsg').html('原密码不正确');
						$('#error').show();
					}
				},
				error: function(){
					d.close();
					dialog().title('提示').content('密码修改失败').showModal();
				}
			});
		},
		updatepwdvalid: function(){
			jQuery.validator.addMethod("pattern", function (value, element, params) {
			    if (!params.test(value)) {
			        return false;
			    }
			    return true;
			});
			var v = jQuery("#changepwdform").validate({
				onkeyup:false,
				onfocusout:false,
				rules: {
					oldpassword: {
						required: true
					},
					newpassword: {
						required: true,
						minlength: 6,
						pattern: /^[a-zA-Z0-9_]{6,12}$/
					},
					repassword: {
						required: true,
						minlength: 6,
						equalTo: "#newpassword"
					}
				},
				messages: {
					oldpassword: {
						required: "原密码不能为空"
					},
					newpassword: {
						required: "新密码不能为空",
						minlength: "新密码不能少于6个字符组成",
						pattern: '新密码只能是字母、数字、下划线组成, 且只能6-12个字符'
					},
					repassword: {
						required: "确认密码不能为空",
						minlength: "确认密码不能少于6个字符组成",
						equalTo: "两次输入密码不一致"
					}
				},
				showErrors:function(errorMap, errorList) {
					if(errorList.length > 0){
						var error = errorList[0].message;
						$('#errormsg').html(error);
						$('#error').show();
					}else{
						$('#errormsg').html('');
						$('#error').hide();
					}
				}
			});
			var mm = v.form();
			return mm;
		},
		exit: function(){
        	$.confirm(util.i18n('LOGOUT_CONFIRM'), function(v){
        		if(v){
        			sessionStorage["username"] = '';
        			sessionStorage.token = '';
        			location.href = 'login.html';
        		}
        	});
		}
	};
	return obj;
});