function roomListShow() {
    displayWhat("room_div");

    $("#room_page_tr").hover(function () {
        $("#room_page_tr").attr({"style": "background:#ffffff"});
    }, function () {
        $("#room_page_tr").attr({"style": "background:#ffffff"});
    });
    $("body").css("background", "#F8F9FA");

    roomUseLayuiPage();

    console.log("room");
    return false;
}

function alertLayer(msg) {
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.msg(msg);
    });
}

function creatRoom() {
    var create_room_html =
        '<input type="text" id="room_create_name_input" name="title" required lay-verify="required" placeholder="请输入房间名" autocomplete="off" class="layui-input" value="木头的房间">' +
        '<input type="password" id="room_create_pwd_input" name="password" style="margin-top: 10px" required lay-verify="required" placeholder="请输入房间密码（可以不输哦！）" autocomplete="off" class="layui-input">' +
        '<span id="span"></span>';
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            title: '创建房间'
            , resize: false
            , content: create_room_html
            , yes: function (index, layero) {
                //do something
                console.log(index);
                var $dom = $(layero);
                // console.log($("#room_create_name_input").val());
                // console.log($("#room_create_pwd_input").val());
                var room_name = $dom.find("#room_create_name_input").val();
                var room_pwd = $dom.find("#room_create_pwd_input").val();
                console.log(room_name.length);
                if (room_name.length < 1 || room_name.length > 7) {
                    $dom.find("#span").html("房间名长度不能大于七")
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                    return;
                }
                if(creatRoomRequest(room_name, room_pwd, $dom) === true) {
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            }
        });
    });

}

function creatRoomRequest(room_name, room_password, $dom) {
    let isSuccess = false;
    $.ajax({
        async: false,
        type: "POST",
        url: base_gobang_url + "/room/create",
        xhrFields: {withCredentials:true},	//前端适配：允许session跨域
        crossDomain: true,
        contentType: "application/json",
        data:JSON.stringify( {
            "roomName": room_name,
            "roomPassword": room_password
        }),
        dataType: "json",
        success: function (data) {
            domTipsShow($dom, data.msg);
            isSuccess = data.success;
        },
        error: function () {
            alert("系统繁忙");
        }
    });
    return isSuccess;
}

function queryRoomRequest(pageIndex, pageSize) {
    let totalCount = 0;
    $.ajax({
        async: false,
        type: "GET",
        url: base_gobang_url + "/room",
        xhrFields: {withCredentials:true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {
            pageIndex: pageIndex,
            pageSize: pageSize
        },
        dataType: "json",
        success: function (data) {
            isSuccess = data.success;
            alertLayer(data.msg);

            totalCount = data.data.totalCount;
            new Vue({
                el: '#room_table_div',
                data: {
                    dataList: data.data.data
                }
            });

        },
        error: function () {
            alert("系统繁忙");
        }
    });
    return totalCount;
}

function roomUseLayuiPage() {
    layui.use('laypage', function () {
        var laypage = layui.laypage;
        let count = queryRoomRequest(1, 10);
        //执行一个laypage实例
        laypage.render({
            elem: "room_page_div" //注意，这里的 test1 是 ID，不用加 # 号
            , count: count //数据总数，从服务端得到
            , layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'] //refresh
            , jump: function (obj, first) {
                //obj包含了当前分页的所有参数，比如：
                console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                console.log(obj.limit); //得到每页显示的条数
                // //首次不执行
                if (!first) {
                    queryRoomRequest(obj.curr, obj.limit);
                }
            }
        });
    });

}

function useLayuiPage(page_id) {

    layui.use('laypage', function () {
        var laypage = layui.laypage;

        //执行一个laypage实例
        laypage.render({
            elem: page_id //注意，这里的 test1 是 ID，不用加 # 号
            , count: 3 //数据总数，从服务端得到
            , layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'] //refresh
            , jump: function (obj, first) {
                //obj包含了当前分页的所有参数，比如：
                console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                console.log(obj.limit); //得到每页显示的条数
                //
                // //首次不执行
                // if (!first) {
                //     //do something
                // }
                queryRoomRequest(obj.curr, obj.limit);

            }
        });
    });

}