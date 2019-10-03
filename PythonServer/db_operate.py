from mysql import connector
import os, shutil

'''
android need check server access is OK, maybe at begining or anytime.
synchronize local file system, so if you change db item, local file would be changed too.
'''
def user_list():
    command='select email from user'
    result=do(command)
    return result

def check_user_exist(mail, name):
    command='select * from user where email='+'"'+mail+'"'
    result=do(command)
    if len(result)==0:
        command='insert into user value('+list_to_value_string([mail,name])+')'
        result=do(command)
        os.makedirs(mail)
    return result

def create_model(owner_email, name):
    id=id_hash_string()
    command='insert into model(`id`,`owner_email`,`name`) value('+list_to_value_string([id,owner_email,name])+')'
    result=do(command)
    command='insert into model_users value('+list_to_value_string([id,owner_email])+')'
    do(command)
    if result=='OK':
        os.makedirs(owner_email+'/'+id)
    return result
    
        
def delete_model(mail, id):
    command='delete from model where id='+list_to_value_string([id])
    result=do(command)
    shutil.rmtree(mail+'/'+id)
    return result
    
def rename_model(id, newname):
    command='update model set name='+list_to_value_string([newname])+' where id='+list_to_value_string([id])
    result=do(command)
    return result

def self_model_list(mail):
    command='select id,name,acc,loss from model where owner_email='+list_to_value_string([mail])+' AND trainStatu IN ('+list_to_value_string(['0','1'])+')'
    result=do(command)
    return result

def predict_model_list(user):
    command='select id,name,owner_email from model,model_users where id=model_id and trainStatu=1 and user='+list_to_value_string([user])
    result=do(command)
    return result

def training_model_list(mail):
    command='select id,name from model where owner_email='+list_to_value_string([mail])+' AND trainStatu='+list_to_value_string(['2'])
    result=do(command)
    return result

def add_model_user(id,user):
    command='insert into model_users value ('+list_to_value_string([id,user])+')'
    result=do(command)
    return result
    
def model_size(id):
    command='select size from model where id='+list_to_value_string([id])
    result=do(command)
    return result

def create_label(owner_email, name, model_id):
    id=id_hash_string()
    command='insert into label value('+list_to_value_string([id,name,model_id])+')'
    result=do(command)
    command='insert into label_group value('+list_to_value_string([id,id])+')'
    do(command)
    if result=='OK':
        os.makedirs(owner_email+'/'+model_id+'/'+id)
    return result

def delete_label(owner_email, model_id, id):
    command='delete from label where id='+list_to_value_string([id])
    result=do(command)
    if result=='OK':
        shutil.rmtree(owner_email+'/'+model_id+'/'+id)
    return result

def rename_label(id, newname):
    command='update label set name='+list_to_value_string([newname])+' where id='+list_to_value_string([id])
    result=do(command)
    return result

def model_label_list(model_id):
    command='select id,name from label where model_id='+list_to_value_string([model_id])
    result=do(command)
    return result

def all_label_list(mail):
    command='select model.name as modelname,model_id,label.name as labelname,label.id from model,label where model.id=label.model_id and owner_email='+list_to_value_string([mail])
    result=do(command)
    return result

def import_label(import_label_id,into_label_id):
    command='insert into label_group value('+list_to_value_string([into_label_id,import_label_id])+')'
    result=do(command)
    return result

def label_images(label_id):
    command='SELECT model_id,add_label from `label`,`label_group` where `label_group`.`add_label`=`label`.`id` and label_id='+list_to_value_string([label_id])
    result=do(command)
    return result

def change_model_statu(model_id, statu):
    command='update model set trainStatu='+list_to_value_string([statu])+' where id='+list_to_value_string([model_id])
    result=do(command)
    return result

def change_model_detail(model_id, size, acc, loss):
    command='update model set size='+list_to_value_string([size])+' , acc='+list_to_value_string([acc])+' , loss='+list_to_value_string([loss])+' where id='+list_to_value_string([model_id])
    result=do(command)
    return result

def update_model_label_class(model, dict_string, acc_list):
    command='select model_id from label_class where model_id='+list_to_value_string([model])
    result=do(command)
    if len(result)==0:
        dict_string=dict_string.replace('"','\\"')
        acc_list=acc_list.replace('"','\\"')
        command='insert into label_class value('+list_to_value_string([model, dict_string, acc_list])+')'
        result=do(command)
    else:
        dict_string=dict_string.replace('"','\\"')
        acc_list=acc_list.replace('"','\\"')
        command='update label_class set dict_string='+list_to_value_string([dict_string])+' , acc_list='+list_to_value_string([acc_list])+' where model_id='+list_to_value_string([model])
        result=do(command)
    return result

def is_model_trained(model):
    command='select model_id from label_class where model_id='+list_to_value_string([model])
    result=do(command)
    if len(result)==0:
        return False
    else:
        return True

def label_class(model):
    command='select dict_string from label_class where model_id='+list_to_value_string([model])
    result=do(command)
    return result








def filesys_db_sync():
    mail_list=user_list()
    for user in mail_list:
        os.makedirs(user['email'])
        model_list=self_model_list(user['email'])
        for model in model_list:
            os.makedirs(user['email']+'/'+model['id'])
            label_list=model_label_list(model['id'])
            for label in label_list:
                os.makedirs(user['email']+'/'+model['id']+'/'+label['id'])
    return 'OK'

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
        string+='"'+value+'",'
    string=string[:-1]
    return string
    









