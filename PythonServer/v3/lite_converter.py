from flask import Flask, request, send_file
app = Flask(__name__)
import uuid, io, os
import tensorflow as tf

@app.route('/',methods=['POST'])
def root():
    filename=str(uuid.uuid4())
    extension='h5'
    with open(filename+'.'+extension, 'wb') as f:
        f.write(request.data)
    converter = tf.lite.TFLiteConverter.from_keras_model_file(filename+'.'+extension)
    tflite_model = converter.convert()
    tflite_model=io.BytesIO(tflite_model)
    os.remove(filename+'.'+extension)
    return send_file(tflite_model, mimetype='application/octet-stream')
@app.errorhandler(404)
def page_not_found(e):
    return 'not found'
    
if __name__ == '__main__':
    app.run(host='0.0.0.0',port=80)
