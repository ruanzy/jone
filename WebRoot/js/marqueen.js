(function($) {
	$.fn.textSlider = function(settings) {
		settings = jQuery.extend({
			speed : "normal",
			line : 2,
			timer : 3000
		}, settings);
		return this.each(function() {
			$.fn.textSlider.scllor($(this), settings);
		});
	};
	$.fn.textSlider.scllor = function($this, settings) {
		var ul = $("ul:eq(0)", $this);
		var timerID;
		var li = ul.children();
		var liHight = $(li[0]).height();
		var upHeight = 0 - settings.line * liHight;// 婊氬姩鐨勯珮搴︼紱
		var scrollUp = function() {
			ul.animate({
				marginTop : upHeight
			}, settings.speed, function() {
				for (i = 0; i < settings.line; i++) {
					ul.find("li:first", $this).appendTo(ul);
				}
				ul.css({
					marginTop : 0
				});
			});
		};
		var autoPlay = function() {
			timerID = window.setInterval(scrollUp, settings.timer);
		};
		var autoStop = function() {
			window.clearInterval(timerID);
		};
		// 浜嬩欢缁戝畾
		ul.hover(autoStop, autoPlay).mouseout();
	};
})(jQuery);