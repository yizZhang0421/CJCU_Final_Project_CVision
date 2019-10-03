from flask import Flask, request, send_file, Response, stream_with_context
from flask_cors import CORS
app = Flask(__name__)
CORS(app, resources=r'/*')

import cv2, base64, json, io, urllib, os, re, copy, requests
import numpy as np

from train_server import train_start, progress_stream
from prediction_server import predict_start
import db_operate, code_table
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

from google.oauth2 import id_token
from google.auth.transport import requests as google_requests

def db_result_parse_to_response_string(db_operate_result, message='ok'):
    if isinstance(db_operate_result, list) or isinstance(db_operate_result, dict):
        ok=copy.deepcopy(code_table.ok)
        ok['data']=db_operate_result
        #return urllib.parse.quote_plus(json.dumps(ok, ensure_ascii=False))
        return json.dumps(ok, ensure_ascii=False)
    elif db_operate_result=='ok':
        ok=copy.deepcopy(code_table.ok)
        ok['message']=message
        return json.dumps(ok, ensure_ascii=False)
    else:
        return json.dumps(code_table.error_duplicate_name, ensure_ascii=False)
    
def is_key_exist(key):
    result=db_operate.is_key_exist(key)
    if len(result)==0:
        return False
    else:
        return True

@app.route('/register',methods=['POST'])
def register():
    idtoken=urllib.parse.unquote(request.form['idtoken'])
    try:
        idinfo=id_token.verify_oauth2_token(idtoken, google_requests.Request(), '488772557570-m50f06mgi4lqnkki7jevo3cjkgtqercc.apps.googleusercontent.com')
        result=db_operate.register(idinfo['email'], idinfo['name'])
        return db_result_parse_to_response_string(result)
    except:
        return json.dumps(code_table.error_login_fail, ensure_ascii=False)
    
@app.route('/get_key',methods=['POST'])
def get_key():
    idtoken=urllib.parse.unquote(request.form['idtoken'])
    clientid=urllib.parse.unquote(request.form['clientid'])
    try:
        idinfo=id_token.verify_oauth2_token(idtoken, google_requests.Request(), clientid)
        result=db_operate.get_key(idinfo['email'])
        if len(result)==0:
            return json.dumps(code_table.error_unregistered, ensure_ascii=False)
        else:
            return db_result_parse_to_response_string(result[0])
    except:
        return json.dumps(code_table.error_login_fail, ensure_ascii=False)

