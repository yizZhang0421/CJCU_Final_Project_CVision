import cv2, json
import numpy as np

import db_operate

from keras.models import load_model
import keras.backend as K
from tensorflow import Graph, Session
from threading import local
K.clear_session()


'''
graph_bear=Graph()
session_bear=Session(graph=graph_bear)
with graph_bear.as_default():
    with session_bear.as_default():
        model_bear=load_model('models/'+'1557822345285-7CC7D436-469D-4DF6-8B8A-BC2C886287D0'+'.h5')
'''

def predict_start(progress_dict, model_id, image):
    #global model_bear, graph_bear, session_bear
    class_label=json.loads(db_operate.get_class_label(model_id))
    image=cv2.resize(image,(100,100),interpolation=cv2.INTER_CUBIC)
    image=image/255
    image=np.array([image])
    d=local()
    d.result=None
    
    d.graph=Graph()
    d.session=Session(graph=d.graph)
    with d.graph.as_default():
        with d.session.as_default():
            d.model=load_model('models/'+model_id+'.h5')
            if len(class_label)==2:
                d.result=d.model.predict_classes(image)[0][0]
            else:
                d.result=d.model.predict_classes(image)[0]
                
    '''
    if model_id!='1557822345285-7CC7D436-469D-4DF6-8B8A-BC2C886287D0':
        d.graph=Graph()
        d.session=Session(graph=d.graph)
        with d.graph.as_default():
            with d.session.as_default():
                d.model=load_model('models/'+model_id+'.h5')
                if len(class_label)==2:
                    d.result=d.model.predict_classes(image)[0][0]
                else:
                    d.result=d.model.predict_classes(image)[0]
    else:
        with graph_bear.as_default():
            with session_bear.as_default():
                d.result=model_bear.predict_classes(image)[0]
    '''
    
    del d.model
    del d.graph
    del d.session
    
    progress_dict[model_id]=class_label[str(d.result)]