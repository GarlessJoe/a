package com.joe.auth.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joe.auth.service.SysRoleService;
import com.joe.common_.result.Result;
import com.joe.model.system.SysRole;
import com.joe.vo.system.AssignRoleVo;
import com.joe.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/system/sysRole")
@Api(tags = "角色管理系统")
public class sysRoleController {
    @Autowired
    private SysRoleService srs; //统一返回数据格式
    //查询所有的角色

    @ApiOperation("获取角色")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId){
        Map<String , Object> map = srs.findRoleDataByUserId(userId);
        return Result.ok(map);

    }
    //为用户分配角色
    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    @ApiOperation("为用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssignRoleVo assignRoleVo){
        srs.doAssign(assignRoleVo);
        return Result.ok();
    }
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("/findAll")
    @ApiOperation("查找所有用户")
    public Result findAll(){
        List<SysRole> list = srs.list(null);
        return Result.ok(list);
    }
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysRoleQueryVo sysRoleQueryVo) {
        //调用service的方法实现
        //1 创建Page对象，传递分页相关参数
        //page 当前页  limit 每页显示记录数
        Page<SysRole> pageParam = new Page<>(page,limit);

        //2 封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if(!StringUtils.isEmpty(roleName)) {
            //封装 like模糊查询
            wrapper.like(SysRole::getRoleName,roleName);
        }

        //3 调用方法实现
        IPage<SysRole> pageModel = srs.page(pageParam, wrapper);
        return Result.ok(pageModel);

    }
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("添加角色")
    @PostMapping("/save")
    public Result add(@RequestBody SysRole role){
        boolean isSuccess = srs.save(role);
        if(isSuccess){
            return Result.ok();
        }
        else{
            return Result.fail();
        }
    }
    @ApiOperation("根据id查询")
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        SysRole role = srs.getById(id);
        return Result.ok(role);
    }
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("修改角色")
    @PutMapping("/update")
    public Result update(@RequestBody SysRole role){
        boolean isSuccess = srs.updateById(role);

        if(isSuccess){
            return Result.ok();
        }
        else{
            return Result.fail();
        }
    }
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("根据id删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){

        boolean isSuccess = srs.removeById(id);
        if(isSuccess){
            return Result.ok();
        }
        else{
            return Result.fail();
        }
    }
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Integer> ids){
        boolean isSuccess = srs.removeByIds(ids);
        if(isSuccess){
            return Result.ok();
        }
        else{
            return Result.fail();
        }
    }
}
