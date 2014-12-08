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
<div style='font-size:10px;'><span id='used' style='color:red;'></span>/<span id='total'></span></div>
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
	function onData() {
 		$.ajax({
			url : 'svr/mem',
			type : 'get',
			async : false,
			dataType : 'json',
			success : function(result) {
				var total = result.total;
				var free = result.free;
				var use = result.use;
				var totalThread = result.totalThread;
				var pv1 = '0.00%';
				var pv2 = ((use/total)*100).toFixed(2) + '%';
				$('#pb1').width(pv1).html(pv1);
				$('#pb2').width(pv2).html(pv2);
				$('#totalThread').text(totalThread);
				$('#used').html(use + 'M');
				$('#total').html(total + 'M');
			}
		});
		//setTimeout(onData, 2000);
	}
	onData();
</script>