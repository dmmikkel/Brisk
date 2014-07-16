var BriskClient = (function($, undefined){

    function BriskClient(options) {
        this.site = options.site || "";
        this.base = options.base || "/_api";
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

/* Data binding */
(function(BriskBinding, $, undefined){

    var briskClient = new BriskClient({});

    var pages;

    var objectPageListeners;
    var eachPageListeners;

    $(function(){
        objectPageListeners = $("*[data-brisk-object='page']");
        eachPageListeners = $("*[data-brisk-each='page']");

        loadPages();
    });

    BriskBinding.setPages = function(p) {
        pages = p;
        updatePages();
    };

    function loadPages() {
        briskClient.getPages(function(json){
            pages = json;
            updatePages();
        });
    }

    function updatePages() {
        for (var i = 0; i < pages.length; i++) {
            var page = pages[i];
            var objectListeners = objectPageListeners.filter(function(){
                return $(this).data("brisk-key") == page.key
            });
            mapObject(page, objectListeners);

        }
    }

    function mapObject(object, parents) {
        parents.find("*[data-brisk-property]").each(function(){
            var self = $(this);
            if (self.is("input")
                self.val(object[self.data("brisk-property")]);
            else
                self.text(object[self.data("brisk-property")]))
        });
    }

})(window.BriskBinding = window.BriskBinding || {}, jQuery);