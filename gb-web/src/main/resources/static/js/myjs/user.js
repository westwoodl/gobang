function recordListShow() {
    displayWhat("record_div");

    $("#record_page_tr").hover(function () {
        $("#record_page_tr").attr({"style": "background:#ffffff"});
    }, function () {
        $("#record_page_tr").attr({"style": "background:#ffffff"});
    });
    $("body").css("background", "#F8F9FA");

    useLayuiPage('record_page_div');

    console.log("record");
    return false;
}

function userDetailShow() {
    displayWhat("user_detail_div");
    $("body").css("background", "#F8F9FA");
    userStartShow(3.5);
    return false;
}

function userStartShow(start_num) {
    layui.use('rate', function () {
        var rate = layui.rate;

        //渲染
        var ins1 = rate.render({
            elem: '#user_start_div'  //绑定元素
            , value: start_num
            , half: true
            , readonly: true
        });
    });
}

/**
 * 用户信息修改弹出层
 */
function updateUserDetail(obj) {
    var update_user_html =
        '<input type="text" id="search_user_input" name="title" required lay-verify="required" placeholder="请输入用户名" autocomplete="off" class="layui-input">' +
        '<span id="span"></span>';
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            title: '查找用户'
            , resize: false
            , content: update_user_html
            , yes: function (index, layero) {
                //do something
                console.log(index);
                var $dom = $(layero);
                // console.log($("#room_create_name_input").val());
                // console.log($("#room_create_pwd_input").val());
                var user_name = $dom.find("#search_user_input").val();

                if (user_name.length < 1) {
                    $dom.find("#span").html("请输入用户名");
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                    return;
                }

                if (user_name.length > 4) {
                    $dom.find("#span").html("用户名长度不会大于4");
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                    return;
                }
                if (queryUserRequest(user_name) === true) {
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                } else {
                    $dom.find("#span").html("系统繁忙，查询失败");
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                }
            }
        });
    });
}

function searchUser() {
    var search_user_html =
        '<input type="text" id="search_user_input" name="title" required lay-verify="required" placeholder="请输入用户名" autocomplete="off" class="layui-input">' +
        '<span id="span"></span>';
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            title: '查找用户'
            , resize: false
            , content: search_user_html
            , yes: function (index, layero) {
                //do something
                console.log(index);
                var $dom = $(layero);
                // console.log($("#room_create_name_input").val());
                // console.log($("#room_create_pwd_input").val());
                var user_name = $dom.find("#search_user_input").val();

                if (user_name.length < 1) {
                    $dom.find("#span").html("请输入用户名");
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                    return;
                }

                if (user_name.length > 4) {
                    $dom.find("#span").html("用户名长度不会大于4");
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                    return;
                }
                if (queryUserRequest(user_name) === true) {
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                } else {
                    $dom.find("#span").html("系统繁忙，查询失败");
                    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
                }
            }
        });
    });
    return false;
}

function loginOut() {
    //todo
    return false;
}


