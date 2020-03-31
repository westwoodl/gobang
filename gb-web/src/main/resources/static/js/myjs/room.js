function roomListShow() {
    displayWhat("room_div");

    $("#room_page_tr").hover(function () {
        $("#room_page_tr").attr({"style": "background:#ffffff"});
    }, function () {
        $("#room_page_tr").attr({"style": "background:#ffffff"});
    });
    $("body").css("background", "#F8F9FA");

    useLayuiPage('room_page_div');

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
                if(creatRoomRequest(room_name, room_pwd) === true) {
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                } else {
                    $dom.find("#span").html("系统繁忙，创建失败")
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                }
            }
        });
    });

}

function creatRoomRequest(room_name, room_password) {
    //TODO
    return false;
}

function queryRoomRequest(pageIndex, pageSize) {
    //TODO
    return false;
}

/**
 * 计划不用
 */
function useLayuiTable() {
    layui.use('table', function () {
        var table = layui.table;

        //第一个实例
        table.render({
            elem: '#demo'
            , height: 312
            , url: '/demo/table/user/' //数据接口
            , page: true //开启分页
            , cols: [[ //表头
                {field: 'id', title: 'ID', width: 80, sort: true, fixed: 'left'}
                , {field: 'username', title: '用户名', width: 80}
                , {field: 'sex', title: '性别', width: 80, sort: true}
                , {field: 'city', title: '城市', width: 80}
                , {field: 'sign', title: '签名', width: 177}
                , {field: 'experience', title: '积分', width: 80, sort: true}
                , {field: 'score', title: '评分', width: 80, sort: true}
                , {field: 'classify', title: '职业', width: 80}
                , {field: 'wealth', title: '财富', width: 135, sort: true}
            ]]
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