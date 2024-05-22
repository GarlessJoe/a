package com.joe.auth;

import com.joe.auth.mapper.SysRoleMapper;
import com.joe.auth.service.SysRoleService;
import com.joe.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
public class testmpDemo1 {
    @Autowired
    private SysRoleMapper mapper;

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
}
