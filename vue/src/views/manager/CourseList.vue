<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 330px; margin-right: 10px" v-model="data.keyword" placeholder="Search by course name/number/instructor" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left: 10px" @click="load">Search</el-button>
      <el-button type="info" @click="reset">Reset</el-button>
    </div>

    <div class="card">
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
              <el-button type="primary" @click="selectCourse(scope.row)">Enroll</el-button>
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
  user:JSON.parse(localStorage.getItem('student-user')||'{}')
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

const selectCourse = (row) =>{
  request.post('/studentCourse/add',{studentId: data.user.id, name:row.name, no:row.no,courseId:row.id}).then(res =>{
    if(res.code==='200'){
      ElMessage.success("Success!")
    }
    else {
      ElMessage.error(res.msg);
    }
  })
}

</script>