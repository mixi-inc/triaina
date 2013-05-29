(function(){
var DialogPage = (function(){
    var Klass = function(){
        this.initialize();

    };
    (function(){
        this.initialize = function(){
            $('#confirmSubmit').click($.scope(this, this._display));
        };

        this._display = function() {
            var title = $('#confirmTitle').val() || "Confirm";
            var body = $('#confirmBody').val() || "no message.";
            WebBridge.call('system.dialog.confirm', {'title' : title, 'body': body});
        };
    }).apply(Klass.prototype);
    return Klass;
})();

var page = new DialogPage();

})();

