var langs = {"zh":"Chinese", "en" : "English"};
var lang = 'zh';
if ($.cookie('lang')) {
	lang = $.cookie('lang');
}
$('#lang').html(langs[lang]);
$('#info').click(function() {
	var dialog = $.dialog({
		//title:'个人资料',
		width : 350,
		//url:'view/income/list.html'
		url : 'ftl/userinfo.html',
		buttons : [ {
			text : 'OK',
			cls : 'btn-primary',
			action : function(d) {

			}
		}, {
			text : 'Close',
			cls : 'btn-default',
			action : function(d) {
				dialog.close();
			}

		} ],
		onShow : function() {
			$('#sex').MultiRadio();
		}
	});
});
$('#set').click(function() {
	var dialog = $.dialog({
		title : '设置',
		url : 'view/user/set.html',
		buttons : [ {
			text : 'OK',
			cls : 'btn-primary',
			action : function(d) {

			}
		}, {
			text : 'Close',
			cls : 'btn-default',
			action : function(d) {
				dialog.close();
			}

		} ],
		onShow : function() {

		}
	});
});
$('#logout').click(function() {
	document.location = 'logout';
});
$('#theme').SideDown({
	width : 'auto',
	url : 'theme.html'
});
$('#toggle').click(function() {
	var sb = $('#sidebar');
	if (sb.is(":visible")) {
		$('#sidebar').hide();
		$('#content').css('margin-left', 0);
		$('#pill').css('left', 0);
	} else {
		$('#sidebar').show();
		var sw = $('#sidebar').outerWidth();
		$('#content').css('margin-left', sw);
		$('#pill').css('left', sw);
	}
});

function changeLang(lang){
	$('#lang').html(langs[lang]);
	$.cookie('lang', lang, {
		path : '/',
		expires : 10
	});
	document.location = '';
}