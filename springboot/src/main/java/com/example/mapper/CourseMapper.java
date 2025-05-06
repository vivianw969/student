package com.example.mapper;

import com.example.entity.Course;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CourseMapper {
    @Select("SELECT * FROM course " +
            "WHERE (name LIKE concat('%',#{name},'%') OR " +
            "no LIKE concat('%',#{no},'%') OR " +
            "teacher LIKE concat('%',#{teacher},'%')) " +
            "ORDER BY id")
    List<Course> selectAll(Course course);

    @Insert("INSERT INTO course(name,no,descr,length,teacher) values (#{name},#{no},#{descr},#{length},#{teacher})")
    void insert(Course course);

    @Update("UPDATE course SET name= #{name}, no =#{no}, descr =#{descr}, length=#{length}, teacher=#{teacher} " +
           "WHERE id = #{id}")
    void updateById(Course course);

    @Delete("DELETE from course where id = #{id}")
    void deleteById(Integer id);
}
