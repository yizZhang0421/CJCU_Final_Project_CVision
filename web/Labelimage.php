<!doctype html>
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
    <script src="//cdn.jsdelivr.net/npm/alertifyjs@1.11.2/build/alertify.min.js"></script>

    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.11.2/build/css/alertify.min.css" />
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.11.2/build/css/themes/default.min.css" />
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.11.2/build/css/themes/semantic.min.css" />
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.11.2/build/css/themes/bootstrap.min.css" />

    <title>客製化影像辨識</title>


    <link rel='stylesheet' id='all-css-0-1'
        href='https://s2.wp.com/_static/??-eJyNU1ty2zAMvFAQNg+3X52chaQgCTYpcAjKrm9fUEoU13bY/GgAaBeLF80pgeep4FRMnCGFeaBJzCl5jiCRAp6vvEcv8mDu02R24jOlQqxezyHwqYUf+YgZ3OxcQGWXc8ANTpMPc6dhDZjOykiaQB4jTbeQvZii/w+O/2xGSzfQobKwJOsPsHhf6JIU4wIvKJdtPr9XeVnGHQHbKQCcze+z2/yVRGbisvSzGa1snjNqPCZbKiJiRxYDRoW1aDH9/GBVc9RemzJrqc6ljCKg30hzhDKq0C1vDZs0O5Nt5wP1PcLz1Q7/A7YiWNZBLzP+toruaFWCpeZmU9QNVQRn/csHQgj2ZArGFGy5ObpGAmFPNsC6tUunRR6QQTuz9UH840AfLOUWVQ8DLw4Jjs8tdEad4KDmsIzz022RCgtkTJwL9Jzjtf+9A49WCuZaYX3MmerT2WLNFL4OoqbYrK/2P76aIbCzoQLe4u+n3e7H7mn38vpr/xeIoMk8?cssminify=yes'
        type='text/css' media='all' />


    <?php
    include('global_config.php');
    $key = $_POST['key'] ;
    $name = $_POST['name'];
    $model_id = $_POST['model_id'] ;
    $label_id = $_POST['label_id'] ;
    $label_name = $_POST['label_name'];
    $id_token = $_POST['id_token'];
    $model_name = $_POST['model_name'] ;
    $model_status = $_POST['model_status'];
    
    ?>


    <script>
        var ip = "<?php echo $base_url; ?>";
        var key = "<?php echo $key; ?>";
        var name = "<?php echo $name; ?>";
        var model_name = "<?php echo $model_name; ?>";
        var model_id = "<?php echo $model_id; ?>";
        var label_id = "<?php echo $label_id; ?>";
        var id_token = "<?php echo $id_token; ?>";
        var model_status = "<?php echo $model_status; ?>";
        var img_json = null;
        window.onload = function () {
            getimage_path();
            document.getElementById('Signin').innerHTML = name + ' 你好！';
            document.getElementById("signout").innerHTML = '<div onclick="signOut()">(登出)</div>';
        };
        function doFormRequest(url, action) {
            var login_json = { "key": key, "name": name, "id_token": id_token };
            var form = document.createElement("form");

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

        function getimage_path() {
            $.post({
                url: ip + 'label_images',
                data: {
                    key: encodeURIComponent(key),
                    label_id: encodeURIComponent(label_id)
                },
                type: "POST",
                // dataType: 'json',
                success: function (msg) {
                    msg = decodeURIComponent(msg);
                    img_json = JSON.parse(msg);

                    var table = document.getElementById("imgTable");
                    var table_html = "";
                    for (var i = 0; i < img_json.data.length; i++) {
                        var img_path = ip + "get_image?key=" + encodeURIComponent(key) +
                            "&label_id=" + encodeURIComponent(label_id)
                            + "&image_id=" + img_json.data[i];
                        table_html += '<div class="Box"><img src="' + img_path + '"class="img-responsive"><input class="imgCheck" type="checkbox" name="1"></div>'
                    }

                    table.innerHTML = table_html;
                },
                error: function (xhr, ajaxOptions, thrownError) {
                }
            });
        }
        function signOut() {
            var auth2 = gapi.auth2.getAuthInstance();
            auth2.signOut().then(function () {
                window.location.href = 'index.php';
                console.log('User signed out.');
            });
        }

        function deleteAction() {

            var array1 = [];

            var container = document.getElementsByClassName("imgCheck");

            for (var i = 0; i < container.length; i++) {
                if (container[i].checked == true) {
                    var temp = container[i].parentNode.childNodes;

                    for (var j = 0; j < temp.length; j++) {
                        if (temp[j].nodeName.toLocaleLowerCase() == 'img') {
                            var stringIndex = temp[j].src.search("image_id=");
                            var stringTemp = temp[j].src.substring(stringIndex + 9, temp[j].src.length);
                            array1.push(stringTemp);
                        }
                    }
                }

            }



            for (var k = 0; k < array1.length; k++) {

                var image = array1[k];

                $.post({
                    url: ip + 'delete_image',
                    data: {
                        key: encodeURIComponent(key),

                        label_id: encodeURIComponent(label_id),
                        image_id: encodeURIComponent(image),
                    },
                    type: "POST",
                    success: function (msg) {
                        msg = decodeURIComponent(msg);
                        msg = JSON.parse(msg);
                        console.log(msg.message);
                        if (msg.message == "ok") {
                        } else {
                            alert("有錯誤");
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                    }
                });

            }
            // getimage_path();
            console.log(array1.length);
            var time = array1.length * 150;
            del_Timeout = setTimeout(getimage_path, time);
            var actionBox = document.getElementById("container_actionButton").style.display = "none";

        }


        function deleteImg() {

            var show = document.getElementsByClassName("imgCheck");
            for (var i = 0; i < show.length; i++) {
                show[i].style.display = "block";


            }
            var actionBox = document.getElementById("container_actionButton").style.display = "block";



        }

        function cancel() {
            var show = document.getElementsByClassName("imgCheck");
            for (var i = 0; i < show.length; i++) {
                show[i].style.display = "none";
            }
            var actionBox = document.getElementById("container_actionButton").style.display = "none";

        }



        function signOut() {
            var auth2 = gapi.auth2.getAuthInstance();
            auth2.signOut().then(function () {
            });
        }




        function doFormRequest_toLabel(url, action) {
            var form = document.createElement("form");

            form.action = url;
            form.method = action;

            // append input attribute and valus
            var label_json = { "key": key, "name": name, "model_name": model_name, "model_id": model_id, "model_status": model_status, "id_token": id_token };

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
        function Introduction() {
            alertify.alert("<?php echo $teach; ?>", function () { });
        }
    </script>
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
    <style>
        .Box {
            border-style: solid;
            width: 19%;
            height: 0;
            overflow: hidden;
            float: left;
            margin-left: 1%;
            margin-top: 3%;
            padding-bottom: 14.55%;
            position: relative;
        }

        .Box>img {
            position: absolute;
            width: 100%;
            height: 100%
        }

        .upload_cover {
            position: relative;
            width: 100px;
            height: 100px;
            text-align: center;
            cursor: pointer;
            transition: opacity 0.5s;

        }

        .upload_cover:hover {
            opacity: 0.5;
        }

        #upload_input {
            display: none;
        }

        .upload_icon {
            font-weight: bold;
            font-size: 180%;
            position: absolute;
            margin-top: -13%;
            left: 0;
            width: 70%;
            height: 70%;
            top: 20%;
        }

        .delAvatar {
            position: absolute;
            right: 2px;
            top: 2px;
        }

        .imgCheck {
            position: absolute;
            top: 0;
            right: 0;
            zoom: 180%;
            display: none;

        }

        #container_actionButton {
            margin-top: 10%;
            margin-left: 41%;
            display: none;
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
                            <li><a style="cursor:pointer;" onclick="doFormRequest('modellist.php','post')">模型</a></li>
                            <li><a style="cursor:pointer;" onclick="doFormRequest_toLabel('label.php','post')">label</a>
                            </li>

                            <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-66">
                                <!-- 请选取一个图像文件:<input type="file" id="file" name="file[]" multiple="multiple"/>  -->
                                <label class="upload_cover">
                                    <input id="upload_input" type="file" name="file[]" multiple="multiple" />
                                    <img class="upload_icon" src='./icon/add.png'>
                                    <i class="delAvatar fa fa-times-circle-o" title="刪除"></i>
                                </label>
                            </li>

                            <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-66">
                                <label class="upload_cover">
                                    <img class="upload_icon" src='./icon/delete.png' onclick="deleteImg();">
                                </label>
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
        <div class="container">
            <div class="row">
                <div class="col-md-12" id="imgTable">
                </div>
            </div>
        </div>

        <script>
            var label_name = "<?php echo $label_name; ?>";
            var file_input = document.getElementById('upload_input');
            alertify.message('圖片上傳時請勿離開此頁面',3 );  
            //為了處理新增完即時更新圖片所設置的變數 k             
            document.getElementById('site-title').innerHTML = label_name;
            file_input.addEventListener("change", function () {  
                var k = 0;   
                var alertmsg = alertify.message('',500000 );  
                for (var i = 0; i < file_input.files.length; i++) {
                    //console.log("initial i : " + i);
                    var file = file_input.files[i];
                    // console.log(file);
                    var reader = new FileReader();
                    reader.onload = function (event) {
                        // txt拿到圖片的base64字串
                        var base64 = event.target.result;
                        //字串處理，把字串前面的一些雜訊過濾掉(得到純粹的像素字串)
                        var hPosition = base64.search(",");
                        var ree = base64.substring(hPosition + 1, base64.length);                        
                        //這邊呼叫上傳
                        $.post({
                            url: ip + 'write_image',
                            data: {
                                key: encodeURIComponent(key),
                                label_id: encodeURIComponent(label_id),
                                base64_image: encodeURIComponent(ree)
                            },
                            type: "POST",
                            // dataType: 'json',
                            success: function (msg) {
                                msg = decodeURIComponent(msg);
                                var progress = Math.round(k/i*100);
                                // console.log(progress);
                                var dic = "<div style='background-color:#5555ff;width:"+progress+"%'>"+progress+"%</div>"
                                console.log(dic);
                                alertmsg.setContent("上傳中<br>"+dic);                                                        
                                k++
                                // console.log("k:" + k);
                                if (k == i) {                                    
                                    getimage_path();
                                    alertmsg.dismiss();
                                    alertify.success('上傳完成!!');
                                }
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                            }
                        });

                    };

                    reader.readAsDataURL(file);
                }




            });
        </script>




        <div class="menu-wrapper">
            <nav id="site-navigation" class="main-navigation">
                <div id='container_actionButton'>
                    <ul id="header-menu" class="menu">
                        <li><a style="cursor:pointer;font-size: 140%;font-family:Microsoft JhengHei;"
                                onclick="cancel()">取消</a></li>
                        <li style="visibility:hidden;"><a> </a></li>
                        <li id="identifyButton"><a
                                style="cursor:pointer;font-size: 140%;font-family:Microsoft JhengHei;"
                                onclick="deleteAction()">確定刪除</a></li>
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






</body>

</html>