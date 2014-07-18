var BriskClient = (function($, undefined){

    function BriskClient(options) {
        var o = options || {};
        this.site = o.site || "";
        this.base = o.base || "/_api";
    }

    BriskClient.prototype.getPages = function(callback) {
        verifyCallback(callback);
        $.getJSON(this.base + "/pages/", {}, callback);
    };

    BriskClient.prototype.getPage = function(key, callback) {
        verifyCallback(callback);
        $.getJSON(this.base + "/pages/" + key, {}, callback);
    };

    function verifyCallback(callback) {
        if (callback === undefined)
            throw "Callback missing";
        if (typeof (callback) !== "function")
            throw "Callback is not a function";
    }

    return BriskClient;

})(jQuery);

var client = new BriskClient();