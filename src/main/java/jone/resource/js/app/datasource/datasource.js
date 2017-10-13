define(
		[ 'util', 'grid', 'ztree' ],
		function(util) {
			var cols;
			var database_id;
			var d_name;
			var main = null;
			var add=null;
			var slave = [];
			 function getFieldName(node,col){
				 var name="";
				 if(node.aliasname==node.name){
					 name=node.schema;
				 	if(name&&name!=null){
				 		name+=".";
				 	}else{
				 		name="";
				 	}
				 }
				 name+=node.aliasname+"."+col.name;
				 return name;
			 }
			 function getTableName(node){
				 var name="";
				 if(node.aliasname==node.name){
					 name=node.schema;
				 	if(name&&name!=null){
				 		name+=".";
				 	}else{
				 		name="";
				 	}
				 }
				 name+=node.aliasname;
				 return name;
			 }
			 function getTableName2(node){
				 var name="";
				 if(node.schema){
				 name=node.schema;
				 	if(name&&name!=null){
				 		name+=".";
				 	}else{
				 		name="";
				 	}
				 }
				 name+=node.name;
				 return name;
			 }
			 function getTableName3(node){
				 var name="";
				 if(node.schema&&node.schema!=null){
				  name=(node.schema).formatName();
				 	if(name&&name!=null){
				 		name+=".";
				 	}else{
				 		name="";
				 	}
				 }
				 name+=(node.name).formatName();
				 return name;
			 }
			 function getTableName4(node){
				 var name="";
				 if(node.schema&&node.schema!=null){
				 name=(node.schema).formatName();	
				 	name+=".";
				 }
				 name+=(node.name).formatName();
				 return name;
			 }
			 function getFieldName3(node,col){
				 var name="";
				 if(node.aliasname==node.name){
					 if(node.schema&&node.schema!=null){
						 name=(node.schema).formatName();
					 }
				 	if(name&&name!=null){
				 		name+=".";
				 	}else{
				 		name="";
				 	}
				 }
				 name+=(node.aliasname).formatName()+"."+(col.name).formatName();
				 return name;
			 }
			 function getFieldName2(node,col){	 
				 var name="";
				 if(node.name!=node.aliasname){
					 return (node.aliasname).formatName();
				 }
				 if(node.aliasname==node.name){
					 if(node.schema&&node.schema!=null){
						 name=(node.schema).formatName()
					 };
				 	if(name&&name!=null){
				 		name+=".";
				 	}else{
				 		name="";
				 	}
				 }
				 name+=(node.aliasname).formatName()+"."+(col.name).formatName();
				 return name;
			 }
			 var database_type;
			 String.prototype.formatName =function(){
		
				if(database_type=="mysql"){
				 return "`"+this.replace("`", "``")+"`";
				}else{
					return "\""+this.replace("\"", "\"\"")+"\"";
				}
			 }
			function addCol(data) {
				var col = "<tr><td>"
						+getFieldName(data.node,data.col)
						+ "</td><td>"
						+ "<select style='width: 90%; border-left: 0px; border-top: 0px; border-right: 0px; border-bottom: 1px;'>"
						+ "<option></option><option>max</option><option>min</option><option>count</option><option>length</option>"
						+ "<option>avg</option></select></td><td><input type='checkbox' value=''></td><td><input type='text' value=''"
						+ "style='border-left: 0px; border-top: 0px; border-right: 0px; border-bottom: 1px'></td></tr>";
				var col=$(col);
				col.data("data",data);
				$(".condtab_field>div>table>tbody").append(col);
				col.find("select").bind("change",function(){
					obj.getSQLText();
					return false;
				})
				col.find("input[type='text']").bind("keyup",function(){
					obj.getSQLText();
					return false;
				})
				col.find("input[type='checkbox']").bind("click",function(){
					obj.getSQLText();
				})
				obj.getSQLText();

			}
			function removeCol(data) {
				var d = $(".condtab_field>div>table>tbody>tr");
				d.each(function(i,e){
					var data2=$(e).data("data");
					if(data2.col==data.col){
						$(e).remove();
					}	
				})
				
				obj.getSQLText();

			}
			function addGroup() {
				var tr = $("<tr><td><input type='text' value='' style='border-left: 0px; border-top: 0px; border-right: 0px; border-bottom: 1px' readonly>"
						+ "<a href='#' class='addfield' style='float:right'>...</a></td><td><a class='deleterow'><i class='fa fa-times-circle'></i>"+util.i18n('DELETE')+"</a></td></tr>");

				$(".condtab_group>.contenttable>table>tbody").append(tr);
				tr.find("a.deleterow").bind("click", function() {
					deleterow(this);
					obj.getSQLText();
				});
				var cols=getAllCols();
				tr.find("a.addfield").bind("click", function() {
					var html = util.tpl('html/datasource/field.html', {
						cols : cols
					});
					var methis = $(this);
					var d = dialog({
						content : html,
						padding : 0,
						okValue : util.i18n('DONE'),
						ok : function() {
							var input2 = $("#col>p>input[name='col']:checked");
							var tr = $(methis).parent();
							var input = tr.find("input:first");
							input.val(input2.val());
							var data=input2.data("data");
							input.data("data",data);
							obj.getSQLText();
						},
						cancelValue : util.i18n('CANCEL'),
						cancel : $.noop,
						onshow : function() {

							var input2 = $("#col>p>input[name='col']");
							input2.each(function(i,e){
								$(e).data("data",cols[i]);
							})
						
						}
					});
					d.showModal();
				})
				obj.getSQLText();
			}
			//增加一个排序
			function addOrderBy() {

				var tr = $("<tr><td><input type='text' value='' style='border-left: 0px; border-top: 0px; border-right: 0px; border-bottom: 1px' readonly/>"
						+ "<a href='#' class='addfield' style='float:right'>...</a></td><td><a class='deleterow'><i class='fa fa-times-circle'></i>"+util.i18n('DELETE')+"</a></td></tr>");
				$(".contab_orderby>.contenttable>table>tbody").append(tr);
				tr.find("a.deleterow").bind("click", function() {//排序删除
					deleterow(this);
					obj.getSQLText();
				});
				var cols=getAllCols();
				tr.find("a.addfield").bind("click", function() {
					var html = util.tpl('html/datasource/field.html', {
						cols : cols
					});
					var methis = $(this);
					var d = dialog({
						content : html,
						padding : 0,
						okValue : util.i18n('DONE'),
						ok : function() {
							var input2 = $("#col>p>input[name='col']:checked");
							var tr = $(methis).parent();
							var input = tr.find("input");
							input.val(input2.val());
							input.data("data",input2.data("data"));
							obj.getSQLText();
						},
						cancelValue : util.i18n('CANCEL'),
						cancel : $.noop,
						onshow : function() {

							var input2 = $("#col>p>input[name='col']");
							input2.each(function(i,e){
								$(e).data("data",cols[i]);
							})
						
						}
					});
					d.showModal();
				})
				obj.getSQLText();
			}
			function deleterow(o) {
				$(o).parent().parent().remove();
				obj.getSQLText();
			}
			function addCondition() {
				var tr = $("<tr><td><input type='text' value='' style='border-left: 0px; border-top: 0px; border-right: 0px; border-bottom: 1px' /><a href='#' class='addfield' style='float:right'>...</a></td> </td><td>"
						+ "<select style='width: 90%; border-left: 0px; border-top: 0px; border-right: 0px; border-bottom: 1px;'><option>=</option><option>&gt;</option><option>&gt;=</option>"
						+ "<option>&lt;</option><option>&lt;=</option></select></td><td>"
						+ "<input type='text' class='con_val' value='' style='border-left: 0px; border-top: 0px; border-right: 0px; border-bottom: 1px'/></td>"
						+ "<td><a class='deleterow'><i class='fa fa-times-circle'></i>"+util.i18n('DELETE')+"</a></td></tr>");

				$(".condtab_condition>.contenttable>table>tbody").append(tr);
				tr.find("a.deleterow").bind("click", function() {
					deleterow(this);
					return false;
				});
				tr.find("td>select").bind("change",function(){
					obj.getSQLText();
					return false;
				})
				tr.find(".con_val").bind("keyup",function(){
					obj.getSQLText();
					return false;
				})
				tr.find("a.addfield").bind("click", function() {
					var cols=getAllCols();
					var html = util.tpl('html/datasource/field.html', {
						cols : cols
					});
					var methis = $(this);
					var d = dialog({
						content : html,
						padding : 0,
						okValue : util.i18n('DONE'),
						ok : function() {
							var input2 = $("#col>p>input[name='col']:checked");
							var tr = $(methis).parent();
							var input = tr.find("input");
							input.val(input2.val());
							var data=input2.data("data");
							input.data("data",data);
							obj.getSQLText();
						},
						cancelValue : util.i18n('CANCEL'),
						cancel : $.noop,
						onshow : function() {
							var input2 = $("#col>p>input[name='col']");
							input2.each(function(i,e){
								$(e).data("data",cols[i]);
							})
						}
					});
					d.showModal();
				})
				obj.getSQLText();
			}
			function getAllCols() {
				var cols = [];
				$("#sourcetable_charts>div ").each(function(i, e) {
					var node = $(e).data("node")
					var columns = $(e).find(".col");
					var aliasname=getTableName(node);
					columns.each(function(i,e){
						var column=$(e).data("column");
						var colname =getFieldName(node,column);
						cols.push({name:colname,node:node,col:column});
					});
				})
				return cols;
			}

			var obj = {
				init : function() {
					var me2 = this;
					util.render('html/datasource/datasource.html', 'sys_main');
					$('#restfulChart').on('click',
							function() {
								util.selectMenu('#datasource');
								require([ 'app/datasource/database' ],
										function(datasoure) {
											database.init();
										});
							});
					$('#restfulChart').on(
							'click',
							function() {
								util.selectMenu('#datasource');
								require([ 'app/datasource/database' ],
										function(datasoure) {
											database.init();
										});
							});
					$('#datalist').grid({
						url : 'DatabaseSource/list',
						sm : "m",
						columns : [ {
							header : util.i18n('SOURCENAME'),
							field : 'name'
						}, {
							header : util.i18n('DATABASE_NAME'),
							field : 'database_name'
						}, {
							header : util.i18n('SQL'),
							field : 'source_sql'
						}, {
							header : util.i18n('CREATE_USER'),
							field : 'create_user'
						}, {
							header : util.i18n('CREATE_TIME'),
							field : 'create_time',

							align : 'center'
						}
						]
					});

					var setting = {
						edit : {
							enable : true,
							showRemoveBtn : false,
							showRenameBtn : false,
							drag : {}
						},
						data : {
							simpleData : {
								enable : true
							}
						},
						async : {
							enable : true,
							url : "database/getTree",
							autoParam : [ "name", "schema", "object_type", "id" ],
							otherParam : {
								"database_id" : function() {
									return database_id
								}
							}
						// dataFilter: filter
						},
						data : {
							keep : {
								parent : true,
								leaf : true
							},
							simpleData : {
								enable : true
							}
						},
						callback : {
							onDrop : zTreeOnDrop
						// onDragMove: MoveTest.dragMove,
						// onMouseUp: MoveTest.dom2Tree
						},
						view : {
							selectedMulti : false
						}
					};

					var zNodes =null;

					function zTreeOnDrop(event, treeId, treeNodes, targetNode,
							moveType, isCopy) {
						var node = jQuery.extend(true, {}, treeNodes[0]);
						if (!node.object_type == "table"||!node.object_type == "view") {
							return;
						}
						$.ajax({	url : "database/getTree",
									type : 'post',
									dataType : 'json',
									data : {
										name : node.name,
										schema : node.schema,
										object_type : node.object_type,
										id : node.id,
										database_id : database_id
									},
									success : function(data) {
										var columns = data.columns;
										var c = 'tbchart_box_main';
										var main = $('#sourcetable_charts .tbchart_box_main');
										var fslave = $('#sourcetable_charts .tbchart_box_slave.first_slave');
										if (main.length == 0) {
											
										} else if (fslave.length == 0) {
											c = 'tbchart_box_slave first_slave';
										} else {
											c = 'tbchart_box_slave';
										}
										var html = "<div class='" + c + "'>";
										if (main.length == 0) {
										} else {
											html += "	<div class='tbchart_relations' ><div class='ass01' >"+util.i18n('RELATIVE')+"<ul class='ass_list' style='display:none'>"
													+ "<li>"+util.i18n('INNER')+"</li><li>"+util.i18n('LEFT')+"</li><li>"+util.i18n('RIGHT')+"</li><li>"+util.i18n('OUTTER')+"</li></ul>";
											html += "</div></div>"
											html += "<div class='tbchart_box_slave_main'>";
//											main.find('.tbchart_box').append("<style>.tbchart_box::after{display:block}</style>");
										}
										if (main.length == 0) {
											html += "<div class='tbchart_box noslave'>";
										}else{
											main.find(".tbchart_box").prop("class","tbchart_box");
											html += "<div class='tbchart_box'>";
										}
										html += "<div class='data_tbchart_title'><input type='text' style='background-color: #f6f7f9;width:100px;border-left: 0px; border-top: 0px; border-right: 0px; border-bottom: 1px'value='"
												+ getTableName2(node) + "' readonly='readonly'/><span class='tb_close'>X</span></div>";
										html += "<div class='data_tbchart'>";
										html += "<table><thead><tr><td width='18px'><input type='checkbox' class='all'></td><td>"+util.i18n('FIELD')+"</td></tr></thead><tbody>";
										var b=[];
										for ( var a in columns) {
											html += "<tr><td><input type='checkbox' class='col'></td><td>"
													+ a + "</td></tr>";
											b.push(columns[a]);
										}
										html += "</tbody></table>";

										if (main.length == 0) {
										} else {
											html += "</div>";
										}

										html += "</div></div>";
										var html_j = $(html);
										html_j.find("input.col").each(function(i,e){
											$(this).data("column",b[i]);
										});
										html_j.find("input[type='text']").click(function(){
											var p=$(this).parent().parent().parent();
											if(p.prop("class")=="tbchart_box_slave_main"){
												p=p.parent();
											}
											var node=p.data("node");
											var aliasname=node.aliasname;
											if(aliasname){
												$(this).val(aliasname);
											}else{
												$(this).val(node.name);
											} 
											 $(this).removeAttr("readonly");
											 return false;
										})
										//修改别名
										html_j.find("input[type='text']").keyup(function(e){
											if($(this).prop("readonly")!="readonly"){
											if(e.keyCode==13){
												$(this).prop("readonly","readonly");
												var p=$(this).parent().parent().parent();
												if(p.prop("class")=="tbchart_box_slave_main"){
													p=p.parent();
												}
												var node=p.data("node");
												var aliasname=$(this).val();
												var node=p.data("node");
												node.aliasname=aliasname;
										
												if(aliasname!=node.name){
													$(this).val(aliasname+"("+getTableName2(node)+")");
												}	
												obj.repaint();
											}
											}
											return false;
										}); 
										html_j.find(".tb_close").bind("click",function(){
											var p=$(this).parent().parent().parent();
											var c=p.prop("class");
											if(c=="tbchart_box_main"){
												obj.removeAll();
												p.remove();
												$('#sourcetable_charts .tbchart_box_slave').remove();	
												obj.getSQLText();
												
											} else if(c=="tbchart_box_slave_main") {
												 p=p.parent();
												 c=p.prop("class");
											     if(c=="tbchart_box_slave first_slave"){
											    var len=$('#sourcetable_charts .tbchart_box_slave').length;
											    if(len==1){
													$(".tbchart_box_main").find(".tbchart_box").prop("class","tbchart_box noslave");
												}
											    else if(len>=2){
													$($('#sourcetable_charts .tbchart_box_slave')[1]).prop("class","tbchart_box_slave first_slave");
												}
												obj.removeCols(p.data("columns"));
												p.remove();
												obj.getSQLText();
											}else{
												p.remove();
												obj.getSQLText();
											}
											}
											 
										})
										html_j.find(".tbchart_relations").bind(
												"click", function() {
													html_j.find("ul").toggle();
												})
										node.aliasname=node.name;
										html_j.data("node", node);
							
										html_j.data("columns", columns);

										html_j.find("ul li").bind(
												"click",
												function() {
													var node = html_j
															.data("node");
													var main_node = main
															.data("node");

													return false;
												})
										var all = html_j.find("input.all");
										var cols = html_j.find("input.col");
										all.bind("click", function() {// 全选

											if (this.checked) {
												cols.prop("checked", true);
											} else {
												cols.prop("checked", false);
											}
											// return false;
										})
										html_j.find(".col").bind(
												"click",
												function() {
													var p=$(this).parent().parent().parent().parent().parent().parent().parent();
													if(p.prop("class")=="tbchart_box_slave_main"){
														p=p.parent();
													}
													var node=p.data("node");
													var aliasname=node.aliasname;
													if (this.checked) {
														addCol({node:node,col:$(this).data("column")});
													} else {
														removeCol({node:node,col:$(this).data("column")});
													}
													// return false;
												})
										//全选全不选		
										html_j.find(".all").bind(
												"click",
												function() {
													var p=$(this).parent().parent().parent().parent().parent().parent().parent();
													if(p.prop("class")=="tbchart_box_slave_main"){
														p=p.parent();
													}
													var node=p.data("node");
													var aliasname=node.aliasname;
													var me=this
													p.find(".col").each(function(i,e){
														if (me.checked) {
															$(e).prop("checked","checked");
															addCol({node:node,col:$(e).data("column")});
														} else {
															$(e).removeAttr("checked");
															removeCol({node:node,col:$(e).data("column")});
														}	
													});
													
													// return false;
												})
										$("#sourcetable_charts").append(html_j);
										$(html_j).find("ul.ass_list li")
												.bind(
														"click",
														function() {
															var slave = $(this).parent().parent().parent().parent();
															var main = $(".tbchart_box_main");
															var slave_columns = slave
																	.data("columns");
															var main_columns = main
																	.data("columns");
															var slave_node = slave
																	.data("node");
															var main_node = main
																	.data("node");
															var aliasname=getTableName(main_node);
															var s_name=getTableName(slave_node);
															var html = "<div class='ass_list_table2'><table><thead><tr><td width='300px'>"
																	+ aliasname
																	+ "</td><td width='40px'>"+util.i18n('CONDITION')+"</td><td width='300px'>"
																	+ s_name
																	+ "</td><td width='40px'>"+util.i18n('OPERATE')+"</td></tr></thead>"
																	+ "<tbody>";
															var cons = slave
																	.data("condition");
															if (cons) {
																for ( var a2 in cons) {
																	html += "<tr>";
																	html += "<td><select>";
																	for ( var a in main_columns) {
																		if (a == cons[a2][0]) {
																			html += "<option selected>"+ a+ "</option>"
																		} else {
																			html += "<option>"+ a+ "</option>"
																		}
																	}
																	html += "</td></select>";
																	html += "<td>";
																	html += "=";
																	html += "</td>";
																	html += "<td><select>";
																	for ( var a in slave_columns) {
																		if (a == cons[a2][1]) {
																			html += "<option selected>"+ a+ "</option>"
																		} else {
																			html += "<option>"+ a+ "</option>"
																		}
																	}
																	html += "</select></td><td><a class='deleterow'><i class='fa fa-times-circle'></i>"+util.i18n('DELETE')+"</a></td></tr>";
																}
															}
															html += "</tbody></table></div>";
															$(this).css("background-color","#eee");
															var text = $(this).text();
															if (text == util.i18n('LEFT')) {
																$(this).parent().parent().attr("class","ass02");
															} else if (text == util.i18n('RIGHT')) {
																$(this).parent().parent().attr("class","ass03");
															} else if (text == util.i18n('INNER')) {
																$(this).parent().parent().attr("class","ass01");
															} else if (text == util.i18n('OUTTER')) {
																$(this).parent().parent().attr("class","ass04");
															}
															$(this).siblings().css("background-color","#99bcd1");
															var li = $(this);
															var d = dialog({
																
																title :util.i18n("RELATIVE"),
																content : html,
																padding : 0,
																onshow : function() {
																	$(".ass_list_table2>table>tbody>tr").find(
																					"a.deleterow").bind(
																					"click",
																					function() {
																						deleterow(this);
																					});

																},
																button : [
																          	{
																			value : util.i18n('ADD'),
																			callback : function() {
																				var tr = $(".ass_list_table2>table>tbody");
																				var html = "<tr>";
																				html += "<td><select>";
																				for ( var a in main_columns) {
																					html += "<option>"+a+ "</option>";
																				}
																				html += "</td></select>";
																				html += "<td>";
																				html += "=";
																				html += "</td>";
																				html += "<td><select>";
																				for ( var a in slave_columns) {
																					html += "<option>"
																							+ a
																							+ "</option>";
																				}
																				html += "</select></td><td><a class='deleterow'><i class='fa fa-times-circle'></i>"+util.i18n('DELETE')+"</a></td></tr>";
																				var h = $(html);
																				tr.append(h);
																				h.find("a.deleterow").bind("click",
																								function() {
																									deleterow(this);
																								});

																				return false;
																			},
																			autofocus : true
																		},

																		{
																			value : util.i18n('DONE'),
																			callback : function() {
																				var tr = $(".ass_list_table2>table>tbody>tr");
																				var a = [];
																				tr.each(function(i,e) {
																							var tds = $(this).find("td>select");
																							var b = [];
																							b.push($(tds[0]).val());
																							b.push($(tds[1]).val());
																							a.push(b);
																						})
																				slave.data("condition",a);
																				obj.getSQLText();
																				return true;
																			},
																			autofocus : true
																		} ]
															});
															d.showModal();
														})
														obj.getSQLText();
									}

								});
						
						return false;
					}
					;
					$("#deletesource").on("click", function() {

						var selected = $('#datalist').grid('getSelected');

						if (selected.length == 0) {
							dialog({
								title : util.i18n('ALERT'),
								content : util.i18n('SELECT_A_LINE'),
								okValue : util.i18n('DONE'),
								ok : function(data) {
								}
							});
							return;
						}
						var ids = [];
						for ( var k in selected) {
							ids.push(selected[k].id);
						}
						me2.del(ids.join());

					});
					$("#query").on("click", function() {
						var data = {
							name : $("#name").val()
						};
						$('#datalist').grid('setQueryParam', data);
						$('#datalist').grid('reload');
					});
					$("#addsource")
							.on("click",
									function() {
										var me = this;
										var html = util
												.tpl('html/datasource/addsource.html');
										 add = dialog({
											title : util.i18n('SETTING'),
											content : html,
											padding : 0,
											onshow : function() {
												// 在弹出框里面绑定事件
										
												$.ajax({
															type : 'post',
															dataType : 'json',
															url : 'database/listAll',
															success : function(
																	data) {
																var s = "<option value=''>"+util.i18n('PLEASESELECT')+"</option>";
																var database = {};
																$.each(data,function(key,val) {
																					s += "<option database_type="+val.database_type+" value="+ val.id+ ">"+ val.name+ "</option>";
																					database[val.id] = val;
																				});
																
																$("#database_select").html(s);
																$("#database_select").bind("change",
																				function() {
																					database_id = $(this).val();
																					var me=this;
																					if ($(this).val() != "") {
//																						if (database[database_id].database_type == "mysql") {
																							$.ajax({
																										url : "database/getTree",
																										type : 'post',
																										data : {
																											database_id : database_id,
																											object_type : 'database'
																										},
																										dataType : 'json',
																										success : function(
																												result) {
																											 if(database_id){
																											 var op= $(me).find('option:selected');																										
																											 database_type= op.attr("database_type");
																											 }
																											$.fn.zTree.init($("#sourceTableTree"),setting,result);
																										}

																									});

//																						}
																						$(".addsource_maintab li,.addsource_condtab li").on("click",function() {
																											$(this).siblings().removeClass("addsource_tabactive");
																											$(this).addClass("addsource_tabactive");
																										});

																					}
																				})
															}

														});

												$("#sqltextarea")
														.on("keyup",function() {
																	$("#sqltextarea_yl").val($("#sqltextarea").val());
																	return false;
																});
												var li_2 = $(".addsource_maintab>li");
												var div_2 = $(".source_rightcontent>div");
												li_2.on("click",function() {
																	var index = li_2
																			.index($(this));
																	var e = $(div_2[index]);
																	$(this).siblings().removeClass("addsource_tabactive");
																	$(this).addClass("addsource_tabactive");
																	if (index == 1) {
																		$(".sqltextarea").focus();// 让输入框获取焦点
																	}
																	if (index == 1) {
																		$(div_2[0]).hide();
																	} else {
																		$(div_2[1]).hide();
																	}
																	e.show();
																});

												// cond tab 4的点击事件
												var li = $(".addsource_condtab li");
												var div = $(".addsource_condtab_content>div");
												li.on(	"click",function() {
																	var index = li
																			.index($(this));
																	var e = $(div[index]);
																	$(this).siblings().removeClass("addsource_tabactive");
																	$(this).addClass("addsource_tabactive");
																	e.siblings().hide();
																	e.show();

																});
												$("#addgroupby").bind("click",
														function() {
															addGroup();
														})
												$("#addcondition").bind(
														"click", function() {
															addCondition();
														})
												$("#addOrderby").bind("click",
														function() {
															addOrderBy();
														})
											},
											okValue : util.i18n("SAVE_CONFIG"),
											ok : function() {
												var database_id=$.trim($("#database_select").val());
												if(database_id==""){
													var d_success =dialog({
														title : util.i18n('ALERT'),
														content : util.i18n('DATABASE_SELECT'),
														okValue : util.i18n('DONE'),
														ok : function(data) {
														}
													});
									        		d_success.showModal();
													return false;
												}
												var sqltextarea_yl=$.trim($("#sqltextarea_yl").val());
												if(sqltextarea_yl==""){
													var d_success =dialog({
														title : util.i18n('ALERT'),
														content : util.i18n('SQL_SELECT'),
														okValue : util.i18n('DONE'),
														ok : function(data) {
														}
													});
									        		d_success.showModal();
													return false;
												}
												 d_name = dialog({
													title : util.i18n("SOURCENAME"),
													content : util.i18n("SOURCENAME")+':<input name="database_name" id="database_name"/>',
													okValue : util.i18n('DONE'),
													ok : function() {
														return me2.save();
													},
													cancelValue : util.i18n('CANCEL'),
													cancel : $.noop
												});
												d_name.showModal();
												return false;

											},
											cancelValue : util.i18n('CANCEL'),
											cancel : $.noop
										});
										add.showModal();
									});

				},
				getSQLText:function(){
					var tabletext=this.getTableText();
					if(tabletext!=null){$("#sqltextarea_yl").val("select "+this.fieldsql()+" from "+tabletext +this.wheresql()+this.groupbysql())+this.orderbysql();}
					
				},
				removeAll:function(){
						var tds=$("div.condtab_condition>.contenttable>table>tbody>tr");
						tds.remove();
						tds=$("div.condtab_group>.contenttable>table>tbody>tr");
						tds.remove();
						tds=$("div.contab_orderby>.contenttable>table>tbody>tr");
						tds.remove();
						tds=$("div.condtab_field>.contenttable>table>tbody>tr");
						tds.remove();
					
						
				},
				removeCols:function(cols){
					for(var a in cols){
						var tds=$("div.condtab_condition>.contenttable>table>tbody>tr>td:first>input");
						tds.each(function(i,e){
							var data=$(e).data("data");
							var node=data.node;
							var col=data.col;
							if(col==cols[a]){
								$(e).parent().parent().remove();
							}
						})
						tds=$("div.condtab_group>.contenttable>table>tbody>tr>td:first>input");
						tds.each(function(i,e){
							var data=$(e).data("data");
							var node=data.node;
							var col=data.col;
							if(col==cols[a]){
								$(e).parent().parent().remove();
							}
						})
					
						tds=$("div.contab_orderby>.contenttable>table>tbody>tr>td:first>input");
						tds.each(function(i,e){
							var data=$(e).data("data");
							var node=data.node;
							var col=data.col;
							if(col==cols[a]){
								$(e).parent().parent().remove();
							}
						})
						tds=$("div.condtab_field>.contenttable>table>tbody>tr");
						tds.each(function(i,e){
							var data=$(e).data("data");
							var node=data.node;
							var col=data.col;
							var recol=cols[a];
							if(col==recol){
								$(e).remove();
							}
						})
						
					}
					
				
				},
				repaint:function(){
					var tds=$("div.condtab_condition>.contenttable>table>tbody>tr>td:first>input");
					tds.each(function(i,e){
						var data=$(e).data("data");
						var node=data.node;
						var col=data.col;
						$(e).val(getFieldName(node,col));
					})
					 tds=$("div.condtab_group>.contenttable>table>tbody>tr>td:first>input");
					tds.each(function(i,e){
						var data=$(e).data("data");
						var node=data.node;
						var col=data.col;
						$(e).val(getFieldName(node,col));
					})
				
					tds=$("div.contab_orderby>.contenttable>table>tbody>tr>td:first>input");
					tds.each(function(i,e){
						var data=$(e).data("data");
						var node=data.node;
						var col=data.col;
						$(e).val(getFieldName(node,col));
					})
					tds=$("div.condtab_field>.contenttable>table>tbody>tr");
					tds.each(function(i,e){
						var data=$(e).data("data");
						var node=data.node;
						var col=data.col;
						$(e).find("td:first").text(getFieldName(node,col));
					})
					obj.getSQLText();
				},
				getTableText : function() {
					var fsql = "";
					var main = $('#sourcetable_charts .tbchart_box_main');
					var main_node = main.data("node");
						if(!main_node){
							return;
					}
					var aliasname = main_node.aliasname;
					fsql += getTableName4(main_node);
					if(aliasname!=main_node.name){
						fsql += " as "+aliasname.formatName();	
					}
					var slave = $('#sourcetable_charts .tbchart_box_slave');
					slave.each(function(i, e) {
						var fslave=$(e);
						var fs_node = fslave.data("node");
						var fs_name=getTableName4(fs_node);
						
						
						var fs_aname=(fs_node.aliasname).formatName();
						var r=fslave.find(".tbchart_relations>div");
						var c = r.prop("class");
						var cons = fslave.data("condition");
						if(cons){
						if (c == "ass01") {
							fsql += " inner ";
						} else if (c == "ass02") {
							fsql += " left ";
						} else if (c == "ass03") {
							fsql += " right ";
						} else if (c == "ass04") {
							fsql += " full ";
						}
						fsql+="  join  ";
						fsql+=fs_name;
						if(fs_node.aliasname!=fs_node.name){
							fsql += " as "+(fs_node.aliasname).formatName();							
						}
						fsql+="  on ";
						for ( var i in cons) {
							fsql+=aliasname+"."+cons[i][0];
							fsql+="=";
							fsql+=fs_name+"."+cons[i][1]+" ";
						}
						}else{
							fsql+="  ,  ";
							fsql+=fs_name;
							if(fs_node.aliasname!=fs_node.name){
								fsql += " as "+(fs_node.aliasname).formatName();							
							}
						}
					});
					return fsql;
				},
				wheresql:function(){
					var wheresql="";
					$(".condtab_condition>div>table>tbody>tr").each(function(i,e){
						var tds=$(this).find("td");
						var data=($(tds[0]).find("input")).data("data");
						if(!data){
							return;
						}
						var node=data.node;
						var col=data.col;
						var type=col.type;
						var typeName=(col.typeName).toLowerCase();
						var name=getFieldName2(node,col);
						var val=$(tds[2]).find("input").val();
						if(val==""){
							
							return ;
						}
						if(typeName=="char"||typeName=="varchar"||typeName=="varchar2"||typeName=="nvarchar2"
							||typeName=="date"||typeName=='timstamp'||type=='nchar'||type=='time'
								||type=='longvarchar'||type=='unqualified'){
							val="'"+val.replace("'","''")+"'";
						}
						var sql=name+ $(tds[1]).find("select").val()+val;
						wheresql+=sql;
						wheresql+=" and";
					});
					if(wheresql==""){
						return "";
						}else{
							return " where "+wheresql.substring(0,wheresql.length-3)
						} 
				},
				fieldsql:function(){
					var fsql="";
					tds=$("div.condtab_field>.contenttable>table>tbody>tr");
					$(".condtab_field>div>table>tbody>tr").each(function(i,e){
						var tds=$(this).find("td");						
						var data=$(e).data("data");
						var node=data.node;
						var col=data.col;
						var fname=getFieldName3(node,col);
						var fuc=$(tds[1]).find("select").val();
						var dic=$(tds[2]).find("input").attr('checked');
						var aname=$(tds[3]).find("input").val();
						if(dic){
							fname+=" distinct "
						}
							
						
						if(fuc!=""){
							fsql+=fuc+"("+fname+")";
						}
						else{
							fsql+=fname;
						}
						if(aname!=""){
							fsql+= " as " +aname;
						}
						fsql+=","
					});
					
					if(fsql==""){
						return " * ";
						}else{
							return fsql.substring(0,fsql.length-1);
						} 
				},
				groupbysql:function(){
					var fsql="";
					$(".condtab_group>div>table>tbody>tr").each(function(i,e){
						var input=$(this).find("input");
						
						var data=input.data("data");
						if(data){
						var node=data.node;
						var col=data.col;
						tname=getFieldName2(node,col);
						fsql+=" "+tname+",";
						}
					});
					if(fsql==""){
						return "";
					}else{
							return " group by "+fsql.substring(0,fsql.length-1)
						} 
				},
				orderbysql:function(){
					var fsql="";
					$(".contab_orderby>.contenttable>table>tbody>tr").each(function(i,e){
						var input=$(this).find("input");		
						var data=input.data("data");
						if(data){
						var node=data.node;
						var col=data.col;
						tname=getFieldName2(node,col);
						fsql+=" "+tname+",";	
						}
					});
					if (fsql == "") {
						return "";
					} else {
						return " order by "
								+ fsql.substring(0, fsql.length - 1)
					} 
				},
				coltd : function(col) {
					return "<td> col.table_name"+ "</td><td>"+ col.name+ "</td>"
							+ "<td><select style=\"width: 90%;border-left:0px;border-top:0px;border-right:0px;border-bottom:1px;\" >"
							+ "<option>max</option><option>min</option><option>count</option><option>length</option></select></td>"
							+ "<td><input type=\"checkbox\" ></td>"
							+ "<td><input type=\"text\" value=\"\" style='border-left:0px;border-top:0px;border-right:0px;border-bottom:1px '></td>"
				},
				removeSlave:function(){
					setTimout($(this).remove(),200); 
					
				},
				del : function(id) {

					var d = dialog({
						title :util.i18n('ALERT'),
						content : util.i18n('DELETE_TITLE'),
						okValue : util.i18n('DONE'),
						ok : function() {
							var url = "databaseSource/del";
							var param = {
								id : id
							};
							$.ajax({
								url : url,
								type : 'post',
								data : param,
								dataType : 'json',
								success : function(result) {
									$('#datalist').grid('reload');
								}
							});
						},
						cancelValue :util.i18n('CANCEL'),
						cancel : $.noop
					});
					d.showModal();

				},
				save : function() {
					var url = "databaseSource/add";
					var name=$.trim($("#database_name").val());
//					var database_id=$.trim($("#database_select").val());
//					if(database_id==""){
//						var d_success =dialog({
//							title : util.i18n('ALERT'),
//							content : util.i18n('DATABASE_SELECT'),
//							okValue : util.i18n('DONE'),
//							ok : function(data) {
//							}
//						});
//		        		d_success.showModal();
//						return false;
//					}
//					if(condtab_href_sql==""){
//						var d_success =dialog({
//							title : util.i18n('ALERT'),
//							content : util.i18n('SQL_SELECT'),
//							okValue : util.i18n('DONE'),
//							ok : function(data) {
//							}
//						});
//		        		d_success.showModal();
//						return false;
//					}
		        	if(name.length>20||name.length<=0){
		        		var d_success =dialog({
							title : util.i18n('ALERT'),
							content : util.i18n('DATASOURCENAME_REG'),
							okValue : util.i18n('DONE'),
							ok : function(data) {
							}
						});
		        		d_success.showModal();
		        		return false;
		        	}
					$.ajax({
						url: 'databaseSource/validname',
						type: 'post',
						async: false,
						data: {name:name},
						dataType: 'json',
				        success: function(result){
				        		
				        		if (!result) {
				        			var d_success =	dialog({
										title : util.i18n('ALERT'),
										content : util.i18n('DATASOURCENAME_ALREADY_EXIST'),
										okValue : util.i18n('DONE'),
										ok : function(data) {
										}
									});
				        			d_success.showModal();
					        		return;
					        	}    	
				        		$('#form').ajaxSubmit({
									url : url,
									type : 'post',
									dataType : 'json',
									data : {
										name : $("#database_name").val()
									},
									success : function() {
										var d_success = dialog({
											title : util.i18n('ALERT'),
											content : util.i18n('SAVE_SUCCESSFUL'),
											okValue : util.i18n('DONE'),
											ok : function() {
												d_name.remove();
												add.remove();
												$('#datalist').grid('reload');
											}
										});
										d_success.showModal();
									},
									error : function() {
										var d_error = dialog({
											title : util.i18n('ALERT'),
											content : util.i18n('SAVE_FAILURE'),
											okValue : util.i18n('DONE'),
											ok : function() {
												$('#datalist').grid('reload');
											}
										});
										d_error.showModal();
									}
								});
						}
					});
					return false;
				}
			};
			return obj;
		});