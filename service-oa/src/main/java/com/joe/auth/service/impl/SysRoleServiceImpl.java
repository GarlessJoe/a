package com.joe.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joe.auth.mapper.SysRoleMapper;
import com.joe.auth.service.SysRoleService;
import com.joe.model.system.SysRole;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper , SysRole> implements SysRoleService {

}
