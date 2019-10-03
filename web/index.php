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


    <link href="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.core.css" rel="stylesheet">
    <link href="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.default.css" rel="stylesheet">
    <script src="//cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.min.js"></script>


    <?php 
    include('global_config.php')
    ?>

    <title>客製化影像辨識</title>
    <script type="text/javascript">
        /* <![CDATA[ */
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
            /* ]]> */
    </script>

    <style type="text/css">
        img.wp-smiley,
    </style>
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

        #masthead {
            background: linear-gradient(180deg, #BBBBBB 0%, #EEEEEE 100%);
            background: -moz-linear-gradient(180deg, #BBBBBB 0%, #EEEEEE 100%);
            background: -webkit-linear-gradient(270deg, #BBBBBB 0%, #ffffff 100%);
            background: -o-linear-gradient(180deg, #BBBBBB 0%, #EEEEEE 100%);
        }

        #colophon {
            background-color: #555555;
            color: #ffffff;
            margin-top: 5%;
            font-family: Microsoft JhengHei;
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

        #Signin,#signout{

            font-size : 1vw ;

        }
    </style>
</head>

<body>
    <script>
        var ip = "<?php echo $base_url; ?>";
        var Name = "";
        var Mail = "";
        var login_json = Object;
        var id_token = "";
        var key = "";
        var client_id = "488772557570-m50f06mgi4lqnkki7jevo3cjkgtqercc.apps.googleusercontent.com";
        var registered_bool = false;
        function onSignIn(googleUser) {
            // Useful data for your client-side scripts:            
            var profile = googleUser.getBasicProfile();
            Name = profile.getName();
            Mail = profile.getEmail();
            //login_json = { "mail": Mail, "name": Name };
            document.getElementById("Signin").innerHTML = Name + " 你好！";
            document.getElementById("signout").innerHTML = '<div onclick="signOut()">(登出)</div>';
            Mail = encodeURIComponent(Mail);
            client_id = encodeURIComponent(client_id);
            id_token = googleUser.getAuthResponse().id_token;
            id_token = encodeURIComponent(id_token);
            $.post({
                url: ip + 'get_key',
                data: {
                    idtoken: id_token,
                    clientid: client_id
                },
                type: "POST",
                // dataType: 'TEXT',
                // contentType: "application/json; charset=utf-8",
                success: function (msg) {
                    msg = JSON.parse(msg);
                    if (msg.message == "this email is un-registered, please visite cvision website to register.") {
                        document.getElementById("registered").style.display = "block";                        
                    }
                    else if (msg.message == "invalid id token, login fail.") {
                        alert("Google登入失敗")
                    }
                    else {
                        console.log(msg.data);
                        document.getElementById("model_but").style.display = "block";
                        document.getElementById("synopsis").style.display = "block";
                        key = msg.data.key;
                        registered_bool = true;
                        login_json = { "name": Name, "key": key, "id_token": id_token };
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                }
            });

        };
        function signOut() {
            var auth2 = gapi.auth2.getAuthInstance();
            auth2.signOut().then(function () {
                
                window.location.href='index.php';
                console.log('User signed out.');
            });
        }
        function doFormRequest(url, action) {
            if (registered_bool) {
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
            else {
                alert("請先進行註冊");
            }

        }
        function registered() {
            id_token = encodeURIComponent(id_token);
            $.post({
                url: ip + 'register',
                data: {
                    idtoken: id_token,
                },
                type: "POST",
                success: function (msg) {
                    msg = JSON.parse(msg);
                    console.log(msg);
                    if (msg.message == "login fail") {
                        alert("Google登入失敗");
                    }
                    else {
                        key = msg.data[0].key;
                        console.log(key);
                        login_json = { "name": Name, "key": key, "id_token": id_token };
                        registered_bool = true;
                        document.getElementById("model_but").style.display = "block";
                        document.getElementById("registered").style.display = "none";
                        document.getElementById("synopsis").style.display = "block";
                        for (var keys in login_json) {
                            if (login_json.hasOwnProperty(keys)) {
                                var val = login_json[keys];
                                console.log("key:" + keys);
                                console.log("val:" + val)
                            }
                        }
                    }

                },
                error: function (xhr, ajaxOptions, thrownError) {
                }
            });
            
            var infoString = "已成功完成註冊！！<br><br> 以下是使用教學 :<div align='left'> 1.進入模型頁面新增模型 <img src='/icon/modelbutton.png' width='12.5%'> <br> 2.點擊新增的模型名稱，進入分類頁面 <br> 3.可在分類頁面建立新的分類 <br> 4.點擊新建立的分類名稱，進入該分類的圖片管理頁面 <br> 5.所有分類的圖片建立完成後，可回分類頁面，進行模型訓練 <br> 6.訓練完成後，可在模型頁面查看訓練結果並可至分類頁面使用辨識功能</div> <br> 注意事項 : <br> <div align='left'> a.分類數量至少要兩個 <br> b. 每個分類的圖片數量至少要10張</div>";
            alertify.alert(infoString, function(){});
        }
        function Introduction(){
            alertify.alert("<?php echo $teach; ?>", function(){});
        }
    </script>
    <div id="page" class="site">
        <header id="masthead" class="">
            <br>
            <div align="right" style="margin-right:2%" id="signcontent">
                <div class="g-signin2" data-onsuccess="onSignIn" data-prompt="select_account" id="Signin"
                    style="display:inline-block;margin-top:0%;">
                </div>
                <div id='signout' style="display:inline-block; cursor:pointer;"></div>
            </div>
            <div class="header-wrapper">
                <div class="site-branding">
                    <a href="index.php" class="custom-logo-link" rel="home" itemprop="url"><img width="445" height="407"
                            src="./logo.png" class="custom-logo" alt="客製化影像辨識" itemprop="logo" /></a>
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
                                    href="">首頁</a></li>
                            <li id="model_but" style="display: none">
                                <a style="cursor:pointer;" onclick="doFormRequest('modellist.php','post')">模型</a></li>

                            <li id="registered" style="display: none; cursor:pointer;">
                                <a onclick="registered()">註冊</a>
                            </li>
                            <li id="synopsis" style="display: none; cursor:pointer;">
                                <a onclick="Introduction()">操作說明</a>
                            </li>
                        </ul>
                    </div>
                </nav><!-- #site-navigation -->
            </div><!-- .menu-wrapper -->
        </header><!-- #masthead -->

        <div class="container">
            <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
                <div class="carousel-inner">
                    <div class="carousel-item active">
                        <a href="https://zh.wikipedia.org/wiki/%E6%B7%B1%E5%BA%A6%E5%AD%A6%E4%B9%A0" title="深度學習"><img
                                src="https://cdn-images-1.medium.com/max/1600/1*5ZuLCsB1KXEPgHu-qJ8WxQ.png" alt="深度學習"
                                style="height: 400px;width: 1600px;" /></a>
                    </div>
                </div>
            </div>
        </div>

        <div class="container">
            <div class="row">
                <div class="col-sm-8">
                    <h1>深度學習</h1>
                    <div>深度學習（deep learning）是機器學習的分支，是一種試圖使用包含複雜結構或由多重非線性變換構成的多個處理層對資料進行高層抽象的演算法。</div>
                </div>
                <div class="col-sm-4">
                    <img src="./automl-lead.svg">
                </div>
            </div>
            <div class="row" style="margin-top:7%; ">
                <div class="col-sm-8">
                    <h1>
                        客製化服務
                    </h1>
                    <div>
                        即使您的機器學習專業知識有限，也能輕鬆訓練出高品質的自訂機器學習模型。
                    </div>
                </div>
                <div class="col-sm-4">
                    <img src="https://cdn2.ettoday.net/images/2785/d2785335.jpg" width="320">
                </div>
            </div>
        </div>

        <!--news or something-->
        <div class="container" style="margin-top:5%; ">
            <div class="row">
                <div class="col-sm-8">
                    <div class="revision05_focuslist_title">News</div>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item"><a
                                href="https://www.ithome.com.tw/news/127699">Uber貢獻其分散式訓練框架Horovod給LF深度學習基金會</a></li>
                        <li class="list-group-item"><a href="http://bangqu.com/q9i9G5.html">微軟亞洲研究院：NLP將迎來黃金十年</a></li>
                        <li class="list-group-item"><a
                                href="https://www.youtube.com/watch?v=r-aa-a8sbzA&feature=youtu.be&fbclid=IwAR1pcEuj_E1yjnK4ANf7E03uw6Tbf0T0neJGmwGxn1PElt8FfbtKXEF20b8">AI挑檸檬完結篇｜先不要管蜂蜜檸檬了，你聽過用人工智慧挑檸檬嗎？</a>
                        </li>
                        <li class="list-group-item"><a
                                href="http://bangqu.com/2b21I5.html">強化學習新模型Jumper，讓神經網絡學習在閱讀中何時做決定</a></li>
                        <li class="list-group-item"><a
                                href="https://mr6.life/2018/12/13/%E7%A0%94%E7%A9%B6%E8%AD%A6%E5%91%8A%EF%BC%9A%E6%84%88%E6%83%B3%E6%89%BE%E5%88%B0%E8%87%AA%E5%B7%B1%E7%9C%9F%E6%AD%A3%E7%9A%84%E8%88%88%E8%B6%A3%EF%BC%8C%E5%B0%B1%E6%84%88%E4%B8%80%E8%BC%A9%E5%AD%90/?fbclid=IwAR3LO9499IbDkM7ZIzIuitLGrwMJoj7x2V9PvgIvzUITKyJcOpjPpYyw22Y">研究警告：愈想找到自己真正的興趣，就愈一輩子都找不到!</a>
                        </li>
                    </ul>
                </div>
                <div class="col-sm-4">
                    <div class="revision05_focuslist_title">熱門焦點</div>
                    <div>
                        <a href="https://tbrain.trendmicro.com.tw/" title="AI實戰吧!" target="_self"><img
                                src="./TBRAIN.png"></a>
                    </div>
                </div>
            </div>
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
    <div style="display:none">
    </div>
    <div id="report-form-window" style="display:none;"></div>

</body>

</html>