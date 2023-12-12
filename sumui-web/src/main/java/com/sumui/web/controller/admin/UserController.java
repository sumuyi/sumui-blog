package com.sumui.web.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sumui.common.annotation.OperateLog;
import com.sumui.common.constants.OperateTypeEnum;
import com.sumui.common.model.ReqResult;
import com.sumui.common.model.system.SysUser;
import com.sumui.dao.mapper.SysUserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/")
@Tag(name = "用户管理", description = "用户数据增删改查")
public class UserController {

    @Autowired
    private SysUserMapper userMapper;

    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    @PostMapping("doLogin")
    public ReqResult<String> doLogin(String username, String password) throws Exception {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对 
        if("zhang".equals(username) && "123456".equals(password)) {
            // 会话登录：参数填写要登录的账号id，建议的数据类型：long | int | String， 不可以传入复杂类型，如：User、Admin 等等
            StpUtil.login(10001);
            return ReqResult.ok("登录成功");
        }
        return ReqResult.fail("登录失败");
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    @GetMapping("list")
    @OperateLog(title = "用户列表",businessType = OperateTypeEnum.QUERY)
    public ReqResult<List<SysUser>> getUserList(){
        return ReqResult.ok(userMapper.selectList(new LambdaQueryWrapper<>()));
    }
    
}
