(function(){
var NetworkPage = (function(){
    var Klass = function(){
        this.initialize();
    };
    (function(){
        this.initialize = function(){
	    $('#wifi_enable_btn').click(this._enableWiFi);
	    $('#wifi_disable_btn').click(this._disableWiFi);
	    $('#wifi_mac_btn').click(this._getMacAddress);

	    $('#post_btn').click(this._post);
        };
	this._enableWiFi = function() {
	    WebBridge.notify('system.wifi.enable');
	};
	this._disableWiFi = function() {
	    WebBridge.notify('system.wifi.disable');
	};
	this._getMacAddress = function() {
	    WebBridge.call('system.wifi.mac.get', function(res) {
		$('#wifi_mac_text').text(res['result']['mac_address']);
	    });
	};

	this._post = function() {
	    WebBridge.call('system.net.http.send', {'url': location.href, 'method': 'POST', 'body':
						    {'value1': 'data1'}, 'notification':
						    {'progress': 'uploading..', 'completed': 'completed!!', 'failed': 'failed!!', 'summary': 'data1'}},
			   function(res) {
		$('#post_state_text').text(res['result']['code']);
	    });
	};
    }).apply(Klass.prototype);
    return Klass;
})();

var page = new NetworkPage();

})();

