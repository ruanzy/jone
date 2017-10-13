define(['util'], function(util){
	var obj = {
		config : {
			title : null,
			x : null,
			y : {}
		},
		option : {
		    title : {
		        text: "",
		        subtext: null
		    },
		    tooltip : {
		        trigger: 'axis'
		    },
		    legend: {
		        data: null
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            dataView : {show: true, readOnly: false},
		            magicType : {show: false, type: ['line', 'bar']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    xAxis : [
		        {
		            type : 'category',
//		            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
		            data : null
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : [
//		        {
//		            name:'蒸发量',
//		            type:'bar',
//		            data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
//		            markPoint : {
//		                data : [
//		                    {type : 'max', name: '最大值'},
//		                    {type : 'min', name: '最小值'}
//		                ]
//		            },
//		            markLine : {
//		                data : [
//		                    {type : 'average', name: '平均值'}
//		                ]
//		            }
//		        },
//		        {
//		            name:'降水量',
//		            type:'bar',
//		            data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3],
//		            markPoint : {
//		                data : [
//		                    {name : '年最高', value : 182.2, xAxis: 7, yAxis: 183},
//		                    {name : '年最低', value : 2.3, xAxis: 11, yAxis: 3}
//		                ]
//		            },
//		            markLine : {
//		                data : [
//		                    {type : 'average', name : '平均值'}
//		                ]
//		            }
//		        }
		    ]
		},
		
		/**
		 * 展示图表配置界面(这里不同的图表,根据自身的具体情况,各自编写)
		 */
		showConfigHtml : function(data){
			var html = util.tpl('html/charts/chart/configLine.html', data);
			$("#configChartDiv").html(html);
			
			$(":checkbox[id^='x_']").click(function() {
				$(":checkbox[id^='x_']").removeProp('checked');
				$(this).prop('checked', true);
				
				var column = $(this).attr('id').substring(2);
				$("#y_" + column).removeProp('checked');
			});
			
			$(":checkbox[id^='y_']").click(function() {
				var column = $(this).attr('id').substring(2);
				$("#x_" + column).removeProp('checked');
			});
		},
		
		/**
		 * 根据配置界面,设置图表配置(修改这里的config的属性,databaseChart.js需要获取这里的config)
		 */
		setConfig : function(){
			var me = this;
			me.config = {
					title : null,
					x : null,
					y : {}
				};
			
			me.config.title = $.trim($('#titleText').val());
			
			$(":checkbox[id^='x_']:checked").each(function(){
				var column = $(this).attr('id').substring(2);
				
				me.config.x = column;
			});
			
			$(":checkbox[id^='y_']:checked").each(function(){
				var column = $(this).attr('id').substring(2);
				
				var alias = $.trim($("#t_" + column).val());
				if(alias == ""){
					me.config.y[column] = column;
				}else{
					me.config.y[column] = alias;
				}
			});
		},
		
		setOptionData : function(option, config, data){
			option.legend.data = [];
			option.xAxis[0].data = null;
			option.series = [];
			
			option.title.text = config.title;
			
			var x = config.x;
			option.xAxis[0].data = data.list[x];
			
			$.each(config.y, function(name, alias) {
				option.legend.data.push(alias);

				var serie = {
					name : alias,
					type : 'line',
					data : data.list[name],
				};
				option.series.push(serie);
			});
		},
		
		/**
		 * 校验图表是否配置完成
		 * 返回值 : true 或者 false
		 */
		validateConfig : function(){
			var me = this;
			if($.trim($('#titleText').val()) == ""){
				var d = dialog({
					title : util.i18n('WARNING'),
					content : util.i18n('CHART_NAME') + util.i18n('NOT_SET')
				});
				d.showModal();
				return false;
			}
			
			if($(":checkbox[id^='x_']:checked").length == 0){
				var d = dialog({
					title : util.i18n('WARNING'),
					content : util.i18n('xAxis') + util.i18n('NOT_SET')
				});
				d.showModal();
				return false;
			}
			
			if($(":checkbox[id^='y_']:checked").length == 0){
				var d = dialog({
					title : util.i18n('WARNING'),
					content : util.i18n('yAxis') + util.i18n('NOT_SET')
				});
				d.showModal();
				return false;
			}
			
			return true;
		}
		
	};
	return obj;
});