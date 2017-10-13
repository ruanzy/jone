define(['util'], function(util){
	var obj = {
		init: function(){
			util.render('html/sys/sys.html', 'right');
			var els = $('#sys_menu li');
			$('#sys_menu').on('click', function(e){
				var el = $(e.target);
				var id = el.attr('id');
				els.removeClass('left_select_nav');
				if (id == 'usermgr') {
					el.addClass('left_select_nav');
					require([ 'app/sys/user' ], function(user) {
						user.init();
					});
				}
				if (id == 'rolemgr') {
					el.addClass('left_select_nav');
					require([ 'app/sys/role' ], function(role) {
						role.init();
					});
				}
			});
			$('#usermgr').trigger('click');
		}
	};
	return obj;
});