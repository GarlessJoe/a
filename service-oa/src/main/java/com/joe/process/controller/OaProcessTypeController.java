package com.joe.process.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joe.common_.result.Result;
import com.joe.model.process.ProcessType;
import com.joe.process.service.OaProcessTemplateService;
import com.joe.process.service.OaProcessTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 审批类型 前端控制器
 * </p>
 *
 * @author joe
 * @since 2024-06-03
 */
@RestController
@RequestMapping("/admin/process/processType")
@SuppressWarnings({"unchecked", "rawtypes"})
@Api(value = "审批类型", tags = "审批类型")
public class OaProcessTypeController {
    @Autowired
    private OaProcessTypeService service;


    @ApiOperation(value = "添加")
    @PostMapping("add")
    @PreAuthorize("hasAuthority('bnt.processType.add')")
    public Result add(@RequestBody ProcessType type){
        boolean save = service.save(type);
        return Result.ok();
    }
    @PreAuthorize("hasAuthority('bnt.processType.remove')")
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean b = service.removeById(id);
        if(b) return Result.ok();
        else return Result.fail();
    }
    @ApiOperation(value = "查询所有的审批类型")
    @GetMapping("findAll")
    public Result findAll(){
        List<ProcessType> list = service.list();
        return Result.ok(list);
    }
    @ApiOperation(value = "修改")
    @PreAuthorize("hasAuthority('bnt.processType.update')")
    @PutMapping("update")
    public Result update(@RequestBody ProcessType processType){
        boolean b = service.updateById(processType);
        if(b) return Result.ok();
        else return Result.fail();
    }
    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @ApiOperation(value = "分页列表")
    @GetMapping("{page}/{limit}")
    public Result getList(@PathVariable Long page , @PathVariable Long limit ){
        Page<ProcessType> processTypePage = new Page<>(page, limit);
        IPage<ProcessType> pageModel = service.page(processTypePage);
        return Result.ok(pageModel);
    }
    @ApiOperation(value = "查找")
    @GetMapping("get/{id}")
    @PreAuthorize("hasAuthority('bnt.processType.list')")
    public Result get(@PathVariable Long id){
        ProcessType processType = service.getById(id);
        return Result.ok(processType);
    }
}

