(function(){
var BasicPage = (function(){
    var Klass = function(){
        this.initialize();
    };
    (function(){
        this.initialize = function(){
	    $('#toast_short_btn').click(this._showToastShort);
	    $('#toast_long_btn').click(this._showToastLong);
	    $('#toast_cancel_btn').click(this._cancelToast);

	    $('#vibrator_btn').click(this._vibrate);
	    $('#vibrator_cancel_btn').click(this._cancelVibrater);

	    $('#notification_btn').click($.scope(this, this._notify));
	    $('#notification_clear_btn').click($.scope(this, this._clearNotification));
        };
        this._showToastShort = function(){
	    WebBridge.notify('system.toast.show', {'text': 'Hello!!', 'duration': 0});
        };
	this._showToastLong = function(){
	    WebBridge.notify('system.toast.show', {'text': 'Hellooooooo!!', 'duration': 1});
        };
	this._cancelToast = function(){
	    WebBridge.notify('system.toast.cancel');
        };

	this._vibrate = function() {
	    WebBridge.notify('system.vibrator.vibrate', {'msec': 5000});
	};
	this._cancelVibrater = function() {
	    WebBridge.notify('system.vibrator.cancel');
	};

	this._notify = function() {
	    WebBridge.call('system.notification.notify',
			   {'priority': 1, 'title': 'Hello', 'text': 'world', 'icon': 'info', 'view': 'notification'}, $.scope(this,
			   function(res) {
			       this._notificationId = res['result']['id'];
			   }));
	};
	this._clearNotification = function() {
	    WebBridge.notify('system.notification.clear', {'id': this._notificationId});
	};
    }).apply(Klass.prototype);
    return Klass;
})();

var page = new BasicPage();

})();

