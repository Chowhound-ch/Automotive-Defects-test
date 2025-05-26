
<template>
  <div class="app-container">
    <el-row :gutter="5">
      <el-col :span="11" :offset="1">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>发布二手汽车</span>
          </div>
          <el-form ref="form" :model="form" label-width="120px">
            <el-form-item label="封面">
              <el-upload
                class="cover-uploader"
                action="#"
                :show-file-list="false"
                :http-request="uploadCover"
                :headers="headers"
                :before-upload="beforeAvatarUpload">
                <img v-if="form.url" :src="form.url" class="cover">
                <i v-else class="el-icon-plus cover-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item label="厂商">
              <el-select v-model="form.manufacturer" placeholder="请选择汽车厂商" @input="updateManufacture">
                <div v-for="(vers, manufacturer) in kinds" :key="vers.id">
                  <el-option :label="manufacturer" :value="manufacturer"></el-option>
                </div>
              </el-select>
            </el-form-item>
            <el-form-item label="型号">
              <el-select v-model="form.ver" placeholder="请选择汽车型号">
                <div v-for="( ver , _) in kinds[form.manufacturer].vers" :key="ver">
                  <el-option :label="ver" :value="ver"></el-option>
                </div>
              </el-select>
            </el-form-item>
            <el-form-item label="关联的表面识别">
              <el-select v-model="form.recordId"  placeholder="请选择关联的表面识别" @input="updateRecord" >
                <div  v-for="record in recordList" :key="record.recordId">
                  <el-option :label="record.note" :value="record.recordId"></el-option>
                </div>
              </el-select>
              <el-button v-if="displayRecordId"
                         size="mini"
                         type="text"
                         icon="el-icon-edit"
                         @click="viewRecord"
              >点击查看</el-button>
            </el-form-item>
            <el-form-item label="价格">
              <el-input class="form-input" v-model.number="form.price" placeholder="请输入价格"></el-input>
              <span>（单位：万元）</span>
            </el-form-item>
            <el-form-item label="里程">
              <el-input class="form-input" v-model.number="form.mileage" placeholder="请输入里程"></el-input>
              <span>（单位：万公里）</span>
            </el-form-item>
            <el-form-item label="使用时间">
              <el-input class="form-input" v-model.number="form.years" placeholder="请输入使用时间"></el-input>
              <span>（单位：年）</span>
            </el-form-item>
            <el-form-item label="车体型">
              <el-select v-model="form.type"  placeholder="请选择车体型" >
                <el-option label="小型（5座及以下）" value="小型"></el-option>
                <el-option label="中型（7座及以下）" value="中型"></el-option>
                <el-option label="大型（7座以上）" value="大型"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="排量">
              <el-input class="form-input" v-model.number="form.output" placeholder="请输入使用排量"></el-input>
              <span>（单位：L）</span>
            </el-form-item>
            <el-form-item label="卖家说明">
              <el-input class="text-input"
                        type="textarea"
                        v-model="form.notes"
                        :autosize="{ minRows: 3, maxRows: 6}"
                        placeholder="请输入其他补充说明"></el-input>
            </el-form-item>
            <el-form-item label="展示图片">
              <el-upload
                action="#"
                list-type="picture-card"
                :http-request="uploadCarImg"
                :headers="headers"
                :file-list="form.imgs"
                :on-preview="handlePictureCardPreview"
                :on-remove="handleRemoveCarImg"
                >
                <i class="el-icon-plus"></i>
              </el-upload>
            </el-form-item>
          </el-form>
        </el-card>
        <el-dialog :visible.sync="dialogVisible">
          <img width="100%" :src="dialogImageUrl" alt="">
        </el-dialog>
      </el-col>
      <el-col :span="11" :offset="1">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>证件信息上传</span>
          </div>
          <el-form ref="form" :model="form" label-width="120px">
            <el-form-item label="驾驶证">
              <el-upload
                class="cover-uploader"
                action="#"
                :show-file-list="false"
                :http-request="uploadLic"
                :key="review.licImg === null ? 0 : 1"
                :headers="headers"
                :before-upload="beforeAvatarUpload">
                <img v-if="review.licImg" :src="review.licImg" class="cover">
                <i v-else class="el-icon-plus cover-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item label="保险单据">
              <el-upload
                class="pdf-uploader"
                action="#"
                :http-request="uploadInsurance"
                :key="insurance.length"
                :limit="1"
                :headers="headers"
                :file-list="insurance">
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">只能上传pdf文件，且不超过2mb</div>
              </el-upload>
            </el-form-item>
            <el-form-item label="机动车登记证书">
              <el-upload
                class="cover-uploader"
                action="#"
                :key="review.cert === null ? 0 : 1"
                :show-file-list="false"
                :http-request="uploadCert"
                :headers="headers"
                :before-upload="beforeAvatarUpload">
                <img v-if="review.cert" :src="review.cert" class="cover">
                <i v-else class="el-icon-plus cover-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item label="发票（电子版）">
              <el-upload
                class="pdf-uploader"
                action="#"
                :http-request="uploadInvoice"
                :key="invoice.length"
                :limit="1"
                :headers="headers"
                :file-list="invoice">
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">只能上传pdf文件，且不超过2mb</div>
              </el-upload>
            </el-form-item>
            <el-form-item label="识别号（VIN）">
              <el-input class="form-input" v-model="review.number" placeholder="请输入使用车辆识别号"></el-input>
            </el-form-item>
          </el-form>
        </el-card>
        <el-button class="submit-button" type="primary" @click="onSubmit" size="medium">提交审核</el-button>

      </el-col>
    </el-row>

    <el-dialog :title="''" :visible.sync="displayResult" width="80%" append-to-body>
      <div class="defect-title">
        <i class="el-icon-camera-solid"></i>
        <span class="defect-title-span">识别结果（{{record.note}}）</span>
      </div>
      <el-row :gutter="5">
        <div class="img-block" v-for="file in record.results" :key="file.fileId">
          <el-col :span="6" >
            <el-card :body-style="{ padding: '0px' }">
              <el-image
                class="img-res-image"
                :src="file.url"
                :preview-src-list="[file.url]"
                fit="contain"></el-image>
              <div style="padding: 8px;">
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
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import kinds from '@/utils/car/kinds.js'
import request from "@/utils/request";
import {listRecord} from "@/api/record/record";
import get_type from "@/utils/defect-type";
import Index from "@/views/defect/defect/index.vue";

