<template>
  <div class="card" style="width: 50%; padding: 40px 40px">
    <el-form :model="data.form" ref="formRef" label-width="100px" label-position="right">
      <el-form-item label="Avatar">
        <el-upload class="avatar-uploader" action="http://localhost:9090/files/upload" :show-file-list="false"
                   :on-success="handleImgUploadSuccess"> <!--url for upload files-->
          <img v-if="data.form.avatar" :src="data.form.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item label="Username" prop="username" :label-width="formLabelWidth">
        <el-input v-model="data.form.username" autocomplete="off" disabled></el-input>
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
      <el-form-item>
        <el-button type="primary" @click="update">Save</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>


<script setup>
import {reactive} from "vue";
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";


const data = reactive({
  form:JSON.parse(localStorage.getItem('student-user' || "{}"))
})

const handleImgUploadSuccess = (res) =>{
  data.form.avatar = res.data
}

const update= () =>{
  request.put('/student/update',data.form).then(res =>{
    if(res.code=== '200'){
      ElMessage.success("Success!")
      router.push('/login')
    }else{
      ElMessage.error(res.msg)
    }
  })
}


</script>

<style scoped>
.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
}
</style>