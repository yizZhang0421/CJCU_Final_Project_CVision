<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <meta name="google-signin-scope" content="profile email">
    <meta name="google-signin-client_id"
        content="488772557570-m50f06mgi4lqnkki7jevo3cjkgtqercc.apps.googleusercontent.com">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link href="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.core.css" rel="stylesheet">
    <link href="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.default.css" rel="stylesheet">
    <script src="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.min.js"></script>
    <title>客製化影像辨識</title>

    <?php 
        include('global_config.php');
        $name = $_POST['name'];
        $key = $_POST['key'];
        $model_name = $_POST['model_name'];
        $model_id = $_POST['model_id'] ;
        $model_status = $_POST['model_status'];
        $id_token = $_POST['id_token'] ;
        ?>

    <style>
        .box {
            width: 100%;
            height: 100%;
            text-align: center;
        }

        .box:hover {
            transition-property: width, height;
            transition-duration: 0.3s;
            width: 120%;
        }

        #label_name:hover {
            font-size: 2.5vw;
        }

        #label_name,
        .deleteImg {
            display: inline-block;
            text-align: center;
        }

        #container_actionButton {
            display: inline-block;

        }

        #identifyButton {
            display: block;
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

        #Signin,
        #signout {

            font-size: 1vw;

        }

        #div_del {
            border-right: 100px solid black;
            height: 100%;
        }
    </style>


    <script type="text/javascript">


        /* <![CDATA[ */
        var ip = "<?php echo $base_url; ?>";
        var name = "<?php echo $name; ?>";
        var key = "<?php echo $key; ?>";
        var model_id = "<?php echo $model_id; ?>";
        var id_token = "<?php echo $id_token; ?>";
        var label_json = null;
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
            ShowHello();
            getlabel_list();
            console.log(key);
        }




        function ShowHello() {
            document.getElementById('Signin').innerHTML = name + ' 你好！';
            document.getElementById("signout").innerHTML = '<div onclick="signOut()">(登出)</div>';
        }

        function getlabel_list() {
            $.post({
                url: ip + 'label_list',
                data: {
                    key: encodeURIComponent(key),
                    model_id: encodeURIComponent(model_id)
                },
                type: "POST",
                // dataType: 'json',
                success: function (msg) {
                    msg = decodeURIComponent(msg);
                    label_json = JSON.parse(msg);
                    // console.log(label_json);
                    var label_table = document.getElementById("label_table");
                    var labelHTML = "";
                    var temp = null;
                    var mid = null;
                    var label_temp = null;
                    var count = 0;
                    labelHTML += "<tr>";

                    for (var i = 0; i < label_json.data.length;) {
                        if (count != 2) {
                            temp = i;
                            mid = "'" + label_json.data[i].id + "'";
                            label_temp = "'" + label_json.data[i].name + "'";
                            var deleteImg = "<div class='box'><img  style='float:left;' src='./icon/deleteImg.png' class='deleteImg' onclick=\"delete_label(" + label_temp + "," + mid + ")\" ></div>";
                            var divName = "<div id='label_name' onclick=\"doFormRequest_toLabelImg('Labelimage.php','post'," + label_temp + "," + mid + ")\">" + label_json.data[i].name + "</div>";                            
                            if(label_json.data.length == 1){
                                labelHTML += "<td width='4.6%' style='cursor:pointer;' class='label_click'>" + deleteImg + "</td>" + "<td width='45.4%' style='cursor:pointer;' class='label_click'>" + divName + "</td>";
                            }
                            else{
                                labelHTML += "<td width='7%' style='cursor:pointer;' class='label_click'>" + deleteImg + "</td>" + "<td width='43%' style='cursor:pointer;' class='label_click'>" + divName + "</td>";
                            
                            }                            
                            count++;
                            i++;
                            continue;
                        }
                        else {
                            count = 0;
                            labelHTML += "</tr>";
                            if (i != label_json.data.length - 1) {
                                labelHTML += "<tr>";

                            }

                        }
                    }
                    label_table.innerHTML = labelHTML;
                },
                error: function (xhr, ajaxOptions, thrownError) {
                }
            });
        }

        function create_label() {

            alertify.prompt("請輸入label名稱：", function (e, str) {
                if (e) {

                    labelname = str.trim();
                    if (labelname === '') {
                        alertify.log("label名稱不得為空白!");
                    }

                    else if (labelname != null) {

                        $.post({
                            url: ip + 'create_label',
                            data: {
                                key: encodeURIComponent(key),
                                label_name: encodeURIComponent(labelname),
                                model_id: encodeURIComponent(model_id)
                            },
                            type: "POST",
                            success: function (msg) {
                                msg = decodeURIComponent(msg);
                                msg = JSON.parse(msg);
                                if (msg.message == "ok") {
                                    alertify.log('已新增label：' + str);

                                    getlabel_list();
                                }
                                else if (msg.message == 'invalid operate, duplicate name') {
                                    console.log(msg.message);
                                    alertify.log("發生錯誤 : label名稱重複");
                                }
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                            }
                        });



                    }

                }
                else {
                }
            }, "Ex : label1");
        }


        function delete_label(label_name, label) {
            var name = label_name;
            alertify.confirm('是否確定刪除 ' + name + '  label?', function (e) {
                if (e) {
                    $.post({
                        url: ip + 'delete_label',
                        data: {
                            key: encodeURIComponent(key),
                            label_id: encodeURIComponent(label)
                        },
                        type: "POST",
                        success: function (msg) {
                            msg = decodeURIComponent(msg);
                            msg = JSON.parse(msg);
                            if (msg.message == "ok") {
                                alertify.log("label已成功刪除");
                                getlabel_list();
                            } else {
                                alertify.log("有錯誤");
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                        }
                    });
                }

            })

        }


        function doFormRequest(url, action) {
            var login_json = { "key": key, "name": name, "id_token": id_token };
            var form = document.createElement("form");
            console.log(url);
            form.action = url;
            form.method = action;

            // append input attribute and valus
            for (var keys in login_json) {
                if (login_json.hasOwnProperty(keys)) {
                    var val = login_json[keys];
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



        function doFormRequest_toLabelImg(url, action, label_name, id) {
            var form = document.createElement("form");
            console.log(url);
            form.action = url;
            form.method = action;

            // append input attribute and valus
            var label_json = { "key": key, "model_id": model_id, "model_name": model_name, "label_name": label_name, "label_id": id, "name": name, "id_token": id_token, "model_status": model_status };

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

        function signOut() {
            var auth2 = gapi.auth2.getAuthInstance();
            auth2.signOut().then(function () {
                window.location.href = 'index.php';
                console.log('User signed out.');
            });
        }

        function Introduction(){
            alertify.alert("<?php echo $teach; ?>", function(){});
        }
    </script>

    <link rel='stylesheet' id='all-css-0-1'
        href='https://s2.wp.com/_static/??-eJyNU1ty2zAMvFAQNg+3X52chaQgCTYpcAjKrm9fUEoU13bY/GgAaBeLF80pgeep4FRMnCGFeaBJzCl5jiCRAp6vvEcv8mDu02R24jOlQqxezyHwqYUf+YgZ3OxcQGWXc8ANTpMPc6dhDZjOykiaQB4jTbeQvZii/w+O/2xGSzfQobKwJOsPsHhf6JIU4wIvKJdtPr9XeVnGHQHbKQCcze+z2/yVRGbisvSzGa1snjNqPCZbKiJiRxYDRoW1aDH9/GBVc9RemzJrqc6ljCKg30hzhDKq0C1vDZs0O5Nt5wP1PcLz1Q7/A7YiWNZBLzP+toruaFWCpeZmU9QNVQRn/csHQgj2ZArGFGy5ObpGAmFPNsC6tUunRR6QQTuz9UH840AfLOUWVQ8DLw4Jjs8tdEad4KDmsIzz022RCgtkTJwL9Jzjtf+9A49WCuZaYX3MmerT2WLNFL4OoqbYrK/2P76aIbCzoQLe4u+n3e7H7mn38vpr/xeIoMk8?cssminify=yes'
        type='text/css' media='all' />
    <style id='wpcom-admin-bar-inline-css' type='text/css'>
        .admin-bar {
            position: inherit !important;
            top: auto !important;
        }

        .admin-bar .goog-te-banner-frame {
            top: 32px !important
        }

        @media screen and (max-width: 782px) {
            .admin-bar .goog-te-banner-frame {
                top: 46px !important;
            }
        }

        @media screen and (max-width: 480px) {
            .admin-bar .goog-te-banner-frame {
                position: absolute;
            }
        }
    </style>
    <style id='radcliffe-2-style-inline-css' type='text/css'>
        .hero-area:before {
            opacity: 0.4;
        }
    </style>
</head>

<body>
    <div id="page" class="site">
        <header id="masthead">
            <br>
            <div align="right" style="margin-right:2%" id="signcontent">
                <div class="g-signin2" data-onsuccess="onSignIn" data-prompt="select_account" id="Signin"
                    style="display:inline-block;margin-top:0%;">
                </div>
                <div id='signout' style="display:inline-block; cursor:pointer;"></div>
            </div>
            <div class="header-wrapper">
                <div class="site-branding">
                    <a href="index.php" class="custom-logo-link" rel="home" itemprop="url"><img width="399" height="300"
                            src="./logo.png" class="custom-logo" alt="客製化影像辨識" itemprop="logo"
                            sizes="(max-width: 740px) 100vw, 740px" data-attachment-id="61"
                            data-permalink="https://123456.health.blog/cropped-imageedit_5_2410613130-2-png/"
                            data-orig-file="https://123456health.files.wordpress.com/2019/02/cropped-imageedit_5_2410613130-2.png"
                            data-orig-size="800,600" data-comments-opened="1"
                            data-image-meta="{&quot;aperture&quot;:&quot;0&quot;,&quot;credit&quot;:&quot;&quot;,&quot;camera&quot;:&quot;&quot;,&quot;caption&quot;:&quot;&quot;,&quot;created_timestamp&quot;:&quot;0&quot;,&quot;copyright&quot;:&quot;&quot;,&quot;focal_length&quot;:&quot;0&quot;,&quot;iso&quot;:&quot;0&quot;,&quot;shutter_speed&quot;:&quot;0&quot;,&quot;title&quot;:&quot;&quot;,&quot;orientation&quot;:&quot;0&quot;}"
                            data-image-title="cropped-imageedit_5_2410613130-2.png" data-image-description="&lt;p&gt;https://123456health.files.wordpress.com/2019/02/cropped-imageedit_5_2410613130-2.png&lt;/p&gt;
                                                                                           "
                            data-medium-file="https://123456health.files.wordpress.com/2019/02/cropped-imageedit_5_2410613130-2.png?w=300"
                            data-large-file="https://123456health.files.wordpress.com/2019/02/cropped-imageedit_5_2410613130-2.png?w=740" /></a>
                    <div class="site-branding-text">
                        <h1 class="site-title"><a href="" rel="home">客製化影像辨識</a></h1>
                    </div><!-- .site-branding-text -->
                </div><!-- .site-branding -->
            </div><!-- .header-wrapper -->
            <div class="menu-wrapper">
                <nav id="site-navigation" class="main-navigation">
                    <div>
                        <ul id="header-menu" class="menu">
                            <li id="menu-item-65"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-65"><a
                                    href="index.php">首頁</a></li>
                            <li>
                                <a style="cursor:pointer;" onclick="doFormRequest('modellist.php','post')">模型</a></li>
                            <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-66">
                                <a style="cursor:pointer;" onclick="create_label()">新增label</a>
                            </li>
                            <li id="synopsis" style="cursor:pointer;">
                                <a onclick="Introduction()">操作說明</a>
                            </li>
                        </ul>
                    </div>
                </nav><!-- #site-navigation -->
            </div><!-- .menu-wrapper -->
        </header><!-- #masthead -->

        <h1 id='site-title' class="site-title" style="text-align:center;"></h1>
        <div class="container" style="margin-top:5%; text-align:center;">
            <table id="label_table" border="2" style="font-size:3vw;">

            </table>

        </div>
        <div class="menu-wrapper">
            <nav id="site-navigation" class="main-navigation" style="margin-left:46%; ">
                <div id='container_actionButton'>
                    <ul id="header-menu" class="menu">
                        <li><a style="cursor:pointer;font-size: 140%;font-family:Microsoft JhengHei;"
                                onclick="Train()">開始訓練</a></li>
                    </ul>
                    <ul id="header-menu" class="menu">
                        <li id="identifyButton"><a
                                style="cursor:pointer;font-size: 140%;font-family:Microsoft JhengHei;"
                                onclick="Identify('predict.php','post')">開始辨識</a></li>
                    </ul>
                </div>
            </nav>
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

    <!--  -->

    <script>

        var model_name = "<?php echo $model_name;?>";
        var model_status = "<?php echo $model_status; ?>";
        console.log(model_status);
        document.getElementById('site-title').innerHTML = '' + model_name;

        if (model_status != "0") {
            document.getElementById("identifyButton").style.display = "block";
        }
        else {
            document.getElementById("identifyButton").style.display = "none";
        }



        function Train() {
            $.post({
                url: ip + 'train',
                data: {
                    key: encodeURIComponent(key),
                    model_id: encodeURIComponent(model_id)
                },
                type: "POST",
                // dataType: 'json',
                success: function (msg) {
                    // msg = decodeURIComponent(msg);
                    msg = JSON.parse(msg);
                    console.log(msg);
                    if (msg.message == "ok") {
                        doFormRequest('modellist.php', 'post');
                    }
                    else if (msg.message == "invalid operate, cannot change anything while model training.") {
                        alertify.log("模型訓練中，無法在執行訓練");
                    }
                    else if (msg.message == "invalid operate, label count less than two.") {
                        alertify.log("類別數量小於2");
                    }
                    else if(msg.message == "invalid operate, include label which num of image less then 10."){
                        alertify.log("圖片數量至少要10張");
                    }

                },
                error: function (xhr, ajaxOptions, thrownError) {
                }
            });

        }

        function Identify(url, action) {


            var form = document.createElement("form");
            console.log(url);
            form.action = url;
            form.method = action;


            var label_json = { "key": key, "model_id": model_id, "name": name, "id_token": id_token };

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



    </script>





</body>

</html>