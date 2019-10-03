import os, math, random, cv2, json
import numpy as np
from threading import local

import db_operate

from keras.models import Sequential
from keras.layers import Dense,Dropout,Flatten,Conv2D,MaxPooling2D
from keras.utils import to_categorical
from tensorflow import Graph, Session

def gen(mail, model, label_dirs, batch_size):
    x=[]
    y=[]
    for label in label_dirs:
        for img in os.listdir(mail+'/'+model+'/'+label):
            x.append(mail+'/'+model+'/'+label+'/'+img)
            y.append(label_dirs.index(label))
    xy=list(zip(x,y))
    random.shuffle(xy)
    (x, y)=zip(*xy)
    
    x_=[]
    y_=[]
    for img_path, clas in zip(x, y):
        img=cv2.imread(img_path)
        img=cv2.resize(img,(100,100),interpolation=cv2.INTER_CUBIC)
        img=img/255
        x_.append(img)
        if len(label_dirs)>2:
            y_.append(to_categorical(int(clas), num_classes=len(label_dirs)))
        else:
            y_.append(int(clas))
        if len(x_)==batch_size:
            yield (np.array(x_), np.array(y_))
            x_=[]
            y_=[]
    yield (np.array(x_), np.array(y_))

def train_start(progress_dict, mail, model, batch_size, patient):
    d=local()
    d.graph=Graph()
    d.session=Session(graph=d.graph)
    d.model = Sequential()
    try:
        with d.graph.as_default():
            with d.session.as_default():
                d.label_dirs=db_operate.model_label_list(model)
                d.label_dirs=[d.label_info['id'] for d.label_info in d.label_dirs]
                d.total_num_of_images=0
                for d.label in d.label_dirs:
                    d.total_num_of_images+=len(os.listdir(mail+'/'+model+'/'+d.label))
                d.model.add(Conv2D(filters=8,
                                 kernel_size=(3,3),
                                 padding='same',
                                 input_shape=(100,100,3),
                                 activation='relu'))
                d.model.add(MaxPooling2D(pool_size=(2,2)))
                d.model.add(Conv2D(filters=16,
                                 kernel_size=(3,3),
                                 padding='same',
                                 activation='relu'))
                d.model.add(Conv2D(filters=16,
                                 kernel_size=(3,3),
                                 padding='same',
                                 activation='relu'))
                d.model.add(MaxPooling2D(pool_size=(2,2)))
                d.model.add(Flatten())
                d.model.add(Dense(2048, activation='relu'))
                d.model.add(Dense(512, activation='relu'))
                d.model.add(Dropout(0.25))
                d.model.add(Dense(128, activation='relu'))
                d.model.add(Dropout(0.25))
                if len(d.label_dirs)==2:
                    d.model.add(Dense(1, activation='sigmoid'))
                    d.model.compile(loss="binary_crossentropy", optimizer="adam", metrics=['accuracy'])
                else:
                    d.model.add(Dense(len(d.label_dirs), activation='softmax'))
                    d.model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
                d.acc=-999
                d.epoch=0
                d.acc_list=[]
                d.patient_check=False
                while 1:
                    d.history=d.model.fit_generator(gen(mail, model, d.label_dirs, batch_size), steps_per_epoch=math.ceil(d.total_num_of_images/batch_size), epochs=1, verbose=0)
                    d.epoch+=1
                    progress_dict[model]={'epoch':d.epoch,'acc':d.history.history['acc'][0],'loss':d.history.history['loss'][0]}
                    d.acc_list.append(str(d.history.history['acc'][0]))
                    if (d.history.history['acc'][0]-d.acc)>=0: #delta scale
                        d.acc=d.history.history['acc'][0]
                    else:
                        d.patient_check=False
                        for i in range(patient):# epoch before break
                            d.history=d.model.fit_generator(gen(mail, model, d.label_dirs, batch_size), steps_per_epoch=math.ceil(d.total_num_of_images/batch_size), epochs=1, verbose=0)
                            d.epoch+=1
                            progress_dict[model]={'epoch':d.epoch,'acc':d.history.history['acc'][0],'loss':d.history.history['loss'][0]}
                            d.acc_list.append(str(d.history.history['acc'][0]))
                            if (d.history.history['acc'][0]-d.acc)>0: #delta scale
                                d.acc=d.history.history['acc'][0]
                                d.patient_check=True
                                break
                        if d.patient_check:
                            d.patient_check=False
                            continue
                        else:
                            break
                        
                d.model.save(mail+'/'+model+'/'+'model.h5')
        d.label_class={}
        d.label_dirs=db_operate.model_label_list(model)
        d.label_dirs=[d.label_info['name'] for d.label_info in d.label_dirs]
        for d.label in d.label_dirs:
            d.label_class[str(d.label_dirs.index(d.label))]=d.label
        db_operate.update_model_label_class(model, json.dumps(d.label_class, ensure_ascii=False), json.dumps(d.acc_list, ensure_ascii=False))
        db_operate.change_model_statu(model, '1')
        size=os.path.getsize(mail+'/'+model+'/'+'model.h5')
        size=(size/1024)/1024
        db_operate.change_model_detail(model, str(size), str(d.history.history['acc'][0]), str(d.history.history['loss'][0]))
    except:
        if db_operate.is_model_trained(model):
            db_operate.change_model_statu(model, '1')
        else:
            db_operate.change_model_statu(model, '0')
        print('stop')
    del d.graph
    del d.session
    del d.model
    del d
    del progress_dict[model]
    #mail user finish or terminate

import time
def progress_stream(progress_dict, model):
    while 1:
        try:
            yield json.dumps(progress_dict[model], ensure_ascii=False)+'\n'
        except:
            yield 'finish\n'
        time.sleep(0.5)



'''
from multiprocessing import Process
if __name__ == '__main__':
    model_list=db_operate.self_model_list('z58774556@gmail.com')
    mail='z58774556@gmail.com'
    model='1547914894173-334481f0-5657-400d-ac2f-334a52138306'
    batch_size=32
    patient=0
    a=Process(target=train, args=(mail, model, batch_size, patient))
    a.start()
    a.is_alive()
'''

 