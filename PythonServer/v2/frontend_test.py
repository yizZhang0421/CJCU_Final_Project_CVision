import requests
a='eyJhbGciOiJSUzI1NiIsImtpZCI6ImYyNGQ2YTE5MzA2NjljYjc1ZjE5NzBkOGI3NTRhYTE5M2YwZDkzMWYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxMDA0MDgzODgyMjk1LXJhbHVjbzlrdnRrMGdxMzg2MnAxYmc1Ymo2ZzRuMjNoLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiMTAwNDA4Mzg4MjI5NS11Y3UzdG5mMTA0amxudHZpajFlOHViMDEyZzY3czA1Ni5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwMzQ3MTMxNTYyNDkwNTk1ODQyNSIsImhkIjoibWFpbHN0LmNqY3UuZWR1LnR3IiwiZW1haWwiOiJoMjQ1NjMwMjZAbWFpbHN0LmNqY3UuZWR1LnR3IiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJ5aXogWmhhbmciLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tLy1SRmRWeWFDdHg5RS9BQUFBQUFBQUFBSS9BQUFBQUFBQUFBQS9BQ0hpM3Jjajg4ZWZmOVNhNW5RcWljTl9zMEtrdWVROG1nL3M5Ni1jL3Bob3RvLmpwZyIsImdpdmVuX25hbWUiOiJ5aXoiLCJmYW1pbHlfbmFtZSI6IlpoYW5nIiwibG9jYWxlIjoiemgtVFciLCJpYXQiOjE1NTE0NDkwNTcsImV4cCI6MTU1MTQ1MjY1N30.d46RsMRAQTeNI9o3uWgr_LFiUFNAcKU4kKtuBl2t4a4W1mhhMqzvrDb6M02xdNKA-xgKdjOrCLGkswCX0nIHmDLG1vq-Pkur0AyLB8uqZVdqzMEVw8FCmWfKjaFhQ3QPSqtV2eBQXVqYaiuygB8W76VSnfdhLP59XbY8ihCwE2g2pcMS0KZOQCu3ZbVFDARNAvXtW-GJF8pxNjDji6HaP-CJDYPvuJZShrvDynKU9YvaGR7IBsGTq5RPKk5F5aUjqZNQdBpdyFafbKJdbe9yZcnu8dp0SgQdt87QbhPwlRSVacyHFVmngkc5fC7eoiewhiUMGSs-kcnEJDga_m8B2g'
b='4/_QBW0_tlOyoR4EMuOKdm77Sq9ZkQ32oCpi4z6lIWvXXYjL6ZbqzbFqDURYBEDJo7f6y1a3tJzqr256s3y3Z_gvA'
c='A'
response = requests.post(
        url='http://127.0.0.1:8080/create_model',
        data={'idtoken':a,'authcode':b,'model_name':c})
response.text

response = requests.post(
        url='http://127.0.0.1:8080/model_list',
        data={'idtoken':a,'authcode':b})
response.text

response = requests.post(
        url='http://127.0.0.1:8080/delete_model',
        data={'idtoken':a,'authcode':b,'model_id':'1dm92_WSgz-lfqtnQISfH-OxCeXSo9sjudLnhBC92_795R-kaGuA'})
response.text

response = requests.post(
        url='http://127.0.0.1:8080/label_list',
        data={'idtoken':a,'authcode':b,'model_id':'1FkPtj9f9X7sv6-0QfeblMOhcyLvvYRVZQnIeuEkbvuB8YGIMzIo'})
response.text

response = requests.post(
        url='http://127.0.0.1:8080/create_label',
        data={'idtoken':a,'authcode':b,'model_id':'1FkPtj9f9X7sv6-0QfeblMOhcyLvvYRVZQnIeuEkbvuB8YGIMzIo','label_name':'L1'})
response.text

response = requests.post(
        url='http://127.0.0.1:8080/delete_label',
        data={'idtoken':a,'authcode':b,'label_id':'1Dm17o3s34f0W8tjGp6uXOaPg5r8HqVyU_j0-7argECqbriaOyfo'})
response.text

import drive_operate
mail='h24563026@mailst.cjcu.edu.tw'
models=drive_operate.model_list(mail)
labels=drive_operate.label_list(mail, models[0]['id'])
label_id='1zSYax6RSDxOS0OuDUbYPsj6czQ41Kw941HIXAGu9NJIC_qUOt8c'
image=open('C:/Users/z5877/Desktop/timetable.png', 'rb').read()
drive_operate.write_image(mail, label_id, image)

drive_operate.label_images(mail, label_id)

drive_operate.delete_image(mail, '1athvhYp8IvTr39WlNsON-vhq-xzacBBnS2cuTXbW4hc0TpdTMAU')

image_id='1KFG1k94Jks_ajBv8QJ6udr7lUvTZXx4wQU0tFD8KPLaTSD0Rytk'
response = requests.post(
        url='http://127.0.0.1:8080/get_image',
        data={'idtoken':a,'authcode':b,'image_id':image_id,'type':'asd'})
response.text





from google.oauth2 import id_token
from google.auth.transport import requests as google_requests
CLIENT_ID='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com'
idinfo = id_token.verify_oauth2_token(a, google_requests.Request(), CLIENT_ID)


from oauth2client import client
from apiclient import discovery
import httplib2
credentials = client.credentials_from_clientsecrets_and_code(
        'credentials.json',
        ['https://www.googleapis.com/auth/drive.appfolder'],
        b,
        redirect_uri='http://localhost')
http_auth = credentials.authorize(httplib2.Http())
drive_service = discovery.build('drive', 'v3', http=http_auth)

from oauth2client.client import GoogleCredentials
credentials=GoogleCredentials(refresh_token=credentials.refresh_token,
                       client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                       client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                       token_uri='https://oauth2.googleapis.com/token',
                       access_token=None,
                       token_expiry=None,
                       user_agent=None)