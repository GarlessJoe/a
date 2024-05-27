package com.joe.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joe.auth.mapper.SysRoleMapper;
import com.joe.auth.service.SysRoleService;
import com.joe.auth.service.SysUserRoleService;
import com.joe.model.system.SysRole;
import com.joe.vo.system.AssignRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Autowired
    private SysUserRoleService service;
    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {
        //查询所有的角色，返回list集合
        List<SysRole> all = baseMapper.selectList(null);
        LambdaQueryWrapper<Object> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        return null;
    }

    @Override
    public void doAssign(AssignRoleVo assignRoleVo) {

    }
}
