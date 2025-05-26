# encoding:utf-8
import shutil

import flask, json
import os
import cv2
import requests
import uuid
from urllib.request import urlretrieve

from sympy import content

from ultralytics import YOLO
from QtFusion.path import abs_path
import tempfile
from minio import Minio


minioClient = Minio('nas.com:9001',
                  access_key='O84xYuJLPOfowUzdrk3z',
                  secret_key='8DTax5rnvDaiWiojowiEhYRgbdkrgNjLj8Xbz0d6',
                  secure=False)
bucket_name = 'automotive-defect'
url_prefix = 'http://nas.com:9001/' + bucket_name + '/'

# 实例化api，把当前这个python文件当作一个服务，__name__代表当前这个python文件
api = flask.Flask(__name__)

path = "imgTmpDir"
res_path = "resTmpDir"
model_path = "modelDir"
models = {'yolov8n': None}

def save_file_tmp(img_url=None, buffer=None, base_dir=path):
    with tempfile.NamedTemporaryFile(dir=base_dir, suffix=".jpg", delete=False) as temp_file:
        if img_url:
            response = requests.get(img_url, timeout=10)
            response.raise_for_status()
            temp_file.write(response.content)
        if buffer is not None:
            temp_file.write(buffer.tobytes())
        temp_path = temp_file.name

    return temp_path

def save_file_minio(path):
    unique_id = uuid.uuid4()
    found = minioClient.bucket_exists(bucket_name)
    if not found:
        minioClient.make_bucket(bucket_name)
        print("Created bucket", bucket_name)
    else:
        print("Bucket", bucket_name, "already exists")

    filename = str(uuid.uuid4()) + os.path.basename(path)

    minioClient.fput_object(
        bucket_name, filename, path,
    )
    print(
        path, "successfully uploaded as object",
        filename, "to bucket", bucket_name,
    )
    return url_prefix + filename

# post入参访问方式二：josn格式参数
@api.route('/defect', methods=['post'])
def defect():
    # from-data格式参数
    img_url = flask.request.json.get('img_url')
    model_url = flask.request.json.get('model_url')
    if not model_url:
        des_model = 'yolov8n'
    print('模型：' + model_url)
    # 保存模型
    model_file = None
    with tempfile.NamedTemporaryFile(dir=model_path, suffix=".pt", delete=False) as temp_file:
        response = requests.get(model_url, timeout=10)
        response.raise_for_status()
        temp_file.write(response.content)
        model_file = temp_file.name


    if img_url:
        temp_path = save_file_tmp(img_url=img_url)
        # urlretrieve(img_url, path + "/temp.jpg")
        if model_url not in models or models[model_url] is None:
            models[model_url] = YOLO(model=model_file, task='segment')

        model =models[model_url]
        results = model.predict(temp_path)
        annotated_img = results[0].plot()
        success, buffer = cv2.imencode(".jpg", annotated_img)

        if success:
            # 保存二进制流到本地文件
            res_img = save_file_tmp(buffer=buffer, base_dir=res_path)
            detections = []
            for box in results[0].boxes:
                detections.append({
                    "class": model.names[int(box.cls)],
                    "confidence": float(box.conf),
                    "bbox": box.xyxy[0].tolist()
                })

            url = save_file_minio(res_img)
            return json.dumps({'msg': '识别成功', 'code': 0, 'data': {'url': url, 'res': detections}}, ensure_ascii=False)
        else:
            print("编码失败")
            return json.dumps({'msg': '操作失败', 'code': 1000}, ensure_ascii=False)
        # model.load_model(model_path=abs_path("weights/yolov8s.pt", path_type="current"))

    return json.dumps({'msg': '参数错误', 'code': 2001}, ensure_ascii=False)

def del_file(filepath):
    """
    删除某一目录下的所有文件或文件夹
    :param filepath: 路径
    :return:
    """
    del_list = os.listdir(filepath)
    for f in del_list:
        file_path = os.path.join(filepath, f)
        if os.path.isfile(file_path):
            os.remove(file_path)
        elif os.path.isdir(file_path):
            shutil.rmtree(file_path)


if __name__ == '__main__':
    if not os.path.exists(path):
        os.makedirs(path)
    if not os.path.exists(res_path):
        os.makedirs(res_path)
    if not os.path.exists(model_path):
        os.makedirs(model_path)

    del_file(path)
    del_file(res_path)
    del_file(model_path)

    api.run(port=8888, debug=True, host='127.0.0.1', use_reloader=False)  # 启动服务
    # debug=True,改了代码后，不用重启，它会自动重启
    # 'host='127.0.0.1'别IP访问地址
