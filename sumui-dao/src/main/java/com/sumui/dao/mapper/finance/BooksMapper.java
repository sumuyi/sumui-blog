package com.sumui.dao.mapper.finance;

import com.sumui.common.model.dto.BooksDTO;
import com.sumui.common.model.finance.Books;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author flk-sunl
* @description 针对表【books(账本表)】的数据库操作Mapper
* @createDate 2025-02-21 10:21:17
* @Entity com.sumui.common.model.finance.Books
*/
public interface BooksMapper extends BaseMapper<Books> {
    List<Books> getUserBooks(@Param("userId") String userId);

    Integer getBookMemberCount(@Param("bookId") String bookId);

    List<BooksDTO> selectBooksDTOByUserId(@Param("userId") String userId, @Param("familyBookId") Long familyBookId);
}




