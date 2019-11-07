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
    <script src="https://raw.githubusercontent.com/wilq32/jqueryrotate/master/jQueryRotate.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <script src="./js/exif.js"></script>
    <script src="./js/jQueryRotate.js"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link href="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.core.css" rel="stylesheet">
    <link href="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.default.css" rel="stylesheet">
    <script src="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.min.js"></script>
    <title>CVision</title>

    <?php 
        include('global_config.php');
        $key = $_POST['key'];
        $model_id = $_POST['model_id'] ;
        $name = $_POST['name'] ;
        $id_token = $_POST['id_token'];        
        ?>

    <style>
        .alertify{
            top : 0;
            font-size:2.6vh;
        }
        #upload_input {
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
        #uploadImg {
            width: 5vw;
            opacity: 1;
        }
        #uploadImg:hover {
            transition-property: opacity;
            transition-duration: 0.3s;
            opacity: 0.5;
        }
    </style>

    <script type="text/javascript">
        var ip = "<?php echo $base_url; ?>";
        var name = "<?php echo $name; ?>";
        var key = "<?php echo $key; ?>";
        var id_token = "<?php echo $id_token; ?>";
        window.onload = function () {
            var temp = document.getElementsByClassName('abcRioButton');
            temp[0].style.setProperty('background-color', '#d9d9d9');
            temp[0].style.setProperty('width', '95%');
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
        var model_id = "<?php echo $model_id; ?>";
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
            document.getElementById('Signin').innerHTML = name + ' 你好！';
            document.getElementById("signout").innerHTML = '<div onclick="signOut()">(登出)</div>';
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

        #upload {
            position: relative;
            width: 100%;
            margin: 0;
        }
        #identifyButton {
            cursor: pointer;
            font-size: 1.6vw;
            font-family: Microsoft JhengHei;
            border: 1px solid #ff0000;
            border-radius: 50% 50% 50% 50%;
            background: none;
            padding: 1% 2%;
            color: #ff0000;
            transition: 0.8s;
            position: relative;
            overflow: hidden;
        }
        #identifyButton:hover {
            color: #fff;
        }
        #identifyButton::before {
            top: 0;
            border-radius: 50% 50% 50% 50%;
            content: "";
            position: absolute;
            left: 0;
            width: 100%;
            height: 100%;
            background: #fff;
            z-index: -1;
            transition: 0.6s;
        }
        #identifyButton:hover::before {
            background: #ff1a1a;
        }
        #box {
            position: relative;
            overflow: hidden;
            margin: 20px;
        }
        #box:after {
            padding-top: 42.8%;
            content: "";
            display: block;
        }
        #blah {
            position: absolute;
            top: 0;
            bottom: 0;
            right: 0;
            left: 0;
            max-height: 100%;
            margin: auto;
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
            <div style="height:auto;" class="text-center">
                <a href="index.php"  rel="home" itemprop="url">
                    <img width="26%" height="auto" src="./logo1.png" class="rounded" alt="客製化影像辨識" itemprop="logo" />
                </a>
                <div class="site-branding-text">
                    <h1 class="site-title"><a href="index.php" rel="home">CVision</a></h1>
                </div><!-- .site-branding-text -->
            </div><!-- .site-branding -->  
        </header><!-- #masthead -->
        <div class="container" style="margin-top:3%;">
            <nav id="site-navigation" class="navbar navbar-expand-lg navbar-light">
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNavDropdown" style="font-weight:bold;"> 
                    <ul class="navbar-nav">
                        <li class="nav-item active">
                            <a href="index.php" class="nav-link active">首頁</a>
                        </li>
                        <li id="model_but" class="nav-item">
                            <a style="cursor:pointer;" onclick="doFormRequest('modellist.php','post')" class="nav-link">模型</a>
                        </li>                        
                        <li id="synopsis" style="cursor:pointer;" class="nav-item">
                            <a onclick="Introduction()" class="nav-link">操作說明</a>
                        </li>
                        <li id="store_but" style="cursor:pointer;" class="nav-item">
                            <a class="nav-link" onclick="doFormRequest('store.php','post')">模型分享頁面</a>
                        </li>
                    </ul>
                </div>
            </nav><!-- #site-navigation -->
            <h2 style="text-align:center;margin-top:3%;">上傳圖片進行辨識</h2>
        </div>
        
        <div class="row">
            <div class="col-sm-2"></div>
            <div class="col-sm-8" id="box">
                <img id="blah" width="56%" />
            </div>

            <div class="col-sm-2"></div>
        </div>

        <div class="container">
            <div class="row justify-content-center">
                <div class="col-sm-4"></div>
                <label class="upload_cover col-sm-4" style="cursor:pointer;">
                    <input id="upload_input" type="file" />
                    <div id='upload' style="text-align :center">
                        <img id='uploadImg' src="./icon/upload.png" style="margin:0 auto;">
                    </div>
                </label>
            <div class="col-sm-4"></div>
            </div>
        </div>
        <div style="text-align :center; margin-top:2.5vh">
            <a id='identifyButton' onclick="predict()">開始辨識</a>
        </div>

        <footer id="colophon">
            <div class="row">
                <div class="col-lg-1"></div>
                <div class="col-lg-4" style="margin-top: 6%;">
                    
                </div>
                <div class="col-lg-3"></div>
                <div class="col-lg-4" style="font-size:13px;margin: 1.5% 0 0.5% 0">
                    
                </div>
            </div>
        </footer>
    </div><!-- #page -->
    <script>
        var ree = "";
        $("#upload_input").change(function () {
            var Orientation = "";
            var file = this.files[0];
            var objUrl = getObjectURL(file);
            var reader = new FileReader();
            reader.onload = function (event) {
                var base64 = event.target.result;
                var hPosition = base64.search(",");
                ree = base64.substring(hPosition + 1, base64.length);
            }
            reader.readAsDataURL(file);
            if (objUrl) {
                EXIF.getData(file, function () {
                    Orientation = EXIF.getTag(this, 'Orientation');
                    rotate(Orientation, objUrl);
                });
            }
        });
        function rotate(Orientation, objUrl) {
            var up_img = $("#blah");
            var Rotation_angle = 0;
            up_img.attr("src", objUrl);
            up_img.rotate(360 - Rotation_angle);
            if (Orientation != "" && Orientation != 1) {
                console.log(Orientation);
                switch (Orientation) {
                    case 6:
                        Rotation_angle = 90;
                        up_img.rotate(Rotation_angle);
                        break;
                    case 8:
                        Rotation_angle = 270;
                        up_img.rotate(Rotation_angle);
                        break;
                }
            }
        }
        function predict() {
            $.post({
                url: ip + 'predict',
                data: {
                    key: encodeURIComponent(key),
                    model_id: encodeURIComponent(model_id),
                    base64_image: encodeURIComponent(ree)
                },
                type: "POST",
                // dataType: 'json',
                success: function (msg) {
                    msg = decodeURIComponent(msg);
                    msg = JSON.parse(msg);
                    console.log(msg.data);
                    alertify.alert("辨識結果 : " + msg.data, function () { });
                    // document.getElementById("site-title").innerHTML = "辨識結果:" + msg.data;
                },
                error: function (xhr, ajaxOptions, thrownError) {
                }
            });
        }
        function getObjectURL(file) {
            var url = null;
            if (window.createObjectURL != undefined) { // basic
                url = window.createObjectURL(file);
            } else if (window.URL != undefined) { // mozilla(firefox)
                url = window.URL.createObjectURL(file);
            } else if (window.webkitURL != undefined) { // webkit or chrome
                url = window.webkitURL.createObjectURL(file);
            }
            return url;
            console.log(url)
        }
    </script>

</body>

</html>