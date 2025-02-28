package com.sumui.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sumui.common.model.dto.BookFamilyUsersDTO;
import com.sumui.common.model.finance.BookFamilyUsers;
import com.sumui.common.model.finance.Books;
import com.sumui.service.BookFamilyUsersService;
import com.sumui.dao.mapper.finance.BookFamilyUsersMapper;
import com.sumui.service.BooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author flk-sunl
* @description 针对表【book_family_users(家庭账本关联人员)】的数据库操作Service实现
* @createDate 2025-02-26 16:54:07
*/
@Service
@RequiredArgsConstructor
public class BookFamilyUsersServiceImpl extends ServiceImpl<BookFamilyUsersMapper, BookFamilyUsers> implements BookFamilyUsersService{

    private final BooksService booksService;

    /**
     * 获取家庭账本的用户id
     *
     * @param bookId 账本id
     * @return 用户id
     */
    @Override
    public List<String> getBookFamilyUserIds(Long bookId) {
        if (bookId == null) {
            return ListUtil.empty();
        }
        Books book = booksService.getById(bookId);
        if (book == null || !book.getType().equals("family")) {
            return ListUtil.empty();
        }
        return this.lambdaQuery().eq(BookFamilyUsers::getBookId, bookId).list()
                .stream().map(BookFamilyUsers::getUserId).collect(Collectors.toList());
    }

    /**
     * 获取用户关联的家庭账本
     * @param familyBookId 家庭账本id
     * @return 家庭账本
     */
    @Override
    public List<BookFamilyUsersDTO> getBookFamilyUsersByBookId(Long familyBookId) {
        if (familyBookId == null) {
            return ListUtil.empty();
        }
        return this.baseMapper.selectFamilyUsersByBookId(familyBookId);
    }
}




