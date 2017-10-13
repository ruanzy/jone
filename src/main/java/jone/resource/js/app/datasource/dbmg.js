define(['util'], function(util){
	var obj = {
		init: function(){
			util.render('html/datasource/dbmg.html', 'right');
			var els = $('#db_menu li');
			$('#db_menu').on('click', function(e){
				var el = $(e.target);
				var id = el.attr('id');
				els.removeClass('left_select_nav');
			
				if (id == 'database_l') {
				
					el.addClass('left_select_nav');
					require([ 'app/datasource/database' ], function(database) {
						database.init();
					});
				}
				if (id == 'database_source_l') {
					el.addClass('left_select_nav');
					require([ 'app/datasource/datasource' ], function(datasource) {
						datasource.init();
					});
				}
			});
			$('#database_l').trigger('click');
		}
	};
	return obj;
});