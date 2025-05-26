
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
          <el-table-column label="上架时间" width=100 align="center" prop="createTime" sortable="custom">
            <template slot-scope="scope">
              {{formatDate(scope.row.createTime)}}
            </template>
          </el-table-column>
          <el-table-column label="里程（万公里）" width="120" align="center" prop="mileage" sortable="custom"/>
          <el-table-column label="审核状态" width="80" align="center" prop="isReviewed" sortable="custom">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.isReviewed === 0" type="warning">未审核</el-tag>
              <el-tag v-else-if="scope.row.isReviewed === 1" type="success">已审核</el-tag>
              <el-tag v-else-if="scope.row.isReviewed === 2" type="danger">审核失败</el-tag>
              <el-tag v-else-if="scope.row.isReviewed === 3" type="info">已下架</el-tag>
              <el-tag v-else-if="scope.row.isReviewed === 4">已预定</el-tag>
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="viewReview(scope.row)"
              >查看审核信息</el-button>
<!--              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['result:result:remove']"
              >删除</el-button>-->
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
        <el-dialog title="审核信息" :visible.sync="showReview" width="900px" append-to-body>
          <el-descriptions class="margin-top" title="展示图" :column="6" size="medium" border>
            <el-descriptions-item :span="3">
              <template slot="label">
                <i class="el-icon-user"></i>
                封面
              </template>
              <el-image
                class="review-image"
                :src="reviewForm.url"
                center
                :preview-src-list="[reviewForm.url]">
              </el-image>
            </el-descriptions-item>
            <el-descriptions-item :span="3">
              <template slot="label">
                <i class="el-icon-user"></i>
                展示图<br>
                （点击图片查看全部）
              </template>
<!--              <el-image v-for="img in reviewForm.imgs" :key="img.imgId" :src="img.url" lazy></el-image>-->
              <el-row :gutter="5">
                <el-col :span="8" v-for="img in reviewForm.imgs" :key="img.imgId">
                  <el-image
                    class="review-multi-image"
                    :src="img.url"
                    center
                    :preview-src-list="getUrlList(reviewForm.imgs)">
                  </el-image>
                </el-col>

              </el-row>
            </el-descriptions-item>
          </el-descriptions>

          <el-descriptions class="margin-top" title="基础信息" :column="6" size="medium" border>
            <el-descriptions-item :span="2">
              <template slot="label">
                <i class="el-icon-user"></i>
                车主
              </template>
              {{reviewForm.user.userName}}
            </el-descriptions-item>
            <el-descriptions-item :span="2">
              <template slot="label">
                <i class="el-icon-user"></i>
                制造商
              </template>
              {{reviewForm.manufacturer}}
            </el-descriptions-item>
            <el-descriptions-item :span="2">
              <template slot="label">
                <i class="el-icon-user"></i>
                型号
              </template>
              {{reviewForm.ver}}
            </el-descriptions-item>
            <el-descriptions-item :span="2">
              <template slot="label">
                <i class="el-icon-user"></i>
                价格
              </template>
              {{reviewForm.price / 10000}} 万元
            </el-descriptions-item>
            <el-descriptions-item :span="2">
              <template slot="label">
                <i class="el-icon-user"></i>
                里程（万公里）
              </template>
              {{reviewForm.mileage / 10000}} 万公里
            </el-descriptions-item>
            <el-descriptions-item :span="2">
              <template slot="label">
                <i class="el-icon-user"></i>
                排量
              </template>
              {{reviewForm.output / 100}} L
            </el-descriptions-item>
            <el-descriptions-item :span="4" :contentStyle="{'text-align': 'center'}">
              <template slot="label">
                <i class="el-icon-user"></i>
                识别号（VIN）
              </template>
              {{reviewForm.number}}
            </el-descriptions-item>
            <el-descriptions-item :span="2">
              <template slot="label">
                <i class="el-icon-user"></i>
                使用时间
              </template>
              {{reviewForm.years}} 年
            </el-descriptions-item>

            <el-descriptions-item :span="2">
              <template slot="label">
                <i class="el-icon-user"></i>
                车型
              </template>
              {{reviewForm.type}}
            </el-descriptions-item>
            <el-descriptions-item :span="4">
              <template slot="label">
                <i class="el-icon-user"></i>
                关联的表面识别
              </template>
              <ShowRecord :recordId="reviewForm.recordId"></ShowRecord>
            </el-descriptions-item>
            <el-descriptions-item :span="6">
              <template slot="label">
                <i class="el-icon-user"></i>
                卖家备注
              </template>
              {{reviewForm.notes}}
            </el-descriptions-item>
          </el-descriptions>
          <el-descriptions class="margin-top" title="证件信息" :column="6" size="medium" border>
            <el-descriptions-item :span="3">
              <template slot="label">
                <i class="el-icon-user"></i>
                驾驶证
              </template>
              <el-image
                class="review-image"
                :src="reviewForm.licImg"
                center
                :preview-src-list="[reviewForm.licImg]">
              </el-image>
            </el-descriptions-item>
            <el-descriptions-item :span="3">
              <template slot="label">
                <i class="el-icon-user"></i>
                机动车登记证书
              </template>
              <el-image
                class="review-image"
                :src="reviewForm.cert"
                center
                :preview-src-list="[reviewForm.cert]">
              </el-image>
            </el-descriptions-item>
            <el-descriptions-item :span="3">
              <template slot="label">
                <i class="el-icon-user"></i>
                保险单据
              </template>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="viewInsurance"
              >查看保险单据</el-button>
            </el-descriptions-item>
            <el-descriptions-item :span="3">
              <template slot="label">
                <i class="el-icon-user"></i>
                发票信息
              </template>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="viewInvoice"
              >查看发票信息</el-button>
            </el-descriptions-item>
          </el-descriptions>

          <el-row :gutter="10" class="review-row" v-if="reviewForm.isReviewed <= 1">
            <el-col :span="4" :offset="8">
              <el-button type="primary" @click="handleReview(true)"> 通过 </el-button>
            </el-col>
            <el-col :span="4" :offset="2">
              <el-button type="danger" @click="handleReview(false)">不通过</el-button>
            </el-col>
          </el-row>
        </el-dialog>
      </el-col>
    </el-row>
    <el-dialog :title="pdfTitle" :visible.sync="showPdf"  width="900px" append-to-body>
      <iframe
        :src="pdfUrl"
        style="width:100%; height:600px;"
        frameborder="0"
      ></iframe>
    </el-dialog>
  </div>
