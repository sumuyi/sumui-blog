package com.sumui.service;

import com.sumui.common.model.finance.Books;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author flk-sunl
* @description 针对表【books(账本表)】的数据库操作Service
* @createDate 2025-02-21 10:21:17
*/
public interface BooksService extends IService<Books> {

    void addUserToBook(String userId);

    List<Books> getBooksByUserId(String userId);
}
