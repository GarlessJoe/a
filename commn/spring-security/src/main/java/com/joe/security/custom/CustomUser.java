package com.joe.security.custom;

import com.joe.model.system.SysUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class CustomUser extends User {
    private SysUser sysUser;
    public CustomUser(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {
        super(sysUser.getUsername(), sysUser.getPassword(), authorities);
        this.sysUser = sysUser;
    }
    public SysUser getSysUser(){
        return sysUser;
    }
    public void setSysUser(SysUser user){
        this.sysUser = user;
    }

}
