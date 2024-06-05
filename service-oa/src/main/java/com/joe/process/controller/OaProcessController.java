package com.joe.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joe.common_.result.Result;
import com.joe.model.process.Process;
import com.joe.process.service.OaProcessService;
import com.joe.vo.process.ProcessQueryVo;
import com.joe.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审批类型 前端控制器
 * </p>
 *
 * @author joe
 * @since 2024-06-03
 */
@Api(tags = "process")
@RestController
@RequestMapping(value = "/admin/process/")
@CrossOrigin  //跨域
public class OaProcessController {
    @Autowired
    private OaProcessService service;
    @PreAuthorize("hasAuthority('bnt.process.list')")
    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @PathVariable Long page,
            @PathVariable Long limit,
            ProcessQueryVo processQueryVo) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = service.selectPage(pageParam, processQueryVo);
        return Result.ok(pageModel);
    }

}

