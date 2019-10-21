import os, math, random, cv2, json
import numpy as np
from threading import local

import db_operate

from keras.models import model_from_json
from keras.utils import to_categorical
from tensorflow import Graph, Session

from sklearn.model_selection import train_test_split

import create_hyper_py

def train_start(progress_dict, hyper_thread_dict, model_id, labels):
    d=local()
    d.graph=Graph()
    d.session=Session(graph=d.graph)
    
    progress_dict[model_id]={'epoch':'Building', 'acc':None ,'loss':None}
    
    create_hyper_py.run([i['id'] for i in labels], model_id, hyper_thread_dict)
    
    progress_dict[model_id]={'epoch':'Training', 'acc':None ,'loss':None}    
    try:
        if hyper_thread_dict[model_id]==-1:
            raise Exception('stop train')
        with d.graph.as_default():
            with d.session.as_default():
                d.model = model_from_json(open('hyper_space/'+model_id+'.json', 'r').read())
                if len(labels)==2:
                    d.model.compile(loss="binary_crossentropy", optimizer="adam", metrics=['accuracy'])
                else:
                    d.model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
                os.remove('hyper_space/'+model_id+'.py')
                os.remove('hyper_space/'+model_id+'.json')
                
                X_train=[]
                X_test=[]
                y_train=[]
                y_test=[]
                for label in labels:
                    x=[]
                    y=[]
                    for img in os.listdir('labels/'+label['id']):
                        x.append('labels/'+label['id']+'/'+img)
                        y.append(labels.index(label))
                    xy=list(zip(x,y))
                    random.shuffle(xy)
                    (x, y)=zip(*xy)
                    X_train_, X_test_, y_train_, y_test_ = train_test_split(x, y, test_size=0.2)
                    X_train+=X_train_
                    X_test+=X_test_
                    y_train+=y_train_
                    y_test+=y_test_
                
                def gen_train(X_train, y_train):
                    while 1:
                        xy=list(zip(X_train,y_train))
                        random.shuffle(xy)
                        (X_train,y_train)=zip(*xy)
                        x=[]
                        y=[]
                        for img_path, clas in zip(X_train, y_train):
                            img=cv2.imread(img_path)
                            img=cv2.resize(img,(100,100),interpolation=cv2.INTER_CUBIC)
                            img=img/255
                            x.append(img)
                            if len(labels)>2:
                                y.append(to_categorical(int(clas), num_classes=len(labels)))
                            else:
                                y.append(int(clas))
                            if len(x)==32:
                                yield (np.array(x), np.array(y))
                                x=[]
                                y=[]
                        yield (np.array(x), np.array(y))
                def gen_val(X_test, y_test):
                    while 1:
                        xy=list(zip(X_test,y_test))
                        random.shuffle(xy)
                        (X_test,y_test)=zip(*xy)
                        x=[]
                        y=[]
                        for img_path, clas in zip(X_test, y_test):
                            img=cv2.imread(img_path)
                            img=cv2.resize(img,(100,100),interpolation=cv2.INTER_CUBIC)
                            img=img/255
                            x.append(img)
                            if len(labels)>2:
                                y.append(to_categorical(int(clas), num_classes=len(labels)))
                            else:
                                y.append(int(clas))
                            if len(x)==32:
                                yield (np.array(x), np.array(y))
                                x=[]
                                y=[]
                        yield (np.array(x), np.array(y))
                
                d.acc=-999
                d.epoch=0
                d.patient=3
                #==========================================================#
                while 1:
                    d.history_train=d.model.fit_generator(gen_train(X_train, y_train), steps_per_epoch=math.ceil(len(X_train)/32), epochs=1, verbose=1)
                    d.history_test=d.model.evaluate_generator(gen_val(X_test, y_test), steps=math.ceil(len(X_test)/32))
                    d.epoch+=1
                    progress_dict[model_id]={'epoch':d.epoch,'acc':d.history_test[1],'loss':d.history_test[0]}
                    if d.history_test[1]>d.acc:
                        d.acc=d.history_test[1]
                        continue
                    else:
                        is_update=False
                        for i in range(d.patient):# train patient times 
                            d.history_train=d.model.fit_generator(gen_train(X_train, y_train), steps_per_epoch=math.ceil(len(X_train)/32), epochs=1, verbose=1)
                            d.history_test=d.model.evaluate_generator(gen_val(X_test, y_test), steps=math.ceil(len(X_test)/32))
                            d.epoch+=1
                            progress_dict[model_id]={'epoch':d.epoch,'acc':d.history_test[1],'loss':d.history_test[0]}
                            if d.history_test[1]>d.acc:
                                is_update=True
                                d.acc=d.history_test[1]
                                break
                        if is_update==False:
                            break
                #==========================================================#
                d.model.save('models/'+model_id+'.h5')
                
        #finish fill data to database
        size=os.path.getsize('models/'+model_id+'.h5')
        size=(size/1024)/1024
        class_label=json.dumps({labels.index(label):label['name'] for label in labels}, ensure_ascii=False).replace('"', '\\"')
        db_operate.fill_model_details(model_id, size, d.history_test[1], d.history_test[0], class_label)
        db_operate.train_model_finish(model_id)
        del d.model
    except:
        db_operate.train_model_stop(model_id)
        try:
            os.remove('hyper_space/'+model_id+'.py')
        except:
            pass
        try:
            os.remove('hyper_space/'+model_id+'.json')
        except:
            pass
        del d.model
    del d.graph
    del d.session
    del d
    del progress_dict[model_id]
    #mail user finish or terminate

import time
def progress_stream(progress_dict, model):
    while 1:
        try:
            yield json.dumps(progress_dict[model], ensure_ascii=False)+'\n'
        except:
            yield 'finish\n'
            break
        time.sleep(0.5)


 