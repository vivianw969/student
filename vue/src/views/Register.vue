<template>
<div>
<div class="login-container">
  <div style="width: 350px" class="login-box">
    <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px">Register</div>
  <el-form :model="data.form" ref="formRef" :rules="rules">
    <el-form-item prop="username">
      <el-input prefix-icon="User" v-model="data.form.username" placeholder="Your username"/>
    </el-form-item>
    <el-form-item prop="password">
      <el-input show-password prefix-icon="Lock" v-model="data.form.password" placeholder="Your password"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" style="width: 100%" @click="register">Register</el-button>
    </el-form-item>
    </el-form>
    <div style="margin-top: 30px; text-align: right; font-size: 16px">
      Switch to an existing account <a href="/login"> here</a>
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
  form:{}
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

const register = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('Register success!')
          router.push('/login')
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
  background-image: url("@/assets/imgs/bg2.png");
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