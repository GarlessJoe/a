package com.joe.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joe.auth.mapper.SysRoleMapper;
import com.joe.auth.service.SysRoleService;
import com.joe.auth.service.SysUserRoleService;
import com.joe.model.system.SysRole;
import com.joe.model.system.SysUserRole;
import com.joe.vo.system.AssignRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Autowired
    private SysUserRoleService service;
    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {
        //查询所有的角色，返回list集合
        List<SysRole> all = baseMapper.selectList(null);
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> ExistList = service.list(wrapper);
        //从查询出来的用户id对应角色的list集合，获取所有的角色id
        List<Long> ExistRoleIdList = ExistList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());
        ArrayList<SysRole> assignRoleList = new ArrayList<>();
        for (SysRole sysRole : all) {
          if(ExistRoleIdList.contains(sysRole.getId()))
          {
              assignRoleList.add(sysRole);
          }
        }
        HashMap<String, Object> roleMap = new HashMap<>();
        roleMap.put("assiginRoleList" , assignRoleList);
        roleMap.put("allRolesList" , all);
        return roleMap;
    }

    @Override
    public void doAssign(AssignRoleVo assignRoleVo) {
        //把用户之前分配的数据删除，通过用户角色关系表中的userid删除
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, assignRoleVo.getUserId());
        boolean remove = service.remove(wrapper);
        //重新分配
        List<Long> roleIdList = assignRoleVo.getRoleIdList();
        for (Long roleId : roleIdList) {
            if(StringUtils.isEmpty(roleId)){
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserId(assignRoleVo.getUserId());
            service.save(sysUserRole);
        }

    }
}
