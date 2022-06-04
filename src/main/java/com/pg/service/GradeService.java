package com.pg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pg.entity.Grade;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author pg
* @description 针对表【tb_grade】的数据库操作Service
* @createDate 2022-06-02 21:59:42
*/
public interface GradeService extends IService<Grade> {

    IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName);
}
