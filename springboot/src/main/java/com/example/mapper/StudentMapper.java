package com.example.mapper;


import com.example.entity.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface StudentMapper {

    @Select("SELECT * FROM student WHERE username = #{username}")
    Student selectByUsername(String username);

    @Insert("INSERT INTO student (username,password,name,phone,email,sex,birthday,avatar,role)" +
            "values (#{username},#{password},#{name},#{phone},#{email},#{sex},#{birthday},#{avatar},#{role})")
    void insert(Student student);


    @Delete("DELETE FROM student where id=#{id}")
    void deleteById(Integer id);

    @Update("UPDATE student SET username = #{username}, password = #{password}, name = #{name}, " +
            "email = #{email}, phone = #{phone}, sex = #{sex}, birthday = #{birthday}, avatar = #{avatar} " +
            "WHERE id = #{id}")
    void updateById(Student student);


    @Select("SELECT * FROM student WHERE username LIKE concat('%',#{username},'%') AND name LIKE concat('%',#{name},'%') " +
            "ORDER BY id desc")
    List<Student> selectAll(Student student);
}
