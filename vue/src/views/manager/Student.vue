<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 330px; margin-right: 10px" v-model="data.username" placeholder="Search by username" :prefix-icon="Search"/>
      <el-input style="width: 330px; margin-right: 10px" v-model="data.name" placeholder="Search by name" :prefix-icon="Search"/>
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
          <el-table-column prop="name" label="Student Name"/>
          <el-table-column prop="username" label="Student Username"/>
          <el-table-column prop="phone" label="Phone"/>
          <el-table-column prop="email" label="Email"/>
          <el-table-column prop="sex" label="Sex"/>
          <el-table-column prop="birthday" label="Birthday"/>
          <el-table-column prop="avatar" label="Avatar">
          <template #default="scope">
            <el-image v-if="scope.row.avatar" :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" style="width:40px; border-radius: 5px"> </el-image>
          </template>
          </el-table-column>
            <el-table-column label="Action" width="180">
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

    <el-dialog width="45%" v-model="data.formVisible" title="Student Info" destory-on-close="true"> <!--用于清空cache-->
      <el-form :model="data.form" :rules="rules" ref="formRef" label-width="100px" label-position="right">
        <el-form-item label="Username" prop="username" :label-width="formLabelWidth">
          <el-input v-model="data.form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Password" prop="password" :label-width="formLabelWidth">
          <el-input showpassword v-model="data.form.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Name" :label-width="formLabelWidth">
          <el-input v-model="data.form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Phone" :label-width="formLabelWidth">
          <el-input v-model="data.form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Email" :label-width="formLabelWidth">
          <el-input v-model="data.form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Sex" >
          <el-radio-group v-model="data.form.sex">
            <el-radio label="Male"></el-radio>
            <el-radio label="Female"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Birthday">
          <el-date-picker style="width: 100%" format="YYYY-MM-DD" value-format="YYYY-MM-DD" v-model="data.form.birthday"></el-date-picker>
        </el-form-item>
        <el-form-item label="Avatar">
          <el-upload action="http://localhost:9090/files/upload" list-type="picture" :on-success="handleImgUploadSuccess"> <!--url for upload files-->
            <el-button type="primary">Upload Avatar</el-button>
          </el-upload>
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
import { ref, reactive } from "vue"
import { Search} from '@element-plus/icons-vue'
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";


const rules = reactive({
  username: [
    { required: true, message: 'Please input username', trigger: 'blur' },
  ],
  password: [
    { required: true, message: 'Please input password', trigger: 'blur' },
  ],
})

const formRef = ref()

const data = reactive({
 username:'',
  name:'',
  password:'',
  phone:'',
  sex:'',
  birthday:'',
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
    request.get(baseURL+'/selectPage', {
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      username: data.username,
      name: data.name
    }
  }).then(res =>{
    data.tableData = res.data?.list || [] //the latter one means empty; ? prevents err when the res is null
    data.total = res.data?.total || 0
  })
}
//调用方法获取后台数据
load()

const reset =() =>{
  data.username = ''
  data.name = ''
  load()
}

const handleCurrentChange = (pageNum) =>{
  //翻页时重新加载数据
  load()
}



const handleAdd =() =>{
  data.form= {} //first we empty the data in the form
  data.formVisible=true //show the form
}

const baseURL = '/student'
const save =() =>{
  formRef.value.validate((valid) =>{
    if(valid){
      request.request({
        url: data.form.id ? baseURL+'/update' : baseURL+'/add',
        method: data.form.id ? 'PUT' : 'POST',
        data:data.form
      }).then(res=>{ //pass in the data in the form
        if(res.code ==='200'){
          load()//get the latest data
          data.formVisible=false
          ElMessage.success("Success!")
        }
        else{
          ElMessage.error(res.msg)
          console.log(formRef)
        }
      })
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
    request.delete(baseURL+'/delete/'+id).then(res =>{
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


const handleImgUploadSuccess = (res) =>{
  data.form.avatar=res.data;
}


</script>