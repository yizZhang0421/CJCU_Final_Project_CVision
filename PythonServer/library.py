from flask import Flask, request, send_file, Response, stream_with_context
from flask_cors import CORS
import cv2, base64, json, io, urllib, os, re, copy, requests, time, math, random, shutil, uuid, psutil
from shutil import copyfile
import numpy as np

from google.oauth2 import id_token
from google.auth.transport import requests as google_requests

from multiprocessing import Process, Manager
import subprocess as subp
from threading import local, Thread

from keras.models import model_from_json, load_model
from keras.utils import to_categorical
import keras.backend as K
import tensorflow as tf
from tensorflow import Graph, Session

from sklearn.model_selection import train_test_split

from mysql import connector

import code_table
from train_server import train_start, progress_stream
from prediction_server import predict_start
from create_hyper_py import run as hyper_run
import db_operate


def dl_library():
    return {
            'model_from_json': model_from_json,
            'load_model': load_model,
            'to_categorical': to_categorical,
            'K': K,
            'tf': tf,
            'Graph': Graph,
            'Session': Session
            }