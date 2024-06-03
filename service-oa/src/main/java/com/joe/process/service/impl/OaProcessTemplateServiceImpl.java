package com.joe.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joe.model.process.ProcessTemplate;

import com.joe.model.process.ProcessType;
import com.joe.process.mapper.OaProcessTemplateMapper;
import com.joe.process.service.OaProcessTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joe.process.service.OaProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author joe
 * @since 2024-06-03
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {
@Autowired
private OaProcessTypeService service;
    @Override
    public IPage<ProcessTemplate> selectProcessTemplate(Page<ProcessTemplate> page) {
        Page<ProcessTemplate> processTemplatePage = baseMapper.selectPage(page, null);
        List<ProcessTemplate> records = page.getRecords();
        for (ProcessTemplate record : records) {
            Long processTypeId = record.getProcessTypeId();
            LambdaQueryWrapper<ProcessType> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessType::getId , processTypeId);
            ProcessType processType = service.getOne(wrapper);
            if(processType == null){
                continue;
            }else{
                record.setProcessTypeName(processType.getName());
            }
        }
        return  processTemplatePage;
    }
}
