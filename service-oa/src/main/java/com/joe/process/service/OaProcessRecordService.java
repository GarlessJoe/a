package com.joe.process.service;

import com.joe.model.process.ProcessRecord;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批记录 服务类
 * </p>
 *
 * @author joe
 * @since 2024-06-04
 */
public interface OaProcessRecordService extends IService<ProcessRecord> {
void record(Long processId ,Integer status , String description);
}
