<template>
  <div class="app-container">
    <el-switch
      v-model="visible"
      @change="changeVisible"
      active-text="展开"
      inactive-text="收起">
    </el-switch>
    <div class="publish" v-show="visible">
      <video  class="camera"  ref="video"></video>
      <canvas v-show="false" id="canvasCamera" ref="canvas"></canvas>
      <div v-if="imgSrc" class="img_bg_camera">
        <img :src="imgSrc" class="tx_img" />
      </div>
      <div class="input-div">
        每轮间隔:<el-input-number class="input-count"
                                  v-model="longTime"
                                  controls-position="right"
                                  :min="0.1"
                                  :precision="2"
                                  :step="0.1"
                                  :max="10"></el-input-number><br>
        拍摄间隔:<el-input-number class="input-count"
                                  v-model="shortTime"
                                  controls-position="right"
                                  :min="0.1"
                                  :precision="2"
                                  :step="0.1"
                                  :max="10"></el-input-number><br>
        拍摄次数:<el-input-number class="input-count"
                                  v-model="snapshotTime"
                                  controls-position="right"
                                  :min="0.1"
                                  :precision="2"
                                  :step="0.1"
                                  :max="10"></el-input-number>
      </div>
      <el-switch
        v-model="openCamera"
        active-text="自动识别"
        @change="changeCamera"
        inactive-text="关闭自动">
      </el-switch>
    </div>

  </div>

</template>

<script>
export default {
  name:'Camera',
  data() {
    return {
      shortTime: 0.5,
      longTime: 15,
      snapshotTime: 4,
      count: 0,
      visible: false,
      openCamera: false, // 是否打开摄像头
      mediaStreamTrack: {},
      video_stream: '', // 视频stream
      imgSrc: '', // 拍照图片
      imgSrcList: [], // 拍照图片
      canvas: null,
      context: null,
      init: '',
    };
  },
  mounted() {
    // 进入页面 自动调用摄像头
    this.getCamera();
  },
  methods: {
    // 调用打开摄像头功能
    getCamera() {
      // 获取 canvas 画布
      this.canvas = document.getElementById('canvasCamera');
      this.canvas.width = 2560; // 设置canvas宽度
      this.canvas.height = 1440; // 设置canvas高度
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
    },
    // 拍照 绘制图片
    async setImage() {
      if (!this.openCamera) return;
      let shortT = this.shortTime
      // 点击canvas画图
      for (let i = 0; i < this.snapshotTime; i++) {
        this.context.drawImage(
          this.$refs.video,
          0,
          0,
          2560,
          1440,
        );
        // 获取图片base64链接
        const image = this.$refs.canvas.toDataURL('image/png')
        this.imgSrcList.push(image);
        await new Promise(resolve => setTimeout(resolve, shortT * 1000));
        // await new Promise(resolve => setTimeout(() => {
        //   console.log("1 秒后执行");
        //   }, time));
      }

      this.$emit('refreshDataList', this.imgSrcList, this.count, this.init);
      this.imgSrcList = []
      this.count += 4

      setTimeout(this.setImage, this.longTime * 1000); // 每隔longTime秒拍照一次
    },
    // 打开摄像头
    OpenCamera() {
      console.log('打开摄像头');
      this.getCamera();
    },
    // 关闭摄像头
    CloseCamera() {
      console.log('关闭摄像头');
      this.$refs.video.srcObject.getTracks()[0].stop();
    },
    changeCamera(status) {
      if (status) {
        this.init = Date.now().toString()
        this.count = 0;
        this.setImage()
      }
    },
    changeVisible(status){
      this.$emit('changeVisible', status);
    }
  },

  // watch: {
  //   longTime(newVal) {
  //
  //   },
  // },
};
</script>

<style scoped>
.camera{
  width: 100%;
  display: block;
}
#canvasCamera{
  width: 2560px;
  height: 1440px;
}
.input-count{
  margin-top: 5px;
  margin-left: 8px;
  margin-right: 10px;
  width: 55%;
}
</style>
