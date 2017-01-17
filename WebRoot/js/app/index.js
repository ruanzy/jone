define(['util'], function(util){
	var obj = {
		init: function(){
			util.render('html/header.html', 'header');
			util.render('html/footer.html', 'footer');
			this.bindEvent();
		},
		bindEvent: function(){
			$('#m_doc').on('click', function(){
				util.selectMenu('#m_doc');
				require(['app/doc'], function(doc) {
					doc.init();
				});
			});
			$('#m_apitest').on('click', function(){
				util.selectMenu('#m_apitest');
				require(['app/apitest'], function(apitest) {
					apitest.init();
				});
			});
			$('#m_rui').on('click', function(){
				util.selectMenu('#m_rui');
				require(['app/rui'], function(rui) {
					rui.init();
				});
			});
			$('#m_db').on('click', function(){
				util.selectMenu('#m_db');
				require(['app/db'], function(db) {
					db.init();
				});
			});
			var current = sessionStorage.getItem('current');
			if(current){
				$(current).trigger('click');
			}else{
				$('#m_doc').trigger('click');
			}
			$('#about').on('click', function(){
				$.noty('增加成功!');
			});
		}
	};
	return obj;
});