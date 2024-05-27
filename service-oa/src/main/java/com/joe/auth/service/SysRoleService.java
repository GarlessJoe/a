package com.joe.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joe.model.system.SysRole;
import com.joe.vo.system.AssignRoleVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    Map<String, Object> findRoleDataByUserId(Long userId);

    void doAssign(AssignRoleVo assignRoleVo);
}
