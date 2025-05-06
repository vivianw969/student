<template>
  <div>

    <div class="card" style="line-height: 30px">
      <div>Welcome, <span style="color: blue;"> {{ user.name }} </span>！</div>
    </div>

  </div>
</template>

<script setup>
  import request from "@/utils/request";
  const user = JSON.parse(localStorage.getItem('student-user') || '{}')
</script>