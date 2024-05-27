package com.joe.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.joe.auth.service.SysRoleMenuService;
import com.joe.auth.utils.MenuHelper;
import com.joe.auth.mapper.SysMenuMapper;
import com.joe.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joe.auth.utils.MenuHelper;
import com.joe.common.exception.OutException;
import com.joe.model.system.SysMenu;
import com.joe.model.system.SysRoleMenu;
import com.joe.vo.system.AssignMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author joe
 * @since 2024-05-27
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysRoleMenuService service;
    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        //构建树形结构
        List<SysMenu> sysMenus = MenuHelper.buildTree(sysMenuList);
        return sysMenus;
    }

    @Override
    public void removeMenuById(Long id) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId , id);
        Integer i = baseMapper.selectCount(wrapper);
        if(i>0){
            throw new OutException(201 , "菜单不删除");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getStatus , 1);
        List<SysMenu> allSysMenus = baseMapper.selectList(wrapper);
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuWrapper.eq(SysRoleMenu::getRoleId , roleId);
        List<SysRoleMenu> SysRoleMenulist = service.list(sysRoleMenuWrapper);
        List<Long> MenuidList = SysRoleMenulist.stream().map(c -> c.getMenuId()).collect(Collectors.toList());
        for (SysMenu menu : allSysMenus) {
            if(MenuidList.contains(menu.getId())){
                menu.setSelect(true);
            }else {
                menu.setSelect(false);
            }
        }
        List<SysMenu> sysMenus = MenuHelper.buildTree(allSysMenus);
        return sysMenus;
    }

    @Override
    public void doAssign(AssignMenuVo assignMenuVo) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId , assignMenuVo.getRoleId());
        service.remove(wrapper);
        List<Long> menuIdList = assignMenuVo.getMenuIdList();
        for (Long l : menuIdList) {
            if(StringUtils.isEmpty(l)){
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(l);
            sysRoleMenu.setRoleId(assignMenuVo.getRoleId());
            service.save(sysRoleMenu);
        }
    }
}
