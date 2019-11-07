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
    <script src="//cdn.jsdelivr.net/npm/alertifyjs@1.12.0/build/alertify.min.js"></script>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.12.0/build/css/alertify.min.css"/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.12.0/build/css/themes/default.min.css"/>    
    <title>CVision</title>

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
            getlabel_info();
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
        var share_but = document.getElementById("share");
        function getlabel_info(){                        
            $.post({
                url: ip + 'get_label_info',
                data: {
                    label_id: encodeURIComponent(label_id),
                },
                type: "POST",
                success: function (msg) {
                    msg = decodeURIComponent(msg);
                    msg = JSON.parse(msg).data[0].share; 
                    console.log(msg);
                    if (msg == "0") {
                        $("#share").html('<a style="cursor: pointer; display: block;" onclick="share_label(1)" class="nav-link" id="share">公開分享類別</a>');                                                
                    } 
                    else {                        
                        $("#share").html('<a style="cursor: pointer; display: block;" onclick="share_label(0)" class="nav-link" id="share">取消分享類別</a>');                        
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                }
            });
        }
        function share_label(share){
            $.post({
                url: ip + 'share_label',
                data: {
                    key: encodeURIComponent(key),
                    label_id: encodeURIComponent(label_id),
                    share: encodeURIComponent(share)
                },
                type: "POST",
                success: function (msg) {
                    msg = decodeURIComponent(msg);
                    msg = JSON.parse(msg).message; 
                    console.log(msg);
                    if (msg == "ok") {
                        if(share==1){
                            alertify.notify('成功分享', 'custom', 2 );                            
                            $("#share").html('<a style="cursor: pointer; display: block;" onclick="share_label(0)" class="nav-link" id="share">取消分享類別</a>');                                                                        
                        }
                        else{
                            alertify.notify('成功取消分享', 'custom', 2);                            
                            $("#share").html('<a style="cursor: pointer; display: block;" onclick="share_label(1)" class="nav-link" id="share">公開分享類別</a>');                        
                        }
                    }                     
                },
                error: function (xhr, ajaxOptions, thrownError) {
                }
            });
        }
        function Introduction() {
            alertify.alert("<?php echo $teach; ?>", function () { });
        }
    </script>
    <style>
    .alertify{
            top : 0;
            font-size:2.6vh;
        }
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
            top: 50%;
            left: 50%;
            display: block;
            min-width: 100%;
            min-height: 100%;
            transform:translate(-50%,-50%);
        }

        .upload_cover {
            position: relative;
            width: 50px;
            height: 50px;
            text-align: center;
            cursor: pointer;
            transition: opacity 0.5s;
        }
        
        .ajs-message.ajs-custom { color: #ffffff;  background-color: #252525;  border-color: #252525; }
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
            width: 100%;
            height: 100%;
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
        .menu-wrapper{
            margin-top:3%;
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

            <div style="height:auto;" class="text-center">
                <a href="index.php"  rel="home" itemprop="url">
                    <img width="26%" height="auto" src="./logo1.png" class="rounded" alt="客製化影像辨識" itemprop="logo" />
                </a>
                <div class="site-branding-text">
                <h1 class="site-title"><a href="index.php" rel="home">CVision</a></h1>
                </div><!-- .site-branding-text -->
            </div><!-- .site-branding -->        
        </header><!-- #masthead -->

        
        <div class="container" style="margin-top:3%; text-align:center;">            
            <nav class="navbar navbar-expand-lg navbar-light">
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNavDropdown" style="font-weight:bold;">
                    <ul class="navbar-nav">
                        <li class="nav-item active">
                            <a href="index.php" class="nav-link active">首頁</a></li>
                        <li class="nav-item">
                            <a style="cursor:pointer;" onclick="doFormRequest('modellist.php','post')" class="nav-link">模型</a>
                        </li>
                        <li class="nav-item">
                            <a style="cursor:pointer;" onclick="doFormRequest_toLabel('label.php','post')" class="nav-link">類別</a>
                        </li>                        
                        <li id="synopsis" style="cursor:pointer;" class="nav-item">
                            <a onclick="Introduction()" class="nav-link">操作說明</a>                            
                        </li>
                        <li class="nav-item" id="share">
                            <a style="cursor:pointer;" onclick="share_label()" class="nav-link" ></a>
                        </li>
                        <li class="nav-item">
                            <a style="cursor:pointer;" onclick="doFormRequest('store.php','post')" class="nav-link">模型分享頁面</a>
                        </li> 
                        <li class="nav-item">
                                <!-- 请选取一个图像文件:<input type="file" id="file" name="file[]" multiple="multiple"/>  -->
                            <label class="upload_cover">
                                <input id="upload_input" type="file" name="file[]" multiple="multiple" />
                                <img class="upload_icon" src='./icon/add.png'>
                                <i class="delAvatar fa fa-times-circle-o" title="刪除"></i>
                            </label>
                        </li>
                        <li class="nav-item">
                            <label class="upload_cover">
                                <img class="upload_icon" src='./icon/delete.png' onclick="deleteImg();">
                            </label>
                        </li>
                    </ul>
                </div>
            </nav><!-- #site-navigation -->
            <h2 id='site-title' class="site-title" style="text-align:center;"></h2>
            <div class="row">
                <div class="col-md-12" id="imgTable">
                </div>
            </div>
        </div>

        <script>
            var label_name = "<?php echo $label_name; ?>";
            var file_input = document.getElementById('upload_input');
            alertify.notify('圖片上傳時請勿離開此頁面', 'custom', 3 );  
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

        <div>
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
                <div class="col-lg-4" style="margin-top: 6%;">
                    
                </div>
                <div class="col-lg-3"></div>
                <div class="col-lg-4" style="font-size:13px;margin: 1.5% 0 0.5% 0">                    
                </div>
            </div>
        </footer>
    </div><!-- #page -->

    <!--  -->






</body>

</html>