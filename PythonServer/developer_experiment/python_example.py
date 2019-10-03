from oauth2client.client import OAuth2WebServerFlow

flow = OAuth2WebServerFlow(client_id='484210321385-5vd73saefu7ldrtcb7j8870n5tg8l8b5.apps.googleusercontent.com',
                           client_secret='P_9nmojcQc_meL_XEn9uFRht',
                           scope='profile',
                           redirect_uri='urn:ietf:wg:oauth:2.0:oob')
auth_uri = flow.step1_get_authorize_url()

code='4/EgGCtmVvc2JV34C-qUIZBzrWAfE3-kEwg08BlosL6qCZyf5p1nz-6io'
credentials = flow.step2_exchange(code)
import json
idtoken=json.loads(credentials.to_json())['id_token_jwt']
clientid=credentials.client_id