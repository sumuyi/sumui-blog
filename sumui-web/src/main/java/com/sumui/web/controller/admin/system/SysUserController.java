package com.sumui.web.controller.admin.system;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sumui.common.annotation.OperateLog;
import com.sumui.common.constants.OperateTypeEnum;
import com.sumui.common.model.ReqResult;
import com.sumui.common.model.security.LoginBody;
import com.sumui.common.model.security.RegisterUserInfo;
import com.sumui.common.model.system.SysUser;
import com.sumui.dao.mapper.system.SysUserMapper;
import com.sumui.service.service.system.SysOperLogService;
import com.sumui.service.service.system.SysUserService;
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
    public ReqResult<String> addUser(@RequestBody RegisterUserInfo registerUserInfo) throws Exception {
        return ReqResult.ok(userService.registerUserInfo(registerUserInfo));
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    @GetMapping("list")
    @OperateLog(title = "用户列表",businessType = OperateTypeEnum.QUERY)
    public ReqResult<List<SysUser>> getUserList(){
        logService.getUUID();
        return ReqResult.ok(userMapper.selectList(new LambdaQueryWrapper<>()));
//        throw new ApiException("测试日志记录请求失败的场景");
    }
}
