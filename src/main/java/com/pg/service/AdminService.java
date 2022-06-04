package com.pg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pg.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pg.entity.LoginForm;

/**
* @author pg
* @description 针对表【tb_admin】的数据库操作Service
* @createDate 2022-06-02 21:58:15
*/
public interface AdminService extends IService<Admin> {
    public Admin login(LoginForm loginForm);

    Admin getAdminById(Long userId);

    IPage<Admin> getAllAdmin(Page<Admin> page, String adminName);
}
