import os
from datasets import load_dataset

ds = load_dataset("JijoJS/car-damage-new1", name="full", cache_dir="Z:/workspace/project/code-yolov11/data-jijojs-cache"
                 )

# 定义 YOLO 数据集根目录
dataset_root = "Z:/workspace/project/code-yolov11/data-jijojs"  # 使用短路径避免Windows问题

# 创建子目录
os.makedirs(os.path.join(dataset_root, "train/images"), exist_ok=True)
os.makedirs(os.path.join(dataset_root, "train/labels"), exist_ok=True)
os.makedirs(os.path.join(dataset_root, "val/images"), exist_ok=True)
os.makedirs(os.path.join(dataset_root, "val/labels"), exist_ok=True)
os.makedirs(os.path.join(dataset_root, "test/images"), exist_ok=True)
os.makedirs(os.path.join(dataset_root, "test/labels"), exist_ok=True)

# 提取数据集中所有类别名称（假设类别在 'category' 字段）
categories = ds['train'].features['objects'].feature['category'].names
class_map = {name: idx for idx, name in enumerate(categories)}

# 示例输出：{"dent": 0, "scratch": 1, "broken_light": 2}
print("Class mapping:", class_map)

from PIL import Image
import numpy as np


def convert_to_yolo_format(split, ds_split):
    for idx, sample in enumerate(ds_split):
        image = sample["image"]
        objects = sample["objects"]

        # 保存图片
        image_path = os.path.join(dataset_root, split, "images", f"{idx}.jpg")
        image.save(image_path)

        # 获取图片尺寸
        img_width, img_height = image.size

        # 生成标签文件
        label_path = os.path.join(dataset_root, split, "labels", f"{idx}.txt")
        with open(label_path, "w") as f:
            for bbox, category in zip(objects["bbox"], objects["category"]):
                # 提取边界框坐标（假设为 [x_min, y_min, x_max, y_max] 格式）
                x_min, y_min, x_max, y_max = bbox

                # 计算归一化中心坐标和宽高
                x_center = (x_min + x_max) / 2 / img_width
                y_center = (y_min + y_max) / 2 / img_height
                width = (x_max - x_min) / img_width
                height = (y_max - y_min) / img_height

                # 写入标签文件
                class_id = category
                f.write(f"{class_id} {x_center:.6f} {y_center:.6f} {width:.6f} {height:.6f}\n")


# 转换训练集和验证集
convert_to_yolo_format("train", ds["train"])
convert_to_yolo_format("val", ds["validation"])  # 假设验证集为 ds["test"]
convert_to_yolo_format("test", ds["test"])  # 假设验证集为 ds["test"]

