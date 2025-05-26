
<template>
  <div class="app-container">
    <el-link type="primary" @click="viewRecord">{{record.note}}</el-link>
    <el-dialog :title="''" :visible.sync="showRecord" width="80%" append-to-body>
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
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import get_type from "@/utils/defect-type";

export default {
  name: "show_record",
  data() {
    return {
      showRecord: false,
      record: {}
    }
  },
  props: {
    recordId: {
      type: Number,
      default: null
    }
  },
  watch: {
    recordId: {
      handler(newRecordId) {
        console.log(newRecordId, 'newRecordId');
        if (newRecordId) {
          this.getNewRecord(newRecordId);
        }
      },
      immediate: true
    }
  },
  methods: {
    viewRecord() {
      this.showRecord = true;
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
<style scoped lang="scss">
.defect-title{
  width: 100%;
  height: 10%;
  text-align: center;
  padding-bottom: 20px;
  font-size: 48px;
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
</style>
