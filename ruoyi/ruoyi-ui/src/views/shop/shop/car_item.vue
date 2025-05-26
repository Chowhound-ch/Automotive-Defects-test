<template>
  <div class="app-container">
      <el-row :gutter="5">
        <div v-for="car in cars" :key="car.carId" >
          <el-col :span="8">
            <div class="car-item-card" @click="viewCarDetail(car.carId)">
              <el-card
                class="car-item"
                :body-style="{ padding: '12px' }"
              >
                <el-image
                  class="img-res-image"
                  :src="car.url"
                  fit="contain"></el-image>
                <el-descriptions :title="car.manufacturer + ' - ' + car.ver"
                                 size="medium"
                                 :column="2"
                                 content-class-name="car-item-content"
                                 label-class-name="car-item-label">
                  <el-descriptions-item label="车型">{{car.type}}</el-descriptions-item>
                  <el-descriptions-item label="价格">{{car.price /10000}} 万元</el-descriptions-item><br>
                  <el-descriptions-item label="上架">{{getDate(car.createTime)}}</el-descriptions-item>
                  <el-descriptions-item label="里程">{{car.mileage / 10000}} 万公里</el-descriptions-item>

                </el-descriptions>

                <div class="card-avatar">
                  <el-avatar :src="baseURL + car.user.avatar" size="small"/>
                  <span id="car-username">{{car.user.userName}}</span>
                </div>
              </el-card>
            </div>

          </el-col>
        </div>
      </el-row>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />

  </div>
</template>

<script>
 import request from "@/utils/request";

 export default{
  name: "car_item",
  props: ['carSort'],
   data() {
    return {
      cars: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      baseURL: 'http://localhost' + process.env.VUE_APP_BASE_API,
      total: 0
    };
  },
   created() {
    console.log('ddd', this.baseURL)
    this.getList();
   }
   ,
   methods:{
     getList(sort){
       let params = {...this.queryParams};
       if (sort) {
         params = {...params, ...sort}
       }
       request({
         method: "GET",
         url: "/car/list",
         params
       }).then(res => {
         console.log(res, 'dadsfdafw');
         this.cars = res.rows;
         this.total = this.cars.length

       })
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
        // 本月
       desTime.setDate(desTime.getDate() + 7);

        if (time.getFullYear() === desTime.getFullYear) {
          return time.getMonth() + 1 + '-' + time.getDate();
        } else {
          return time.getFullYear() + '-' + (time.getMonth() + 1) + '-' + time.getDate();
        }
     },
     viewCarDetail(carId) {
       console.log(carId);
       this.$router.push({
         path: `/shop/detail`,
         query: {carId: carId}
       });
     }
   },
   watch: {
      carSort: {
        handler(val) {
          console.log(val, 'val');
          this.getList(val);
        },
        immediate: false,
        deep: true
      }
   }
 }
</script>

<style>
.card-avatar{
  float: right;
  padding-right: 10px;
  display: flex;
}
.car-item{
  padding-bottom: 5px;
}
#car-username{
  font-size: 18px;
  align-self: center;
  color: #666;
}
.car-item-content{
  font-size: 11px;
}

.car-item-label{
  font-size: 11px;
}
</style>
