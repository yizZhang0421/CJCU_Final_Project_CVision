from hyperopt import Trials, STATUS_OK, tpe
from hyperas import optim
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

def data():
    return 0,0,0,0


#script='from hyperopt import STATUS_OK\nfrom hyperas.distributions import choice, uniform\n\nfrom keras.models import Sequential\nfrom keras.layers import Dense, Dropout, Flatten, Conv2D, MaxPooling2D\nfrom keras.utils import to_categorical\nfrom tensorflow import Graph, Session\n\nimport random, cv2, os, math\nimport numpy as np\nfrom sklearn.model_selection import train_test_split\n\n\ndef model():\n\tgraph=Graph()\n\tsession=Session(graph=graph)\n\twith graph.as_default():\n\t\twith session.as_default():\n\t\t\tmodel = Sequential()\n\t\t\tmodel.add(Conv2D(filters={{choice([8, 16])}}, kernel_size=(2,2), padding=\'same\', input_shape=(100,100,3), activation=\'relu\'))\n\t\t\tmodel.add(Conv2D({{choice([32, 64])}}, (3, 3), activation=\'relu\', padding=\'same\'))\n\t\t\tmodel.add(MaxPooling2D(pool_size=(2,2)))  \n\t\t\tmodel.add(Dropout({{uniform(0, 1)}}))\n\t\t\tmodel.add(Flatten())\n\t\t\tmodel.add(Dense({{choice([128, 512])}}, activation=\'relu\'))\n\t\t\tmodel.add(Dropout({{uniform(0, 1)}}))   \n\t\t\tmodel.add(Dense({{choice([16, 32])}}, activation=\'relu\'))\n\t\t\tmodel.add(Dropout({{uniform(0, 1)}}))\n\t\t\t\n\t\t\tlabels=[LABEL_ID_CONCAT_WITH_COMMA_STRING]\n\t\t\tif len(labels)==2:\n\t\t\t\tmodel.add(Dense(1, activation=\'sigmoid\'))\n\t\t\t\tmodel.compile(loss="binary_crossentropy", optimizer="adam", metrics=[\'accuracy\'])\n\t\t\telse:\n\t\t\t\tmodel.add(Dense(len(labels), activation=\'softmax\'))\n\t\t\t\tmodel.compile(loss=\'categorical_crossentropy\', optimizer=\'adam\', metrics=[\'accuracy\'])\n\t\t\t\n\t\t\tX_train=[]\n\t\t\tX_test=[]\n\t\t\ty_train=[]\n\t\t\ty_test=[]\n\t\t\tfor label in labels:\n\t\t\t\tx=[]\n\t\t\t\ty=[]\n\t\t\t\tfor img in os.listdir(\'labels/\'+label):\n\t\t\t\t\tx.append(\'labels/\'+label+\'/\'+img)\n\t\t\t\t\ty.append(labels.index(label))\n\t\t\t\txy=list(zip(x,y))\n\t\t\t\trandom.shuffle(xy)\n\t\t\t\t(x, y)=zip(*xy)\n\t\t\t\tX_train_, X_test_, y_train_, y_test_ = train_test_split(x, y, test_size=0.2)\n\t\t\t\tX_train+=X_train_\n\t\t\t\tX_test+=X_test_\n\t\t\t\ty_train+=y_train_\n\t\t\t\ty_test+=y_test_\n\t\t\t\n\t\t\tdef gen_train(X_train, y_train):\n\t\t\t\twhile 1:\n\t\t\t\t\txy=list(zip(X_train,y_train))\n\t\t\t\t\trandom.shuffle(xy)\n\t\t\t\t\t(X_train,y_train)=zip(*xy)\n\t\t\t\t\tx=[]\n\t\t\t\t\ty=[]\n\t\t\t\t\tfor img_path, clas in zip(X_train, y_train):\n\t\t\t\t\t\timg=cv2.imread(img_path)\n\t\t\t\t\t\timg=cv2.resize(img,(100,100),interpolation=cv2.INTER_CUBIC)\n\t\t\t\t\t\timg=img/255\n\t\t\t\t\t\tx.append(img)\n\t\t\t\t\t\tif len(labels)>2:\n\t\t\t\t\t\t\ty.append(to_categorical(int(clas), num_classes=len(labels)))\n\t\t\t\t\t\telse:\n\t\t\t\t\t\t\ty.append(int(clas))\n\t\t\t\t\t\tif len(x)==32:\n\t\t\t\t\t\t\tyield (np.array(x), np.array(y))\n\t\t\t\t\t\t\tx=[]\n\t\t\t\t\t\t\ty=[]\n\t\t\t\t\tyield (np.array(x), np.array(y))\n\t\t\tdef gen_val(X_test, y_test):\n\t\t\t\twhile 1:\n\t\t\t\t\txy=list(zip(X_test,y_test))\n\t\t\t\t\trandom.shuffle(xy)\n\t\t\t\t\t(X_test,y_test)=zip(*xy)\n\t\t\t\t\tx=[]\n\t\t\t\t\ty=[]\n\t\t\t\t\tfor img_path, clas in zip(X_test, y_test):\n\t\t\t\t\t\timg=cv2.imread(img_path)\n\t\t\t\t\t\timg=cv2.resize(img,(100,100),interpolation=cv2.INTER_CUBIC)\n\t\t\t\t\t\timg=img/255\n\t\t\t\t\t\tx.append(img)\n\t\t\t\t\t\tif len(labels)>2:\n\t\t\t\t\t\t\ty.append(to_categorical(int(clas), num_classes=len(labels)))\n\t\t\t\t\t\telse:\n\t\t\t\t\t\t\ty.append(int(clas))\n\t\t\t\t\t\tif len(x)==32:\n\t\t\t\t\t\t\tyield (np.array(x), np.array(y))\n\t\t\t\t\t\t\tx=[]\n\t\t\t\t\t\t\ty=[]\n\t\t\t\t\tyield (np.array(x), np.array(y))\n\t\t\t\n\t\t\tmodel.fit_generator(gen_train(X_train, y_train), steps_per_epoch=math.ceil(len(X_train)/32), epochs=3, verbose=1)\n\t\t\tloss=model.evaluate_generator(gen_val(X_test, y_test), steps=math.ceil(len(X_test)/32))[0]\n\t\n\tdel graph\n\tdel session\n\treturn {\'loss\': loss, \'status\': STATUS_OK, \'model\': model}\n'
lines=open('hyper_script_string.py', 'r').readlines()
lines=[i.replace('    ', '\t') for i in lines]
script=""
for i in lines:
    script+=i

best_run, best_model=optim.minimize(
        model=model,
        data=data,
        algo=tpe.suggest,
        max_evals=10,
        trials = Trials(), s=script, verbose=False)
open('hyper_space/MODEL_ID.json', 'w').write(best_model.to_json())