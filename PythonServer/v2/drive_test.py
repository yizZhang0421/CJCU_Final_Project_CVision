from apiclient import discovery
import httplib2
from oauth2client import client
from apiclient.http import MediaFileUpload
from oauth2client.client import GoogleCredentials

credentials = client.credentials_from_clientsecrets_and_code(
    'credentials.json',
    ['https://www.googleapis.com/auth/drive.appfolder'],
    '4/-wDvJeilCZEN6n7B-VUEyX91Wp1NobMhZtvxddR8PJeScBk2PydkzAJnQlLe2ts-5LI95HE5xGQ83JWRSnYdVLY',
    redirect_uri='http://localhost')

## use refresh token to refresh access token
credentials.refresh_token
test=GoogleCredentials(refresh_token=credentials.refresh_token,
                       client_id='1004083882295-ucu3tnf104jlntvij1e8ub012g67s056.apps.googleusercontent.com',
                       client_secret='B8RFOwE_Kd2mFDdIoRYF7Ehu',
                       token_uri='https://oauth2.googleapis.com/token',
                       access_token=None,
                       token_expiry=None,
                       user_agent=None)
credentials=test
##

http_auth = credentials.authorize(httplib2.Http())
drive_service = discovery.build('drive', 'v3', http=http_auth)

credentials.access_token
credentials.refresh_token
a=credentials.to_json()


#create folder in root
file_metadata = {
    'name': 'Invoices',
    'parents': ['appDataFolder'],
    'mimeType': 'application/vnd.google-apps.folder'
}
file = drive_service.files().create(body=file_metadata,
                                    fields='id').execute()

#app folder root id
appdata_folderid='appDataFolder'


#create file in folder
file_metadata = {
    'name': 'config.json',
    'parents': ['1L_ZcOQAnnk_b8NlHu_Z1PPYfF2e9j7X6YO08eD3EMW1-ohN0dy4']
}
media = MediaFileUpload('cir.sql',
                        mimetype='application/json',
                        resumable=True)
file = drive_service.files().create(body=file_metadata,
                                    media_body=media,
                                    fields='id').execute()




#list all files include folder and sub files
response = drive_service.files().list(
        spaces='appDataFolder',
        fields='nextPageToken, files(id, name, mimeType)',
        pageSize=10).execute()
for file in response.get('files', []):
    print (file.get('name'), file.get('id'), file.get('mimeType'))
    

#list all folders
response = drive_service.files().list(
        spaces='appDataFolder',
        fields='nextPageToken, files(id, name)',
        q="mimeType='application/vnd.google-apps.folder'",
        pageSize=10).execute()
for file in response.get('files', []):
    print ('Found file: %s (%s)' % (file.get('name'), file.get('id')))

    
#list all files in specific folder
response = drive_service.files().list(
        spaces='appDataFolder',
        fields='nextPageToken, files(id, name)',
        q="mimeType!='application/vnd.google-apps.folder' and '1L_ZcOQAnnk_b8NlHu_Z1PPYfF2e9j7X6YO08eD3EMW1-ohN0dy4' in parents",
        pageSize=10).execute()
for file in response.get('files', []):
    print ('Found file: %s (%s)' % (file.get('name'), file.get('id')))

#delete folder
file = drive_service.files().delete(fileId='1L_ZcOQAnnk_b8NlHu_Z1PPYfF2e9j7X6YO08eD3EMW1-ohN0dy4').execute()

#empty appDataFolder
response = drive_service.files().list(
        spaces='appDataFolder',
        fields='nextPageToken, files(id, name)',
        q="mimeType!='application/vnd.google-apps.folder' and '1L_ZcOQAnnk_b8NlHu_Z1PPYfF2e9j7X6YO08eD3EMW1-ohN0dy4' in parents",
        pageSize=10).execute()
for file in response.get('files', []):
    file = drive_service.files().delete(fileId=file.get('id')).execute()

'''download file byte from file id
file_id = '0BwwA4oUTeiV1UVNwOHItT0xfa2M'
request = drive_service.files().get_media(fileId=file_id)
fh = io.BytesIO()
downloader = MediaIoBaseDownload(fh, request)
done = False
while done is False:
    status, done = downloader.next_chunk()
    print ("Download %d%%." % int(status.progress() * 100))
'''

'''insert file from byte stream
file_metadata = {
    'title': 'My Report',
    'mimeType': 'application/vnd.google-apps.spreadsheet'
}
file = service.files().insert(body=file_metadata,, new InputStreamContent(mimeType, 
new ByteArrayInputStream(filename))).execute();
'''


# get quota    
response=drive_service.about().get(fields='storageQuota').execute()

'storageQuota: '+str(((int(response['storageQuota']['limit'])/1024)/1024)/1024)+'G'

# if exchange creditial's refresh_token is None, use code below, it would remove permission, 
# esnd some signal to prompt driev allow popup and allow again, send The First access token to server
import requests
requests.post(
        'https://accounts.google.com/o/oauth2/revoke',
        params={'token': credentials.access_token},
        headers = {'content-type': 'application/x-www-form-urlencoded'})
    
    
    
    