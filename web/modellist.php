<html lang="en">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="google-signin-scope" content="profile email">
    <meta name="google-signin-client_id"
        content="488772557570-m50f06mgi4lqnkki7jevo3cjkgtqercc.apps.googleusercontent.com">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

    <link href="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.core.css" rel="stylesheet">
    <link href="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.default.css" rel="stylesheet">
    <script src="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.min.js"></script>

    <title>客製化影像辨識</title>
    <script type="text/javascript">
        function addLoadEvent(func) {
            var oldonload = window.onload;
            if (typeof window.onload != 'function') {
                window.onload = func;
            } else {
                window.onload = function () {
                    oldonload();
                    func();
                }
            }
        }


        
        window.onload = function () {
            var temp = document.getElementsByClassName('abcRioButton');

            
            temp[0].style.setProperty('background-color', '#d9d9d9');
            temp[0].style.setProperty('width', '95%');
        }


    </script>

    <style id='css' type="text/css">
        img.wp-smiley,
        td {
            font-size: 2.2vw;
            width: 33%;
        }

        .TableTitle {
            font-size: 3.5vw;
            width: 33%;
            font-weight: bold;
        }

        .box {
            width: 25%;
            height: 25%;
            margin-top: 2.5%;
            text-align : center;
            padding-bottom: 16.55%;
            position: relative;
            cursor:pointer ;
        }

        .box>deleteImg {
            position: absolute;
            width: 100%;
            height: 100%;
        }

        .box:hover {
            transition-property: width, height;
            transition-duration: 0.3s;
            width: 27%;
        }

        #model_name {
            margin-left: 10px;
            font-size: 2.0vw;
        }

        #model_name:hover {
            transition-property: font-size;
            transition-duration: 0.3s;
            font-size: 2.3vw;
        }

        #model_name,
        #deleteImg {
            display: inline-block;
            text-align: center;

        }

        #Modeltable {
            border: 3px;
        }

        #colophon {
            background-color: #555555;
            color: #ffffff;
            margin-top: 5%;
            font-family: Microsoft JhengHei;
        }

        #masthead {
            background: linear-gradient(180deg, #BBBBBB 0%, #EEEEEE 100%);
            background: -moz-linear-gradient(180deg, #BBBBBB 0%, #EEEEEE 100%);
            background: -webkit-linear-gradient(270deg, #BBBBBB 0%, #ffffff 100%);
            background: -o-linear-gradient(180deg, #BBBBBB 0%, #EEEEEE 100%);
        }

        #footerImg {

            display: inline-block;

            -webkit-filter: blur(1px) brightness(50%) opacity(75%);

        }

        #footerImg:hover {

            transition-property: -webkit-filter;
            transition-duration: 0.3s;
            -webkit-filter: blur(0px) brightness(100%) opacity(100%);


        }

        .footerText {
            font-size: 13px;
            font-family: 'proxima-nova', sans-serif;
            display: inline-block;

        }

        #Main_content {}

        #Text2::before {
            content: " | ";
            color: #f2f2f2;
        }

        #Text3::before {
            content: " | ";
            color: #f2f2f2;
        }

        .copyright {
            float: right;
            font-size: 13px;
            font-family: 'proxima-nova', sans-serif;
            color: #f2f2f2;

        }

        #Signin,#signout{

        font-size : 1vw ;

        }
    </style>
    <link rel='stylesheet' id='all-css-0-1'
        href='https://s2.wp.com/_static/??-eJyNU1ty2zAMvFAQNg+3X52chaQgCTYpcAjKrm9fUEoU13bY/GgAaBeLF80pgeep4FRMnCGFeaBJzCl5jiCRAp6vvEcv8mDu02R24jOlQqxezyHwqYUf+YgZ3OxcQGWXc8ANTpMPc6dhDZjOykiaQB4jTbeQvZii/w+O/2xGSzfQobKwJOsPsHhf6JIU4wIvKJdtPr9XeVnGHQHbKQCcze+z2/yVRGbisvSzGa1snjNqPCZbKiJiRxYDRoW1aDH9/GBVc9RemzJrqc6ljCKg30hzhDKq0C1vDZs0O5Nt5wP1PcLz1Q7/A7YiWNZBLzP+toruaFWCpeZmU9QNVQRn/csHQgj2ZArGFGy5ObpGAmFPNsC6tUunRR6QQTuz9UH840AfLOUWVQ8DLw4Jjs8tdEad4KDmsIzz022RCgtkTJwL9Jzjtf+9A49WCuZaYX3MmerT2WLNFL4OoqbYrK/2P76aIbCzoQLe4u+n3e7H7mn38vpr/xeIoMk8?cssminify=yes'
        type='text/css' media='all' />
    <?php
            include('global_config.php');
            $name = $_POST['name'];
            $key = $_POST['key'] ;
            $id_token = $_POST['id_token'];
