package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.common.Role;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.Student;
import com.example.service.AdminService;
import com.example.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class WebController {

    @Resource
    private AdminService adminService;

    @Resource
    private StudentService studentService;

    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {

        return Result.success();
    }

    /**
     * Admin&student登陆接口
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account){
        Account dbAccount;
        if(Role.ADMIN.name().equals(account.getRole())){ //admin log-in
            dbAccount = adminService.login(account);
        }
        else if(Role.STUDENT.name().equals(account.getRole())){ //student log-in
           dbAccount = studentService.login(account);
        }
        else{
            return Result.error("Wrong Role!");
        }
       return Result.success(dbAccount);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Account account){
        if(ObjectUtil.isEmpty(account.getUsername())||ObjectUtil.isEmpty(account.getPassword())){
            return Result.error("Must have username or password!");
        }
        studentService.register(account);
        return Result.success();
    }


}
