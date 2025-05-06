package com.example.service;


import com.example.entity.StudentCourse;
import com.example.exception.CustomException;
import com.example.mapper.StudentCourseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentCourseService {
    @Resource
    StudentCourseMapper studentCourseMapper;

    public void add(StudentCourse studentCourse){
       StudentCourse course = studentCourseMapper.selectByCondition(studentCourse); //Query by student ID and course ID to check if they have enrolled in this course before
        if(course!=null){
            throw new CustomException("Course enrolled already!");
        }
        studentCourseMapper.insert(studentCourse);
    }

    public PageInfo<StudentCourse> selectPage(Integer pageNum, Integer pageSize, StudentCourse studentCourse) {
        PageHelper.startPage(pageNum,pageSize);
        List<StudentCourse> list = studentCourseMapper.selectAll(studentCourse);
        return PageInfo.of(list);

    }

    public void deleteById(Integer id) {
        studentCourseMapper.deleteById(id);
    }
}
