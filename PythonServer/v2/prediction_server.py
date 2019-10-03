import cv2
import numpy as np

from keras.models import load_model
import keras.backend as K
from tensorflow import Graph, Session
from threading import local
K.clear_session()

def predict_start(model_path, img, dict):
    img=cv2.resize(img,(100,100),interpolation=cv2.INTER_CUBIC)
    img=img/255
    img=np.array([img])
    d=local()
    d.result=None
    d.graph=Graph()
    d.session=Session(graph=d.graph)
    with d.graph.as_default():
        with d.session.as_default():
            d.model=load_model(model_path+'model.h5')
            if len(dict)==2:
                d.result=d.model.predict_classes(img)[0][0]
            else:
                d.result=d.model.predict_classes(img)[0]
    del d.graph
    del d.session
    del d.model
    return d.result
    
