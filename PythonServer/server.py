from flask import Flask, request, send_file, Response, stream_with_context
app = Flask(__name__)

import cv2, base64, json, io, os, urllib
import numpy as np

from train_server import train_start, progress_stream
from prediction_server import predict_start
import db_operate, code_table

from google.oauth2 import id_token
from google.auth.transport import requests as google_requests
def isInvalid(idtoken):
    try:
        idinfo=id_token.verify_oauth2_token(idtoken, google_requests.Request(), '1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com')
        db_operate.check_user_exist(idinfo['email'], idinfo['name'])
        return idinfo['email']
    except:
        return True

def db_result_parse_to_response_string(db_operate_result):
    if isinstance(db_operate_result, list):
        ok=code_table.ok
        ok['data']=db_operate_result
        return urllib.parse.quote_plus(json.dumps(ok, ensure_ascii=False))
    elif db_operate_result=='ok':
        return urllib.parse.quote_plus(json.dumps(code_table.ok, ensure_ascii=False))
    elif db_operate_result=='1169':
        return urllib.parse.quote_plus(json.dumps(code_table.error_duplicate_name, ensure_ascii=False))

@app.route('/model_list',methods=['POST'])
def model_list():
    idtoken=urllib.parse.unquote(request.form['idtoken'])
    mail=isInvalid(idtoken)
    if mail==True:
        return json.dumps(code_table.error_login_fail, ensure_ascii=False)
    id_name_acc_loss=db_operate.self_model_list(mail)
    return db_result_parse_to_response_string(id_name_acc_loss)
    
@app.route('/create_model',methods=['POST'])
def create_model():
    idtoken=urllib.parse.unquote(request.form['idtoken'])
    mail=isInvalid(idtoken)
    if mail==True:
        return json.dumps(code_table.error_login_fail, ensure_ascii=False)
    model_name=urllib.parse.unquote(request.form['model_name'])
    ok_error=db_operate.create_model(mail, model_name)
    return db_result_parse_to_response_string(ok_error)

@app.route('/delete_model',methods=['POST'])
def delete_model():
    idtoken=urllib.parse.unquote(request.form['idtoken'])
    mail=isInvalid(idtoken)
    if mail==True:
        return json.dumps(code_table.error_login_fail, ensure_ascii=False)
    model_id=urllib.parse.unquote(request.form['model_id'])
    ok_error=db_operate.delete_model(mail,model_id)
    return db_result_parse_to_response_string(ok_error)

@app.route('/model_label_list',methods=['POST'])
def model_label_list():
    idtoken=urllib.parse.unquote(request.form['idtoken'])
    mail=isInvalid(idtoken)
    if mail==True:
        return json.dumps(code_table.error_login_fail, ensure_ascii=False)
    model_id=urllib.parse.unquote(request.form['model_id'])
    id_name=db_operate.model_label_list(model_id)
    return db_result_parse_to_response_string(id_name)

@app.route('/create_label',methods=['POST'])
def create_label():
    idtoken=urllib.parse.unquote(request.form['idtoken'])
    mail=isInvalid(idtoken)
    if mail==True:
        return json.dumps(code_table.error_login_fail, ensure_ascii=False)
    name=urllib.parse.unquote(request.form['name'])
    model_id=urllib.parse.unquote(request.form['id'])
    ok_error=db_operate.create_label(mail, name, model_id)
    return db_result_parse_to_response_string(ok_error)

@app.route('/delete_label',methods=['POST'])
def delete_label():
    idtoken=urllib.parse.unquote(request.form['idtoken'])
    mail=isInvalid(idtoken)
    if mail==True:
        return json.dumps(code_table.error_login_fail, ensure_ascii=False)
    model_id=urllib.parse.unquote(request.form['id'])
    label_id=urllib.parse.unquote(request.form['label_id'])
    ok_error=db_operate.delete_label(mail, model_id, label_id)
    return db_result_parse_to_response_string(ok_error)

@app.route('/label_images',methods=['POST'])
def label_images():
    idtoken=urllib.parse.unquote(request.form['idtoken'])
    mail=isInvalid(idtoken)
    if mail==True:
        return json.dumps(code_table.error_login_fail, ensure_ascii=False)
    label_id=urllib.parse.unquote(request.form['id'])
    modelid_addlabel=db_operate.label_images(label_id)
    images=[]
    for folder in modelid_addlabel:
        for image in os.listdir(mail+'/'+folder['model_id']+'/'+folder['add_label']):
            new_dict={}
            new_dict['model_id']=folder['model_id']
            new_dict['add_label']=folder['add_label']
            new_dict['image']=image
            images.append(new_dict)
    if modelid_addlabel=='OK' or (isinstance(modelid_addlabel,list)==False and modelid_addlabel.isdigit()):
        return urllib.parse.quote_plus(modelid_addlabel)
    else:
        return urllib.parse.quote_plus(json.dumps(images, ensure_ascii=False))

@app.route('/get_image',methods=['POST','GET'])
def get_image():
    mail=urllib.parse.unquote(request.args.get('mail'))
    model=urllib.parse.unquote(request.args.get('model'))
    label=urllib.parse.unquote(request.args.get('label'))
    image=urllib.parse.unquote(request.args.get('image'))
    if request.args.get('type') is not None:
        type=urllib.parse.unquote(request.args.get('type'))
    img=cv2.imread(mail+'/'+model+'/'+label+'/'+image)
    if request.args.get('type') is not None and type!='full':
        width=300
        size=(width,int(img.shape[0]*(width/img.shape[1])))
        img=cv2.resize(img, size, interpolation=cv2.INTER_AREA)
    img=cv2.imencode('.jpg', img)[1].tostring()
    img=io.BytesIO(img)
    return send_file(img, mimetype='image/jpg')

