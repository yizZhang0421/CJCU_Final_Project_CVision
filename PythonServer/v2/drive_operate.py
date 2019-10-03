import io, db_operate, requests, cv2, json, random
import numpy as np
from io import BytesIO

from apiclient import discovery
import httplib2
from oauth2client import client
from oauth2client.client import GoogleCredentials
from apiclient.http import MediaIoBaseUpload, MediaIoBaseDownload
from google.oauth2 import id_token
from google.auth.transport import requests as google_requests

def isInvalid(idtoken, authcode):
    try:
        idinfo=id_token.verify_oauth2_token(idtoken, google_requests.Request(), '1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com')
        if db_operate.get_refresh_token(idinfo['email'])==None:
            credentials = client.credentials_from_clientsecrets_and_code(
                    'credentials.json',
                    ['https://www.googleapis.com/auth/drive.appfolder'],
                    authcode,
                redirect_uri='http://localhost')
            if credentials.refresh_token==None or credentials.refresh_token=='':
                requests.post(
                        'https://accounts.google.com/o/oauth2/revoke',
                        params={'token': credentials.access_token},
                        headers = {'content-type': 'application/x-www-form-urlencoded'})
                return True
            db_operate.registe(idinfo['email'], credentials.refresh_token)
        return idinfo['email']
    except:
        return True
    
def model_list(mail):
    refresh_token=db_operate.get_refresh_token(mail)
    credentials=GoogleCredentials(refresh_token=refresh_token,
                      client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                           client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                           token_uri='https://oauth2.googleapis.com/token',
                           access_token=None,
                           token_expiry=None,
                           user_agent=None)
    http_auth = credentials.authorize(httplib2.Http())
    drive_service = discovery.build('drive', 'v3', http=http_auth)
    response = drive_service.files().list(
            spaces='appDataFolder',
            fields='files(id, name)',
            q="mimeType='application/vnd.google-apps.folder' and 'appDataFolder' in parents").execute()
    result=list()
    in_training_list=db_operate.show_training_list(mail)
    for i in response.get('files', []):
        if i.get('id') not in in_training_list:
            result.append({'id':i.get('id'), 'name':i.get('name')})
    return result

def create_model(mail, model_name):
    refresh_token=db_operate.get_refresh_token(mail)
    credentials=GoogleCredentials(refresh_token=refresh_token,
                      client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                           client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                           token_uri='https://oauth2.googleapis.com/token',
                           access_token=None,
                           token_expiry=None,
                           user_agent=None)
    http_auth = credentials.authorize(httplib2.Http())
    drive_service = discovery.build('drive', 'v3', http=http_auth)
    response = drive_service.files().list(
            spaces='appDataFolder',
            fields='files(id, name)',
            q="mimeType='application/vnd.google-apps.folder' and 'appDataFolder' in parents").execute()
    try:
        [i.get('name') for i in response.get('files', [])].index(model_name)
        return False
    except:
        pass
    file_metadata = {
            'name': model_name,
            'parents': ['appDataFolder'],
            'mimeType': 'application/vnd.google-apps.folder'}
    drive_service.files().create(body=file_metadata).execute()
    return True

def delete_model(mail, model_id):
    refresh_token=db_operate.get_refresh_token(mail)
    credentials=GoogleCredentials(refresh_token=refresh_token,
                      client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                           client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                           token_uri='https://oauth2.googleapis.com/token',
                           access_token=None,
                           token_expiry=None,
                           user_agent=None)
    http_auth = credentials.authorize(httplib2.Http())
    drive_service = discovery.build('drive', 'v3', http=http_auth)
    drive_service.files().delete(fileId=model_id).execute()

def label_list(mail, model_id):
    refresh_token=db_operate.get_refresh_token(mail)
    credentials=GoogleCredentials(refresh_token=refresh_token,
                      client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                           client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                           token_uri='https://oauth2.googleapis.com/token',
                           access_token=None,
                           token_expiry=None,
                           user_agent=None)
    http_auth = credentials.authorize(httplib2.Http())
    drive_service = discovery.build('drive', 'v3', http=http_auth)
    response = drive_service.files().list(
            spaces='appDataFolder',
            fields='files(id, name)',
            q='mimeType="application/vnd.google-apps.folder" and "'+model_id+'" in parents').execute()
    return [{'id':i.get('id'), 'name':i.get('name')} for i in response.get('files', [])]

