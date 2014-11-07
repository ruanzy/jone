var CACHE = {};
$.ajax({
	url:'common/dic',
	type: 'post',
	async: false,
	dataType: 'json',
    success: function(data){
    	CACHE['dic'] = data;
	}
});
$.ajax({
	url:'common/res',
	type: 'post',
	async: false,
	dataType: 'json',
    success: function(data){
    	CACHE['res'] = data;
	}
});