<template>
  <div class="app-container">
    <el-row :gutter="5">
      <el-col :span="6">
        <div>
          <div class="left-form-div">
            <el-form class="left-form" ref="form" :model="form" label-width="80px">
              <el-form-item label="模型选择">
                <el-select class="form-input" v-model="form.model" placeholder="请选择要使用的模型" @change="onSelectModel">
                  <template v-for="item in models">
                    <el-option :label="item.detail" :value="item.modelId"></el-option>
                  </template>
                </el-select>
              </el-form-item>
              <div v-if="noteHtml !== null">
                <el-form-item label="模型简介">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="openModel = true"
                  >点击查看</el-button>
                </el-form-item>
              </div>
              <el-form-item label="摄像头">
                <Camera class="camera-component"
                        :long-time="snap.longTime"
                        :short-time="snap.shortTime"
                        :snapshot-time="snap.snapshotTime"
                        @changeVisible="changeVisible"
                        @refreshDataList="refreshDataList"
                ></Camera>

              </el-form-item>
              <el-form-item label="上传图片">
                <el-upload
                  class="upload-file"
                  action="#"
                  ref="upload"
                  :http-request="uploadFile"
                  :limit="fileLimit"
                  :key="fileList.length"
                  :on-remove="handleRemove"
                  :on-exceed="handleExceed"
                  :file-list="fileList"
                  :before-upload="beforeUpload"
                  :headers="headers"
                  :on-preview="handlePreview"
                  multiple>
                  <el-button size="small" type="primary" v-show="!cameraVisible" >点击上传</el-button>
                  <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过5M</div>
                </el-upload>
              </el-form-item>
              <el-form-item label="记录名称">
                <el-input v-model="form.note" :disabled="cameraVisible"  class="form-input" placeholder="请输入记录名称"></el-input>
              </el-form-item>
              <el-form-item v-show="!cameraVisible">
                <el-button type="primary" @click="onSubmit">开始识别</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>

      </el-col>

      <el-col :span="18">
        <div class="defect-title">
          <i class="el-icon-camera-solid"></i>
          <span class="defect-title-span">识别结果</span>
        </div>
        <div class="img-block" v-for="file in resFileList" :key="file.fileId">
          <el-col :span="6" >
            <el-card :body-style="{ padding: '0px' }">
              <el-image
                class="img-res-image"
                :src="file.url"
                :preview-src-list="[file.url]"
                fit="contain"></el-image>
              <div style="padding: 10px;">
                <span>检测结果：</span>
                <div  class="img_tag_row">
                  <div v-for="item in file.defects" style="padding: 2px">
                    <el-tag :type="item.color" class="img-res-tag" size="mini" >{{item.detail}}</el-tag>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
        </div>
      </el-col>

    </el-row>

    <el-dialog :title="'模型名称：' + model.detail" :visible.sync="openModel" width="50%" append-to-body>
      <hr>
      <div v-html="noteHtml"></div>
    </el-dialog>

  </div>
</template>

<script>
import { listDefect, getDefect, delDefect, addDefect, updateDefect } from "@/api/defect/defect";
import item from "@/layout/components/Sidebar/Item.vue";
import get_type from "@/utils/defect-type";
import request from "@/utils/request";
import MarkdownIt from "markdown-it";
import Camera from "@/views/defect/defect/camera.vue";

