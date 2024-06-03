package com.joe.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.joe.auth.mapper.SysRoleMapper;
import com.joe.auth.service.SysRoleService;
import com.joe.auth.service.SysUserService;
import com.joe.model.system.SysRole;
import com.joe.model.system.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
public class testmpDemo1 {
    @Autowired
    private SysRoleMapper mapper;
    @Autowired
    SysUserService service;
    @Test
    public void getAll(){
        List<SysRole> sysRoles = mapper.selectList(null);
        sysRoles.forEach(System.out::println);
    }
    @Test
    public void getAll2(){

    }
    @Test
    public void insert(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("lisi");
        sysRole.setRoleCode("1");
        sysRole.setDescription("great");
        int insert = mapper.insert(sysRole);
        System.out.println(insert);
    }
    @Test
    public void testGetOne(){
        String name = "lisi";

        System.out.println( service.getByUsername(name));
    }
}
