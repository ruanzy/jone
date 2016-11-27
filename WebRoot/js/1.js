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
$M2 = {
		init: function(){
			util.render('html/2.html', 'rui-content');
			$('#loading').hide();
			$('pre code').each(function(i, block) {
				hljs.highlightBlock(block);
			});
		}	
	};
$M3 = {
		init: function(){
			util.render('html/3.html', 'rui-content');
			$('#loading').hide();
			$('pre code').each(function(i, block) {
				hljs.highlightBlock(block);
			});
		}	
	};
$M4 = {
		init: function(){
			util.render('html/4.html', 'rui-content');
			$('#loading').hide();
			$('pre code').each(function(i, block) {
				hljs.highlightBlock(block);
			});
		}	
	};
$M5 = {
		init: function(){
			util.render('html/5.html', 'rui-content');
			$('#loading').hide();
			$('pre code').each(function(i, block) {
				hljs.highlightBlock(block);
			});
		}	
	};
$M6 = {
		init: function(){
			util.render('html/6.html', 'rui-content');
			$('#loading').hide();
			$('pre code').each(function(i, block) {
				hljs.highlightBlock(block);
			});
		}	
	};
$M7 = {
		init: function(){
			util.render('html/7.html', 'rui-content');
			$('#loading').hide();
			$('pre code').each(function(i, block) {
				hljs.highlightBlock(block);
			});
		}	
	};
$JONE['M1'] = $M1;
$JONE['M2'] = $M2;
$JONE['M3'] = $M3;
$JONE['M4'] = $M4;
$JONE['M5'] = $M5;
$JONE['M6'] = $M6;
$JONE['M7'] = $M7;