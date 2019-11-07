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
    <title>CVision</title>

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
            width: 80%;
            height: 80%;
            margin: 0px auto;
        }

        .box:hover {
            transition-property: width, height;
            transition-duration: 0.3s;
            width: 100%;
        }

        #label_name:hover {
            font-size: 2.5vw;
            text-decoration:underline;
        }

        #label_name,
        .deleteImg {
            cursor:pointer;
            color:blue;
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
        .alertify{
            top : 0;
            font-size:2.6vh;
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

        .font-family{
            text-align :center;
            font-family:DFKai-sb;    
            text-align:center;    
            cursor:pointer;      
        }
        .font-family:hover{
            transition-duration: 0.2s;
            font-weight:bold;
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
                            var deleteImg = "<div class='box'><img  style='float:left;' src='./icon/deleteImg.png' class='deleteImg img-fluid' onclick=\"delete_label(" + label_temp + "," + mid + ")\" alt='Responsive image'></div>";
                            var divName = "<div id='label_name' onclick=\"doFormRequest_toLabelImg('Labelimage.php','post'," + label_temp + "," + mid + ")\">" + label_json.data[i].name + "</div>";                            
                            if(label_json.data.length == 1){
                                labelHTML += "<td width='4.6%' style='cursor:pointer;'>" + deleteImg + "</td>" + "<td width='45.4%'>" + divName + "</td>";
                            }
                            else{
                                labelHTML += "<td width='7%' style='cursor:pointer;'>" + deleteImg + "</td>" + "<td width='43%'>" + divName + "</td>";                            
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
            alertify.prompt("請輸入類別名稱：", function (e, str) {
                if (e) {
                    labelname = str.trim();
                    if (labelname === '') {
                        alertify.log("類別名稱不得為空白!");
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
                                    alertify.log("發生錯誤 : 類別名稱重複");
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
                            <a href="index.php" class="nav-link active">首頁</a>
                        </li>
                        <li class="nav-item">
                            <a style="cursor:pointer;" onclick="doFormRequest('modellist.php','post')" class="nav-link">模型</a>
                        </li>
                        <li class="nav-item">
                            <a style="cursor:pointer;" onclick="create_label()" class="nav-link">新增類別</a>
                        </li>
                        <li style="cursor:pointer;">
                            <a onclick="Introduction()" class="nav-link">操作說明</a>
                        </li>
                        <li class="nav-item" id="share">
                            <a style="cursor:pointer;" onclick="share_model()" class="nav-link" ></a>
                        </li>
                        <li class="nav-item">
                            <a style="cursor:pointer;" onclick="doFormRequest('store.php','post')" class="nav-link">模型分享頁面</a>
                        </li>                        
                    </ul>
                </div>
            </nav><!-- #site-navigation -->
            <h2 id='site-title' class="site-title" style="text-align:center;margin-top:3%"></h2>
            <table id="label_table" border="2" style="font-size:3vw;margin-top:5%;" class="table table-striped">
                
            </table>
        </div>
        <div class="container" style="margin-top:3%">
            <div class="row">
                <div class="col-sm-5"></div>
                <label class="col-sm-2" style="font-size:140%;text-align :center;">                    
                    <a onclick="Train()" class="font-family">開始訓練</a>
                    <a id='identifyButton' style="margin-top:1%" onclick="Identify('predict.php','post')" class="font-family">開始辨識</a>
                </label>
                <div class="col-sm-5"></div>
            </div>
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

    <script>
        var model_name = "<?php echo $model_name;?>";
        var model_status = "<?php echo $model_status; ?>";      
        var share_but = document.getElementById("share");        
        document.getElementById('site-title').innerHTML = '' + model_name;        
        function share_model(share) {
            $.post({
                url: ip + 'share_model',
                data: {
                    key: encodeURIComponent(key),
                    model_id: encodeURIComponent(model_id),
                    share: encodeURIComponent(share)
                },
                type: "POST",
                success: function (msg) {
                    msg = decodeURIComponent(msg);
                    msg = JSON.parse(msg);
                    if (msg.message == "ok") {
                        if(share==1)
                        {
                            alertify.log("成功分享");
                            share_but.innerHTML='<a style="cursor: pointer; display: block;" onclick="share_model(0)" class="nav-link" id="share">取消分享模型</a>';
                        }
                        else
                        {
                            alertify.log("成功取消分享");
                            share_but.innerHTML='<a style="cursor: pointer; display: block;" onclick="share_model(1)" class="nav-link" id="share">公開分享模型</a>';
                        }
                        // get_model_info();
                    } 
                    else {
                        alertify.log("有錯誤");
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                }
            });
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
        function get_model_info(){       
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
                    share_status = msg.data[0].share;
                    if(share_status == 0){
                        share_but.innerHTML='<a style="cursor: pointer; display: block;" onclick="share_model(1)" class="nav-link" id="share">公開分享模型</a>';
                    }
                    else{
                        share_but.innerHTML='<a style="cursor: pointer; display: block;" onclick="share_model(0)" class="nav-link" id="share">取消分享模型</a>';
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
        get_model_info();
        if (model_status != "0") {
            document.getElementById("identifyButton").style.display = "block";
            share_but.style.display = "block";            
        }
        else {
            document.getElementById("identifyButton").style.display = "none";
            share_but.style.display = "block";
            // share_but.style.width = "0";
        }
    </script>


</body>

</html>