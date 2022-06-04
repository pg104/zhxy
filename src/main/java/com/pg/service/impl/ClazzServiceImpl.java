package com.pg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pg.entity.Clazz;
import com.pg.service.ClazzService;
import com.pg.mapper.ClazzMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author pg
* @description 针对表【tb_clazz】的数据库操作Service实现
* @createDate 2022-06-02 21:59:36
*/
@Service
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService{

    @Override
    public IPage<Clazz> getClazzByOpr(Page<Clazz> page, Clazz clazz) {
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper();
        String gradeName = clazz.getGradeName();
        if (!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("grade_name", gradeName);
        }
        String name = clazz.getName();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");
        Page<Clazz> clazzPage = baseMapper.selectPage(page, queryWrapper);
        return clazzPage;
    }

    @Override
    public List<Clazz> getClazzs() {
        return baseMapper.selectList(null);
    }
}




