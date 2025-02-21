package com.sumui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sumui.common.model.finance.UserBooks;
import com.sumui.dao.mapper.finance.UserBooksMapper;
import com.sumui.service.UserBooksService;
import org.springframework.stereotype.Service;

/**
* @author flk-sunl
* @description 针对表【user_books(用户账本关联表)】的数据库操作Service实现
* @createDate 2025-02-21 10:21:17
*/
@Service
public class UserBooksServiceImpl extends ServiceImpl<UserBooksMapper, UserBooks> implements UserBooksService{

}




