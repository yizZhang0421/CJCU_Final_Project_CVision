
<html style="height: 100%;">
    <head>
        <title>Model_Store</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="google-signin-scope" content="profile email">
        <meta name="google-signin-client_id"
        content="488772557570-m50f06mgi4lqnkki7jevo3cjkgtqercc.apps.googleusercontent.com">
        <script src="https://apis.google.com/js/platform.js" async defer></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.2/css/all.css">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.core.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.default.css" rel="stylesheet">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/alertify.js/0.3.10/alertify.min.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
        <script src="./js/getmodel_label.js"></script>
        <style>
            .deDefaultBSmargin{
                margin-left: 0;margin-right: 0;
            }
            .sideMenu{
                height: 40px;
                padding: 0 24px;
                display: flex;
                flex-direction: row;
                flex-wrap: wrap;
                align-items: center;
            }
            .sideMenu:hover{
                background-color:#F2F2F2;
                cursor: pointer;
            }
            .item{
                margin: 4px 2px;
            }
            .itemIconFade:hover{
                opacity: 0.6;
                cursor: pointer;
            }
            .itemIconFade{
                background-color: #999999;
                opacity: 0;
                transition: opacity .25s ease-in-out;
            }
            .main {
                opacity: 1;
                display: block;
                width: 100%;
                height: auto;
                transition: .5s ease;
                backface-visibility: hidden;
            }
            .container:hover .main {
                opacity: 0.3;
                cursor:pointer;
            }
            .all .pic{
                float:left;margin-right:10px;width:100px;height:100px;
            }
            .all .text{
                float:left;width:500px;
            }
            #model_alert{
                max-width:45%;
                height:auto;
                overflow:hidden;
            }
            .label_alert{
                max-width:100%;
                height:auto;                
            }
            .model_img,.label_img{
                max-width:100%;
                max-height:100%;
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
            var page = "model";
            var model_label_map = null;
            window.onload = function () {
                ShowHello();
                model_info('');
                model_label_map = getMap(key);
            }            
            function model_Introduction(id,name,sample_list){
                var html="<p>"+name+"</p>"; 
                sample_list = decodeURIComponent(sample_list);
                sample_list = JSON.parse(sample_list);                                
                // var sample_list = get_model_samples(id);     
                html+='<div id="demo" class="carousel slide" data-ride="carousel">'
                +'<div class="carousel-inner" interval=false>'
                +'<div class="carousel-item active">'
                +'<img id="model_alert" src="data:image/png;base64, '+sample_list[0].base64+'">'
                +'<br>'
                +sample_list[0].name
                +'</div>'
                for(var i=1;i<sample_list.length;i++){
                    html+='<div class="carousel-item">'
                    +'<img id="model_alert" src="data:image/png;base64, '+sample_list[i].base64+'">'
                    +'<br>'
                    +sample_list[i].name
                    +'</div>'
                }
                html+='</div>'
                +'<a class="carousel-control-prev" href="#demo" data-slide="prev" onclck="">'
                +'<span style="background-image: url(\'./icon/left-arrow.png\')" class="carousel-control-prev-icon"></span>'
                +'</a>'
                +'<a class="carousel-control-next" href="#demo" data-slide="next">'
                +'<span style="background-image: url(\'./icon/right-arrow.png\')" class="carousel-control-next-icon"></span></a>'
                +'</div>';                                                           
                alertify.set({ labels: {
                    ok     : "匯入",
                    cancel : "關閉"
                }});       
                alertify.confirm(html,
                    function(e){
                        if(e){
                            $.post({
                                url: ip + 'model_import',
                                data: {
                                    key: encodeURIComponent(key), 
                                    model_id: encodeURIComponent(id)                                       
                                },
                                type: "POST",
                                success: function (msg) {                        
                                    msg = JSON.parse(msg);
                                    if(msg.message=="ok"){
                                        alertify.success("成功匯入");
                                    }
                                    else if(msg.status==901){
                                        alertify.error("不可匯入自己的模型")
                                    }                                                                       
                                },
                                error: function (xhr, ajaxOptions, thrownError) {
                                }
                            });                            
                        }
                    }                    
                );                                            
            }
            function get_model_samples(id){
                var sample_data = null;
                $.post({
                    url: ip + 'model_samples',
                    data: {                        
                        model_id: encodeURIComponent(id)                                       
                    },
                    async: false,
                    type: "POST",
                    success: function (msg) {                        
                        msg = JSON.parse(msg);
                        sample_data = msg.data;                        
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                    }
                });  
                return sample_data;
            }
            function label_Introduction(from_id,key,name,image_list){
                var iterator1 = model_label_map.keys();
                image_list = decodeURIComponent(image_list);
                image_list = JSON.parse(image_list);                                
                // var html = "<h2>"+name+"</h2><div class='col-lg-9'>"
                // for(var i =0;i<image_list.length;i++){
                //     html+="<img id='label_alert' src='data:image/png;base64, "+image_list[i].base64+"'>"
                // }
                // html+="</div>";
                var html = "<h2>"+name+"</h2><table align='center'><tr>"
                for(var i =0;i<image_list.length;i++){
                    html+="<td style='margin: 0 5%; width:33%;'><img class='label_alert' src='data:image/png;base64, "+image_list[i].base64+"'></td>";
                }
                html+="</tr></table>";
                html+="<select id='myLabel' style='margin-top:3%'>"
                for(var i=0;i<model_label_map.size;i++){
                    var map_key = iterator1.next().value;
                    var label_arr = model_label_map.get(map_key);
                    for(var j=0;j<label_arr.length;j++){
                        html+="<option value='"+label_arr[j].id+"'>"+map_key.name+"/"+label_arr[j].name+"</option>"
                    }
                }
                html+="</select>";                
                alertify.set({ labels: {
                    ok     : "匯入",
                    cancel : "關閉"
                }});       
                alertify.confirm(html,
                    function(e){
                        if(e){
                            var to_Label =  document.getElementById("myLabel").value;                            
                            $.post({
                                url: ip + 'label_import',
                                data: {
                                    key: encodeURIComponent(key), 
                                    from_label: encodeURIComponent(from_id),                                       
                                    to_label: encodeURIComponent(to_Label)                                       
                                },
                                type: "POST",
                                success: function (msg) {                        
                                    msg = JSON.parse(msg);
                                    if(msg.message=="ok"){
                                        alertify.success("成功匯入");
                                    }              
                                    else if(msg.status==902){
                                        alertify.error("來源類別與目的類別不可相同");
                                    }                                                               
                                },
                                error: function (xhr, ajaxOptions, thrownError) {
                                }
                            });                            
                        }                        
                    }                    
                );                                            
            }
            function getlabel_samples(id){
                var label_sample = null;
                $.post({
                    url: ip + 'label_samples',
                    data: {
                        label_id: encodeURIComponent(id)                                        
                    },
                    async: false,
                    type: "POST",
                    success: function (msg) {
                        msg = JSON.parse(msg);                        
                        label_sample = msg.data;
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                    }
                });
                return label_sample;
            }
            function model_info(keyword){          
                $("#search").html('<button class="btn btn-primary fa fa-search" id="search_but" type="button" onclick="search(\'model\')"></button>');                      
                $.post({
                    url: ip + 'model_store',
                    data: {
                        keyword: encodeURIComponent(keyword),                                        
                    },                    
                    type: "POST",
                    success: function (msg) {
                        var html = '';                            
                        var data = JSON.parse(msg).data;  
                        for(var i = 0;i<data.length;i++){
                            var id = data[i].id;
                            var name = data[i].name;
                            var email = data[i].email;
                            var sample_list = get_model_samples(data[i].id); 
                            var sample_json = JSON.stringify(sample_list);
                            sample_json = encodeURIComponent(sample_json);                                                                                                               
                            html+='<div class="item" style="height: 244px;width: 160px;display: inline-block;background-color: #FFFFFF">';
                            html+='<div style="position: relative;height: 160px;width: 160px;">';
                            html+='<div style="position: absolute;height: 100%;width: 100%;padding: 15px;">';
                            html+='<div style="height: 100%;width: 100%;text-align: center;"><img class="model_img" src="data:image/png;base64, '+sample_list[0].base64+'"></div>';
                            html+='</div>';                                                        
                            html+='<div onclick=\'model_Introduction(\"'+id+'\",\"'+name+'\",\"'+sample_json+'\")\' class="itemIconFade" style="position: absolute;height: 100%;width: 100%;"></div>';                            
                            html+='</div>';
                            html+='<div style="height: 84px;width: 160px;padding: 10px;">';
                            html+='<div class="" style="white-space:nowrap;overflow:hidden;">';
                            html+='<a onclick=\'model_Introduction(\"'+id+'\",\"'+name+'\",\''+sample_json+'\')\' href="#" style="font-size: 16px;color: #333333;font-family: Roboto,Noto,sans-serif;">'+name+'</a>';
                            html+='</div>';
                            html+='<div style="font-size: 13px;color: #333333;font-family: Roboto,Noto,sans-serif;">'+email+'</div>';
                            html+='</div>';
                            html+='</div>';
                        }
                    document.getElementById("itemContainer").innerHTML = html;                        
                    // $("#itemContainer").html(html);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                    }
                });
            }
            function label_info(keyword){
                $("#search").html('<button class="btn btn-primary fa fa-search" id="search_but" type="button" onclick="search(\'label\')"></button>');
                $.post({
                    url: ip + 'label_store',
                    data: {
                        keyword: encodeURIComponent(keyword),                                        
                    },
                    type: "POST",
                    success: function (msg){
                        var html = "";   
                        var data = JSON.parse(msg).data;      
                        for(var i =0;i<data.length;i++){
                            var id = data[i].id;
                            var sample_list = getlabel_samples(id);
                            var sample_json = JSON.stringify(sample_list);
                            sample_json = encodeURIComponent(sample_json);
                            html+='<div class="item" style="height: 244px;width: 160px;display: inline-block;background-color: #FFFFFF">';
                            html+='<div style="position: relative;height: 160px;width: 160px;">';
                            html+='<div style="position: absolute;height: 100%;width: 100%;padding: 15px;">';
                            html+='<div style="height: 100%;width: 100%;text-align: center;"><img class="label_img" src="data:image/png;base64, '+sample_list[0].base64+'"></div>';
                            html+='</div>';
                            html+='<div onclick="label_Introduction(\''+id+'\',\''+key+'\',\''+data[i].name+'\',\''+sample_json+'\')" class="itemIconFade" style="position: absolute;height: 100%;width: 100%;"></div>';
                            html+='</div>';
                            html+='<div style="height: 84px;width: 160px;padding: 10px;">';
                            html+='<div class="" style="white-space:nowrap;overflow:hidden;">';
                            html+='<a onclick="label_Introduction(\''+id+'\',\''+key+'\',\''+data[i].name+'\',\''+sample_json+'\')" href="#" style="font-size: 16px;color: #333333;font-family: Roboto,Noto,sans-serif;">'+data[i].name+'</a>';
                            html+='</div>';
                            html+='<div style="font-size: 13px;color: #333333;font-family: Roboto,Noto,sans-serif;">'+data[i].email+'</div>';
                            html+='<div style="font-size: 13px;color: #333333;font-family: Roboto,Noto,sans-serif;">'+data[i].description+'</div>'
                            html+='</div>';
                            html+='</div>';
                        }
                    $("#itemContainer").html(html);                                                      
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                    }
                });
            }
            function search(type){
                var value = document.getElementById("enter").value;
                if(type=='model'){
                    model_info(value);
                }
                else{
                    label_info(value);
                }
            }
            function ShowHello() {
                document.getElementById('Signin').innerHTML = name + ' 你好！';
                document.getElementById("signout").innerHTML = '<div onclick="signOut()">(登出)</div>';
            }
            function signOut() {
                var auth2 = gapi.auth2.getAuthInstance();
                auth2.signOut().then(function () {
                    window.location.href = 'index.php';
                });
            }
        </script>
    </head>
    <body style="background-color: #F9F9F9;display: flex;flex-flow: column;height: 100%;">
        <div class="row deDefaultBSmargin" style="padding: 10px 0;background-color: #FFFFFF;">
            <div class="col-lg-2" style="margin-top: 10px;height: 60px;background-image: url('icon/store_logo.jpg');background-repeat: no-repeat; background-size: contain;background-position: center center;cursor:pointer;" onclick="location.href='index.php';"></div>
            <div class="col-lg-6" style="height: 60px;">
                <div class="input-group mb-3" style="position: relative;top: 50%;transform: translateY(-50%);">
                    <input type="text" class="form-control" placeholder="搜尋模型、資料集" id='enter'>
                    <div class="input-group-append" id="search">
                        <button class="btn btn-primary fa fa-search" id="search_but" type="button" onclick="search('model')"></button>
                    </div>
                </div>
            </div>
            <div class="col-lg-4" style="height: 60px;text-align: center;position: relative;top: 50%;transform: translateY(-50%);">
                <div align="right" style="margin-right:2%" id="signcontent">
                    <div class="g-signin2" data-onsuccess="onSignIn" data-prompt="select_account" id="Signin"
                        style="display:inline-block;margin-top:0%;">
                    </div>
                    <div id='signout' style="display:inline-block; cursor:pointer;"></div>
                </div>                    
            </div>
        </div>
        <div id="content" class="row deDefaultBSmargin" style="flex: 1 0 auto;">
            <div id="sideMenu" class="col-lg-2 deDefaultBSmargin" style="background-color: #FFFFFF;padding: 0;">
                <div class="sideMenu" onclick="model_info('')">
                    <div style="margin-right: 24px;height: 24px;width: 24px;background-image: url('icon/model.png');background-repeat: no-repeat; background-size: contain;background-position: center center"></div>
                    <div style="font-size: 14px;margin-right: 24px;color: #0D0D0D;font-family: Roboto,Noto,sans-serif;">模型</div>
                </div>
                <div class="sideMenu" onclick="label_info('')">
                    <div style="margin-right: 24px;height: 24px;width: 24px;background-image: url('icon/dataset.png');background-repeat: no-repeat; background-size: contain;background-position: center center"></div>
                    <div style="font-size: 14px;margin-right: 24px;color: #0D0D0D;font-family: Roboto,Noto,sans-serif;">資料集</div>
                </div>
            </div>
            <div id='itemContainer' class='col-lg-10' style='background-color: #F9F9F9;padding: 40px 20px 10px 20px;overflow-y: scroll;'>
                <script>
                    $("#itemContainer").css({'height': ($("#content").height() + 'px')}); //fixed height
                </script>
            </div>
        </div>
    </body>
</html>
