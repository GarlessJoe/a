package com.joe.auth.controller;

import com.joe.common_.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class indexController {
    //login
    @PostMapping("login")
    public Result login(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("token" , "admin-token");
        return Result.ok(map);
    }
    @GetMapping("info")
    public Result info(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("roles" , "[admin]");
        map.put("name" , "admin");
        map.put("avatar" , "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        return Result.ok(map);
    }
    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }
}
