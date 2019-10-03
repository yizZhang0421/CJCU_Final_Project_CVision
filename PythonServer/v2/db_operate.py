from mysql import connector

def registe(mail, token):
    command='select * from refresh_token where mail='+list_to_value_string([mail])
    result=do(command)
    if len(result)==0:
        command='insert into refresh_token value('+list_to_value_string([mail, token])+')'
        do(command)

def get_refresh_token(mail):
    try:
        command='select token from refresh_token where mail='+list_to_value_string([mail])
        return do(command)[0]['token']
    except:
        return None

def acc_loss(id):
    command='select acc,loss from model where id='+list_to_value_string([id])
    return do(command)

def delete_model(model_id):
    command='delete from model where id='+list_to_value_string([model_id])
    do(command)


def model_history(model_id):
    command='select * from model where id='+list_to_value_string([model_id])
    return do(command)
def addto_training_list(db_data):
    if len(model_history(db_data['id']))==0:
        command='insert into model(id, statu) value('+list_to_value_string([str(db_data['id']), str(db_data['statu'])])+')'
    else:
        command='update model set statu='+list_to_value_string([db_data['statu']])+' where id='+list_to_value_string([db_data['id']])
    do(command)
def show_training_list(mail):
    command='select id from model where owner='+list_to_value_string([mail])+' and statu in ('+list_to_value_string(['1', '2'])+')'
    return do(command)
def train_finish(db_data):
    command='update model set '+key_equal(db_data, ['id','num_of_images'])+' where id='+list_to_value_string([db_data['id']])
    do(command)
def train_stop(db_data):
    if db_data['statu']==1:
        command='delete from model where id='+list_to_value_string([db_data['id']])
    elif db_data['statu']==2:
        command='update model set statu='+list_to_value_string(['0'])+' where id='+list_to_value_string([db_data['id']])
    do(command)
def predictable_list(mail):
    command='select id from model where owner='+list_to_value_string([mail])+' and statu='+list_to_value_string(['0'])
    return do(command)
    
    
    
    
    
    


import uuid, time
def id_hash_string():
    ms=int(round(time.time() * 1000))
    id = str(ms)+'-'+str(uuid.uuid4())
    return id

def do(command):
    db=connector.connect(host="localhost",database='cvision',user="root",passwd="")
    cursor=db.cursor()
    try:
        cursor.execute(command)
        if cursor.description==None:#not query
            db.commit()
            db.close()
            return 'OK'
        columns = [col[0] for col in cursor.description]
        dictionary = [dict(zip(columns, row)) for row in cursor.fetchall()]
        db.close()
        return dictionary
    except connector.IntegrityError as err:
        #error no reference: https://dev.mysql.com/doc/refman/8.0/en/server-error-reference.html
        db.close()
        return str(err.errno)
def list_to_value_string(list):
    string=''
    for value in list:
        string+='"'+value+'",'
    string=string[:-1]
    return string
def key_equal(dic, exclude_key=[]):
    result=''
    for key in dic:
        if key in exclude_key:
            continue
        result+=key+'="'+dic[key]+'",'
    result=result[:-1]
    return result
    









