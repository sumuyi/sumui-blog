package com.sumui.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sumui.common.constants.RedisConstants;
import com.sumui.common.constants.StatusEnum;
import com.sumui.common.exception.BizException;
import com.sumui.common.model.dto.BooksDTO;
import com.sumui.common.model.finance.Books;
import com.sumui.common.model.system.SysUser;
import com.sumui.common.utils.redis.RedisUtils;
import com.sumui.dao.mapper.finance.BooksMapper;
import com.sumui.service.BookFamilyUsersService;
import com.sumui.service.BooksService;
import com.sumui.service.convert.BooksConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author flk-sunl
* @description 针对表【books(账本表)】的数据库操作Service实现
* @createDate 2025-02-21 10:21:17
*/
@Service
public class BooksServiceImpl extends ServiceImpl<BooksMapper, Books> implements BooksService{

    @Resource
    private BooksConvert booksConvert;
    @Resource
    private BookFamilyUsersService bookFamilyUsersService;

    /**
     * 关联用户到默认账本
     * @param userId 用户id
     */
    @Override
    public void addUserToBook(String userId) {
        if (StrUtil.isBlank(userId)) {
//            throw new BizException(StatusEnum.ILLEGAL_ARGUMENTS);
            return;
        }
        Books books = new Books();
        books.setId(IdUtil.getSnowflakeNextIdStr());
        books.setUserId(userId);
        books.setName("默认账本");
        books.setCreatedBy("1");
        books.setUpdatedBy("1");
        this.save(books);
    }

    /**
     * 获取用户的账本列表
     * @param userId 用户id
     * @return 账本列表
     */
    @Override
    public List<Books> getBooksByUserId(String userId) {
        if (StrUtil.isBlank(userId)) {
//            throw new BizException(StatusEnum.ILLEGAL_ARGUMENTS);
            return ListUtil.empty();
        }
        SysUser user = (SysUser) StpUtil.getSession().get(SaSession.USER);
        return this.lambdaQuery().eq(Books::getUserId, userId)
                .or(user != null && user.getFamilyBookId() != null,
                        wq -> {
                            assert user != null;
                            wq.eq(Books::getId, user.getFamilyBookId());
                        }
                ).list();
    }

    /**
     * 创建账本
     *
     * @param bookDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createBook(BooksDTO bookDTO) {
        if (StrUtil.isBlank(bookDTO.getName())) {
            throw new BizException(StatusEnum.ILLEGAL_ARGUMENTS.getCode(), "账本名称不能为空");
        }

        String userId = StpUtil.getLoginIdAsString();
        Books book = booksConvert.toBooks(bookDTO);
        book.setId(IdUtil.fastSimpleUUID());
        book.setUserId(userId);
        book.setStatus(0);
        book.setCreatedAt(new Date());
        book.setUpdatedAt(new Date());
        book.setCreatedBy(userId);
        book.setUpdatedBy(userId);

        this.save(book);

        // 如果有成员，添加成员
        if (bookDTO.getMemberIds() != null && bookDTO.getMemberIds().length > 0) {
            List<String> memberIds = new ArrayList<>();
            for (String memberId : bookDTO.getMemberIds()) {
                if (!userId.equals(memberId)) {
                    memberIds.add(memberId);
                }
            }
            if (!memberIds.isEmpty()) {
                bookFamilyUsersService.addMembers(book.getId(), memberIds, "read");
            }
        }

        // 设置为当前账本
        setDefaultBook(book.getId(), userId);

        return book.getId();
    }

    /**
     * 设置默认账本
     */
    @Override
    public Boolean setDefaultBook(String bookId, String userId) {
        if (StrUtil.isBlank(bookId)) {
            throw new BizException(StatusEnum.ILLEGAL_ARGUMENTS.getCode(), "账本ID不能为空");
        }

        if (StrUtil.isBlank(userId)) {
            userId = StpUtil.getLoginIdAsString();
        }

        // 检查账本是否存在
        Books book = this.getById(bookId);
        if (book == null) {
            throw new BizException(StatusEnum.BOOKS_NOT_FOUND.getCode(), "账本不存在");
        }

        // 检查用户是否有权限访问该账本
        boolean hasPermission = bookFamilyUsersService.checkUserBookPermission(bookId, userId, "read");
        if (!book.getUserId().equals(userId) && !hasPermission) {
            throw new BizException(StatusEnum.BOOKS_NOT_AUTHORIZED.getCode(), "无权访问该账本");
        }

        // 设置当前账本
        String key = RedisConstants.INVITE_CODE_KEY + userId + ":" + bookId;
        RedisUtils.setCacheObject(key, RedisConstants.INVITE_CODE_EXPIRE);

        return true;
    }
}




