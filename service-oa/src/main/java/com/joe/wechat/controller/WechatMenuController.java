package com.joe.wechat.controller;


import com.joe.auth.service.SysMenuService;
import com.joe.common_.result.Result;
import com.joe.model.wechat.Menu;
import com.joe.wechat.service.WechatMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author joe
 * @since 2024-06-05
 */
@RestController
@RequestMapping("/admin/wechat/menu")
public class WechatMenuController {
    @Autowired
    private WechatMenuService menuService;
    @ApiOperation(value = "获取全部菜单")
    @PreAuthorize("hasAuthority('bnt.menu.list')")
    @GetMapping("findMenuInfo")
    public Result findMenuInfo(){
        return Result.ok(menuService.findMenuInfo());

    }

    @PreAuthorize("hasAuthority('bnt.menu.list')")
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        return Result.ok(menu);
    }
    @PreAuthorize("hasAuthority('bnt.menu.add')")
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody Menu menu) {
        menuService.save(menu);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.menu.update')")
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.menu.remove')")
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        menuService.removeById(id);
        return Result.ok();
    }


}

