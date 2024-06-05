package com.joe.process.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joe.auth.service.SysUserService;
import com.joe.common_.result.Result;
import com.joe.model.process.Process;
import com.joe.model.process.ProcessTemplate;
import com.joe.model.process.ProcessType;
import com.joe.process.service.OaProcessService;
import com.joe.process.service.OaProcessTemplateService;
import com.joe.process.service.OaProcessTypeService;
import com.joe.vo.process.ApprovalVo;
import com.joe.vo.process.ProcessFormVo;
import com.joe.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "审批流管理")
@RestController
@RequestMapping(value = "/admin/process")
@CrossOrigin//解决跨域问题
public class ProcessController {
    @Autowired
    private OaProcessTypeService service;
    @Autowired
    private OaProcessTemplateService templateService;
    @Autowired
    private OaProcessService processService;
    @Autowired
    private SysUserService userService;
    @ApiOperation(value = "查询审批分类和对应的模板")
    @GetMapping("findProcessType")
    public Result findProcessType(){
        //查询所有的审批分类
        List<ProcessType> list = service.list();
        //查询对应的审批模版
        for (ProcessType processType : list) {
            processType.setProcessTemplateList( templateService.selectProcessTemplateById(processType.getId()));

        }
        return Result.ok(list);
    }
    @ApiOperation(value = "获取审批模版")
    @GetMapping("getProcessTemplate/{templateId}")
    public Result getProcessTemplate(@PathVariable Long templateId){
       return Result.ok(templateService.getById(templateId));
    }
    @ApiOperation(value = "启动流程")
    @PostMapping("/startUp")
    public Result startUp(@RequestBody ProcessFormVo processFormVo){
        processService.startUp(processFormVo);
        return Result.ok();

    }

    @ApiOperation(value = "待处理")
    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(@PathVariable Long page , @PathVariable Long limit){
        Page<Process> processPage = new Page<>(page , limit);
        IPage<ProcessVo> pageModel = processService.findPending(processPage);
        return Result.ok(pageModel);
    }
    @ApiOperation(value = "获取审批详情")
    @GetMapping("show/{id}")
    public Result show(@PathVariable Long id) {
        return Result.ok(processService.show(id));
    }
    @ApiOperation(value = "审批")
    @PostMapping("approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        processService.approve(approvalVo);
        return Result.ok();
    }
    @ApiOperation(value = "已处理")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result findProcessed(@PathVariable Long page , @PathVariable Long limit){
        Page<Process> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = processService.findProcessed(pageParam);
        return  Result.ok(pageModel);
    }
    @ApiOperation(value = "已发起")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(@PathVariable Long page ,@PathVariable Long limit){
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = processService.findStarted(pageParam);
        return Result.ok(pageModel);
    }
    @GetMapping("getCurrentUser")
    public Result getCurrentUser(){
       Map<String, Object> map = userService.getCurrentUser();
       return Result.ok(map);
    }
}
