(function(){
var SensorPage = (function(){
    var Klass = function(){
        this.initialize();

    };
    (function(){
        this.initialize = function(){
	    $('#acc_enable_btn').click($.scope(this, this._enableAcc));
	    $('#acc_disable_btn').click(this._disableAcc);
        };

	this._enableAcc = function() {
	    WebBridge.observe('accCallback', this._accCallback);
	    WebBridge.call('system.sensor.accelerometer.enable', {'rate' : 100, 'callback': 'accCallback'});
	};
	this._disableAcc = function() {
	    WebBridge.notify('system.sensor.accelerometer.disable');
	};
	this._accCallback = function(acc) {
	    $('#acc_x_text').text('x:' + acc['params']['x']);
	    $('#acc_y_text').text('y:' + acc['params']['y']);
	    $('#acc_z_text').text('z:' + acc['params']['z']);
	};
    }).apply(Klass.prototype);
    return Klass;
})();

var page = new SensorPage();

})();
