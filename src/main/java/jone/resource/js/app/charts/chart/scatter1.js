define([ 'util', 'echarts' ], function(util, echarts) {
	var itemStyleS = [ {
		normal : {
			shadowBlur : 10,
			shadowColor : 'rgba(120, 36, 50, 0.5)',
			shadowOffsetY : 5,
			color : new echarts.graphic.RadialGradient(0.4, 0.3, 1, [ {
				offset : 0,
				color : 'rgb(251, 118, 123)'
			}, {
				offset : 1,
				color : 'rgb(204, 46, 72)'
			} ])
		}
	}, {
		normal : {
			shadowBlur : 10,
			shadowColor : 'rgba(25, 100, 150, 0.5)',
			shadowOffsetY : 5,
			color : new echarts.graphic.RadialGradient(0.4, 0.3, 1, [ {
				offset : 0,
				color : 'rgb(129, 227, 238)'
			}, {
				offset : 1,
				color : 'rgb(25, 183, 207)'
			} ])
		}
	} ];
	function getItemStyle(index) {
		if (index < itemStyleS.length)
			return itemStyleS[index];
		return null;
	}
	
	var data = [
			[ [ 28604, 77, 17096869, 'Australia', 1990 ],
					[ 31163, 77.4, 27662440, 'Canada', 1990 ],
					[ 1516, 68, 1154605773, 'China', 1990 ],
					[ 13670, 74.7, 10582082, 'Cuba', 1990 ],
					[ 28599, 75, 4986705, 'Finland', 1990 ],
					[ 29476, 77.1, 56943299, 'France', 1990 ],
					[ 31476, 75.4, 78958237, 'Germany', 1990 ],
					[ 28666, 78.1, 254830, 'Iceland', 1990 ],
					[ 1777, 57.7, 870601776, 'India', 1990 ],
					[ 29550, 79.1, 122249285, 'Japan', 1990 ],
					[ 2076, 67.9, 20194354, 'North Korea', 1990 ],
					[ 12087, 72, 42972254, 'South Korea', 1990 ],
					[ 24021, 75.4, 3397534, 'New Zealand', 1990 ],
					[ 43296, 76.8, 4240375, 'Norway', 1990 ],
					[ 10088, 70.8, 38195258, 'Poland', 1990 ],
					[ 19349, 69.6, 147568552, 'Russia', 1990 ],
					[ 10670, 67.3, 53994605, 'Turkey', 1990 ],
					[ 26424, 75.7, 57110117, 'United Kingdom', 1990 ],
					[ 37062, 75.4, 252847810, 'United States', 1990 ] ],
			[ [ 44056, 81.8, 23968973, 'Australia', 2015 ],
					[ 43294, 81.7, 35939927, 'Canada', 2015 ],
					[ 13334, 76.9, 1376048943, 'China', 1990 ],
					[ 21291, 78.5, 11389562, 'Cuba', 2015 ],
					[ 38923, 80.8, 5503457, 'Finland', 2015 ],
					[ 37599, 81.9, 64395345, 'France', 2015 ],
					[ 44053, 81.1, 80688545, 'Germany', 2015 ],
					[ 42182, 82.8, 329425, 'Iceland', 2015 ],
					[ 5903, 66.8, 1311050527, 'India', 2015 ],
					[ 36162, 83.5, 126573481, 'Japan', 2015 ],
					[ 1390, 71.4, 25155317, 'North Korea', 2015 ],
					[ 34644, 80.7, 50293439, 'South Korea', 2015 ],
					[ 34186, 80.6, 4528526, 'New Zealand', 2015 ],
					[ 64304, 81.6, 5210967, 'Norway', 2015 ],
					[ 24787, 77.3, 38611794, 'Poland', 2015 ],
					[ 23038, 73.13, 143456918, 'Russia', 2015 ],
					[ 19360, 76.5, 78665830, 'Turkey', 2015 ],
					[ 38225, 81.4, 64715810, 'United Kingdom', 2015 ],
					[ 53354, 79.1, 321773631, 'United States', 2015 ] ] ];
	var obj = {
		/**
		 * 用于保存图表配置
		 */
		config : {},
		option : {
			backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8,
					[ {
						offset : 0,
						color : '#f7f8fa'
					}, {
						offset : 1,
						color : '#cdd0d5'
					} ]),
			title : {
				text : '1990 与 2015 年各国家人均寿命与 GDP'
			},
			legend : {
				data : [ '1990', '2015' ]
			},
			toolbox: {
		        show : true,
		        feature : {
		            dataView : {show: true, readOnly: false},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
			xAxis : {
				splitLine : {
					lineStyle : {
						type : 'dashed'
					}
				}
			},
			yAxis : {
				splitLine : {
					lineStyle : {
						type : 'dashed'
					}
				},
				scale : true
			},
			series : [
					{
						name : '1990',
						data : data[0],
						type : 'scatter',
						symbolSize : function(data) {
							return Math.sqrt(data[2]) / 5e2;
						},
						label : {
							emphasis : {
								show : true,
								formatter : function(param) {
									return param.data[3];
								},
								position : 'top'
							}
						},
						itemStyle : {
							normal : {
								shadowBlur : 10,
								shadowColor : 'rgba(120, 36, 50, 0.5)',
								shadowOffsetY : 5,
								color : new echarts.graphic.RadialGradient(0.4,
										0.3, 1, [ {
											offset : 0,
											color : 'rgb(251, 118, 123)'
										}, {
											offset : 1,
											color : 'rgb(204, 46, 72)'
										} ])
							}
						}
					},
					{
						name : '2015',
						data : data[1],
						type : 'scatter',
						symbolSize : function(data) {
							return Math.sqrt(data[2]) / 5e2;
						},
						label : {
							emphasis : {
								show : true,
								formatter : function(param) {
									return param.data[3];
								},
								position : 'top'
							}
						},
						itemStyle : {
							normal : {
								shadowBlur : 10,
								shadowColor : 'rgba(25, 100, 150, 0.5)',
								shadowOffsetY : 5,
								color : new echarts.graphic.RadialGradient(0.4,
										0.3, 1, [ {
											offset : 0,
											color : 'rgb(129, 227, 238)'
										}, {
											offset : 1,
											color : 'rgb(25, 183, 207)'
										} ])
							}
						}
					} ]
		},

		/**
		 * 展示图表配置界面(这里不同的图表,根据自身的具体情况,各自编写)
		 */
		showConfigHtml : function(data) {
			var html = util.tpl('html/charts/chart/configScatter1.html', data);
			$("#configChartDiv").html(html);

			$(":checkbox").click(function() {
				if(!$(this).is(':checked')){
					return;
				}
				
				var type = $(this).attr('id').substring(0, 1);
				var column = $(this).attr('id').substring(2);

				$(":checkbox[id^='" + type + "_']").removeProp('checked');
				$(":checkbox[id$='_" + column + "']").removeProp('checked');

				$(this).prop('checked', true);
			});
		},

		/**
		 * 根据配置界面,设置图表配置(修改这里的config属性,databaseChart.js需要获取这里的config)
		 */
		setConfig : function() {
			var me = this;
			me.config = {};

			me.config.title = $.trim($('#titleText').val());

			$(":checkbox:checked").each(function() {
				var type = $(this).attr('id').substring(0, 1);
				var column = $(this).attr('id').substring(2);

				me.config[type] = column;
			});
		},

		/**
		 * 实时查看图表时,根据config和data,修改option中的缓存数据为实时数据
		 * 
		 * @param option
		 *            图表创建时的option属性
		 * @param config
		 *            图表创建时的用户配置
		 * @param data
		 *            图表对应数据源的实时数据,结构如下 data : { list : { column1 :
		 *            [1,2,3,4,5,6,7,8,9], column2 : [1,2,3,4,5,6,7,8,9] } }
		 */
		setOptionData : function(option, config, data) {
			option.legend.data = [];
			option.series = [];
			
			option.title.text = config.title;
			
			var dataMap = {};
			if (config.g && config.g != null && data.list[config.g]) {
				$.each(data.list[config.g], function(index, item) {
					if (!dataMap.hasOwnProperty(item)) {
						dataMap[item] = [];
					}
				});
			} else {
				dataMap["no_group"] = [];
			}

			var data1;
			var v_denominator = 0.0;
			var v_denominator_max = 0.0;
			$.each(data.list[config.x], function(index, item) {
				data1 = new Array();
				data1.push(item);
				data1.push(data.list[config.y][index]);
				if (config.n && config.n != null && data.list[config.n]) {
					data1.push(data.list[config.n][index]);
				}
				if (config.v && config.v != null && data.list[config.v]) {
					var value = data.list[config.v][index];
					data1.push(value);
					
					if(value > 0){
						v_denominator = Math.sqrt(value)/100;
						v_denominator_max = v_denominator_max > v_denominator ? v_denominator_max : v_denominator;
					}
				}
				var group = "no_group";
				if (config.g && config.g != null && data.list[config.g]) {
					group = data.list[config.g][index];
					data1.push(group);
				}
				dataMap[group].push(data1);
			});

			var n_index = 0;
			var v_index = 0;
			if (config.n && config.n != null && data.list[config.n]) {
				n_index += 2;
				v_index++;
			}
			if (config.v && config.v != null && data.list[config.v]) {
				v_index += 2;
			}
			
			var i = 0;
			$.each(dataMap, function(key, arr) {
				var serie = {
					name : key,
					data : arr,
					type : 'scatter',
					symbolSize : function(data) {
						if (v_index < 2)
							return 10;
						return Math.sqrt(data[v_index]) / v_denominator_max;
					},
					symbolSizeFunction : "var symbolSize = function(data) {if ("+v_index+" < 2)return 10;return Math.sqrt(data["+v_index+"]) / "+v_denominator_max+";}",
					label : {
						emphasis : {
							show : true,
							formatter : function(param) {
								if (n_index < 2)
									return "";
								return param.data[n_index];
							},
							formatterFunction : "var formatter = function(param){if ("+n_index+" < 2)return '';return param.data["+n_index+"];}",
							position : 'top'
						}
					},
					itemStyle : getItemStyle(i)
				};

				option.series.push(serie);
				if (config.g && config.g != null && data.list[config.g]) {
					option.legend.data.push(key);
				}

				i++;
			});

		},

		/**
		 * 校验图表是否配置完成 返回值 : true 或者 false
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

			if ($(":checkbox[id^='x_']:checked").length == 0) {
				var d = dialog({
					title : util.i18n('WARNING'),
					content : util.i18n('xAxis') + util.i18n('NOT_SET')
				});
				d.showModal();
				return false;
			}

			if ($(":checkbox[id^='y_']:checked").length == 0) {
				var d = dialog({
					title : util.i18n('WARNING'),
					content : util.i18n('yAxis') + util.i18n('NOT_SET')
				});
				d.showModal();
				return false;
			}

			return true;
		},
		
		setOptionFunction : function(option) {
			$.each(option.series, function(index, serie){
				eval(serie.symbolSizeFunction);
				serie.symbolSize = symbolSize;
				
				eval(serie.label.emphasis.formatterFunction);
				serie.label.emphasis.formatter = formatter;
			});
		},

	};
	return obj;
});