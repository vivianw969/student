<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 330px; margin-right: 10px" v-model="data.courseName" placeholder="Search by course name" :prefix-icon="Search"/>
      <el-input style="width: 330px; margin-right: 10px" v-model="data.studentName" placeholder="Search by student name" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left: 10px" @click="load">Search</el-button>
      <el-button type="info" @click="reset">Reset</el-button>
    </div>

    <div class="card">
      <div>
        <el-table :data="data.tableData" style="width: 100%">
          <el-table-column prop="id" label="ID" width="70px"/>
          <el-table-column prop="courseName" label="Course Name"/>
          <el-table-column prop="studentName" label="Student Name"/>
          <el-table-column prop="score" label="Grade"/>
          <el-table-column prop="comment" label="Comment"/>
          <el-table-column prop="feedback" label="Feedback"/>
          <el-table-column>
            <template #default="scope">
              <div style="display: flex;align-items: center; justify-content: flex-end">
              <el-button style="width: 60px" type="primary" @click="handleEdit(scope.row)" v-if="data.user.role==='ADMIN'">Edit</el-button>
              <el-button style="width: 60px" type="danger"  @click="deleteById(scope.row.id)" v-if="data.user.role==='ADMIN'">Delete</el-button>
              <el-button style="width: 60px" type="primary"  @click="handleEdit(scope.row)" v-if="data.user.role==='STUDENT'">Feedback</el-button>
            </div>
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

    <el-dialog width="45%" v-model="data.formVisible" title="Feedback">
      <el-form :model="data.form" label-width="100px" label-position="right">
        <el-form-item label="Grade" v-if="data.user.role==='ADMIN'">
          <el-input v-model="data.form.score" autocomplete="off" />
        </el-form-item>
        <el-form-item type="textarea" label="Comment" v-if="data.user.role==='ADMIN'">
          <el-input v-model="data.form.comment" autocomplete="off" />
        </el-form-item>
        <el-form-item type="textarea" label="Feedback" v-if="data.user.role==='STUDENT'">
          <el-input v-model="data.form.feedback" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
          <span class="dialog-footer">
            <el-button @click="data.formVisible=false">Cancel</el-button>
            <el-button type="primary" @click="save">Save</el-button>
          </span>
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
  courseName:'',
  studentName:'',
  tableData:[],
  total:0,
  pageNum:1, //current page number
  pageSize:5,
  user:JSON.parse(localStorage.getItem('student-user')||'{}'),
  gradeForm:{},
  formVisible:false
})

//empty parentheses means it doesn't take any parameters
const load =() => {
  let params ={
    pageNum:data.pageNum,
    pageSize:data.pageSize,
    courseName: data.courseName,
    studentName: data.studentName
  }
  if(data.user.role === 'STUDENT'){ //如果是学生 就查询自己的选课记录
    params.studentId = data.user.id
  }
    request.get("/grade/selectPage", {
    params:params
  }).then(res =>{
    data.tableData = res.data?.list || [] //the latter one means empty; ? prevents err when the res is null
    data.total = res.data?.total || 0
  })
}
//调用方法获取后台数据
load()

const reset =() =>{
  data.courseName = ''
  data.studentName = ''
  load()
}

const handleCurrentChange = (pageNum) =>{
  //翻页时重新加载数据
  load()
}

const deleteById = (id) =>{
    ElMessageBox.confirm("Can't restore data after delete. Continue?", "Delete Confirm",{type:'warning',confirmButtonText: 'Yes',
      cancelButtonText: 'Cancel',}).then(res=>{
      request.delete('/grade/delete/'+id).then(res =>{
        if(res.code ==='200'){
          load()//get the latest data
          ElMessage.success("Success!")
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

  const handleEdit = (row) =>{
    data.form = JSON.parse(JSON.stringify(row)) //拷贝行数据到表单对象
    data.formVisible = true
}

const save = () =>{
  request.put("/grade/update",data.form).then(res =>{
   if(res.code==='200'){
     load()
     data.formVisible=false
     ElMessage.success("Success!")
   } else{
     ElMessage.error(res.msg)
   }
  })
}


</script>