def create_label(mail, model_id, label_name):
    refresh_token=db_operate.get_refresh_token(mail)
    credentials=GoogleCredentials(refresh_token=refresh_token,
                      client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                           client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                           token_uri='https://oauth2.googleapis.com/token',
                           access_token=None,
                           token_expiry=None,
                           user_agent=None)
    http_auth = credentials.authorize(httplib2.Http())
    drive_service = discovery.build('drive', 'v3', http=http_auth)
    response = drive_service.files().list(
            spaces='appDataFolder',
            fields='files(id, name)',
            q='mimeType="application/vnd.google-apps.folder" and "'+model_id+'" in parents').execute()
    try:
        [i.get('name') for i in response.get('files', [])].index(label_name)
        return False
    except:
        pass
    file_metadata = {
            'name': label_name,
            'parents': [model_id],
            'mimeType': 'application/vnd.google-apps.folder'}
    drive_service.files().create(body=file_metadata).execute()
    return True
    
def delete_label(mail, label_id):# should modify to remove parents, if 0 just delete file
    refresh_token=db_operate.get_refresh_token(mail)
    credentials=GoogleCredentials(refresh_token=refresh_token,
                      client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                           client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                           token_uri='https://oauth2.googleapis.com/token',
                           access_token=None,
                           token_expiry=None,
                           user_agent=None)
    http_auth = credentials.authorize(httplib2.Http())
    drive_service = discovery.build('drive', 'v3', http=http_auth)
    drive_service.files().delete(fileId=label_id).execute()

def write_image(mail, label_id, image):
    refresh_token=db_operate.get_refresh_token(mail)
    credentials=GoogleCredentials(refresh_token=refresh_token,
                      client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                           client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                           token_uri='https://oauth2.googleapis.com/token',
                           access_token=None,
                           token_expiry=None,
                           user_agent=None)
    http_auth = credentials.authorize(httplib2.Http())
    drive_service = discovery.build('drive', 'v3', http=http_auth)
    file_metadata = {
            'name': id_hash_string(),
            'parents': [label_id]
            }
    fh = BytesIO(image)
    media_body = MediaIoBaseUpload(fh, mimetype='image/*')
    drive_service.files().create(body=file_metadata, media_body=media_body).execute()
    
def label_images(mail, label_id):
    refresh_token=db_operate.get_refresh_token(mail)
    credentials=GoogleCredentials(refresh_token=refresh_token,
                      client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                           client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                           token_uri='https://oauth2.googleapis.com/token',
                           access_token=None,
                           token_expiry=None,
                           user_agent=None)
    http_auth = credentials.authorize(httplib2.Http())
    drive_service = discovery.build('drive', 'v3', http=http_auth)
    response = drive_service.files().list(
            spaces='appDataFolder',
            fields='files(id, name)',
            q='"'+label_id+'" in parents').execute()
    return [i.get('id') for i in response.get('files', [])]

def delete_image(mail, image_id):
    refresh_token=db_operate.get_refresh_token(mail)
    credentials=GoogleCredentials(refresh_token=refresh_token,
                      client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                           client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                           token_uri='https://oauth2.googleapis.com/token',
                           access_token=None,
                           token_expiry=None,
                           user_agent=None)
    http_auth = credentials.authorize(httplib2.Http())
    drive_service = discovery.build('drive', 'v3', http=http_auth)
    drive_service.files().delete(fileId=image_id).execute()

def get_image(mail, image_id):
    refresh_token=db_operate.get_refresh_token(mail)
    credentials=GoogleCredentials(refresh_token=refresh_token,
                      client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                           client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                           token_uri='https://oauth2.googleapis.com/token',
                           access_token=None,
                           token_expiry=None,
                           user_agent=None)
    http_auth = credentials.authorize(httplib2.Http())
    drive_service = discovery.build('drive', 'v3', http=http_auth)
    request = drive_service.files().get_media(fileId=image_id)
    fh = io.BytesIO()
    downloader = MediaIoBaseDownload(fh, request)
    done = False
    while done is False:
        done = downloader.next_chunk()
    return fh.getvalue()

def check_isTrainable(mail, model_id):
    refresh_token=db_operate.get_refresh_token(mail)
    credentials=GoogleCredentials(refresh_token=refresh_token,
                      client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                           client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                           token_uri='https://oauth2.googleapis.com/token',
                           access_token=None,
                           token_expiry=None,
                           user_agent=None)
    http_auth = credentials.authorize(httplib2.Http())
    drive_service = discovery.build('drive', 'v3', http=http_auth)
    response = drive_service.files().list(
            spaces='appDataFolder',
            fields='files(id)',
            q='"'+model_id+'" in parents').execute()
    labels=[i.get('id') for i in response.get('files', [])]
    if len(labels)<2:
        return False
    for label in labels:
        response = drive_service.files().list(
                spaces='appDataFolder',
                fields='files(id)',
                q='"'+label+'" in parents').execute()
        images=[i.get('id') for i in response.get('files', [])]
        if len(images)==0:
            return False
    return True

