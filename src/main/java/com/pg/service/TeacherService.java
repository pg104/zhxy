package com.pg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pg.entity.LoginForm;
import com.pg.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author pg
* @description 针对表【tb_teacher】的数据库操作Service
* @createDate 2022-06-02 21:59:54
*/
public interface TeacherService extends IService<Teacher> {

    public Teacher login(LoginForm loginForm);

    Teacher getTeacherById(Long userId);

    IPage<Teacher> getTeachersByOpr(Page<Teacher> page, Teacher teacher);
}
