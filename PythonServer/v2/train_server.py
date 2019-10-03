import os, math, json
from threading import local

import db_operate, drive_operate

from keras.models import Sequential
from keras.layers import Dense,Dropout,Flatten,Conv2D,MaxPooling2D
from tensorflow import Graph, Session

def train_start(progress_dict, db_data):
    db_operate.addto_training_list(db_data)
    d=local()
    d.graph=Graph()
    d.session=Session(graph=d.graph)
    d.model = Sequential()
    try:
        with d.graph.as_default():
            with d.session.as_default():
                d.label_dict=json.loads(db_data['class_label'])
                d.total_num_of_images=db_data['num_of_images']
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
                if len(d.label_dict)==2:
                    d.model.add(Dense(1, activation='sigmoid'))
                    d.model.compile(loss="binary_crossentropy", optimizer="adam", metrics=['accuracy'])
                else:
                    d.model.add(Dense(len(d.label_dict), activation='softmax'))
                    d.model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
                d.acc=-999
                d.epoch=0
                d.acc_list=[]
                d.patient_check=False
                while 1:
                    gen=drive_operate.train_data_generator(db_data['owner'], db_data['id'], 32)
                    d.history=d.model.fit_generator(gen, steps_per_epoch=math.ceil(d.total_num_of_images/32), epochs=1, verbose=1)
                    d.epoch+=1
                    progress_dict[db_data['id']]={'epoch':d.epoch,'acc':d.history.history['acc'][0],'loss':d.history.history['loss'][0]}
                    d.acc_list.append(str(d.history.history['acc'][0]))
                    if (d.history.history['acc'][0]-d.acc)>=0: #delta scale
                        d.acc=d.history.history['acc'][0]
                    else:
                        d.patient_check=False
                        for i in range(2):# epoch before break
                            d.history=d.model.fit_generator(drive_operate.train_data_generator(db_data['owner'], db_data['id'], 32), steps_per_epoch=math.ceil(d.total_num_of_images/32), epochs=1, verbose=1)
                            d.epoch+=1
                            progress_dict[db_data['id']]={'epoch':d.epoch,'acc':d.history.history['acc'][0],'loss':d.history.history['loss'][0]}
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
                        
                d.model.save('models/'+db_data['id']+'.h5')
        size=os.path.getsize('models/'+db_data['id']+'.h5')
        size=(size/1024)/1024
        db_data['statu']=0
        db_data['acc']=d.history.history['acc'][0]
        db_data['loss']=d.history.history['loss'][0]
        db_data['size']=size
        db_operate.train_finish(db_data)
    except:
        db_operate.train_stop(db_data)
        print('stop')
    del d.graph
    del d.session
    del d.model
    del d
    del progress_dict[db_data['id']]
    #mail user finish or terminate

import time
def progress_stream(progress_dict, model_id):
    while 1:
        try:
            yield json.dumps(progress_dict[model_id], ensure_ascii=False)+'\n'
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

 