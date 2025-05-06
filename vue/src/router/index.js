import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Manager',
      component: () => import('@/views/Manager.vue'),
      redirect: '/home',
      children: [
        { path: 'home', name: 'Home', component: () => import('@/views/manager/Home.vue')},
        { path: 'course', name: 'Course', component: () => import('@/views/manager/Course.vue')},
        { path: 'student', name: 'Student', component: () => import('@/views/manager/Student.vue')},
        { path: 'person', name: 'Person', component: () => import('@/views/manager/Person.vue')},
        { path: 'courseList', name: 'CourseList', component: () => import('@/views/manager/CourseList.vue')},
        { path: 'studentCourse', name: 'StudentCourse', component: () => import('@/views/manager/StudentCourse.vue')},
        { path: 'grade', name: 'Grade', component: () => import('@/views/manager/Grade.vue')},
      ]
    },
    { path: '/login', name: 'Login', component: () => import('@/views/Login.vue'),},
    { path: '/register', name: 'Register', component: () => import('@/views/Register.vue'),},
  ]
})

export default router
