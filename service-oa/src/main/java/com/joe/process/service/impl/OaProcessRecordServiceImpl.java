package com.joe.process.service.impl;

import com.joe.auth.service.SysUserService;
import com.joe.model.process.ProcessRecord;

import com.joe.model.system.SysUser;
import com.joe.process.mapper.OaProcessRecordMapper;
import com.joe.process.service.OaProcessRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joe.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批记录 服务实现类
 * </p>
 *
 * @author joe
 * @since 2024-06-04
 */
@Service
public class OaProcessRecordServiceImpl extends ServiceImpl<OaProcessRecordMapper, ProcessRecord> implements OaProcessRecordService {
    @Autowired
    private SysUserService userService;
    @Override
    public void record(Long processId, Integer status, String description) {

        ProcessRecord processRecord = new ProcessRecord();
        SysUser user = userService.getById(LoginUserInfoHelper.getUserId());
        processRecord.setProcessId(processId);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUser(user.getName());
        processRecord.setOperateUserId(LoginUserInfoHelper.getUserId());
        baseMapper.insert(processRecord);
    }
}