def model_db_data(mail, model_id):
    refresh_token=db_operate.get_refresh_token(mail)
    credentials=GoogleCredentials(refresh_token=refresh_token,
                      client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                           client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                           token_uri='https://oauth2.googleapis.com/token',
                           access_token=None,
                           token_expiry=None,
                           user_agent=None)
    http_auth = credentials.authorize(httplib2.Http())
    drive_service = discovery.build('drive', 'v3', http=http_auth)
    response = drive_service.files().list(
            spaces='appDataFolder',
            fields='files(id, name)',
            q='"'+model_id+'" in parents').execute()
    labels_name=[i.get('name') for i in response.get('files', [])]
    num_of_images=0
    labels=[i.get('id') for i in response.get('files', [])]
    for label in labels:
        response = drive_service.files().list(
                spaces='appDataFolder',
                fields='files(id)',
                q='"'+label+'" in parents').execute()
        images=[i.get('id') for i in response.get('files', [])]
        num_of_images+=len(images)
    db_data={}
    db_data['id']=model_id
    db_data['name']=(drive_service.files().get(fileId=model_id).execute())['name']
    db_data['acc']=None
    db_data['loss']=None
    db_data['class_label']=json.dumps({labels_name.index(i):i for i in labels_name}, ensure_ascii=False)
    db_data['owner']=mail
    db_data['size']=None
    db_data['statu']=None
    db_data['num_of_images']=num_of_images
    return db_data

def train_data_generator(mail, model_id, batch_size):
    x=[]
    y=[]
    refresh_token=db_operate.get_refresh_token(mail)
    credentials=GoogleCredentials(refresh_token=refresh_token,
                      client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                           client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                           token_uri='https://oauth2.googleapis.com/token',
                           access_token=None,
                           token_expiry=None,
                           user_agent=None)
    http_auth = credentials.authorize(httplib2.Http())
    drive_service = discovery.build('drive', 'v3', http=http_auth)
    response = drive_service.files().list(
            spaces='appDataFolder',
            fields='files(id, name)',
            q='"'+model_id+'" in parents').execute()
    labels=[i.get('id') for i in response.get('files', [])]
    for label in labels:
        response = drive_service.files().list(
                spaces='appDataFolder',
                fields='files(id)',
                q='"'+label+'" in parents').execute()
        images=[i.get('id') for i in response.get('files', [])]
        x+=images
        y+=[labels.index(label)]*len(images)
    xy=list(zip(x,y))
    random.shuffle(xy)
    (x, y)=zip(*xy)
    
    x_=[]
    y_=[]
    for image_id, label in zip(x, y):
        try:
            request = drive_service.files().get_media(fileId=image_id)
        except:
            credentials=GoogleCredentials(refresh_token=refresh_token,
                              client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                                   client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                                   token_uri='https://oauth2.googleapis.com/token',
                                   access_token=None,
                                   token_expiry=None,
                                   user_agent=None)
            http_auth = credentials.authorize(httplib2.Http())
            drive_service = discovery.build('drive', 'v3', http=http_auth)
            request = drive_service.files().get_media(fileId=image_id)
        fh = io.BytesIO()
        downloader = MediaIoBaseDownload(fh, request)
        done = False
        while done is False:
            done = downloader.next_chunk()
        img=fh.getvalue()
        img=np.frombuffer(img, np.uint8)
        img=cv2.imdecode(img, cv2.IMREAD_COLOR)
        img=cv2.resize(img,(100,100),interpolation=cv2.INTER_CUBIC)
        img=img/255
        x_.append(img)
        if len(labels)>2:
            onehot=np.zeros([len(labels)])
            onehot[label]=1
            y_.append(onehot)
        else:
            y_.append(int(label))
        if len(x_)==batch_size:
            yield (np.array(x_), np.array(y_))
            x_=[]
            y_=[]
    yield (np.array(x_), np.array(y_))
    
    
    
    
    
import uuid, time
def id_hash_string():
    ms=int(round(time.time() * 1000))
    id = str(ms)+'-'+str(uuid.uuid4())
    return id
    
    
    