const markdown = new MarkdownIt()
export default {
  name:'DefectRecognize',
  components: {Camera},
  computed: {
    item() {
      return item
    }
  },
  data() {
    return {
      uploadCd: Date.now(),
      snap: {
        longTime: 5000,
        shortTime: 1000,
        snapshotTime: 4
      },
      cameraVisible: false,
      markdown,
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: '',
        model: '',
        note: '',
      },
      openModel: false,
      noteHtml: null,
      model: {},
      models: [],
      resFileList: [],
      fileList: [],
      fileLimit: 50,
      // 允许的文件类型
      fileType: [ "pdf", "doc", "docx", "xls", "xlsx","txt","png","jpg", "bmp", "jpeg"],
      // 运行上传文件大小，单位 M
      fileSize: 500,
      //请求头
      headers: { "Content-Type": "multipart/form-data" },
    }
  },
  mounted() {

    request({
      url: "/model/model/list",
      method: "get",
    }).then((res) => {
      this.models = res.rows;
      console.log(res.rows);
      this.open = true;
      this.title = "修改用户";
    })
  },
  methods: {
    changeVisible(status){
      this.cameraVisible = status;
      this.fileList = []
      this.form.note = '';
    },
    refreshDataList(imgList, count, init){
      console.log('refreshDataList', imgList);
      this.form.note = '自动-' + init + '-' + (count - 4);
      console.log('自动识别-' + init + '-' + count - 4, init)
      this.fileList = [];
      imgList.forEach(item => {
        // 提取纯 Base64 部分
        let strs = item.split(',');
        const base64Data = strs[1]; // 取逗号后的内容
        console.log('ffffff', strs[0])
        let mime = strs[0].match(/:(.*?);/)[1];
        console.log('ffffff', mime)
        let bstr = atob(base64Data)
        let n = bstr.length;
        let u8arr = new Uint8Array(n);

        while (n--) {
          u8arr[n] = bstr.charCodeAt(n);
        }
        console.log('ffffff')
        this.uploadFile({file: new File([u8arr], init + '-' + count++ + '.png' , { type: mime })})
      })

      setTimeout(() => {
        this.onSubmit();
      }, 2000);

    },
    onSelectModel(modelId) {
      this.models.forEach(model => {
        if (model.modelId === modelId) {
          this.noteHtml = markdown.render(model.note);
          this.model = model;
        }
      })
    },
    onSubmit() {
      request({
        url: "/defect/defect/recognize",
        method: "post",
        data: {files: this.fileList, model: this.form.model, note: this.form.note},
      }).then((res) => {
          if (res.code === 200) {
            this.$message({
              type: 'success',
              message: '识别成功！'
            });
            console.log(res.data.results);
            this.resFileList = res.data.results;
            this.resFileList.forEach(item => {
              if (item.defectId === 0) {
                item.defects = [{color: 'success', detail: '正常'}];
              }else {
                let defects = get_type(item.defectId);
                item.defects = [];
                defects.forEach(def => {
                  item.defects.push({color: 'danger', detail: def});
                })
              }
              console.log('rrrrrr', res);
            })
          }
      })


    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    //上传文件的事件
    uploadFile(item, a){
      if (this.uploadCd < Date.now() - 3000) {
        this.$message('文件上传中........')

      }
      this.uploadCd = Date.now();
      //上传文件的需要formdata类型;所以要转
      let FormDatas = new FormData()
      console.log(item, a, a);
      FormDatas.append('file',item.file);
      request({
        method: 'post',
        url: '/file/file',
        headers:this.headers,
        data: FormDatas
      }).then(res=>{

        if(res.data.fileId !== '' || res.data.fileId !== null){
          this.fileList.push(res.data);//成功过后手动将文件添加到展示列表里

          console.log(this.fileList, "dddd");
        }
      })
    },
    //超出文件个数的回调
    handleExceed(){
      this.$message({
        type:'warning',
        message:'超出最大上传文件数量的限制！'
      });
    },
    //上传文件之前
    beforeUpload(file){
      if (file.type !== "" || file.type != null || file.type !== undefined){
        //截取文件的后缀，判断文件类型
        const FileExt = file.name.replace(/.+\./, "").toLowerCase();
        //计算文件的大小
        const isLt5M = file.size / 1024 / 1024 < 50; //这里做文件大小限制
        //如果大于50M
        if (!isLt5M) {
          this.$message.error('上传文件大小不能超过 500 MB!');
          return false;
        }
        //如果文件类型不在允许上传的范围内
        if(this.fileType.includes(FileExt)){
          return true;
        }
        else {
          this.$message.error("上传文件格式不正确!");
          return false;
        }
      }
    },
    useCamera() {
      // 获取 canvas 画布
      this.canvas = document.getElementById('canvasCamera');
      this.context = this.canvas.getContext('2d');
      // 旧版本浏览器可能根本不支持mediaDevices，我们首先设置一个空对象
      if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {};
      }
      // 正常支持版本
      navigator.mediaDevices
        .getUserMedia({
          video: true,
        })
        .then((stream) => {
          // 摄像头开启成功
          this.mediaStreamTrack = typeof stream.stop === 'function' ? stream : stream.getTracks()[0];
          this.video_stream = stream;
          this.$refs.video.srcObject = stream;
          this.$refs.video.play();
        })
        .catch(err => {
          console.log(err);
        });
    }
  }
}
</script>
<style scoped>
.camera-component{
  width: 100%;
  padding: 0 15px 15px;
}
.app-container {

 }
.defect-img{
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin: auto;

}
.defect-title{
  width: 100%;
  height: 13%;
  text-align: center;
  padding-bottom: 20px;
  font-size: 56px;
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei";
}
.img-res-tag{
  text-align: center;

}
.img_tag_row{
  height: 20px;
  display: flex;
  flex: auto;
}
.img-res-image{
  width: 208px;
  height: 140px;
  margin-left: 5px;
}
.demonstration{
  height: 13px;
}
 .left-form-div {
   height: 100%;
   box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
 }
.left-form {
   margin-top: 10px;
   padding-top: 20px;
   padding-bottom: 20px;
 }
.upload-file{
   width: 90%;
 }
.form-input{
  width: 80%;
}
</style>
