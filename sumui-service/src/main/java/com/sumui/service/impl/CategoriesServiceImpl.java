package com.sumui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sumui.common.model.finance.Categories;
import com.sumui.dao.mapper.finance.CategoriesMapper;
import com.sumui.service.CategoriesService;
import org.springframework.stereotype.Service;

/**
* @author flk-sunl
* @description 针对表【categories(账单分类表)】的数据库操作Service实现
* @createDate 2025-02-21 10:21:17
*/
@Service
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories> implements CategoriesService{

}




