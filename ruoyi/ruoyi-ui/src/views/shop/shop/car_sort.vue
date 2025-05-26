
<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="70px">
      <el-form-item label="厂商">
        <el-select v-model="form.manufacture" placeholder="请选择汽车厂商" @change="updateManufacture">
          <div v-for="(vers, manufacture) in kinds" :key="vers.id">
            <el-option :label="manufacture" :value="manufacture"></el-option>
          </div>
        </el-select>
      </el-form-item>
      <el-form-item label="型号">
        <el-select v-model="form.ver" placeholder="请选择汽车型号">
          <div v-for="( ver , _) in kinds[form.manufacture].vers" :key="ver">
            <el-option :label="ver" :value="ver"></el-option>
          </div>
        </el-select>
      </el-form-item>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>价格（万元）</span>
        </div>
        <el-form-item label="最低" label-width="50px">
          <el-input v-model="form.priceMin" class="range-input" placeholder="输入最低价格"></el-input>
        </el-form-item>
        <el-form-item label="最高" label-width="50px">
          <el-input v-model="form.priceMax" class="range-input" placeholder="输入最高价格"></el-input>
        </el-form-item>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>里程（万公里）</span>
        </div>
        <el-form-item label="最低" label-width="50px">
          <el-input v-model="form.mileageMin" class="range-input" placeholder="输入最低里程"></el-input>
        </el-form-item>
        <el-form-item label="最高" label-width="50px">
          <el-input v-model="form.mileageMax" class="range-input" placeholder="输入最高里程"></el-input>
        </el-form-item>
      </el-card>
      <el-form-item label="商品id" style="margin-top:10px" >
        <el-input v-model="form.carId" class="keyword-input" placeholder="输入商品id"></el-input>
      </el-form-item>
      <el-form-item label="上架日期" style="margin-top:10px">
        <el-radio-group v-model="date_limit" @input="changeCreateTime">
          <el-radio :label="0">全部</el-radio>
          <el-radio :label="1">今日内</el-radio>
          <el-radio :label="2">昨日前</el-radio>
          <el-radio :label="3">本周内</el-radio>
          <el-radio :label="4">本月内</el-radio>
        </el-radio-group>
      </el-form-item>
<!--      <el-form-item label="关键词">-->
<!--        <el-input v-model="form.keyword" class="keyword-input" placeholder="输入关键词"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import kinds from '@/utils/car/kinds'
import request from "@/utils/request";
export default {
  name: "car_sort",
  data() {
    let price_limit = 1000
    let mileage_limit = 50
    return {
      form: {
        manufacture: '全部',
        ver: '全部',
        priceMin:0,
        priceMax: 100,
        mileageMin:0,
        mileageMax:20,
        createTime: '',
        keyword: ''
      },
      price_limit,
      mileage_limit,
      kinds,
      kind:{},
      date_limit: 0,
      cars: []
    }
  },
  methods: {
    onSubmit() {
      this.updateSort()
    },
    updateSort() {
      console.log('form_sort', {
        ...this.form,
        priceMin: this.form.priceMin * 10000,
        priceMax: this.form.priceMax * 10000,
        mileageMin: this.form.mileageMin * 10000,
        mileageMax: this.form.mileageMax * 10000,
      })
      this.$emit('sort', {
        ...this.form,
        priceMin: this.form.priceMin * 10000,
        priceMax: this.form.priceMax * 10000,
        mileageMin: this.form.mileageMin * 10000,
        mileageMax: this.form.mileageMax * 10000,
      })
    },
    changeCreateTime(val) {
      var date = new Date();
      date.setHours(0, 0, 0, 0)
      switch (val) {
        case 0: {
          this.form.createTime = ''
          break;
        }
        case 1: {
          this.form.createTime = date;
          break;
        }
        case 2: {
          date.setDate(date.getDate() - 1);
          this.form.createTime = date;
          break;
        }
        case 3: {
          date.setDate(date.getDate() - 7);
          this.form.createTime = date;
          break;
        }
        case 4: {
          date.setDate(date.getDate() - 30);
          this.form.createTime = date;
          break;
        }
        default: {
          this.form.createTime = ''
          break;
        }

      }
    },
    updateManufacture() {
      this.form.ver = '全部'
    },
  }
}
</script>
<style scoped>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}
.slider{
  width: 30px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
.clearfix{
  font-size: 15px;
}
.keyword-input {
  width: 100px;
}
.range-input{
  width: 120px;
}
</style>
