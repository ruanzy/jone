define([ 'util' ], function(util) {
	var obj = {
		/**
		 * 用于保存图表配置
		 */
		config : {},
		option : {
			title : {
				text : '基础雷达图'
			},
			tooltip : {
//				trigger : 'item',
//				backgroundColor : 'rgba(0,0,250,0.2)'
			},
			legend : {
				data : [ '预算分配（Allocated Budget）', '实际开销（Actual Spending）' ]
			},
			toolbox: {
		        show : true,
		        feature : {
		            dataView : {show: true, readOnly: false},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
			visualMap: {
				color: ['red', 'yellow']
			},
			radar : {
				// shape: 'circle',
				indicator : [ {
					name : '销售（sales）',
					max : 6500
				}, {
					name : '管理（Administration）',
					max : 16000
				}, {
					name : '信息技术（Information Techology）',
					max : 30000
				}, {
					name : '客服（Customer Support）',
					max : 38000
				}, {
					name : '研发（Development）',
					max : 52000
				}, {
					name : '市场（Marketing）',
					max : 25000
				} ]
			},
			series : [ {
//				name : '预算 vs 开销（Budget vs spending）',
				type : 'radar',
				symbol: 'none',
				// areaStyle: {normal: {}},
				itemStyle : {
					normal : {
						lineStyle : {
							width : 1
						}
					},
					emphasis : {
						areaStyle : {
							color : 'rgba(0,250,0,0.3)'
						}
					}
				},
				data : [ {
					value : [ 4300, 10000, 28000, 35000, 50000, 19000 ],
					name : '预算分配（Allocated Budget）'
				}, {
					value : [ 5000, 14000, 28000, 31000, 42000, 21000 ],
					name : '实际开销（Actual Spending）'
				} ]
			} ]
		},

		/**
		 * 展示图表配置界面(这里不同的图表,根据自身的具体情况,各自编写)
		 *
		 * @param data 图表对应数据源的实时数据,结构如下
		 * data : {
		 * 		list : {
		 * 			column1 : [1,2,3,4,5,6,7,8,9],
		 * 			column2 : [1,2,3,4,5,6,7,8,9]
		 * 		}
		 * }
		 */
		showConfigHtml : function(data) {
			var html = util.tpl('html/charts/chart/configRadar.html', data);
			$("#configChartDiv").html(html);
			
			$(":checkbox[id^='n_']").click(function() {
				if(!$(this).is(':checked')){
					return;
				}
				
				$(":checkbox[id^='n_']").removeProp('checked');
				$(this).prop('checked', true);
				
				var column = $(this).attr('id').substring(2);
				$("#z_" + column).removeProp('checked');
			});
			
			$(":checkbox[id^='z_']").click(function() {
				var column = $(this).attr('id').substring(2);
				$("#n_" + column).removeProp('checked');
			});
		},

		/**
		 * 根据配置界面,设置图表配置(修改这里的config属性,databaseChart.js需要获取这里的config)
		 */
		setConfig : function() {
			var me = this;
			me.config = {
					title : null,
					n : null,
					z : {}
				};
			
			me.config.title = $.trim($('#titleText').val());
			
			$(":checkbox[id^='n_']:checked").each(function(){
				var column = $(this).attr('id').substring(2);
				
				me.config.n = column;
			});
			
			$(":checkbox[id^='z_']:checked").each(function(){
				var column = $(this).attr('id').substring(2);
				
				var alias = $.trim($("#a_" + column).val());
				var maxValue = $.trim($("#m_" + column).val());
				if(isNaN(maxValue) || maxValue <= 0){
					maxValue = null;
				}
				
				me.config.z[column] = {a : alias, m : maxValue};
			});
		},

		/**
		 * 实时查看图表时,根据config和data,修改option中的缓存数据为实时数据
		 * @param option 图表创建时的option属性
		 * @param config 图表创建时的用户配置
		 * @param data 图表对应数据源的实时数据,结构如下
		 * data : {
		 * 		list : {
		 * 			column1 : [1,2,3,4,5,6,7,8,9],
		 * 			column2 : [1,2,3,4,5,6,7,8,9]
		 * 		}
		 * }
		 */
		setOptionData : function(option, config, data) {
			option.legend.data = [];
			option.radar.indicator = [];
			option.series[0].data = [];
			
			option.title.text = config.title;
			
			if(config.n){
				option.legend.data = data.list[config.n];
			}
			
			$.each(config.z, function(z, item){
				
				var indicatorItem = {};
				if(item.a == ""){
					indicatorItem["name"] = z;
				}else{
					indicatorItem["name"] = item.a;
				}
				
				var max = 0;
				$.each(data.list[z], function(index, v){
					if(!option.series[0].data[index]){
						option.series[0].data[index] = {value : []};
					}
					
					option.series[0].data[index]["value"].push(v);
					
					if(config.n){
						option.series[0].data[index]["name"] = data.list[config.n][index];
					}
					if(!item.m){
						max = max > v ? max : v;
					}
				});
				indicatorItem["max"] = item.m ? item.m : max;
				
				option.radar.indicator.push(indicatorItem);
			});
		},

		/**
		 * 校验图表是否配置完成
		 * 返回值 : true 或者 false
		 */
		validateConfig : function() {
			var me = this;
			if($.trim($('#titleText').val()) == ""){
				var d = dialog({
					title : util.i18n('WARNING'),
					content : util.i18n('CHART_NAME') + util.i18n('NOT_SET')
				});
				d.showModal();
				return false;
			}
			
			if($(":checkbox[id^='z_']:checked").length < 3){
				var d = dialog({
					title : util.i18n('WARNING'),
					content : util.i18n('CHECK_AXIS')
				});
				d.showModal();
				return false;
			}
			
			return true;
		},

	};
	return obj;
});