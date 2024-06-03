package com.joe.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joe.auth.service.SysUserService;
import com.joe.common_.MD5.MD5;
import com.joe.common_.result.Result;
import com.joe.model.system.SysRole;
import com.joe.model.system.SysUser;
import com.joe.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author joe
 * @since 2024-05-26
 */
@Api(tags = "用户管理系统")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService service;
    @ApiOperation("用户提交分页查询")
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page , @PathVariable Long limit , SysUserQueryVo sysUserQueryVo){
        //创建page对象
        Page<SysUser> pages = new Page<>(page, limit);
        //封装对象 ， 判断条件不为空
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        String username= sysUserQueryVo.getKeyword();
        String begin= sysUserQueryVo.getCreateTimeBegin();
        String end = sysUserQueryVo.getCreateTimeEnd();
        if(!StringUtils.isEmpty(username)){
            queryWrapper.like(SysUser::getUsername , username);

        }
        //ge大于等于
        if(!StringUtils.isEmpty(begin)){
            queryWrapper.ge(SysUser::getCreateTime , begin);
        }
        //le小于等于
        if(!StringUtils.isEmpty(end)){
            queryWrapper.le(SysUser::getCreateTime , end);
        }
        //调用map方法实现条件分页查询
        IPage<SysUser> pagemodel = service.page(pages, queryWrapper);
        return Result.ok(pagemodel);
    }
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @ApiOperation(value = "获取用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = service.getById(id);
        return Result.ok(user);
    }
    @PreAuthorize("hasAuthority('bnt.sysUser.add')")
    @ApiOperation(value = "保存用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser user) {
        user.setPassword(MD5.encrypt(user.getPassword()));
        service.save(user);
        return Result.ok();
    }
    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @ApiOperation(value = "更新用户")
    @PutMapping("update")
    public Result updateById(@RequestBody SysUser user) {
        service.updateById(user);
        return Result.ok();
    }
    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @ApiOperation(value = "删除用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        service.removeById(id);
        return Result.ok();
    }
    @ApiOperation("用户状态更改")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id , @PathVariable Integer status)
    {
        service.upadteStatus(id ,status);
        return Result.ok();
    }
}

