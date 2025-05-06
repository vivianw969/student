package com.example.mapper;


import com.example.entity.Grade;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface GradeMapper {
@Insert("INSERT INTO grade (course_id,student_id,score,comment,feedback) " +
        "VALUES (#{courseId},#{studentId},#{score},#{comment},#{feedback})")
    void insert(Grade grade);

    List<Grade> selectAll(Grade grade); //需要关联查询 所以在xml里创建

    @Update("UPDATE grade SET score=#{score}, comment=#{comment}, feedback =#{feedback} WHERE id =#{id}")
    void update(Grade grade);


    @Select("SELECT * FROM grade WHERE student_id = #{studentId} and course_id =#{courseId}")
    Grade selectByCondition(Grade grade);

    @Delete("DELETE FROM grade where id= #{id}")
    void deleteById(Integer id);
}
