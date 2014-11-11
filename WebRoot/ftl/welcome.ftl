<div class="well" style='width:auto;height:auto;'>
	<div class="well-header well-header-success">
		<span class='well-header-title'> <i class="icon-desktop"></i>
			服务器信息
		</span>
		<div class='well-header-buttons'>
			<i class='icon-plus'></i>
		</div>
	</div>
	<div class="well-body">
		<pre>
服务器IP: ${ip}

操作系统: ${os}

CPU个数: ${processors}

JAVA版本: ${version}

最大可用内存: ${max}G
		</pre>
	</div>
</div>
<div class="well" style='width:auto;height:auto;'>
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
		$('#total').html(total);
		$('#free').html(free);
		$('#use').html(use);
		//chart.segments[0].value = use;
		chart.segments[0].value = free;
		chart.update();
	}
</script>