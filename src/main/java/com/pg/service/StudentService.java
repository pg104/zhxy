package com.pg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pg.entity.LoginForm;
import com.pg.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author pg
* @description 针对表【tb_student】的数据库操作Service
* @createDate 2022-06-02 21:59:49
*/
public interface StudentService extends IService<Student> {

    public Student login(LoginForm loginForm);

    Student getStudentById(Long userId);

    IPage<Student> getStudentByOpr(Page<Student> page, Student student);
}
