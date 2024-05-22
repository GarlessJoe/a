package com.joe.auth;

import com.joe.auth.service.SysRoleService;
import com.joe.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SpringbootTest {
  @Autowired
  private SysRoleService sysRoleService;
    @Test
    public void GetAll2(){
        List<SysRole> list = sysRoleService.list();
    }

}
