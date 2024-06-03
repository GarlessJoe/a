package com.joe.process.service.impl;

import com.joe.model.process.ProcessType;
import com.joe.process.mapper.OaProcessTypeMapper;
import com.joe.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author joe
 * @since 2024-06-03
 */
@Service
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {

}
