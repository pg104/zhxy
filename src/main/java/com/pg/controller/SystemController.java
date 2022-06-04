package com.pg.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pg.entity.Admin;
import com.pg.entity.LoginForm;
import com.pg.entity.Student;
import com.pg.entity.Teacher;
import com.pg.service.AdminService;
import com.pg.service.StudentService;
import com.pg.service.TeacherService;
import com.pg.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Api(tags = "系统控制器")
@RestController
@RequestMapping("/sms/system")
@Slf4j
public class SystemController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("更新用户密码的控制器")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(@PathVariable("oldPwd") String oldPwd,
                            @PathVariable("newPwd") String newPwd,
                            @RequestHeader String token){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            //token过期
            return Result.fail().message("token已失效，请重新登录后修改密码");
        }
        //获取用户ID和类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);
        switch (userType){
            case 1:
                QueryWrapper<Admin> queryWrapper1 = new QueryWrapper();
                queryWrapper1.eq("id", userId);
                queryWrapper1.eq("password", oldPwd);
                Admin admin = adminService.getOne(queryWrapper1);
                if (admin != null){
                    //修改
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                } else {
                    return Result.fail().message("原密码有误");
                }
                break;
            case 2:
                QueryWrapper<Student> queryWrapper2 = new QueryWrapper();
                queryWrapper2.eq("id", userId);
                queryWrapper2.eq("password", oldPwd);
                Student student = studentService.getOne(queryWrapper2);
                if (student != null){
                    //修改
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                } else {
                    return Result.fail().message("原密码有误");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapper3 = new QueryWrapper();
                queryWrapper3.eq("id", userId);
                queryWrapper3.eq("password", oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapper3);
                if (teacher != null){
                    //修改
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                } else {
                    return Result.fail().message("原密码有误");
                }
                break;
        }
        return Result.ok();
    }

    @ApiOperation("文件上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(@RequestPart("multipartFile") MultipartFile multipartFile){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        String lastFileName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = uuid + lastFileName;
        //保存文件 将文件发送到第三方/独立的图片服务器上
        String realPath = "/Users/pg/IdeaProjects/zhxy/target/classes/public/upload/" + newFileName;
        try {
            multipartFile.transferTo(new File(realPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //响应图片的路径
        String path = "upload/" + newFileName;
        return Result.ok(path);
    }


    @ApiOperation("通过token口令获取当前登录的用户的信息")
    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        //从token中解析出用户id和用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        Map<String,Object> map = new HashMap<>();
        switch (userType){
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType", 1);
                map.put("user", admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType", 2);
                map.put("user", student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType", 3);
                map.put("user", teacher);
                break;
        }
        return Result.ok(map);
    }

    @ApiOperation("登录的方法")
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request){
        //验证码校验
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if ("".equals(sessionVerifiCode) || sessionVerifiCode == null){
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        if (!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)){
            return Result.fail().message("验证码有误，请小心输入后重试");
        }
        //冲session域中移除现有验证码
        session.removeAttribute("verifiCode");
        //分用户类型进行校验

        //准备一个map存放用户响应的数据
        Map<String,Object> map = new HashMap<>();
        switch (loginForm.getUserType()){
            case 1:
                Admin admin = adminService.login(loginForm);
                if (admin != null){
                    //将用户类型和用户id转换成一个密文，以token的名称向客户端反馈
                    String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                    map.put("token", token);
                } else {
                    log.info("用户名或密码有误");
                    return Result.fail().message("用户名或密码有误");
                }
                return Result.ok(map);
            case 2:
                Student student = studentService.login(loginForm);
                if (student != null){
                    //将用户类型和用户id转换成一个密文，以token的名称向客户端反馈
                    String token = JwtHelper.createToken(student.getId().longValue(), 2);
                    map.put("token", token);
                } else {
                    log.info("用户名或密码有误");
                    return Result.fail().message("用户名或密码有误");
                }
                return Result.ok(map);
            case 3:
                Teacher teacher = teacherService.login(loginForm);
                if (teacher != null){
                    //将用户类型和用户id转换成一个密文，以token的名称向客户端反馈
                    String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                    map.put("token", token);
                } else {
                    log.info("用户名或密码有误");
                    return Result.fail().message("用户名或密码有误");
                }
                return Result.ok(map);
        }
        return Result.fail().message("查无此用户");
    }

    @ApiOperation("获取验证码图片")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证码文本放入session域，为下一次验证作准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode",verifiCode);
        //将验证码图片响应给浏览器
        try {
            ImageIO.write(verifiCodeImage,"JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
