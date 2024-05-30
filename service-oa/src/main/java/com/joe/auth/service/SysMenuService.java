package com.joe.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.joe.model.system.SysMenu;
import com.joe.vo.system.AssignMenuVo;
import com.joe.vo.system.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author joe
 * @since 2024-05-27
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();
    List<RouterVo> findUserMenuList(Long userId);
    void removeMenuById(Long id);

    List<SysMenu> findMenuByRoleId(Long roleId);
    List<String> findUserPermsList(Long userId);
    void doAssign(AssignMenuVo assignMenuVo);
}
