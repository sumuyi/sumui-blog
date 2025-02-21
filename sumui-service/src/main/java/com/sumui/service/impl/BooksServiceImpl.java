package com.sumui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sumui.common.model.finance.Books;
import com.sumui.dao.mapper.finance.BooksMapper;
import com.sumui.service.BooksService;
import org.springframework.stereotype.Service;

/**
* @author flk-sunl
* @description 针对表【books(账本表)】的数据库操作Service实现
* @createDate 2025-02-21 10:21:17
*/
@Service
public class BooksServiceImpl extends ServiceImpl<BooksMapper, Books> implements BooksService{

}




