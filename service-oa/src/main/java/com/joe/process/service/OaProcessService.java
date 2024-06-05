package com.joe.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joe.model.process.Process;
import com.joe.vo.process.ApprovalVo;
import com.joe.vo.process.ProcessFormVo;
import com.joe.vo.process.ProcessQueryVo;
import com.joe.vo.process.ProcessVo;

import java.util.Map;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author joe
 * @since 2024-06-03
 */
public interface OaProcessService extends IService<Process> {

    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);
    void deployByZip(String deployPath);

    void startUp(ProcessFormVo processFormVo);

    IPage<ProcessVo> findPending(Page<Process> processPage);

    Map<String, Object> show(Long id);

    void approve(ApprovalVo approvalVo);

    IPage<ProcessVo> findProcessed(Page<Process> pageParam);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);
}
