<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="${comment}" prop="url">-->
<!--        <el-input-->
<!--          v-model="queryParams.url"-->
<!--          placeholder="请输入${comment}"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="${comment}" prop="detail">-->
<!--        <el-input-->
<!--          v-model="queryParams.detail"-->
<!--          placeholder="请输入${comment}"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
<!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
<!--      </el-form-item>-->
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['model:model:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['model:model:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['model:model:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['model:model:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="modelList" @selection-change="handleSelectionChange" :show-overflow-tooltip="true">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="模型id" align="center" prop="modelId" />
      <el-table-column label="模型名" align="center" prop="detail" />
      <el-table-column label="模型文件名" align="center" prop="url" />

      <el-table-column label="模型简介">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="showDetail(scope.row)"
          >点击查看</el-button>
        </template>
      </el-table-column>
      <el-table-column label="链接" align="center" prop="fileUrl" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['model:model:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['model:model:remove']"
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

    <!-- 添加或修改模型管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form
        ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="模型文件">
          <el-upload
            class="pdf-uploader"
            action="#"
            :http-request="uploadModel"
            :limit="1"
            :key="modelFile.length"
            :headers="headers"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :file-list="modelFile">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传pdf文件，且不超过2mb</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="模型文件名" prop="url" >
          <el-input v-model="form.url" placeholder="输入模型文件名" />
        </el-form-item>
        <el-form-item label="模型名" prop="detail">
          <el-input v-model="form.detail" placeholder="输入模型名" />
        </el-form-item>
        <el-form-item label="详细描述" prop="note">
          <el-input class="text-input"
                    type="textarea"
                    v-model="form.note"
                    :autosize="{ minRows: 3, maxRows: 6}"
                    placeholder="请输入消息描述"></el-input>
        </el-form-item>
        <el-form-item label="效果预览" prop="view">
          <div v-html="noteHtml"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'模型名称：' + detail.title" :visible.sync="detail.openModel" width="50%" append-to-body>
      <hr>
      <div v-html="detail.noteHtml"></div>
    </el-dialog>
  </div>
</template>

<script>
import { listModel, getModel, delModel, addModel, updateModel } from "@/api/model/model";
import MarkdownIt from 'markdown-it';
import request from "@/utils/request";

const markdown = new MarkdownIt()

export default {
  name: "Model",
  data() {
    return {
      detail: {
        openModel: false,
        title: '',
        noteHtml: ''
      },
      markdown,
      headers: { "Content-Type": "multipart/form-data" },
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
      // 模型管理表格数据
      modelList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      noteHtml: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        url: null,
        detail: null
      },
      // 表单参数
      form: {note: ''},
      modelFile: [],
      // 表单校验
      rules: {
        url: [
          { required: true, message: "模型文件名不能为空", trigger: "blur" }
        ],
        detail: [
          { required: true, message: "模型名不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    },
    render(str) {
      let des = (str === null  || str === undefined )? "" : str
      console.log(des, typeof des, str, 'ddd')
      this.noteHtml =  markdown.render(des)
    },
    /** 查询模型管理列表 */
    uploadModel(item){
      this.modelFile = []
      this.form.fileUrl = null
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
          this.form.fileUrl = res.data.url;
          this.modelFile.push(res.data)
          this.form.url = item.file.name
        }
      })
    },
    //上传文件的事件
    uploadFile(item, fun){

    },
    showDetail(model){
      this.detail.noteHtml = markdown.render(model.note)
      this.detail.title = model.detail
      this.detail.openModel =true
    },
    getList() {
      this.loading = true;
      listModel(this.queryParams).then(response => {
        this.modelList = response.rows;
        console.log(this.modelList);
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
        modelId: null,
        url: null,
        detail: null
      };
      this.modelFile = [];
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
      this.ids = selection.map(item => item.modelId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加模型管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const modelId = row.modelId || this.ids
      getModel(modelId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改模型管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.modelId != null) {
            updateModel(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addModel(this.form).then(response => {
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
      const modelIds = row.modelId || this.ids;
      this.$modal.confirm('是否确认删除模型管理编号为"' + modelIds + '"的数据项？').then(function() {
        return delModel(modelIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('model/model/export', {
        ...this.queryParams
      }, `model_${new Date().getTime()}.xlsx`)
    }
  },
  watch: {
    // 监听搜索条件
    'form.note': {
      handler(newVal, oldVal) {
        this.render(newVal)
      },
    }
  }
};
</script>
