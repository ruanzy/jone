$.ajax({
	url : 'http://localhost:8088/JOne/common/cookies',
	async : false,
	dataType : "jsonp",
	jsonpCallback : "callback",
	success : function(data) {
		var SSOTOKEN;
		if (data) {
			$.each(data, function() {
				var name = this.name;
				if (name == 'SSOTOKEN') {
					SSOTOKEN = this;
					return false;
				}
			});
		}
		if (SSOTOKEN) {
			var loginuser = SSOTOKEN['value'].split('_')[0];
			$('#loginuser').text(loginuser);
		} else {
			document.location = 'http://11.0.0.106:8088/JOne/login.jsp?go=http://11.0.0.106:8088/LogStat';
		}
	}
});