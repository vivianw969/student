<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 330px; margin-right: 10px" v-model="data.keyword" placeholder="Search by course name/number/instructor" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left: 10px" @click="load">Search</el-button>
      <el-button type="info" @click="reset">Reset</el-button>
    </div>

    <div class="card">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">New</el-button>
      </div>
      <div>
        <el-table :data="data.tableData" style="width: 100%">
          <el-table-column prop="id" label="ID" width="70px"/>
          <el-table-column prop="name" label="Course Name"/>
          <el-table-column prop="no" label="Course Code"/>
          <el-table-column prop="descr" label="Course Description"/>
          <el-table-column prop="length" label="Course Length"/>
          <el-table-column prop="teacher" label="Instructor"/>
          <el-table-column>
            <template #default="scope">
              <!-- scope.row helps define the target we are editing on -->
              <el-button type="primary" plain size="small" @click="handleEdit(scope.row)">Edit</el-button>
              <el-button type="danger" plain size="small" @click="handleDelete(scope.row.id)">Delete</el-button>
            </template>
          </el-table-column>

        </el-table>
      </div>
    </div>
    <div class="card">
      <el-pagination v-model:current-page="data.pageNum"
                     v-model:page-size="data.pageSize"
                     @current-change="handleCurrentChange"
                     background layout="prev,pager,next"
                     :total="data.total"/>
    </div>

    <el-dialog width="45%" v-model="data.formVisible" title="Edit Course Info">
      <el-form :model="data.form" label-width="100px" label-position="right">
        <el-form-item label="Course Name" :label-width="formLabelWidth">
          <el-input v-model="data.form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Course Code" :label-width="formLabelWidth">
          <el-input v-model="data.form.no" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Description" :label-width="formLabelWidth">
          <el-input v-model="data.form.descr" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Length" :label-width="formLabelWidth">
          <el-input v-model="data.form.length" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Instructor" :label-width="formLabelWidth">
          <el-input v-model="data.form.teacher" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">Cancel</el-button>
          <el-button type="primary" @click="save">Save</el-button>
        </div>
      </template>

    </el-dialog>

  </div>
</template>

<script setup>
import { reactive } from "vue"
import { Search} from '@element-plus/icons-vue'
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
 keyword:'',
  tableData:[
  ],
  total:0,
  pageNum:1, //current page number
  pageSize:5,
  formVisible: false,
  form: {}
})

//empty parentheses means it doesn't take any parameters
const load =() => {
    request.get("/course/selectPage", {
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      name: data.keyword,
      no: data.keyword,
      teacher: data.keyword
    }
  }).then(res =>{
    data.tableData = res.data?.list || [] //the latter one means empty; ? prevents err when the res is null
    data.total = res.data?.total || 0
  })
}
//Call method to get backend data
load()

const reset =() =>{
  data.keyword = ''
  load()
}

const handleCurrentChange = (pageNum) =>{
  //Reload data when changing pages
  load()
}

const handleAdd =() =>{
  data.form= {} //first we empty the data in the form
  data.formVisible=true //show the form
}

const save =()=>{
request.request({
  url: data.form.id ? '/course/update' : '/course/add',
  method: data.form.id ? 'PUT' : 'POST',
  data:data.form
}).then(res=>{ //pass in the data in the form
  if(res.code ==='200'){
    load()//get the latest data
    data.formVisible=false
    ElMessage.success("Operation successful")
  }
  else{
    ElMessage.error(res.msg)
  }
})
}

const handleEdit =(row) =>{
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible=true
}

const handleDelete =(id) =>{
  ElMessageBox.confirm("Can't restore data after delete. Continue?", "Delete Confirm",{type:'warning',confirmButtonText: 'Yes',
    cancelButtonText: 'Cancel',}).then(res=>{
    request.delete('/course/delete/'+id).then(res =>{
      if(res.code ==='200'){
        load()//get the latest data
        ElMessage.success("Operation successful")
      }
      else{
        ElMessage.error(res.msg)
      }
    })
  }).catch(res=>{
  ElMessage({
    type:'info',
    message:'Delete Canceled'
  })
  })
}

</script>