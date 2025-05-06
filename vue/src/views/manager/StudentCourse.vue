<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 330px; margin-right: 10px" v-model="data.keyword" placeholder="Search by course name/number" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left: 10px" @click="load">Search</el-button>
      <el-button type="info" @click="reset">Reset</el-button>
    </div>

    <div class="card">
      <div>
        <el-table :data="data.tableData" style="width: 100%">
          <el-table-column prop="id" label="ID" width="70px"/>
          <el-table-column prop="name" label="Course Name"/>
          <el-table-column prop="no" label="Course Code"/>
          <el-table-column prop="studentName" label="Student Name"/>

          <el-table-column>
            <template #default="scope">
              <el-button type="danger" @click="deleteById(scope.row.id)">Drop</el-button>
              <el-button type="primary" v-if="data.user.role==='ADMIN'" @click="addGrade(scope.row)">Grade</el-button>
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

    <el-dialog width="35%" v-model="data.formVisible" title="Grade">
      <el-form :model="data.gradeForm" label-width="100px" label-position="right">
        <el-form-item label="Course Name" :label-width="formLabelWidth">
          <el-input v-model="data.gradeForm.name" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="Grade" :label-width="formLabelWidth">
          <el-input v-model="data.gradeForm.score" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Comment" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="data.gradeForm.comment" autocomplete="off"></el-input>
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
 keyword:'',
  tableData:[
  ],
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
    name: data.keyword,
    no: data.keyword,
  }
  if(data.user.role === 'STUDENT'){ //If student, query their own course records
    params.studentId = data.user.id
  }
    request.get("/studentCourse/selectPage", {
    params:params
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

const deleteById = (id) =>{
    ElMessageBox.confirm("Can't restore data after delete. Continue?", "Delete Confirm",{type:'warning',confirmButtonText: 'Yes',
      cancelButtonText: 'Cancel',}).then(res=>{
      request.delete('/studentCourse/delete/'+id).then(res =>{
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

  const addGrade = (row) =>{
    data.formVisible = true
    data.gradeForm.name = row.name //Display course name
    data.gradeForm.courseId = row.courseId
    data.gradeForm.studentId = row.studentId
    data.gradeForm.score = row.score
    data.gradeForm.comment = row.comment
}

const save = () =>{
  request.post("/grade/add",data.gradeForm).then(res =>{
   if(res.code==='200'){
     data.formVisible=false
     ElMessage.success("Success!")
   } else{
     ElMessage.error(res.msg)
   }
  })
}


</script>