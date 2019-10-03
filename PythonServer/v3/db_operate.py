from mysql import connector
import os, shutil

import code_table

def register(mail, name):
    command='select user.key from user where email='+list_to_value_string([mail])
    result=do(command)
    if len(result)==0:
        key=id_hash_string()
        command='insert into user values('+list_to_value_string([key, mail, name])+')'
        do(command)
        command='select user.key from user where email='+list_to_value_string([mail])
        result=do(command)
        return result
    else:
        return result
    
def get_key(mail):
    command='select user.key from user where email='+list_to_value_string([mail])
    result=do(command)
    return result

def is_key_exist(key):
    command='select * from user where user.key='+list_to_value_string([key])
    result=do(command)
    return result

def create_model(key, name):
    id=id_hash_string()
    command='insert into model(`id`, `key`, `name`) values('+list_to_value_string([id, key, name])+')'
    result=do(command)
    return result

def model_list(key):
    command='select id, name, acc, loss from model where model.key='+list_to_value_string([key])+' and train_status not in (1,2)'
    result=do(command)
    return result

def get_model_info(model_id):
    command='SELECT `model`.`name`, `model`.`size`, `model`.`acc`, `model`.`loss`, `model`.`class_label`, `user`.`email` FROM `model`, `user` WHERE `model`.`key` = `user`.`key` AND `model`.`id` = '+list_to_value_string([model_id])
    result=do(command)
    return result

def get_model_key(model_id):
    command='select model.key from model where id='+list_to_value_string([model_id])
    result=do(command)
    if len(result)==0:
        return ''
    return result[0]['key']
        
def delete_model(model_id):
    labelid_name=label_list(model_id)
    command='delete from model where id='+list_to_value_string([model_id])
    result=do(command)
    try:
        os.remove('models/'+model_id+'.h5')
    except:
        pass
    for label in labelid_name:
        shutil.rmtree('labels/'+label['id'])
    return result

def is_model_exist(model_id):
    command='select * from model where model.id='+list_to_value_string([model_id])
    result=do(command)
    return result
    
def rename_model(id, newname):
    pass

def add_model_user(id,user):
    pass
    
def model_size(id):
    pass

def create_label(name, model_id):
    id=id_hash_string()
    command='insert into label values('+list_to_value_string([id, name, model_id])+')'
    result=do(command)
    os.mkdir('labels/'+id)
    return result

def rename_label(id, newname):
    pass

def label_list(model_id):
    command='select id, name from label where model_id='+list_to_value_string([model_id])
    result=do(command)
    return result

def delete_label(label_id):
    command='delete from label where id='+list_to_value_string([label_id])
    result=do(command)
    shutil.rmtree('labels/'+label_id)
    return result

def is_label_exist(label_id):
    command='select * from label where id='+list_to_value_string([label_id])
    result=do(command)
    return result

def get_label_key(label_id):
    command='select model.key from model, label where label.model_id=model.id and label.id='+list_to_value_string([label_id])
    result=do(command)
    if len(result)==0:
        return ''
    return result[0]['key']

def get_label_model_id(label_id):
    command='select model_id from label where id='+list_to_value_string([label_id])
    result=do(command)
    if len(result)==0:
        return ''
    return result[0]['model_id']

def progress_list(key):
    command='select id, name from model where model.key='+list_to_value_string([key])+' and train_status in (1,2)'
    result=do(command)
    return result

def predictable_model_list(key):
    command='SELECT `model`.`id`, `model`.`name`, `user`.`email` FROM `model`, `user` WHERE `model`.`key` = `user`.`key` AND `model`.`key` = '+list_to_value_string([key])+' AND train_status = '+list_to_value_string([code_table.model_status_predictable])
    result=do(command)
#    command='SELECT `model`.`id`, `model`.`name`, `user`.`email` FROM `model_users`, `model`, `user` WHERE `model_users`.`model_id` = `model`.`id` AND `model`.`key` = `user`.`key` AND `model_users`.`key` = '+list_to_value_string([key])+' AND `model`.`train_status` = '+list_to_value_string([code_table.model_status_predictable])
#    result+=do(command)
    return result


def train_model(model_id):
    current_model_status=get_model_status(model_id)
    if current_model_status==code_table.model_status_None:
        command='update model set train_status='+list_to_value_string([code_table.model_status_first_train])+' where id='+list_to_value_string([model_id])
    elif current_model_status==code_table.model_status_predictable:
        command='update model set train_status='+list_to_value_string([code_table.model_status_improve])+' where id='+list_to_value_string([model_id])
    result=do(command)
    return result
def train_model_stop(model_id):
    current_model_status=get_model_status(model_id)
    if current_model_status==code_table.model_status_first_train:
        command='update model set train_status='+list_to_value_string([code_table.model_status_None])+' where id='+list_to_value_string([model_id])
    elif current_model_status==code_table.model_status_improve:
        command='update model set train_status='+list_to_value_string([code_table.model_status_predictable])+' where id='+list_to_value_string([model_id])
    result=do(command)
    return result
def train_model_finish(model_id):
    command='update model set train_status='+list_to_value_string([code_table.model_status_predictable])+' where id='+list_to_value_string([model_id])
    result=do(command)
    return result
def get_model_status(model_id):
    command='select train_status from model where id='+list_to_value_string([model_id])
    result=do(command)
    return result[0]['train_status']
def fill_model_details(model_id, size, acc, loss, class_label):
    command='update model set size='+list_to_value_string([size])+', acc='+list_to_value_string([acc])+', loss='+list_to_value_string([loss])+', class_label='+list_to_value_string([class_label])+' where id='+list_to_value_string([model_id])
    result=do(command)
    return result

def get_class_label(model_id):
    command='select class_label from model where id='+list_to_value_string([model_id])
    result=do(command)
    return result[0]['class_label']



import uuid, time
def id_hash_string():
    ms=int(round(time.time() * 1000))
    id = str(ms)+'-'+str(uuid.uuid4())
    return id.upper()
def do(command):
    db=connector.connect(host="localhost",database='cvision',user="root",passwd="")
    cursor=db.cursor()
    try:
        cursor.execute(command)
        if cursor.description==None:#not query
            db.commit()
            db.close()
            return 'ok'
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
        string+='"'+str(value)+'",'
    string=string[:-1]
    return string

    









