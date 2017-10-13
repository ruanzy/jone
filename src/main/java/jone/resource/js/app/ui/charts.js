define(['util', 'echarts'], function(util, echarts){
	var obj = {
		init: function(){
			util.render('html/charts.html', 'container');
			$( ".column" ).sortable({
			      connectWith: ".column",
			      handle: ".portlet-header",
			      cancel: ".portlet-toggle",
			      placeholder: "portlet-placeholder ui-corner-all"
			    });
			
			$( ".portlet" )
		      .addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
		      .find( ".portlet-header" )
		        .addClass( "ui-widget-header ui-corner-all" );
		       // .prepend( "<span class='ui-icon ui-icon-minusthick portlet-toggle'></span>");
		 
		    $( ".portlet-toggle" ).on( "click", function() {
		      var icon = $( this );
		      icon.toggleClass( "ui-icon-minusthick ui-icon-plusthick" );
		      icon.closest( ".portlet" ).find( ".portlet-content" ).toggle();
		    });
		    
		    var option = {
		    	    title : {
		    	        text: '访问量统计',
		    	        subtext: '',
		    	        x:'center'
		    	    },
		    	    tooltip : {
		    	        trigger: 'item',
		    	        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    	    },
		    	    legend: {
		    	        orient: 'vertical',
		    	        left: 'left',
		    	        data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
		    	    },
		    	    series : [
		    	        {
		    	            name: '访问来源',
		    	            type: 'pie',
		    	            radius : '55%',
		    	            center: ['50%', '60%'],
		    	            data:[
		    	                {value:335, name:'直接访问'},
		    	                {value:310, name:'邮件营销'},
		    	                {value:234, name:'联盟广告'},
		    	                {value:135, name:'视频广告'},
		    	                {value:1548, name:'搜索引擎'}
		    	            ],
		    	            itemStyle: {
		    	                emphasis: {
		    	                    shadowBlur: 10,
		    	                    shadowOffsetX: 0,
		    	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		    	                }
		    	            }
		    	        }
		    	    ]
		    	};
		    
		    var chart = echarts.init(document.getElementById('e1'));
			chart.setOption(option);

		}
	};
	return obj;
});