</template>
<script>
import Car_sort from "@/views/shop/shop/car_sort.vue";
import request from "@/utils/request";
import {formatDate} from "@/utils/car/date";
import ShowRecord from "@/views/record/record/show_record.vue";

export default {
  name: "review",
  components: {ShowRecord, Car_sort},
  data() {
    return {
      pdfTitle: '',
      pdfUrl: '#',
      showPdf: false,
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
      recordId: null,
      record:{},
      total: 0,
      showReview: false,
      pdfContent: ''
    }
  },
  mounted() {
    this.getListAll();
  },
  methods: {
    formatDate,
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
        url: "/car/listAll",
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
    getList(){
      let start = (this.queryParams.pageNum - 1) * this.queryParams.pageSize;
      let end = this.queryParams.pageNum * this.queryParams.pageSize;
      this.carList = this.carListAll.slice(start, end);
    },
    getUrlList(imgs){
      console.log(imgs, 'imgs');
      let urls = [];
      imgs.forEach(item => {
        urls.push(item.url);
      })
      return urls;
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
    handleReview(sign){
      let params = {carId: this.reviewForm.carId};
      if (sign) {
        params.isReviewed = 1;
      } else {
        params.isReviewed = 2;
      }

      request({
        url: "/car/review",
        method: "POST",
        data: params
      }).then(res => {
        this.showReview = false;
        this.getListAll();
      })
    },
    viewReview(row){
      request({
        url: "/review/" + row.reviewId,
        method: "GET"
      }).then( res => {
        this.reviewForm = {...res.data, user: {}}
        request({
          url: "/car/" + row.carId,
          method: "GET"
        }).then( res => {
          this.reviewForm = {
            ...this.reviewForm,
            ...res.data
          }
          console.log('row.recordId', row.recordId)
          this.recordId = row.recordId
          this.showReview = true;
        })

      })
    },
    viewInsurance() {
      // 下载row.data.insurance
      let insurance = this.reviewForm.insurance.replace(/nas\.com/, "wolves.vip");
      this.pdfUrl = `https://docs.google.com/gview?url=${insurance}&embedded=true`
      this.pdfTitle = '保险单据'
      this.showPdf = true;
    },
    viewInvoice() {
      // 下载row.data.insurance
      let invoice = this.reviewForm.invoice.replace(/nas\.com/, "wolves.vip");
      this.pdfUrl = `https://docs.google.com/gview?url=${invoice}&embedded=true`
      this.pdfTitle = '发票信息'
      this.showPdf = true;
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
.review-image{
  width: 200px;
  height: 200px;
}
.review-multi-image{
  width: 80px;
  height: 80px;
}

.review-row {
  margin-top: 20px;
}
</style>
