$JONE = {

};
$M1 = {
		init: function(){
			util.render('html/1.html', 'rui-content');
			$('#loading').hide();
			$('pre code').each(function(i, block) {
				hljs.highlightBlock(block);
			});
		}	
	};
$JONE['M1'] = $M1;