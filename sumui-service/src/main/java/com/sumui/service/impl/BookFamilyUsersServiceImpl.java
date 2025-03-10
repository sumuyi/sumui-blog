package com.sumui.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sumui.common.constants.StatusEnum;
import com.sumui.common.exception.BizException;
import com.sumui.common.model.dto.BookFamilyUsersDTO;
import com.sumui.common.model.finance.BookFamilyUsers;
import com.sumui.common.model.finance.Books;
import com.sumui.service.BookFamilyUsersService;
import com.sumui.dao.mapper.finance.BookFamilyUsersMapper;
import com.sumui.service.BooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author flk-sunl
* @description 针对表【book_family_users(家庭账本关联人员)】的数据库操作Service实现
* @createDate 2025-02-26 16:54:07
*/
@Service
public class BookFamilyUsersServiceImpl extends ServiceImpl<BookFamilyUsersMapper, BookFamilyUsers> implements BookFamilyUsersService{

    @Resource
    private BooksService booksService;

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

    /**
     * @param bookId     账本id
     * @param memberIds  账本成员id
     * @param permission 权限
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addMembers(String bookId, List<String> memberIds, String permission) {
        if (StrUtil.isBlank(bookId) || memberIds == null || memberIds.isEmpty()) {
            throw new BizException(StatusEnum.ILLEGAL_ARGUMENTS.getCode(), "账本ID和用户ID不能为空");
        }

        String userId = StpUtil.getLoginIdAsString();

        // 检查账本是否存在
        Books book = booksService.getById(bookId);
        if (book == null) {
            throw new BizException(StatusEnum.BOOKS_NOT_FOUND.getCode(), "账本不存在");
        }

        // 检查是否是账本创建者
        if (!book.getUserId().equals(userId)) {
            throw new BizException(StatusEnum.BOOKS_MEMBERS_UNAUTHORIZED.getCode(), "只有账本创建者才能添加成员");
        }

        List<BookFamilyUsers> members = new ArrayList<>();
        for (String memberId : memberIds) {
            // 跳过创建者自己
            if (memberId.equals(userId)) {
                continue;
            }

            // 检查成员是否已存在
            LambdaQueryWrapper<BookFamilyUsers> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BookFamilyUsers::getBookId, bookId)
                    .eq(BookFamilyUsers::getUserId, memberId);
            BookFamilyUsers existMember = this.getOne(queryWrapper);

            if (existMember != null) {
                throw new BizException(StatusEnum.BOOKS_MEMBERS_EXISTS.getCode(), "成员已存在");
            }

            // 添加成员
            BookFamilyUsers member = new BookFamilyUsers();
            member.setId(IdUtil.fastSimpleUUID());
            member.setBookId(bookId);
            member.setUserId(memberId);
            member.setPermission(permission);
            member.setCreateBy(userId);

            members.add(member);
        }

        if (!members.isEmpty()) {
            return this.saveBatch(members);
        }
        return true;
    }

    /**
     * 检查用户是否有账本权限
     */
    @Override
    public Boolean checkUserBookPermission(String bookId, String userId, String permission) {
        if (StrUtil.isBlank(bookId) || StrUtil.isBlank(userId)) {
            return false;
        }

        // 检查账本是否存在
        Books book = booksService.getById(bookId);
        if (book == null) {
            return false;
        }

        // 如果是账本创建者，直接返回true
        if (book.getUserId().equals(userId)) {
            return true;
        }

        // 检查是否是账本成员
        Integer count = baseMapper.checkUserBookPermission(bookId, userId, permission);
        return count != null && count > 0;
    }
}




