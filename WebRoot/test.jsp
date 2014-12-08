<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<div style="float: left;">
<button id='addbtn' class='btn btn-success' funcid='102'><i class="icon-plus-sign"></i> 增加</button>
<button id='delbtn' class='btn btn-success' funcid='103'><i class="icon-pencil"></i> 修改</button>
<button id='assignbtn' class='btn btn-success' funcid='104'><i class="icon-remove-sign"></i> 删除</button>
</div>
<div id="container"></div>
<script type="text/javascript">
var data = [{"name":"\u767e\u5ea6","y":1239,"sliced":true,"selected":true},["google",998], 
["\u641c\u641c",342],["\u5fc5\u5e94",421],["\u641c\u72d7",259],["\u5176\u4ed6",83]] ;
var chart = new Highcharts.Chart({ 
        chart: { 
            renderTo: 'container',  //饼状图关联html元素id值 
            defaultSeriesType: 'pie', //默认图表类型为饼状图 
            plotBackgroundColor: '#ffc',  //设置图表区背景色 
            plotShadow: true   //设置阴影 
        }, 
        title: { 
            text: '搜索引擎统计分析'  //图表标题 
        },
        credits: {
		     enabled: false
		},
        tooltip: { 
            formatter: function() { //鼠标滑向图像提示框的格式化提示信息 
                return '<b>' + this.point.name + '</b>: ' +  
                (this.percentage).toFixed(2) + ' %'; 
            } 
        }, 
        plotOptions: { 
            pie: { 
                allowPointSelect: true, //允许选中，点击选中的扇形区可以分离出来显示 
                cursor: 'pointer',  //当鼠标指向扇形区时变为手型（可点击） 
                //showInLegend: true,  //如果要显示图例，可将该项设置为true 
                dataLabels: { 
                    enabled: true,  //设置数据标签可见，即显示每个扇形区对应的数据 
                    color: '#000000',  //数据显示颜色 
                    connectorColor: '#999',  //设置数据域扇形区的连接线的颜色 
                    style:{ 
                        fontSize: '12px'  //数据显示的大小 
                    }, 
                    formatter: function() { //格式化数据 
                        return '<b>' + this.point.name + '</b>: ' +  
                        (this.percentage).toFixed(2) + ' %'; 
                    } 
                } 
            } 
        }, 
        series: [{ //数据列 
            name: 'search engine', 
            data: data //核心数据列来源于php读取的数据并解析成JSON 
        }] 
    }); 
</script>