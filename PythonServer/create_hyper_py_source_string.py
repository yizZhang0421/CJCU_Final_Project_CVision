from hyperas import optim
from hyperopt import Trials, STATUS_OK, tpe
from hyperas.distributions import choice, uniform

from keras.models import Sequential
from keras.layers import Dense, Dropout, Flatten, Conv2D, MaxPooling2D
from keras.utils import to_categorical
from tensorflow import Graph, Session
import tensorflow as tf

import random, cv2, os, math
import numpy as np
from sklearn.model_selection import train_test_split

from multiprocessing import Process
from threading import local

def model():
    d=local()
    d.config = tf.ConfigProto(
        gpu_options = tf.GPUOptions(per_process_gpu_memory_fraction=0.1),
        # device_count = {'GPU': 1}
    )
    d.config.gpu_options.allow_growth = True
    d.graph=Graph()
    #session=Session(graph=graph, config=config)
    d.session=Session(graph=d.graph, config=d.config)
    with d.graph.as_default():
        with d.session.as_default():
            d.model = Sequential()
            d.model.add(Conv2D(filters={{choice([8, 16])}}, kernel_size=(2,2), padding='same', input_shape=(100,100,3), activation='relu'))
            d.model.add(Conv2D({{choice([8, 16])}}, (3, 3), activation='relu', padding='same'))
            d.model.add(MaxPooling2D(pool_size=(2,2)))  
            d.model.add(Dropout({{uniform(0, 1)}}))
            d.model.add(Flatten())
            d.model.add(Dense({{choice([16, 32, 64])}}, activation='relu'))
            d.model.add(Dropout({{uniform(0, 1)}}))
            d.model.add(Dense({{choice([16, 32, 64])}}, activation='relu'))
            d.model.add(Dropout({{uniform(0, 1)}}))
            
            d.labels=[LABEL_ID_CONCAT_WITH_COMMA_STRING]
            if len(d.labels)==2:
                d.model.add(Dense(1, activation='sigmoid'))
                d.model.compile(loss="binary_crossentropy", optimizer="adam", metrics=['accuracy'])
            else:
                d.model.add(Dense(len(d.labels), activation='softmax'))
                d.model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
            
            d.X_train=[]
            d.X_test=[]
            d.y_train=[]
            d.y_test=[]
            for d.label in d.labels:
                d.x=[]
                d.y=[]
                for d.img in os.listdir('labels/'+d.label):
                    d.x.append('labels/'+d.label+'/'+d.img)
                    d.y.append(d.labels.index(d.label))
                d.xy=list(zip(d.x,d.y))
                random.shuffle(d.xy)
                (d.x, d.y)=zip(*d.xy)
                d.X_train_, d.X_test_, d.y_train_, d.y_test_ = train_test_split(d.x, d.y, test_size=0.2)
                d.X_train+=d.X_train_
                d.X_test+=d.X_test_
                d.y_train+=d.y_train_
                d.y_test+=d.y_test_
            
            def gen_train(X_train, y_train, class_num):
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
                        if class_num>2:
                            y.append(to_categorical(int(clas), num_classes=class_num))
                        else:
                            y.append(int(clas))
                        if len(x)==32:
                            yield (np.array(x), np.array(y))
                            x=[]
                            y=[]
                    if len(x)!=0:
                        yield (np.array(x), np.array(y))
            def gen_val(X_test, y_test, class_num):
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
                        if class_num>1+1:
                            y.append(to_categorical(int(clas), num_classes=class_num))
                        else:
                            y.append(int(clas))
                        if len(x)==32:
                            yield (np.array(x), np.array(y))
                            x=[]
                            y=[]
                    if len(x)!=0:
                        yield (np.array(x), np.array(y))
            
            d.model.fit_generator(gen_train(d.X_train, d.y_train, len(d.labels)), steps_per_epoch=math.ceil(len(d.X_train)/32), epochs=3, verbose=1)
            loss=d.model.evaluate_generator(gen_val(d.X_test, d.y_test, len(d.labels)), steps=math.ceil(len(d.X_test)/32))[0]
    
    del d.graph
    del d.session
    return {'loss': loss, 'status': STATUS_OK, 'model': d.model}
###hyper_script_string###
def data():
    return 0,0,0,0

lines=open('create_hyper_py_source_string.py', 'r').readlines()
lines=[i.replace('    ', '\t') for i in lines]
script=""
for i in lines:
    script+=i
script=script[:script.index('###hyper_script_string###')]

best_run, best_model=optim.minimize(
        model=model,
        data=data,
        algo=tpe.suggest,
        max_evals=10,
        trials = Trials(), s=script, verbose=False)
open('hyper_space/MODEL_ID.json', 'w').write(best_model.to_json())

'''
if __name__ == '__main__':
    p=Process(target=run)
    p.start()
    p.join()
'''
