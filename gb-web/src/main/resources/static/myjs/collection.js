function collectionListShow() {
    if(!isLogin()) {
        return false;
    }
    displayWhat("collection_div");

    $("#collection_page_tr").hover(function () {
        $("#collection_page_tr").attr({"style": "background:#ffffff"});
    }, function () {
        $("#collection_page_tr").attr({"style": "background:#ffffff"});
    });
    $("body").css("background", "#F8F9FA");

    comingSoon();
    return false;
}

