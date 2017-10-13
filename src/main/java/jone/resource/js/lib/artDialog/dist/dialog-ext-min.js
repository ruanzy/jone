$.extend({
	alert : function(msg, callback) {
		var d = dialog({
		    title: '提示',
		    content: msg,
		    width : 300,
		    okValue: '确定',
		    ok: function () {
		    	callback && callback.call(this);
		    },
		});
		d.showModal();
		return d;
	},
	confirm : function(msg, callback) {
		var d = dialog({
		    title: '确认',
		    content: msg,
		    width : 300,
		    okValue: '确定',
		    ok: function () {
		    	callback && callback.call(this, true);
		    },
		    cancelValue: '取消',
		    cancel: function () {
		    	callback && callback.call(this, false);
		    }
		});
		d.showModal();
		return d;
	},
	dialog : function(option) {
		var d = dialog(option);
		d.showModal();
		return d;
	}
});