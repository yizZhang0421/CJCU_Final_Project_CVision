ok={'status':0, 'message':'ok', 'data':''}
error_login_fail={'status':1, 'message':'login fail'}
error_google_down={'status':2, 'message':'cannot reach google authenticate server'}
error_database_down={'status':3, 'message':'database server down, mail admin'}
error_server_down={'status':4, 'message':'server down'}
error_duplicate_name={'status':51, 'message':'invalid operate, duplicate name'}
error_capacity_exceed={'status':52, 'message':'invalid operate, capacity exceed'}

model_status_predictable=0
model_status_first_train=1
model_status_improve=2

model_permission_denyall=0
model_permission_owner=31 #11111, means all, five permission
model_permission_predict=1
model_permission_browerimage=2
model_permission_train=4
