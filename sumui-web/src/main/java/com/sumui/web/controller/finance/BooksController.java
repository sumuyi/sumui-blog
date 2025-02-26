package com.sumui.web.controller.finance;

import cn.dev33.satoken.stp.StpUtil;
import com.sumui.common.constants.StatusEnum;
import com.sumui.common.model.ReqResult;
import com.sumui.common.model.finance.Books;
import com.sumui.service.BooksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("账本管理")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {
    private final BooksService booksService;

    @ApiOperation("获取用户账本列表")
    @GetMapping("/user/list")
    public ReqResult<List<Books>> getUserBooksList() {
        try {
            return ReqResult.ok(booksService.getBooksByUserId(StpUtil.getLoginIdAsString()));
        } catch (Exception e) {
            return ReqResult.fail(StatusEnum.BILL_SAVE_ERROR);
        }
    }
}
