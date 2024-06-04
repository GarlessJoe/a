package com.joe.process.controller;

import com.joe.common_.result.Result;
import com.joe.model.process.ProcessType;
import com.joe.process.service.OaProcessTemplateService;
import com.joe.process.service.OaProcessTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "审批流管理")
@RestController
@RequestMapping(value = "/admin/process")
@CrossOrigin//解决跨域问题
public class ProcessController {
    @Autowired
    private OaProcessTypeService service;
    @Autowired
    private OaProcessTemplateService templateService;
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
}
