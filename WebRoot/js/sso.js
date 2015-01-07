$.ajax({
	url : 'http://11.0.0.106:8088/JOne/common/cookies',
	dataType : "jsonp",
	jsonpCallback : "callback",
	data:"key=SSOTOKEN&callback=?",
	success : function(data) {
		if (data) {
			$("body").append(this.name + "=" + this.value);
		} else {
			document.location = 'http://11.0.0.106:8088/JOne/login.html',
		}
	}
});