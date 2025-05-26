<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型">
          <el-option
            v-for="item in types"
            :key="item.id"
            :label="item.tag"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="模型选择">
        <el-select v-model="queryParams.model" placeholder="请选择要使用的模型">
          <template v-for="item in models">
            <el-option :label="item.detail" :value="item.modelId"></el-option>
          </template>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>


    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="recordId" width="60" />
      <el-table-column label="类型" align="center">
        <template slot-scope="scope">
          {{scope.row.type === 0 ? '单次识别' : '批量识别'}}
        </template>
      </el-table-column>
      <el-table-column label="备注名称" align="center" prop="note" width="240"/>
      <el-table-column label="识别结果" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleResults(scope.row)"
          >点击查看</el-button>
        </template>
      </el-table-column>
      <el-table-column label="用户" align="center">
        <template slot-scope="scope">
          <div class="card-avatar">
            <el-avatar :src="baseURL + scope.row.user.avatar" size="small"/>
            <span class="username-span">{{scope.row.user.userName}}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['record:record:remove']"
          >删除</el-button>
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

    <!-- 添加或修改识别记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="${comment}" prop="result">
          <el-input v-model="form.result" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="isDelete">
          <el-input v-model="form.isDelete" placeholder="请输入${comment}" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="''" :visible.sync="openRes" width="80%" append-to-body>
      <div class="record-dialog">
        <div class="defect-title">
          <i class="el-icon-camera-solid"></i>
          <span class="defect-title-span">识别结果</span>
        </div>
        <el-row :gutter="5">
          <div class="img-block" v-for="file in dataRes" :key="file.fileId">
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
      </div>

    </el-dialog>
  </div>
</template>

<script>
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from "@/api/record/record";
import request from "@/utils/request";
import get_type from "@/utils/defect-type";

export default {
  name: "Record",
  data() {
    return {
      baseURL: 'http://localhost' + process.env.VUE_APP_BASE_API,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 识别记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openRes: false,
      dataRes: [],
      types: [{id: 0, tag: '单图识别'}, {id: 1, tag: '多图识别'}, {id: null, tag: '全部'}],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: null,
        result: null,
        isDelete: null,
        model: null,
        note: null
      },
      models: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        type: [
          { required: true, message: "$comment不能为空", trigger: "change" }
        ],
        result: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
        isDelete: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  mounted() {

    request({
      url: "/model/model/list",
      method: "get",
    }).then((res) => {
      this.models = res.rows;
      this.models.push({url: null, modelId: null, detail: '全部模型'});
    })
  },
  methods: {
    /** 查询识别记录列表 */
    getList() {
      this.loading = true;
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows;
        console.log('-----------', response);
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        recordId: null,
        type: null,
        result: null,
        createTime: null,
        updateTime: null,
        isDelete: null,
        note: null
      };
      this.dataRes = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.recordId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加识别记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const recordId = row.recordId || this.ids
      getRecord(recordId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改识别记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.recordId != null) {
            updateRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRecord(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const recordIds = row.recordId || this.ids;
      this.$modal.confirm('是否确认删除识别记录编号为"' + recordIds + '"的数据项？').then(function() {
        return delRecord(recordIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('record/record/export', {
        ...this.queryParams
      }, `record_${new Date().getTime()}.xlsx`)
    },
    handleResults(row) {
      console.log(row);
      this.reset();
      this.dataRes = row.results;
      this.dataRes.forEach(item => {
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
      this.openRes = true;
    }
  }
};
</script>
<style scoped>
.defect-title{
  width: 100%;
  height: 10%;
  text-align: center;
  padding-bottom: 20px;
  font-size: 48px;
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei";
}
.defect-img{
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin: auto;

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
.card-avatar{
  font-size: 17px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
