package com.example.service;

import com.example.common.Result;
import com.example.entity.Grade;
import com.example.exception.CustomException;
import com.example.mapper.GradeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GradeService {

  @Resource
   GradeMapper gradeMapper;

   public void add(Grade grade){
       Grade dbgrade = gradeMapper.selectByCondition(grade);
       if(dbgrade!=null){
      //已有分数
      throw new CustomException("Graded already!");
       }
       gradeMapper.insert(grade);

   }

    public PageInfo<Grade> selectPage(Integer pageNum, Integer pageSize, Grade grade) {
        PageHelper.startPage(pageNum,pageSize);
        List<Grade> list  = gradeMapper.selectAll(grade);
        return PageInfo.of(list);
    }

    public void update(Grade grade) {
       gradeMapper.update(grade);
    }

    public void deleteById(Integer id) {
       gradeMapper.deleteById(id);
    }
}
