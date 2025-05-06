<template>
<div>
<div class="login-container">
  <div style="width: 350px" class="login-box">
    <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px">Login</div>
  <el-form :model="data.form" ref="formRef" :rules="rules">
    <el-form-item prop="username">
      <el-input prefix-icon="User" v-model="data.form.username" placeholder="Your username"/>
    </el-form-item>
    <el-form-item prop="password">
      <el-input show-password prefix-icon="Lock" v-model="data.form.password" placeholder="Your password"/>
    </el-form-item>
    <el-form-item prop="role">
      <el-select style="width: 100%" placeholder="Your role" v-model="data.form.role">
        <el-option value="ADMIN" label="Admin"></el-option>
        <el-option value="STUDENT" label="Student"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" style="width: 100%" @click="login">Submit</el-button>
    </el-form-item>
    </el-form>
    <div style="margin-top: 30px; text-align: right; font-size: 16px">
      Creat a new account <a href="/register"> here</a>
    </div>
</div>
</div>
</div>>
</template>

<script setup>
import {reactive, ref} from "vue"
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";

const data = reactive( {
  form:{ role:'STUDENT'} //default role
})

const rules = reactive({
  username: [
    { required: true, message: 'Please input username', trigger: 'blur' },
  ],
  password: [
    { required: true, message: 'Please input password', trigger: 'blur' },
  ],
})

const formRef = ref()

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          localStorage.setItem('student-user', JSON.stringify(res.data))
          ElMessage.success('Login success!')
          location.href = '/home'  // 跳转到主页
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  overflow: hidden;
  display: flex;
  align-items:center;
  justify-content:center;
  background-image: url("@/assets/imgs/bg img.png");
  background-size: cover;
}
.login-box {
  background-color: rgba(255,255,255,0.85);
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
  border: 1px solid #dddddd;
  width: 100%;
  padding: 30px;
  border-radius: 5px;
}

</style>