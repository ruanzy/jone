$APITEST = {
	init: function(){
		var me = this;
		util.render('html/apitest.html', 'rui-content');
		$('body').addClass('hide-aside');
		$('#request').tabs({
			tabs: [
				{
					   title: '请求参数',
					   url: 'html/apitest/params.html',
					   open: function(){
							$('#addParam').on('click', function(){
								me.addParam();
							});
					   }
				},
				{
					title: 'Headers',
					url: 'html/apitest/headers.html',
					open: function(){
						$('#addHeader').on('click', function(){
							me.addHeader();
						});
					}
				}
			]
		});
		$('#btntest').on('click', function(){
			me.test();
		});
	},
	addParam: function(){
		var k = $.trim($('#param #k').val());
		var v = $('#param #v').val();
		if(k){
			var arr = [];
			arr.push('<tr>');
			arr.push('<td>', k, '</td>');
			arr.push('<td>', v, '</td>');
			arr.push('</tr>');
			$('#params').append(arr.join(''));
		}
	},
	addHeader: function(){
		var k = $.trim($('#header #k').val());
		var v = $('#header #v').val();
		var headers = {};
		if(k){
			var arr = [];
			arr.push('<tr>');
			arr.push('<td>', k, '</td>');
			arr.push('<td>', v, '</td>');
			arr.push('</tr>');
			$('#headers').append(arr.join(''));
			headers[k] = v;
		}
	},
	test: function(){
		var s = new Date().getTime();
		var url = $('#url').val();
		var headers = {};
		var trs = $('#headers > tbody > tr');
		$.each(trs, function(){
			var tds = $(this).find('td');
			var k = tds.eq(0).text();
			var v = tds.eq(1).text();
			headers[k] = v;
		});
		var params = {};
		var trs2 = $('#params > tbody > tr');
		$.each(trs2, function(){
			var tds = $(this).find('td');
			var k = tds.eq(0).text();
			var v = tds.eq(1).text();
			params[k] = v;
		});
		sessionStorage.token = headers['Authorization'];
		$.ajax({
			url : url,
			type : 'get',
			dataType : 'json',
			headers: headers,
			data: params,
			success : function(result) {
				var e = new Date().getTime();
				$('#result').html(JSON.stringify(result));
				$('#cost').html((e - s) + 'ms');
			}
		});
	}
};