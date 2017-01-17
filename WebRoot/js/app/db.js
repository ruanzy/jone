define(['util', 'dialog'], function(util){
	var obj = {
		init: function(){
			var me = this;
			util.render('html/db.html', 'rui-content');
			$('body').addClass('hide-aside');
			var languageTools = ace.require("ace/ext/language_tools");
			 var editor = ace.edit("editor");
			 editor.setFontSize(14);
			    editor.session.setMode("ace/mode/sql");
			    editor.setTheme("ace/theme/chrome");
			    // enable autocompletion and snippets
			    editor.setOptions({
			        enableBasicAutocompletion: true,
			        enableSnippets: true,
			        enableLiveAutocompletion: true
			    });
				editor.getSession().setUseWrapMode(false);
				editor.setShowPrintMargin(false);
				//editor.setReadOnly(true);
				languageTools.addCompleter({
					getCompletions: function(editor, session, pos, prefix, callback) {
						callback(null,  [
							{
								name : "test",
								value : "test",
								caption: "test",
								meta: "test",
								type: "local",
								score : 1000 // 让test排在最上面
							}
						]);
					}
				});
			$('#exe').on('click', function(){
				me.exe();
			});
		},
		exe: function(){
			 var editor = ace.edit("editor");
			 var sql = editor.session.getTextRange(editor.getSelectionRange());
			 sql = $.trim(sql);
			 if(sql){
				 var data = {sql: sql, page: 1, pagesize: 10};
				 $.ajax({
					 url: 'db/exe',
					 type: 'post',
					 data: data,
					 dataType: 'json',
					 success: function(result){
						 $('#result').html(JSON.stringify(result, null, 2));
						 $.noty('执行成功!');
					 }
				 });
			 }else{
				 $.alert('请选择要执行的sql');
			 }
		}
	};
	return obj;
});