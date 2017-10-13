define(['util', 'grid'], function(util){
	var obj = {
		init: function(){
			util.render('html/charts/charts.html', 'right');
			var els = $('#chart_menu li');
			$('#chart_menu').on('click', function(e){
				var el = $(e.target);
				var id = el.attr('id');
				els.removeClass('left_select_nav');
				if (id == 'databaseChart') {
					el.addClass('left_select_nav');
					require([ 'app/charts/databaseChart' ], function(user) {
						user.init();
					});
				}
				if (id == 'restfulChart') {
					el.addClass('left_select_nav');
					require([ 'app/charts/restfulChart' ], function(role) {
						role.init();
					});
				}
			});
			$('#databaseChart').trigger('click');
		}
	};
	return obj;
});