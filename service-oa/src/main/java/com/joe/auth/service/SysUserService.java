package com.joe.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.joe.model.system.SysUser;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author joe
 * @since 2024-05-26
 */
public interface SysUserService extends IService<SysUser> {
    SysUser getByUsername(String username);
    void upadteStatus(Long id, Integer status);
    int removeById(Long id);
    Map<String, Object> getUserInfo(String username);

    Map<String, Object> getCurrentUser();
}
