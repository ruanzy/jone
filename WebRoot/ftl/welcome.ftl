<div class='row'>
<div class='col-md-4'>
<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">服务器信息</h3>
	</div>
	<div class="panel-body">
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
<span>内存使用情况:</span>
<div class="progress">
  <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" id='pb3'>
  </div>
</div>
<div style='font-size:10px;'><span id='used' style='color:red;'></span>/<span id='total'></span></div>
</p>
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
</div>
</div>
</div>
<div class='col-md-8'>
<div class="panel panel-success">
	<div class="panel-heading">
		<h3 class="panel-title">服务器信息</h3>
	</div>
	<div class="panel-body">
	
	<p id="Dynamic">Dynamic page content</p>
<p id="pagination-here"></p>
 
	"sAjaxSource": "demo/table_ajax.php", // ajax source
		<table class="table table-striped table-hover table-bordered" id="sample_editable_2">
		<thead>
		<tr>
			<th>
				 Username
			</th>
			<th>
				 Full Name
			</th>
			<th>
				 Points
			</th>
			<th>
				 Notes
			</th>
			<th>
				 Edit
			</th>
			<th>
				 Delete
			</th>
		</tr>
		</thead>
		<tbody>
		</tbody>
		</table>
	</div>
</div>
	</div>
</div>

<div class="panel panel-default">
    <div class="panel-heading">
        <div class="panel-title">
            Ajax 异步获取数据
        </div>
    </div>
    <div class="panel-body">
							<div class="table-toolbar">
								<div class="btn-group">
									<button id="sample_editable_1_new" class="btn green">
									Add New <i class="fa fa-plus"></i>
									</button>
								</div>
								<div class="btn-group pull-right">
									<button class="btn dropdown-toggle" data-toggle="dropdown">Tools <i class="fa fa-angle-down"></i>
									</button>
									<ul class="dropdown-menu pull-right">
										<li>
											<a href="#">
												 Print
											</a>
										</li>
										<li>
											<a href="#">
												 Save as PDF
											</a>
										</li>
										<li>
											<a href="#">
												 Export to Excel
											</a>
										</li>
									</ul>
								</div>
							</div>
							<table class="table table-striped table-hover table-bordered" id="sample_editable_1">
							<thead>
							<tr>
								<th>
									 Username
								</th>
								<th>
									 Full Name
								</th>
								<th>
									 Points
								</th>
								<th>
									 Notes
								</th>
								<th>
									 Edit
								</th>
								<th>
									 Delete
								</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>
									 alex
								</td>
								<td>
									 Alex Nilson
								</td>
								<td>
									 1234
								</td>
								<td class="center">
									 power user
								</td>
								<td>
									<a class="edit" href="javascript:;">
										 Edit
									</a>
								</td>
								<td>
									<a class="delete" href="javascript:;">
										 Delete
									</a>
								</td>
							</tr>
							<tr>
								<td>
									 lisa
								</td>
								<td>
									 Lisa Wong
								</td>
								<td>
									 434
								</td>
								<td class="center">
									 new user
								</td>
								<td>
									<a class="edit" href="javascript:;">
										 Edit
									</a>
								</td>
								<td>
									<a class="delete" href="javascript:;">
										 Delete
									</a>
								</td>
							</tr>
							<tr>
								<td>
									 nick12
								</td>
								<td>
									 Nick Roberts
								</td>
								<td>
									 232
								</td>
								<td class="center">
									 power user
								</td>
								<td>
									<a class="edit" href="javascript:;">
										 Edit
									</a>
								</td>
								<td>
									<a class="delete" href="javascript:;">
										 Delete
									</a>
								</td>
							</tr>
							<tr>
								<td>
									 goldweb
								</td>
								<td>
									 Sergio Jackson
								</td>
								<td>
									 132
								</td>
								<td class="center">
									 elite user
								</td>
								<td>
									<a class="edit" href="javascript:;">
										 Edit
									</a>
								</td>
								<td>
									<a class="delete" href="javascript:;">
										 Delete
									</a>
								</td>
							</tr>
							<tr>
								<td>
									 webriver
								</td>
								<td>
									 Antonio Sanches
								</td>
								<td>
									 462
								</td>
								<td class="center">
									 new user
								</td>
								<td>
									<a class="edit" href="javascript:;">
										 Edit
									</a>
								</td>
								<td>
									<a class="delete" href="javascript:;">
										 Delete
									</a>
								</td>
							</tr>
							<tr>
								<td>
									 gist124
								</td>
								<td>
									 Nick Roberts
								</td>
								<td>
									 62
								</td>
								<td class="center">
									 new user
								</td>
								<td>
									<a class="edit" href="javascript:;">
										 Edit
									</a>
								</td>
								<td>
									<a class="delete" href="javascript:;">
										 Delete
									</a>
								</td>
							</tr>
							</tbody>
							</table>
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
				$('#pb3').width(pv2).html(pv2);
				$('#totalThread').text(totalThread);
				$('#used').html(use + 'M');
				$('#total').html(total + 'M');
			}
		});
		//setTimeout(onData, 2000);
	}
	onData();
	TableEditable.init();
	$('#pagination-here').bootpag({
	    total: 38,          // total pages
	    //page: 1,            // default page
	    maxVisible: 10,     // visible pagination
	    leaps: true,         // next/prev leaps through maxVisible
		next: 'next',
	   	prev: 'prev'
	}).on("page", function(event, num){
	    $("#Dynamic").html("Page " + num); // or some ajax content loading...
	});
</script>