@app.route('/create_model',methods=['POST'])
def create_model():
    key=urllib.parse.unquote(request.form['key'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    model_name=urllib.parse.unquote(request.form['model_name'])
    return db_result_parse_to_response_string(db_operate.create_model(key, model_name))

@app.route('/model_list',methods=['POST'])
def model_list():
    key=urllib.parse.unquote(request.form['key'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    id_name_acc_loss=db_operate.model_list(key)
    return db_result_parse_to_response_string(id_name_acc_loss)

@app.route('/get_model_info',methods=['POST'])
def get_model_info():
    model_id=urllib.parse.unquote(request.form['model_id'])
    if len(db_operate.is_model_exist(model_id))==0:
        return json.dumps(code_table.error_object_not_exist, ensure_ascii=False)
    name_size_acc_loss_class_label_email=db_operate.get_model_info(model_id)
    return db_result_parse_to_response_string(name_size_acc_loss_class_label_email)

@app.route('/delete_model',methods=['POST'])
def delete_model():
    key=urllib.parse.unquote(request.form['key'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    model_id=urllib.parse.unquote(request.form['model_id'])
    if len(db_operate.is_model_exist(model_id))==0:
        return json.dumps(code_table.error_object_not_exist, ensure_ascii=False)
    if key!=db_operate.get_model_key(model_id):
        return json.dumps(code_table.error_invalid_key, ensure_ascii=False)
    result=db_operate.delete_model(model_id)
    return db_result_parse_to_response_string(result)

@app.route('/create_label',methods=['POST'])                                    #! would influence while tarining
def create_label():
    key=urllib.parse.unquote(request.form['key'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    model_id=urllib.parse.unquote(request.form['model_id'])
    if key!=db_operate.get_model_key(model_id):
        return json.dumps(code_table.error_invalid_key, ensure_ascii=False)
    current_model_status=db_operate.get_model_status(model_id)
    if current_model_status==code_table.model_status_first_train or current_model_status==code_table.model_status_improve:
        return json.dumps(code_table.error_model_is_training, ensure_ascii=False)
    label_name=urllib.parse.unquote(request.form['label_name'])
    result=db_operate.create_label(label_name, model_id)
    return db_result_parse_to_response_string(result)

@app.route('/label_list',methods=['POST'])
def label_list():
    key=urllib.parse.unquote(request.form['key'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    model_id=urllib.parse.unquote(request.form['model_id'])
    if key!=db_operate.get_model_key(model_id):
        return json.dumps(code_table.error_invalid_key, ensure_ascii=False)
    labelid_name=db_operate.label_list(model_id)
    return db_result_parse_to_response_string(labelid_name)

@app.route('/delete_label',methods=['POST'])                                    #! would influence while tarining
def delete_label():
    key=urllib.parse.unquote(request.form['key'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    label_id=urllib.parse.unquote(request.form['label_id'])
    if len(db_operate.is_label_exist(label_id))==0:
        return json.dumps(code_table.error_object_not_exist, ensure_ascii=False)
    if key!=db_operate.get_label_key(label_id):
        return json.dumps(code_table.error_invalid_key, ensure_ascii=False)
    current_model_status=db_operate.get_model_status(db_operate.get_label_model_id(label_id))
    if current_model_status==code_table.model_status_first_train or current_model_status==code_table.model_status_improve:
        return json.dumps(code_table.error_model_is_training, ensure_ascii=False)
    result=db_operate.delete_label(label_id)
    return db_result_parse_to_response_string(result)
    
@app.route('/write_image',methods=['POST'])                                    #! would influence while tarining
def write_image():
    key=urllib.parse.unquote(request.form['key'])
    label_id=urllib.parse.unquote(request.form['label_id'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    if len(db_operate.is_label_exist(label_id))==0:
        return json.dumps(code_table.error_object_not_exist, ensure_ascii=False)
    if key!=db_operate.get_label_key(label_id):
        return json.dumps(code_table.error_invalid_key, ensure_ascii=False)
    current_model_status=db_operate.get_model_status(db_operate.get_label_model_id(label_id))
    if current_model_status==code_table.model_status_first_train or current_model_status==code_table.model_status_improve:
        return json.dumps(code_table.error_model_is_training, ensure_ascii=False)
    image=urllib.parse.unquote(request.form['base64_image'])
    try:
        image=base64.b64decode(image)
    except:
        return json.dumps(code_table.error_invalid_base64, ensure_ascii=False)
    image = cv2.imdecode(np.frombuffer(image, np.uint8), cv2.IMREAD_COLOR)
    if isinstance(image, np.ndarray) is not True and image is None:
        return json.dumps(code_table.error_not_image_data, ensure_ascii=False)
    # should always write jpg
    image_id=db_operate.id_hash_string()
    cv2.imwrite('labels/'+label_id+'/'+image_id+'.jpg', image)
    return json.dumps(code_table.ok, ensure_ascii=False)

@app.route('/label_images',methods=['POST'])
def label_images():
    key=urllib.parse.unquote(request.form['key'])
    label_id=urllib.parse.unquote(request.form['label_id'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    if len(db_operate.is_label_exist(label_id))==0:
        return json.dumps(code_table.error_object_not_exist, ensure_ascii=False)
    if key!=db_operate.get_label_key(label_id):
        return json.dumps(code_table.error_invalid_key, ensure_ascii=False)
    result=[]
    for img in os.listdir('labels/'+label_id+'/'):
        result.append(re.split('[.]', img)[0])
    return_data=copy.deepcopy(code_table.ok)
    return_data['data']=result
    return json.dumps(return_data, ensure_ascii=False)

@app.route('/get_image',methods=['POST','GET'])
def get_image():#non format return
    key=urllib.parse.unquote(request.args.get('key'))
    label_id=urllib.parse.unquote(request.args.get('label_id'))
    image_id=urllib.parse.unquote(request.args.get('image_id'))
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    if key!=db_operate.get_label_key(label_id):
        return json.dumps(code_table.error_invalid_key, ensure_ascii=False)
    if request.args.get('type') is not None:
        type=urllib.parse.unquote(request.args.get('type'))
    img=cv2.imread('labels/'+label_id+'/'+image_id+'.jpg')
    if request.args.get('type') is not None and type!='full':
        width=300
        size=(width,int(img.shape[0]*(width/img.shape[1])))
        img=cv2.resize(img, size, interpolation=cv2.INTER_AREA)
    img=cv2.imencode('.jpg', img)[1].tostring()
    img=io.BytesIO(img)
    return send_file(img, mimetype='image/jpg')

@app.route('/delete_image',methods=['POST'])                                    #! would influence while tarining
def delete_image():
    key=urllib.parse.unquote(request.form['key'])
    label_id=urllib.parse.unquote(request.form['label_id'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    if key!=db_operate.get_label_key(label_id):
        return json.dumps(code_table.error_invalid_key, ensure_ascii=False)
    model_id=db_operate.get_label_model_id(label_id)
    current_model_status=db_operate.get_model_status(model_id)
    if current_model_status==code_table.model_status_first_train or current_model_status==code_table.model_status_improve:
        return json.dumps(code_table.error_model_is_training, ensure_ascii=False)
    image_id=urllib.parse.unquote(request.form['image_id'])
    try:
        os.remove('labels/'+label_id+'/'+image_id+'.jpg')
    except:
        return json.dumps(code_table.error_object_not_exist, ensure_ascii=False)
    return json.dumps(code_table.ok, ensure_ascii=False)
    
@app.route('/progress_list',methods=['POST'])
def progress_list():
    key=urllib.parse.unquote(request.form['key'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    id_name=db_operate.progress_list(key)
    return db_result_parse_to_response_string(id_name)
    
@app.route('/predictable_model_list',methods=['POST'])
def predictable_model_list():
    key=urllib.parse.unquote(request.form['key'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    id_name_email=db_operate.predictable_model_list(key)
    return db_result_parse_to_response_string(id_name_email)
    
    
    
    


from multiprocessing import Process, Manager, pool
import time
training_thread_dict={}
if __name__ == '__main__':
    progress_dict=Manager().dict()
    hyper_thread_dict=Manager().dict()
def clean_train_dict():
    while 1:
        for t in training_thread_dict:
            if training_thread_dict[t].is_alive()==False:
                del training_thread_dict[t]
        time.sleep(3600)
if __name__ == '__main__':
    Process(target=clean_train_dict).start()
    
@app.route('/train',methods=['POST'])                                    #! would influence while tarining
def train():
    key=urllib.parse.unquote(request.form['key'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    model_id=urllib.parse.unquote(request.form['model_id'])
    if key!=db_operate.get_model_key(model_id):
        return json.dumps(code_table.error_invalid_key, ensure_ascii=False)
    current_model_status=db_operate.get_model_status(model_id)
    if current_model_status==code_table.model_status_first_train or current_model_status==code_table.model_status_improve:
        return json.dumps(code_table.error_model_is_training, ensure_ascii=False)
    
    img_num_check=True
    labels=db_operate.label_list(model_id)
    if len(labels)<2:
        return json.dumps(code_table.error_label_count_less_than_two, ensure_ascii=False)
    for label in labels:
        if len(os.listdir('labels/'+label['id']))<10:
            img_num_check=False
            break
    if img_num_check==False:
        return json.dumps(code_table.error_image_less, ensure_ascii=False)
    
    db_operate.train_model(model_id)
    progress_dict[model_id]={'epoch':0, 'acc':None ,'loss':None}
    
    hyper_thread_dict[model_id]=1
    if __name__ == '__main__':
        training_thread_dict[model_id]=Process(target=train_start, args=(progress_dict, hyper_thread_dict, model_id, labels))
        training_thread_dict[model_id].start()
    return json.dumps(code_table.ok, ensure_ascii=False)

@app.route('/progress',methods=['POST'])                                    #! would fail if not training
def progress():#non format return
    key=urllib.parse.unquote(request.form['key'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    model_id=urllib.parse.unquote(request.form['model_id'])
    if key!=db_operate.get_model_key(model_id):
        return json.dumps(code_table.error_invalid_key, ensure_ascii=False)
    current_model_status=db_operate.get_model_status(model_id)
    if current_model_status is not code_table.model_status_first_train and current_model_status is not code_table.model_status_improve:
        return json.dumps(code_table.error_model_is_not_training, ensure_ascii=False)
    return Response(stream_with_context(progress_stream(progress_dict, model_id)))

@app.route('/terminate_train',methods=['POST'])
def terminate_train():
    key=urllib.parse.unquote(request.form['key'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    model_id=urllib.parse.unquote(request.form['model_id'])
    if key!=db_operate.get_model_key(model_id):
        return json.dumps(code_table.error_invalid_key, ensure_ascii=False)
    current_model_status=db_operate.get_model_status(model_id)
    if current_model_status is not code_table.model_status_first_train and current_model_status is not code_table.model_status_improve:
        return json.dumps(code_table.error_model_is_not_training, ensure_ascii=False)
    try:
        hyper_thread_dict[model_id]=0
    except:
        pass
    time.sleep(1)
    try:
        training_thread_dict[model_id].terminate()
    except:
        pass
    db_operate.train_model_stop(model_id)
    return json.dumps(code_table.ok, ensure_ascii=False)
    
@app.route('/predict',methods=['POST'])                                    #! would fail if model untrained
def predict():
    key=urllib.parse.unquote(request.form['key'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    model_id=urllib.parse.unquote(request.form['model_id'])
    if key!=db_operate.get_model_key(model_id):
        return json.dumps(code_table.error_invalid_key, ensure_ascii=False)
    if db_operate.get_model_status(model_id) is not code_table.model_status_predictable:
        return json.dumps(code_table.error_untrained_model, ensure_ascii=False)
    image=urllib.parse.unquote(request.form['base64_image'])
    try:
        image=base64.b64decode(image)
    except:
        return json.dumps(code_table.error_invalid_base64, ensure_ascii=False)
    image = cv2.imdecode(np.frombuffer(image, np.uint8), cv2.IMREAD_COLOR)
    if isinstance(image, np.ndarray) is not True and image is None:
        return json.dumps(code_table.error_not_image_data, ensure_ascii=False)
    p=pool.ThreadPool()
    result=p.apply_async(predict_start, (model_id, image))
    result=result.get()
    return_data=copy.deepcopy(code_table.ok)
    return_data['data']=result
    return json.dumps(return_data, ensure_ascii=False)

@app.route('/get_tflite',methods=['POST'])
def get_tflite():
    key=urllib.parse.unquote(request.form['key'])
    if is_key_exist(key)==False:
        return json.dumps(code_table.error_key_is_not_exist, ensure_ascii=False)
    model_id=urllib.parse.unquote(request.form['model_id'])
    if key!=db_operate.get_model_key(model_id):
        return json.dumps(code_table.error_invalid_key, ensure_ascii=False)
    if db_operate.get_model_status(model_id) is not code_table.model_status_predictable:
        return json.dumps(code_table.error_untrained_model, ensure_ascii=False)
    
    bytedata=open('models/'+model_id+'.h5', 'rb').read()
    response=requests.post('http://104.199.210.117:1024', data=bytedata)
    #def gen():
    #    return response.content
    #return Response(stream_with_context(gen()))
    return send_file(io.BytesIO(response.content), mimetype='application/octet-stream')


#@app.route('/get_all_models',methods=['POST'])
#def get_all_models():
#    pass

@app.errorhandler(404)
def page_not_found(e):
    return json.dumps(code_table.error_unknow_service, ensure_ascii=False)

if __name__ == '__main__':
    os.system('color')
    print('\033[93m'+'\t\t\t\t\tShould Turn On SQL Server!!'+'\033[0m')
    print('\033[93m'+'\t\t\t\t\tShould Turn On SQL Server!!'+'\033[0m')
    print('\033[93m'+'\t\t\t\t\tShould Turn On SQL Server!!'+'\033[0m')
    print('\033[93m'+'\t\t\t\t\tShould Turn On SQL Server!!'+'\033[0m')
    print('\033[93m'+'\t\t\t\t\tShould Turn On SQL Server!!'+'\033[0m')
    print()
    print('\033[93m'+'\t\t\t\t\tAnd Adjust System Time correctly!!'+'\033[0m')
    app.run(host='0.0.0.0',port=8080)
