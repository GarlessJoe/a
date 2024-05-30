package com.joe.auth.service.impl;


import com.joe.auth.service.SysMenuService;
import com.joe.auth.service.SysUserService;
import com.joe.common.exception.OutException;
import com.joe.common_.result.ResultCode;
import com.joe.model.system.SysUser;
import com.joe.security.custom.CustomUser;
import com.joe.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class userDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysMenuService sysMenuService;
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        SysUser sysUser = userService.getByUsername(name);
        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new OutException(ResultCode.ACCOUNT_STOP);
        }
        List<String> userPermsList = sysMenuService.findUserPermsList(sysUser.getId());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String perm : userPermsList) {
            authorities.add(new SimpleGrantedAuthority(perm.trim()));
        }
        return new CustomUser(sysUser, authorities);
    }
}
