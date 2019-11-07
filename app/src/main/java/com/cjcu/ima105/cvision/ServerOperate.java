package com.cjcu.ima105.cvision;

import android.util.Log;

import com.google.android.gms.common.util.IOUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ServerOperate {
    public static final String SERVER_PROTOCOL="http";
    public static final String SERVER_ADDRESS="192.168.43.178";
    public static final int SERVER_PORT=8080;
    private static String full_server_prefix=SERVER_PROTOCOL+"://"+SERVER_ADDRESS+":"+SERVER_PORT+"/";

    public static MessageServerResponse get_key(String idtoken, String clientid){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("idtoken",idtoken);
        hashMap.put("clientid",clientid);
        String response = do_server_operate("get_key", hashMap);
        int status_code = -1;
        String message = null;
        String key="";
        try {
            JSONObject jsonObject = new JSONObject(response);
            status_code = Integer.parseInt(jsonObject.get("status").toString());
            message = jsonObject.get("message").toString();
            key = ((JSONObject) jsonObject.get("data")).get("key").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MessageServerResponse messageServerResponse = new MessageServerResponse(status_code, message);
        messageServerResponse.set_other_information(key);
        return messageServerResponse;
    }

    public static MessageServerResponse create_model(String key, String model_name){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key", key);
        hashMap.put("model_name",model_name);
        String response = do_server_operate("create_model",hashMap);
        int status_code = -2;
        String message = null;
        try {
            message = new JSONObject(response).get("message").toString();
            status_code = Integer.parseInt(new JSONObject(response).get("status").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MessageServerResponse(status_code, message);
    }
    public static ArrayList<HashMap<String, String>> model_list(String key){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        String response = do_server_operate("model_list",hashMap);
        ArrayList<HashMap<String, String>> result = null;
        try {
            result = multi_result((JSONArray) new JSONObject(response).get("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static MessageServerResponse delete_model(String key, String model_id){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("model_id",model_id);
        String response = do_server_operate("delete_model",hashMap);
        int status_code = -2;
        String message = null;
        try {
            message = new JSONObject(response).get("message").toString();
            status_code = Integer.parseInt(new JSONObject(response).get("status").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MessageServerResponse(status_code, message);
    }
    public static MessageServerResponse create_label(String key, String model_id, String label_name){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("model_id",model_id);
        hashMap.put("label_name",label_name);
        String response = do_server_operate("create_label",hashMap);
        int status_code = -2;
        String message = null;
        try {
            message = new JSONObject(response).get("message").toString();
            status_code = Integer.parseInt(new JSONObject(response).get("status").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MessageServerResponse(status_code, message);
    }
    public static ArrayList<HashMap<String, String>> label_list(String key, String model_id){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("model_id",model_id);
        String response = do_server_operate("label_list",hashMap);
        ArrayList<HashMap<String, String>> result = null;
        try {
            result = multi_result((JSONArray) new JSONObject(response).get("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static MessageServerResponse delete_label(String key, String label_id){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("label_id",label_id);
        String response = do_server_operate("delete_label",hashMap);
        int status_code = -2;
        String message = null;
        try {
            message = new JSONObject(response).get("message").toString();
            status_code = Integer.parseInt(new JSONObject(response).get("status").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MessageServerResponse(status_code, message);
    }
    public static MessageServerResponse write_image(String key, String label_id, String base64_image){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("label_id",label_id);
        hashMap.put("base64_image",base64_image);
        String response = do_server_operate("write_image",hashMap);
        int status_code = -2;
        String message = null;
        try {
            message = new JSONObject(response).get("message").toString();
            status_code = Integer.parseInt(new JSONObject(response).get("status").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MessageServerResponse(status_code, message);
    }
    public static ArrayList<String> label_images(String key, String label_id){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("label_id",label_id);
        String response = do_server_operate("label_images",hashMap);
        ArrayList<String> result = new ArrayList<>();
        try {
            JSONArray jsonArray = (JSONArray) new JSONObject(response).get("data");
            for(int i=0;i<jsonArray.length();i++){
                result.add(jsonArray.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static MessageServerResponse delete_image(String key, String label_id, String image_id){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("label_id",label_id);
        hashMap.put("image_id",image_id);
        String response = do_server_operate("delete_image",hashMap);
        int status_code = -2;
        String message = null;
        try {
            message = new JSONObject(response).get("message").toString();
            status_code = Integer.parseInt(new JSONObject(response).get("status").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MessageServerResponse(status_code, message);
    }
    public static MessageServerResponse train(String key, String model_id){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("model_id",model_id);
        String response = do_server_operate("train",hashMap);
        int status_code = -2;
        String message = null;
        try {
            message = new JSONObject(response).get("message").toString();
            status_code = Integer.parseInt(new JSONObject(response).get("status").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MessageServerResponse(status_code, message);
    }
    public static ArrayList<HashMap<String, String>> progress_list(String key){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        String response = do_server_operate("progress_list",hashMap);
        ArrayList<HashMap<String, String>> result = null;
        try {
            result = multi_result((JSONArray) new JSONObject(response).get("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static MessageServerResponse terminate_train(String key, String model_id){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("model_id",model_id);
        String response = do_server_operate("terminate_train",hashMap);
        int status_code = -2;
        String message = null;
        try {
            message = new JSONObject(response).get("message").toString();
            status_code = Integer.parseInt(new JSONObject(response).get("status").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MessageServerResponse(status_code, message);
    }
    public static ArrayList<HashMap<String, String>> predictable_model_list(String key){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        String response = do_server_operate("predictable_model_list",hashMap);
        ArrayList<HashMap<String, String>> result = null;
        try {
            result = multi_result((JSONArray) new JSONObject(response).get("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static MessageServerResponse predict(String key, String model_id, String base64_image){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("model_id",model_id);
        hashMap.put("base64_image",base64_image);
        String response = do_server_operate("predict",hashMap);
        int status_code = -1;
        String message = null;
        String result="";
        try {
            JSONObject jsonObject = new JSONObject(response);
            status_code = Integer.parseInt(jsonObject.get("status").toString());
            message = jsonObject.get("message").toString();
            result = jsonObject.get("data").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MessageServerResponse messageServerResponse = new MessageServerResponse(status_code, message);
        messageServerResponse.set_other_information(result);
        return messageServerResponse;
    }
    public static HashMap<String, String> get_model_info(String model_id){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("model_id",model_id);
        String response = do_server_operate("get_model_info",hashMap);
        ArrayList<HashMap<String, String>> result = null;
        try {
            result = multi_result((JSONArray) new JSONObject(response).get("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.get(0);
    }
    public static HashMap<String, String> get_label_info(String label_id){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("label_id",label_id);
        String response = do_server_operate("get_label_info",hashMap);
        ArrayList<HashMap<String, String>> result = null;
        try {
            result = multi_result((JSONArray) new JSONObject(response).get("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.get(0);
    }
    public static byte[] get_image(String key, String label_id, String image_id, String type){
        byte[] imageBytes = null;
        try{
            URL url = new URL(ServerOperate.SERVER_PROTOCOL+"://"+ServerOperate.SERVER_ADDRESS+":"+ServerOperate.SERVER_PORT+"/get_image?key="+CurrentPosition.key+"&label_id="+label_id+"&image_id="+image_id+"&type="+type);
            imageBytes = IOUtils.toByteArray(url.openStream());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return imageBytes;
    }

    /********************   TRADE SYSTEM  *********************/
    public static MessageServerResponse share_model(String key, String model_id, boolean share){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("model_id",model_id);
        if(share){
            hashMap.put("share","1");
        }
        else{
            hashMap.put("share","0");
        }
        String response = do_server_operate("share_model",hashMap);
        int status_code = -2;
        String message = null;
        try {
            message = new JSONObject(response).get("message").toString();
            status_code = Integer.parseInt(new JSONObject(response).get("status").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MessageServerResponse(status_code, message);
    }
    public static ArrayList<HashMap<String, String>> model_store(String keyword){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("keyword", keyword);
        String response = do_server_operate("model_store", hashMap);
        ArrayList<HashMap<String, String>> result = null;
        try {
            result = multi_result((JSONArray) new JSONObject(response).get("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static ArrayList<HashMap<String, String>> model_samples(String model_id, String bound){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("model_id",model_id);
        String response="";
        if(bound!=null){
            response = do_server_operate("model_samples?bound="+bound,hashMap);
        }
        else{
            response = do_server_operate("model_samples",hashMap);
        }
        ArrayList<HashMap<String, String>> result = null;
        try {
            result = multi_result((JSONArray) new JSONObject(response).get("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static MessageServerResponse model_import(String key, String model_id){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("model_id",model_id);
        String response = do_server_operate("model_import",hashMap);
        int status_code = -2;
        String message = null;
        try {
            message = new JSONObject(response).get("message").toString();
            status_code = Integer.parseInt(new JSONObject(response).get("status").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MessageServerResponse(status_code, message);
    }
    public static MessageServerResponse share_label(String key, String label_id, boolean share){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("label_id",label_id);
        if(share){
            hashMap.put("share","1");
        }
        else{
            hashMap.put("share","0");
        }
        String response = do_server_operate("share_label",hashMap);
        int status_code = -2;
        String message = null;
        try {
            message = new JSONObject(response).get("message").toString();
            status_code = Integer.parseInt(new JSONObject(response).get("status").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MessageServerResponse(status_code, message);
    }
    public static ArrayList<HashMap<String, String>> label_store(String keyword){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("keyword", keyword);
        String response = do_server_operate("label_store", hashMap);
        ArrayList<HashMap<String, String>> result = null;
        try {
            result = multi_result((JSONArray) new JSONObject(response).get("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static ArrayList<HashMap<String, String>> label_samples(String label_id, String bound){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("label_id",label_id);
        String response="";
        if(bound!=null){
            response = do_server_operate("label_samples?bound="+bound,hashMap);
        }
        else{
            response = do_server_operate("label_samples",hashMap);
        }
        ArrayList<HashMap<String, String>> result = null;
        try {
            result = multi_result((JSONArray) new JSONObject(response).get("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static MessageServerResponse label_import(String key, String from_label, String to_label){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key",key);
        hashMap.put("from_label",from_label);
        hashMap.put("to_label",to_label);
        String response = do_server_operate("label_import",hashMap);
        int status_code = -2;
        String message = null;
        try {
            message = new JSONObject(response).get("message").toString();
            status_code = Integer.parseInt(new JSONObject(response).get("status").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MessageServerResponse(status_code, message);
    }












    private static String do_server_operate(final String operate, final HashMap<String, String> params) {
        class myRunable implements Runnable{
            String response_json_string;
            @Override
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(full_server_prefix+operate).openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Charsert", "UTF-8");
                    DataOutputStream output = new DataOutputStream(httpURLConnection.getOutputStream());
                    String output_string="";
                    for (String key : params.keySet()) {
                        output_string += key + "=" + URLEncoder.encode(params.get(key), "utf-8") + "&";
                    }
                    if(output_string.length()!=0) {
                        output_string = output_string.substring(0, output_string.length() - 1);
                        output.writeBytes(output_string);
                        output.flush();
                        output.close();
                    }

                    String response = new String(IOUtils.readInputStreamFully(httpURLConnection.getInputStream()), "UTF-8");

                    Log.e("asd", operate+" response: "+response);
                    httpURLConnection.disconnect();
                    response_json_string=response;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        myRunable myRunable = new myRunable();
        Thread t = new Thread(myRunable);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return myRunable.response_json_string;
    }
    private static ArrayList<HashMap<String, String>> multi_result(JSONArray jsonArray){
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                HashMap<String, String> hashMap = new HashMap<>();
                Iterator<String> keys = jsonObject.keys();
                while(keys.hasNext()){
                    String key = keys.next();
                    hashMap.put(key, jsonObject.get(key).toString());
                }
                result.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
class MessageServerResponse{
    int status_code;
    String message;
    String other_information;
    MessageServerResponse(int status_code, String message){
        this.status_code=status_code;
        this.message=message;
    }
    public void set_other_information(String other_information){
        this.other_information=other_information;
    }
}
