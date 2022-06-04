package com.pg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pg.entity.LoginForm;
import com.pg.entity.Student;
import com.pg.entity.Teacher;
import com.pg.service.TeacherService;
import com.pg.mapper.TeacherMapper;
import com.pg.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
* @author pg
* @description 针对表【tb_teacher】的数据库操作Service实现
* @createDate 2022-06-02 21:59:54
*/
@Service
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService{

    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Teacher teacher = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    @Override
    public Teacher getTeacherById(Long userId) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        Teacher teacher = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    @Override
    public IPage<Teacher> getTeachersByOpr(Page<Teacher> page, Teacher teacher) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(teacher.getClazzName())){
            queryWrapper.like("clazz_name", teacher.getClazzName());
        }
        if (!StringUtils.isEmpty(teacher.getName())){
            queryWrapper.like("name", teacher.getName());
        }
        queryWrapper.orderByDesc("id");
        Page<Teacher> teacherPage = baseMapper.selectPage(page, queryWrapper);
        return teacherPage;
    }
}




