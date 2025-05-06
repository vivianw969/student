package com.example.controller;

import com.example.annotation.WebLog;
import com.example.common.Result;
import com.example.entity.Student;
import com.example.service.StudentService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    StudentService studentService;

    //Create new student
    @PostMapping("/add")
    @WebLog
    public Result add(@RequestBody Student student){
        studentService.add(student);
        return Result.success();
    }
    //Delete
    @PostMapping("/delete/{id}")
    @WebLog
    public Result delete(@PathVariable Integer id){
        studentService.deleteById(id);
        return Result.success();
    }

    //Update
    @PutMapping("/update")
    public Result update(@RequestBody Student student){
        studentService.updateById(student);
        return Result.success();
    }

    //Read
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam (defaultValue ="1") Integer pageNum,
                             @RequestParam (defaultValue = "10")Integer pageSize,
                             Student student){
        PageInfo<Student> pageInfo = studentService.selectPage(pageNum,pageSize,student); //分页查询
        return Result.success(pageInfo);
    }
}
