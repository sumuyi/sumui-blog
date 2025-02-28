package com.sumui.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sumui.common.constants.StatusEnum;
import com.sumui.common.exception.BizException;
import com.sumui.common.model.finance.Books;
import com.sumui.common.model.system.SysUser;
import com.sumui.common.utils.uuid.IDUtils;
import com.sumui.dao.mapper.finance.BooksMapper;
import com.sumui.service.BooksService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author flk-sunl
* @description 针对表【books(账本表)】的数据库操作Service实现
* @createDate 2025-02-21 10:21:17
*/
@Service
public class BooksServiceImpl extends ServiceImpl<BooksMapper, Books> implements BooksService{

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
}




