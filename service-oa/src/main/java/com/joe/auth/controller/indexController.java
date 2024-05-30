package com.joe.auth.controller;

import com.joe.auth.service.SysUserService;
import com.joe.common.exception.OutException;
import com.joe.common_.MD5.MD5;
import com.joe.common_.jwt.jwtHelper;

import com.joe.common_.result.Result;
import com.joe.model.system.SysUser;
import com.joe.vo.system.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class indexController {
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        SysUser sysUser = sysUserService.getByUsername(loginVo.getUsername());
        if(null == sysUser) {
            throw new OutException(201,"用户不存在");
        }
        if(!MD5.encrypt(loginVo.getPassword()).equals(sysUser.getPassword())) {
            throw new OutException(201,"密码错误");
        }
        if(sysUser.getStatus().intValue() == 0) {
            throw new OutException(201,"用户被禁用");
        }

        Map<String, Object> map = new HashMap<>();

        map.put("token", jwtHelper.createJWT(sysUser.getId(), sysUser.getUsername()));
        return Result.ok(map);
    }
    @ApiOperation(value = "获取用户信息")
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        System.out.println("获取到用户信息");
        String username = jwtHelper.getUserName(request.getHeader("token"));
        Map<String, Object> map = sysUserService.getUserInfo(username);
        return Result.ok(map);
    }
    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }
}
