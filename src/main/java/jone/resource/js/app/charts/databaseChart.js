define([ 'util', 'echarts' ], function(util, echarts) {
	var obj = {
		/**
		 * 当前选中的图表类型
		 */
		chartType : 'bar',
		/**
		 * 当前选中的图表类型所对应的js对象
		 */
		chart : null,
		/**
		 * 当前选中的数据源的ID
		 */
		dataSourceId : null,
		/**
		 * 数据源数据缓存,所有获取过的数据源数据都缓存在这个对象中
		 * 格式:
		 * dataSourceData : {
		 * 		dataSourceId1 : {
		 * 			list : {
		 * 				column1 : [1,2,3,4,5,6,7,8,9],
		 * 				column2 : [1,2,3,4,5,6,7,8,9]
		 * 			}
		 * 		},
		 * 		dataSourceId2 : {
		 * 			list : {
		 * 				column1 : [1,2,3,4,5,6,7,8,9]
		 * 			}
		 * 		}
		 * }
		 */
		dataSourceData : {},
		init : function() {
			var me = this;
			util.render('html/charts/databaseChart.html', 'chart_main');
			$('#create').on('click', function() {
				me.create();
			});
			$("#query").on("click",function(){
				$('#datalist').grid('reload');
			});
			// $('#del').on('click', function(){me.del();});
			me.list();
		},
		reset : function() {
			var me = this;
			me.chartType = 'bar';
			me.chart = null;
			me.dataSourceId = null;
		},
		list : function() {
			var me = this;
			$('#datalist').grid({
				url : 'databaseChart/list',
				columns : [ {header : util.i18n('CHART_NAME'), field : 'chart_name'},
				            {header : util.i18n('DATASOURCE_NAME'), field : 'name'},
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
				
				var space1 = $('<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>');
				
				var showCurrent = $('<a href="javascript:void(0);">'+util.i18n('SHOW_CURRENT')+'</a>');
				showCurrent.on('click', function() {
					me.showCurrent(r);
				});
				
				var space2 = $('<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>');
				
				var del = $('<a href="javascript:void(0);">'+util.i18n('DELETE')+'</a>');
				del.on('click', function() {
					me.del(r.id, r.chart_name);
				});
				
				op.push(show, space1, showCurrent, space2, del);

				return op;
			}
		},
		create : function() {
			var me = this;
			me.reset();
			var html = util.tpl('html/charts/addChart.html');
			var d = null;
			d = dialog({
				title : util.i18n('ADD_DATABASE_CHART'),
				content : html,
				okValue : util.i18n('SAVE'),
				ok : function() {
					if(me.validateConfig()){
						me.setConfig();
						me.save(this);
					}
					return false;
				},
				button : [ {
					value : util.i18n('PREVIEW'),
					callback : function() {
						if(me.validateConfig()){
							me.setConfig();
							me.preview();
						}
						return false;
					},
					autofocus : true
				} ],
				onshow : function() {
					$(".charts_item").hover(function() {
						$(this).find(".charts_explain").show();
					}, function() {
						$(this).find(".charts_explain").hide();
					});
					
					$(".charts_item").click(function() {
						$(".charts_item").removeClass('charts_selected');
						$(this).addClass('charts_selected');
						
						// 根据选择的图表类型,调用对应的js,加载对应的图表配置界面
						var id = $(this).attr('id');
						require(["app/charts/chart/" + id], function(chart){
							me.chartType = id;
							me.chart = chart;
							me.showConfigHtml();
						});
					});

					// 默认选择柱状图
					$("#bar").click();
					// 加载数据源列表
					me.loadDataSource();
				}
			});
			d.showModal();
		},
		
		save : function(d) {
			var me = this;
			var url = "databaseChart/add";
			$('#form').ajaxSubmit({
				url : url,
				type : 'post',
				dataType : 'json',
				data : {
					datasource_id : me.dataSourceId,
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
				error : function(data) {
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
		
		preview : function() {
			var me = this;
			var html = util.tpl('html/charts/previewChart.html');
			var d = null;
			d = dialog({
				title : util.i18n('PREVIEW_DATABASE_CHART'),
				content : html,
				okValue : util.i18n('DONE'),
				ok : function() {
					$.noop
				},
				onshow : function() {
					var previewChart = echarts.init(document.getElementById('previewChartDiv'));
					var option = me.chart.option;
					previewChart.setOption(option);
				}
			});
			d.showModal();
		},
		
		show : function(r) {
			var me = this;
			var html = util.tpl('html/charts/previewChart.html');
			var d = null;
			d = dialog({
				title : util.i18n('SHOW_DATABASE_CHART'),
				content : html,
				okValue : util.i18n('DONE'),
				ok : function() {
					$.noop
				},
				onshow : function() {
					var previewChart = echarts.init(document.getElementById('previewChartDiv'));
					var option = JSON.parse(r.chart_option);
					require(["app/charts/chart/" + r.chart_type], function(chart){
						if(chart.setOptionFunction){
							chart.setOptionFunction(option);
						}
						me.i18nToolbox(option);
						previewChart.setOption(option);
					});
				}
			});
			d.showModal();
		},
		
		showCurrent : function(r) {
			var me = this;
			var html = util.tpl('html/charts/previewChart.html');
			var d = null;
			d = dialog({
				title : util.i18n('SHOW_CURRENT_DATABASE_CHART'),
				content : html,
				okValue : util.i18n('DONE'),
				ok : function() {
					$.noop
				},
				onshow : function() {
					var previewChart = echarts.init(document.getElementById('previewChartDiv'));
					previewChart.setOption(JSON.parse(r.chart_option));
					
					var d = dialog({
						title : util.i18n('LOADING_DATA'),
						cancel : false,
					});
					d.showModal();
					
					$.ajax({
						url : 'databaseChart/getDataSourceData',
						type : 'post',
						data : {
							datasource_id : r.datasource_id
						},
						dataType : 'json',
						cache : false,
						error : function() {
							d.close().remove();
							me.clearConfigHtml();
							var dError = dialog({
								title : util.i18n('WARNING'),
								content : util.i18n('FAILED_TO_LOAD_DATA') + util.i18n('CHECK_DATA_SOURCE'),
							});
							dError.showModal();
						},
						success : function(data) {
							d.close().remove();
							
							if(data.error){
								me.clearConfigHtml();
								var dError = dialog({
									title : util.i18n('WARNING'),
									content : util.i18n(data.error) + util.i18n('CHECK_DATA_SOURCE'),
								});
								dError.showModal();
								return;
							}
							
							var columns = {list : {}};
							$.each(data, function(index, item){
								$.each(item, function(key, value){
									if(!columns.list.hasOwnProperty(key)){
										columns.list[key] = new Array();
									}
									columns.list[key].push(value);
								});
							});
							
							require(["app/charts/chart/" + r.chart_type], function(chart){
								var option = JSON.parse(r.chart_option);
								chart.setOptionData(option, JSON.parse(r.chart_config), columns);
								me.i18nToolbox(option);
								previewChart.setOption(option);
							});
						}
					});
				}
			});
			d.showModal();
		},
		
		del : function(id, chart_name) {
			var d = null;
			d = dialog({
				title : util.i18n('DELETE'),
				content : util.i18n('DELETE_TITLE'),
				okValue : util.i18n('DONE'),
				ok : function() {
					var url = "databaseChart/del";
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
		
		loadDataSource : function() {

			var me = this;
			$.ajax({
				url : 'databaseSource/listAll',
				type : 'GET',
				dataType : 'json',
				cache : false,
				error : null,
				success : function(data) {
					var list = {list : data};
					var html = util.tpl('html/charts/dataSourceTemplate.html', list);
					$("#dataSourceBox").html(html);
					
					$('input[type="checkbox"]').click(function() {
						$('input[type="checkbox"]').removeProp('checked');
						$(this).prop('checked', true);
						
						// 加载数据源的数据
						var id = $(this).attr('id');
						me.dataSourceId = id;
						if(me.dataSourceData[id]){
							me.showConfigHtml();
							return;
						}
						
						var d = dialog({
							title : util.i18n('LOADING_DATA'),
							cancel : false,
						});
						d.showModal();
						
						me.loadDataSourceData(d);
					});
				}
			});
		},
		
		loadDataSourceData : function(d){
			var me = this;
			$.ajax({
				url : 'databaseChart/getDataSourceData',
				type : 'post',
				data : {
					datasource_id : me.dataSourceId
				},
				dataType : 'json',
				cache : false,
				error : function() {
					d.close().remove();
					me.clearConfigHtml();
					var dError = dialog({
						title : util.i18n('WARNING'),
						content : util.i18n('FAILED_TO_LOAD_DATA') + util.i18n('CHECK_DATA_SOURCE'),
					});
					dError.showModal();
				},
				success : function(data) {
					d.close().remove();
					
					if(data.error){
						me.clearConfigHtml();
						var dError = dialog({
							title : util.i18n('WARNING'),
							content : util.i18n(data.error) + util.i18n('CHECK_DATA_SOURCE'),
						});
						dError.showModal();
						return;
					}
					
					var columns = {list : {}};
					$.each(data, function(index, item){
						$.each(item, function(key, value){
							if(!columns.list.hasOwnProperty(key)){
								columns.list[key] = new Array();
							}
							columns.list[key].push(value);
						});
					});
					me.dataSourceData[me.dataSourceId] = columns;
					
					me.showConfigHtml();
				}
			});
		},
		
		showConfigHtml : function(){
			var me = this;
			me.clearConfigHtml();
			if(me.chart != null && me.dataSourceId != null && me.dataSourceData[me.dataSourceId]){
				me.chart.showConfigHtml(me.dataSourceData[me.dataSourceId]);
			}
		},
		
		clearConfigHtml : function(){
			$("#configChartDiv").html("");
		},
		
		setConfig : function(){
			var me = this;
			me.chart.setConfig();
			me.chart.setOptionData(me.chart.option, me.chart.config, me.dataSourceData[me.dataSourceId]);
		},
		
		validateConfig : function(){
			var me = this;
			
			if(me.dataSourceId == null){
				var d = dialog({
					title : util.i18n('WARNING'),
					content : util.i18n('SELECT_DATA_SOURCE')
				});
				d.showModal();
			}else if(!me.dataSourceData.hasOwnProperty(me.dataSourceId)){
				var d = dialog({
					title : util.i18n('WARNING'),
					content : util.i18n('SELECT_VALID_DATA_SOURCE')
				});
				d.showModal();
			}else{
				return me.chart.validateConfig();
			}
			
			return false;
		},
		
		i18nToolbox : function(option){
			option.toolbox = {
		        show : true,
		        feature : {
		            dataView : {show: true, readOnly: false, title: util.i18n('DATA_VIEW'), lang: [util.i18n('DATA_VIEW'), util.i18n('CLOSE'), util.i18n('REFRESH')],},
		            restore : {show: true, title: util.i18n('RESTORE')},
		            saveAsImage : {show: true, title: util.i18n('SAVE_AS_IMAGE')}
		        }
		    };
		}
		
	};
	return obj;
});