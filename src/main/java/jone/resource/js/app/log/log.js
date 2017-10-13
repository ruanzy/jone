define(['util'], function(util){
	var obj = {
		init: function(){
			var me = this;
			util.render('html/log/list.html', 'right');
			var data = me.getOperator();
	        $('#operator').select2({
	            allowClear: true,
	            placeholder: util.i18n('PLEASESELECT'),
	            data : data
	        }).change(function(e){
	            //alert(e.val);
	        });
	        $('#begintime,#endtime').on('click', function(){
	        	WdatePicker({lang: util.lang()});
	        });
	        me.list();
	        $('#search').on('click', function(){
				var operator = $('#operator').val();
				var begintime = $('#begintime').val();
				var endtime = $('#endtime').val();
				var memo = $('#memo').val();
				var params = {
					operator: operator,
					begintime: begintime,
					endtime: endtime,
					memo: memo
				};
				$('#list').grid('setQueryParam', params);
				$('#list').grid('reload');
			});
	        $('#del').on('click', function(){
	        	var rows = $('#list').grid('getSelected');
	        	if(rows.length < 1){
	            	dialog({
	            	    title: util.i18n('ALERT'),
	            	    content: util.i18n('LOG_DELETE_SELECT'),
	            	    okValue: util.i18n('DONE'),
	            	    ok: $.noop
	            	}).showModal();
	        		return;
	        	}
	        	var ids = [];
	        	for(var k in rows){
	        		ids.push(rows[k].id);
	        	}
	        	me.del(ids.join());
	        });
		},
		getOperator: function(){
			var ret = null;
			var url = "log/getOperator";
			$.ajax({
				url: url,
				type: 'post',
				async: false,
				dataType: 'json',
		        success: function(result){
		        	ret = result;
				}
			});
			return ret;
		},
		list: function(){
			$('#list').grid({
				sm : 'm',
				url : 'log/list',
				pagesize : 10,
				columns: [
					{ header: util.i18n('LOG_OPERATOR'), field: 'operator'},
					{ header: util.i18n('OPERATE'), field: 'operation', render : opRender},
					{ header: "IP" , field: 'ip'},
					{ header: util.i18n('LOG_OPERATETIME'), field: 'time'}
					//{ header: "备注" , field: 'memo', tip: true}
				]
			});
			function opRender(v){
				return util.i18n(v);
			}
		},
		del: function(ids){
        	dialog({
        	    title: util.i18n('DELETE'),
        	    content: util.i18n('LOG_DELETE_CONFIRM'),
        	    okValue: util.i18n('DONE'),
        	    ok: function () {
    				var url = "log/del";
    				var param = {ids: ids};
    				$.ajax({
    					url:url,
    					type: 'post',
    					data: param,
    					dataType: 'json',
    			        success: function(result){
    			        	$('#list').grid('reload');
    					}
    				});
        	    },
        	    cancelValue: util.i18n('CANCEL'),
        	    cancel: $.noop
        	}).showModal();
		}
	};
	return obj;
});