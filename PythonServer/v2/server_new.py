# assume all connect is work, so error code only focus on operate reject

from flask import Flask, request, Response, stream_with_context
from flask_cors import CORS
import cv2, base64, urllib, json
import numpy as np
import db_operate, drive_operate
from train_server import train_start, progress_stream
from prediction_server import predict_start
app = Flask(__name__)
CORS(app, resources=r'/*')

@app.route('/model_list',methods=['POST'])
def model_list():#return: id, name, acc, loss
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    id_name_LIST=drive_operate.model_list(mail)
    for i in id_name_LIST:
        acc_loss_DICT=db_operate.acc_loss(i['id'])
        if len(acc_loss_DICT)==0:
            i['acc']=''
            i['loss']=''
        else:
            i['acc']=acc_loss_DICT['acc']
            i['loss']=acc_loss_DICT['loss']
    return json.dumps(id_name_LIST, ensure_ascii=False)
    
@app.route('/create_model',methods=['POST'])
def create_model():#return: ok or error
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    model_name=request.form['model_name']
    ok_error=drive_operate.create_model(mail, model_name)
    if ok_error:
        return '{"ok": "ok"}'
    else:
        return '{"error": "name duplicate"}'

@app.route('/delete_model',methods=['POST'])
def delete_model():
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    model_id=request.form['model_id']
    db_operate.delete_model(model_id)
    drive_operate.delete_model(mail, model_id)
    return '{"ok": "ok"}'

@app.route('/label_list',methods=['POST'])
def label_list():
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    model_id=request.form['model_id']
    id_name=drive_operate.label_list(mail, model_id)
    return json.dumps(id_name, ensure_ascii=False)

@app.route('/create_label',methods=['POST'])
def create_label():
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    model_id=request.form['model_id']
    label_name=request.form['label_name']
    ok_error=drive_operate.create_label(mail, model_id, label_name)
    if ok_error:
        return '{"ok": "ok"}'
    else:
        return '{"error": "name duplicate"}'

@app.route('/delete_label',methods=['POST'])
def delete_label():
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    label_id=request.form['label_id']
    drive_operate.delete_label(mail, label_id)
    return '{"ok": "ok"}'

@app.route('/write_image',methods=['POST'])
def write_image():
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    label_id=request.form['label_id']
    image=request.form['image']
    image=base64.b64decode(image)
    check_isImage = cv2.imdecode(np.frombuffer(image, np.uint8), cv2.IMREAD_COLOR)
    if type(check_isImage)==None:
        return '{"error": "non image data"}'
    drive_operate.write_image(mail, label_id, image)
    return '{"ok": "ok"}'

@app.route('/label_images',methods=['POST'])
def label_images():
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    label_id=request.form['label_id']
    image_id_LIST=drive_operate.label_images(mail, label_id)
    return json.dumps(image_id_LIST, ensure_ascii=False)

@app.route('/delete_image',methods=['POST'])
def delete_image():
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    image_id=request.form['image_id']
    drive_operate.delete_image(mail, image_id)
    return '{"ok": "ok"}'

@app.route('/get_image',methods=['POST'])
def get_image():# return base64 string
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    image_id=request.form['image_id']
    if request.form['type'] is not None:
        type=request.form['type']
    img=drive_operate.get_image(mail, image_id)
    img=np.frombuffer(img, np.uint8)
    img=cv2.imdecode(img, cv2.IMREAD_COLOR)
    if request.form['type'] is not None and type!='full':
        width=300
        size=(width,int(img.shape[0]*(width/img.shape[1])))
        img=cv2.resize(img, size, interpolation=cv2.INTER_AREA)
    img=cv2.imencode('.jpg', img)[1].tostring()
    return base64.b64encode(img)






@app.route('/show_training_list',methods=['POST'])
def show_training_list():
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    id_name=db_operate.show_training_list(mail)
    return json.dumps(id_name, ensure_ascii=False)
    
@app.route('/predictable_list',methods=['POST'])
def predictable_list():
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    id_name_owner=db_operate.predictable_list(mail)
    return json.dumps(id_name_owner, ensure_ascii=False)






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
    progress_dict=Manager().dict()
    
@app.route('/train',methods=['POST'])
def train():# and mail notify
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    model_id=request.form['model_id']
    
    if drive_operate.check_isTrainable(mail, model_id)==False:
        return '{"error": "num of label < 2 or empty label"}'
    db_data=drive_operate.model_db_data(mail, model_id)
    progress_dict[model_id]={'epoch':'0','acc':'none','loss':'inf'}
    
    if len(db_operate.model_history(model_id))==0:
        db_data['statu']=1 # 1:initial, 2:improve, 0:predictable
    else:
        db_data['statu']=2
    if __name__ == '__main__':
        training_dict[model_id]=Process(target=train_start, args=(progress_dict, db_data))
        training_dict[model_id].start()
    return 'OK'

@app.route('/progress',methods=['POST'])
def progress():
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    model_id=request.form['model_id']
    return Response(stream_with_context(progress_stream(progress_dict, model_id)))

@app.route('/terminate_train',methods=['POST'])
def terminate_train():
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    model_id=request.form['model_id']
    training_dict[model_id].terminate()
    return 'OK'
    
@app.route('/predict',methods=['POST'])
def predict():
    idtoken=request.form['idtoken']
    authcode=request.form['authcode']
    mail=drive_operate.isInvalid(idtoken, authcode)
    if mail==True:
        return '{"error": "google sign in fail, try again"}'
    model_id=request.form['model_id']
    image=request.form['image']
    image=cv2.imdecode(np.fromstring(base64.b64decode(image), np.uint8), cv2.IMREAD_COLOR)    
    dict=json.loads(db_operate.model_history(model_id)['class_label'])
    
    p=pool.ThreadPool()
    result=p.apply_async(predict_start, (model_id, image))
    result=result.get()
    result=dict[str(result)]
    
    return result

@app.errorhandler(404)
def page_not_found(e):
    print('unknow url')
    return ''

if __name__ == '__main__':
    app.run(host='0.0.0.0',port=8080)






