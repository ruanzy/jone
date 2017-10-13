define(['util'], function(util){
	var obj = {
		/**
		 * 用于保存图表配置
		 */
		config : {},
		option : {
			title : {
				text : '某地区蒸发量和降水量',
				subtext : '纯属虚构'
			},
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
		showConfigHtml : function(data){
			
		},
		
		/**
		 * 根据配置界面,设置图表配置(修改这里的config属性,databaseChart.js需要获取这里的config)
		 */
		setConfig : function(){
			
		},
		
		/**
		 * 预览和实时查看图表时,根据config和data,修改option中的缓存数据为实时数据
		 * @param option 图表创建时的option属性(默认为各图表js中的option属性)
		 * @param config 图表创建时的用户配置(默认为各图表js中的config属性)
		 * @param data 图表对应数据源的实时数据,结构如下
		 * data : {
		 * 		list : {
		 * 			column1 : [1,2,3,4,5,6,7,8,9],
		 * 			column2 : [1,2,3,4,5,6,7,8,9]
		 * 		}
		 * }
		 */
		setOptionData : function(option, config, data){
			
		},
		
		/**
		 * 校验图表是否配置完成
		 * 返回值 : true 或者 false
		 */
		validateConfig : function(){
			
			return true;
		},
		
		/**
		 * 非必须
		 * 如果option中有function,并且function无法保存到数据库,
		 * "查看"之前需要对从数据库中查询出来的option进行相关function的添加
		 * 如scatter1.js
		 * @param option 图表创建时的option属性
		 */
//		setOptionFunction : function(option) {
//			
//		},
		
	};
	return obj;
});