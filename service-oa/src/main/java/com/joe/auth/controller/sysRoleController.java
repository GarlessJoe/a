package com.joe.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joe.auth.service.SysRoleService;
import com.joe.common_.result.Result;
import com.joe.model.system.SysRole;
import com.joe.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/admin/system/sysRole")
@Api(tags = "角色管理系统")
public class sysRoleController {
    @Autowired
    private SysRoleService srs; //统一返回数据格式
    @GetMapping("/findAll")
    @ApiOperation("查找所有用户")
    public Result findAll(){
        List<SysRole> list = srs.list(null);
        return Result.ok(list);
    }
}
