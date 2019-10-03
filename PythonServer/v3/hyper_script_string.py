from hyperopt import STATUS_OK
from hyperas.distributions import choice, uniform

from keras.models import Sequential
from keras.layers import Dense, Dropout, Flatten, Conv2D, MaxPooling2D
from keras.utils import to_categorical
from tensorflow import Graph, Session

import random, cv2, os, math
import numpy as np
from sklearn.model_selection import train_test_split


def model():
    graph=Graph()
    session=Session(graph=graph)
    with graph.as_default():
        with session.as_default():
            model = Sequential()
            model.add(Conv2D(filters={{choice([8])}}, kernel_size=(2,2), padding='same', input_shape=(100,100,3), activation='relu'))
            model.add(Conv2D({{choice([16])}}, (3, 3), activation='relu', padding='same'))
            model.add(MaxPooling2D(pool_size=(2,2)))  
            model.add(Dropout({{uniform(0, 1)}}))
            model.add(Flatten())
            model.add(Dense({{choice([128])}}, activation='relu'))
            model.add(Dropout({{uniform(0, 1)}}))   
            model.add(Dense({{choice([16, 32])}}, activation='relu'))
            model.add(Dropout({{uniform(0, 1)}}))
            
            labels=[LABEL_ID_CONCAT_WITH_COMMA_STRING]
            if len(labels)==2:
                model.add(Dense(1, activation='sigmoid'))
                model.compile(loss="binary_crossentropy", optimizer="adam", metrics=['accuracy'])
            else:
                model.add(Dense(len(labels), activation='softmax'))
                model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
            
            X_train=[]
            X_test=[]
            y_train=[]
            y_test=[]
            for label in labels:
                x=[]
                y=[]
                for img in os.listdir('labels/'+label):
                    x.append('labels/'+label+'/'+img)
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
            
            model.fit_generator(gen_train(X_train, y_train), steps_per_epoch=math.ceil(len(X_train)/32), epochs=3, verbose=1)
            loss=model.evaluate_generator(gen_val(X_test, y_test), steps=math.ceil(len(X_test)/32))[0]
    
    del graph
    del session
    return {'loss': loss, 'status': STATUS_OK, 'model': model}
