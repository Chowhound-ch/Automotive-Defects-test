
<template>
  <div class="app-container">
    <el-row>
      <el-col :span="6">
        <car_sort @sort="updateCars"/>
      </el-col>
      <el-col :span="18">
        <car_item :carSort="carSort"/>
      </el-col>
    </el-row>
  </div>
</template>


<script>
import Car_sort from "@/views/shop/shop/car_sort.vue";
import Car_item from "@/views/shop/shop/car_item.vue";

export default {
  name: "car",
  components: {Car_item, Car_sort},
  data() {
    return {
      carSort: {}
    }
  },
  methods: {
    updateCars(carSort) {
      this.carSort = carSort;
    }
  }
}
</script>

<style>

</style>
