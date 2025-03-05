package com.sumui.web.controller.admin.system;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sumui.common.model.ReqResult;
import com.sumui.common.model.security.RegisterUserInfo;
import com.sumui.common.model.system.SysUser;
import com.sumui.dao.mapper.system.SysUserMapper;
import com.sumui.service.impl.system.SysUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author sumui
 * @since 2023-12-23 11:16:53
 */
@RestController
@RequestMapping("/sumui/user")
public class SysUserController {
    @Autowired
    private SysUserMapper userMapper;

    @Resource
    private SysUserService userService;

    @PostMapping("add")
    public ReqResult<Boolean> addUser(@RequestBody RegisterUserInfo registerUserInfo) throws Exception {
        return ReqResult.ok(userService.registerUserInfo(registerUserInfo));
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    @SaIgnore
    @GetMapping("list")
//    @OperateLog(title = "用户列表",businessType = OperateTypeEnum.QUERY)
    public ReqResult<List<SysUser>> getUserList(){
//        logService.getUUID();
        return ReqResult.ok(userMapper.selectList(new LambdaQueryWrapper<>()));
//        throw new ApiException("测试日志记录请求失败的场景");
    }

    @ApiOperation("修改用户昵称")
    @PostMapping("update/nickname")
    public ReqResult<Boolean> addUser(@RequestBody SysUser user) throws Exception {
        return ReqResult.ok(userService.updateUserNickName(user));
    }
}
