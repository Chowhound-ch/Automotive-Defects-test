
<template>
  <div class="app-container">
    <el-row>
      <el-col :span="6">
        <car_sort @sort="updateCars"/>
      </el-col>
      <el-col :span="18">
        <el-table v-loading="loading" :data="carList" @selection-change="handleSelectionChange" @sort-change="getListAll">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="id" width="50" align="center" prop="carId" sortable="custom" />
          <el-table-column label="厂商" width="55" align="center" prop="manufacturer" />
          <el-table-column label="型号" width="80" align="center" prop="ver" />
          <el-table-column label="价格（万元）" width="100" align="center" prop="price" sortable="custom" />
          <el-table-column label="预定时间" width=100 align="center" prop="updateTime" sortable="custom">
            <template slot-scope="scope">
              {{formatDate(scope.row.updateTime)}}
            </template>
          </el-table-column>
          <el-table-column label="卖家" width="170" align="center" prop="userId" sortable="custom">
            <template slot-scope="scope">
              <div class="card-avatar">
                <el-avatar :src="baseURL + scope.row.user.avatar" size="small"/>
                <span id="car-username">{{scope.row.user.userName}}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="viewCarDetail(scope.row.carId)"
              >商品详情</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row.carId)"
              >取消预定</el-button>
            </template>
          </el-table-column>

        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>
  </div>
</template>
<script>
import Car_sort from "@/views/shop/shop/car_sort.vue";
import request from "@/utils/request";
import {formatDate} from "@/utils/car/date";
import get_type from "@/utils/defect-type";
import ShowRecord from "@/views/record/record/show_record.vue";

export default {
  name: "order" ,
  components: {ShowRecord, Car_sort},
  data() {
    return {
      loading: true,
      carList: [],
      carListAll: [],
      multipleSelection: [],
      carSort: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      reviewForm: {
        licImg: null,
        insurance: null,
        cert: null,
        invoice: null,
        number: null,
        user: {
          userName: null,
        },
        manufacturer: null,
        ver: null,
        imgs: [{}]
      },
      order: {
        order: null, //"ascending" or "descending"
        prop: null,
      },
      baseURL: 'http://localhost' + process.env.VUE_APP_BASE_API,
      recordId: null,
      record:{},
      total: 0,
      showReview: false,
    }
  },
  mounted() {
    this.getListAll();
  },
  methods: {
    formatDate,
    handleDelete(carId) {
      let params = {carId, isReviewed: 1};
      this.$confirm('确认取消预定？')
        .then(_ => {
          request({
            url: "/car/review",
            method: "POST",
            data: params
          }).then(res => {
            this.showReview = false;
            this.getListAll();
          })
        })
    },
    viewCarDetail(carId) {
      console.log(carId);
      this.$router.push({
        path: `/shop/detail`,
        query: {carId: carId, state: 'order'}
      });
    },
    getListAll(order) {
      let params = {};
      console.log(order);
      if (this.carSort) {
        params = {...this.carSort};
      }
      if (order) {
        order.prop = order.prop.replace(/([a-z])([A-Z])/g, '$1_$2').toLowerCase();
        params = {...params, ...order}
      }
      console.log(params, 'par');request({
        method: "GET",
        url: "/car/listOrder",
        params
      }).then(res => {
        console.log(res, 'dadsfdafw');
        this.carListAll = res.rows;
        this.carListAll.forEach(item => {
          item.mileage = item.mileage / 10000
          item.price = item.price / 10000
        })
        // carList为carListAll前十个
        this.queryParams = {
          pageNum: 1,
          pageSize: 10,
        }
        this.getList()
        this.total = this.carListAll.length
        this.loading = false
      })
    },
    getList() {
      let start = (this.queryParams.pageNum - 1) * this.queryParams.pageSize;
      let end = this.queryParams.pageNum * this.queryParams.pageSize;
      this.carList = this.carListAll.slice(start, end);
    },
    updateCars(carSort) {
      this.loading = true
      this.carSort = carSort;
      this.getListAll()
    },
    handleSelectionChange(val) {
      console.log(val, 'val');
      this.multipleSelection = val;
    },
  }
}
</script>

<style scoped>
.cover {
  width: 178px;
  height: 178px;
  display: block;
}

.review-image {
  width: 200px;
  height: 200px;
}

.review-multi-image {
  width: 80px;
  height: 80px;
}

.review-row {
  margin-top: 20px;
}
</style>
