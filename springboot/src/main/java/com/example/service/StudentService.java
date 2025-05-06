package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Role;
import com.example.entity.Account;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentService{
    @Resource
    private StudentMapper studentMapper;


    public Account login(Account account){
        Account dbStudent = studentMapper.selectByUsername(account.getUsername());
        if (dbStudent==null){
            //no such user
            throw new CustomException("Wrong username or password");
        }
        //password verification
        if (!account.getPassword().equals(dbStudent.getPassword())){
            throw new CustomException("Wrong password");
        }
        return dbStudent;
    }

    public void register(Account account) {
        Student student= new Student();
        student.setUsername(account.getUsername());
        student.setPassword(account.getPassword());
        this.add(student);
    }

    //新增学生
    public void add(Student student) {
      Student dbStudent=  studentMapper.selectByUsername(student.getUsername());
        if(dbStudent!=null){ //已有同名账号
            throw new CustomException("Account already exists!");
        }
        if(ObjectUtil.isEmpty(student.getName())){
            student.setName(student.getName());
        }
        student.setRole(Role.STUDENT.name());
        studentMapper.insert(student);
    }

    public void deleteById(Integer id) {
        studentMapper.deleteById(id);
    }

    public void updateById(Student student) {
        studentMapper.updateById(student);
    }


    public PageInfo<Student> selectPage(Integer pageNum, Integer pageSize, Student student) {
        PageHelper.startPage(pageNum, pageSize);
        List<Student> studentList = studentMapper.selectAll(student);
        return PageInfo.of(studentList);
    }
}
