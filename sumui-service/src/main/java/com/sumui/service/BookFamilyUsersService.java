package com.sumui.service;

import com.sumui.common.model.dto.BookFamilyUsersDTO;
import com.sumui.common.model.finance.BookFamilyUsers;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author flk-sunl
* @description 针对表【book_family_users(家庭账本关联人员)】的数据库操作Service
* @createDate 2025-02-26 16:54:07
*/
public interface BookFamilyUsersService extends IService<BookFamilyUsers> {
    List<String> getBookFamilyUserIds(Long bookId);

    List<BookFamilyUsersDTO> getBookFamilyUsersByBookId(Long familyBookId);
}
