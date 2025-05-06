package com.example.controller;

import com.example.annotation.WebLog;
import com.example.common.Result;
import com.example.entity.Course;
import com.example.service.CourseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    //Pagination query
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Course course){
        PageInfo<Course> pageInfo = courseService.selectPage(pageNum, pageSize,course);
        return Result.success(pageInfo);
    }

    //Add
    @PostMapping("/add")
    public Result add(@RequestBody Course course){
        courseService.add(course);
        return Result.success();
    }

    //Update
@PutMapping("/update")
public Result update(@RequestBody Course course){
    courseService.updateById(course);
    return Result.success();
}

//Delete
    @DeleteMapping("/delete/{id}")
    @WebLog
    public Result delete(@PathVariable Integer id){
        courseService.deleteById(id);
        return Result.success();
    }
}
