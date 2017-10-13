define([ 'util', 'jsonview' ], function(util, jsonview) {
	var json = {
		Entity : {
			"hey" : "guy",
			"anumber" : 243,
			"anobject" : {
				"whoa" : "nuts",
				"anarray" : [ 1, 2, "thr<h1>ee" ],
				"anotherarray" : [ 1, 2 ],
				"more" : "stuff"
			},
			"awesome" : true,
			"bogus" : false,
			"meaning" : null,
			"japanese" : "明日がある。",
			"link" : "http://jsonview.com",
			"notLink" : "http://jsonview.com is great",
			"boli" : [ {
				'haha' : 'dada'
			}, {
				'lala' : 'jaja'
			} ]
		},
		Status : 200
	};
	var obj1 = require("app/charts/databaseChart");
	var obj2 = {
		databaseChart : null,
		restfulData : null,
		init : function() {
			var me = this;
			me.resetR();
			util.render('html/charts/restfulChart.html', 'chart_main');
			
			$("#add").click(function(){
				$("#add,#query").hide();
				$("#list").show();
				$("#restful").show("slow");
				$("#datalist").hide("slow");
			});
			$("#list").click(function(){
				$("#add,#query").show();
				$("#list").hide();
				$("#restful").hide("slow");
				$("#datalist").show("slow");
			});
			
			$("#query").on("click",function(){
				$('#datalist').grid('reload');
			});

			$("#button_submit").click(function() {
				var type = $("#type").val();
				me.sendRestful(type);
			});
			$('#button_get').click(function() {
				me.sendRestful('GET');
			});
			$('#button_post').click(function() {
				me.sendRestful('POST');
			});

			$('.resful_tab li').click(function() {
				$('.resful_tab li').removeClass('resful_tab_select');
				$(this).addClass('resful_tab_select');

				$("div[id^='div_']").hide();
				var id = $(this).attr('id');
				$('#div_' + id).show();
			});

			$('#headers_table tr,#parameters_table tr')
					.on('click', me.selectTr);

			$('#header_append,#parameter_append').on('click', function() {
				me.append(this);
			});

			$('#header_remove,#parameter_remove').on('click', function() {
				me.remove(this);
			});

			me.resetButton_create();
			
			me.list();
		},

		resetR : function() {
			var me = this;
			me.restfulData = null;
			me.reset();
		},

		list : function() {
			var me = this;
			$('#datalist').grid({
				url : 'restfulChart/list',
				columns : [ {header : util.i18n('CHART_NAME'), field : 'chart_name'},
				            {header : util.i18n('CHART_TYPE'), field : 'chart_type', render : typeRender, tipRender : typeRender},
				            {header : util.i18n('CREATE_TIME'), field : 'create_time', render : dateRender, tipRender : dateRender},
				            {header : util.i18n('OPERATE'), field : 'op', render : opRender, align : 'center', width : 200}
				            ]
			});
			function typeRender(v, r) {
				return util.i18n(v);
			}
			function dateRender(v, r) {
				if (v == null || v == "") {
					return "";
				}
				var date = new Date(v);
				return util.dateFormat(date, "yyyy-MM-dd HH:mm:ss");
			}
			function opRender(v, r) {
				var op = new Array();

				var show = $('<a href="javascript:void(0);">'+util.i18n('SHOW')+'</a>');
				show.on('click', function() {
					me.show(r);
				});
				
				var space = $('<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>');
				
				var del = $('<a href="javascript:void(0);">'+util.i18n('DELETE')+'</a>');
				del.on('click', function() {
					me.del(r.id, r.chart_name);
				});
				
				op.push(show, space, del);

				return op;
			}
		},
		
		save : function(d) {
			var me = this;
			var url = "restfulChart/add";
			$('#form').ajaxSubmit({
				url : url,
				type : 'post',
				dataType : 'json',
				data : {
					chart_name : me.chart.option.title.text,
					chart_type : me.chartType,
					chart_option : JSON.stringify(me.chart.option),
					chart_config : JSON.stringify(me.chart.config),
				},
				success : function(data) {
					if(data.error){
						var d_error = dialog({
							title : util.i18n('PROMPT'),
							content : util.i18n('SAVE_FAILURE') + ':' + util.i18n(data.error),
							okValue : util.i18n('DONE'),
							ok : $.noop
						});
						d_error.showModal();
						return;
					}
					var d_success = dialog({
						title : util.i18n('PROMPT'),
						content : util.i18n('SAVE_SUCCESSFUL'),
						okValue : util.i18n('DONE'),
						ok : function() {
							$('#datalist').grid('reload');
						}
					});
					d_success.showModal();
					d.close().remove();
				},
				error : function() {
					var d_error = dialog({
						title : util.i18n('PROMPT'),
						content : util.i18n('SAVE_FAILURE'),
						okValue : util.i18n('DONE'),
						ok : function() {
							$('#datalist').grid('reload');
						}
					});
					d_error.showModal();
				}
			});
		},
		
		del : function(id, chart_name) {
			var d = null;
			d = dialog({
				title : util.i18n('DELETE'),
				content : util.i18n('DELETE_TITLE'),
				okValue : util.i18n('DONE'),
				ok : function() {
					var url = "restfulChart/del";
					var param = {
						id : id,
						chart_name : chart_name
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
				cancelValue : util.i18n('CANCEL'),
				cancel : $.noop
			});
			d.showModal();
		},
		
		resetButton_create : function() {
			var me = this;
			if (me.restfulData && me.restfulData.Status
					&& me.restfulData.Status == 200) {
				$('#create').off('click').on('click', function() {
					me.create();
				});
				$("#create").css("background-color", "");
			} else {
				$("#create").unbind();
				$("#create").css("background-color", "#cdcdcd");
			}
		},

		selectTr : function() {
			$(this).siblings().removeClass('resful_tr_select');
			$(this).addClass('resful_tr_select');
		},

		append : function(eventObj) {
			var me = this;

			var name = $(eventObj).siblings('input[id$="_name"]').val();
			var value = $(eventObj).siblings('input[id$="_value"]').val();
			
			var update = false;
			$(eventObj).parent().siblings("div").find('table tr').each(function(){
				if($(this).children().first().text() == name){
					$(this).children().last().text(value);
					update = true;
					return false;
				}
			});
			
			if(update){
				return;
			}
			
			var tr = '<tr><td class="headers_td_left" title="'+ name +'">' + name + '</td><td title="'+ value +'">'
					+ value + '</td></tr>';
			$(tr).click(me.selectTr).appendTo(
					$(eventObj).parent().siblings("div").find('table'));
		},

		remove : function(eventObj) {
			$(eventObj).siblings("div").find('tr.resful_tr_select').remove();
		},

		sendRestful : function(type) {
			var me = this;

			var url = $.trim($('#url').val());
			if (url == '') {
				var d = dialog({
					title : util.i18n('WARNING'),
					content : 'URL' + util.i18n('NOT_SET')
				});
				d.showModal();
				return false;
			}

			var contentType = $('#content_type').val();
			var contents = $('#contents').val();

			var headers = {};
			$('#headers_table tr').each(function() {
				var name = $(this).children().first().text();
				var value = $(this).children().last().text();
				headers[name] = value;
			});

			var parameters = {};
			$('#parameters_table tr').each(function() {
				var name = $(this).children().first().text();
				var value = $(this).children().last().text();
				parameters[name] = value;
			});

			var data = {
				url : url,
				type : type,
				contentType : contentType,
				headers : headers,
				parameters : parameters
			};

			var d = dialog({
				title : util.i18n('WAITING_FOR_RESPONSE'),
				cancel : false,
			});
			d.showModal();

			$.ajax({
				type : 'POST',
				url : 'restfulChart/getRespone',
				contentType : "application/json; charset=utf-8",
				dataType : 'json',
				data : JSON.stringify(data),
				success : function(data) {
					d.close().remove();
					me.restfulData = data;
//					me.restfulData = json;
					me.resetButton_create();
					$(".resful_response_content").JSONView(me.restfulData);
				},
				error : function() {
					d.close().remove();
				}
			});
		},

		loadDataSource : function() {
			var me = this;
			$("#dataSourceBox").JSONView(me.restfulData);

			$('.jsonview .prop').click(function() {
				$('.jsonview .prop').removeClass('selected');
				$(this).addClass('selected');

				var a = $(this);
				var nodeTree = [];
				for ( var i = 0; i > -1; i++) {
					nodeTree.unshift(a.text().substring(1, a.text().length - 1));
					a = a.parent().parent().siblings('a');
					if (a.length > 0) {
						a = $(a[0]);
					} else {
						break;
					}
				}

				var id = nodeTree.join(".");
				me.dataSourceId = id;
				if (me.dataSourceData[id]) {
					me.showConfigHtml();
					return;
				}

				var data = me.restfulData;
				for ( var i = 0; i < nodeTree.length; i++) {
					data = data[nodeTree[i]];
				}

				var d = dialog({
					title : util.i18n('LOADING_DATA'),
					cancel : false,
				});
				d.showModal();

				me.loadDataSourceData(d, data);
			});
		},

		loadDataSourceData : function(d, data) {
			var me = this;
			
			var rowCount = 0;
			var columnCount = 0;
			var columns = {
					list : {}
				};
			
			// 获取对象数组的行数和列数
			if(data instanceof Array && data.length > 0){
				$.each(data, function(index, item) {
					if(item instanceof Object){
						rowCount++;
						$.each(item, function(key, value) {
							if (!columns.list.hasOwnProperty(key)) {
								columns.list[key] = new Array();
								columnCount++;
							}
						});
					}
				});
			}
			
			// 填充一个空的columns.list
			if(rowCount > 0 && columnCount > 0){
				for(var i = 0; i < rowCount; i++){
					$.each(columns.list, function(key, arr) {
						arr.push(null);
					});
				}
			}else{
				d.close().remove();
				me.clearConfigHtml();
				return;
			}
			
			var rowIndex = 0;
			$.each(data, function(index, item) {
				if(item instanceof Object){
					$.each(item, function(key, value) {
						if(typeof(value) != 'object' && typeof(value) != 'function'){
							columns.list[key][rowIndex] = value;
						}
					});
					rowIndex++;
				}
			});
			me.dataSourceData[me.dataSourceId] = columns;

			me.showConfigHtml();

			d.close().remove();
		},
	};

	var obj = {};
	$.extend(obj, obj1, obj2);

	return obj;
});