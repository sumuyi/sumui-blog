package com.sumui.dao.mapper.finance;

import com.sumui.common.model.dto.BookFamilyUsersDTO;
import com.sumui.common.model.finance.BookFamilyUsers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author flk-sunl
* @description 针对表【book_family_users(家庭账本关联人员)】的数据库操作Mapper
* @createDate 2025-02-26 16:54:07
* @Entity com.sumui.common.model.finance.BookFamilyUsers
*/
public interface BookFamilyUsersMapper extends BaseMapper<BookFamilyUsers> {

    List<BookFamilyUsersDTO> selectFamilyUsersByBookId(@Param("bookId") Long bookId);

    Integer checkUserBookPermission(String bookId, String userId, String permission);
}