export default {
  name: "publish",
  components: {Index},
  data() {
    return {
      form: {
        manufacturer: '全部',
        ver: '全部',
        price: null,
        mileage:null,
        createTime: '',
        keyword: '',
        fileId: null,
        recordId: null,
        years: null,
        type: null,
        output: null,
        notes: null,
        imgs: [],
        url: null
      },
      review: {
        licImg: null,
        insurance: null,
        cert: null,
        invoice: null,
        number: null,
      },
      kinds,
      insurance: [],
      invoice: [],
      headers: { "Content-Type": "multipart/form-data" },
      recordList: [],
      displayRecordId: false,
      displayResult: false,
      record: {},
      dialogImageUrl: '',
      dialogVisible: false
    }
  },
  mounted() {
    this.getRecordList()

    if (!this.$route.fullPath.endsWith('publish')) {
      this.form = JSON.parse(localStorage.getItem('tempFormData'));
      console.log('form', this.form);
      this.form.price = this.form.price / 10000;
      this.form.mileage = this.form.mileage / 10000;
      this.form.output = this.form.output / 100;
      this.review = this.form.review;
      this.insurance = [{name: '保险信息', url: this.form.insurance}]
      this.invoice = [{name: '发票信息', url: this.form.invoice}]
    }

  },
  methods: {
    updateRecord(data){
      this.displayRecordId = true;
      this.recordList.forEach(item => {
        if (item.recordId === data){
          this.record = item;
        }
      })

    },
    getRecordList() {
      this.loading = true;
      listRecord().then(response => {
        this.recordList = response.rows;
      });
    },
    updateManufacture() {
      this.form.ver = '全部'
    },
    uploadCover(item) {
      this.uploadFile(item, (data)=>{
        this.form.url = data.url
      })
    },
    uploadCarImg(item) {
      this.uploadFile(item, (data)=>{
        this.form.imgs.push({name: data.name, url: data.url});
      })
    },
    // 证件
    uploadCert(item) {
      this.uploadFile(item, (data)=>{
        this.review.cert = data.url;
      })
    },
    uploadInsurance(item) {
      this.uploadFile(item, (data)=>{
        this.review.insurance = data.url;
        this.insurance = [data];
      })
    },
    uploadInvoice(item) {
      this.uploadFile(item, (data)=>{
        this.review.invoice = data.url;
        this.invoice = [data];
      })
    },
    uploadLic(item) {
      this.uploadFile(item, (data)=>{
        this.review.licImg = data.url;
      })
    },
    //上传文件的事件
    uploadFile(item, fun){
      this.$message('文件上传中........')
      //上传文件的需要formdata类型;所以要转
      let FormDatas = new FormData()
      FormDatas.append('file',item.file);
      request({
        method: 'post',
        url: '/file/file',
        headers:this.headers,
        data: FormDatas
      }).then(res=>{
        if(res.data.fileId !== '' || res.data.fileId !== null){
          fun(res.data);
        }
      })
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt5M = file.size / 1024 / 1024 < 5;

      if (!isJPG) {
        this.$message.error('上传图片只能是 JPG 格式!');
      }
      if (!isLt5M) {
        this.$message.error('上传图片大小不能超过 5MB!');
      }
      return isJPG && isLt5M;
    },
    viewRecord() {
      console.log(this.record)
      this.record.results.forEach(item => {
        if (item.defectId === 0) {
          item.defects = [{color: 'success', detail: '正常'}];
        }else {
          let defects = get_type(item.defectId);
          item.defects = [];
          defects.forEach(def => {
            item.defects.push({color: 'danger', detail: def});
          })
        }
      })
      this.displayResult = true;
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleRemoveCarImg(_, fileList) {
      this.form.imgs = fileList
    },
    onSubmit() {
      this.form.review = this.review;
      request({
        url: '/car/add',
        method: 'post',
        data: {
          ...this.form,
          price: this.form.price * 10000,
          mileage: this.form.mileage * 10000,
          output: this.form.output * 100,
        }
      }).then(res => {
        if (res.code === 200) {
          this.$message({
            message: '提交成功',
            type: 'success'
          });
          this.reset();
        } else {
          this.$message({
            message: res.msg,
            type: 'error'
          });
        }
        this.$router.push({name: 'Mine'});
      })
    },
    reset() {
      this.form = {
        manufacturer: '全部',
          ver: '全部',
          price: 0,
          mileage:0,
          createTime: '',
          keyword: '',
          fileId: null,
          recordId: null,
          years: null,
          type: null,
          output: null,
          notes: null,
          imgs: [],
      }
      this.review = {
        licImg: null,
          insurance: null,
          cert: null,
          invoice: null
      }
    }

  }
}
</script>

<style scoped>
.box-card{
  width: 100%;
}
.cover-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}
.cover :hover {
  border-color: #409EFF;
}
.cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.cover {
  width: 178px;
  height: 178px;
  display: block;
}
.form-input{
  width: 60%;
}
.text-input{
  width: 70%;
}
.defect-title{
  width: 100%;
  height: 10%;
  text-align: center;
  padding-bottom: 20px;
  font-size: 40px;
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei";
}
.img-res-tag{
  text-align: center;
}.img_tag_row{
   height: 20px;
   display: flex;
   flex: auto;
 }

.img-res-image{
  margin-top: 5px;
  width: 220px;
  height: 150px;
  margin-left: 7px;
}
.pdf-uploader{
  width: 40%;
}
.submit-button{
  margin-top: 20px;
  margin-left: 30%;
  font-size: 26px;
  width: 180px;
  height: 60px;
}
</style>
