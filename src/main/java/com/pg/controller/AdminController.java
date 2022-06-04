package com.pg.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pg.entity.Admin;
import com.pg.service.AdminService;
import com.pg.util.MD5;
import com.pg.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "管理员控制器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("分页带条件查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(@PathVariable("pageNo") Integer pageNo,
                              @PathVariable("pageSize") Integer pageSize,
                              String adminName){
        Page<Admin> page = new Page<>(pageNo,pageSize);
        IPage<Admin> iPage = adminService.getAllAdmin(page,adminName);
        return Result.ok(iPage);
    }

    @ApiOperation("新增或修改管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@RequestBody Admin admin){
        if (admin.getId() == null || admin.getId() == 0){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @ApiOperation("删除单个或多个学生信息")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@RequestBody List<Integer> ids){
        adminService.removeByIds(ids);
        return Result.ok();
    }
}
