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

    <title>CVision</title>
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
            font-size: 2.7vw;
            width: 25%;
            text-align:center;
        }
        .TableTitle {
            text-align:center;
            font-size: 2.3vw;
            width: 33%;
            font-weight: bold;
        }
        td{
            font-size: 2vw;
            display: table-cell;
            height:70px;
            vertical-align:middle;
        }
        .box {
            width: 15%;
            height: 15%;
            margin: 0px auto;
        }
        .box:hover {
            transition-property: width, height;
            transition-duration: 0.3s;
            width: 20%;
        }
        #model_name {
            margin-left: 10px;            
            color:blue;
            font-size: 2.0vw;
            cursor:pointer;
            position: relative;
            top: 50%;
            transform: translateY(-50%);
            margin-left: auto;
            margin-right: auto;
        }
        #model_name:hover {
            transition-property: font-size;
            transition-duration: 0.3s;
            text-decoration:underline;
            font-size: 2.3vw;
        }
        #model_name,
        #deleteImg {
            display: inline-block;
            text-align: center;
            cursor:pointer;
            /* display: block;
            margin-left: auto;
            margin-right: auto; */
        }
        .alertify{
            top : 0;
            font-size:2.6vh;
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
            modelHTML = '<thead>'
                +'<tr>'
                + '<th class="TableTitle" scope="col">模型名稱</th>'
                + '<th class="TableTitle" scope="col">準確率</th>'
                // + '<td class="TableTitle">loss</td>'
                + '<th class="TableTitle" scope="col">刪除模型</th>'
                + '</tr>'
            +'</thead>';            
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
                    console.log(model_json);
                    var temp = null;
                    var mid = null;
                    var model_temp = null;
                    var length = model_json.data.length;
                    for (var i = 0; i < model_json.data.length; i++) {
                        temp = i;
                        mid = "'" + model_json.data[i].id + "'";
                        model_temp = "'" + model_json.data[i].name + "'";
                        var deleteImg = "<div class='box'><img src='./icon/deleteImg.png' id='deleteImg' onclick=\"delete_model(" + model_temp + "," + mid + ")\" class='img-fluid' alt='Responsive image'></div>";
                        var divName = "<div id='model_name' onclick=\"doFormRequest_toLabel('label.php','post'," + temp + "," + mid + ")\">" + model_json.data[i].name + "</div>";
                        // var divName = "<div id='model_name' onclick=\"doFormRequest_toLabel('label.php','post'," + temp + "," + mid + ")\">" + model_json.data[i].name + "</div>";
                        modelHTML += "<tr id='" + model_json.data[i].id + "'>";
                        if (model_json.data[i].loss == null) {
                            modelHTML += "<td>"  + divName + "<div style='color:#ff3333;float:right; font-size:1.5vw;position:relative;top:50%;transform:translateY(-50%);' valign='scenter' >未訓練</div>" + "</td>" + "<div id='status_" + model_json.data[i].id + "' style='display:none;'>0</div>";                            
                        }
                        else {
                            modelHTML += "<td>"  + divName + "<div style='color:#00D600;float:right;font-size:1.5vw;position:relative;top:50%;transform:translateY(-50%);'>訓練完成</div>" + "</td>" + "<div id='status_" + model_json.data[i].id + "' style='display:none;'>1</div>";
                        }                        
                        modelHTML += "<td >" + model_json.data[i].acc + "</td>";                        
                        // modelHTML += "<td>" + model_json.data[i].loss + "</td>";
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
                                var deleteImg = "<div class='box'><img src='./icon/deleteImg.png' id='deleteImg' onclick=\"delete_model(" + model_temp + "," + mid + ")\" class='img-fluid' alt='Responsive image'></div>";
                                // var divName = "<div id='model_name' onclick=\"doFormRequest_toLabel('label.php','post'," + temp + "," + mid + ")\">" + model_json.data[i].name + "</div>";
                                var divName = "<div id='model_name' onclick=\"doFormRequest_toLabel('label.php','post'," + temp + "," + mid + ")\">" + model_json.data[i].name + "</div>";
                                modelHTML += "<tr id='" + model_json.data[i].id + "'>";
                                modelHTML += "<td>" + divName + "<div style='color:#ffcc00;float:right; font-size:1.5vw;position: relative;top: 50%;transform: translateY(-50%);' valign='scenter' >訓練中</div>" + "</td>" + "<div id='status_" + model_json.data[i].id + "'style='display:none;'>0</div>";                                                               
                                modelHTML += "<td>" + "null" + "</td>";
                                // modelHTML += "<td>" + "null" + "</td>";
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
                    // $("#" + model_id + " td:eq(2)").text(msg.data[0].loss);
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

            <div style="height:auto;" class="text-center">
                <a href="index.php"  rel="home" itemprop="url">
                    <img width="26%" height="auto" src="./logo1.png" class="rounded" alt="客製化影像辨識" itemprop="logo" /></a>
                <div class="site-branding-text">
                    <h1 class="site-title"><a href="index.php" rel="home">CVision</a></h1>
                </div><!-- .site-branding-text -->
            </div><!-- .site-branding -->   
        </header><!-- #masthead -->


        <div class="container" style="text-align:center; margin-top:3%;" id="Main_content">
            <nav  class="navbar navbar-expand-lg navbar-light">
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNavDropdown" style="font-weight:bold;">
                    <ul class="navbar-nav">
                        <li class="nav-item active">
                            <a href="index.php" class="nav-link active">首頁</a>
                        </li>
                        <li class="nav-item">
                            <a style="cursor:pointer;" onclick="create_model()" class="nav-link">增加模型</a>
                        </li>
                        <li style="cursor:pointer;" class="nav-item">
                            <a onclick="Introduction()" class="nav-link">操作說明</a>
                        </li>
                        <li id="store_but" style="cursor:pointer;" class="nav-item">
                            <a class="nav-link" onclick="doFormRequest('store.php','post')">模型分享頁面</a>
                        </li>
                    </ul>
                </div>
            </nav><!-- #site-navigation -->
            <h1 class="site-title" style="text-align:center;margin-top:3%">模型</h1>
            <table border="1" id="Modeltable" style="margin-top:5%" class="table table-striped">
                
            </table>
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
</body>







</html>
