import library as lib

def run(label_id_list, model_id):
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
    hyper=lib.subp.Popen('start /wait cmd /c '+'python hyper_space/'+model_id+'.py', shell=True)
    lib.db_operate.optim_progress(model_id, hyper.pid)
    hyper.wait()
    #hyper=subp.Popen('python hyper_space/'+model_id+'.py', shell=False)
    #hyper=subp.Popen('start /wait cmd /c '+'python hyper_space/'+'test3'+'.py', shell=True)
