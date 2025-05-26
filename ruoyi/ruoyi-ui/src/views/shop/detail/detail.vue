
<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="14">
        <el-carousel class="el-carousel" :interval="4000" type="card" height="230px">
          <el-carousel-item v-for="img in car.imgs" :key="img.imgId">
            <el-image
              class="review-image"
              :src="img.url"
              center
              :preview-src-list="[img.url]">
            </el-image>
          </el-carousel-item>
        </el-carousel>
        <div class="defect-img">
          <div class="defect-title">
            <i class="el-icon-camera-solid"></i>
            <span class="defect-title-span">车身表面缺陷识别结果</span>
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
                  <div style="padding: 6px;">
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
        </div>
        </el-col>
      <el-col :span="10">
        <el-card class="car-item">
          <el-descriptions :title="car.manufacturer + ' - ' + car.ver"
                           size="mini"
                           :column="1"
                           :label-style="{ width: '100px', fontSize: '14px', textAlign: 'center' }"
                           :content-style="{ fontSize: '14px' }"
                           border>
            <template v-slot:title>
              <span class="review-title">二手车型号：{{car.manufacturer +' - ' + car.ver}} </span>
            </template>
            <el-descriptions-item label="卖家">
              <div class="card-avatar">
                <el-avatar :src="car.user.avatar != null ? baseURL + car.user.avatar : null" size="small"/>
                <span id="car-username">{{car.user.userName}}</span>
              </div>
            </el-descriptions-item>
            <el-descriptions-item label="车型">{{car.type}}</el-descriptions-item>
            <el-descriptions-item label="价格">{{car.price / 10000}} 万元</el-descriptions-item><br>
            <el-descriptions-item label="上架时间">{{getDate(car.createTime)}}</el-descriptions-item>
            <el-descriptions-item label="里程">{{car.mileage / 10000}} 万公里</el-descriptions-item>
            <el-descriptions-item label="使用时间">{{car.years}} 年</el-descriptions-item>
            <el-descriptions-item label="排量">{{car.output / 100}} L</el-descriptions-item>
            <el-descriptions-item label="识别号（VIN）">{{car.review.number}}</el-descriptions-item>
<!--            <el-descriptions-item label="关联的表面识别">
&lt;!&ndash;              <el-link type="primary" @click="viewRecord">{{record.note}}</el-link>&ndash;&gt;
&lt;!&ndash;              <ShowRecord :recordId="car.recordId"></ShowRecord>&ndash;&gt;
            </el-descriptions-item>-->
            <el-descriptions-item label="卖家备注">
              <el-input
                type="textarea"
                :value="car.notes"
                readonly
                :min="3"
                :max="8"
                :rows="4"
                class="pre-line-text"
              ></el-input>
            </el-descriptions-item>

          </el-descriptions>

          <el-button  v-if="state === 'order'" type="danger" class="buy-button" size="medium" @click="handlerOrder(1)">取消预定</el-button>
          <el-button v-else type="warning" class="buy-button" size="medium" @click="handlerOrder(4)">点击预定</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import request from "@/utils/request";
import {formatDate} from "@/utils/car/date";
import ShowRecord from "@/views/record/record/show_record.vue";
import get_type from "@/utils/defect-type";

export default {
  name: "detail",
  components: {ShowRecord},
  data(){
    return {
      carId: null,
      car: {user: {}, record: {}, review: {}},
      baseURL: 'http://localhost' + process.env.VUE_APP_BASE_API,
      record: null,
      state: null
    }
  },
  mounted() {
    this.carId = this.$route.query.carId;
    this.state = this.$route.query.state;
    if (!this.carId){
      console.log(this.carId);
      this.$router.push({ path: "/" });
    }
    request({
      url: '/car/' + this.carId,
      method: "get",
    }).then(res=>{
      this.car = res.data;
      console.log(this.car);
      this.getNewRecord(this.car.recordId);
    })
  },
  methods: {
    handlerOrder(isReviewed) {
      this.$confirm(`确认${isReviewed === 1 ? '取消': ''}预定？`)
        .then(_ => {
          let params = {carId: this.carId, isReviewed};

          request({
            url: "/car/review",
            method: "POST",
            data: params
          }).then(res => {
            this.showReview = false;
            this.getListAll();
          })

          this.$router.push({name: 'Order'})
        });
    },
    getDate(time){
      time = new Date(time);
      // 如果是今日内
      let desTime = new Date();
      desTime.setHours(0,0,0,0);
      if (time > desTime) {
        return '今日内';
      }
      // 昨日内
      desTime.setDate(desTime.getDate() - 1);
      if (time > desTime) {
        return '昨日内';
      }
      // 本周
      desTime.setDate(desTime.getDate() - 6);
      if (time > desTime) {
        return '本周内';
      }
      desTime.setDate(desTime.getDate() + 7);

      if (time.getFullYear() === desTime.getFullYear) {
        return time.getMonth() + 1 + '-' + time.getDate();
      } else {
        return time.getFullYear() + '-' + (time.getMonth() + 1) + '-' + time.getDate();
      }
    },
    getNewRecord(recordId) {
      request({
        url: "/record/record/" + recordId,
        method: "GET",
      }).then(res => {
        this.record = res.data;
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
        this.showReview = true;
      })
    }
  }
}
</script>
<style scoped>
.review-title{
  font-size: 20px;
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}
.el-carousel{
  margin-top: 20px;
}
.pre-line-text{
  font-size: 14px;
}
.defect-title{
  width: 100%;
  height: 10%;
  text-align: center;
  padding-bottom: 5px;
  font-size: 36px;
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
  margin-top: 5px;
  width: 135px;
  height: 90px;
  margin-left: 1px;
}
.buy-button{
  margin-top: 5px;
  margin-left: 120px;
  font-size: 22px;
  width: 140px;
  height: 40px;

}
.defect-img{
  border-radius: 30px

}
</style>
