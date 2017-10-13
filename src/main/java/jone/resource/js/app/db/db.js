define(['util'], function(util){
	var obj = {
		init: function(){
			var me = this;
			util.render('html/db.html', 'container');
			var setting = {
					data: {
						simpleData: {
							enable: true,
							pIdKey: "pid"
						}
					},
					async: {
						enable: true,
						url: 'sql/tree',
						autoParam: ["id", "level"]
					},
					callback: {
						onDblClick: function(event, treeId, treeNode) {
							if(treeNode.level == 2){
								var t = treeNode.name;
								var db = treeNode.getParentNode().getParentNode().name;
								addTab(db, t);
							}
						}
					}
				};

				var zNodes =[
					{ id:1, pId:0, name:"H2(d:/docdb/docdb)", open:true},
					{ id:11, pId:1, name:"表"},
					{ id:111, pId:11, name:"USERS"},
					{ id:12, pId:1, name:"视图"}
				];

				$.fn.zTree.init($("#treeDemo"), setting);
				$("#dbtree").resizable({
					handles: "e",
					minWidth: 200,
					maxWidth: 400,
					resize: function(event, ui) {
						var w = ui.size.width;
						ui.element.parent('.container1').css('marginLeft', w);
					}
				});
				var tabs = $("#tabs").tabs({
					active: 1,  
					load: function (event, ui) {
						//$("#result").resizable({autoHide: true});
						var editor = ace.edit("editor");
						editor.setFontSize(14);
						editor.setShowPrintMargin(false);
						editor.session.setMode("ace/mode/sql");
						editor.setTheme("ace/theme/chrome");
						editor.setValue("create table users(id int, name varchar(2))");
						$("#editorwrapper").resizable({
							handles: "s",
							minHeight: 100
							//alsoResize: "#result"
						});
						$('#execsql').on('click', function(){
							var sql = editor.session.getTextRange(editor.getSelectionRange());
							sql = $.trim(sql);
							if(sql.length == 0){
								sql = editor.getValue();
							}
							if(sql.length > 0){
								me.execsql(sql);
							}else{
								alert('请输入sql');
							}
						});
						$('#ddialog').on('click', function(){
							$("#uidialog").dialog({
								width: 100,
								height: 300,
								modal: true
							});
						});
					}
				});
				function addTab(db, t) {
				      var title = t + '@' + db;
				      var exists = $("#tabs").tabs("exists", title);
				      if(exists){
				    	  $("#tabs").tabs("select", title);
				    	  return;
				      }
				      var tabContentHtml = $("<div>");
				      var opt = {
				          title: title,
				          padding: '0px',
				          content: tabContentHtml,
				          closable: true
				      };
				      $("#tabs").tabs("add", opt);
				      var data = {table : t};
						$.ajax({
							url: 'sql/getTableData',
							type: 'post',
							data: data,
							dataType: 'json',
							success: function(result){
				                if(result){
				                	var r = result[0];
				                	var columns = [];
				                	var fields = [];
				                	for(var k in r){
				                		fields.push(k);
				                	}
				                	for(var i = 0; i < fields.length; i++){
				                		var col = { header: fields[i], field: fields[i]};
				                		columns.push(col);
				                	}
				                	tabContentHtml.grid({
				                		data : result,
				                		pagesize: -1,
				                		columns: columns
				                	});
				                }
							}
						});
				    }
		},
		execsql: function(sql){
			var data = {sql : sql};
			$.ajax({
				url: 'sql/exec',
				type: 'post',
				data: data,
				dataType: 'json',
				success: function(result){
					//$('#result').html(JSON.stringify(result, null, 2));
					var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
					var parentZNode = zTreeObj.getNodeByParam("id", 2, null);
	                zTreeObj.reAsyncChildNodes(parentZNode, "refresh", false); 
	                zTreeObj.expandNode(parentZNode, true, false);
	                if(result){
	                	var r = result[0];
	                	var columns = [];
	                	var fields = [];
	                	for(var k in r){
	                		fields.push(k);
	                	}
	                	for(var i = 0; i < fields.length; i++){
	                		var col = { header: fields[i], field: fields[i]};
	                		columns.push(col);
	                	}
	                	$('#result').grid({
	                		data : result,
	                		pagesize: -1,
	                		columns: columns
	                	});
	                }
				}
			});
		}
	};
	return obj;
});