package com.pg.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pg.entity.Teacher;
import com.pg.service.TeacherService;
import com.pg.util.MD5;
import com.pg.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "教师控制器")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("分页待条件查询教师信息")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result  getTeachers(@PathVariable("pageNo") Integer pageNo,
                               @PathVariable("pageSize") Integer pageSize,
                               Teacher teacher){
        Page<Teacher> page = new Page<>(pageNo,pageSize);
        IPage<Teacher> iPage = teacherService.getTeachersByOpr(page,teacher);
        return Result.ok(iPage);
    }

    @ApiOperation("添加或修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@RequestBody Teacher teacher){
        if (teacher.getId() ==  null || teacher.getId() == 0){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    @ApiOperation("删除单个或多个教师信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(@RequestBody List<Integer> ids){
        teacherService.removeByIds(ids);
        return Result.ok();
    }


}
