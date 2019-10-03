from hyperopt import Trials, STATUS_OK, tpe
from hyperas import optim
from hyperas.distributions import choice, uniform

from keras.models import Sequential
from keras.layers.core import Dense, Dropout, Activation
from keras.optimizers import RMSprop
 
from keras.datasets import mnist
from keras.utils import np_utils


def data():
    (X_train, y_train), (X_test, y_test) = mnist.load_data()
    X_train = X_train.reshape(60000, 784)
    X_test = X_test.reshape(10000, 784)
    X_train = X_train.astype('float32')
    X_test = X_test.astype('float32')
    X_train /= 255
    X_test /= 255
    nb_classes = 10
    Y_train = np_utils.to_categorical(y_train, nb_classes)
    Y_test = np_utils.to_categorical(y_test, nb_classes)
    return X_train, Y_train, X_test, Y_test



def create_model(X_train, Y_train, X_test, Y_test):
    model = Sequential()
    model.add(Dense({{choice([3, 5])}}, input_shape=(784,)))
    model.add(Activation('relu'))
    model.add(Dropout({{uniform(0, 1)}}))
    model.add(Dense(
            {{choice([10, 12])}}
            ))
    model.add(Activation({{choice(['sigmoid', 'tanh'])}}))
    model.add(Dropout({{uniform(0, 1)}}))
    model.add(Dense(10))
    model.add(Activation('softmax'))
 
    rms = RMSprop()
    model.compile(loss='categorical_crossentropy', optimizer=rms, metrics=['accuracy'])
    
    model.fit(X_train, Y_train,
              batch_size={{choice([20, 40])}},
              nb_epoch=1,
              verbose=2,
              validation_data=(X_test, Y_test))
    score, acc = model.evaluate(X_test, Y_test, verbose=0)
    print('Test accuracy:', acc)
    return {'loss': -acc, 'status': STATUS_OK, 'model': model}



X_train, Y_train, X_test, Y_test = data()

best_run, best_model = optim.minimize(model=create_model,
                                      data=data,
                                      algo=tpe.suggest,
                                      max_evals=10,
                                      trials=Trials())

print("Evalutation of best performing model:")
print(best_model.evaluate(X_test, Y_test))
print(best_run)
