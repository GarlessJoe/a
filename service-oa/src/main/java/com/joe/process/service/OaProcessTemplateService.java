package com.joe.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joe.model.process.ProcessTemplate;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 审批模板 服务类
 * </p>
 *
 * @author joe
 * @since 2024-06-03
 */
public interface OaProcessTemplateService extends IService<ProcessTemplate> {

    IPage<ProcessTemplate> selectProcessTemplate(Page<ProcessTemplate> processTemplatePage);

    void publish(Long id);

    List<ProcessTemplate> selectProcessTemplateById(Long id);
}