@app.route('/write_image_htmlform',methods=['POST'])
def write_image_htmlform():
    mail=urllib.parse.unquote(request.form['mail'])
    model=urllib.parse.unquote(request.form['model'])
    label=urllib.parse.unquote(request.form['label'])
    image = request.files['image']
    image.save(mail+'/'+model+'/'+label+'/'+db_operate.id_hash_string()+'.jpg')
    return 'OK'

@app.route('/write_image_json',methods=['POST'])
def write_image_json():
    path_info=bytes(request.data).decode('utf-8')
    path_info=json.loads(path_info, strict=False)
    mail=path_info['mail']
    model=path_info['model']
    label=path_info['label']
    image=path_info['image']
    open(mail+'/'+model+'/'+label+'/'+db_operate.id_hash_string()+'.jpg','wb').write(base64.b64decode(image))
    return 'OK'

@app.route('/delete_image',methods=['POST'])
def delete_image():
    mail=urllib.parse.unquote(request.form['mail'])
    model=urllib.parse.unquote(request.form['model'])
    label=urllib.parse.unquote(request.form['label'])
    image=urllib.parse.unquote(request.form['image'])
    os.remove(mail+'/'+model+'/'+label+'/'+image)
    return urllib.parse.quote_plus('OK')

@app.route('/delete_image_url',methods=['POST'])
def delete_image_url():
    mail=urllib.parse.unquote(request.args.get('mail'))
    model=urllib.parse.unquote(request.args.get('model'))
    label=urllib.parse.unquote(request.args.get('label'))
    image=urllib.parse.unquote(request.args.get('image'))
    os.remove(mail+'/'+model+'/'+label+'/'+image)
    return 'OK'
    
@app.route('/progress_list',methods=['POST'])
def progress_list():
    mail=urllib.parse.unquote(request.form['mail'])
    id_name=db_operate.training_model_list(mail)
    if id_name=='OK' or (isinstance(id_name,list)==False and id_name.isdigit()):
        return urllib.parse.quote_plus(id_name)
    else:
        return urllib.parse.quote_plus(json.dumps(id_name, ensure_ascii=False))
    
@app.route('/predict_model_list',methods=['POST'])
def predict_model_list():
    mail=urllib.parse.unquote(request.form['mail'])
    id_name_ownermail=db_operate.predict_model_list(mail)
    if id_name_ownermail=='OK' or (isinstance(id_name_ownermail,list)==False and id_name_ownermail.isdigit()):
        return urllib.parse.quote_plus(id_name_ownermail)
    else:
        return urllib.parse.quote_plus(json.dumps(id_name_ownermail, ensure_ascii=False))
    
    
    
    


from multiprocessing import Process, Manager, pool
import time
untrain_queue=[]# not yet
training_dict={}
def clean_train_dict():
    while 1:
        for t in training_dict:
            if training_dict[t].is_alive()==False:
                del training_dict[t]
        time.sleep(3600)
if __name__ == '__main__':
    Process(target=clean_train_dict).start()
@app.route('/train',methods=['POST'])
def train():# and mail notify
    mail=urllib.parse.unquote(request.form['mail'])
    model_id=urllib.parse.unquote(request.form['model'])
    progress_dict[model_id]={'epoch':'0','acc':'none','loss':'inf'}
    
    img_num_check=True
    label_dirs=db_operate.model_label_list(model_id)
    label_dirs=[label_info['id'] for label_info in label_dirs]
    for label in label_dirs:
        if len(os.listdir(mail+'/'+model_id+'/'+label))==0:
            img_num_check=False
            break
    result=db_operate.model_label_list(model_id)
    if len(result)<2 or img_num_check==False:
        return '222000' #num of label smaller than 2 or empty label
    
    db_operate.change_model_statu(model_id, '2')
    batch_size=32
    patient=2
    if __name__ == '__main__':
        training_dict[model_id]=Process(target=train_start, args=(progress_dict, mail, model_id, batch_size, patient))
        training_dict[model_id].start()
    return 'OK'

if __name__ == '__main__':
    progress_dict=Manager().dict()
@app.route('/progress',methods=['POST'])
def progress():
    model_id=urllib.parse.unquote(request.form['model'])
    return Response(stream_with_context(progress_stream(progress_dict, model_id)))

@app.route('/terminate_train',methods=['POST'])
def terminate_train():
    model_id=urllib.parse.unquote(request.form['model'])
    if db_operate.is_model_trained(model_id):
        db_operate.change_model_statu(model_id, '1')
    else:
        db_operate.change_model_statu(model_id, '0')
    training_dict[model_id].terminate()
    return 'OK'
    
@app.route('/predict',methods=['POST'])
def predict():
    req_json=bytes(request.data).decode('utf-8')
    req_json=json.loads(req_json, strict=False)
    mail=req_json['mail']
    model_id=req_json['model']
    image=req_json['image']
    image=cv2.imdecode(np.fromstring(base64.b64decode(image), np.uint8), cv2.IMREAD_COLOR)
    path=mail+'/'+model_id+'/'
    
    dict=json.loads(db_operate.label_class(model_id)[0]['dict_string'], strict=False)
    
    p=pool.ThreadPool()
    result=p.apply_async(predict_start, (path, image, dict))
    result=result.get()
    result=dict[str(result)]
    
    return result

@app.errorhandler(404)
def page_not_found(e):
    print('unknow url')
    return ''

if __name__ == '__main__':
    app.run(host='0.0.0.0',port=8080)