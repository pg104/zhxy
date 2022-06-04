package com.pg.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pg.entity.Student;
import com.pg.service.StudentService;
import com.pg.util.MD5;
import com.pg.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "学生控制器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @ApiOperation("删除单个或多个学生信息")
    @DeleteMapping("/delStudentById")
    public Result deleteStudentById(@RequestBody List<Integer> ids){
        studentService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("新增或修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@RequestBody Student student){
        Integer id = student.getId();
        if (id == null || id == 0){
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @ApiOperation("分页带条件查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(@PathVariable("pageNo") Integer pageNo,
                                  @PathVariable("pageSize")Integer pageSize,
                                  Student student){
        Page<Student> page = new Page<>(pageNo,pageSize);
        IPage<Student> iPage = studentService.getStudentByOpr(page,student);
        return Result.ok(iPage);
    }
}
