import subprocess as subp
import time, psutil

def kill(proc_pid):
    process = psutil.Process(proc_pid)
    for proc in process.children(recursive=True):
        proc.kill()
    process.kill()
def run(label_id_list, model_id, hyper_thread_dict):
    lines=open('create_hyper_py_source_string.py', 'r').readlines()
    lines=[i.replace('    ', '\t') for i in lines]
    script=""
    for i in lines:
        script+=i
    label=''
    for i in label_id_list:
        label+="'"+i+"'"+','
    label=label[:-1]
    script=script.replace('LABEL_ID_CONCAT_WITH_COMMA_STRING',label, 1)
    label=''
    for i in label_id_list:
        label+="\\'"+i+"\\'"+','
    label=label[:-1]
    script=script.replace('LABEL_ID_CONCAT_WITH_COMMA_STRING_2',label)
    script=script.replace('MODEL_ID',model_id)
    open('hyper_space/'+model_id+'.py', 'w').write(script)
    hyper=subp.Popen('start /wait cmd /c '+'python hyper_space/'+model_id+'.py', shell=True)
    while 1:
        if hyper_thread_dict[model_id]==0:
            kill(hyper.pid)
            hyper_thread_dict[model_id]=-1
            break
        if hyper.poll() != None:# is alive
            break
        time.sleep(0.5)