<div class="well" style='width:45%;height:auto;'>
	<div class="well-header well-header-success">
		<span class='well-header-title'> <i class="icon-desktop"></i>
			服务器信息
		</span>
		<div class='well-header-buttons'>
			<i class='icon-plus'></i>
		</div>
	</div>
	<div class="well-body">
		<p>
服务器IP: ${ip}
</p>
<p>
操作系统: ${os}
</p>
<p>
CPU个数: ${processors}
</p>
<p>
JAVA版本: ${version}
</p>
<p>
最大可用内存: ${max}G
</p>
<p>		
当前线程总数:<span id='totalThread'><span>
</p>
<!--
<p>		
<span>CPU使用情况:</span><div class='progress'><div class='progress-bar' id='pb1'></div></div>
</p>-->
<p>		
<span>内存使用情况:</span><div class='progress'><div class='progress-bar' id='pb2'></div></div>
</p>
	</div>
</div>
<div class="well" style='width:auto;height:auto;display:none;'>
	<div class="well-header well-header-success">
		<span class='well-header-title'> <i class="icon-desktop"></i>
			内存情况
		</span>
		<div class='well-header-buttons'>
			<i class='icon-plus'></i>
		</div>
	</div>
	<div class="well-body">
		<canvas id="chart-area" width="201" height="201"></canvas>
		<!--<p><span class='block block-success'></span>总内存:<span id='total'>0</span></p>-->
		<p><span class='block block-danger'></span>已用内存:<span id='use'>0</span></p>
		<p><span class='block block-warning'></span>剩余内存:<span id='free'>0</span></p>
	</div>
</div>
<script type="text/javascript">
	$('.well-header-buttons').click(function(){
		$(this).parent().siblings('.well-body').toggle();
	});
	var ctx = document.getElementById("chart-area").getContext("2d");
	var data = [
		{
			value: 90,
			color: "#f4b400"
		},
		{
			value : 20,
			color : "#d73d32"
		}		
	];
	var chart = new Chart(ctx).Pie(data, {animation : false});
	$('#total').html(110.5);
	$('#free').html(90);
	$('#use').html(20);
	PL._init();
	PL.joinListen('/serverinfo');
	function onData(event) {
		var total = event.get("total");
		var free = event.get("free");
		var use = event.get("use");
		var totalThread = event.get("totalThread");
		var cpuRatio = event.get("cpuRatio");
		$('#total').html(total);
		$('#free').html(free);
		$('#use').html(use);
		//chart.segments[0].value = use;
		chart.segments[0].value = free;
		chart.update();
		var pv1 = '0.00%';
		var pv2 = ((use/total)*100).toFixed(2) + '%';
		$('#pb1').width(pv1).html(pv1);
		$('#pb2').width(pv2).html(pv2);
		$('#totalThread').text(totalThread);
	}
</script>