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

var client = new BriskClient({});

function loadPages() {
    var list = $(".page-list");
    list.empty();
    var pages = client.getPages(function(pages){
        for (var i = 0; i < pages.length; i++) {
            var page = pages[i];
            var el = $("<a/>").attr("href", "#")
                .addClass("list-group-item")
                .addClass("page-item")
                .text(page.name)
                .click(function(e){
                    e.preventDefault();
                    var self = $(this);
                    $("#page-name").val(self.text());
                    $(".page-item").removeClass("active");
                    self.addClass("active");
                });
            $(".page-list").append(el);
        };
    });
}