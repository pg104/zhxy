package com.pg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pg.entity.Clazz;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author pg
* @description 针对表【tb_clazz】的数据库操作Service
* @createDate 2022-06-02 21:59:36
*/
public interface ClazzService extends IService<Clazz> {

    IPage<Clazz> getClazzByOpr(Page<Clazz> page, Clazz clazz);

    List<Clazz> getClazzs();
}
