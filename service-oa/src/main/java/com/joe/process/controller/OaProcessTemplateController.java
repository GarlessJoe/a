package com.joe.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joe.common_.result.Result;
import com.joe.model.process.ProcessTemplate;
import com.joe.process.service.OaProcessTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审批模板 前端控制器
 * </p>
 *
 * @author joe
 * @since 2024-06-03
 */
@RestController
@Api(value = "审批模板管理", tags = "审批模板管理")
@RequestMapping(value = "/admin/process/processTemplate")
@SuppressWarnings({"unchecked", "rawtypes"})
public class OaProcessTemplateController {
    @Autowired
    private OaProcessTemplateService service;
    @ApiOperation(value = "获取分页提交数据")
    @GetMapping("{page}/{limit}")
    public Result index(
            @PathVariable Long page ,
             @PathVariable Long limit
    )
    {
        Page<ProcessTemplate> processTemplatePage = new Page<>(page, limit);
        IPage<ProcessTemplate> pageModel = service.selectProcessTemplate(processTemplatePage);
        return Result.ok(pageModel);
    }
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        ProcessTemplate processTemplate = service.getById(id);
        return Result.ok(processTemplate);
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody ProcessTemplate processTemplate) {
        service.save(processTemplate);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody ProcessTemplate processTemplate) {
        service.updateById(processTemplate);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.remove')")
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        service.removeById(id);
        return Result.ok();
    }

}

