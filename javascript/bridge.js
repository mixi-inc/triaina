/**
@fileOverview Bridge(Triaina)
@author shinya.takimoto<shinya.takimoto@gmail.com>
@description
Webリソースを利用し、Dynamicなネイティブアプリケーションを実装するためのフレームワーク
JSON-RPCのようなメッセージプロトコルを使用し、その呼び出しと受け取りのインターフェースを提供します
@example
// Webview -> Device -> Webview
WebBridge.call('form.media.click', {
    filter: ["jpeg", "png"],
    max: 1
}, function(response){
    // response data
    // {
    //     bridge: "1.1",
    //     dest: "form.media.click",
    //     result: [{
    //         id: "xxxxxxx",
    //         filename: "",
    //     }]
    // }
    // create input data & added input value
    fileStoreElement.appendChild(input);
});
*/

(function(){

// WebBridge
var WebBridge = (function(){
    // constants
    var BRIDGE_VERSION = "1.1";
    var BRIDGE_ID_PREFIX = "bridge.";
    var DEBUG = true;

    // DeviceBridge
    var existsDeviceBridge = function(){
        return !!(window.DeviceBridge);
    };

    var isRequestNotify = function(notify){
        return (notify && notify.hasOwnProperty('params')) ? true : false;
    }

    var Klass = function(){
        this.initialize();
    };
    (function(){
        this.initialize = function(){
            // manage ids
            this._ids = {};
            this._idsCount = 0;
            // status
            this._isEnableBridge = false;
            this._isEnableLog  = false;
            this._isBuildingBridge = false;
            // callbacks by request from web
            this._callbackTable = {};
            // observers of request form device
            this._observerTable = {};
            // wait callback on enabled bridge
            this._waitersOnEnabled = [];
        };
        this._buildBridge = function(option){
            if(this._isBuildingBridge) return;
            this._isBuildingBridge = true;
            if(!option) option = {};
            this._isEnableLog = DEBUG;

            var _self = this;
            if(existsDeviceBridge()){
                this._notifyAvailabled(function(notify){
                    _self._isBuildingBridge = false;
                });
            }else{
                var _self = this;
                window.WebBridge.observe("system.device.status", function(notify){
                    _self.log("observe system.device.status");
                    if(notify.params && notify.params.status && (notify.params.status == "opened")){
                        _self._notifyAvailabled(function(notify){
                            _self._isBuildingBridge = false;
                        });
                    }
                });
            }
        };
        this._notifyAvailabled = function(option, callback){
            var _self = this;
            this.call("system.web.status", {
                "status": "available"
            }, function(notify){
                _self._isEnableBridge = true;
                if(callback) callback(notify);
                for(var i=0;i<_self._waitersOnEnabled.length;i++){
                    var waiter = _self._waitersOnEnabled[i];
                    if(waiter) waiter();
                }
                _self._waitersOnEnabled = [];
            });
        };
        this.isEnableBridge = function(){
            return this._isEnableBridge;
        };
        this.onStart = function(callback){
            if(this._isEnableBridge){
                callback();
            }else{
                this._waitersOnEnabled.push(callback);
                this._buildBridge();
            }
        };
        this.getId = function(){
            var id = BRIDGE_ID_PREFIX + this._idsCount;
            this._ids[id] = true;
            this._idsCount++;
            return id;
        };
        this.call = function(dest, params, callback){
            var request = {
                bridge: BRIDGE_VERSION,
                dest: dest,
                params: params,
                id: this.getId()
            };
            this.execute(request, callback);
        };
        this.notify = function(dest, params){
            var request = {
                bridge: BRIDGE_VERSION,
                dest: dest,
                params: params,
                id: null
            };
            return this.execute(request);
        };
        this.execute = function(request, callback){
            this.log(request);
            var requestJSON = JSON.stringify(request);
            if(callback && request.id){
                this._callbackTable[request.id] = callback;
            }
            if(existsDeviceBridge()){
                window.DeviceBridge.notifyToDevice(encodeURIComponent(requestJSON));
                return true;
            }else{
                this.onStart(function(){
                    window.DeviceBridge.notifyToDevice(encodeURIComponent(requestJSON));
                });
                if(!this._isEnableBridge) this._buildBridge();
                console.error("not found DeviceBridge JavaScriptInterface");
                console.error("stack DeviceBridge notity");
                console.error(requestJSON);
            }
        };
        this.notifyToWeb = function(notify){
            var json;
            try{
                json = JSON.parse(decodeURIComponent(notify));
            }catch(e){
                throw(e);
            }
            var id = json.id;

            if(isRequestNotify(json)){
                var observers = this._observerTable[json.dest];
                if(observers){
                    for(var i=0;i<observers.length;i++){
                        var observer = observers[i];
                        observer(json);
                    }
                }
            }else{
                // [todo] callbackを消す
                var callback = this._callbackTable[id];
                if(callback) callback(json);
            }
        };
        this.observe = function(dest, observer){
            if(!dest && !observer) return;
            if(!this._observerTable[dest]){
                this._observerTable[dest] = [];
            }
            this._observerTable[dest].push(observer);
        };
        this.log = function(message){
            if(this._isEnableLog) console.log(message);
        };
    }).apply(Klass.prototype);
    return Klass;
})();

window.WebBridge = new WebBridge();

})();