?>
    <script>
        var ip = "<?php echo $base_url; ?>";
        var name = "<?php echo $name; ?>";
        var key = "<?php echo $key; ?>";
        var id_token = "<?php echo $id_token; ?>";
        console.log(key);
        var model_json = null;
        var model_name = [];
        var modelHTML = null;
        window.onload = function () {
            ShowHello();
            getprogress_list();
            getmodel_list();
        };

        function ShowHello() {
            document.getElementById('Signin').innerHTML = name + ' 你好！';
            document.getElementById("signout").innerHTML = '<div onclick="signOut()">(登出)</div>';
        }
        function getprogress_list() {

        }

        function getmodel_list() {
            modelHTML = '<tr>'
                + '<td class="TableTitle">name</td>'
                + '<td class="TableTitle">acc</td>'
                + '<td class="TableTitle">loss</td>'
                + '<td class="TableTitle">delete</td>'
                + '</tr>';
            $.post({
                url: ip + 'model_list',
                data: {
                    key: encodeURIComponent(key)
                },
                type: "POST",
                // dataType: 'json',
                success: function (msg) {
                    msg = decodeURIComponent(msg);
                    model_json = JSON.parse(msg);
                    console.log(model_json.message);
                    var temp = null;
                    var mid = null;
                    var model_temp = null;
                    var length = model_json.data.length;
                    for (var i = 0; i < model_json.data.length; i++) {
                        temp = i;
                        mid = "'" + model_json.data[i].id + "'";
                        model_temp = "'" + model_json.data[i].name + "'";
                        var deleteImg = "<div class='box'><img src='./icon/deleteImg.png' id='deleteImg' onclick=\"delete_model(" + model_temp + "," + mid + ")\" ></div>";
                        var divName = "<div id='model_name' onclick=\"doFormRequest_toLabel('label.php','post'," + temp + "," + mid + ")\">" + model_json.data[i].name + "</div>";
                        modelHTML += "<tr id='" + model_json.data[i].id + "'>";
                        if (model_json.data[i].loss == null) {
                            modelHTML += "<td style='cursor:pointer;' class='label_click'>"  + divName + "<div style='color:#ff3333;float:right; font-size:1.5vw;'>還沒訓練</div>" + "</td>" + "<div id='status_" + model_json.data[i].id + "' style='display:none;'>0</div>";
                        }
                        else {
                            modelHTML += "<td style='cursor:pointer;' class='label_click'>"  + divName + "<div style='color:#00D600;float:right;font-size:1.5vw;'>訓練完成</div>" + "</td>" + "<div id='status_" + model_json.data[i].id + "' style='display:none;'>1</div>";
                        }
                        modelHTML += "<td >" + model_json.data[i].acc + "</td>";
                        modelHTML += "<td>" + model_json.data[i].loss + "</td>";
                        modelHTML += "<td align='center'>" + deleteImg + "</td>";
                        modelHTML += "</tr>";
                        model_name[i] = model_json.data[i].name;
                    }

                    $.post({
                        url: ip + 'progress_list',
                        data: {
                            key: encodeURIComponent(key)
                        },
                        type: "POST",
                        // dataType: 'json',
                        success: function (msg) {
                            var Modeltable = document.getElementById("Modeltable");
                            msg = decodeURIComponent(msg);
                            model_json = JSON.parse(msg);
                            var temp = null;
                            var mid = null;
                            var model_temp = null;
                            for (var i = 0; i < model_json.data.length; i++) {
                                temp = length;
                                mid = "'" + model_json.data[i].id + "'";
                                model_temp = "'" + model_json.data[i].name + "'";
                                var deleteImg = "<div class='box'><img src='./icon/deleteImg.png' id='deleteImg' onclick=\"delete_model(" + model_temp + "," + mid + ")\" ></div>";
                                var divName = "<div id='model_name' onclick=\"doFormRequest_toLabel('label.php','post'," + temp + "," + mid + ")\">" + model_json.data[i].name + "</div>";
                                modelHTML += "<tr id='" + model_json.data[i].id + "'>";
                                modelHTML += "<td style='cursor:pointer;' class='label_click '>" + divName + "<div style='color:#ffcc00;float:right; font-size:1.5vw;'>訓練中</div>" + "</td>" + "<div id='status_" + model_json.data[i].id + "' style='display:none;'>0</div>";
                                modelHTML += "<td >" + "null" + "</td>";
                                modelHTML += "<td>" + "null" + "</td>";
                                modelHTML += "<td align='center'>" + deleteImg + "</td>";
                                modelHTML += "</tr>";
                                model_name[length] = model_json.data[i].name;
                                console.log(model_json.data[i]);
                                progress(model_json.data[i].id);
                                length = length+1
                            }
                            Modeltable.innerHTML = modelHTML;
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                        }
                    });

                },
                error: function (xhr, ajaxOptions, thrownError) {
                }
            });

        }

        function create_model() {
            
            alertify.prompt("請輸入模型名稱：", function (e, str) {
                if (e) {
                    var modelname = "";
                    modelname = str.trim();
                    console.log(modelname);
                    if (modelname == '') {
                        console.log('AAAAAAAA');
                        alertify.log('模型名稱不得為空白!');
                    }
                    else if (modelname != null) {
                        $.post({
                            url: ip + 'create_model',
                            data: {
                                key: encodeURIComponent(key),
                                model_name: encodeURIComponent(modelname)
                            },
                            type: "POST",
                            success: function (msg) {
                                msg = decodeURIComponent(msg);
                                msg = JSON.parse(msg);
                                console.log(msg.message);
                                if (msg.message == "ok") {
                                    alertify.log('已新增模型：' + str);
                                    getmodel_list();
                                } else {
                                    if (msg.message == 'invalid operate, duplicate name') {
                                        console.log(msg.message);
                                        alertify.log("發生錯誤 : model名稱重複");
                                    }
                                    else {
                                        alertify.log("有錯誤");
                                    }
                                }
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                            }
                        });
                    }
                }
                else {
                }
            }, "Ex : catRecog");



        }

        function delete_model(model_name, model_id) {
            
            
            var name = model_name;
            alertify.confirm('是否確定刪除 ' + name + '  模型?', function (e) {
                if (e) {
                    $.post({
                    url: ip + 'delete_model',
                    data: {
                        key: encodeURIComponent(key),
                        model_id: encodeURIComponent(model_id)
                    },
                    type: "POST",
                    success: function (msg) {
                        msg = decodeURIComponent(msg);
                        msg = JSON.parse(msg);
                        if (msg.message == "ok") {
                            alertify.log("model已成功刪除");
                            getmodel_list();
                        } else {
                            alertify.log("有錯誤");
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                    }
                });
                    
                } else {
                }
            });
        

        }

        function doFormRequest_toLabel(url, action, index, id) {
            var form = document.createElement("form");
            var elementId = "status_" + id;
            var statusCatch = document.getElementById(elementId).innerHTML;
            form.action = url;
            form.method = action;
            // append input attribute and valus
            var label_json = { "key": key, "name": name, "model_name": model_name[index], "model_id": id, "model_status": statusCatch, "id_token": id_token };

            for (var keys in label_json) {
                if (label_json.hasOwnProperty(keys)) {
                    var val = label_json[keys];
                    input = document.createElement("input");
                    input.type = "hidden";
                    input.name = keys;
                    input.value = val;

                    // append key-value to form
                    form.appendChild(input)
                }
            }

            // send post request
            document.body.appendChild(form);
            form.submit();

            // remove form from document
            document.body.removeChild(form);
        }

        function progress(model_id) {
            $.post({
                type: 'post',
                url: ip + 'progress',
                data: {
                    key: encodeURIComponent(key),
                    model_id: encodeURIComponent(model_id)
                },
                xhrFields: {
                    onprogress: function (e) {
                        var progressResponse;
                        var response = e.currentTarget.response;
                        var check = response.substring(response.length - 7, response.length - 1);
                        if (check == "finish") {
                            Training_finished(model_id);
                        }
                        else {
                            console.log("還在訓練");
                        }
                    }
                }
            }).done(function (data) {

            }).fail(function (error) {
                console.log('Error: ', error);
            });
        }

        function signOut() {
            var auth2 = gapi.auth2.getAuthInstance();
            auth2.signOut().then(function () {

                window.location.href='index.php';
                console.log('User signed out.');
            });
        }
        function Training_finished(model_id) {
            $.post({
                url: ip + 'get_model_info',
                data: {
                    key: encodeURIComponent(key),
                    model_id: encodeURIComponent(model_id)
                },
                type: "POST",
                success: function (msg) {
                    msg = decodeURIComponent(msg);
                    msg = JSON.parse(msg);
                    console.log(msg);
                    console.log("acc:" + msg.data[0].acc);
                    console.log("loss:" + msg.data[0].loss);
                    $("#" + model_id + " td:eq(0) div:eq(1)").text("訓練完成");
                    $("#" + model_id + " td:eq(0) div:eq(1)").css({ color: "#00D600" });
                    $("#" + model_id + " td:eq(1)").text(msg.data[0].acc);
                    $("#" + model_id + " td:eq(2)").text(msg.data[0].loss);
                    $("#status_" + model_id).text("1");
                },
                error: function (xhr, ajaxOptions, thrownError) {
                }
            });
        }
        function Introduction(){
            alertify.alert("<?php echo $teach; ?>", function(){});
        }
    </script>

    <style id='radcliffe-2-style-inline-css' type='text/css'>
        .hero-area:before {
            opacity: 0.4;
        }
    </style>

</head>

<body id="body">

    <div id="page" class="site">
        <header id="masthead" style="background-color:0000ff">

        <br>
            <div align="right" style="margin-right:2%" id="signcontent">
                <div class="g-signin2" data-onsuccess="onSignIn" data-prompt="select_account" id="Signin"
                    style="display:inline-block;margin-top:0%;">
                </div>
                <div id='signout' style="display:inline-block; cursor:pointer;"></div>
            </div>
            <div class="header-wrapper">
                <div class="site-branding">
                    <a class="custom-logo-link" rel="home" itemprop="url"><img width="445" height="407" src="./logo.png"
                            class="custom-logo" alt="客製化影像辨識" itemprop="logo" /></a>
                    <div class="site-branding-text">
                        <h1 class="site-title"><a href="index.php" rel="home">客製化影像辨識</a></h1>
                    </div><!-- .site-branding-text -->
                </div><!-- .site-branding -->
            </div><!-- .header-wrapper -->
            <div class="menu-wrapper">
                <nav id="site-navigation" class="main-navigation">
                    <div>
                        <ul id="header-menu" class="menu">
                            <li id="menu-item-65"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-65">
                                <a href="index.php">首頁</a></li>
                            <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-66">
                                <a style="cursor:pointer;" onclick="create_model()">增加模型</a>
                            </li>
                            <li id="synopsis" style="cursor:pointer;">
                                <a onclick="Introduction()">操作說明</a>
                            </li>
                        </ul>
                    </div>
                </nav><!-- #site-navigation -->
            </div><!-- .menu-wrapper -->
        </header><!-- #masthead -->


        <div class="container" style="text-align:center; margin-top:5%;" id="Main_content">
            <h1 class="site-title" style="text-align:center; margin-top: 5%">模型</h1>
            <table border="1" id="Modeltable" style="margin-top:5%">
                <tr>
                    <td class="TableTitle">name</td>
                    <td class="TableTitle">acc</td>
                    <td class="TableTitle">loss</td>
                </tr>
            </table>
        </div>


        <footer id="colophon">
            <div class="row">
                <div class="col-lg-1"></div>
                <div class="col-lg-4" style="font-size:13px;margin: 1.5% 0 1.5% 0;font-family:微軟正黑體;">
                    長榮大學資訊管理學系畢業專案發表<br>
                    成員:尤家駿、余冠靖、張逸宗<br>
                    指導老師:周信宏
                </div>
                <div class="col-lg-3"></div>
                <div class="col-lg-4" style="font-size:13px;margin: 1.5% 0 0.5% 0">
                    <a href="http://www.cjcu.edu.tw" class="logo">
                        <img src="http://www.cjcu.edu.tw/images/logo-O1.png?v=1550022993" alt="長榮大學校徽-回首頁" width="15%">
                        <img src="http://www.cjcu.edu.tw/images/cjcu.png?v=1550036037" alt="長榮大學書法題字" width="23%">
                    </a>
                </div>
            </div>
        </footer>
    </div><!-- #page -->


    <script>
                // function changeStatusColor(){
                //     var css = document.getElementById('css');
                //     var c = css.sheet ;
                //     var table = document.getElementById('Modeltable');
                //     var len = table.rows.length ;
                //     var _row = table.rows ;
                //     for(var i = 0;i < len;i++){
                //         var _cell = _row[i].cells;
                //         var acc=_cell[1].innerHTML;
                //         var loss = _cell[2].innerHTML ;
                //         if(acc=='acc')
                //         {
                //             continue ;
                //         }
                //         if(acc=='null' && loss == 'null'){
                //             table.rows[i].style.setProperty('background-color','#ff3333','important');
                //         }
                //         else{
                //             table.rows[i].style.setProperty('background-color','#66ff66','important');
                //         }
                //     }
                // }

    </script>
</body>







</html>
