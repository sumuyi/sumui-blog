package com.sumui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sumui.common.model.finance.Tags;
import com.sumui.dao.mapper.finance.TagsMapper;
import com.sumui.service.TagsService;
import org.springframework.stereotype.Service;

/**
* @author flk-sunl
* @description 针对表【tags(标签表)】的数据库操作Service实现
* @createDate 2025-02-21 10:21:17
*/
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements TagsService{

}




