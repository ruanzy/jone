define(['util'], function(util){
	var obj = {
		config : {
			nameKey : null,
			valueKey : null
		},
		option : {
		    title : {
		        text: '',
		        subtext: null,
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
//		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		        formatter: "{b} : {c} ({d}%)"
		    },
		    legend: {
		        orient: 'vertical',
		        left: 'left',
//		        data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
		        data: null
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            dataView : {show: true, readOnly: false},
		            magicType : {show: false, type: ['pie', 'funnel']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    series : [
//		        {
//		            name: '访问来源',
//		            type: 'pie',
//		            radius : '55%',
//		            center: ['50%', '60%'],
//		            data:[
//		                {value:335, name:'直接访问'},
//		                {value:310, name:'邮件营销'},
//		                {value:234, name:'联盟广告'},
//		                {value:135, name:'视频广告'},
//		                {value:1548, name:'搜索引擎'}
//		            ],
//		            itemStyle: {
//		                emphasis: {
//		                    shadowBlur: 10,
//		                    shadowOffsetX: 0,
//		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
//		                }
//		            }
//		        }
		    ]
		},
				                    
		
		/**
		 * 展示图表配置界面(这里不同的图表,根据自身的具体情况,各自编写)
		 */
		showConfigHtml : function(data){
			var html = util.tpl('html/charts/chart/configPie.html', data);
			$("#configChartDiv").html(html);
			
			$(":checkbox[id^='name_']").click(function() {
				$(":checkbox[id^='name_']").removeProp('checked');
				$(this).prop('checked', true);
				
				var column = $(this).attr('id').substring(5);
				$("#value_" + column).removeProp('checked');
			});
			
			$(":checkbox[id^='value_']").click(function() {
				$(":checkbox[id^='value_']").removeProp('checked');
				$(this).prop('checked', true);
				
				var column = $(this).attr('id').substring(6);
				$("#name_" + column).removeProp('checked');
			});
		},
		
		/**
		 * 根据配置界面,设置图表配置(修改这里的config的属性,databaseChart.js需要获取这里的config)
		 */
		setConfig : function(){
			var me = this;
			me.config = {
					nameKey : null,
					valueKey : null
				};
			
			me.config.title = $.trim($('#titleText').val());
			
			$(":checkbox[id^='name_']:checked").each(function(){
				var column = $(this).attr('id').substring(5);
				me.config.nameKey = column;
			});
			
			$(":checkbox[id^='value_']:checked").each(function(){
				var column = $(this).attr('id').substring(6);
				me.config.valueKey = column;
			});
		},
		
		setOptionData : function(option, config, data){
			option.series = [];
			
			option.title.text = config.title;
			
			var names = data.list[config.nameKey];
			var values = data.list[config.valueKey];
			
			var data1 = [];
			$.each(names, function(i, n){
				data1.push({
					name : n,
					value : values[i]
				});
			});
			
			var serie = {
					// name: '访问来源',
		            type: 'pie',
		            radius : ['45%', '55%'],
		            center: ['50%', '60%'],
		            data: data1,
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            }
		        };
			
			option.series.push(serie);
			
			option.legend.data = names;
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
			
			if($(":checkbox[id^='name_']:checked").length == 0){
				var d = dialog({
					title : util.i18n('WARNING'),
					content : util.i18n('NAME') + util.i18n('NOT_SET')
				});
				d.showModal();
				return false;
			}
			
			if($(":checkbox[id^='value_']:checked").length == 0){
				var d = dialog({
					title : util.i18n('WARNING'),
					content : util.i18n('VALUE') + util.i18n('NOT_SET')
				});
				d.showModal();
				return false;
			}
			
			return true;
		}
	};
	return obj;
});