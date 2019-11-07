var map = new Map();
var global_key = "";
function getMap(key){
    global_key = key;
    var model_data = null;
    $.post({
        url: ip + 'model_list',
        data: {
            key: encodeURIComponent(global_key)
        },
        async: false,
        type: "POST",
        // dataType: 'TEXT',
        // contentType: "application/json; charset=utf-8",
        success: function (msg) {                   
            msg = JSON.parse(msg);                        
            model_data = msg.data;
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log(xhr);
            console.log(ajaxOptions);
            console.log(thrownError);
        }
    });
    for(var i=0;i<model_data.length;i++){        
        var label_data = getlabel_list(model_data[i].id);
        map.set(model_data[i],label_data);
    }
    return map;
}

function getlabel_list(model_id){
    var label_data = null;
    $.post({
        url: ip + 'label_list',
        data: {
            key: encodeURIComponent(global_key),
            model_id: encodeURIComponent(model_id)
        },
        async: false,
        type: "POST",
        success: function (msg) {                   
            msg = JSON.parse(msg);                        
            label_data = msg.data;
        },
        error: function (xhr, ajaxOptions, thrownError) {
        }
    });
    return label_data;
}