function loginOrRegister() {


    let login_user_html =
        '<div style="display: flex">' +
        '<i class="layui-icon layui-icon-email" style="margin-right: 10px;margin-top:5px;font-size: 30px; color: gray"></i>' +
        '<input type="text" id="account_input" name="title" required lay-verify="required" placeholder="请输入账号" autocomplete="off" class="layui-input">' +
        '</div>' +

        '<div  style="margin-top: 10px;display: flex">' +
        '<i class="layui-icon layui-icon-key" style="margin-right: 10px;margin-top:5px;font-size: 30px; color: gray"></i>' +
        '<input type="text" id="pwd_input" name="title" required lay-verify="required" placeholder="请输入密码" autocomplete="off" class="layui-input">' +
        '</div>';

    let register_user_html =
        '<div style="display: flex">' +
        '<i class="layui-icon layui-icon-face-smile" style="margin-right: 10px;margin-top:5px;font-size: 30px; color: gray"></i>' +
        '<input type="text" id="reg_name_input" name="title" required lay-verify="required" placeholder="请输入昵称" autocomplete="off" class="layui-input">' +
        '</div>' +

        '<div  style="margin-top: 10px;display: flex">' +
        '<i class="layui-icon layui-icon-email" style="margin-right: 10px;margin-top:5px;font-size: 30px; color: gray"></i>' +
        '<input type="text" id="reg_account_input" name="title" required lay-verify="required" placeholder="请输入账号" autocomplete="off" class="layui-input">' +
        '</div>' +

        '<div  style="margin-top: 10px;display: flex">' +
        '<i class="layui-icon layui-icon-key" style="margin-right: 10px;margin-top:5px;font-size: 30px; color: gray"></i>' +
        '<input type="text" id="reg_pwd_input" name="title" required lay-verify="required" placeholder="请输入密码" autocomplete="off" class="layui-input">' +
        '</div>';


    let login_and_register_html = '\n' +
        '<span id="span"></span>' +
        '<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">\n' +
        '  <ul class="layui-tab-title">\n' +
        '    <li class="layui-this">登录</li>\n' +
        '    <li>注册</li>\n' +
        '  </ul>\n' +
        '  <div class="layui-tab-content" style="height: 100px;">\n' +
        '    <div class="layui-tab-item layui-show">\n' +
        login_user_html +
        '    </div>\n' +
        '    <div class="layui-tab-item">\n' +
        register_user_html +
        '    </div>\n' +
        '  </div>\n' +
        '</div> ';
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            title: '用户'
            , resize: false
            , content: login_and_register_html
            , yes: function (index, layero) {
                //do something
                console.log(index);
                let $dom = $(layero);

                let account_input = $dom.find("#account_input").val();
                let pwd_input = $dom.find("#pwd_input").val();
                let reg_name_input = $dom.find("#reg_name_input").val();
                let reg_account_input = $dom.find("#reg_account_input").val();
                let reg_pwd_input = $dom.find("#reg_pwd_input").val();

                if ($dom.find(".layui-this").html() === "登录") {
                    console.log(account_input);
                    console.log(pwd_input);
                    if (checkLoginParameter(account_input, pwd_input, $dom) && userLoginRequest(account_input, pwd_input, $dom)) {
                        layer.close(index); //如果设定了yes回调，需进行手工关闭
                        alertLayer("登录成功");
                    }
                } else {
                    //注册亲求
                    console.log(reg_name_input);
                    console.log(reg_account_input);
                    console.log(reg_pwd_input);
                    if (checkRegParameter(reg_name_input, reg_account_input, reg_pwd_input, $dom) && userRegisterRequest(reg_name_input, reg_account_input, reg_pwd_input, $dom)) {
                        layer.close(index); //如果设定了yes回调，需进行手工关闭
                        alertLayer("注册成功");
                    }
                }
            }
        });
    });
    return false;
}

//登录亲求
function userLoginRequest(account_input, pwd_input, $dom) {
    let isSuccess = false;
    $.ajax({
        async: false,
        type: "GET",
        url: base_gobang_url + "/user/login",
        xhrFields: {withCredentials:true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {
            account: account_input,
            password: pwd_input
        },
        dataType: "json",
        success: function (data) {
            domTipsShow($dom, data.msg);
            isSuccess = data.success;
            if(isSuccess) {
                $("#user_a").html(
                    '<img src="http://pic4.zhimg.com/50/v2-7fece9a613445edb78271216c8c20c6d_hd.jpg" class="layui-nav-img">\n' +
                    '&nbsp;' + data.data.userName
                );
            }
        },
        error: function () {
            alert("系统繁忙");
        }
    });
    return isSuccess;
}

//注册亲求
function userRegisterRequest(reg_name_input, reg_account_input, reg_pwd_input, $dom) {
    let isSuccess = false;
    $.ajax({
        async: false,
        type: "POST",
        url: base_gobang_url + "/user",
        xhrFields: {withCredentials:true},	//前端适配：允许session跨域
        crossDomain: true,
        data: {
            userName: reg_name_input,
            account: reg_account_input,
            password: reg_pwd_input
        },
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

function queryUserRequest(user_name) {
    //todo
    return false;
}


function displayUserDetail() {
    return false;
}


/**
 * 参数校验 适用于登录和注册
 */

function checkRegParameter(reg_name_input, reg_account_input, reg_pwd_input, $dom) {
    if (reg_name_input.length > 1 && reg_name_input.length < 6) {
        return checkLoginParameter(reg_account_input, reg_pwd_input, $dom);
    } else {
        domTipsShow($dom, "昵称只能是1-5位");
        return false;
    }
}

function domTipsShow($dom, msg) {
    $dom.find("#span").html(msg);
    $dom.find("#span").attr("style", "font-size: 10px;color: red;margin-top: 20px;");
}

function checkLoginParameter(account_input, pwd_input, $dom) {
    if (checkOneParameter(account_input)) {
        if (checkOneParameter(pwd_input)) {
            return true;
        } else {
            domTipsShow($dom, "密码只能是3-10位的数字和字母");
            return false;
        }
    } else {
        domTipsShow($dom, "账号只能是3-10位的数字和字母");
        return false;
    }
}

function checkOneParameter(one_str) {
    if (one_str.length < 1) {
        return false;
    }
    if (one_str.length < 3) {
        return false;
    }
    if (one_str.length > 10) {
        return false;
    }
    return checkS(one_str);
}

checkS = function(str){
    // 判断字符串是否为数字和字母组合
    let zg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]*$/;

    if (!zg.test(str)) {
        return /^[A-Za-z0-9]+$/.test(str);
    } else {
        return true;
    }
};